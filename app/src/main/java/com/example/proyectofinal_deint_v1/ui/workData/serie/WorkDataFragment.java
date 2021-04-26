package com.example.proyectofinal_deint_v1.ui.workData.serie;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.proyectofinal_deint_v1.R;
import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.Exercise;
import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.workData.serie.Serie;
import com.example.proyectofinal_deint_v1.ui.adapter.ExerciseAdapter;
import com.example.proyectofinal_deint_v1.ui.adapter.SerieAdapter;
import com.example.proyectofinal_deint_v1.ui.boxData.exercise.ExerciseListFragment;
import com.example.proyectofinal_deint_v1.ui.workData.dialog.EditSerieContract;
import com.example.proyectofinal_deint_v1.ui.workData.dialog.EditSeriePresenter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class WorkDataFragment extends Fragment implements EditSerieContract.View,SerieAdapter.onSerieClickListener {

    private FloatingActionButton btnAddSerie;
    private FloatingActionButton btnAddWorkData;
    private Exercise exercise;
    private RecyclerView recyclerView;
    private SerieAdapter adapter;
    private List<Serie> repositoryList;
    private EditSerieContract.Presenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        if(getArguments() != null){
            if(WorkDataFragmentArgs.fromBundle(getArguments()).getExercise() != null){
                exercise = WorkDataFragmentArgs.fromBundle(getArguments()).getExercise();
            }
        }
        presenter.getRepository();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter = new EditSeriePresenter(this);
        btnAddSerie = view.findViewById(R.id.btnAddSerie);
        btnAddWorkData = view.findViewById(R.id.btnAddWorkData);
        btnAddSerie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    showDeleteDialog();
            }
        });
        btnAddWorkData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //insertar workData en la BD
                presenter.addWorkData(getContext(),exercise);
            }
        });
        recyclerView = view.findViewById(R.id.rvSerie);
        //2.Crea el diseño del REcycler VIew
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new SerieAdapter(getContext(),repositoryList, (SerieAdapter.onSerieClickListener)this);
        recyclerView.setAdapter(adapter);
    }

    private void showDeleteDialog() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("exercise",exercise);
        bundle.putSerializable("serie",null);
        NavHostFragment.findNavController(WorkDataFragment.this).navigate(R.id.action_workDataFragment_to_serieEDitDialogFragment,bundle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_work_data, container, false);
    }

    //region Método sobreescritos del presenter
    @Override
    public void setEmptyRepositoryError() {
        Toast.makeText(getContext(), getString(R.string.err_EmptyRepository), Toast.LENGTH_LONG).show();
    }

    @Override
    public void setFireBaseConnectionError() {
        Snackbar.make(getView(),getString(R.string.err_EmptyRepository),Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void setWeightEmptyError() {

    }

    @Override
    public void setRepsEmptyError() {

    }

    @Override
    public void onSuccessDelete() {

    }

    @Override
    public void onSuccesAdd() {
        NavHostFragment.findNavController(WorkDataFragment.this).navigate(R.id.homeFragment);
    }

    @Override
    public void onSuccesModify() {

    }

    @Override
    public void onSucess() {

    }

    @Override
    public void onSuccess(List repository) {
        this.repositoryList = repository;
        this.adapter.updateData(repositoryList);
    }
    //endregion

    //region Listener del RecyclerView --> ADAPTER
    @Override
    public void onClick(Serie serie) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("serie",serie);
        NavHostFragment.findNavController(WorkDataFragment.this).navigate(R.id.action_workDataFragment_to_serieEDitDialogFragment,bundle);
    }

    @Override
    public void onLongClick(Serie serie) {

    }

    @Override
    public void onClick(View view) {

    }
    //endregion
}