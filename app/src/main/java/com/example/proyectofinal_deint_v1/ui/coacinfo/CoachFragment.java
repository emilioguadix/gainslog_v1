package com.example.proyectofinal_deint_v1.ui.coacinfo;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.proyectofinal_deint_v1.R;
import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.Exercise;
import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.workData.serie.Serie;
import com.example.proyectofinal_deint_v1.data.model.model.user.Request;
import com.example.proyectofinal_deint_v1.data.model.model.user.User;
import com.example.proyectofinal_deint_v1.ui.adapter.ExerciseAdapter;
import com.example.proyectofinal_deint_v1.ui.adapter.RequestAdapter;
import com.example.proyectofinal_deint_v1.ui.boxData.exercise.ExerciseListFragment;
import com.example.proyectofinal_deint_v1.ui.confirmDialog.ExerciseDialogFragment;
import com.example.proyectofinal_deint_v1.ui.confirmDialog.RequestDialogFragment;
import com.example.proyectofinal_deint_v1.ui.login.LoginContract;
import com.example.proyectofinal_deint_v1.ui.login.LoginPresenter;
import com.example.proyectofinal_deint_v1.ui.utils.CommonUtils;
import com.example.proyectofinal_deint_v1.ui.workData.serie.WorkDataFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;

public class CoachFragment extends Fragment implements LoginContract.View, RequestAdapter.onRequestClickListener {

    private NavigationView navigationView;
    private FloatingActionButton btnAdd;
    private LoginContract.Presenter presenter;
    private NestedScrollView nestedScrollView;
    private RecyclerView rvCoachRequest;
    private  String email;
    private String idToken;
    private RequestAdapter adapter;
    private List<Request> repository;
    private Request requestDeleted;
    private boolean permission;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter = new LoginPresenter(this);
        navigationView = getActivity().findViewById(R.id.navigation_view);
        navigationView.getMenu().getItem(2).setChecked(true);
        nestedScrollView = view.findViewById(R.id.llCoachClient);
        rvCoachRequest = view.findViewById(R.id.rvCoachRequest);
        btnAdd = view.findViewById(R.id.btnAddCoach);
        if(CommonUtils.isCoachUser(getContext())){
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
            rvCoachRequest.setLayoutManager(layoutManager);
            adapter = new RequestAdapter(new ArrayList<>(), (RequestAdapter.onRequestClickListener) CoachFragment.this);
            rvCoachRequest.setAdapter(adapter);
            btnAdd.setVisibility(View.GONE);
            rvCoachRequest.setVisibility(View.VISIBLE);
            nestedScrollView.setVisibility(View.GONE);
            presenter.getRequestList(getContext());
        }
        else {
            if (getArguments() != null && !getArguments().getString("email").isEmpty()) {
                email = getArguments().getString("email");
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                permission = sharedPreferences.getBoolean(getString(R.string.key_permission_coach),true);
                checkRequest();
            }
        }
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEditDialog();
            }
        });
    }

    private void checkRequest(){
        presenter.sendRequestCoach(getContext(),email,permission);
    }

    private void showEditDialog() {
        NavHostFragment.findNavController(CoachFragment.this).navigate(R.id.sendRequestFragment);
    }

    @Override
    public void onStart() {
        super.onStart();

        if(getArguments() != null) {
            if (getArguments().getBoolean(RequestDialogFragment.CONFIRM_DELETE)) {
                requestDeleted = (Request) getArguments().getSerializable("deleted");
                //Llamar al presenter para borrar la peticion
                presenter.updateRequest(getContext(),requestDeleted);
            }
            if (getArguments().getBoolean(RequestDialogFragment.CONFIRM_UPDATE)) {
                requestDeleted = (Request) getArguments().getSerializable("deleted");
                //Llamar al presenter para borrar la peticion
                presenter.deleteRequest(getContext(),requestDeleted);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_coach, container, false);
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void setAutenthicationError() {
    }

    @Override
    public void setWebServConnectionError() {

    }

    @Override
    public void onSuccessUser(User user) {

    }

    @Override
    public void onSuccessDelete(Request request) {
        adapter.delete(request);
    }

    @Override
    public void onSuccessUpdate() {
        Snackbar.make(getView(), getResources().getString(R.string.suc_update_request), Snackbar.LENGTH_SHORT).show();
        presenter.getRequestList(getContext());
    }

    @Override
    public void onSuccessRequest(List<Request> list) {
        repository = list;
        adapter.updateData(list);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(getString(R.string.key_coach_count_request),repository.size());
        editor.commit();
        //Recoge el listado y del cliente el cual ha aceptado mira aver si tiene permisos de modificado
        checkPermission(repository);
    }

    private void checkPermission(List<Request> list){
        int perm = 0;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        for(int i = 0; i < list.size(); i++){
            if(list.get(i).get_accepted() == 1){
                perm = list.get(i).getPerm();
            }
        }
        editor.putBoolean(getString(R.string.key_permission_coach),perm == 1 ? true:false );
        editor.apply();
        editor.commit();
        boolean b = sharedPreferences.getBoolean(getString(R.string.key_permission_coach),false);
    }

    @Override
    public void setUserEmptyError() {
        Snackbar.make(getView(), getResources().getString(R.string.err_request_doneprev), Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void setPasswordEmptyError() {

    }

    @Override
    public void setPasswordFormatError() {

    }


    @Override
    public void setEmailNotVerifiedError() {
        Snackbar.make(getView(), getResources().getString(R.string.email_not_exist), Snackbar.LENGTH_SHORT).show();
    }
    @Override
    public void onSucess() {
        Snackbar.make(getView(), getResources().getString(R.string.request_send_successful), Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(Request request) {

    }

    @Override
    public void onLongClick(Request request) {
        requestDeleted= request;
        showDeleteDialog(request);
    }


    private void showDeleteDialog(Request request) {
        Bundle bundle = new Bundle();
        bundle.putString(ExerciseDialogFragment.MESSAGE, getString(R.string.message_accept_colaboration)  + request.getClientEmail());
        bundle.putSerializable("deleted",request);
        NavHostFragment.findNavController(CoachFragment.this).navigate(R.id.requestDialogFragment,bundle);

    }

    @Override
    public void onClick(View view) {

    }
}