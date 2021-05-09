package com.example.proyectofinal_deint_v1.ui.chartPage.workData;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.workData.WorkData;
import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.workData.serie.Serie;
import com.example.proyectofinal_deint_v1.ui.homePage.HomeFragmentInteractorImpl;
import com.example.proyectofinal_deint_v1.ui.utils.CommonUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChartWorkDataInteractorImpl {


    private ChartWorkDataInteractorImpl.ChartWorkDataInteractor callback;

    public ChartWorkDataInteractorImpl(ChartWorkDataInteractor callback) {
        this.callback = callback;
    }

    public interface ChartWorkDataInteractor{
        //WorkData
        void onEmptyRepositoryWorkDataError();
        void onFireBaseConnectionError();
        void onSuccessWorkData(List<WorkData> workData);
        void onSuccesWorkDataList(Context context, String userUID ,List<WorkData> workDataList);
    }

    public void getList_ByDate(Context context, String cInit, String cFin){
        getListWebServ_ByDate(context, FirebaseAuth.getInstance().getCurrentUser().getUid(),cInit,cFin);
    }

    private void getListWebServ_ByDate(Context context,String userUID ,String cInit, String cFin){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        List<WorkData> workDataList = new ArrayList<>();
        String URL = "http://vps-3c722567.vps.ovh.net/GainsLog/crud/workData/listar_date.php";
        StringRequest request1 = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    try {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            WorkData tmp = new WorkData();
                            tmp.setId(jsonObject.getInt("id"));
                            tmp.setNameExercise(jsonObject.getString("nameExercise"));
                            tmp.setIdExercise(jsonObject.getInt("idExercise"));
                            tmp.setLogDate(CommonUtils.getDateFromStringTimeStamp(jsonObject.getString("logDate")));
                            //Añadir además el listado de series al workData;
                            workDataList.add(tmp);
                        }
                        callback.onSuccesWorkDataList(context,userUID,workDataList);
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

    public void getListSerie(Context context, String userUID, List<WorkData> workDataList,int position){
        List<Serie> serieList = new ArrayList<>();
        String URL2 = "http://vps-3c722567.vps.ovh.net/GainsLog/crud/serie/listar.php";
        StringRequest request2 = new StringRequest(Request.Method.POST, URL2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    try {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            Serie tmp = new Gson().fromJson(jsonObject.toString(), Serie.class);
                            serieList.add(tmp);
                        }
                        workDataList.get(position).setSerieList(serieList);
                        if(position == workDataList.size()-1){
                            callback.onSuccessWorkData(workDataList);
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
                params.put("userId", userUID);
                params.put("workId", String.valueOf(workDataList.get(position).getId()));
                return params;
            }
        };
        Volley.newRequestQueue(context).add(request2);
    }

}
