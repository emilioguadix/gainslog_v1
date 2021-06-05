package com.example.proyectofinal_deint_v1.ui.chartPage.bodyData;

import android.content.Context;

import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.bodyData.BodyData;
import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.workData.WorkData;
import com.example.proyectofinal_deint_v1.ui.base.BasePresenter;

import java.util.List;

public interface ChartBodyDataContract {
    interface View{
        void setEmptyRepositoryWorkDataError();
        void setFireBaseConnectionError();
        void onSuccessBodyData(List<BodyData> bodyData);
    }

    interface Presenter extends BasePresenter {
        void getRepositoryBodyData_ByDate(Context context, String cInit, String cFin);
    }
}
