package com.example.proyectofinal_deint_v1.data.repository.products;

import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.workData.serie.Serie;

import java.util.ArrayList;
import java.util.List;

//Repositorio provisional, antes de logear/guardar el workData.
public class SerieRepository {
    private static SerieRepository repository;
    private List<Serie> list;

    static {
        repository = new SerieRepository();
    }

    private SerieRepository() {
        list = new ArrayList<>();
    }

    public static SerieRepository getInstance() {
        return repository;
    }

    public List<Serie> getList(){
        return list;
    }

    private int findSerie(Serie serie){
        for (int i = 0; i < list.size(); i++){
            if(list.get(i).equals(serie)){
                return i;
            }
        }
        return -1;
    }

    public void deleteSerie(Serie serie){
        list.remove(serie);
    }

    public void add(Serie serie){
        serie.setNumSerie(this.list.size()+1);
        this.list.add(serie);
    }

    public boolean modify(Serie oldSerie,Serie newSerie) {
        if(findSerie(oldSerie) != -1){
            this.list.set(findSerie(oldSerie),newSerie);
            return  true;
        }
        return false;
    }

}