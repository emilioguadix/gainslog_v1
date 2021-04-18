package com.example.proyectofinal_deint_v1.ui.preferences.account;

import android.content.Context;
import android.graphics.Bitmap;

import com.example.proyectofinal_deint_v1.ui.base.BasePresenter;
import com.example.proyectofinal_deint_v1.ui.base.BaseView;

public interface AccountPrefContract {
    interface View extends BaseView{
        void onuIDEmptyError();
        void onNameEmptyError();
        void onConnectionDatabaseError();
        void onSucessUserUpdate();
        void onSuccesUserImgUpdate();
        void onFileNotFoundError();
        void onFirebaseImageUpdateError();
    }

    interface Presenter extends BasePresenter{
        void updateUserName(Context context, String uID, String name);
        void updateUserImage(Bitmap img);
    }
}
