package com.example.proyectofinal_deint_v1.ui.boxData.target;

import android.content.Context;

import com.example.proyectofinal_deint_v1.data.model.model.target.Target;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TargetPresenter implements TargetContract.Presenter, TargetInteractorImp.TargetInteractor {

    TargetInteractorImp interactor;
    TargetContract.View view;

    public TargetPresenter(TargetContract.View view){
        this.view = view;
        interactor = new TargetInteractorImp(this);
    }

    @Override
    public void getRepository(Context context, boolean showExpirateTargets) {
        this.interactor.getRepository(context, showExpirateTargets);
    }

    @Override
    public void deleteTarget(Context context,Target target) {
        this.view.hideProgress();
        this.interactor.deleteTarget(context,target);
    }

    @Override
    public void addTarget(Context context, Target target) {
        this.view.hideProgress();
        this.interactor.addTarget(context,target);
    }

    @Override
    public void modifyTarget(Context context, Target oldTarget, Target newTarget) {
        this.view.hideProgress();
        this.interactor.modifyTarget(context, oldTarget, newTarget);
    }

    @Override
    public void onDestroy() {
        this.view = null;
        this.interactor = null;
    }

    @Override
    public void onSuccess(List repository) {
        this.view.hideProgress();
        this.view.onSuccess(repository);
    }

    @Override
    public void onSuccessDelete() {
        this.view.onSuccessDelete();
    }

    @Override
    public void onNameEmptyError() {
        this.view.setNameEmptyError();
    }

    @Override
    public void setExerciseNotExistsError() {
        this.view.setExerciseNotExistsError();
    }

    @Override
    public void setDateExpError() {
        this.view.setDateExpError();
    }

    @Override
    public void onSuccessAdd() {
        this.view.onSuccesAdd();
    }

    @Override
    public void onSuccesModify() {
        this.view.onSuccesModify();
    }
}
