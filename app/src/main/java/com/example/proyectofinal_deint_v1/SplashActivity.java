package com.example.proyectofinal_deint_v1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

import com.example.proyectofinal_deint_v1.ui.login.LoginActivity;

public class SplashActivity extends AppCompatActivity {
    private static final long WAIT_TIME = 2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    private void iniciarLogin(){
        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
        //Destruimos la actividad splash, de esta manera, si el usuario quiere acceder hacia atrás no podrá.
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Para que ejecute una pequeña pausa de 2 segundos, que serán utilizados para estables las conexiones
        //al servidor pertinente, dondé se encuentran almacenados los datos.
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                iniciarLogin();
            }
        },WAIT_TIME);
    }
}