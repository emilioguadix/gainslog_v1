package com.example.proyectofinal_deint_v1.ui.signup;

import android.content.Context;

import com.example.proyectofinal_deint_v1.ui.base.BasePresenter;
import com.example.proyectofinal_deint_v1.ui.user.UserViewSign;

public interface SignUpContract {

    interface View extends UserViewSign {
        void showProgress();
        void hideProgress();
    }

    //Método para añadir el usuario al repositorio de usuarios
    interface Presenter extends BasePresenter {
        void addUser(Context context, String userName, String password, String confirmPassword, String email, int typeUser);
    }
}
