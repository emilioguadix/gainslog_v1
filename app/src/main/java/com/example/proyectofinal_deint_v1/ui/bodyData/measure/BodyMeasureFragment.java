package com.example.proyectofinal_deint_v1.ui.bodyData.measure;

import android.os.Bundle;
import android.service.controls.actions.FloatAction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.proyectofinal_deint_v1.R;
import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.bodyData.BodyData;
import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.bodyData.Measurement;
import com.example.proyectofinal_deint_v1.ui.bodyData.BodyDataFragment;
import com.example.proyectofinal_deint_v1.ui.utils.CommonUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

public class BodyMeasureFragment extends Fragment{

    //region Fields
    private TextInputLayout tilNeck;
    private TextInputEditText tieNeck;
    private TextInputLayout tilChest;
    private TextInputEditText tieChest;
    private TextInputLayout tilShoulder;
    private TextInputEditText tieShoulder;
    private TextInputLayout tilWaist;
    private TextInputEditText tieWaist;
    private TextInputLayout tilHips;
    private TextInputEditText tieHips;
    private TextInputLayout tilAbd;
    private TextInputEditText tieAbd;
    private TextInputLayout tilBicepsL;
    private TextInputEditText tieBicepsL;
    private TextInputLayout tilBicepsR;
    private TextInputEditText tieBicepsR;
    private TextInputLayout tilQuadL;
    private TextInputEditText tieQuadL;
    private TextInputLayout tilQuadR;
    private TextInputEditText tieQuadR;
    private TextInputLayout tilCalfL;
    private TextInputEditText tieCalfL;
    private TextInputLayout tilCalfR;
    private TextInputEditText tieCalfR;
    //endregion
    private FloatingActionButton btnAddMeasures;
    List<Measurement> list;
    private BodyData bodyData;
    private boolean isModify;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments().getBoolean("modify")) {
            isModify = true;
        }

        if(getArguments().getSerializable("body") != null){
            bodyData = (BodyData) getArguments().getSerializable("body");
        }
        else{
            bodyData = new BodyData();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        list = new ArrayList<>();
        //region Fields id link
        tilNeck = view.findViewById(R.id.tilNeck);
        tieNeck = view.findViewById(R.id.tieNeck);
        tilAbd = view.findViewById(R.id.tilAbd);
        tieAbd = view.findViewById(R.id.tieAbd);
        tilChest = view.findViewById(R.id.tilChest);
        tieChest = view.findViewById(R.id.tieChest);
        tilShoulder = view.findViewById(R.id.tilShoulder);
        tieShoulder = view.findViewById(R.id.tieShoulder);
        tilCalfL = view.findViewById(R.id.tilCalfL);
        tieCalfL = view.findViewById(R.id.tieCalfL);
        tilCalfR = view.findViewById(R.id.tilCalfR);
        tieCalfR = view.findViewById(R.id.tieCalfR);
        tilQuadL = view.findViewById(R.id.tilQuadL);
        tieQuadL = view.findViewById(R.id.tieQuadL);
        tilQuadR = view.findViewById(R.id.tilQuadR);
        tieQuadR = view.findViewById(R.id.tieQuadR);
        tilBicepsL = view.findViewById(R.id.tilBicepsL);
        tieBicepsL = view.findViewById(R.id.tieBicepsL);
        tilBicepsR = view.findViewById(R.id.tilBicepsR);
        tieBicepsR = view.findViewById(R.id.tieBicepsR);
        tilWaist = view.findViewById(R.id.tilWaist);
        tieWaist = view.findViewById(R.id.tieWaist);
        tilHips = view.findViewById(R.id.tilHips);
        tieHips = view.findViewById(R.id.tieHips);
        btnAddMeasures = view.findViewById(R.id.btnAddMeasure);
        if(bodyData.getMeasurements() != null && bodyData.getMeasurements().size() > 0){
            setFields();
        }
        if(getArguments().getBoolean("boxMode") || CommonUtils.isCoachUser(getContext())){
            enableDisableViewGroup();
        }
        //endregion
        btnAddMeasures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                catchFields();
                navigateBack();
            }
        });
    }

    public void enableDisableViewGroup() {
        tieNeck.setEnabled(false);
        tieAbd.setEnabled(false);
        tieChest.setEnabled(false);
        tieBicepsL.setEnabled(false);
        tieBicepsR.setEnabled(false);
        tieCalfL.setEnabled(false);
        tieCalfR.setEnabled(false);
        tieQuadL.setEnabled(false);
        tieQuadR.setEnabled(false);
        tieShoulder.setEnabled(false);
        tieWaist.setEnabled(false);
        tieHips.setEnabled(false);
        btnAddMeasures.setVisibility(View.GONE);

    }

    private void catchFields(){
        addToListMeasureNotEmpty(tieNeck);
        addToListMeasureNotEmpty(tieAbd);
        addToListMeasureNotEmpty(tieChest);
        addToListMeasureNotEmpty(tieBicepsL);
        addToListMeasureNotEmpty(tieBicepsR);
        addToListMeasureNotEmpty(tieCalfL);
        addToListMeasureNotEmpty(tieCalfR);
        addToListMeasureNotEmpty(tieQuadL);
        addToListMeasureNotEmpty(tieQuadR);
        addToListMeasureNotEmpty(tieShoulder);
        addToListMeasureNotEmpty(tieWaist);
        addToListMeasureNotEmpty(tieHips);
    }

    private void addToListMeasureNotEmpty(TextInputEditText tie){
        if(!tie.getText().toString().isEmpty()){
            Measurement measurement = new Measurement();
            measurement.setIdMuscle(getIdMuscleByTieType(tie.getId()));
            measurement.setMeasure(Double.parseDouble(tie.getText().toString()));
            measurement.setSide(getSideMeasureByTieType(tie.getId()));
            list.add(measurement);
        }
    }

    //Devuelve el id del músculo correspondiente a texInputEditTExt.
    private int getIdMuscleByTieType(int tieId){
        switch (tieId){
            case R.id.tieNeck:
                return 1;
            case R.id.tieShoulder:
                return 5;
            case R.id.tieBicepsL: case R.id.tieBicepsR:
                return  7;
            case R.id.tieQuadL: case R.id.tieQuadR:
                return  11;
            case R.id.tieCalfL:case R.id.tieCalfR:
                return  12;
            case R.id.tieAbd:
                return  4;
            case R.id.tieChest:
                return  3;
            case R.id.tieHips:
                return  10;
            case R.id.tieWaist:
                return  17;
        }
        return 0;
    }

    //Rellena cada campo con su correspondiente valor.
    private void setFields(){
        TextInputEditText tie = new TextInputEditText(getContext());
        for(int i= 0; i < bodyData.getMeasurements().size(); i++){
            switch (bodyData.getMeasurements().get(i).getIdMuscle()){
                case 1:
                    tie = tieNeck;
                    break;
                case 5:
                    tie = tieShoulder;
                    break;
                case 7:
                    tie = (bodyData.getMeasurements().get(i).getSide().equals("L")) ? tieBicepsL : tieBicepsR;
                    break;
                case 11:
                    tie = (bodyData.getMeasurements().get(i).getSide().equals("L")) ? tieQuadL : tieQuadR;
                    break;
                case 12:
                    tie = (bodyData.getMeasurements().get(i).getSide().equals("L")) ? tieCalfL : tieCalfR;
                    break;
                case 4:
                    tie = tieAbd;
                    break;
                case 3:
                    tie = tieChest;
                    break;
                case 10:
                    tie = tieHips;
                    break;
                case 17:
                    tie = tieWaist;
                    break;
            }
            tie.setText(String.valueOf(bodyData.getMeasurements().get(i).getMeasure()));
        }
    }

    //Devuelve el side de la medida correspondiente a texInputEditTExt.
    private String getSideMeasureByTieType(int tieId){
        switch (tieId){
            case R.id.tieBicepsL: case R.id.tieCalfL: case R.id.tieQuadL:
                return "L";
            case R.id.tieBicepsR: case R.id.tieQuadR: case R.id.tieCalfR:
                return "R";
        }
        return "N";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_body_data_measure, container, false);
    }

    private void navigateBack(){
        Bundle bundle = new Bundle();
        bodyData.setMeasurements(list);
        bundle.putBoolean("modify",isModify);
        bundle.putSerializable("body",bodyData);
        bundle.putBoolean("boxMode",getArguments().getBoolean("boxMode"));
        NavHostFragment.findNavController(BodyMeasureFragment.this).navigate(R.id.bodyDataFragment,bundle);
    }
}