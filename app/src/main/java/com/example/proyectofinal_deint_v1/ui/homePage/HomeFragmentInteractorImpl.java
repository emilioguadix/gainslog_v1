package com.example.proyectofinal_deint_v1.ui.homePage;

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
import com.example.proyectofinal_deint_v1.data.model.model.target.Target;
import com.example.proyectofinal_deint_v1.data.repository.products.MeasureRepository;
import com.example.proyectofinal_deint_v1.data.repository.products.SerieRepository;
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

public class HomeFragmentInteractorImpl {

    private HomeFragmentInteractor callback;

    public interface HomeFragmentInteractor{
        //WorkData
        void onEmptyRepositoryWorkDataError();
        void onFireBaseConnectionError();
        void onSuccessDeleteWorkData();
        void onSuccessDeleteBodyData();
        void onSuccessWorkData(List<WorkData> workData);
        void onSuccessBodyData(List<BodyData> bodyData);
        void onSuccesWorkDataList(Context context, String userUID ,List<WorkData> workDataList);
        void onSuccesBodyDataList(Context context, String userUID ,List<BodyData> bodyDataList);
    }

    public HomeFragmentInteractorImpl(HomeFragmentInteractor callback) {
        this.callback = callback;
    }

    public void getRepositoryWorkData(final Context context){
        if(CommonUtils.isCoachUser(context)){
            listRequest(context,"work");
        }
        else {
            getWorkData_WebService(context, FirebaseAuth.getInstance().getCurrentUser().getUid());
        }
    }

    public void getRepositoryBodyData(final Context context){
        if(CommonUtils.isCoachUser(context)){
            listRequest(context,"body");
        }
        else {
            getBodyData_WebServ(context, FirebaseAuth.getInstance().getCurrentUser().getUid());
        }
    }

    public void deleteWorkData(Context context, WorkData workData){
        deleteWorkData_WebService(context,FirebaseAuth.getInstance().getCurrentUser().getUid(),workData);
    }

    public void deleteBodyData(Context context, BodyData bodyData){
        deleteBodyData_WebService(context,FirebaseAuth.getInstance().getCurrentUser().getUid(),bodyData);
    }

    public void listRequest(Context context,String tagRep){
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
                                switch (tagRep){
                                    case "work":
                                        getWorkData_WebService(context,tmp.getFb_id_user());
                                        return;
                                    case "body":
                                        getBodyData_WebServ(context,tmp.getFb_id_user());
                                        return;
                                }
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

    private void deleteWorkData_WebService(Context context, String userUID,WorkData workData){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        String URL = "http://vps-3c722567.vps.ovh.net/GainsLog/crud/workData/borrar.php";
        StringRequest request1 = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callback.onSuccessDeleteWorkData();
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
                params.put("workId", String.valueOf(workData.getId()));
                return params;
            }
        };
        requestQueue.add(request1);
    }

    private void deleteBodyData_WebService(Context context, String userUID,BodyData bodyData){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        String URL = "http://vps-3c722567.vps.ovh.net/GainsLog/crud/bodyData/delete.php";
        StringRequest request1 = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callback.onSuccessDeleteBodyData();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                callback.onFireBaseConnectionError();
            }

        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", userUID);
                params.put("body", String.valueOf(bodyData.getId()));
                return params;
            }
        };
        requestQueue.add(request1);
    }

    private void getWorkData_WebService(Context context, String userUID){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        List<WorkData> workDataList = new ArrayList<>();
        String URL = "http://vps-3c722567.vps.ovh.net/GainsLog/crud/workData/listar.php";
        StringRequest request1 = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    if(jsonArray.length() <= 0){callback.onSuccessWorkData(workDataList);return;}
                    try {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            WorkData tmp = new WorkData();
                            tmp.setId(jsonObject.getInt("id"));
                            tmp.setNameExercise(jsonObject.getString("nameExercise"));
                            tmp.setIdExercise(jsonObject.getInt("idExercise"));
                            tmp.setLogDate(CommonUtils.getDateFromStringTimeStamp(jsonObject.getString("logDate")));
                            tmp.setTypeExercise(jsonObject.getInt("typeExercise"));
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
                callback.onFireBaseConnectionError();
            }

        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", userUID);
                return params;
            }
        };
        requestQueue.add(request1);
    }

    public void getBodyData_WebServ(Context context,String userUID){
        List<BodyData> bodyDataList = new ArrayList<>();
        String URL = "http://vps-3c722567.vps.ovh.net/GainsLog/crud/bodyData/list.php";
        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    try {
                        if(jsonArray.length() <= 0){callback.onSuccessBodyData(bodyDataList);return;}
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            BodyData tmp = new Gson().fromJson(jsonObject.toString(), BodyData.class);
                            //Añadir además el listado de series al workData;
                            bodyDataList.add(tmp);
                        }
                        callback.onSuccesBodyDataList(context,userUID,bodyDataList);
                    } catch (JSONException ex) {
                        ex.printStackTrace();
                    }
                }
                catch(JSONException exception){exception.printStackTrace();}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                callback.onFireBaseConnectionError();
            }

        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", userUID);
                return  params;
            }
        };
        Volley.newRequestQueue(context).add(request);

    }
}
