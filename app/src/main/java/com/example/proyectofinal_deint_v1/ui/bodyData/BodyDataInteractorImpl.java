package com.example.proyectofinal_deint_v1.ui.bodyData;

import android.content.Context;
import android.icu.util.Measure;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.bodyData.BodyData;
import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.bodyData.Measurement;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class BodyDataInteractorImpl {

    private BodyDataInteractor callback;

    public BodyDataInteractorImpl(BodyDataInteractor callback) {
        this.callback = callback;
    }

    public interface BodyDataInteractor{
        void setFireBaseConError();
        void onSuccessBodyDataAdd();
        void onSuccessBodyDataModify();
    }

    public void addBodyData(Context context, BodyData bodyData){
        String URL = "http://vps-3c722567.vps.ovh.net/GainsLog/crud/bodyData/addBodyData.php";
        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONArray(response).getJSONObject(0);
                    BodyData tmp = new Gson().fromJson(jsonObject.toString(), BodyData.class);
                    if(tmp.getMeasurements() != null && tmp.getMeasurements().size() > 0){
                        for(int i = 0; i < bodyData.getMeasurements().size(); i++){
                            addMeasures(context,bodyData.getMeasurements().get(i));
                        }
                    }
                    callback.onSuccessBodyDataAdd();
                }
                catch(JSONException exception){exception.printStackTrace();}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                callback.setFireBaseConError();
            }

        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", FirebaseAuth.getInstance().getCurrentUser().getUid());
                params.put("weight", String.valueOf(bodyData.getWeight()));
                params.put("fat", String.valueOf(bodyData.getFatPer()));
                params.put("note", bodyData.getNote() == null ? "" : bodyData.getNote());
                return  params;
            }
        };
        Volley.newRequestQueue(context).add(request);

    }

    private void addMeasures(Context context, Measurement measurement){
        String URL = "http://vps-3c722567.vps.ovh.net/GainsLog/crud/bodyData/addBodyData.php";
        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONArray(response).getJSONObject(0);
                    if (response.equals("yes")) {
                    }
                }
                catch(JSONException exception){exception.printStackTrace();}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                callback.setFireBaseConError();
            }

        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", FirebaseAuth.getInstance().getCurrentUser().getUid());
                params.put("bodyData", String.valueOf(measurement.getBodyData()));
                params.put("value", String.valueOf(measurement.getMeasure()));
                params.put("note", String.valueOf(measurement.getIdMuscle()));
                return  params;
            }
        };
        Volley.newRequestQueue(context).add(request);
    }
}
