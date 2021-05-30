package com.example.proyectofinal_deint_v1.data.repository.products;

import android.icu.util.Measure;

import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.workData.serie.Serie;

import java.util.ArrayList;
import java.util.List;

//Repositorio provisional, antes de logear/guardar el workData.
public class MeasureRepository {
    private static MeasureRepository repository;
    private List<Measure> list;

    static {
        repository = new MeasureRepository();
    }

    private MeasureRepository() {
        list = new ArrayList<>();
    }

    public static MeasureRepository getInstance() {
        return repository;
    }

    public List<Measure> getList(){
        return list;
    }

    private int findMeasure(Measure measure){
        for(int i = 0; i < getList().size(); i++){
            if(measure.equals(getList().get(i))){
                return i;
            }
        }
        return -1;
    }
    
    public void deleteMeasure(Measure measure){
        list.remove(measure);
    }

    public void addMeasure(Measure measure){
        this.list.add(measure);
    }

    public boolean modifyMeasure(Measure oldMeasure,Measure newMeasure) {
        if(findMeasure(oldMeasure) != -1){
            this.list.set(findMeasure(oldMeasure),newMeasure);
            return  true;
        }
        return false;
    }

}