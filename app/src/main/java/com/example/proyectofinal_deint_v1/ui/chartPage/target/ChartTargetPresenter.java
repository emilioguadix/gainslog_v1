package com.example.proyectofinal_deint_v1.ui.chartPage.target;

import android.content.Context;

import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.bodyData.BodyData;
import com.example.proyectofinal_deint_v1.data.model.model.target.Target;
import com.example.proyectofinal_deint_v1.ui.chartPage.bodyData.ChartBodyDataContract;
import com.example.proyectofinal_deint_v1.ui.chartPage.bodyData.ChartBodyDataInteractorImpl;

import java.util.List;

public class ChartTargetPresenter implements ChartTargetContract.Presenter, ChartTargetInteractorImpl.ChartTargetInteractor {

    private ChartTargetContract.View view;
    private ChartTargetInteractorImpl interactor;

    public ChartTargetPresenter(ChartTargetContract.View view) {
        this.view = view;
        this.interactor = new ChartTargetInteractorImpl(this);
    }

    @Override
    public void onDestroy() {
        this.view = null;
        this.interactor = null;
    }

    @Override
    public void getRepositoryTarget_ByDate(Context context, String cFin) {
        this.interactor.getList_ByDate(context,cFin);
    }

    @Override
    public void setEmptyRepositoryTargetError() {
        this.view.setEmptyRepositoryTargetError();
    }

    @Override
    public void setFireBaseConnectionError() {
        this.view.setFireBaseConnectionError();
    }

    @Override
    public void onSuccessTarget(List<Target> targets) {
        this.view.onSuccessTarget(targets);
    }

}

