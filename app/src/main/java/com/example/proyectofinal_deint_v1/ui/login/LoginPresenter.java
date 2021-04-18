package com.example.proyectofinal_deint_v1.ui.login;

import android.content.Context;

import com.example.proyectofinal_deint_v1.data.model.model.user.User;

public class LoginPresenter implements LoginContract.Presenter, LoginInteractorImpl.LoginInteractor {
    private LoginContract.View view;
    private LoginInteractorImpl interactor;

    //Constructor, necesita la vista con la que este interaturará.
    public LoginPresenter(LoginContract.View view){
        this.view = view;
        this.interactor = new LoginInteractorImpl(this);
    }
    //Presenter
    @Override
    public void validateCredentials(String user, String password) {
        view.showProgress();
        interactor.validateCredentials(user,password);
    }

    @Override
    public void getUser(Context context, String userUID) {
        this.view.showProgress();
        this.interactor.getUser(context, userUID);
    }

    //Métodos del interactor
    @Override
    public void onUserEmptyError() {
        view.hideProgress();
        view.setUserEmptyError();
    }

    @Override
    public void onPasswordEmptyError() {
        view.hideProgress();
        view.setPasswordEmptyError();
    }

    @Override
    public void onPasswordFormatError() {
        view.hideProgress();
        view.setPasswordFormatError();

    }

    @Override
    public void onAuthenticationError() {
        view.hideProgress();
        view.setAutenthicationError();
    }

    @Override
    public void onEmailNotVerifiedError() {
        this.view.hideProgress();
        this.view.setEmailNotVerifiedError();
    }

    @Override
    public void onSuccess() {
        view.hideProgress();
        view.onSucess();
    }

    @Override
    public void onWebServConnectionError() {
        this.view.hideProgress();
        this.view.setWebServConnectionError();
    }

    @Override
    public void onSuccessUser(User user) {
        this.view.hideProgress();
        this.view.onSuccessUser(user);
    }

    @Override
    public void onDestroy() {
        view.hideProgress();
        this.view = null;
        this.interactor = null;
    }
}
