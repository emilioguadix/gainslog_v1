package com.example.proyectofinal_deint_v1.ui.homePage;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.proyectofinal_deint_v1.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class HomeFragment extends Fragment {

    private FloatingActionButton btnWorkData;
    private RecyclerView rvLogs;
    private TextView tvTitle;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnWorkData = view.findViewById(R.id.btnAddWorkData);
        tvTitle = view.findViewById(R.id.title_home_time);
        tvTitle.setText(getString(R.string.title_home_time,getDateNow()));
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

    private String getDateNow(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        return df.format(c.getTime());
    }
}