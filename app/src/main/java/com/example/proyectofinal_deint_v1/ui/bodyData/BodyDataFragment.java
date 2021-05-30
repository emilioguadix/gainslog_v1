package com.example.proyectofinal_deint_v1.ui.bodyData;

import android.os.Bundle;

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

import com.example.proyectofinal_deint_v1.R;
import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.bodyData.BodyData;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class BodyDataFragment extends Fragment implements BodyDataContract.View{

    private Button btnMeasure;
    private TextInputLayout tilWeight;
    private TextInputLayout tilFat;
    private TextInputLayout tilNote;
    private TextInputEditText tieWeight;
    private TextInputEditText tieFat;
    private TextInputEditText tieNote;
    private FloatingActionButton btnAdd;
    private BodyData bodyData;

    private BodyDataContract.Presenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bodyData = new BodyData();
        tilWeight = view.findViewById(R.id.tilBodyWeight);
        tilFat = view.findViewById(R.id.tilFatPer);
        tieWeight = view.findViewById(R.id.tieBodyWeight);
        tieFat = view.findViewById(R.id.tieFatPer);
        tilNote = view.findViewById(R.id.tilBodyNote);
        tieNote = view.findViewById(R.id.tieBodyNote);
        btnAdd = view.findViewById(R.id.btnBodyDataSave);
        tieWeight.addTextChangedListener(new BodyTextWatcher(tieWeight));
        tieFat.addTextChangedListener(new BodyTextWatcher(tieFat));
        btnMeasure = view.findViewById(R.id.btnAddMeas);
        presenter = new BodyDataPresenter(this);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                catchFields();
                presenter.addBodyData(getContext(),bodyData);
            }
        });
        btnMeasure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BodyDataFragmentDirections.ActionBodyDataFragmentToBodyMeasureFragment action = BodyDataFragmentDirections.actionBodyDataFragmentToBodyMeasureFragment(null);
                NavHostFragment.findNavController(BodyDataFragment.this).navigate(action);
            }
        });
    }

    private void catchFields(){
        bodyData.setWeight(Double.parseDouble(tieWeight.getText().toString().isEmpty() ? "0" : tieWeight.getText().toString() ));
        bodyData.setFatPer(Double.parseDouble(tieFat.getText().toString().isEmpty() ? "0" : tieFat.getText().toString() ));
        bodyData.setNote(tieNote.getText().toString());
    }

    @Override
    public void setFireBaseConError() {

    }

    @Override
    public void onSuccessBodyDataAdd() {
        NavHostFragment.findNavController(BodyDataFragment.this).navigate(R.id.boxDataFragment);
    }

    @Override
    public void onSuccessBodyDataModify() {
        NavHostFragment.findNavController(BodyDataFragment.this).navigate(R.id.boxDataFragment);
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
                case R.id.tieFatPer:
                    validateFatPer(tieFat.getText().toString());
                    break;
            }
        }
    }

    private void validateWeight(String toString) {
        if(toString.isEmpty()){
            //SET ERROR
        }
        else{
            tilWeight.setErrorEnabled(false);
        }
    }

    private void validateFatPer(String toString) {
        if(toString.isEmpty()){
            //SET ERROR
        }
        else{
            tilFat.setErrorEnabled(false);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_body_data, container, false);
    }
}