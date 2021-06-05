package com.example.proyectofinal_deint_v1.ui.homePage;

import android.content.Context;

import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.bodyData.BodyData;
import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.workData.WorkData;

import java.util.List;

public class HomeFragmentPresenter implements HomeFragmentContract.Presenter,HomeFragmentInteractorImpl.HomeFragmentInteractor {

    private HomeFragmentContract.View view;
    private HomeFragmentInteractorImpl interactor;

    public HomeFragmentPresenter(HomeFragmentContract.View view) {
        this.view = view;
        this.interactor = new HomeFragmentInteractorImpl(this);
    }

    @Override
    public void getRepositoryWorkData(Context context) {
        this.interactor.getRepositoryWorkData(context);
    }

    @Override
    public void getRepositoryBodyData(Context context) {
        this.interactor.getRepositoryBodyData(context);
    }

    @Override
    public void deleteWorkData(Context context, WorkData workData) {
        this.interactor.deleteWorkData(context, workData);
    }

    @Override
    public void deleteBodyData(Context context, BodyData bodyData) {
        this.interactor.deleteBodyData(context, bodyData);
    }

    @Override
    public void onDestroy() {
        this.interactor = null;
        this.view = null;
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
    public void onSuccessDeleteWorkData() {
        this.view.onSuccessDeleteWorkData();
    }

    @Override
    public void onSuccessDeleteBodyData() {
        this.view.onSuccessDeleteBodyData();
    }

    @Override
    public void onSuccessWorkData(List<WorkData> workData) {
        this.view.onSuccessWorkData(workData);
    }

    @Override
    public void onSuccessBodyData(List<BodyData> bodyData) {
        this.view.onSuccessBodyData(bodyData);
    }

    @Override
    public void onSuccesWorkDataList(Context context, String userUID, List<WorkData> workDataList) {
        for (int i = 0; i < workDataList.size(); i++) {
            this.interactor.getListSerie(context, userUID, workDataList,i);
        }
    }

    @Override
    public void onSuccesBodyDataList(Context context, String userUID, List<BodyData> bodyDataList) {
        for (int i = 0; i < bodyDataList.size(); i++) {
            this.interactor.getListMeasure(context, userUID, bodyDataList,i);
        }
    }
}
