package com.example.proyectofinal_deint_v1.ui.chartPage.workData;

import android.content.Context;

import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.workData.WorkData;
import com.example.proyectofinal_deint_v1.ui.base.BasePresenter;

import java.util.Calendar;
import java.util.List;

public interface ChartWorkDataContract {
    interface View{
        void setEmptyRepositoryWorkDataError();
        void setFireBaseConnectionError();
        void onSuccessWorkData(List<WorkData> workData);
    }

    interface Presenter extends BasePresenter {
        void getRepositoryWorkData_ByDate(Context context, String cInit, String cFin);
    }
}
