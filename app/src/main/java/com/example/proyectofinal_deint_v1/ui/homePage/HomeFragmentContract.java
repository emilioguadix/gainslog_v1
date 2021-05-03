package com.example.proyectofinal_deint_v1.ui.homePage;

import android.content.Context;

import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.Exercise;
import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.workData.WorkData;
import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.workData.serie.Serie;
import com.example.proyectofinal_deint_v1.ui.base.BasePresenter;
import com.example.proyectofinal_deint_v1.ui.base.BaseView;
import com.example.proyectofinal_deint_v1.ui.base.BaseViewBoxData;

import java.util.List;

public interface HomeFragmentContract {
    interface View{
        //WorkData
        void setEmptyRepositoryWorkDataError();
        void setFireBaseConnectionError();
        void onSuccessDeleteWorkData();
        void onSuccessWorkData(List<WorkData> workData);
    }

    interface Presenter extends BasePresenter {
        //WorkData
        void getRepositoryWorkData(Context context);
        void deleteWorkData(Context context,WorkData workData);
    }
}
