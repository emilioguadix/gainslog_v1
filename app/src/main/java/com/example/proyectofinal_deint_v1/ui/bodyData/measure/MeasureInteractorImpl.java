package com.example.proyectofinal_deint_v1.ui.bodyData.measure;

import android.content.Context;

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

public class MeasureInteractorImpl {

    private MeasureInteractor callback;

    public MeasureInteractorImpl(MeasureInteractor callback) {
        this.callback = callback;
    }

    public interface MeasureInteractor{
        void onSuccessMeasureAdd();
        void onSuccessMeasureClear();
    }

    public void addMeasure(List<Measurement> list){
        MeasureRepository.getInstance().setList(new ArrayList<>());
        MeasureRepository.getInstance().setList(list);
        callback.onSuccessMeasureAdd();
    }

    public void clearMeasure(){
        MeasureRepository.getInstance().setList(new ArrayList<>());
        callback.onSuccessMeasureClear();
    }
}
