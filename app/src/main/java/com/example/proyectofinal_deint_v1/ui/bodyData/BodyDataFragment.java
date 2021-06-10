package com.example.proyectofinal_deint_v1.ui.bodyData;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.proyectofinal_deint_v1.R;
import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.bodyData.BodyData;
import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.bodyData.Measurement;
import com.example.proyectofinal_deint_v1.ui.boxData.bodyData.BodyDataBoxFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;

public class BodyDataFragment extends Fragment implements BodyDataContract.View{

    private Button btnMeasure;
    private Button btnPhoto;
    private ImageButton btnShowPhoto;
    private TextInputLayout tilWeight;
    private TextInputLayout tilFat;
    private TextInputLayout tilNote;
    private TextInputEditText tieWeight;
    private TextInputEditText tieFat;
    private TextInputEditText tieNote;
    private FloatingActionButton btnAdd;
    private BodyData bodyData;
    private BodyData oldBodyData;
    private boolean isModify;
    private StorageReference mStorage;
    private StorageReference filePath;
    private Uri uriPhotoSelected;

    private BodyDataContract.Presenter presenter;
    private static final int GALLERY_INTENT = 1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                if(getArguments() != null && getArguments().getBoolean("boxMode")) {
                    Bundle bundle = new Bundle();
                    bundle.putString("setDate",getArguments().getString("setDate"));
                    bundle.putBoolean("boxMode",getArguments().getBoolean("boxMode"));
                    NavHostFragment.findNavController(BodyDataFragment.this).navigate(R.id.bodyDataBoxFragment);
                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public void onStart() {
        super.onStart();
        bodyData = new BodyData();
        if(getArguments()!= null){
            if (getArguments().getSerializable("body") != null) {
                oldBodyData = (BodyData) getArguments().getSerializable("body");
                if(getArguments().getBoolean("modify") != false)
                {
                    isModify = true;
                    changeIconBtn();
                }
                bodyData.setId(oldBodyData.getId());
                setFields();
            }
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mStorage = FirebaseStorage.getInstance().getReference();
        tilWeight = view.findViewById(R.id.tilBodyWeight);
        tilFat = view.findViewById(R.id.tilFatPer);
        tieWeight = view.findViewById(R.id.tieBodyWeight);
        tieFat = view.findViewById(R.id.tieFatPer);
        tilNote = view.findViewById(R.id.tilBodyNote);
        tieNote = view.findViewById(R.id.tieBodyNote);
        btnAdd = view.findViewById(R.id.btnBodyDataSave);
        btnPhoto = view.findViewById(R.id.btnAddPhoto);
        btnShowPhoto = view.findViewById(R.id.btnShowPhoto);
        tieWeight.addTextChangedListener(new BodyTextWatcher(tieWeight));
        btnMeasure = view.findViewById(R.id.btnAddMeas);
        presenter = new BodyDataPresenter(this);
        if(getArguments().getBoolean("boxMode")){
            disableUpdating();
        }
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!tieWeight.getText().toString().equals("")) {
                    catchFields();
                    if (isModify) {
                        presenter.modifyBodyData(getContext(),oldBodyData,bodyData);
                    }
                    else {
                        presenter.addBodyData(getContext(), bodyData);
                    }
                }
                else{
                    tieWeight.setText("");
                }
            }
        });
        btnMeasure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                catchFields();
                Bundle bundle = new Bundle();
                bundle.putSerializable("body",bodyData);
                bundle.putBoolean("modify",isModify);
                bundle.putBoolean("boxMode",getArguments().getBoolean("boxMode"));
                NavHostFragment.findNavController(BodyDataFragment.this).navigate(R.id.bodyMeasureFragment,bundle);
            }
        });
        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,GALLERY_INTENT);
            }
        });
        btnShowPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getUrlFromPhoto();
            }
        });
    }

    private void disableUpdating(){
        tieWeight.setEnabled(false);
        tieFat.setEnabled(false);
        tieNote.setEnabled(false);
        btnPhoto.setEnabled(false);
        btnAdd.setVisibility(View.GONE);
    }

    private void getUrlFromPhoto(){
        StorageReference stRef;
        if(bodyData != null && bodyData.getId() != 0){
            stRef = mStorage.child("fotos").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(String.valueOf(bodyData.getId()));
            stRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Intent intentWeb = new Intent();
                    intentWeb.setAction(Intent.ACTION_VIEW);
                    intentWeb.setData(uri);
                    startActivity(intentWeb);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure( Exception e) {
                    Toast.makeText(getActivity(),getString(R.string.err_img_dontfound),Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERY_INTENT && resultCode == RESULT_OK){
            uriPhotoSelected = data.getData();
            filePath = mStorage.child("fotos").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
            Toast.makeText(getContext(),getString(R.string.img_bodyData_uploading),Toast.LENGTH_SHORT).show();
        }
    }

    private void changeIconBtn(){
        btnAdd.setImageResource(R.mipmap.ic_save);
    }

    private void setFields(){
        tieWeight.setText(oldBodyData.getWeight() != 0 ? String.valueOf(oldBodyData.getWeight()) : "");
        tieFat.setText(oldBodyData.getFatPer() != 0 ? String.valueOf(oldBodyData.getFatPer()) : "");
        tieNote.setText(oldBodyData.getNote() != null ? oldBodyData.getNote() : "");
    }

    private void catchFields(){
        bodyData.setWeight(Double.parseDouble(tieWeight.getText().toString().isEmpty() ? "0" : tieWeight.getText().toString() ));
        bodyData.setFatPer(Double.parseDouble(tieFat.getText().toString().isEmpty() ? "0" : tieFat.getText().toString() ));
        bodyData.setNote(tieNote.getText().toString());
        if(oldBodyData != null && oldBodyData.getMeasurements().size() > 0){
            bodyData.setMeasurements(oldBodyData.getMeasurements());
        }
    }

    @Override
    public void setFireBaseConError() {
        Snackbar.make(getView(),getString(R.string.err_firebaseConnection),Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccessBodyDataAdd(BodyData bodyData) {
        NavHostFragment.findNavController(BodyDataFragment.this).navigate(R.id.homeFragment);
        if(uriPhotoSelected != null) {
            //Subir foto a storage, además añado a la url el id del bodyData creado --> (/fotos/userUID/bodyDataId)
            filePath =  mStorage.child("fotos").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(String.valueOf(bodyData.getId()));
            filePath.putFile(uriPhotoSelected).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getActivity(), getString(R.string.upload_img_succesful), Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(Exception e) {
                    Toast.makeText(getActivity(), getString(R.string.upload_img_failure), Toast.LENGTH_SHORT).show();

                }
            });
        }
    }

    @Override
    public void onSucess() {

    }

    class BodyTextWatcher implements TextWatcher {
        private View view;

        BodyTextWatcher(View view) {
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
                case R.id.tieBodyWeight:
                    validateWeight(tieWeight.getText().toString());
                    break;
            }
        }
    }

    private void validateWeight(String toString) {
        if(toString.isEmpty()){
            //SET ERROR
            tilWeight.setErrorEnabled(true);
            tilWeight.setError(getString(R.string.err_weight_empty));
        }
        else{
            tilWeight.setErrorEnabled(false);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_body_data, container, false);
    }
}