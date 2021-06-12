package com.example.proyectofinal_deint_v1.ui.chartPage.target;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.bodyData.BodyData;
import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.bodyData.Measurement;
import com.example.proyectofinal_deint_v1.data.model.model.target.Target;
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

public class ChartTargetInteractorImpl {

    private ChartTargetInteractorImpl.ChartTargetInteractor callback;

    public ChartTargetInteractorImpl(ChartTargetInteractor callback) {
        this.callback = callback;
    }

    public interface ChartTargetInteractor{
        void setEmptyRepositoryTargetError();
        void setFireBaseConnectionError();
        void onSuccessTarget(List<Target> targets);
    }

    public void getList_ByDate(Context context, String cFin){
        if(CommonUtils.isCoachUser(context)){
            listRequest(context,cFin);
            return;
        }
        getRepositoryTarget_ByDate(context, FirebaseAuth.getInstance().getCurrentUser().getUid(),cFin);
    }

    public void listRequest(Context context, String cFin){
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
                                getRepositoryTarget_ByDate(context,tmp.getFb_id_user(),cFin);
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

    private void getRepositoryTarget_ByDate(Context context,String userUID, String cFin){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        List<Target> targetList = new ArrayList<>();
        String URL = "http://vps-3c722567.vps.ovh.net/GainsLog/crud/target/listar_date.php";
        StringRequest request1 = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    try {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            Target tmp = new Target();
                            tmp.setIdUser(jsonObject.getInt("idUser"));
                            tmp.setOvercome(jsonObject.getString("overcome"));
                            tmp.setNameTarget(jsonObject.getString("name"));
                            tmp.setDescription(jsonObject.getString("description"));
                            tmp.setId(jsonObject.getInt("id"));
                            tmp.setExpirationDate(CommonUtils.getDateFromStringTimeStamp(jsonObject.getString("expirationDate")));
                            targetList.add(tmp);
                        }
                        callback.onSuccessTarget(targetList);
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
                params.put("fin", cFin);
                return params;
            }
        };
        requestQueue.add(request1);
    }

}
