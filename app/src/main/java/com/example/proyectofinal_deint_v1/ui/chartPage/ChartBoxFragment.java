package com.example.proyectofinal_deint_v1.ui.chartPage;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.proyectofinal_deint_v1.R;
import com.google.android.material.navigation.NavigationView;

public class ChartBoxFragment extends Fragment {
    private CardView cvWorkData;
    private CardView cvExercise;
    private CardView cvBodyData;
    private CardView cvTarget;
    private NavigationView navigationView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                NavHostFragment.findNavController(ChartBoxFragment.this).navigate(R.id.homeFragment);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cvWorkData = view.findViewById(R.id.cvWorkData);
        cvExercise = view.findViewById(R.id.cvExercise);
        cvBodyData = view.findViewById(R.id.cvBodyData);
        cvTarget = view.findViewById(R.id.cvTarget);
        navigationView = getActivity().findViewById(R.id.navigation_view);
        navigationView.getMenu().getItem(3).setChecked(true);
        //Gestión de clicks en los cardviews
        cvBodyData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(ChartBoxFragment.this).navigate(R.id.chartBodyDataFragment);
            }
        });
        //Gestión de clicks en los cardviews
        cvWorkData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(ChartBoxFragment.this).navigate(ChartBoxFragmentDirections.actionChartBoxFragmentToChartWorkDataFragment());
            }
        });

        cvExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(ChartBoxFragment.this).navigate(ChartBoxFragmentDirections.actionChartBoxFragmentToChartExerciseFragment(null));
            }
        });
        cvTarget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(ChartBoxFragment.this).navigate(R.id.chartTargetFragment);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chart_box, container, false);
    }
}