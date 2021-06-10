package com.example.proyectofinal_deint_v1.ui.login;

import com.example.proyectofinal_deint_v1.data.model.model.user.User;
import com.example.proyectofinal_deint_v1.ui.main.GainslogMainActivity;
import com.example.proyectofinal_deint_v1.ui.signup.SingUpActivity;
import com.example.proyectofinal_deint_v1.ui.utils.CommonUtils;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.preference.PreferenceManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectofinal_deint_v1.R;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity implements LoginContract.View {
    //Campos
    private Button btnLogin;
    private TextInputLayout tilUser;
    private TextInputLayout tilPassword;
    private TextInputEditText tieUser;
    private TextInputEditText tiePassword;
    //Presenter con el que interactuará la vista
    private LoginPresenter presenter;
    private ProgressBar progressBar;
    private TextView btnPasswordForgot;
    private FirebaseAuth firebaseAuth;
    private SharedPreferences sharedPreferences;
    private String userEmail;
    private String userPassword;
    private CardView cvSigninGoogle;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        btnLogin = findViewById(R.id.btnLogin);
        progressBar = findViewById(R.id.progressBar);
        tilUser = findViewById(R.id.tilUser);
        tilPassword = findViewById(R.id.tilPassword);
        tieUser = findViewById(R.id.tieUser);
        tiePassword = findViewById(R.id.tiePassword);
        cvSigninGoogle = findViewById(R.id.cvSignGoogle);
        btnPasswordForgot = findViewById(R.id.passwordForgot);
        firebaseAuth = FirebaseAuth.getInstance();
        btnPasswordForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!tieUser.getText().toString().isEmpty()) {
                    firebaseAuth.sendPasswordResetEmail(firebaseAuth.getCurrentUser().getEmail()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(LoginActivity.this, getString(R.string.email_send_correctly), Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(LoginActivity.this, getString(R.string.email_send_incorrectly), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else{
                    Snackbar.make(findViewById(android.R.id.content), getResources().getString(R.string.err_need_email), Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        presenter = new LoginPresenter(this);
        autoLogin();
        //Lanzar errores en tiempo de ejecucion, cada vez que el usuario escriba
        tieUser.addTextChangedListener(new LoginTextWatcher(tieUser));
        tiePassword.addTextChangedListener(new LoginTextWatcher(tiePassword));

    }



    private void autoLogin(){
        userEmail =  sharedPreferences.getString(getString(R.string.key_user),"");
        userPassword =  sharedPreferences.getString(getString(R.string.key_password),"");
        if(userEmail != null && userPassword != null){
            tieUser.setText(userEmail);
            tiePassword.setText(userPassword);
        }
        presenter.validateCredentials(tieUser.getText().toString(), tiePassword.getText().toString());
    }

    public void onClick_logintoSing(View view) {
        startActivity(new Intent(LoginActivity.this, SingUpActivity.class));
    }

    public void onClick_login(View view) {
        presenter.validateCredentials(tieUser.getText().toString(), tiePassword.getText().toString());
    }

    @Override
    public void showProgress() {
        this.progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        this.progressBar.setVisibility(View.GONE);
    }

    @Override
    public void setAutenthicationError() {
        Snackbar.make(findViewById(android.R.id.content), getResources().getString(R.string.err_authentication), Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void setEmailNotVerifiedError() {
        Snackbar.make(findViewById(android.R.id.content), getResources().getString(R.string.err_email_notVerified), Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void setWebServConnectionError() {
        Snackbar.make(findViewById(android.R.id.content), getResources().getString(R.string.err_webserv_connection), Snackbar.LENGTH_SHORT).show();

    }

    //Método que se ejecuta cuando se obtiene el usuario de MYSQL(vps) gracias al UID de firebase
    @Override
    public void onSuccessUser(User user) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        //Se recoge el Editor de preferencias
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getString(R.string.key_user_name),user.get_name());
        editor.commit();
        hideSoftKeyboard();
        startActivity(new Intent(LoginActivity.this, GainslogMainActivity.class));
    }

    @Override
    public void setUserEmptyError() {
        tilUser.setError(getResources().getString(R.string.err_userEmpty));
        showSoftKeyboard(tieUser);
    }

    @Override
    public void setPasswordEmptyError() {
        tilPassword.setError(getResources().getString(R.string.err_passwordEmpty));
        showSoftKeyboard(tiePassword);
    }

    @Override
    public void setPasswordFormatError() {
        tilPassword.setError(getResources().getString(R.string.err_passwordFormat));
        showSoftKeyboard(tiePassword);
    }

    //CLASES Y MÉTODOS PARA LANZAR ERRORES EN TIEMPO DE EJECUCIÓN
        // --> Clase
    class LoginTextWatcher implements TextWatcher {
        private View view;

        LoginTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.tieUser:
                    validateUser(tieUser.getText().toString());
                    break;
                case R.id.tiePassword:
                    validatePassword(tiePassword.getText().toString());
                    break;
            }
        }
    }

        // --> Métodos
    private void validateUser(String userName){
        if(userName.isEmpty()){
           setUserEmptyError();
        }
        else{
            tilUser.setErrorEnabled(false);
        }
    }
    private void validatePassword(String password){
        if(password.isEmpty()){
            setPasswordEmptyError();
        }
        else{
            tilPassword.setErrorEnabled(false);
        }
    }

    //Si el presente lanza el metodo de onSuccess, pasar del login --> a la pantalla main de la aplicación
    @Override
    public void onSucess() {
        //Guardamos las preferencias del usuario
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        //Se recoge el Editor de preferencias
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getString(R.string.key_user),tieUser.getText().toString());
        editor.putString(getString(R.string.key_password),tiePassword.getText().toString());
        editor.putString(getString(R.string.key_password),tiePassword.getText().toString());
        editor.commit();
        //Obtener el usuario de mysql para tener todos los datos completos.
        presenter.getUser(LoginActivity.this, FirebaseAuth.getInstance().getCurrentUser().getUid());
    }

    public void showSoftKeyboard(View view) {
        view.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    public void hideSoftKeyboard(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }
}