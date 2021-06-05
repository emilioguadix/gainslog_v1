package com.example.proyectofinal_deint_v1.ui.chartPage.bodyData;

import android.content.Context;

import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.bodyData.BodyData;
import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.workData.WorkData;
import com.example.proyectofinal_deint_v1.ui.chartPage.workData.ChartWorkDataContract;
import com.example.proyectofinal_deint_v1.ui.chartPage.workData.ChartWorkDataInteractorImpl;

import java.util.List;

import retrofit2.http.Body;

public class ChartBodyDataPresenter implements ChartBodyDataContract.Presenter, ChartBodyDataInteractorImpl.ChartBodyDataInteractor {

    private ChartBodyDataContract.View view;
    private ChartBodyDataInteractorImpl interactor;

    public ChartBodyDataPresenter(ChartBodyDataContract.View view) {
        this.view = view;
        this.interactor = new ChartBodyDataInteractorImpl(this);
    }

    @Override
    public void getRepositoryBodyData_ByDate(Context context, String cInit, String cFin) {
        this.interactor.getList_ByDate(context, cInit, cFin);
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
    public void onSuccessBodyData(List<BodyData> bodyData) {
        this.view.onSuccessBodyData(bodyData);
    }

    @Override
    public void onSuccesBodyDataList(Context context, String userUID, List<BodyData> bodyData) {
        for (int i = 0; i < bodyData.size(); i++) {
            this.interactor.getListMeasure(context, userUID, bodyData,i);
        }

    }
}
