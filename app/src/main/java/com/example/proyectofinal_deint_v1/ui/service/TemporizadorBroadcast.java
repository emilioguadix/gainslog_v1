package com.example.proyectofinal_deint_v1.ui.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.proyectofinal_deint_v1.ui.boxData.target.EditTargetFragment;
import com.example.proyectofinal_deint_v1.ui.boxData.target.TargetListFragment;
import com.example.proyectofinal_deint_v1.ui.main.GainslogMainActivity;

public class TemporizadorBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //Se inicia un servicio
        //Se lanza una notificación
        //Se abre una actividad de nuestra aplicación
        Intent intentActivity = new Intent(context, GainslogMainActivity.class);
        intentActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intentActivity);
    }
}
