package com.example.proyectofinal_deint_v1.ui.bodyData.measure;

import android.content.Context;

import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.bodyData.BodyData;
import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.bodyData.Measurement;
import com.example.proyectofinal_deint_v1.ui.bodyData.BodyDataContract;
import com.example.proyectofinal_deint_v1.ui.bodyData.BodyDataInteractorImpl;

import java.util.List;

public class MeasurePresenter implements MeasureContract.Presenter, MeasureInteractorImpl.MeasureInteractor {

    private MeasureContract.View view;
    private MeasureInteractorImpl interactor;

    public MeasurePresenter(MeasureContract.View view) {
        this.view = view;
        this.interactor = new MeasureInteractorImpl(this);
    }

    @Override
    public void addMeasure(List<Measurement> measure) {
        this.interactor.addMeasure(measure);
    }

    @Override
    public void clearMeasure() {
        this.interactor.clearMeasure();
    }

    @Override
    public void onDestroy() {
        this.interactor = null;
        this.view = null;
    }
    @Override
    public void onSuccessMeasureAdd() {
        this.view.onSuccessMeasureAdd();
    }

    @Override
    public void onSuccessMeasureClear() {
        this.view.onSuccessMeasureClear();
    }
}
