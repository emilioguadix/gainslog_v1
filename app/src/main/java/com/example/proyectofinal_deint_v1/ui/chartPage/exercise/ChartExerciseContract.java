package com.example.proyectofinal_deint_v1.ui.chartPage.exercise;

import android.content.Context;

import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.Exercise;
import com.example.proyectofinal_deint_v1.ui.base.BasePresenter;

import java.util.List;

public interface ChartExerciseContract {

    interface View{
        void setEmptyRepositoryWorkDataError();
        void setFireBaseConnectionError();
        void onSuccessExerciseList(List<Exercise> exerciseList);
        void onSuccessExerciseTotalList(List<Exercise> exerciseList);
    }

    interface Presenter extends BasePresenter {
        void getRepositoryExercise(Context context,Exercise exerciseFilters);
        void getRepositoryTotal(Context context);
    }
}
