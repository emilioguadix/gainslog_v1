package com.example.proyectofinal_deint_v1.ui.workData.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.preference.EditTextPreference;
import androidx.preference.PreferenceManager;

import com.example.proyectofinal_deint_v1.R;
import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.Exercise;
import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.workData.WorkData;
import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.workData.serie.Serie;
import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.workData.serie.TypeSerie;
import com.example.proyectofinal_deint_v1.data.repository.products.SerieRepository;
import com.example.proyectofinal_deint_v1.ui.boxData.exercise.ExerciseListFragment;
import com.example.proyectofinal_deint_v1.ui.boxData.exercise.ExerciseListFragmentArgs;
import com.example.proyectofinal_deint_v1.ui.utils.CommonUtils;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

public class SerieEDitDialogFragment extends DialogFragment implements EditSerieContract.View {

    public static final String serieObject = "serie";
    private Serie serie;
    private boolean addMode;
    private boolean boxMode;
    private boolean addModeSerie;
    private EditSerieContract.Presenter presenter;
    private TextInputLayout tilWeight;
    private TextInputEditText tieWeight;
    private TextInputLayout tilReps;
    private TextInputEditText tieReps;
    private TextInputLayout tilIntensity;
    private TextInputEditText tieIntensity;
    private TextInputLayout tilTimeRest;
    private TextInputEditText tieTimeRest;
    private TextInputLayout tilTime;
    private TextInputEditText tieTime;
    private TextInputLayout tilNote;
    private TextInputEditText tieNote;
    private Spinner spnTypeSerie;
    private Spinner spnTypeIntesity;
    private CheckBox cbxStar;
    private int typeSerieSelected;
    private WorkData exercise;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        presenter = new EditSeriePresenter(SerieEDitDialogFragment.this);
        if(getArguments() != null)
        {
            if(getArguments().getSerializable("workData") != null){
                exercise = (WorkData) getArguments().getSerializable("workData");
            }
            addMode = getArguments().getBoolean("addMode");
            addModeSerie = getArguments().getBoolean("addModeSerie");
            boxMode = getArguments().getBoolean("boxMode");
            serie = (Serie) getArguments().getSerializable("serie");
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle(getString(R.string.app_editSerie));
            //CREAR VISTA DEL DIALOG
            View viewDialog = getLayoutInflater().inflate(R.layout.serie_dialog_layout,null);
            //Configuramos los spiners, con los datos correspondientes...null);
            builder.setView(viewDialog);
            //region Recoger campos de la ventana de dialogo
            spnTypeIntesity = viewDialog.findViewById(R.id.spnTypeIntensity);
            spnTypeSerie = viewDialog.findViewById(R.id.spnTypeSerie);
            tilWeight = viewDialog.findViewById(R.id.tilWeight);
            tieWeight = viewDialog.findViewById(R.id.tieWeight);
            tilReps = viewDialog.findViewById(R.id.tilReps);
            tieReps = viewDialog.findViewById(R.id.tieReps);
            tilIntensity = viewDialog.findViewById(R.id.tilIntensity);
            tieIntensity = viewDialog.findViewById(R.id.tieIntensity);
            tilTimeRest = viewDialog.findViewById(R.id.tilTimeRest);
            tieTimeRest = viewDialog.findViewById(R.id.tieTimeRest);
            tilTime = viewDialog.findViewById(R.id.tilTimeToUp);
            tieTime = viewDialog.findViewById(R.id.tieTimeToUp);
            tilNote = viewDialog.findViewById(R.id.tilNote);
            tieNote = viewDialog.findViewById(R.id.tieNote);
            cbxStar = viewDialog.findViewById(R.id.cbx_favorite);
            if(getArguments().getBoolean("boxMode") || CommonUtils.isCoachUser(getContext())){
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                if(!sharedPreferences.getBoolean(getString(R.string.key_permission_coach),true)){
                    updateDenied();
                }
            }
            //Configuramos los spiners, con los datos correspondientes...
            final ArrayAdapter adapter1 = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item, TypeSerie.getList(getContext()));
            //Seteamos el adapter a cada uno de los spinners.
            spnTypeSerie.setAdapter(adapter1);
            final ArrayAdapter adapter2 = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.typeIntensity));
            //Seteamos el adapter a cada uno de los spinners.
            spnTypeIntesity.setAdapter(adapter2);
            //Gestión de errores en tiempo real
            tieWeight.addTextChangedListener(new EditTextWatcher(tieIntensity));
            tieReps.addTextChangedListener(new EditTextWatcher(tieReps));
            spnTypeSerie.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    typeSerieSelected = i;
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    typeSerieSelected = 0;
                }
            });
            //endregion
            loadDataInputsFields();
            //RESPUESTAS DE LA VENTANA DE DIALOGO
            if(!boxMode) {
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (getArguments().getBoolean("boxMode")) {
                        } else {
                            if (addModeSerie) {
                                presenter.addSerie(catchFields());
                            } else {
                                presenter.modifySerie(serie, catchFields());
                            }
                        }
                    }
                });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                });
            }

            return builder.create();
        }
        return null;
    }

    private void updateDenied() {
        spnTypeIntesity.setEnabled(false);
        spnTypeSerie.setEnabled(false);
        tieIntensity.setEnabled(false);
        tieNote.setEnabled(false);
        tieWeight.setEnabled(false);
        cbxStar.setEnabled(false);
        tieReps.setEnabled(false);
        tieTime.setEnabled(false);
        tieTime.setEnabled(false);
        tieTimeRest.setEnabled(false);
    }

    //CLASES Y MÉTODOS PARA LANZAR ERRORES EN TIEMPO DE EJECUCIÓN
    // --> Clase
    class EditTextWatcher implements TextWatcher {
        private View view;

        EditTextWatcher(View view) {
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
                case R.id.tieWeight:
                    validateWeight(tieWeight.getText().toString());
                    break;
                case R.id.tieReps:
                    validateReps(tieReps.getText().toString());
                    break;
            }
        }
    }

    private void validateWeight(String weight){
        if(weight.isEmpty()){
            setWeightEmptyError();
        }
        else{
            tilNote.setErrorEnabled(false);
        }
    }

    private void validateReps(String reps){
        if(reps.isEmpty()){
            setRepsEmptyError();
        }
        else{
            tilReps.setErrorEnabled(false);
        }
    }

    private void loadDataInputsFields(){
        tieWeight.setText(String.valueOf(serie.getWeight()));
        tieReps.setText(String.valueOf(serie.getReps()));
        tieTime.setText(String.valueOf(serie.getTime()));
        tieIntensity.setText(String.valueOf(serie.getIntensity()));
        tieTimeRest.setText(String.valueOf(serie.getTimeRest()));
        cbxStar.setChecked(serie.getMarked() == 0 ? false : true);
        spnTypeSerie.setSelection(serie.getTypeSerie());
        tieNote.setText(serie.getNote());
        spnTypeIntesity.setSelection((serie.getTypeIntensity().equals("RIR")) ? 0 : 1);
    }

    private Serie catchFields(){
        Serie tmp = new Serie();
        tmp.setWeight(Integer.parseInt(tieWeight.getText().toString().isEmpty() ? "0" : tieWeight.getText().toString() ));
        tmp.setReps(Integer.parseInt(tieReps.getText().toString().isEmpty() ? "0" : tieReps.getText().toString() ));
        tmp.setTypeSerie(typeSerieSelected);
        tmp.setTypeIntensity(spnTypeIntesity.getSelectedItem().toString());
        tmp.setIntensity(Integer.parseInt(tieIntensity.getText().toString().isEmpty() ? "0" : tieIntensity.getText().toString() ));
        tmp.setTimeRest(Integer.parseInt(tieTimeRest.getText().toString().isEmpty() ? "0" : tieTimeRest.getText().toString() ));
        tmp.setTime(Long.parseLong(tieTime.getText().toString().isEmpty() ? "0" : tieTime.getText().toString() ));
        tmp.setMarked((cbxStar.isChecked()) ? 1:0);
        tmp.setNote(tieNote.getText().toString());
        return tmp;
    }

    private void goToBoxDataWork(){
        NavHostFragment.findNavController(SerieEDitDialogFragment.this).navigate(R.id.workDataBoxFragment);
    }

    @Override
    public void setEmptyRepositoryError() {

    }

    @Override
    public void setFireBaseConnectionError() {
        Snackbar.make(getView(),getString(R.string.err_firebaseConnection),Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void setWeightEmptyError() {
        tilWeight.setError(getString(R.string.err_empty_reps));
    }

    @Override
    public void setRepsEmptyError() {
        tilReps.setError(getString(R.string.err_empty_reps));
    }

    @Override
    public void onSuccessDelete() {
    }

    @Override
    public void onSuccesAdd() {
        presenter.getRepository();

    }

    @Override
    public void onSuccesWorkDataAdd() {

    }

    @Override
    public void onSuccesModify() {
        presenter.getRepository();
    }

    @Override
    public void onSucess() {

    }

    @Override
    public void onSuccess(List repository) {
        exercise.setSerieList(new ArrayList<>(repository));
        Bundle bundle = new Bundle();
        bundle.putSerializable("workData",exercise);
        bundle.putSerializable("addMode",addMode);
        bundle.putBoolean("addModeSerie",addModeSerie);
        NavHostFragment.findNavController(SerieEDitDialogFragment.this).navigate(R.id.workDataFragment,bundle);
    }
}