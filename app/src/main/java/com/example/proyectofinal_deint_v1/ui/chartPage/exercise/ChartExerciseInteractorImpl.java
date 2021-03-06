package com.example.proyectofinal_deint_v1.ui.chartPage.exercise;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.Exercise;
import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.Muscle;
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

public class ChartExerciseInteractorImpl {


    private ChartExerciseInteractor callback;

    public ChartExerciseInteractorImpl(ChartExerciseInteractor callback) {
        this.callback = callback;
    }

    public interface ChartExerciseInteractor{
        //WorkData
        void onEmptyRepositoryWorkDataError();
        void onFireBaseConnectionError();
        void onSuccessExerciseList(List<Exercise> exerciseList);
        void onSuccessExerciseTotalList(List<Exercise> exerciseList);
    }

    public void getList_ByDate(Context context, Exercise exercise, boolean filter){
        if(CommonUtils.isCoachUser(context)){
            listRequest(context, exercise, filter);
            return;
        }
            getExercises(context, FirebaseAuth.getInstance().getCurrentUser().getUid(), exercise,filter);

    }

    public void listRequest(Context context, Exercise exercise, boolean filter){
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
                                getExercises(context,tmp.getFb_id_user(),exercise,filter);
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

    //Obtiene el listado de ejercicios de un usuario en la base de datos, a trav??s del web service
    private void getExercises(Context context, String userUID,Exercise exercise,boolean filter) {
        List<Exercise> exerciseList = new ArrayList<>();
        String URL = (filter)  ? "http://vps-3c722567.vps.ovh.net/GainsLog/crud/exercise/stats/list_type_musc.php" : "http://vps-3c722567.vps.ovh.net/GainsLog/crud/exercise/listar.php";
        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    try {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            Exercise tmp = new Gson().fromJson(jsonObject.toString(), Exercise.class);
                            //Llamar al m??todo para obtener el listado de m??sculos principales. -->
                            exerciseList.add(tmp);
                            getMainMuscle(context,tmp,userUID);
                        }
                        if(filter){callback.onSuccessExerciseList(exerciseList);}
                        else{callback.onSuccessExerciseTotalList(exerciseList);}
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
                params.put("muscles", CommonUtils.getMusclesList(exercise.getMainMuscles(),false));
                return params;
            }
        };
        Volley.newRequestQueue(context).add(request);
    }

    //Obtiene el listado de m??sculos implicados en un ejercicio y setea el listado de m??sculos de ese ejercicio
    //Pasado por parametros con el nuevo listado extra??do desde el webservice.
    private void getMainMuscle(Context context,Exercise exerciseTmp, String userUID){
        List<Muscle> muscleList = new ArrayList<>();
        String URL = "http://vps-3c722567.vps.ovh.net/GainsLog/crud/mainMuscle/getMainMuscles.php";
        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    try {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            Muscle tmp = new Gson().fromJson(jsonObject.toString(), Muscle.class);
                            muscleList.add(tmp);
                        }
                        //A??adir lista de m??suculos al exercise
                        exerciseTmp.setMainMuscles(muscleList);
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
                params.put("idExercise", String.valueOf(exerciseTmp.getId()));
                params.put("fb_id", userUID);
                return params;
            }
        };
        Volley.newRequestQueue(context).add(request);
    }

}
