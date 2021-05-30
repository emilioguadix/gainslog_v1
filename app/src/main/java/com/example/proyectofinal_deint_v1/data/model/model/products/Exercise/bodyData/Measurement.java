package com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.bodyData;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Objects;

public class Measurement implements Serializable {
    @SerializedName("bodyId")
    private int bodyData;
    @SerializedName("idMuscle")
    private int idMuscle;
    @SerializedName("measure")
    private double measure;
    @SerializedName("side")
    private String side;


    public Measurement() {
    }

    //region Propiedades
    public int getBodyData() {
        return bodyData;
    }

    public void setBodyData(int bodyData) {
        this.bodyData = bodyData;
    }

    public int getIdMuscle() {
        return idMuscle;
    }

    public void setIdMuscle(int idMuscle) {
        this.idMuscle = idMuscle;
    }

    public double getMeasure() {
        return measure;
    }

    public void setMeasure(double measure) {
        this.measure = measure;
    }
    //endregion


    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    @Override
    public String toString() {
        return "Measurement{" +
                "bodyData=" + bodyData +
                ", idMuscle=" + idMuscle +
                ", measure=" + measure +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Measurement that = (Measurement) o;
        return bodyData == that.bodyData &&
                idMuscle == that.idMuscle &&
                Objects.equals(side, that.side);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bodyData, idMuscle, side);
    }
}
