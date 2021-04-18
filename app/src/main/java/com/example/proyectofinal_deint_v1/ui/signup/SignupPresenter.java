package com.example.proyectofinal_deint_v1.ui.signup;

import android.content.Context;

public class SignupPresenter implements SignUpContract.Presenter, SingupInteractorImp.SigunInteractor {
    private SignUpContract.View view;
    private SingupInteractorImp interactor;

    //Constructor
    public SignupPresenter(SignUpContract.View view){
        this.view = view;
        this.interactor = new SingupInteractorImp(this);
    }


    @Override
    public void addUser(Context context, String userName, String password, String confirmPassword, String email, int typeUser) {
        interactor.addUser(context,userName,password,confirmPassword,email,typeUser);
    }

    @Override
    public void onUserEmptyError() {
        view.hideProgress();
        view.setUserEmptyError();
    }

    @Override
    public void onUserFireBaseError() {
        view.hideProgress();
        view.setUserFireBaseError();
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
    public void onPasswordConfirmError() {
        view.hideProgress();
        view.setConfirmPasswordError();
    }

    @Override
    public void onEmailEmptyError() {
        view.hideProgress();
        view.setEmailEmptyError();
    }

    @Override
    public void onEmailFormatError() {
        view.hideProgress();
        view.setEmailFormatError();
    }

    @Override
    public void onSuccess() {
        view.hideProgress();
         view.onSucess();
    }

    @Override
    public void onDestroy() {
        this.view = null;
        this.interactor = null;
    }
}
