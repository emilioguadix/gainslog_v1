package com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.workData;

import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.workData.serie.Serie;

import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class WorkDat {
    private int id;
    private int idExercise;
    private Calendar logDate;
    private List<Serie> serieList;

    public WorkDat() {
    }

    //region Propiedades

    public List<Serie> getSerieList() {
        return serieList;
    }

    public void setSerieList(List<Serie> serieList) {
        this.serieList = serieList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdExercise() {
        return idExercise;
    }

    public void setIdExercise(int idExercise) {
        this.idExercise = idExercise;
    }

    public Calendar getLogDate() {
        return logDate;
    }

    public void setLogDate(Calendar logDate) {
        this.logDate = logDate;
    }
    //endregion

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkDat workDat = (WorkDat) o;
        return id == workDat.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "WorkDat{" +
                "id=" + id +
                ", idExercise=" + idExercise +
                ", logDate=" + logDate +
                '}';
    }
}
