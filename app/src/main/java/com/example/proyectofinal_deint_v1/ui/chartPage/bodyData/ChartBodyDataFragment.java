package com.example.proyectofinal_deint_v1.ui.chartPage.bodyData;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.proyectofinal_deint_v1.R;
import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.bodyData.BodyData;
import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.workData.WorkData;
import com.example.proyectofinal_deint_v1.ui.chartPage.workData.ChartWorkDataContract;
import com.example.proyectofinal_deint_v1.ui.chartPage.workData.ChartWorkDataPresenter;
import com.example.proyectofinal_deint_v1.ui.utils.CommonUtils;
import com.google.android.material.snackbar.Snackbar;
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
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

public class ChartBodyDataFragment extends Fragment implements ChartBodyDataContract.View{

    private TextInputLayout tilDateInit;
    private TextInputLayout tilDateFin;
    private TextInputEditText tieDateInit;
    private TextInputEditText tieDateFin;
    private CheckBox chWeight;
    private CheckBox chFatPer;
    private Calendar cInit;
    private Button btnFilter;
    private List<BodyData> repository;
    private ChartBodyDataContract.Presenter presenter;
    private LineChartView chartView;
    private TreeMap<String,BodyData> listChartData;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cInit = Calendar.getInstance();
        tilDateInit = view.findViewById(R.id.tilDateInit);
        tilDateFin = view.findViewById(R.id.tilDateFin);
        tieDateInit = view.findViewById(R.id.tieDateInit);
        tieDateFin = view.findViewById(R.id.tieDateFin);
        btnFilter = view.findViewById(R.id.btnFilterBodyDataChart);
        chartView = view.findViewById(R.id.bodyDataChart);
        chWeight = view.findViewById(R.id.chart_spnWeight);
        chFatPer = view.findViewById(R.id.chart_spnFatPer);
        chartView.setInteractive(true);
        presenter = new ChartBodyDataPresenter (this);

