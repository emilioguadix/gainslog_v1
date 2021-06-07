package com.example.proyectofinal_deint_v1.ui.bodyData;

import android.content.Context;
import android.icu.util.Measure;

import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.bodyData.BodyData;
import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.bodyData.Measurement;

public class BodyDataPresenter implements BodyDataContract.Presenter, BodyDataInteractorImpl.BodyDataInteractor {

    private BodyDataContract.View view;
    private BodyDataInteractorImpl interactor;

    public BodyDataPresenter(BodyDataContract.View view) {
        this.view = view;
        this.interactor = new BodyDataInteractorImpl(this);
    }

    @Override
    public void onDestroy() {
        this.view = null;
        this.interactor = null;
    }

    @Override
    public void setFireBaseConError() {
        this.view.setFireBaseConError();
    }

    @Override
    public void onSuccessBodyDataAdd(BodyData bodyData) {
        this.view.onSuccessBodyDataAdd(bodyData);
    }

    @Override
    public void addBodyData(Context context, BodyData bodyData) {
        this.interactor.addBodyData(context, bodyData);
    }

    @Override
    public void modifyBodyData(Context context, BodyData oldBodyData, BodyData newBodyData) {
        this.interactor.modifyBodyData(context, oldBodyData, newBodyData);
    }
}
