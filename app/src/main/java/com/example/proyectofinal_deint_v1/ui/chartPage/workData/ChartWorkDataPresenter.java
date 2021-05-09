package com.example.proyectofinal_deint_v1.ui.chartPage.workData;

import android.content.Context;

import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.workData.WorkData;
import com.example.proyectofinal_deint_v1.ui.homePage.HomeFragmentContract;
import com.example.proyectofinal_deint_v1.ui.homePage.HomeFragmentInteractorImpl;

import java.util.Calendar;
import java.util.List;

public class ChartWorkDataPresenter implements ChartWorkDataContract.Presenter, ChartWorkDataInteractorImpl.ChartWorkDataInteractor {

    private ChartWorkDataContract.View view;
    private ChartWorkDataInteractorImpl interactor;

    public ChartWorkDataPresenter(ChartWorkDataContract.View view) {
        this.view = view;
        this.interactor = new ChartWorkDataInteractorImpl(this);
    }

    @Override
    public void getRepositoryWorkData_ByDate(Context context, String cInit, String cFin) {
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
    public void onSuccessWorkData(List<WorkData> workData) {
        this.view.onSuccessWorkData(workData);
    }

    @Override
    public void onSuccesWorkDataList(Context context, String userUID, List<WorkData> workDataList) {
        for (int i = 0; i < workDataList.size(); i++) {
            this.interactor.getListSerie(context, userUID, workDataList,i);
        }

    }
}
