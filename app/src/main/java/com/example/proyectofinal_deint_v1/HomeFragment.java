package com.example.proyectofinal_deint_v1;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.proyectofinal_deint_v1.ui.boxData.exercise.ExerciseListFragment;
import com.example.proyectofinal_deint_v1.ui.boxData.exercise.ExerciseListFragmentDirections;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HomeFragment extends Fragment {

    private FloatingActionButton btnWorkData;
    private RecyclerView rvLogs;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnWorkData = view.findViewById(R.id.btnAddWorkData);

        btnWorkData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeFragmentDirections.ActionHomeFragmentToExerciseListFragment action = HomeFragmentDirections.actionHomeFragmentToExerciseListFragment(true);
                NavHostFragment.findNavController(HomeFragment.this).navigate(action);
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }
}