package com.example.proyectofinal_deint_v1.ui.chartPage.exercise;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;

import com.example.proyectofinal_deint_v1.R;
import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.Exercise;
import com.example.proyectofinal_deint_v1.ui.boxData.exercise.EditExerciseFragment;
import com.example.proyectofinal_deint_v1.ui.boxData.exercise.muscle.MuscleListFragment;
import com.example.proyectofinal_deint_v1.ui.utils.CommonUtils;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.security.spec.ECField;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.PieChartView;

public class ChartExerciseFragment extends Fragment implements ChartExerciseContract.View {
    private Exercise exercise;
    private TextInputLayout tilMuscles;
    private TextInputEditText tieMuscles;
    private CheckBox spnType;
    private ChartExerciseContract.Presenter presenter;
    private List<Exercise> repository;
    private Button btnFilter;
    private static int MAXTYPES = 3;
    private TreeMap<Integer,List<Exercise>> repByType = new TreeMap<>();
    private List<Exercise> totalRep;

    private PieChartView chart;
    private PieChartData data;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            if(getArguments().getSerializable("exercise")!=null)
            exercise = (Exercise) getArguments().getSerializable("exercise");
        } // This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                NavHostFragment.findNavController(ChartExerciseFragment.this).navigate(R.id.chartBoxFragment);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tilMuscles = view.findViewById(R.id.tilMuscles);
        tieMuscles = view.findViewById(R.id.tieMuscles);
        spnType = view.findViewById(R.id.chart_spnType);
        btnFilter = view.findViewById(R.id.btnFilterExerciseChart);
        chart = view.findViewById(R.id.exerciseChart);
        presenter = new ChartExercisePresenter(this);
        presenter.getRepositoryTotal(getContext());
        loadDataInputs();
        tieMuscles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("addMode",false);
                bundle.putSerializable("exercise",exercise);
                bundle.putSerializable("oldExercise",null);
                NavHostFragment.findNavController(ChartExerciseFragment.this).navigate(R.id.muscleListFragment,bundle);
            }
        });
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Exercise tmp = new Exercise();
                tmp.setMainMuscles(exercise == null ? new ArrayList<>() : exercise.getMainMuscles());
                if(tmp.getMainMuscles().size() <= 0 && !spnType.isChecked()){
                    Snackbar.make(getView(), getString(R.string.err_nofilters_add),Snackbar.LENGTH_SHORT).show();
                }
                else {
                    presenter.getRepositoryExercise(getContext(), tmp);
                }
        }});
    }

    private void loadDataInputs(){
        tieMuscles.setText(CommonUtils.getMusclesList(exercise == null ? new ArrayList<>():exercise.getMainMuscles() ,true));
    }

    private void generateData() {
        String[] types = getResources().getStringArray(R.array.typeExercise);
        List<SliceValue> values = new ArrayList<SliceValue>();
        DecimalFormat df= new DecimalFormat("#.##");
        for (int i = 0; i < repByType.size(); ++i) {
            if(repByType.get(i).size() > 0) {
                float porcentaje = (float)(repByType.get(i).size())/(float)(getTotalDatCount(repByType));
                SliceValue sliceValue = new SliceValue(porcentaje, ChartUtils.COLORS[i]);
                sliceValue.setLabel(types[i] + " " + df.format(porcentaje * 100) + "%");
                values.add(sliceValue);
            }
        }
        data = new PieChartData(values);
        data.setHasLabels(true);
        chart.setPieChartData(data);
    }

    private void generateData2() {
        String[] types = getResources().getStringArray(R.array.typeExercise);
        List<SliceValue> values = new ArrayList<SliceValue>();
        DecimalFormat df= new DecimalFormat("#.##");
        float porcentaje = (float)(repository.size())/(float)(totalRep.size());
        porcentaje *= 100;
        SliceValue sliceValue = new SliceValue(porcentaje, ChartUtils.COLOR_BLUE);
        String mainMuscles = exercise.getMainMuscles().size() > 2 ? getString(R.string.tvMuscleMain) : CommonUtils.getMusclesList(exercise.getMainMuscles(),true);
        sliceValue.setLabel(mainMuscles + " " + df.format(porcentaje)+ "%");
        values.add(sliceValue);
        SliceValue sliceValue2 = new SliceValue(((float)100)-porcentaje, ChartUtils.COLOR_GREEN);
        sliceValue2.setLabel("Others" + " " + df.format(sliceValue2.getValue()) + "%");
        values.add(sliceValue2);
        data = new PieChartData(values);
        data.setHasLabels(true);
        chart.setPieChartData(data);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chart_exercise, container, false);

    }

    @Override
    public void setEmptyRepositoryWorkDataError() {

    }

    @Override
    public void setFireBaseConnectionError() {

    }

    @Override
    public void onSuccessExerciseList(List<Exercise> exerciseList) {
        if(spnType.isChecked()){
            //M??todo para trozear en listados por tipo de ejercicio.
            castList_toTreeMap(exerciseList);
            generateData();
        }
        else if(exercise.getMainMuscles().size() > 0) {
            repository = exerciseList;
            generateData2();
        }
    }

    @Override
    public void onSuccessExerciseTotalList(List<Exercise> exerciseList) {
        totalRep = exerciseList;
    }

    private int getTotalDatCount(TreeMap<Integer,List<Exercise>> treeMap){
        int count = 0;
        for (int i = 0; i < treeMap.size(); ++i) {
            count += treeMap.get(i).size();
        }
        return count;
    }

    private void castList_toTreeMap(List<Exercise> exerciseList){
            //Inicializamos los tres tipos de listados por tipo de ejercicio
            for (int i = 0; i < MAXTYPES; i++) { // MAXTYPES = 3
                repByType.put(i, new ArrayList<>());
            }
            for (int i = 0; i < exerciseList.size(); i++) {
                repByType.get(exerciseList.get(i).getTypeExercise()).add(exerciseList.get(i));
            }
    }
}