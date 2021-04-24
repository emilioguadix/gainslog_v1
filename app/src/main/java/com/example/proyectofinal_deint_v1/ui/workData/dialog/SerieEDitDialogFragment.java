package com.example.proyectofinal_deint_v1.ui.workData.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.preference.EditTextPreference;

import com.example.proyectofinal_deint_v1.R;
import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.workData.serie.Serie;
import com.example.proyectofinal_deint_v1.data.repository.products.SerieRepository;
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

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        presenter = new EditSeriePresenter(SerieEDitDialogFragment.this);
        if(getArguments() != null)
        {
            if(getArguments().getSerializable("serie") != null){
                addMode = false;
                serie = (Serie) getArguments().getSerializable("serie");
            }
            else{
                addMode = true;
                serie = new Serie();
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle(getString(R.string.app_editSerie));
            //CREAR VISTA DEL DIALOG
            View viewDialog = getLayoutInflater().inflate(R.layout.serie_dialog_layout,null);
            builder.setView(viewDialog);
            //region Recoger campos de la ventana de dialogo
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
            //endregion

            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(addMode){
                        serie = catchFields();
                        presenter.addSerie(serie);
                    }
                    SerieRepository.getInstance().getList();
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

    private Serie catchFields(){
        Serie tmp = new Serie();
        serie.setWeight(Integer.parseInt(tieWeight.getText().toString()));
        serie.setReps(Integer.parseInt(tieReps.getText().toString()));
        serie.setIntensity(Integer.parseInt(tieIntensity.getText().toString()));
        serie.setTimeRest(Long.parseLong(tieTimeRest.getText().toString()));
        serie.setTime(Long.parseLong(tieTime.getText().toString()));
        return serie;
    }

    @Override
    public void setEmptyRepositoryError() {

    }

    @Override
    public void setWeightEmptyError() {

    }

    @Override
    public void setRepsEmptyError() {

    }

    @Override
    public void onSuccessDelete() {
        NavHostFragment.findNavController(SerieEDitDialogFragment.this).navigate(R.id.workDataFragment);
    }

    @Override
    public void onSuccesAdd() {
        NavHostFragment.findNavController(SerieEDitDialogFragment.this).navigate(R.id.workDataFragment);
    }

    @Override
    public void onSuccesModify() {
        NavHostFragment.findNavController(SerieEDitDialogFragment.this).navigate(R.id.workDataFragment);
    }

    @Override
    public void onSucess() {

    }

    @Override
    public void onSuccess(List repository) {

    }
}