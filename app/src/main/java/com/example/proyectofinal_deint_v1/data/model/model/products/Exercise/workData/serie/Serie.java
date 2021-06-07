package com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.workData.serie;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Objects;

public class Serie implements Serializable {
    @SerializedName("tipoSerie")
    private int typeSerie;
    @SerializedName("id")
    private int numSerie;
    @SerializedName("peso")
    private int weight;
    @SerializedName("intensidad")
    private String typeIntensity;
    @SerializedName("int_value")
    private int intensity;
    @SerializedName("repes")
    private int reps;
    @SerializedName("descanso")
    private long timeRest;
    @SerializedName("tiempo_ej")
    private long time;
    @SerializedName("nota")
    private String note;
    @SerializedName("marked")
    private int marked;
    private int copiesSerie;

    public Serie() {
        this.typeSerie = 0;
        this.typeIntensity = "RIR";
        this.weight = 0;
        this.reps = 0;
        this.intensity = 0;
        this.time = 0;
        this.timeRest = 0;
        this.note = "";
        this.copiesSerie = 0;
    }

    //region Propiedades

    public int getCopiesSerie() {
        return copiesSerie;
    }

    public void setCopiesSerie(int copiesSerie) {
        this.copiesSerie = copiesSerie;
    }

    public int getMarked() {
        return marked;
    }

    public void setMarked(int marked) {
        this.marked = marked;
    }

    public int getTypeSerie() {
        return typeSerie;
    }

    public void setTypeSerie(int typeSerie) {
        this.typeSerie = typeSerie;
    }

    public int getNumSerie() {
        return numSerie;
    }

    public void setNumSerie(int numSerie) {
        this.numSerie = numSerie;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getTypeIntensity() {
        return typeIntensity;
    }

    public void setTypeIntensity(String typeIntensity) {
        this.typeIntensity = typeIntensity;
    }

    public int getIntensity() {
        return intensity;
    }

    public void setIntensity(int intensity) {
        this.intensity = intensity;
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public long getTimeRest() {
        return timeRest;
    }

    public void setTimeRest(long timeRest) {
        this.timeRest = timeRest;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    //endregion

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Serie serie = (Serie) o;
        return numSerie == serie.numSerie;
    }

    @Override
    public int hashCode() {
        return Objects.hash(numSerie);
    }

    @Override
    public String toString() {
        return "SET " + String.valueOf(this.getNumSerie()) + " -> " +
                String.valueOf(this.getWeight()) + "Kg x " +
                String.valueOf(this.getReps()) + "reps x " +
                this.getTypeIntensity() + this.getIntensity() + " (" +
                String.valueOf(this.getTimeRest()) + "s) ";
    }
}
