package com.example.proyectofinal_deint_v1.ui.chartPage.exercise;

import android.content.Context;

import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.Exercise;
import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.workData.WorkData;
import com.example.proyectofinal_deint_v1.ui.chartPage.workData.ChartWorkDataContract;
import com.example.proyectofinal_deint_v1.ui.chartPage.workData.ChartWorkDataInteractorImpl;

import java.util.List;

public class ChartExercisePresenter implements ChartExerciseContract.Presenter, ChartExerciseInteractorImpl.ChartExerciseInteractor {

    private ChartExerciseContract.View view;
    private ChartExerciseInteractorImpl interactor;

    public ChartExercisePresenter(ChartExerciseContract.View view) {
        this.view = view;
        this.interactor = new ChartExerciseInteractorImpl(this);
    }

    @Override
    public void getRepositoryExercise(Context context, Exercise exercise) {
        this.interactor.getList_ByDate(context, exercise,true);
    }

    @Override
    public void getRepositoryTotal(Context context) {
        this.interactor.getList_ByDate(context,new Exercise(),false);
    }

    @Override
    public void onDestroy() {
        this.view = null;
        this.interactor = null;
    }

    @Override
    public void onEmptyRepositoryWorkDataError() {
        this.view.setEmptyRepositoryWorkDataError();
    }

    @Override
    public void onFireBaseConnectionError() {
        this.view.setFireBaseConnectionError();
    }

    @Override
    public void onSuccessExerciseList(List<Exercise> exerciseList) {
        this.view.onSuccessExerciseList(exerciseList);
    }

    @Override
    public void onSuccessExerciseTotalList(List<Exercise> exerciseList) {
        this.view.onSuccessExerciseTotalList(exerciseList);
    }
}
