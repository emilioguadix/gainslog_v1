package com.example.proyectofinal_deint_v1.ui.login;

import android.content.Context;

import com.example.proyectofinal_deint_v1.data.model.model.user.Request;
import com.example.proyectofinal_deint_v1.data.model.model.user.User;
import com.example.proyectofinal_deint_v1.ui.base.BasePresenter;
import com.example.proyectofinal_deint_v1.ui.user.UserView;

import java.util.List;

public interface LoginContract {


    interface View extends UserView{
        //Método que muestra una barra de progreso mientras que el interactor realiza la comprobación
        void showProgress();

        void hideProgress();

        //Método que muestra un error indicando que la autenticación no fue la correcta.
        void setAutenthicationError();
        void setEmailNotVerifiedError();
        void setWebServConnectionError();
        void onSuccessUser(User user);
        void onSuccessDelete(com.example.proyectofinal_deint_v1.data.model.model.user.Request request);
        void onSuccessUpdate();
        void onSuccessRequest(List<Request> list);
    }

    interface Presenter extends BasePresenter{
        //Validará al usuario al entrar en el login.
        void validateCredentials(String user, String password);
        void getUser(Context context,String userUID);
        void  updateToken(Context context, String userUID);
        void  sendRequestCoach(Context context,String emailCoach);
        void getRequestList(Context context);
        void updateRequest(Context context, Request request1);
        void deleteRequest(Context context, Request request1);
    }
}
