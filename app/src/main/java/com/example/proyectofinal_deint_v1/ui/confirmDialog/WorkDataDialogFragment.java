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
import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.workData.WorkData;

public class WorkDataDialogFragment extends DialogFragment {

    public static final String CONFIRM_DELETE = "delete";
    public static final String TITLE = "title";
    public static final String MESSAGE = "message";

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        if(getArguments() != null)
        {
            String title = getArguments().getString(TITLE);
            String message = getArguments().getString(MESSAGE);

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle(title);
            builder.setMessage(message);
            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {


                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Bundle bundle = new Bundle();
                    bundle.putBoolean(CONFIRM_DELETE,true);
                    WorkData workData = (WorkData) getArguments().getSerializable("deleted");
                    bundle.putSerializable("deleted",workData);
                    //De esta manera no crea una nueva instacia del frangment listDependencyFragment
                    NavHostFragment.findNavController(WorkDataDialogFragment.this).navigate(R.id.homeFragment, bundle);
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