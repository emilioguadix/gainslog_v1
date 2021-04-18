package com.example.proyectofinal_deint_v1.ui.boxData.exercise;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.Exercise;
import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.Muscle;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExerciseInteractorImp {

    ExerciseInteractor callback;

    interface ExerciseInteractor {
        void onSuccess(List repository);
        void onSuccessDelete();
        void onNameEmptyError();
        void onDeleteExError();
        void onExerciseExistsError();
        void onMusclesEmptyError();
        void onSuccessAdd();
        void onSuccesModify();
    }

    public ExerciseInteractorImp(ExerciseInteractor presenter) {
        this.callback = presenter;
    }

    //Método que accede al repositorio de ejercicios(en este caso), y devuelve un arraylist<object>
    public void getRepository(Context context) {
        getExercises(context,FirebaseAuth.getInstance().getCurrentUser().getUid(),false);
    }

    public void sortListExercise(Context context){
        getExercises(context,FirebaseAuth.getInstance().getCurrentUser().getUid(),true);
    }

    public void deleteExercise(Context context, Exercise exercise) {
        deleteWebServ(context,FirebaseAuth.getInstance().getCurrentUser().getUid(),exercise);
    }

    public void modifyExercise(Context context, final Exercise oldExercise,final Exercise exercise)  {
        if (TextUtils.isEmpty(exercise.getName())) {
            callback.onNameEmptyError();
            return;
        }

        if (exercise.getMainMuscles().size() <= 0) {
            callback.onMusclesEmptyError();
            return;
        }
        updateWebServ(context,FirebaseAuth.getInstance().getCurrentUser().getUid(),oldExercise,exercise);
    }

    public void addExercise(Context context,final Exercise exercise) {
            if (TextUtils.isEmpty(exercise.getName())) {
                callback.onNameEmptyError();
                return;
            }

            if (exercise.getMainMuscles().size() <= 0) {
                callback.onMusclesEmptyError();
                return;
            }
            insertWebServ(context,FirebaseAuth.getInstance().getCurrentUser().getUid(),exercise);
    }

    //MÉTODOS QUE LLAMAN A UN WEB SERVICE UBICADO EN EL VPS

    private void deleteWebServ(Context context,String userUID, Exercise exercise){
        String URL = "http://vps-3c722567.vps.ovh.net/GainsLog/crud/exercise/borrar.php";
        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Devulve res --> no en caso de que el ejercicio no exista en el listado
                if (response.equals("yes")) {
                    callback.onSuccessDelete();
                } else {
                    callback.onDeleteExError();
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
                params.put("idExercise",String.valueOf(exercise.getId()));
                return params;
            }
        };
        Volley.newRequestQueue(context).add(request);

    }

    //Actualiza un ejercicio
    private void updateWebServ(Context context, String userUID,Exercise oldExercise,Exercise exercise){
        List<Muscle> muscleList = new ArrayList<>();
        String URL = "http://vps-3c722567.vps.ovh.net/GainsLog/crud/exercise/actualizar.php";
        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                    //Devulve res --> no en caso de que el ejercicio no exista en el listado
                    if (response.equals("no")) {
                        callback.onSuccesModify();
                    } else {
                        callback.onExerciseExistsError();
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
                params.put("oldName", oldExercise.getName());
                params.put("idExercise",String.valueOf(oldExercise.getId()));
                params.put("name", exercise.getName());
                params.put("typeExercise", String.valueOf(exercise.getTypeExercise()));
                params.put("description", exercise.getDescription() == null ? "" : exercise.getDescription());
                params.put("material", exercise.getMaterial() == null ? "" : exercise.getMaterial());
                params.put("notes", exercise.getNotes() == null ? "" : exercise.getNotes());
                params.put("img", "");
                params.put("urlVideo", exercise.getUrlVideo() == null ? "" : exercise.getUrlVideo());
                params.put("favorite", !exercise.isFavorite()  ? "0" : "1");
                //Seteamos el listado de músculos implicados -->
                String muscleIds = "";
                if(exercise.getMainMuscles().size() > 0) {
                    muscleIds = getMusclesList(exercise.getMainMuscles());
                }
                params.put("muscles", muscleIds);
                return params;
            }
        };
        Volley.newRequestQueue(context).add(request);

    }

    //Añade un ejercicio
    private void insertWebServ(Context context, String userUID,Exercise exercise){
        List<Muscle> muscleList = new ArrayList<>();
        String URL = "http://vps-3c722567.vps.ovh.net/GainsLog/crud/exercise/insertar.php";
        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONArray(response).getJSONObject(0);
                    //Devulve res --> no en caso de que el ejercicio no exista en el listado
                    if (jsonObject.getString("res").equals("no")) {
                        callback.onSuccessAdd();
                    } else {
                        callback.onExerciseExistsError();
                    }
                }
                catch(JSONException exception){exception.printStackTrace();}
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
                params.put("name", exercise.getName());
                params.put("typeExercise", String.valueOf(exercise.getTypeExercise()));
                params.put("description", exercise.getDescription() == null ? "" : exercise.getDescription());
                params.put("material", exercise.getMaterial() == null ? "" : exercise.getMaterial());
                params.put("notes", exercise.getNotes() == null ? "" : exercise.getNotes());
                params.put("img", "");
                params.put("urlVideo", exercise.getUrlVideo() == null ? "" : exercise.getUrlVideo());
                params.put("favorite", !exercise.isFavorite()  ? "0" : "1");
                //Seteamos el listado de músculos implicados -->
                String muscleIds = "";
                if(exercise.getMainMuscles().size() > 0) {
                    muscleIds = getMusclesList(exercise.getMainMuscles());
                }
                params.put("muscles", muscleIds);
                return params;
            }
        };
        Volley.newRequestQueue(context).add(request);

    }

    //Método que dado un listado de músculos devuelve en un string la concatenación de estos separados por coma --> 1,2,3
    private String getMusclesList(List<Muscle> arraylist){
        String tmpMusc = "";
        for (Muscle muscle : arraylist) {
            tmpMusc += muscle.getId() + ",";
        }
        return (arraylist.size() >= 1) ? tmpMusc.substring(0, tmpMusc.length() - 1) : "";
    }

    //Obtiene el listado de ejercicios de un usuario en la base de datos, a través del web service
    private void getExercises(Context context, String userUID,boolean shortByName) {
        List<Exercise> exerciseList = new ArrayList<>();
        String URL = (shortByName) ? "http://vps-3c722567.vps.ovh.net/GainsLog/crud/exercise/listar_orderBy_name.php" : "http://vps-3c722567.vps.ovh.net/GainsLog/crud/exercise/listar.php";
        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    try {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            Exercise tmp = new Gson().fromJson(jsonObject.toString(), Exercise.class);
                            //Llamar al método para obtener el listado de músculos principales. -->
                            exerciseList.add(tmp);
                            getMainMuscle(context,tmp,userUID);
                        }
                        callback.onSuccess(exerciseList);
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

    //Obtiene el listado de músculos implicados en un ejercicio y setea el listado de músculos de ese ejercicio
    //Pasado por parametros con el nuevo listado extraído desde el webservice.
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
                        //Añadir lista de músuculos al exercise
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
