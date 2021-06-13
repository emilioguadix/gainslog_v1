package com.example.proyectofinal_deint_v1.ui.boxData.exercise;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
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

public class ExerciseListFragment extends Fragment implements ExerciseContract.View, ExerciseAdapter.onBoxDataClickListener {
    public static final String TAG = "ExerciseListFragment";
    private RecyclerView recyclerView;
    private FloatingActionButton btnAdd;
    private ExercisePresenter presenter;
    private ExerciseAdapter adapter;
    private static String[] typeData = null;
    private List<Exercise> repositoryList;
    private Exercise exerciseDeleted;
    private SharedPreferences sharedPreferences;
    private boolean sortList = false;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public void onStart() {
        super.onStart();

        if(getArguments() != null) {
            if (getArguments().getBoolean(ExerciseDialogFragment.CONFIRM_DELETE)) {
                exerciseDeleted = (Exercise) getArguments().getSerializable("deleted");
                presenter.deleteExercise(getContext(),exerciseDeleted);
            }
            if(getArguments().getBoolean("isWorkData")) {
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


    // calling on create option menu
    // layout to inflate our menu file.
    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater menuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater);
        menu.clear();
        menuInflater.inflate(R.menu.search_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.actionSearch);
        SearchView searchView = (SearchView) searchItem.getActionView();
        // below line is to call set on query text listener method.
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // inside on query text change method we are
                // calling a method to filter our recycler view.
                filter(newText);
                return true;
            }
        });
    }

    private void filter(String text) {
        // creating a new array list to filter our data.
        List<Exercise> filteredlist = new ArrayList<>();
        // running a for loop to compare elements.
        for (Exercise item : repositoryList) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                // if the item is matched we are
                // adding it to our filtered list.
                filteredlist.add(item);
            }
        } adapter.updateData(filteredlist);
    }


        @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
            OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
                @Override
                public void handleOnBackPressed() {
                    if(btnAdd.getVisibility() == View.GONE) { // VIENE DE WORKDATA
                        NavHostFragment.findNavController(ExerciseListFragment.this).navigate(R.id.homeFragment);
                    }
                    else{
                        NavHostFragment.findNavController(ExerciseListFragment.this).navigate(R.id.boxDataFragment);
                    }                }
            };
            requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        repositoryList = new ArrayList<>();
        typeData = getResources().getStringArray(R.array.typeBoxData);
        presenter = new ExercisePresenter(this);
        recyclerView = view.findViewById(R.id.recyclerViewBoxdata);
        btnAdd = view.findViewById(R.id.btnAddTypedata);
        swipeRefreshLayout = view.findViewById(R.id.swExerciseList);
        final ArrayAdapter adapterTmp = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,typeData);
        //1.asigamos al recycler el adapter personalizado
        //2.Crea el diseño del REcycler VIew
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ExerciseAdapter(repositoryList, (ExerciseAdapter.onBoxDataClickListener) ExerciseListFragment.this);
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(sortList){presenter.sortListExercise(getContext());}
                else{presenter.getRepository(getContext());}
                swipeRefreshLayout.setRefreshing(false);
            }
        });
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
    public void onResume() {
        super.onResume();

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
            workData.setTypeExercise(exercise.getTypeExercise());
            workData.setSerieList(new ArrayList<>());
            workData.setIdExercise(exercise.getId());
            Bundle bundle = new Bundle();
            bundle.putSerializable("workData",workData);
            bundle.putBoolean("addMode",true);
            bundle.putBoolean("isWorkData",btnAdd.getVisibility() == View.GONE ? true : false);
            NavHostFragment.findNavController(ExerciseListFragment.this).navigate(R.id.workDataFragment,bundle);

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