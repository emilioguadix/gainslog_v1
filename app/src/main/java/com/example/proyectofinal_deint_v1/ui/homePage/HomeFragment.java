package com.example.proyectofinal_deint_v1.ui.homePage;

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
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.proyectofinal_deint_v1.R;
import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.bodyData.BodyData;
import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.workData.WorkData;
import com.example.proyectofinal_deint_v1.ui.adapter.BodyDataAdapter;
import com.example.proyectofinal_deint_v1.ui.adapter.WorkDataAdapter;
import com.example.proyectofinal_deint_v1.ui.confirmDialog.ExerciseDialogFragment;
import com.example.proyectofinal_deint_v1.ui.utils.CommonUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class HomeFragment extends Fragment implements HomeFragmentContract.View,WorkDataAdapter.onWorkDataClickListener, BodyDataAdapter.onBodyDataClickListener {

    private FloatingActionButton btnWorkData;
    private FloatingActionButton btnBodyData;
    private RecyclerView rvWorkData;
    private RecyclerView rvBodyData;
    private HomeFragmentContract.Presenter presenter;
    private List<WorkData> repositoryWorkData;
    private List<BodyData> repositoryBodyData;
    private WorkDataAdapter workDataAdapter;
    private BodyDataAdapter bodyDataAdapter;
    private WorkData workDataDeleted;
    private BodyData bodyDataDeleted;
    private TextView tvNoItemsWork;
    private TextView tvNoItemsBody;
    private NavigationView navigationView;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public void onStart() {
        super.onStart();
        if(getArguments() != null) {
            if (getArguments().getBoolean("delete")){
                workDataDeleted = (WorkData) getArguments().getSerializable("deleted");
                presenter.deleteWorkData(getContext(),workDataDeleted);
            }
            if (getArguments().getBoolean("deleteBody")){
                bodyDataDeleted = (BodyData) getArguments().getSerializable("deletedBody");
                presenter.deleteBodyData(getContext(),bodyDataDeleted);
            }
        }
        presenter.getRepositoryWorkData(getContext());
        presenter.getRepositoryBodyData(getContext());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnWorkData = view.findViewById(R.id.btnAddWorkData);
        btnBodyData = view.findViewById(R.id.btnAddBodyData);
        rvWorkData = view.findViewById(R.id.rvWorkData_hf);
        rvBodyData = view.findViewById(R.id.rvBodyData_hf);
        tvNoItemsBody = view.findViewById(R.id.noItemsBody);
        tvNoItemsWork = view.findViewById(R.id.noItemsWork);
        navigationView = getActivity().findViewById(R.id.navigation_view);
        swipeRefreshLayout = view.findViewById(R.id.swHome);
        navigationView.getMenu().getItem(0).setChecked(true);
        presenter = new HomeFragmentPresenter(this);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        if(CommonUtils.isCoachUser(getContext())){
            boolean tmp = sharedPreferences.getBoolean(getString(R.string.key_permission_coach),false);
              if(tmp){
                btnWorkData.setVisibility(View.VISIBLE);
            }
            else {
                btnWorkData.setVisibility(View.GONE);
            }
            btnBodyData.setVisibility(View.GONE);
        }
        //.asigamos al recycler el adapter personalizado
        //2.Crea el dise??o del REcycler VIew
        rvWorkData.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false));
        rvBodyData.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false));
        workDataAdapter = new WorkDataAdapter(getContext(),repositoryWorkData, (WorkDataAdapter.onWorkDataClickListener) HomeFragment.this);
        bodyDataAdapter = new BodyDataAdapter(getContext(),repositoryBodyData, (BodyDataAdapter.onBodyDataClickListener) HomeFragment.this);
        rvWorkData.setAdapter(workDataAdapter);
        rvBodyData.setAdapter(bodyDataAdapter);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.getRepositoryBodyData(getContext());
                presenter.getRepositoryWorkData(getContext());
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        btnBodyData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(HomeFragment.this).navigate( HomeFragmentDirections.actionHomeFragmentToBodyDataFragment());
            }
        });
        btnWorkData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("isWorkData",true);
               NavHostFragment.findNavController(HomeFragment.this).navigate(R.id.ExerciseListFragment,bundle);
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
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
        presenter.getRepositoryWorkData(getContext());
    }

    @Override
    public void onSuccessDeleteBodyData() {
        bodyDataAdapter.delete(bodyDataDeleted);
        presenter.getRepositoryBodyData(getContext());
    }

    @Override
    public void onSuccessWorkData(List<WorkData> workData) {
        repositoryWorkData = workData;
        if(repositoryWorkData.size() > 0){tvNoItemsWork.setVisibility(View.GONE);}
        else{tvNoItemsWork.setVisibility(View.VISIBLE);}
        //Actualizar recyclerview/adapter
        workDataAdapter.updateData(repositoryWorkData);
    }

    @Override
    public void onSuccessBodyData(List<BodyData> bodyData) {
        repositoryBodyData = bodyData;
        if(repositoryBodyData.size() > 0){tvNoItemsBody.setVisibility(View.GONE);}
        else{tvNoItemsBody.setVisibility(View.VISIBLE);}
        bodyDataAdapter.updateData(repositoryBodyData);
    }

    @Override
    public void onClick(WorkData workData) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("workData",workData);
        bundle.putBoolean("addMode",false);
        bundle.putBoolean("boxMode",false);
        NavHostFragment.findNavController(HomeFragment.this).navigate(R.id.workDataFragment,bundle);
    }

    @Override
    public void onLongClick(WorkData workData) {
        if(!CommonUtils.isCoachUser(getContext())) {
            showDeleteDialog(workData);
        }
    }

    private void showDeleteDialog(WorkData workData) {
        Bundle bundle = new Bundle();
        bundle.putString(ExerciseDialogFragment.TITLE, getString(R.string.title_delete));
        bundle.putString(ExerciseDialogFragment.MESSAGE, getString(R.string.message_delete_workData,workData.getNameExercise()));
        bundle.putBoolean("delete",false);
        bundle.putSerializable("deleted",workData);
        NavHostFragment.findNavController(HomeFragment.this).navigate(R.id.action_homeFragment_to_workDataDialogFragment,bundle);

    }

    private void showDeleteDialog(BodyData bodyData) {
        Bundle bundle = new Bundle();
        bundle.putString(ExerciseDialogFragment.TITLE, getString(R.string.title_delete));
        bundle.putString(ExerciseDialogFragment.MESSAGE, getString(R.string.message_delete_workData,"ID["+String.valueOf(bodyData.getId()) + "]"));
        bundle.putBoolean("deleteBody",false);
        bundle.putSerializable("deletedBody",bodyData);
        NavHostFragment.findNavController(HomeFragment.this).navigate(R.id.bodyDataDialogFragment,bundle);

    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onClick(BodyData bodyData) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("modify",true);
        bundle.putSerializable("body",bodyData);
        NavHostFragment.findNavController(HomeFragment.this).navigate(R.id.bodyDataFragment,bundle);
    }

    @Override
    public void onLongClick(BodyData bodyData) {
        if(!CommonUtils.isCoachUser(getContext())) {
            showDeleteDialog(bodyData);
        }
    }
}