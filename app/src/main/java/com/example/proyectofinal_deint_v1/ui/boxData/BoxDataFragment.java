package com.example.proyectofinal_deint_v1.ui.boxData;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.preference.PreferenceManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.proyectofinal_deint_v1.R;
import com.example.proyectofinal_deint_v1.ui.utils.CommonUtils;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BoxDataFragment extends Fragment {

    private CardView exercise;
    private CardView target;
    private NavigationView navView;
    private TextView userName;
    private TextView userEmail;
    private SharedPreferences sharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Una vez iniciada sesión configuramos los datos de la cabecera con los del usuario logeado.
        navView = getActivity().findViewById(R.id.navigation_view);
        View headerView = navView.getHeaderView(0);
        userName = headerView.findViewById(R.id.username);
        userEmail = headerView.findViewById(R.id.email);
        //Asignamos los datos del usuario
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        userEmail.setText(sharedPreferences.getString(getString(R.string.key_user),""));
        userName.setText(sharedPreferences.getString(getString(R.string.key_user_name),""));
        //--------------------------------------------------------------------------------------
        exercise = view.findViewById(R.id.cvExercise);
        target = view.findViewById(R.id.cvTarget);
        exercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(BoxDataFragment.this).navigate(R.id.ExerciseListFragment);
            }
        });
        target.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(BoxDataFragment.this).navigate(R.id.targetListFragment);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_box_data, container, false);
    }
}