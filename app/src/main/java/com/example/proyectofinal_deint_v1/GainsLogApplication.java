package com.example.proyectofinal_deint_v1;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;

import androidx.preference.PreferenceManager;

import com.example.proyectofinal_deint_v1.ui.service.TemporizadorBroadcast;

public class GainsLogApplication extends Application {

    public static final String CHANNEL_ID="123";
    private SharedPreferences sharedPreferences;

    @Override
    public void onCreate() {
        super.onCreate();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        //Configuración del Servicio
        TemporizadorBroadcast temporizadorBroadcast = new TemporizadorBroadcast();
        IntentFilter intentFilter = new IntentFilter("com.example.temporizadoractivity_intent");
        registerReceiver(temporizadorBroadcast,intentFilter,null,null);
        //InventoryPreferences.newInstance(this);
        //Creamos el canal de notificaciones
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        //Vamos a crear la clase NotificationChannel pero sólo para
        //API mayor que 26, porque la clase NotificationChannel no está en la librería de soporte.
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            CharSequence name = getString(R.string.channel_name);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            //Se registra el canal en el sistema, y una vez que se ha registrado no se puede cambiar
            //la importancia o bien otra configuración que se haya establecido.
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

}
