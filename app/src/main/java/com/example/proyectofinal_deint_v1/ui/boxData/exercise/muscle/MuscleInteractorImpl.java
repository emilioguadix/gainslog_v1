package com.example.proyectofinal_deint_v1.ui.boxData.exercise.muscle;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.Muscle;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MuscleInteractorImpl{
    private MuscleInteractor callback;


    public interface ResponseListener{
        void catchResponse(List<Muscle> list);
    }

    public interface MuscleInteractor{
        void onConnecctiontoRepositoryError();
        void onSuccess(List<Muscle> list);
    }

    public MuscleInteractorImpl(MuscleInteractor callback) {
        this.callback = callback;
    }

    public void gotResponse(Context context){
        List<Muscle> muscleList = new ArrayList<>();
        //Debemos ejecutar el fichero php de la vps, y además, obtener la respuesta de este que en este caso será un .json
        String URL = "http://vps-3c722567.vps.ovh.net/GainsLog/crud/muscle/Listar.php";
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                Muscle tmp = new Gson().fromJson(jsonObject.toString(),Muscle.class);
                                muscleList.add(tmp);
                            }
                            callback.onSuccess(muscleList);
                        }
                        catch(JSONException ex){ex.printStackTrace();}
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onConnecctiontoRepositoryError();
                        error.printStackTrace();
                    }
                }

        );
        Volley.newRequestQueue(context).add(request);
    }
}
