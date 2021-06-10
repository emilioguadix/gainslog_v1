package com.example.proyectofinal_deint_v1.ui.boxData;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.SearchView;

import com.example.proyectofinal_deint_v1.R;
import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.workData.WorkData;
import com.example.proyectofinal_deint_v1.ui.adapter.BodyDataAdapter;
import com.example.proyectofinal_deint_v1.ui.adapter.WorkDataAdapter;
import com.example.proyectofinal_deint_v1.ui.chartPage.workData.ChartWorkDataContract;
import com.example.proyectofinal_deint_v1.ui.chartPage.workData.ChartWorkDataPresenter;
import com.example.proyectofinal_deint_v1.ui.homePage.HomeFragment;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class WorkDataBoxFragment extends Fragment implements ChartWorkDataContract.View, WorkDataAdapter.onWorkDataClickListener {

    private String date;
    private ChartWorkDataContract.Presenter presenter;
    private List<WorkData> repository;
    private WorkDataAdapter workDataAdapter;
    private RecyclerView rvWorkDataBox;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    // calling on create option menu
    // layout to inflate our menu file.
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater);
        menu.clear();
        menuInflater.inflate(R.menu.dateset_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.actionDate);
        ImageButton searchView = (ImageButton) searchItem.getActionView();
        // below line is to call set on query text listener method.
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDatePickerCurrentTime().show();
            }
        });
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter = new ChartWorkDataPresenter(this);
        //2.Crea el diseño del REcycler VIew
        rvWorkDataBox = view.findViewById(R.id.rvWorkDataBox);
        rvWorkDataBox.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL,false));
        workDataAdapter = new WorkDataAdapter(getContext(),new ArrayList<>(), (WorkDataAdapter.onWorkDataClickListener) WorkDataBoxFragment.this);
        rvWorkDataBox.setAdapter(workDataAdapter);
    }

    //Devuelve un cuadrado de dialogo para seleccionar una fecha, y además guarda el resultado en los text inputs.
    private DatePickerDialog getDatePickerCurrentTime(){
        Calendar cInit = Calendar.getInstance();
        DatePickerDialog dialog =  new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                date = String.format("%04d", year) +"-"+String.format("%02d", monthOfYear+1)
                        +"-"+String.format("%02d", dayOfMonth);
                presenter.getRepositoryWorkData_ByDate(getContext(),date,date);
            }
        }, cInit.get(Calendar.YEAR), cInit.get(Calendar.MONTH), cInit.get(Calendar.DAY_OF_MONTH));
        dialog.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());
        return dialog;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_work_data_box, container, false);
    }

    @Override
    public void setEmptyRepositoryWorkDataError() {
        Snackbar.make(getView(),getString(R.string.err_empty_reps),Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void setFireBaseConnectionError() {
        Snackbar.make(getView(),getString(R.string.err_firebaseConnection),Snackbar.LENGTH_SHORT).show();

    }

    @Override
    public void onSuccessWorkData(List<WorkData> workData) {
        repository = workData;
        workDataAdapter.updateData(repository);
    }

    @Override
    public void onClick(WorkData workData) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("workData",workData);
        bundle.putBoolean("addMode",false);
        bundle.putBoolean("boxMode",true);
        NavHostFragment.findNavController(WorkDataBoxFragment.this).navigate(R.id.workDataFragment,bundle);
    }
    //No tiene función
    @Override
    public void onLongClick(WorkData workData) {

    }

    @Override
    public void onClick(View view) {

    }
}