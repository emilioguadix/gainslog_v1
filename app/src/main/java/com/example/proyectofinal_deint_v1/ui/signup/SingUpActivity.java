package com.example.proyectofinal_deint_v1.ui.signup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.proyectofinal_deint_v1.R;
import com.example.proyectofinal_deint_v1.data.model.model.user.TypeUser;
import com.example.proyectofinal_deint_v1.ui.login.LoginActivity;
import com.example.proyectofinal_deint_v1.ui.main.GainslogMainActivity;
import com.example.proyectofinal_deint_v1.ui.utils.CommonUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SingUpActivity extends AppCompatActivity implements SignUpContract.View{

    //Campos
    private ProgressBar progressBar;
    private TextInputLayout tilUser;
    private TextInputEditText tieUser;
    private TextInputLayout tilPassword;
    private TextInputEditText tiePassword;
    private TextInputLayout tilConfirmPassword;
    private TextInputEditText tieConfirmPassword;
    private TextInputLayout tilEmail;
    private TextInputEditText tieEmail;
    private CheckBox chxCoachUser;
    private Button btnSignUp;
    private FirebaseAuth uAuth;

    private SignupPresenter presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_sing_up);
        progressBar = findViewById(R.id.progressBarSign);
        tilUser = findViewById(R.id.tilUser);
        tieUser = findViewById(R.id.tieUser);
        tilPassword = findViewById(R.id.tilPassword);
        tiePassword = findViewById(R.id.tiePassword);
        tilConfirmPassword = findViewById(R.id.tilConfirmPassword);
        tieConfirmPassword = findViewById(R.id.tieConfirmPassword);
        tilEmail = findViewById(R.id.tilEmail);
        tieEmail = findViewById(R.id.tieEmail);
        chxCoachUser = findViewById(R.id.chx_beTrainer);
        btnSignUp = findViewById(R.id.btnSingUp);

        //Corregir campos en tiempo de ejecución
        tieUser.addTextChangedListener(new SingUpTextWatcher(tieUser));
        tiePassword.addTextChangedListener(new SingUpTextWatcher(tiePassword));
        tieConfirmPassword.addTextChangedListener(new SingUpTextWatcher(tieConfirmPassword));
        tieEmail.addTextChangedListener(new SingUpTextWatcher(tieEmail));

        presenter = new SignupPresenter(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public void onClick_showHelpCoach(View view) {
        String url = "http://gainslog.azurewebsites.net/index.php/";

        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    public void onClick_SingUp(View view) {
        showProgress();
        int typeUser = (chxCoachUser.isChecked()) ? TypeUser.COACH : TypeUser.NORMAL;
        presenter.addUser(SingUpActivity.this,tieUser.getText().toString(), tiePassword.getText().toString(),tieConfirmPassword.getText().toString(),tieEmail.getText().toString(),typeUser);

    }

    @Override
    public void showProgress() {
        btnSignUp.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        btnSignUp.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    //CLASES Y MÉTODOS PARA LANZAR ERRORES EN TIEMPO DE EJECUCIÓN
    // --> Clase
    class SingUpTextWatcher implements TextWatcher {
        private View view;

        SingUpTextWatcher(View view) {
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
                case R.id.tieConfirmPassword:
                    validateConfirmPassword(tieConfirmPassword.getText().toString());
                    break;
                case R.id.tieEmail:
                    validateEmail(tieEmail.getText().toString());
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
        else if(!CommonUtils.isPasswordValid(password)){
            setPasswordFormatError();
        }
        else{
            tilPassword.setErrorEnabled(false);
        }
    }
    private void validateConfirmPassword(String confirmPassword){
        if(!confirmPassword.equals(tiePassword.getText().toString())){
            setConfirmPasswordError();
        }
        else{
            tilPassword.setErrorEnabled(false);
        }
    }
    private void validateEmail(String email){
        if(!CommonUtils.isEmailValid(email)){
            setEmailFormatError();
        }
        else if(email.isEmpty()){
            setEmailEmptyError();
        }
        else{
            tilEmail.setErrorEnabled(false);
        }
    }

    @Override
    public void setUserEmptyError() {
        tieUser.setError(getResources().getString(R.string.err_userEmpty));
        showSoftKeyboard(tilUser);
    }

    @Override
    public void setUserFireBaseError() {
        Snackbar.make(findViewById(android.R.id.content),getResources().getString(R.string.err_user_fb_error),Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void setPasswordEmptyError() {
        tiePassword.setError(getResources().getString(R.string.err_passwordEmpty));
        showSoftKeyboard(tilPassword);
    }

    @Override
    public void setPasswordFormatError() {
        tiePassword.setError(getResources().getString(R.string.err_passwordFormat));
        showSoftKeyboard(tilPassword);
    }

    @Override
    public void setConfirmPasswordError() {
        tieConfirmPassword.setError(getResources().getString(R.string.err_confirmPassword));
        showSoftKeyboard(tilConfirmPassword);
    }

    @Override
    public void setEmailEmptyError() {
        tieEmail.setError(getResources().getString(R.string.err_emailEmpty));
        showSoftKeyboard(tilEmail);
    }

    @Override
    public void setEmailFormatError() {
        tieEmail.setError(getResources().getString(R.string.err_emailFormat));
        showSoftKeyboard(tilEmail);
    }


    public void showSoftKeyboard(View view) {
        view.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    @Override
    public void onSucess() {
        startActivity(new Intent(SingUpActivity.this, LoginActivity.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }
}