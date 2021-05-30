package com.example.proyectofinal_deint_v1.ui.homePage;

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
import android.widget.TextView;
import com.example.proyectofinal_deint_v1.R;
import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.Exercise;
import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.workData.WorkData;
import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.workData.serie.Serie;
import com.example.proyectofinal_deint_v1.ui.adapter.TargetAdapter;
import com.example.proyectofinal_deint_v1.ui.adapter.WorkDataAdapter;
import com.example.proyectofinal_deint_v1.ui.boxData.exercise.ExerciseListFragment;
import com.example.proyectofinal_deint_v1.ui.boxData.exercise.ExerciseListFragmentArgs;
import com.example.proyectofinal_deint_v1.ui.boxData.target.TargetListFragment;
import com.example.proyectofinal_deint_v1.ui.confirmDialog.ExerciseDialogFragment;
import com.example.proyectofinal_deint_v1.ui.workData.serie.WorkDataFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class HomeFragment extends Fragment implements HomeFragmentContract.View,WorkDataAdapter.onWorkDataClickListener {

    private FloatingActionButton btnWorkData;
    private FloatingActionButton btnBodyData;
    private RecyclerView rvWorkData;
    private HomeFragmentContract.Presenter presenter;
    private List<WorkData> repositoryWorkData;
    private WorkDataAdapter workDataAdapter;
    private WorkData workDataDeleted;

    @Override
    public void onStart() {
        super.onStart();
        if(getArguments() != null) {
            if (getArguments().getBoolean("delete")){
                workDataDeleted = (WorkData) getArguments().getSerializable("deleted");
                presenter.deleteWorkData(getContext(),workDataDeleted);
            }
        }
        presenter.getRepositoryWorkData(getContext());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnWorkData = view.findViewById(R.id.btnAddWorkData);
        btnBodyData = view.findViewById(R.id.btnAddBodyData);
        rvWorkData = view.findViewById(R.id.rvWorkData_hf);
        presenter = new HomeFragmentPresenter(this);
        //1.asigamos al recycler el adapter personalizado
        //2.Crea el diseño del REcycler VIew
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false);
        rvWorkData.setLayoutManager(layoutManager);
        workDataAdapter = new WorkDataAdapter(getContext(),repositoryWorkData, (WorkDataAdapter.onWorkDataClickListener) HomeFragment.this);
        rvWorkData.setAdapter(workDataAdapter);
        btnBodyData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(HomeFragment.this).navigate( HomeFragmentDirections.actionHomeFragmentToBodyDataFragment());
            }
        });
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
        SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd");
        return df.format(c.getTime());
    }

    @Override
    public void setEmptyRepositoryWorkDataError() {

    }

    @Override
    public void setFireBaseConnectionError() {

    }

    @Override
    public void onSuccessDeleteWorkData() {
        workDataAdapter.delete(workDataDeleted);
    }

    @Override
    public void onSuccessWorkData(List<WorkData> workData) {
        repositoryWorkData = workData;
        //Actualizar recyclerview/adapter
        workDataAdapter.updateData(repositoryWorkData);
    }

    @Override
    public void onClick(WorkData workData) {
        HomeFragmentDirections.ActionHomeFragmentToWorkDataFragment2 action = HomeFragmentDirections.actionHomeFragmentToWorkDataFragment2(workData);
        NavHostFragment.findNavController(HomeFragment.this).navigate(action);
    }

    @Override
    public void onLongClick(WorkData workData) {
        showDeleteDialog(workData);
    }

    private void showDeleteDialog(WorkData workData) {
        Bundle bundle = new Bundle();
        bundle.putString(ExerciseDialogFragment.TITLE, getString(R.string.title_delete));
        bundle.putString(ExerciseDialogFragment.MESSAGE, getString(R.string.message_delete_workData,workData.getNameExercise()));
        bundle.putBoolean("delete",false);
        bundle.putSerializable("deleted",workData);
        NavHostFragment.findNavController(HomeFragment.this).navigate(R.id.action_homeFragment_to_workDataDialogFragment,bundle);

    }

    @Override
    public void onClick(View view) {

    }
}