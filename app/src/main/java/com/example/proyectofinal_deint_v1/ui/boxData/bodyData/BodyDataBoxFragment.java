package com.example.proyectofinal_deint_v1.ui.boxData.bodyData;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.proyectofinal_deint_v1.R;
import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.bodyData.BodyData;
import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.workData.WorkData;
import com.example.proyectofinal_deint_v1.ui.adapter.BodyDataAdapter;
import com.example.proyectofinal_deint_v1.ui.adapter.WorkDataAdapter;
import com.example.proyectofinal_deint_v1.ui.chartPage.bodyData.ChartBodyDataContract;
import com.example.proyectofinal_deint_v1.ui.chartPage.bodyData.ChartBodyDataPresenter;
import com.example.proyectofinal_deint_v1.ui.chartPage.workData.ChartWorkDataContract;
import com.example.proyectofinal_deint_v1.ui.chartPage.workData.ChartWorkDataPresenter;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class BodyDataBoxFragment extends Fragment implements ChartBodyDataContract.View, BodyDataAdapter.onBodyDataClickListener {

    private String date;
    private ChartBodyDataContract.Presenter presenter;
    private List<BodyData> repository;
    private BodyDataAdapter bodyDataAdapter;
    private RecyclerView rvBodyDataBox;
    private String setDate;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                NavHostFragment.findNavController(BodyDataBoxFragment.this).navigate(R.id.boxDataFragment);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    // calling on create option menu
    // layout to inflate our menu file.
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater);
        menu.clear();
        menuInflater.inflate(R.menu.dateset_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionDate:
                getDatePickerCurrentTime().show();
                return true;
        }
        return true;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter = new ChartBodyDataPresenter(this);
        //2.Crea el diseño del REcycler VIew
        rvBodyDataBox = view.findViewById(R.id.rvBodyDataBox);
        swipeRefreshLayout = view.findViewById(R.id.swBodyBox);
        rvBodyDataBox.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL,false));
        bodyDataAdapter = new BodyDataAdapter(getContext(),new ArrayList<>(), (BodyDataAdapter.onBodyDataClickListener) BodyDataBoxFragment.this);
        rvBodyDataBox.setAdapter(bodyDataAdapter);
        if(getArguments() != null && !getArguments().getString("setDate").isEmpty()){
            setDate = getArguments().getString("setDate");
            presenter.getRepositoryBodyData_ByDate(getContext(),setDate,setDate);
        }
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.getRepositoryBodyData_ByDate(getContext(),setDate,setDate);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    //Devuelve un cuadrado de dialogo para seleccionar una fecha, y además guarda el resultado en los text inputs.
    private DatePickerDialog getDatePickerCurrentTime(){
        Calendar cInit = Calendar.getInstance();
        DatePickerDialog dialog =  new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                date = String.format("%04d", year) +"-"+String.format("%02d", monthOfYear+1)
                        +"-"+String.format("%02d", dayOfMonth);
                presenter.getRepositoryBodyData_ByDate(getContext(),date,date);
            }
        }, cInit.get(Calendar.YEAR), cInit.get(Calendar.MONTH), cInit.get(Calendar.DAY_OF_MONTH));
        dialog.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());
        return dialog;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_body_data_box, container, false);
    }

    @Override
    public void setEmptyRepositoryWorkDataError() {
        Snackbar.make(getView(),getString(R.string.err_empty_repository),Snackbar.LENGTH_SHORT).show();
        bodyDataAdapter.updateData(new ArrayList<>());
    }

    @Override
    public void setFireBaseConnectionError() {
        Snackbar.make(getView(),getString(R.string.err_firebaseConnection),Snackbar.LENGTH_SHORT).show();

    }

    @Override
    public void onSuccessBodyData(List<BodyData> bodyData) {
        repository = bodyData;
        bodyDataAdapter.updateData(repository);
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onClick(BodyData bodyData) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("body",bodyData);
        bundle.putBoolean("boxMode",true);
        bundle.putString("setDate",date);
        NavHostFragment.findNavController(BodyDataBoxFragment.this).navigate(R.id.bodyDataFragment,bundle);

    }

    @Override
    public void onLongClick(BodyData bodyData) {

    }
}