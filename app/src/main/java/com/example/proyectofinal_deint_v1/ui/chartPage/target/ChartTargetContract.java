package com.example.proyectofinal_deint_v1.ui.chartPage.target;

import android.content.Context;

import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.bodyData.BodyData;
import com.example.proyectofinal_deint_v1.data.model.model.target.Target;
import com.example.proyectofinal_deint_v1.ui.base.BasePresenter;

import java.util.List;

public interface ChartTargetContract {
    interface View{
        void setEmptyRepositoryTargetError();
        void setFireBaseConnectionError();
        void onSuccessTarget(List<Target> targets);
    }

    interface Presenter extends BasePresenter {
        void getRepositoryTarget_ByDate(Context context, String cFin);
    }
}
