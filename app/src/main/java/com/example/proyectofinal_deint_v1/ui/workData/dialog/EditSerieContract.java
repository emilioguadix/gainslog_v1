package com.example.proyectofinal_deint_v1.ui.workData.dialog;

import android.content.Context;

import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.Exercise;
import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.workData.WorkData;
import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.workData.serie.Serie;
import com.example.proyectofinal_deint_v1.ui.base.BasePresenter;
import com.example.proyectofinal_deint_v1.ui.base.BaseView;
import com.example.proyectofinal_deint_v1.ui.base.BaseViewBoxData;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface EditSerieContract {
    interface View extends BaseView, BaseViewBoxData {
        void setEmptyRepositoryError();
        void setFireBaseConnectionError();
        void setWeightEmptyError();
        void setRepsEmptyError();
        void onSuccessDelete();
        void onSuccesAdd();
        void onSuccesWorkDataAdd();
        void onSuccesModify();
    }

    interface Presenter extends BasePresenter {
        void updateRepository(WorkData workData);
        void getRepository();
        void deleteSerie(Serie serie);
        void addSerie(Serie serie);
        void modifySerie(Serie oldSerie, Serie newSerie);
        void modifyWorkData(Context context, WorkData oldWorkData);
        void addWorkData(Context context,WorkData workData);
    }
}
