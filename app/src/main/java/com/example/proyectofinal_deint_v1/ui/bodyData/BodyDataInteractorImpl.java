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
import com.example.proyectofinal_deint_v1.data.repository.products.MeasureRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BodyDataInteractorImpl {

    private BodyDataInteractor callback;

    public BodyDataInteractorImpl(BodyDataInteractor callback) {
        this.callback = callback;
    }

    public interface BodyDataInteractor{
        void setFireBaseConError();
        void onSuccessBodyDataAdd();
    }

    public void addBodyData(Context context, BodyData bodyData) {
        insertWebServBodyData(context,FirebaseAuth.getInstance().getCurrentUser().getUid(),bodyData);
    }

    public void modifyBodyData(Context context, BodyData oldBodyData, BodyData newBodyData) {
        modifyWebServBodyData(context,FirebaseAuth.getInstance().getCurrentUser().getUid(),oldBodyData,newBodyData);
    }

    public void modifyWebServBodyData(Context context,String userUID,BodyData oldBodyData, BodyData newBodyData) {
        String URL = "http://vps-3c722567.vps.ovh.net/GainsLog/crud/bodyData/modifyBodyData.php";
        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONArray(response).getJSONObject(0);
                    BodyData tmp = new Gson().fromJson(jsonObject.toString(), BodyData.class);
                    List<Measurement> listTmp = MeasureRepository.getInstance().getList() != null ? MeasureRepository.getInstance().getList() : new ArrayList<>();
                    tmp.setMeasurements(listTmp);
                    if(tmp.getMeasurements() != null && tmp.getMeasurements().size() > 0){
                        for(int i = 0; i < tmp.getMeasurements().size(); i++){
                            tmp.getMeasurements().get(i).setBodyData(tmp.getId());
                            addMeasures(context,tmp.getMeasurements().get(i));
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
                params.put("oldId", String.valueOf(oldBodyData.getId()));
                params.put("id", userUID);
                params.put("weight", String.valueOf(newBodyData.getWeight()));
                params.put("fat", String.valueOf(newBodyData.getFatPer()));
                params.put("note", newBodyData.getNote() == null ? "" : newBodyData.getNote());
                return  params;
            }
        };
        Volley.newRequestQueue(context).add(request);

    }

    public void insertWebServBodyData(Context context,String userUID, BodyData bodyData){
        String URL = "http://vps-3c722567.vps.ovh.net/GainsLog/crud/bodyData/addBodyData.php";
        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONArray(response).getJSONObject(0);
                    BodyData tmp = new Gson().fromJson(jsonObject.toString(), BodyData.class);
                    List<Measurement> listTmp = MeasureRepository.getInstance().getList() != null ? MeasureRepository.getInstance().getList() : new ArrayList<>();
                    tmp.setMeasurements(listTmp);
                    if(tmp.getMeasurements() != null && tmp.getMeasurements().size() > 0){
                        for(int i = 0; i < tmp.getMeasurements().size(); i++){
                            tmp.getMeasurements().get(i).setBodyData(tmp.getId());
                            addMeasures(context,tmp.getMeasurements().get(i));
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
                params.put("id", userUID);
                params.put("weight", String.valueOf(bodyData.getWeight()));
                params.put("fat", String.valueOf(bodyData.getFatPer()));
                params.put("note", bodyData.getNote() == null ? "" : bodyData.getNote());
                return  params;
            }
        };
        Volley.newRequestQueue(context).add(request);

    }

    private void addMeasures(Context context, Measurement measurement){
        String URL = "http://vps-3c722567.vps.ovh.net/GainsLog/crud/bodyData/measure/addMeasure.php";
        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                    if (!response.equals("yes")) {
                        callback.setFireBaseConError();
                    }
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
                params.put("idMuscle", String.valueOf(measurement.getIdMuscle()));
                params.put("value", String.valueOf(measurement.getMeasure()));
                params.put("side", measurement.getSide() == null ? "" : measurement.getSide());
                return  params;
            }
        };
        Volley.newRequestQueue(context).add(request);
    }
}
