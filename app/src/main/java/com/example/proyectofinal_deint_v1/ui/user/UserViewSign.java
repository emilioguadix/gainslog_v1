package com.example.proyectofinal_deint_v1.ui.user;

import com.example.proyectofinal_deint_v1.ui.base.BaseView;

    public interface UserViewSign extends BaseView{
        void setUserEmptyError();
        void setUserFireBaseError();
        void setPasswordEmptyError();
        void setPasswordFormatError();
        void setConfirmPasswordError();
        void setEmailEmptyError();
        void setEmailFormatError();
    }
