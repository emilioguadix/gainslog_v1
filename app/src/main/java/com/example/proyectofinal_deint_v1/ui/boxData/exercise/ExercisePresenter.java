package com.example.proyectofinal_deint_v1.ui.boxData.exercise;

import android.content.Context;

import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.Exercise;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class ExercisePresenter implements ExerciseContract.Presenter, ExerciseInteractorImp.ExerciseInteractor {

    ExerciseInteractorImp interactor;
    ExerciseContract.View view;

    public ExercisePresenter(ExerciseContract.View view) {
        this.view = view;
        interactor = new ExerciseInteractorImp(this);
    }

    @Override
    public void getRepository(Context context) {
        view.showProgress();
        interactor.getRepository(context);
    }

    @Override
    public void deleteExercise(Context context,Exercise exercise) {
        view.showProgress();
        interactor.deleteExercise(context,exercise);
    }

    @Override
    public void addExercise(Context context, Exercise exercise) {
        view.showProgress();
        interactor.addExercise(context,exercise);
    }

    @Override
    public void modifyExercise(Context context,Exercise oldExercise, Exercise newExercise) throws ExecutionException, InterruptedException {
        view.showProgress();
        interactor.modifyExercise(context,oldExercise, newExercise);
    }

    @Override
    public void sortListExercise(Context context) {
        view.hideProgress();
        interactor.sortListExercise(context);
    }

    @Override
    public void onDestroy() {
        interactor = null;
        view = null;
    }

    @Override
    public void onSuccess(List repository) {
        view.onSuccess(repository);
        view.hideProgress();
    }

    @Override
    public void onSuccessDelete() {
        view.hideProgress();
        view.onSuccessDelete();
    }

    @Override
    public void onNameEmptyError() {
        view.hideProgress();
        view.setNameEmptyError();
    }

    @Override
    public void onDeleteExError() {
        view.hideProgress();
        view.setDeleteExError();
    }

    @Override
    public void onExerciseExistsError() {
        view.hideProgress();
        view.setExerciseExistsError();
    }

    @Override
    public void onMusclesEmptyError() {
        view.hideProgress();
        view.setMusclesEmptyError();
    }

    @Override
    public void onSuccessAdd() {
        view.hideProgress();
        view.onSuccesAdd();
    }

    @Override
    public void onSuccesModify() {
        view.hideProgress();
        view.onSuccesModify();
    }
}
