package com.example.proyectofinal_deint_v1.ui.boxData.exercise;

import android.content.Context;

import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.Exercise;
import com.example.proyectofinal_deint_v1.ui.base.BasePresenter;
import com.example.proyectofinal_deint_v1.ui.base.BaseView;
import com.example.proyectofinal_deint_v1.ui.base.BaseViewBoxData;

import java.util.concurrent.ExecutionException;

public interface ExerciseContract {
    interface View extends BaseView, BaseViewBoxData {
        void showProgress();
        void hideProgress();
        void setConnecctiontoRepositoryError();
        void setNameEmptyError();
        void setExerciseExistsError();
        void setMusclesEmptyError();
        void onSuccessDelete();
        void onSuccesAdd();
        void onSuccesModify();
        void setDeleteExError();
    }

    interface Presenter extends BasePresenter{
        void getRepository(Context context);
        void deleteExercise(Context context, Exercise exercise);
        void addExercise(Context context, Exercise exercise);
        void modifyExercise(Context context,Exercise oldExercise, Exercise newExercise) throws ExecutionException, InterruptedException;
        void sortListExercise(Context context);
    }
}
