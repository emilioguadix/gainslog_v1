package com.example.proyectofinal_deint_v1.ui.bodyData.measure;

import android.content.Context;

import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.bodyData.BodyData;
import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.bodyData.Measurement;
import com.example.proyectofinal_deint_v1.ui.base.BasePresenter;
import com.example.proyectofinal_deint_v1.ui.base.BaseView;

import java.util.List;

public interface MeasureContract {
    interface View extends BaseView {
        //Método que muestra un error indicando que la autenticación no fue la correcta.
        void onSuccessMeasureAdd();
        void onSuccessMeasureClear();
    }

    interface Presenter extends BasePresenter {
        void addMeasure(List<Measurement> measure);
        void clearMeasure();
    }
}
