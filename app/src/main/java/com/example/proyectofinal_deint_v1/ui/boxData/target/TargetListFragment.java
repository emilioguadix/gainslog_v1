package com.example.proyectofinal_deint_v1.ui.boxData.target;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.proyectofinal_deint_v1.R;
import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.Exercise;
import com.example.proyectofinal_deint_v1.data.model.model.target.Target;
import com.example.proyectofinal_deint_v1.ui.adapter.ExerciseAdapter;
import com.example.proyectofinal_deint_v1.ui.adapter.TargetAdapter;
import com.example.proyectofinal_deint_v1.ui.boxData.exercise.ExerciseListFragment;
import com.example.proyectofinal_deint_v1.ui.boxData.exercise.ExerciseListFragmentDirections;
import com.example.proyectofinal_deint_v1.ui.boxData.exercise.ExercisePresenter;
import com.example.proyectofinal_deint_v1.ui.confirmDialog.ExerciseDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class TargetListFragment extends Fragment implements  TargetContract.View,TargetAdapter.onBoxDataClickListener{
    public static final String TAG = "TargetListFragment";
    private RecyclerView recyclerView;
    private FloatingActionButton btnAdd;
    private TargetPresenter presenter;
    private TargetAdapter adapter;
    private List repositoryList;
    private Target targetDeleted;

    @Override
    public void onStart() {
        super.onStart();

        if(getArguments() != null) {
            if (getArguments().getBoolean(ExerciseDialogFragment.CONFIRM_DELETE)) {
                targetDeleted = (Target) getArguments().getSerializable("deleted");
                presenter.deleteTarget(getContext(),targetDeleted);
            }
        }
        presenter.getRepository(getContext());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.rvTarget);
        btnAdd = view.findViewById(R.id.btnAddTarget);
        presenter = new TargetPresenter(this);
        //1.asigamos al recycler el adapter personalizado
        //2.Crea el diseño del REcycler VIew
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new TargetAdapter(repositoryList, (TargetAdapter.onBoxDataClickListener) TargetListFragment.this);
        recyclerView.setAdapter(adapter);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               TargetListFragmentDirections.ActionTargetListFragmentToEditTargetFragment action = TargetListFragmentDirections.actionTargetListFragmentToEditTargetFragment(null,true,null);
               NavHostFragment.findNavController(TargetListFragment.this).navigate(action);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_target_list, container, false);
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }
    @Override
    public void setNameEmptyError() {

    }

    @Override
    public void setExerciseNotExistsError() {

    }

    @Override
    public void setDateExpError() {

    }

    @Override
    public void onSuccessDelete() {
        adapter.delete(targetDeleted);
        showSnackBarDeleted();
    }

    @Override
    public void onSuccesAdd() {
        adapter.add(targetDeleted);
    }

    @Override
    public void onSuccesModify() {

    }

    @Override
    public void onSucess() {
    }

    @Override
    public void onClick(Target target) {
        TargetListFragmentDirections.ActionTargetListFragmentToEditTargetFragment action = TargetListFragmentDirections.actionTargetListFragmentToEditTargetFragment(target,false,target.getNameTarget());
        NavHostFragment.findNavController(TargetListFragment.this).navigate(action);
    }

    @Override
    public void onLongClick(Target target) {
        targetDeleted = target;
        showDeleteDialog(target);
    }

    private void showDeleteDialog(Target target) {
        Bundle bundle = new Bundle();
        bundle.putString(ExerciseDialogFragment.TITLE, getString(R.string.title_delete));
        bundle.putString(ExerciseDialogFragment.MESSAGE, getString(R.string.message_delete_exercise,target.getNameTarget()));
        bundle.putSerializable("deleted",target);
        NavHostFragment.findNavController(TargetListFragment.this).navigate(R.id.action_targetListFragment_to_targetDialogFragment,bundle);

    }

    private void showSnackBarDeleted() {
        //Aquí se muestra el snackbar
        Snackbar.make(getView(),  getString(R.string.message_delete_exercise,targetDeleted.getNameTarget()), Snackbar.LENGTH_SHORT)
                .setAction(getString(R.string.undo), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        undoDeleted();
                    }
                }).setDuration(Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {

    }

    private void undoDeleted(){
        presenter.addTarget(getContext(),targetDeleted);
    }

    @Override
    public void onSuccess(List repository) {
        this.repositoryList = repository;
        adapter.updateData(repositoryList);
    }
}