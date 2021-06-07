package com.example.proyectofinal_deint_v1.ui.preferences.account;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.bumptech.glide.Glide;
import com.example.proyectofinal_deint_v1.R;
import com.example.proyectofinal_deint_v1.ui.login.LoginActivity;
import com.example.proyectofinal_deint_v1.ui.main.GainslogMainActivity;
import com.example.proyectofinal_deint_v1.ui.utils.CommonUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class AccountPreferences extends PreferenceFragmentCompat implements AccountPrefContract.View{

    private FirebaseAuth firebaseAuth;
    private AccountPrefContract.Presenter presenter;
    private EditTextPreference preferenceUserName;
    private String userName;
    private String newUserName;
    int TAKE_IMAGE_CODE = 10002;
    private SharedPreferences sharedPreferences;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        firebaseAuth = FirebaseAuth.getInstance();
        presenter = new AccountPrefPresenter(this);
        addPreferencesFromResource(R.xml.account_preference);
        initUserPreferences();
        initPasswordPreferences();
        initUserImgPreferences();
    }

    //MÉTODOS PARA CONTROLAR EL CAMBIO DE LA IMAGEN DE PERFIL

    private void initUserImgPreferences() {
        Preference prefereces = findPreference(getString(R.string.key_user_imgProfile));
        prefereces.setOnPreferenceClickListener (new Preference.OnPreferenceClickListener(){
            public boolean onPreferenceClick(Preference preference){
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), TAKE_IMAGE_CODE);
                return true;
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TAKE_IMAGE_CODE) {
            switch (resultCode) {
                case RESULT_OK:
                    //SI ha ido bien debemos obtener esa imagen y ademas llamar a un método para subirla a firebase
                    Uri uri = data.getData();
                    //Transformar la uri de la img a bitmap para subirlo a firebase
                    InputStream imageStream = null;
                    try {
                        imageStream = getActivity().getContentResolver().openInputStream(
                                uri);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(),getString(R.string.err_img_file),Toast.LENGTH_SHORT).show();
                    }
                    // Transformamos la URI de la imagen a inputStream y este a un Bitmap
                    Bitmap bmp = BitmapFactory.decodeStream(imageStream);
                    presenter.updateUserImage(bmp);
                    Toast.makeText(getContext(),getString(R.string.img_profile_uploading),Toast.LENGTH_LONG).show();
            }
        }
    }

    //---------------------------------------------------------------------------------------------------------------

    private void initUserPreferences(){
        preferenceUserName = getPreferenceManager().findPreference(getString(R.string.key_user_name));
        userName = preferenceUserName.getText();
        preferenceUserName.setOnBindEditTextListener(new EditTextPreference.OnBindEditTextListener() {
            @Override
            public void onBindEditText(@NonNull EditText editText) {
                editText.setSingleLine();
                editText.setInputType(InputType.TYPE_CLASS_TEXT);
                editText.selectAll();
            }
        });
        preferenceUserName.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                String value = newValue.toString();
                //Cambiar el nombre de usuario de la base de datos mysql
                newUserName = value;
                presenter.updateUserName(getContext(),firebaseAuth.getCurrentUser().getUid(),value);
                return false;
            }
        });
    }

    private void initPasswordPreferences(){
        EditTextPreference editTextPreference = getPreferenceManager().findPreference(getString(R.string.key_password));
        editTextPreference.setOnBindEditTextListener(new EditTextPreference.OnBindEditTextListener() {
            @Override
            public void onBindEditText(@NonNull EditText editText) {
                editText.setSingleLine();
                editText.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
                editText.selectAll();
            }
        });
        editTextPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                String value = newValue.toString();
                if(CommonUtils.isPasswordValid(value)) {
                    firebaseAuth.getCurrentUser().updatePassword(value);
                    return true;
                }
                else{
                    Snackbar.make(getView(),getString(R.string.err_passwordFormat),Snackbar.LENGTH_SHORT).show();
                    return false;
                }
            }
        });
    }

    @Override
    public void onuIDEmptyError() {
        Snackbar.make(getView(),getString(R.string.err_user_uid),Snackbar.LENGTH_SHORT).show();
        changeUserName(false);
    }

    @Override
    public void onNameEmptyError() {
        Snackbar.make(getView(),getString(R.string.err_nameEmpty),Snackbar.LENGTH_SHORT).show();
        changeUserName(false);
    }

    @Override
    public void onConnectionDatabaseError() {
        Snackbar.make(getView(),getString(R.string.err_webserv_connection),Snackbar.LENGTH_SHORT).show();
        changeUserName(false);
    }

    @Override
    public void onSucessUserUpdate() {
        Toast.makeText(getContext(),getString(R.string.msg_userupdate),Toast.LENGTH_SHORT).show();
        changeUserName(true);
        startActivity(new Intent(getActivity(), GainslogMainActivity.class));
    }

    @Override
    public void onSuccesUserImgUpdate() {
        Snackbar.make(getView(),getString(R.string.msg_imgProfile_update),Snackbar.LENGTH_SHORT).show();
        startActivity(new Intent(getActivity(), GainslogMainActivity.class));
    }

    @Override
    public void onFileNotFoundError() {
        Snackbar.make(getView(),getString(R.string.err_img_file),Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onFirebaseImageUpdateError() {
        Snackbar.make(getView(),getString(R.string.err_img_notUpdate),Snackbar.LENGTH_SHORT).show();
    }


    //Método para cambiar las preferencias guardadas si se produce error(change = false) dejará el nombre de usuario inicial
    //Si el nombre de usuario se ha cambiado correctamente(change = true), cambiar el nombre de usuario por el nuevo.
    private void changeUserName(boolean change){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if(change){
            editor.putString(getString(R.string.key_user_name),newUserName);
            preferenceUserName.setText(newUserName);
            preferenceUserName.setSummary(newUserName);
        }
        else{
            editor.putString(getString(R.string.key_user_name),userName);
            preferenceUserName.setSummary(userName);
        }
        editor.commit();
    }

    @Override
    public void onSucess() {

    }
}
