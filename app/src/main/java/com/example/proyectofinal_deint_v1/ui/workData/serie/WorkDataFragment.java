package com.example.proyectofinal_deint_v1.ui.workData.serie;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
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
import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.workData.WorkData;
import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.workData.serie.Serie;
import com.example.proyectofinal_deint_v1.data.model.model.target.Target;
import com.example.proyectofinal_deint_v1.ui.adapter.ExerciseAdapter;
import com.example.proyectofinal_deint_v1.ui.adapter.SerieAdapter;
import com.example.proyectofinal_deint_v1.ui.boxData.exercise.ExerciseListFragment;
import com.example.proyectofinal_deint_v1.ui.boxData.target.TargetListFragment;
import com.example.proyectofinal_deint_v1.ui.confirmDialog.ExerciseDialogFragment;
import com.example.proyectofinal_deint_v1.ui.workData.dialog.EditSerieContract;
import com.example.proyectofinal_deint_v1.ui.workData.dialog.EditSeriePresenter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class WorkDataFragment extends Fragment implements EditSerieContract.View,SerieAdapter.onSerieClickListener {

    private FloatingActionButton btnAddSerie;
    private FloatingActionButton btnAddWorkData;
    private WorkData exercise;
    private CoordinatorLayout coordinatorLayout;
    private RecyclerView recyclerView;
    private SerieAdapter adapter;
    private List<Serie> repositoryList;
    private EditSerieContract.Presenter presenter;
    private Serie serieDeleted;
    private WorkData oldWorkData;
    private boolean addMode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getArguments() != null) {
            if ((WorkData)getArguments().getSerializable("workData") != null) {
                oldWorkData = (WorkData) getArguments().getSerializable("workData");
                exercise = (WorkData) getArguments().getSerializable("workData");
                addMode = getArguments().getBoolean("addMode");
                if(!getArguments().getBoolean(ExerciseDialogFragment.CONFIRM_DELETE)) {
                    presenter.updateRepository(oldWorkData);
                }
            }
            if (getArguments().getBoolean(ExerciseDialogFragment.CONFIRM_DELETE)) {
                serieDeleted = (Serie) getArguments().getSerializable("deleted");
                presenter.deleteSerie(serieDeleted);
            }
        }
        presenter.getRepository();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter = new EditSeriePresenter(this);
        btnAddSerie = view.findViewById(R.id.btnAddSerie);
        coordinatorLayout = view.findViewById(R.id.coordinatorWorkData);
        btnAddWorkData = view.findViewById(R.id.btnAddWorkData);
        btnAddSerie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEditDialog();
            }
        });
        btnAddWorkData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //insertar workData en la BD
                if(addMode) {
                    presenter.addWorkData(getContext(), exercise);
                }
                else{
                    presenter.modifyWorkData(getContext(),oldWorkData);
                }
            }
        });
        recyclerView = view.findViewById(R.id.rvSerie);
        //2.Crea el diseño del REcycler VIew
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new SerieAdapter(getContext(),repositoryList, (SerieAdapter.onSerieClickListener)this);
        recyclerView.setAdapter(adapter);
    }

    private void showEditDialog() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("workData",exercise);
        bundle.putBoolean("addModeSerie",true);
        bundle.putSerializable("serie",new Serie());
        bundle.putSerializable("addMode",addMode);
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
    }

    @Override
    public void setFireBaseConnectionError() {
        Snackbar.make(coordinatorLayout,getString(R.string.err_EmptyRepository),Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void setWeightEmptyError() {

    }

    @Override
    public void setRepsEmptyError() {

    }

    @Override
    public void onSuccessDelete() {
        adapter.delete(serieDeleted);
    }

    @Override
    public void onSuccesAdd() {
        adapter.add(serieDeleted);
    }

    @Override
    public void onSuccesWorkDataAdd() {
        NavHostFragment.findNavController(WorkDataFragment.this).navigate(R.id.homeFragment);
    }

    @Override
    public void onSuccesModify() {
        NavHostFragment.findNavController(WorkDataFragment.this).navigate(R.id.homeFragment);
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
    public void onClick(Serie tmp) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("workData",exercise);
        bundle.putSerializable("addMode",addMode);
        bundle.putBoolean("addModeSerie",false);
        bundle.putSerializable("serie",tmp);
        bundle.putBoolean("addMode",addMode);
        NavHostFragment.findNavController(WorkDataFragment.this).navigate(R.id.serieEDitDialogFragment,bundle);
    }

    @Override
    public void onLongClick(Serie serie) {
        serieDeleted = serie;
        showDeleteDialog(serieDeleted);
    }

    private void showDeleteDialog(Serie serie) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("workData",exercise);
        bundle.putSerializable("addMode",addMode);
        bundle.putString(ExerciseDialogFragment.TITLE, getString(R.string.title_delete));
        bundle.putString(ExerciseDialogFragment.MESSAGE, getString(R.string.message_delete_exercise,"SET " + String.valueOf(serie.getNumSerie())));
        bundle.putSerializable("deleted",serie);
        NavHostFragment.findNavController(WorkDataFragment.this).navigate(R.id.serieDialogFragment,bundle);
    }

    @Override
    public void onClick(View view) {

    }
    //endregion
}