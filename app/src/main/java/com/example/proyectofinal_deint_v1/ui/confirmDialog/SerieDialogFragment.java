package com.example.proyectofinal_deint_v1.ui.confirmDialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.proyectofinal_deint_v1.R;
import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.Exercise;
import com.example.proyectofinal_deint_v1.ui.workData.dialog.SerieEDitDialogFragmentArgs;
import com.example.proyectofinal_deint_v1.ui.workData.dialog.SerieEDitDialogFragmentDirections;

public class SerieDialogFragment extends DialogFragment {

    public static final String CONFIRM_DELETE = "delete";
    public static final String TITLE = "title";
    public static final String MESSAGE = "message";
    private Exercise exercise;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        if(getArguments() != null)
        {
            String title = getArguments().getString(TITLE);
            String message = getArguments().getString(MESSAGE);
            exercise = SerieEDitDialogFragmentArgs.fromBundle(getArguments()).getExercise();
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle(title);
            builder.setMessage(message);
            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {


                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Bundle bundle = new Bundle();
                    bundle.putBoolean(CONFIRM_DELETE,true);
                    bundle.putSerializable("exercise",exercise);
                    bundle.putSerializable("deleted",getArguments().getSerializable("deleted"));
                    //De esta manera no crea una nueva instacia del frangment listDependencyFragment
                    NavHostFragment.findNavController(SerieDialogFragment.this).navigate(R.id.workDataFragment, bundle);
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
}