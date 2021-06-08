package com.example.proyectofinal_deint_v1.ui.login;

import android.content.Context;
import android.os.Handler;
import android.telephony.gsm.GsmCellLocation;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.proyectofinal_deint_v1.data.model.model.user.TypeUser;
import com.example.proyectofinal_deint_v1.data.model.model.user.User;
import com.example.proyectofinal_deint_v1.ui.utils.CommonUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

//EL interactor, recibirá los datos desde la vista/presenter, e irá al repositorio a hacer las comprobaciones
public class LoginInteractorImpl {
    private LoginInteractor presenter;
    private FirebaseAuth uAuth;

    public interface LoginInteractor{
        void onUserEmptyError();
        void onPasswordEmptyError();
        void onPasswordFormatError();
        void onAuthenticationError();
        void onEmailNotVerifiedError();
        void onSuccess();
        void onWebServConnectionError();
        void onSuccessUser(User user);
    }

    public  LoginInteractorImpl(LoginInteractor presenter){this.presenter = presenter; uAuth = FirebaseAuth.getInstance();}

    //Método para hacer las comprobaciones de los datos introducidos por el usuario, y luego ir a validar el usuario al repositorio.
    public void validateCredentials(final String email, final String password){
                if (TextUtils.isEmpty(email)) {
                    presenter.onUserEmptyError();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    presenter.onPasswordEmptyError();
                    return;
                }
                //Comprobar si las credenciales del usuario son las correctas
                uAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    FirebaseUser user = uAuth.getCurrentUser();
                                    if(user.isEmailVerified()){
                                        presenter.onSuccess();
                                        return;
                                    }
                                    else{
                                        presenter.onEmailNotVerifiedError();
                                    }
                                } else {
                                    // If sign in fails, display a message to the user.
                                    presenter.onAuthenticationError();
                                }
                            }
                        });
    }

    public void getUser(Context context, String userUID){
        String URL = "http://vps-3c722567.vps.ovh.net/GainsLog/crud/user/getUserName.php";
        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    Gson gson = new Gson();
                    User userTmp = gson.fromJson(jsonObject.toString(),User.class);
                    if(userTmp != null){
                        presenter.onSuccessUser(userTmp);
                    }
                }
                catch (JSONException exception){
                    exception.printStackTrace();
                    presenter.onWebServConnectionError();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                presenter.onWebServConnectionError();
            }

        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String,String>();
                params.put("id",userUID);
                return params;
            }
        };
        Volley.newRequestQueue(context).add(request);
    }
}
