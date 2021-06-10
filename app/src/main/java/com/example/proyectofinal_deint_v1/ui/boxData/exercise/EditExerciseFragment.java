package com.example.proyectofinal_deint_v1.ui.boxData.exercise;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.fragment.NavHostFragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.example.proyectofinal_deint_v1.R;
import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.Exercise;
import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.Muscle;
import com.example.proyectofinal_deint_v1.ui.boxData.exercise.muscle.MuscleListFragment;
import com.example.proyectofinal_deint_v1.ui.chartPage.exercise.ChartExerciseFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class EditExerciseFragment extends Fragment implements ExerciseContract.View {
    private EditText edName;
    private TextInputLayout tilName;
    private Spinner spnType;
    private EditText edMuscles;
    private TextInputLayout tilMuscles;
    private ImageButton btnAddListMuscles;
    private EditText edDescrition;
    private EditText edNotes;
    private EditText edMaterial;
    private FloatingActionButton floatingActionButton;
    private Exercise exercise;
    private String[] arrayMuscles;
    private ExerciseContract.Presenter presenter;
    private Exercise oldExercise;
    private boolean addMode;

    @Override
    public void onStart() {
        super.onStart();
        //Gracias al objeto recibido por el fragment, vemos si es para modificar/borrar un ejercicio o añadirlo.
        if(addMode){
            if(exercise == null) {
                exercise = new Exercise();
            }
        }

        loadDataInputs();
    }





    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        exercise = EditExerciseFragmentArgs.fromBundle(getArguments()).getExercise();
        addMode = EditExerciseFragmentArgs.fromBundle(getArguments()).getAddMode();
        oldExercise = EditExerciseFragmentArgs.fromBundle(getArguments()).getOldExercise();
        spnType = view.findViewById(R.id.spnType);
        edName = view.findViewById(R.id.tieNameExercise);
        tilName = view.findViewById(R.id.tilNameExercise);
        tilName.setErrorEnabled(false);
        edDescrition = view.findViewById(R.id.tiedescriptionExercise);
        edNotes = view.findViewById(R.id.tienotesExercise);
        edMuscles = view.findViewById(R.id.tieMusclesExercise);
        tilMuscles = view.findViewById(R.id.tilMusclesExercise);
        btnAddListMuscles = view.findViewById(R.id.imgBtn_listMusclesAdd);
        edMaterial = view.findViewById(R.id.tiematerialExercise);
        floatingActionButton = view.findViewById(R.id.btnEditExercise);
        arrayMuscles = getResources().getStringArray(R.array.muscles);
        presenter = new ExercisePresenter(this);
        edName.addTextChangedListener(new EditTextWatcher(edName));
        edMuscles.addTextChangedListener(new EditTextWatcher(edMuscles));
        //Configuramos los spiners, con los datos correspondientes...
        final ArrayAdapter adapter1 = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.typeExercise));
        //Seteamos el adapter a cada uno de los spinners.
        spnType.setAdapter(adapter1);
        //Cuando clica sobre el tipo del ejercicio, se modifica este campo
        spnType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                exercise.setTypeExercise(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        btnAddListMuscles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("addMode",addMode);
                bundle.putSerializable("exercise",catchFieldstoExercise());
                bundle.putSerializable("oldExercise",oldExercise);
                NavHostFragment.findNavController(EditExerciseFragment.this).navigate(R.id.muscleListFragment,bundle);
            }
        });

        //Cuando clicka para guardar la modificación o añadir un ejercicio
       floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!addMode){
                    try {
                        presenter.modifyExercise(getContext(),oldExercise,catchFieldstoExercise());
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    presenter.addExercise(getContext(),catchFieldstoExercise());
                }
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                Bundle bundle = new Bundle();
                bundle.putBoolean("isWorkData",getArguments().getBoolean("isWorkData"));
                NavHostFragment.findNavController(EditExerciseFragment.this).navigate(R.id.ExerciseListFragment,bundle);            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_exercise, container, false);
    }

    @Override
    public void onResume() {

        super.onResume();
    }

    private void loadDataInputs(){
        edName.setText(exercise.getName());
        spnType.setSelection(exercise.getTypeExercise());
        edMuscles.setText(getMusclesList(exercise.getMainMuscles() == null ? new ArrayList<>() : exercise.getMainMuscles()));
        edDescrition.setText(exercise.getDescription());
        edMaterial.setText(exercise.getMaterial());
        edNotes.setText(exercise.getNotes());
    }

    private String getMusclesList(List<Muscle> arraylist){
        String tmpMusc = "";
            for (Muscle muscle : arraylist) {
                tmpMusc += muscle.getName() + ",";
            }
            return (arraylist.size() >= 1) ? tmpMusc.substring(0, tmpMusc.length() - 1) : "";
    }

    //Método que recoge la información de los diferentes campos de texto y modifica las características del ejercicios/exercise
    private Exercise catchFieldstoExercise(){
        Exercise ex = new Exercise();
        ex.setName(edName.getText().toString());
        ex.setTypeExercise(spnType.getSelectedItemPosition());
        ex.setDescription(edDescrition.getText().toString());
        ex.setMaterial(edMaterial.getText().toString());
        ex.setNotes(edNotes.getText().toString());
        ex.setMainMuscles(exercise.getMainMuscles());
        return ex;
    }

    @Override
    public void onSuccess(List repository) {

    }

    //CLASES Y MÉTODOS PARA LANZAR ERRORES EN TIEMPO DE EJECUCIÓN
    // --> Clase
    class EditTextWatcher implements TextWatcher {
        private View view;

        EditTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.tieNameExercise:
                    validateName(edName.getText().toString());
                    break;
                case R.id.tieMusclesExercise:
                    validateMuscles(edMuscles.getText().toString());
                    break;
            }
        }
    }


    private void validateName(String name){
        if(name.isEmpty()){
            setNameEmptyError();
        }
        else{
            tilName.setErrorEnabled(false);
        }
    }

    private void validateMuscles(String muscle){
        if(muscle.isEmpty()){
            setMusclesEmptyError();
        }
        else{
            tilMuscles.setErrorEnabled(false);
        }
    }
    @Override
    public void setNameEmptyError() {
        tilName.setError(getString(R.string.err_nameEmpty));
    }

    @Override
    public void setExerciseExistsError() {
        Snackbar.make(getView(),getResources().getString(R.string.err_exerciseExists),Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void setMusclesEmptyError() {
        tilMuscles.setError(getString(R.string.err_musclesEmpty));
    }

    @Override
    public void onSuccesAdd() {
        Bundle bundle = new Bundle();
        bundle.putBoolean("isWorkData",false);
        NavHostFragment.findNavController(EditExerciseFragment.this).navigate(R.id.ExerciseListFragment,bundle);

    }

    @Override
    public void onSuccesModify() {
        Bundle bundle = new Bundle();
        bundle.putBoolean("isWorkData",false);
        NavHostFragment.findNavController(EditExerciseFragment.this).navigate(R.id.ExerciseListFragment,bundle);

    }

    @Override
    public void setDeleteExError() {

    }

    //region Metodos no utiles del contrato de la interfaz ExerciseContrac.View

    @Override
    public void onSucess() {
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }


    @Override
    public void onSuccessDelete() {

    }
    //endregion

    @Override
    public void setConnecctiontoRepositoryError() {

    }
}