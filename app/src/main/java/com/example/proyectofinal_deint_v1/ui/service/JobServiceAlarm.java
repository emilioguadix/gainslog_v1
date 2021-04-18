package com.example.proyectofinal_deint_v1.ui.service;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.util.Log;

//Este servicio manda  un intent al BroadcatReceiver después de un tiempo programado que realiza una acción determinada dentro de la aplicación.
public class JobServiceAlarm extends JobService {

    private static final String TAG = "JobService";
    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d(TAG,"Job Started...");

        Intent intent = new Intent("com.example.temporizadoractivity_intent");
        sendBroadcast(intent);
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.d(TAG,"Job Cancelled...");
        return false;
    }
}
