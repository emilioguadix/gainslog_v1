package com.example.proyectofinal_deint_v1.ui.workData.dialog;

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
import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.workData.WorkData;
import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.workData.serie.Serie;
import com.example.proyectofinal_deint_v1.data.repository.products.SerieRepository;
import com.example.proyectofinal_deint_v1.ui.utils.CommonUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditSerieInteractorImp {

    EditSerieInteractor callback;
    public interface EditSerieInteractor {
        void onSuccess(List repository);
        void onEmptyRepositoryError();
        void onFireBaseConnectionError();
        void onWeightEmptyError();
        void onRepsEmptyError();
        void onSuccessDelete();
        void onSuccesAdd();
        void onSuccesWorkDataAdd();
        void onSuccesModify();
    }

    public EditSerieInteractorImp(EditSerieInteractor callback) {
        this.callback = callback;
    }

    public void deleteSerie(Serie serie){
        SerieRepository.getInstance().deleteSerie(serie);
        callback.onSuccessDelete();
    }

    public void addSerie(Serie serie){
        if(String.valueOf(serie.getWeight()).isEmpty()){
            callback.onWeightEmptyError();
            return;
        }
        if(String.valueOf(serie.getReps()).isEmpty()){
            callback.onRepsEmptyError();
            return;
        }
            SerieRepository.getInstance().add(serie);
            callback.onSuccesAdd();
            return;
    }

    public void modifySerie(Serie oldSerie,Serie newSerie){
        if(String.valueOf(oldSerie.getWeight()).isEmpty()){
            callback.onWeightEmptyError();
            return;
        }
        if(String.valueOf(oldSerie.getReps()).isEmpty()){
            callback.onRepsEmptyError();
            return;
        }

        if(SerieRepository.getInstance().modify(oldSerie,newSerie)){
            callback.onSuccesAdd();
        }
        return;
    }

    public void getRepository(){
        List<Serie> list = SerieRepository.getInstance().getList();

        Collections.sort(list, new Comparator<Serie>(){
            @Override
            public int compare(Serie o1, Serie o2) {
                return (o1.getNumSerie() > o2.getNumSerie()) ? 1 : -1;
            }
        });
        if(list.size() > 0){
            callback.onSuccess(list);
            return;
        }
        callback.onEmptyRepositoryError();
        return;
    }

    public void updateRepository(WorkData workData){
        SerieRepository.getInstance().getList().clear();
        SerieRepository.getInstance().getList().addAll(workData.getSerieList());
    }

    public void modifyWorkData(Context context, WorkData oldWorkData){
        if(CommonUtils.isCoachUser(context)){
            listRequest(context,oldWorkData,"modify");
        }
        else {
            resetWorkDataSerieList(context, FirebaseAuth.getInstance().getCurrentUser().getUid(), oldWorkData);
        }
    }

    public void addWorkData(Context context,WorkData workData) {
        if(CommonUtils.isCoachUser(context)){
            listRequest(context,workData,"add");
        }
        else {
            insertWebServ(context, FirebaseAuth.getInstance().getCurrentUser().getUid(), workData);
        }
    }

    public void listRequest(Context context,WorkData workData,String mode){
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
                                switch (mode){
                                    case "add":
                                        insertWebServ(context,tmp.getFb_id_user(),workData);
                                    case "modify":
                                        resetWorkDataSerieList(context, tmp.getFb_id_user(), workData);

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

    private void insertWebServ(Context context, String userUID,WorkData workData) {
        List<Serie> listSerie = SerieRepository.getInstance().getList();
        String URL = "http://vps-3c722567.vps.ovh.net/GainsLog/crud/workData/insertar.php";
        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            //`RESPONSE` devuelve el id del ultimo workData insertado en la base de datos
            public void onResponse(String response) {
                if(!response.isEmpty()){
                    for (int i = 0; i < listSerie.size(); i++){
                        insertWebServ(context,userUID,Integer.parseInt(response),listSerie.get(i));
                    }
                    SerieRepository.getInstance().getList().clear();
                    callback.onSuccesWorkDataAdd();
                }
                else {
                    callback.onFireBaseConnectionError();
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
                params.put("fb_id", userUID);
                params.put("exercise",String.valueOf(workData.getIdExercise()));
                params.put("typeExercise",String.valueOf(workData.getTypeExercise()));
                return params;
            }
        };
        Volley.newRequestQueue(context).add(request);
    }

    private void resetWorkDataSerieList(Context context,String userUID,WorkData workData){
        String URL = "http://vps-3c722567.vps.ovh.net/GainsLog/crud/serie/modificar.php";
        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                for (int i = 0; i < SerieRepository.getInstance().getList().size(); i++){
                    insertWebServ(context,userUID,workData.getId() ,SerieRepository.getInstance().getList().get(i));
                }

                callback.onSuccesModify();
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
                params.put("fb_id", userUID);
                params.put("workId", String.valueOf(workData.getId()));
                return params;
            }
        };
        Volley.newRequestQueue(context).add(request);
    }

    //Insertar el listado de series, asignado a ese workData
    private void insertWebServ(Context context,String userUID,int workDataId,Serie serie){
        String URL = "http://vps-3c722567.vps.ovh.net/GainsLog/crud/serie/insertar.php";
        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals(String.valueOf(serie.getNumSerie()))){
                    //Continuar operando...
                }
                else {
                    callback.onFireBaseConnectionError();
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
                params.put("fb_id", userUID);
                params.put("workId", String.valueOf(workDataId));
                //Datos acerca de la serie -->
                params.put("id", String.valueOf(serie.getNumSerie()));
                params.put("intType", serie.getTypeIntensity());
                params.put("int", String.valueOf(serie.getIntensity()));
                params.put("tipoSerie", String.valueOf(serie.getTypeSerie()));
                params.put("nota", String.valueOf(serie.getNote()));
                params.put("peso", String.valueOf(serie.getWeight()));
                params.put("reps", String.valueOf(serie.getReps()));
                params.put("timeRest", String.valueOf(serie.getTimeRest()));
                params.put("time", String.valueOf(serie.getTime()));
                params.put("marked",String.valueOf(serie.getMarked()));
                return params;
            }
        };
        Volley.newRequestQueue(context).add(request);
    }
}
