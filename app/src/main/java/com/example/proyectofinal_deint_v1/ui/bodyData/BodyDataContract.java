package com.example.proyectofinal_deint_v1.ui.bodyData;

import android.content.Context;
import android.icu.util.Measure;

import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.bodyData.BodyData;
import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.bodyData.Measurement;
import com.example.proyectofinal_deint_v1.data.model.model.user.User;
import com.example.proyectofinal_deint_v1.ui.base.BasePresenter;
import com.example.proyectofinal_deint_v1.ui.base.BaseView;
import com.example.proyectofinal_deint_v1.ui.user.UserView;

public interface BodyDataContract {
    interface View extends BaseView {
        //Método que muestra un error indicando que la autenticación no fue la correcta.
        void setFireBaseConError();
        void onSuccessBodyDataAdd();
        void onSuccessBodyDataModify();
    }

    interface Presenter extends BasePresenter {
        void addBodyData(Context context, BodyData bodyData);
        void addMeasure(Context context, Measurement measure);
        void modifyMeasure(Context context,Measurement oldMeasure, Measurement newMeasure);
    }
}
