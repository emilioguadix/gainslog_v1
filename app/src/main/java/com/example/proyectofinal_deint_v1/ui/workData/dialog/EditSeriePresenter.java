package com.example.proyectofinal_deint_v1.ui.workData.dialog;

import android.content.Context;

import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.Exercise;
import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.workData.WorkData;
import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.workData.serie.Serie;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class EditSeriePresenter implements EditSerieContract.Presenter, EditSerieInteractorImp.EditSerieInteractor {

    private EditSerieInteractorImp interactor;
    private EditSerieContract.View view;

    public EditSeriePresenter(EditSerieContract.View view) {
        this.interactor = new EditSerieInteractorImp(this);
        this.view = view;
    }


    @Override
    public void updateRepository(WorkData workData) {
        this.interactor.updateRepository(workData);
    }

    @Override
    public void getRepository() {
        this.interactor.getRepository();
    }

    @Override
    public void deleteSerie(Serie serie) {
        this.interactor.deleteSerie(serie);
    }

    @Override
    public void addSerie(Serie serie) {
        this.interactor.addSerie(serie);
    }

    @Override
    public void modifySerie(Serie oldSerie, Serie newSerie){
        this.interactor.modifySerie(oldSerie,newSerie);
    }

    @Override
    public void modifyWorkData(Context context, WorkData oldWorkData) {
        this.interactor.modifyWorkData(context, oldWorkData);
    }

    @Override
    public void addWorkData(Context context, int idExercise) {
        this.interactor.addWorkData(context, idExercise);
    }

    @Override
    public void onDestroy() {
        this.interactor = null;
        this.view = null;
    }

    @Override
    public void onSuccess(List repository) {
        this.view.onSuccess(repository);
    }

    @Override
    public void onEmptyRepositoryError() {
        this.view.setEmptyRepositoryError();
    }

    @Override
    public void onFireBaseConnectionError() {
        this.view.setFireBaseConnectionError();
    }

    @Override
    public void onWeightEmptyError() {
        this.view.setWeightEmptyError();
    }

    @Override
    public void onRepsEmptyError() {
        this.view.setRepsEmptyError();
    }

    @Override
    public void onSuccessDelete() {
        this.view.onSuccessDelete();
    }

    @Override
    public void onSuccesAdd() {
        this.view.onSuccesAdd();
    }

    @Override
    public void onSuccesWorkDataAdd() {
        this.view.onSuccesWorkDataAdd();
    }

    @Override
    public void onSuccesModify() {
        this.view.onSuccesModify();
    }
}
