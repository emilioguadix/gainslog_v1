package com.example.proyectofinal_deint_v1.ui.coacinfo.request;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.proyectofinal_deint_v1.R;
import com.example.proyectofinal_deint_v1.ui.coacinfo.CoachFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
public class SendRequestDialogFragment extends DialogFragment {
    private TextInputLayout tilEmail;
    private TextInputEditText tieEmail;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getString(R.string.app_editSerie));
        //CREAR VISTA DEL DIALOG
        View viewDialog = getLayoutInflater().inflate(R.layout.sendrequest_dialog_layout,null);
        //Configuramos los spiners, con los datos correspondientes...null);
        builder.setView(viewDialog);
        tilEmail = viewDialog.findViewById(R.id.tilWeight);
        tieEmail = viewDialog.findViewById(R.id.tieWeight);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Bundle bundle = new Bundle();
                bundle.putString("email",tieEmail.getText().toString());
                NavHostFragment.findNavController(SendRequestDialogFragment.this).navigate(R.id.coachFragment,bundle);
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
}