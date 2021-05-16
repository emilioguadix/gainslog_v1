package com.example.proyectofinal_deint_v1.ui.workData.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

import com.example.proyectofinal_deint_v1.R;
import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.Exercise;
import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.workData.WorkData;
import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.workData.serie.Serie;
import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.workData.serie.TypeSerie;
import com.example.proyectofinal_deint_v1.data.repository.products.SerieRepository;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

public class SerieEDitDialogFragment extends DialogFragment implements EditSerieContract.View {

    public static final String serieObject = "serie";
    private Serie serie;
    private boolean addMode;
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
    private TextInputLayout tilCopies;
    private TextInputEditText tieCopies;
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
            if(getArguments().getSerializable("exercise") != null){
                exercise = (WorkData) getArguments().getSerializable("exercise");
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle(getString(R.string.app_editSerie));
            //CREAR VISTA DEL DIALOG
            View viewDialog = getLayoutInflater().inflate(R.layout.serie_dialog_layout,null);
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
            tilCopies = viewDialog.findViewById(R.id.tilCopies);
            tieCopies = viewDialog.findViewById(R.id.tieCopies);
            cbxStar = viewDialog.findViewById(R.id.cbx_favorite);
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
            addMode = (getArguments().getSerializable("serie") != null) ? false : true;
            serie = (!addMode) ? (Serie) getArguments().getSerializable("serie") : new Serie();
            loadDataInputsFields();
            //RESPUESTAS DE LA VENTANA DE DIALOGO
            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(addMode){
                        serie = catchFields();
                        presenter.addSerie(serie);
                    }
                    else{
                        presenter.modifySerie(serie,catchFields());
                    }
                }
            });
            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dismiss();
                }
            });

            return builder.create();
        }
        return null;
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
        tieTimeRest.setText(String.valueOf(serie.getTimeRest()));
        cbxStar.setChecked(serie.isMarked());
        spnTypeSerie.setSelection(serie.getTypeSerie());
        tieNote.setText(serie.getNote());
        spnTypeIntesity.setSelection((serie.getTypeIntensity().equals("RIR")) ? 0 : 1);
        tieCopies.setText(String.valueOf(serie.getCopiesSerie()));
    }

    private Serie catchFields(){
        Serie tmp = new Serie();
        serie.setWeight(Integer.parseInt(tieWeight.getText().toString().isEmpty() ? "0" : tieWeight.getText().toString() ));
        serie.setReps(Integer.parseInt(tieReps.getText().toString().isEmpty() ? "0" : tieReps.getText().toString() ));
        serie.setTypeSerie(typeSerieSelected);
        serie.setTypeIntensity(spnTypeIntesity.getSelectedItem().toString());
        serie.setIntensity(Integer.parseInt(tieIntensity.getText().toString().isEmpty() ? "0" : tieIntensity.getText().toString() ));
        serie.setTimeRest(Integer.parseInt(tieTimeRest.getText().toString().isEmpty() ? "0" : tieTimeRest.getText().toString() ));
        serie.setTime(Long.parseLong(tieTime.getText().toString().isEmpty() ? "0" : tieTime.getText().toString() ));
        serie.setMarked((cbxStar.isChecked()) ? true:false);
        serie.setNote(tieNote.getText().toString());
        serie.setCopiesSerie(Integer.parseInt(tieCopies.getText().toString().isEmpty() ? "0" : tieCopies.getText().toString() ));
        return serie;
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
        SerieEDitDialogFragmentDirections.ActionSerieEDitDialogFragmentToWorkDataFragment action = SerieEDitDialogFragmentDirections.actionSerieEDitDialogFragmentToWorkDataFragment(exercise);
        NavHostFragment.findNavController(SerieEDitDialogFragment.this).navigate(action);
    }

    @Override
    public void onSuccesWorkDataAdd() {

    }

    @Override
    public void onSuccesModify() {
        SerieEDitDialogFragmentDirections.ActionSerieEDitDialogFragmentToWorkDataFragment action = SerieEDitDialogFragmentDirections.actionSerieEDitDialogFragmentToWorkDataFragment(exercise);
        NavHostFragment.findNavController(SerieEDitDialogFragment.this).navigate(action);
    }

    @Override
    public void onSucess() {

    }

    @Override
    public void onSuccess(List repository) {

    }
}