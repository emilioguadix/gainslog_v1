package com.example.proyectofinal_deint_v1.ui.user;

import com.example.proyectofinal_deint_v1.ui.base.BaseView;

//En esta interfaz se definen los errores que podrán ocurrir en el login del usuario.
public interface UserView extends BaseView {
    void setUserEmptyError();
    void setPasswordEmptyError();
    void setPasswordFormatError();
}
