package com.example.proyectofinal_deint_v1.ui.preferences.account;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.Preference;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.proyectofinal_deint_v1.R;
import com.example.proyectofinal_deint_v1.data.model.model.user.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class AccountPrefInteractorImpl {

    private AccountPrefInteractor callback;

    public AccountPrefInteractorImpl(AccountPrefInteractor callback) {
        this.callback = callback;
    }

    public interface AccountPrefInteractor{
        void setConnectionDatabaseError();
        void setuIDEmptyError();
        void setNameEmptyError();
        void onSucessUserUpdate();
        void onSuccesUserImgUpdate();
        void setFileNotFoundError();
        void setFirebaseImageUpdateError();
    }

    public void updateUserImage(Bitmap img) {
        if(img != null){
            handleUpload(img);
        }
        else{
            callback.setFileNotFoundError();
            return;
        }
    }

    //MÉTODO PARA ACTUALIZAR EL NOMBRE DE USUARIO
    public void updateUserName(Context context, String uID, String name){
        if(uID.isEmpty()){
            callback.setuIDEmptyError();
            return;
        }
        if(name.isEmpty()){
            callback.setNameEmptyError();
            return;
        }
        updateName(context, uID, name);

    }

    private void updateName(Context context, String userUID,String newName){
        String URL = "http://vps-3c722567.vps.ovh.net/GainsLog/crud/user/updateUserName.php";
        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    Gson gson = new Gson();
                    User userTmp = gson.fromJson(jsonObject.toString(),User.class);
                    if(userTmp != null && userTmp.get_name().equals(newName)){
                        callback.onSucessUserUpdate();
                    }
                    else{
                        callback.setConnectionDatabaseError();
                    }
                }
                catch (JSONException exception){
                    exception.printStackTrace();
                    callback.setConnectionDatabaseError();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                callback.setConnectionDatabaseError();
            }

        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String,String>();
                params.put("id",userUID);
                params.put("name",newName);
                return params;
            }
        };
        Volley.newRequestQueue(context).add(request);
    }

    //MÉTODOS PARA CONTROLAR EL CAMBIO DE LA IMAGEN DE PERFIL

    //Método que una vez tomada la nueva foto la transforma para poder subirla a firebase storage(byte a byte).
    private void handleUpload(Bitmap bitmap) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final StorageReference reference = FirebaseStorage.getInstance().getReference()
                .child("profileImages")
                .child(uid + ".jpeg");

        reference.putBytes(baos.toByteArray())
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        getDownloadUrl(reference);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    //Método que una vez subida la foto al storage de firebase obtiene el enlace para luego usar ese enlace
    private void getDownloadUrl(StorageReference reference) {
        reference.getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        setUserProfileUrl(uri);
                    }
                });
    }

    //Método que actualiza la imagen del usuario en firebase.
    private void setUserProfileUrl(Uri uri) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                .setPhotoUri(uri)
                .build();

        user.updateProfile(request)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        callback.onSuccesUserImgUpdate();
                        return;
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.setFirebaseImageUpdateError();
                        return;
                    }
                });
    }

    //---------------------------------------------------------------------------------------------------------------
}
