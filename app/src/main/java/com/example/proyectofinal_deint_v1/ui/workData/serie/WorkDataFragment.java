package com.example.proyectofinal_deint_v1.ui.workData.serie;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.proyectofinal_deint_v1.R;
import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.Exercise;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class WorkDataFragment extends Fragment {

    private FloatingActionButton btnAddSerie;
    private Exercise exercise;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            if(getArguments().getSerializable("exercise") != null){
            }
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnAddSerie = view.findViewById(R.id.btnAddSerie);
        btnAddSerie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    showDeleteDialog();
            }
        });
    }

    private void showDeleteDialog() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("serie",null);
        NavHostFragment.findNavController(WorkDataFragment.this).navigate(R.id.action_workDataFragment_to_serieEDitDialogFragment,bundle);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_work_data, container, false);
    }
}