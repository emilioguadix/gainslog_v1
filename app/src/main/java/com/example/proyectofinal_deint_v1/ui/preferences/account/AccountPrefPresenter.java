package com.example.proyectofinal_deint_v1.ui.preferences.account;

import android.content.Context;
import android.graphics.Bitmap;

public class AccountPrefPresenter implements AccountPrefContract.Presenter,AccountPrefInteractorImpl.AccountPrefInteractor{

    private AccountPrefContract.View view;
    private AccountPrefInteractorImpl interactor;

    public AccountPrefPresenter(AccountPrefContract.View view) {
        this.view = view;
        this.interactor = new AccountPrefInteractorImpl(this);
    }

    @Override
    public void updateUserName(Context context, String uID, String name) {
        this.interactor.updateUserName(context, uID, name);
    }

    @Override
    public void updateUserImage(Bitmap img) {
        this.interactor.updateUserImage(img);
    }

    @Override
    public void onDestroy() {
        this.view = null;
        this.interactor = null;
    }

    @Override
    public void setConnectionDatabaseError() {
        this.view.onConnectionDatabaseError();
    }

    @Override
    public void setuIDEmptyError() {
        this.view.onuIDEmptyError();
    }

    @Override
    public void setNameEmptyError() {
        this.view.onNameEmptyError();
    }

    @Override
    public void onSucessUserUpdate() {
        this.view.onSucessUserUpdate();
    }

    @Override
    public void onSuccesUserImgUpdate() {
        this.view.onSuccesUserImgUpdate();
    }

    @Override
    public void setFileNotFoundError() {
        this.view.onFileNotFoundError();
    }

    @Override
    public void setFirebaseImageUpdateError() {
        this.view.onFirebaseImageUpdateError();
    }
}
