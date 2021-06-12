package com.example.proyectofinal_deint_v1.ui.notify;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavDeepLinkBuilder;
import androidx.preference.PreferenceManager;

import com.example.proyectofinal_deint_v1.ui.login.LoginActivity;
import com.example.proyectofinal_deint_v1.ui.main.GainslogMainActivity;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;
import java.util.Random;

public class  MyFirebaseMessagingService extends FirebaseMessagingService {

    public static final String ACTION_MESSAGE_RECEIVED = "ACTION_MESSAGE_RECEIVED";

    @Override
    public void onMessageReceived(RemoteMessage mssg) {
        Intent intent = new Intent(ACTION_MESSAGE_RECEIVED); // put extra vars as needed
        boolean delivered = LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
        // 'delivered' is true if there is at least someone listening to the broadcast, eg. your activity
        // If your activity is not running, then 'delivered' is false so you can act accordingly
    }

}
