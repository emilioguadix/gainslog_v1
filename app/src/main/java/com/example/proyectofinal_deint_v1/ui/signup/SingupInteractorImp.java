package com.example.proyectofinal_deint_v1.ui.signup;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.proyectofinal_deint_v1.data.model.model.user.User;
import com.example.proyectofinal_deint_v1.ui.utils.CommonUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.Map;

public class SingupInteractorImp implements SignUpContract.Presenter {

    private SigunInteractor presenter;
    private FirebaseAuth uAuth;

    //Métodos que lanzarás al presenter
    interface SigunInteractor{
        void onUserEmptyError();
        void onUserFireBaseError();
        void onPasswordEmptyError();
        void onPasswordFormatError();
        void onPasswordConfirmError();
        void onEmailEmptyError();
        void onEmailFormatError();
        void onSuccess();
    }

    //Constructor
    public SingupInteractorImp(SigunInteractor presenter){this.presenter = presenter; uAuth = FirebaseAuth.getInstance();}

    @Override
    public void addUser(final Context context, final String userName, final String password, final String confirmPassword, final String email, final int typeUser) {

                if (TextUtils.isEmpty(userName)) {
                    presenter.onUserEmptyError();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    presenter.onPasswordEmptyError();
                    return;
                }
                //Comprobar el formato de la contraseña-
                if (!CommonUtils.isPasswordValid(password)) {
                    presenter.onPasswordFormatError();
                    return;
                }
                if (!confirmPassword.equals(password)) {
                    presenter.onPasswordConfirmError();
                    return;
                }
                if (TextUtils.isEmpty(email)) {
                    presenter.onEmailEmptyError();
                    return;
                }
                if (!CommonUtils.isEmailValid(email)) {
                    presenter.onEmailFormatError();
                    return;
                }
                //Finalmente si se cumple todos los requisitos añadimos el usuario a firebase.
                uAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    FirebaseUser user = uAuth.getCurrentUser();
                                    user.sendEmailVerification();
                                    //insertar el usuario en la tabla MYSQL, mediante web service
                                    insertUserData(context,userName,user.getUid(),(typeUser == 2)?true:false,email);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    presenter.onUserFireBaseError();
                                }
                            }
                        });
    }

    private void insertUserData(Context context,String user_name,String user_id, boolean coach,String email){
        String URL = "http://vps-3c722567.vps.ovh.net/GainsLog/crud/user/insertar.php";
        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                presenter.onSuccess();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                presenter.onUserFireBaseError();
            }

        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String,String>();
                params.put("id",user_id);
                params.put("coach",coach ? "1" : "0");
                params.put("name",user_name);
                params.put("email",email);


                return params;
            }
        };
        Volley.newRequestQueue(context).add(request);
    }

    @Override
    public void onDestroy() {
    }


}
