package com.example.proyectofinal_deint_v1.ui.boxData.target;

import android.content.Context;

import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.Exercise;
import com.example.proyectofinal_deint_v1.data.model.model.target.Target;
import com.example.proyectofinal_deint_v1.ui.base.BasePresenter;
import com.example.proyectofinal_deint_v1.ui.base.BaseView;
import com.example.proyectofinal_deint_v1.ui.base.BaseViewBoxData;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public interface TargetContract {
    interface View extends BaseView, BaseViewBoxData {
        void showProgress();
        void hideProgress();
        void setNameEmptyError();
        void setExerciseNotExistsError();
        void setDateExpError();
        void onSuccessDelete();
        void onSuccesAdd();
        void onSuccesModify();
    }

    interface Presenter extends BasePresenter{
        void getRepository(Context context);
        void deleteTarget(Context context,Target target);
        void addTarget(Context context,Target target);
        void modifyTarget(Context context, Target oldTarget, Target newTarget);
    }
}
