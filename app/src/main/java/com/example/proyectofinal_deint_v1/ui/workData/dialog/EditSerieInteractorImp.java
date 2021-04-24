package com.example.proyectofinal_deint_v1.ui.workData.dialog;

import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.workData.serie.Serie;
import com.example.proyectofinal_deint_v1.data.repository.products.SerieRepository;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class EditSerieInteractorImp {

    EditSerieInteractor callback;

    public interface EditSerieInteractor {
        void onSuccess(List repository);
        void onEmptyRepositoryError();

        void onWeightEmptyError();
        void onRepsEmptyError();
        void onSuccessDelete();
        void onSuccesAdd();
        void onSuccesModify();
    }

    public EditSerieInteractorImp(EditSerieInteractor callback) {
        this.callback = callback;
    }

    public void deleteSerie(Serie serie){
        SerieRepository.getInstance().deleteSerie(serie);
        callback.onSuccessDelete();
    }

    public void addSerie(Serie serie){
        if(String.valueOf(serie.getWeight()).isEmpty()){
            callback.onWeightEmptyError();
            return;
        }
        if(String.valueOf(serie.getReps()).isEmpty()){
            callback.onRepsEmptyError();
            return;
        }
        SerieRepository.getInstance().add(serie);
        callback.onSuccesAdd();
        return;
    }

    public void modifySerie(Serie oldSerie,Serie newSerie){
        if(String.valueOf(oldSerie.getWeight()).isEmpty()){
            callback.onWeightEmptyError();
            return;
        }
        if(String.valueOf(oldSerie.getReps()).isEmpty()){
            callback.onRepsEmptyError();
            return;
        }
        if(SerieRepository.getInstance().modify(oldSerie,newSerie)){
            callback.onSuccesAdd();
            return;
        }
        return;
    }

    public void getRepository(){
        List<Serie> list = SerieRepository.getInstance().getList();

        Collections.sort(list, new Comparator<Serie>(){
            @Override
            public int compare(Serie o1, Serie o2) {
                return (o1.getNumSerie() > o2.getNumSerie()) ? 1 : -1;
            }
        });
        if(list.size() > 0){
            callback.onSuccess(list);
            return;
        }
        callback.onEmptyRepositoryError();
        return;
    }
}
