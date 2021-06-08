package com.example.proyectofinal_deint_v1.ui.boxData.exercise.muscle;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.proyectofinal_deint_v1.R;
import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.Exercise;
import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.Muscle;
import com.example.proyectofinal_deint_v1.ui.chartPage.exercise.ChartExerciseFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class MuscleListFragment extends Fragment implements MuscleListContract.View{
    private ListView listMuscle;
    private FloatingActionButton btnConfirm;
    private List<Muscle> muscleList;
    private List<Muscle> arrayMuscleSelected;
    private Exercise exercise;
    private CoordinatorLayout coordinatorLayout;
    private boolean addMode;
    private MuscleListContract.Presenter presenter;
    private Exercise oldExercise;
    private MuscleListContract listener;
    private boolean isChartFragment;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        presenter = new MusclePresenter(this);
        presenter.gotResponse(getContext());
        exercise = (Exercise) getArguments().getSerializable("exercise");
        addMode = getArguments().getBoolean("addMode");
        oldExercise = (Exercise) getArguments().getSerializable("oldExercise");
        coordinatorLayout = view.findViewById(R.id.coordinatorMuscleList);
        listMuscle = view.findViewById(R.id.list_muscleMain);
        btnConfirm = view.findViewById(R.id.btnConfirmListMuscle);
        if(exercise != null) {
            arrayMuscleSelected = exercise.getMainMuscles();
        }
        //Si no contiene ninguno de estos argumentos quiere decir que la llamada a este fragmente proviene de ChartExerciseFragment
        if(!addMode && oldExercise == null){
            isChartFragment = true;
            listMuscle.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
            exercise  = new Exercise();
        }
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Navegar hacia el fragment anterior pasando como argumento un array de string,
                if(catchFieldsSelected().size() >= 1 || isChartFragment){
                    exercise.setMainMuscles(catchFieldsSelected());
                    if(!isChartFragment) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("exercise",exercise);
                        bundle.putBoolean("addMode",addMode);
                        bundle.putSerializable("oldExercise",oldExercise);
                        NavHostFragment.findNavController(MuscleListFragment.this).navigate(R.id.editExerciseFragment,bundle);
                    }
                    else {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("exercise",exercise);
                        bundle.putBoolean("addMode",addMode);
                        bundle.putSerializable("oldExercise",oldExercise);
                        NavHostFragment.findNavController(MuscleListFragment.this).navigate(R.id.chartExerciseFragment,bundle);
                    }
                }
                else{
                    Snackbar.make(coordinatorLayout,getResources().getString(R.string.err_musclesEmpty),Snackbar.LENGTH_SHORT).show();
                }

            }
        });
        super.onViewCreated(view, savedInstanceState);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_muscle_list, container, false);
    }

    private List<Muscle> catchFieldsSelected(){
        List<Muscle> list = new ArrayList<>();
        for (int i = 0; i < listMuscle.getCount(); i++){
            if(listMuscle.isItemChecked(i)){
                list.add(muscleList.get(i));
            }
        }
        return list;
    }

    private void checkMuscleListSelected(){
        if(exercise != null) {
            for (int i = 0; i < listMuscle.getCount(); i++) {
                for (int j = 0; j < arrayMuscleSelected.size(); j++) {
                    if (arrayMuscleSelected.get(j).getName().equals(listMuscle.getItemAtPosition(i).toString())) {
                        listMuscle.setItemChecked(i, true);
                    }
                }
            }
        }
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void setConnecctiontoRepositoryError() {
        Snackbar.make(getView(),getString(R.string.err_ConectionRepositoory),Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccess(List<Muscle> list) {
        muscleList = list;
        final ArrayAdapter adapter = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_multiple_choice,list);
        listMuscle.setAdapter(adapter);
        if(arrayMuscleSelected != null) {
            checkMuscleListSelected();
        }
    }

    @Override
    public void onSucess() {

    }
}