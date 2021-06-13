package com.example.proyectofinal_deint_v1.ui.login;

import android.content.Context;

import com.example.proyectofinal_deint_v1.data.model.model.user.Request;
import com.example.proyectofinal_deint_v1.data.model.model.user.User;

import java.util.List;

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

    @Override
    public void updateToken(Context context, String userUID) {
        this.interactor.updateToken(context, userUID);
    }

    @Override
    public void sendRequestCoach(Context context, String emailCoach,boolean permission) {
        this.interactor.sendRequestCoach(context,emailCoach,permission);
    }

    @Override
    public void getRequestList(Context context) {
        this.interactor.listRequest(context);
    }

    @Override
    public void updateRequest(Context context, Request request1) {
        this.interactor.updateRequest(context, request1);
    }

    @Override
    public void deleteRequest(Context context, Request request1) {
        this.interactor.deleteRequest(context, request1);
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
    public void onSuccessUpdate() {
        this.view.onSuccessUpdate();
    }

    @Override
    public void onSuccessDelete(Request request) {
        this.view.onSuccessDelete(request);
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
    public void onSuccessRequest(List<Request> list) {
        this.view.onSuccessRequest(list);
    }

    @Override
    public void onDestroy() {
        view.hideProgress();
        this.view = null;
        this.interactor = null;
    }
}
