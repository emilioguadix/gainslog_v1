package com.example.proyectofinal_deint_v1.ui.workData.dialog;

import android.content.Context;

import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.Exercise;
import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.workData.serie.Serie;
import com.example.proyectofinal_deint_v1.ui.base.BasePresenter;
import com.example.proyectofinal_deint_v1.ui.base.BaseView;
import com.example.proyectofinal_deint_v1.ui.base.BaseViewBoxData;

import java.util.concurrent.ExecutionException;

public interface EditSerieContract {
    interface View extends BaseView, BaseViewBoxData {
        void setEmptyRepositoryError();
        void setWeightEmptyError();
        void setRepsEmptyError();
        void onSuccessDelete();
        void onSuccesAdd();
        void onSuccesModify();
    }

    interface Presenter extends BasePresenter {
        void getRepository();
        void deleteSerie(Serie serie);
        void addSerie(Serie serie);
        void modifySerie(Serie oldSerie, Serie newSerie);
    }
}