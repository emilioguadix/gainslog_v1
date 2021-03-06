package com.example.proyectofinal_deint_v1.ui.boxData.target;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.proyectofinal_deint_v1.data.model.model.target.Target;
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

public class TargetInteractorImp {

    TargetInteractor callback;

    interface TargetInteractor {
        void onSuccess(List repository);
        void onSuccessDelete();
        void onNameEmptyError();
        void setExerciseNotExistsError();
        void setDateExpError();
        void onSuccessAdd();
        void onSuccesModify();
    }

    public TargetInteractorImp(TargetInteractor presenter) {
        this.callback = presenter;
    }

    public void getRepository(Context context,boolean showExpirateTargets) {
        if(CommonUtils.isCoachUser(context)){
            listRequest(context,showExpirateTargets);
            return;
        }
        getTargets(context, FirebaseAuth.getInstance().getCurrentUser().getUid(),showExpirateTargets);
    }


    public void listRequest(Context context, boolean showExpirateTargets){
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
                            //Llamar al m??todo para obtener el listado de m??sculos principales. -->
                            if(tmp.get_accepted() == 1){
                                getTargets(context,tmp.getFb_id_user(),showExpirateTargets);
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

    public void deleteTarget(Context context, Target target) {
        deleteTargetWebServ(context,target,FirebaseAuth.getInstance().getCurrentUser().getUid());
    }

    public void modifyTarget(Context context,final Target oldTarget, final Target newTarget)  {
        if (TextUtils.isEmpty(newTarget.getNameTarget())) {
            callback.onNameEmptyError();
            return;
        }
        modifyTargetWebServ(context,oldTarget,newTarget,FirebaseAuth.getInstance().getCurrentUser().getUid());
    }

    public void addTarget(Context context,final Target target) {
        if (TextUtils.isEmpty(target.getNameTarget())) {
            callback.onNameEmptyError();
            return;
        }
        insertTargetWebServ(context,FirebaseAuth.getInstance().getCurrentUser().getUid(),target);
    }

    private void deleteTargetWebServ(Context context,Target target,String userUID){
        String URL = "http://vps-3c722567.vps.ovh.net/GainsLog/crud/target/borrar.php";
        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("yes")) {
                    callback.onSuccessDelete();
                }
                else {
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
                params.put("idUser", userUID);
                params.put("id", String.valueOf(target.getId()));
                return params;
            }
        };
        Volley.newRequestQueue(context).add(request);
    }

    private void modifyTargetWebServ(Context context,Target oldTarget, Target newTarget,String userUID){
        String URL = "http://vps-3c722567.vps.ovh.net/GainsLog/crud/target/modificar.php";
        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("yes")) {
                    callback.onSuccesModify();
                }
                else {
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
                params.put("idUser", userUID);
                params.put("id", String.valueOf(oldTarget.getId()));
                params.put("name", newTarget.getNameTarget());
                params.put("overcome", newTarget.isOvercome() ? "1" : "0");
                params.put("description", newTarget.getDescription());
                params.put("expirationDate", String.valueOf(CommonUtils.getTimeStampOfCalendar(newTarget.getExpirationDate())));
                return params;
            }
        };
        Volley.newRequestQueue(context).add(request);
    }

    private void getTargets(Context context, String userUID,boolean showExpirateTargets) {
        List<Target> targetList = new ArrayList<>();
        String URL = (showExpirateTargets) ? "http://vps-3c722567.vps.ovh.net/GainsLog/crud/target/listar_all.php" : "http://vps-3c722567.vps.ovh.net/GainsLog/crud/target/listar.php";
        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
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
                        callback.onSuccess(targetList);
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
                return params;
            }
        };
        Volley.newRequestQueue(context).add(request);
    }

    private void insertTargetWebServ(Context context,String userUID,Target target){
        String URL = "http://vps-3c722567.vps.ovh.net/GainsLog/crud/target/insertar.php";
        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("yes")) {
                    callback.onSuccessAdd();
                }
                else {
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
                params.put("idUser", userUID);
                params.put("name", target.getNameTarget());
                params.put("description", target.getDescription());
                params.put("expirationDate", String.valueOf(CommonUtils.getTimeStampOfCalendar(target.getExpirationDate())));
                params.put("overcome", target.isOvercome() ? "1" : "0");
                return params;
            }
        };
        Volley.newRequestQueue(context).add(request);
    }
}
