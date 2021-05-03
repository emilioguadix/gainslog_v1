package com.example.proyectofinal_deint_v1.ui.boxData.exercise;

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
import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.workData.WorkData;
import com.example.proyectofinal_deint_v1.ui.adapter.ExerciseAdapter;
import com.example.proyectofinal_deint_v1.ui.confirmDialog.ExerciseDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class ExerciseListFragment extends Fragment implements ExerciseContract.View, ExerciseAdapter.onBoxDataClickListener{
    public static final String TAG = "ExerciseListFragment";
    private RecyclerView recyclerView;
    private FloatingActionButton btnAdd;
    private ExercisePresenter presenter;
    private ExerciseAdapter adapter;
    private static String[] typeData = null;
    private List repositoryList;
    private Exercise exerciseDeleted;
    private SharedPreferences sharedPreferences;
    private boolean sortList = false;

    @Override
    public void onStart() {
        super.onStart();

        if(getArguments() != null) {
            if (getArguments().getBoolean(ExerciseDialogFragment.CONFIRM_DELETE)) {
                exerciseDeleted = (Exercise) getArguments().getSerializable("deleted");
                presenter.deleteExercise(getContext(),exerciseDeleted);
            }
            if(ExerciseListFragmentArgs.fromBundle(getArguments()).getIsWorkData()) {
                btnAdd.setVisibility(View.GONE);
            }
        }
        //Preguntar si quiere tener el listado de ejercicios(en este caso) ordenado por el nombre
        //Guardamos las preferencias del usuario
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sortList = sharedPreferences.getBoolean(getString(R.string.key_sortExercise),true);
        if(sortList){presenter.sortListExercise(getContext());}
        else{presenter.getRepository(getContext());}
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        repositoryList = new ArrayList<>();
        typeData = getResources().getStringArray(R.array.typeBoxData);
        presenter = new ExercisePresenter(this);
        recyclerView = view.findViewById(R.id.recyclerViewBoxdata);
        btnAdd = view.findViewById(R.id.btnAddTypedata);
        final ArrayAdapter adapterTmp = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,typeData);
        //1.asigamos al recycler el adapter personalizado
        //2.Crea el diseño del REcycler VIew
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ExerciseAdapter(repositoryList, (ExerciseAdapter.onBoxDataClickListener) ExerciseListFragment.this);
        recyclerView.setAdapter(adapter);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExerciseListFragmentDirections.ActionBoxDataFragmentToEditExerciseFragment2 action = ExerciseListFragmentDirections.actionBoxDataFragmentToEditExerciseFragment2(null,true,null);
                NavHostFragment.findNavController(ExerciseListFragment.this).navigate(action);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_exercise_list, container, false);
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void setConnecctiontoRepositoryError() {
        Toast.makeText(getContext(),getResources().getString(R.string.err_ConectionRepositoory),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setNameEmptyError() {

    }

    @Override
    public void setExerciseExistsError() {

    }

    @Override
    public void setMusclesEmptyError() {

    }
    @Override
    public void onSuccessDelete() {
        adapter.delete(exerciseDeleted);
        showSnackBarDeleted();
    }

    @Override
    public void onSuccesAdd() {
        adapter.add(exerciseDeleted);
    }

    @Override
    public void onSuccesModify() {

    }

    @Override
    public void setDeleteExError() {
        Snackbar.make(getView(),getString(R.string.err_deleteEx_exception),Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onSucess() {
    }

    @Override
    public void onClick(Exercise exercise) {
        if(ExerciseListFragmentArgs.fromBundle(getArguments()).getIsWorkData()){
            WorkData workData = new WorkData();
            workData.setIdExercise(exercise.getId());
            ExerciseListFragmentDirections.ActionExerciseListFragmentToWorkDataFragment action = ExerciseListFragmentDirections.actionExerciseListFragmentToWorkDataFragment(workData);
            NavHostFragment.findNavController(ExerciseListFragment.this).navigate(action);

        }
        else {
            //Cargar Fragment para permitir modificar el objeto ejercicio
            ExerciseListFragmentDirections.ActionBoxDataFragmentToEditExerciseFragment2 action = ExerciseListFragmentDirections.actionBoxDataFragmentToEditExerciseFragment2(exercise, false, exercise);
            NavHostFragment.findNavController(ExerciseListFragment.this).navigate(action);
        }
    }

    @Override
    public void onLongClick(Exercise exercise) {
        exerciseDeleted = exercise;
        showDeleteDialog(exercise);
    }

    private void showDeleteDialog(Exercise exercise) {
        Bundle bundle = new Bundle();
        bundle.putString(ExerciseDialogFragment.TITLE, getString(R.string.title_delete));
        bundle.putString(ExerciseDialogFragment.MESSAGE, getString(R.string.message_delete_exercise,exercise.getName()));
        bundle.putSerializable("deleted",exercise);
        NavHostFragment.findNavController(ExerciseListFragment.this).navigate(R.id.action_BoxDataFragment_to_baseDialogFragment2,bundle);

    }

    private void showSnackBarDeleted() {
        //Aquí se muestra el snackbar
        Snackbar.make(getView(),  getString(R.string.message_delete_exercise,exerciseDeleted.getName()), Snackbar.LENGTH_SHORT)
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
        presenter.addExercise(getContext(),exerciseDeleted);
    }

    @Override
    public void onSuccess(List repository) {
        this.repositoryList = repository;
        adapter.updateData(repositoryList);
    }
}