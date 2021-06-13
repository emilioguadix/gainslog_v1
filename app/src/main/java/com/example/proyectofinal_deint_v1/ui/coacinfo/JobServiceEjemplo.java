package com.example.proyectofinal_deint_v1.ui.coacinfo;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.navigation.NavDeepLinkBuilder;
import androidx.preference.PreferenceManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.proyectofinal_deint_v1.GainsLogApplication;
import com.example.proyectofinal_deint_v1.R;
import com.example.proyectofinal_deint_v1.data.model.model.user.Request;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class JobServiceEjemplo extends JobService {

        private boolean jobCancelled = false;
        private  static int count = 0;
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public boolean onStartJob(JobParameters jobParameters) {
            Log.d("TAG", "onStartJob");
            doBackWork(jobParameters);
            return true;
        }

        private void doBackWork(final JobParameters jobParameters){
            Log.d("TAG", "doBackWork");
            new Thread(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void run() {
                    while(!jobCancelled){
                        comprobarPeticiones();
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    Log.d("TAG", "Job Fisnished");
                    jobFinished(jobParameters, false);
                }
            }).start();
        }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void comprobarPeticiones() {
        count = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getInt(getString(R.string.key_coach_count_request),0);
        listRequest(getApplicationContext());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void showNotification(){
        PendingIntent pendingIntent =new NavDeepLinkBuilder(getApplicationContext())
                .setGraph(R.navigation.new_graph)
                .setDestination(R.id.coachFragment)
                .createPendingIntent();
        Notification.Builder builder = new Notification.Builder(getApplicationContext(), GainsLogApplication.CHANNEL_ID)
                .setAutoCancel(true)
                .setSound(Uri.parse(""))
                .setSmallIcon(R.mipmap.ic_launcher_gainslogger)
                .setContentTitle(getResources().getString(R.string.tit_not_coach))
                .setContentText(getResources().getString(R.string.body_not_coach));
                //.setContentIntent(pendingIntent);
        //Se añade la notificación creada, al gestor de notificaciones
        NotificationManager notificationManager = (NotificationManager)getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(new Random().nextInt(1000),builder.build());
        playNotificationSound();
    }

    private void playNotificationSound() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        try {
            Uri path = Uri. parse (ContentResolver. SCHEME_ANDROID_RESOURCE + "://" + getApplicationContext().getPackageName() + "/raw/" + sharedPreferences.getString(getString(R.string.key_tone),""));
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(),path);
            if(sharedPreferences.getBoolean(getString(R.string.key_notifications),true)){
                r.play();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
        public boolean onStopJob(JobParameters jobParameters) {
            Log.d("TAG", "onStopJob");
            jobCancelled = true;
            return false;
        }

    public void listRequest(Context context){
        List<Request> list = new ArrayList<>();
        String URL = "http://vps-3c722567.vps.ovh.net/GainsLog/crud/firebase/listRequest.php";
        StringRequest request = new StringRequest(com.android.volley.Request.Method.POST, URL, new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                        if(jsonArray.length() > count){
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
                            editor.putInt(getString(R.string.key_coach_count_request),jsonArray.length());
                            editor.apply();
                            editor.commit();
                            showNotification();
                        }
                } catch (JSONException exception) {
                    exception.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }

        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String,String>();
                params.put("fb_id", FirebaseAuth.getInstance().getCurrentUser().getUid());
                return params;
            }
        };
        Volley.newRequestQueue(context).add(request);

    }

    }
