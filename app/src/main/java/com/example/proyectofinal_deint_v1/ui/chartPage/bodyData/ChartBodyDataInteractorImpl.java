package com.example.proyectofinal_deint_v1.ui.chartPage.bodyData;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.Exercise;
import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.bodyData.BodyData;
import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.bodyData.Measurement;
import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.workData.WorkData;
import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.workData.serie.Serie;
import com.example.proyectofinal_deint_v1.ui.utils.CommonUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChartBodyDataInteractorImpl {

    private ChartBodyDataInteractorImpl.ChartBodyDataInteractor callback;

    public ChartBodyDataInteractorImpl(ChartBodyDataInteractor callback) {
        this.callback = callback;
    }

    public interface ChartBodyDataInteractor{
        //WorkData
        void onEmptyRepositoryWorkDataError();
        void onFireBaseConnectionError();
        void onSuccessBodyData(List<BodyData> bodyData);
        void onSuccesBodyDataList(Context context, String userUID ,List<BodyData> bodyData);
    }

    public void getList_ByDate(Context context, String cInit, String cFin){
        if(CommonUtils.isCoachUser(context)){
            listRequest(context, cInit, cFin);
            return;
        }
        getListWebServ_ByDate(context, FirebaseAuth.getInstance().getCurrentUser().getUid(),cInit,cFin);
    }

    public void listRequest(Context context, String cInit, String cFin){
        String URL = "http://vps-3c722567.vps.ovh.net/GainsLog/crud/firebase/listRequest.php";
        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    try {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            com.example.proyectofinal_deint_v1.data.model.model.user.Request tmp = new Gson().fromJson(jsonObject.toString(), com.example.proyectofinal_deint_v1.data.model.model.user.Request.class);
                            //Llamar al método para obtener el listado de músculos principales. -->
                            if(tmp.get_accepted() == 1){
                                getListWebServ_ByDate(context,tmp.getFb_id_user(),cInit,cFin);
                            }
                        }
                    } catch (JSONException ex) {
                        ex.printStackTrace();
                    }
                } catch (JSONException exception) {
                    exception.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }

        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String,String>();
                params.put("fb_id",FirebaseAuth.getInstance().getCurrentUser().getUid());
                return params;
            }
        };
        Volley.newRequestQueue(context).add(request);

    }

    private void getListWebServ_ByDate(Context context,String userUID ,String cInit, String cFin){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        List<BodyData> bodyDataList = new ArrayList<>();
        String URL = "http://vps-3c722567.vps.ovh.net/GainsLog/crud/bodyData/listar_date.php";
        StringRequest request1 = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    if(jsonArray.length() <= 0){callback.onEmptyRepositoryWorkDataError();}
                    try {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            BodyData tmp = new BodyData();
                            tmp.setId(jsonObject.getInt("id"));
                            tmp.setWeight(jsonObject.getDouble("weight"));
                            tmp.setFatPer(jsonObject.getDouble("fatPer"));
                            tmp.setNote(jsonObject.getString("note"));
                            tmp.setLogDate(CommonUtils.getDateFromStringTimeStamp(jsonObject.getString("logDate")));
                            bodyDataList.add(tmp);
                        }
                        callback.onSuccesBodyDataList(context,userUID,bodyDataList);
                    } catch (JSONException ex) {
                        ex.printStackTrace();
                    }
                } catch (JSONException exception) {
                    exception.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }

        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", userUID);
                params.put("init", cInit);
                params.put("fin", cFin);
                return params;
            }
        };
        requestQueue.add(request1);
    }

    public void getListMeasure(Context context, String userUID, List<BodyData> bodyDataList,int position){
        List<Measurement> measurements = new ArrayList<>();
        String URL2 = "http://vps-3c722567.vps.ovh.net/GainsLog/crud/bodyData/measure/list.php";
        StringRequest request2 = new StringRequest(Request.Method.POST, URL2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    try {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            Measurement tmp = new Gson().fromJson(jsonObject.toString(), Measurement.class);
                            measurements.add(tmp);
                        }
                        bodyDataList.get(position).setMeasurements(measurements);
                        if(position == bodyDataList.size()-1){
                            callback.onSuccessBodyData(bodyDataList);
                        }
                    } catch (JSONException ex) {
                        ex.printStackTrace();
                    }
                } catch (JSONException exception) {
                    exception.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }

        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", userUID);
                params.put("body", String.valueOf(bodyDataList.get(position).getId()));
                return params;
            }
        };
        Volley.newRequestQueue(context).add(request2);
    }

}
