package com.example.proyectofinal_deint_v1.ui.main;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavController;
import androidx.navigation.NavDeepLinkBuilder;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.PreferenceManager;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.proyectofinal_deint_v1.GainsLogApplication;
import com.example.proyectofinal_deint_v1.ui.coacinfo.JobServiceEjemplo;
import com.example.proyectofinal_deint_v1.ui.notify.MyFirebaseMessagingService;
import com.example.proyectofinal_deint_v1.R;
import com.example.proyectofinal_deint_v1.ui.login.LoginActivity;
import com.example.proyectofinal_deint_v1.ui.utils.CommonUtils;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;

public class GainslogMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Thread t;

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private NavController navController;
    private AppBarConfiguration appBarConfiguration;
    private CircleImageView circleImageView;
    private SharedPreferences sharedPreferences;
    private TextView userName;
    private TextView userEmail;
    private final static int ID_SERVICIO = 99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        navController = Navigation.findNavController(this,R.id.nav_host_fragment);
        navigationView.setNavigationItemSelectedListener(this);
        setSupportActionBar(toolbar);
        //Vincular con Navigation Controler
        //Al poner varios fragment en top.level, aparecerá el icono hamburguesa en todos ellos podemoss acceder al menú principal.
        Set<Integer> topLevelDestinations = new HashSet<>();
        topLevelDestinations.add(R.id.homeFragment);
        topLevelDestinations.add(R.id.boxDataFragment);
        topLevelDestinations.add(R.id.coachFragment);
        topLevelDestinations.add(R.id.chartBoxFragment);
        appBarConfiguration = new AppBarConfiguration.Builder(topLevelDestinations)
                .setOpenableLayout(drawerLayout)
                .build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        //Actualización de la info del usuario de firebase a la interfaz
        View headerView = navigationView.getHeaderView(0);
        circleImageView = headerView.findViewById(R.id.profile_image);
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(GainslogMainActivity.this,R.id.nav_host_fragment).navigate(R.id.settingsPreferences);
            }
        });
        updateUIProfileUser();
        setUser();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void showNotification(String cuerpo, String titulo) {
        PendingIntent pendingIntent =new NavDeepLinkBuilder(this)
                .setGraph(R.navigation.new_graph)
                .setDestination(R.id.coachFragment)
                .createPendingIntent();
        Notification.Builder builder = new Notification.Builder(this, GainsLogApplication.CHANNEL_ID)
                .setAutoCancel(true)
                .setSound(Uri.parse(""))
                .setSmallIcon(R.mipmap.ic_launcher_gainslogger)
                .setContentTitle(titulo)
                .setContentText(cuerpo)
                .setContentIntent(pendingIntent);
        //Se añade la notificación creada, al gestor de notificaciones
        NotificationManager notificationManager = (NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE);
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

    private void setUser(){
        //Una vez iniciada sesión configuramos los datos de la cabecera con los del usuario logeado.
        View headerView = navigationView.getHeaderView(0);
        userName = headerView.findViewById(R.id.username);
        userEmail = headerView.findViewById(R.id.email);
        //Asignamos los datos del usuario
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        userEmail.setText(sharedPreferences.getString(getString(R.string.key_user),""));
        userName.setText(sharedPreferences.getString(getString(R.string.key_user_name),""));
        //--------------------------------------------------------------------------------------

    }

    @Override
    protected void onStart() {
        super.onStart();
            ComponentName componentName = new ComponentName(getApplicationContext(), JobServiceEjemplo.class);
            JobInfo info;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                info = new JobInfo.Builder(ID_SERVICIO, componentName)
                        .setRequiresCharging(true)
                        .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                        .setPersisted(true)
                        .setMinimumLatency(5 * 1000)
                        .build();
            } else {
                info = new JobInfo.Builder(ID_SERVICIO, componentName)
                        .setRequiresCharging(true)
                        .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                        .setPersisted(true)
                        .setPeriodic(5 * 1000)
                        .build();
            }
            JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
            int resultado = scheduler.schedule(info);
            if (resultado == JobScheduler.RESULT_SUCCESS) {
                Log.d("TAG", "Job Acabado");
            } else {
                Log.d("TAG", "Job ha fallado");
            }
    }

    private void updateUIProfileUser(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            if (user.getPhotoUrl() != null) {
                Glide.with(this)
                        .load(user.getPhotoUrl())
                        .into(circleImageView);
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isOpen()){
            drawerLayout.close();
        }
        else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.close();
        switch(item.getItemId()){
            case R.id.action_home:
                Navigation.findNavController(this,R.id.nav_host_fragment).navigate(R.id.homeFragment);
                break;
            case R.id.action_boxData:
                Navigation.findNavController(this,R.id.nav_host_fragment).navigate(R.id.boxDataFragment);
                break;
            case R.id.action_trainer:
                Navigation.findNavController(this,R.id.nav_host_fragment).navigate(R.id.coachFragment);
                break;
            case R.id.action_settings:
                Navigation.findNavController(this,R.id.nav_host_fragment).navigate(R.id.settingsPreferences);
                break;
            case R.id.action_aboutus:
                Navigation.findNavController(this,R.id.nav_host_fragment).navigate(R.id.aboutFragment);
                break;
            case R.id.action_chart:
                Navigation.findNavController(this,R.id.nav_host_fragment).navigate(R.id.chartBoxFragment);
                break;
            case R.id.action_close_session:
                //Limpiar las preferencias(userEmail,userPass) y navegar hacia LoginActivity -->
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(getString(R.string.key_user),"");
                editor.putString(getString(R.string.key_password),"");
                editor.commit();
                startActivity(new Intent(GainslogMainActivity.this, LoginActivity.class));
                break;
        }
        return true;
    }
}