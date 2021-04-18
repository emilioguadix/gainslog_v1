package com.example.proyectofinal_deint_v1.ui.boxData.exercise.muscle;

import android.content.Context;

import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.Exercise;
import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.Muscle;
import com.example.proyectofinal_deint_v1.ui.base.BasePresenter;
import com.example.proyectofinal_deint_v1.ui.base.BaseView;
import com.example.proyectofinal_deint_v1.ui.base.BaseViewBoxData;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface MuscleListContract {
    interface View extends BaseView{
        void showProgress();
        void hideProgress();
        void setConnecctiontoRepositoryError();
        void onSuccess(List<Muscle> list);
    }

    interface Presenter extends BasePresenter {
        void gotResponse(Context context);
    }
}
