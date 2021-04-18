package com.example.proyectofinal_deint_v1.ui.boxData.exercise.muscle;

import android.content.Context;

import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.Muscle;

import java.util.List;

public class MusclePresenter implements MuscleListContract.Presenter,MuscleInteractorImpl.MuscleInteractor {

    private MuscleInteractorImpl interactor;
    private MuscleListContract.View view;

    public MusclePresenter(MuscleListContract.View view) {
        this.view = view;
        this.interactor = new MuscleInteractorImpl(this);
    }

    @Override
    public void onConnecctiontoRepositoryError() {
        this.view.hideProgress();
        this.view.setConnecctiontoRepositoryError();
    }

    @Override
    public void onSuccess(List<Muscle> list) {
        this.view.hideProgress();
        this.view.onSuccess(list);
    }

    @Override
    public void gotResponse(Context context) {
        this.view.showProgress();
        this.interactor.gotResponse(context);
    }

    @Override
    public void onDestroy() {
        this.interactor = null;
        this.view = null;
    }
}