        tieDateInit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDatePickerCurrentTime(tieDateInit).show();
            }
        });
        tieDateFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDatePickerCurrentTime(tieDateFin).show();
            }
        });
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.getRepositoryBodyData_ByDate(getContext(),tieDateInit.getText().toString(),tieDateFin.getText().toString());
            }
        });
    }

    private void setAxisChartData(){
        LineChartData data = new LineChartData();
        List<AxisValue> axisXValues = new ArrayList<>();
        List<Map.Entry<String, BodyData>> entryList = new ArrayList<>(listChartData.entrySet());
        //Añadir a el eje x los valores de las fechas de los distintos workData encontrados.
        for (int i = 0; i < entryList.size(); i++){
            axisXValues.add(new AxisValue(i).setLabel(entryList.get(i).getKey()));
        }
        //Configuramos el eje X con sus valores correspondientes(valores -> fecha de ese registro)
        Axis axisX = new Axis(axisXValues);
        axisX.setHasTiltedLabels(false);  //Is the font on the X axis oblique or straight, and true oblique?
        axisX.setTextColor(Color.GRAY);  //Setting font color
        axisX.setTextSize(12);//Set font size
        axisX.setName(getString(R.string.axisXLineChartBodyData));
        data.setAxisXBottom(axisX);
        //Configuramos el eje Y con los valores que en este caso se hará de forma automática ->  nº de registros de ese día)
        Axis axisY = new Axis();
        axisY.setHasTiltedLabels(false);  //Is the font on the X axis oblique or straight, and true oblique?
        axisY.setTextColor(Color.GRAY);  //Setting font color
        axisY.setTextSize(12);//Set font size
        axisY.setName(getString(R.string.axisYLineChartBodyData));
        data.setAxisYLeft(axisY);
        //Los valores se extraen de un método(getChartDataValues()) que devuelve List<Line> compuesto de Point Values, con los valores ya cargados.
        data.setLines(getChartDataValues());
        chartView.setLineChartData(data);
        //Viewport para saber cuanto mostrará el gráfico como maximo, en este caso
        Viewport v = new Viewport(chartView.getMaximumViewport());
        v.top = (float) whatMaxValueChart(listChartData); //max value
        v.bottom = 0f;  //min value
        v.right = entryList.size();
        chartView.setMaximumViewport(v);
        chartView.setCurrentViewport(v);
    }

    //Recorre el treeMap y evalua cuál es el día con maximos registros logeados.
    //Nos sirve para saber cuantos valores  se deberán mostrar en el eje Y en la gráfica.
    private double whatMaxValueChart(TreeMap<String,BodyData> tree){
        double maxEntry = 0;
        for(Map.Entry<String, BodyData> entry : tree.entrySet()){
            if(entry.getValue().getWeight() > maxEntry)
                maxEntry = entry.getValue().getWeight();
        }
        return maxEntry;
    }

    private List<Line> getChartDataValues(){
        // Get entry set of the TreeMap
        // Converting entrySet to ArrayList
        List<Map.Entry<String, BodyData> > entryList
                = new ArrayList<>(listChartData.entrySet());
        List<Line> lines = new ArrayList<>();
        List<PointValue> values = new ArrayList<PointValue>();
        for (int j = 0; j < listChartData.size(); ++j) {
            String label = getLabelValue(entryList.get(j).getValue());
            values.add(new PointValue(j, (float)entryList.get(j).getValue().getWeight()).setLabel(label));
        }
        Line line = new Line(values);
        line.setColor(Color.BLUE);
        line.setHasLabels(true);
        lines.add(line);
        return lines;
    }

    private String getLabelValue(BodyData bodyData){
        String tmp = "";
        if(chWeight.isChecked()){
            tmp += String.valueOf(bodyData.getWeight()) + "Kg ";
        }
        if (chFatPer.isChecked()) {
            tmp += String.valueOf(bodyData.getFatPer()) + "% ";
        }
        return tmp;
    }

    //Devuelve un cuadrado de dialogo para seleccionar una fecha, y además guarda el resultado en los text inputs.
    private DatePickerDialog getDatePickerCurrentTime(TextInputEditText tieChange){
        DatePickerDialog dialog =  new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String fecha = String.format("%04d", year) +"-"+String.format("%02d", monthOfYear+1)
                        +"-"+String.format("%02d", dayOfMonth);
                tieChange.setText(fecha);
            }
        }, cInit.get(Calendar.YEAR), cInit.get(Calendar.MONTH), cInit.get(Calendar.DAY_OF_MONTH));
        dialog.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chart_body_data, container, false);
    }

    @Override
    public void setEmptyRepositoryWorkDataError() {
        Snackbar.make(getView(),  getString(R.string.err_empty_repository), Snackbar.LENGTH_SHORT).show();

    }

    @Override
    public void setFireBaseConnectionError() {

    }

    @Override
    public void onSuccessBodyData(List<BodyData> bodyData) {
        repository = bodyData;
        listChartData = castList_TreeMap(bodyData);
        setAxisChartData();
    }

    //Método que transforma el listado obtenido y lo pasa a TreeMap para que sea más facil
    //Operar con estos datos y pasarlo a las gráficas. Guardamos como key las fechas de los registros
    //Y como value, el último bodyData de ese día.
    private TreeMap<String,BodyData> castList_TreeMap(List<BodyData> bodyData){
        TreeMap<String,BodyData> tmp = new TreeMap<String, BodyData>();
        BodyData bodyDataTmp;
        for (int i = 0; i < bodyData.size(); i++){
            bodyDataTmp = bodyData.get(i);
            String date = CommonUtils.getStringfromCalendar(bodyDataTmp.getLogDate());
            if(!tmp.containsKey(date)){
                tmp.put(date,bodyDataTmp);
            }
        }
        return tmp;
    }
}