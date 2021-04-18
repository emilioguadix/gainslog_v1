package com.example.proyectofinal_deint_v1.ui.main;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.proyectofinal_deint_v1.R;
import com.example.proyectofinal_deint_v1.SplashActivity;
import com.example.proyectofinal_deint_v1.ui.login.LoginActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashSet;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;

public class GainslogMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private NavController navController;
    private AppBarConfiguration appBarConfiguration;
    private CircleImageView circleImageView;
    int TAKE_IMAGE_CODE = 10001;

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
        topLevelDestinations.add(R.id.boxDataFragment);
        topLevelDestinations.add(R.id.coachFragment);
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
                //Navigation.findNavController(this,R.id.nav_host_fragment_container).navigate(R.id.BoxDataFragment);
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