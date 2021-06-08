package com.example.proyectofinal_deint_v1.ui.chartPage.target;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.proyectofinal_deint_v1.R;
import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.Exercise;
import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.bodyData.BodyData;
import com.example.proyectofinal_deint_v1.data.model.model.target.Target;
import com.example.proyectofinal_deint_v1.ui.chartPage.bodyData.ChartBodyDataContract;
import com.example.proyectofinal_deint_v1.ui.chartPage.bodyData.ChartBodyDataPresenter;
import com.example.proyectofinal_deint_v1.ui.utils.CommonUtils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.LineChartView;
import lecho.lib.hellocharts.view.PieChartView;

public class ChartTargetFragment extends Fragment implements ChartTargetContract.View{

    private TextInputLayout tilDateFin;
    private TextInputEditText tieDateFin;
    private Calendar cInit;
    private Button btnFilter;
    private List<Target> repository;
    private ChartTargetContract.Presenter presenter;
    private PieChartView chart;
    private TreeMap<Integer,List<Target>> repByType = new TreeMap<>();
    private PieChartData data;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tilDateFin = view.findViewById(R.id.tilDateFinTarget);
        tieDateFin = view.findViewById(R.id.tieDateFinTarget);
        btnFilter = view.findViewById(R.id.btnFilterTargetChart);
        chart = view.findViewById(R.id.targetChart);
        chart.setInteractive(true);
        presenter = new ChartTargetPresenter(this);
        tieDateFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDatePickerCurrentTime(tieDateFin).show();
            }
        });
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.getRepositoryTarget_ByDate(getContext(),tieDateFin.getText().toString());
            }
        });
    }

    //Devuelve un cuadrado de dialogo para seleccionar una fecha, y además guarda el resultado en los text inputs.
    private DatePickerDialog getDatePickerCurrentTime(TextInputEditText tieChange){
        Calendar cInit = Calendar.getInstance();
        DatePickerDialog dialog =  new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String fecha = String.format("%04d", year) +"-"+String.format("%02d", monthOfYear+1)
                        +"-"+String.format("%02d", dayOfMonth);
                tieChange.setText(fecha);
            }
        }, cInit.get(Calendar.YEAR), cInit.get(Calendar.MONTH), cInit.get(Calendar.DAY_OF_MONTH));
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chart_target, container, false);
    }

    @Override
    public void setEmptyRepositoryTargetError() {
        Toast.makeText(getContext(),getString(R.string.err_empty_reps),Toast.LENGTH_SHORT).show();

    }

    @Override
    public void setFireBaseConnectionError() {

    }

    @Override
    public void onSuccessTarget(List<Target> targets) {
        repository = targets;
        castList_toTreeMap(repository);
        generateData();
    }

    private void generateData() {
        List<SliceValue> values = new ArrayList<SliceValue>();
        float porcentaje = (float)(repByType.get(0).size())/(float)(getTotalDatCount(repByType));
        SliceValue sliceValue = new SliceValue(porcentaje, ChartUtils.COLOR_GREEN);
        sliceValue.setLabel(getString(R.string.overcome) + " " + porcentaje+ "%");
        values.add(sliceValue);
        porcentaje = (float)(repByType.get(1).size())/(float)(getTotalDatCount(repByType));
        SliceValue sliceValue2 = new SliceValue(porcentaje, ChartUtils.COLOR_BLUE);
        sliceValue2.setLabel(getString(R.string.overcome_notYet)+ " " + porcentaje+ "%");
        values.add(sliceValue2);

        data = new PieChartData(values);
        data.setHasLabels(true);
        chart.setPieChartData(data);
    }

    private int getTotalDatCount(TreeMap<Integer,List<Target>> treeMap){
        int count = 0;
        for (int i = 0; i < treeMap.size(); ++i) {
            count += treeMap.get(i).size();
        }
        return count;
    }

    private void castList_toTreeMap(List<Target> targetList){
        //Inicializamos los tres tipos de listados por tipo de ejercicio
        repByType.put(0, new ArrayList<>()); //not overcome
        repByType.put(1, new ArrayList<>()); // overcome
        for (int i = 0; i < targetList.size(); i++) {
            repByType.get(targetList.get(i).getOvercome()).add(targetList.get(i));
        }
    }
}