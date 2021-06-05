package com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.bodyData;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class BodyData implements Serializable {

    @SerializedName("id")
    private int id;
    @SerializedName("weight")
    private double weight;
    @SerializedName("fatPer")
    private double fatPer;
    @SerializedName("note")
    private String note;

    private Calendar logDate;

    private List<Measurement> measurements;

    public BodyData() {
    }

    //region Propiedades

    public Calendar getLogDate() {
        return logDate;
    }

    public void setLogDate(Calendar logDate) {
        this.logDate = logDate;
    }

    public List<Measurement> getMeasurements() {
        return measurements;
    }

    public void setMeasurements(List<Measurement> measurements) {
        this.measurements = measurements;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getFatPer() {
        return fatPer;
    }

    public void setFatPer(double fatPer) {
        this.fatPer = fatPer;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
    //endregion

    @Override
    public String toString() {
        return "BodyData{" +
                "id=" + id +
                ", weight=" + weight +
                ", fatPer=" + fatPer +
                ", name='" + note + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BodyData bodyData = (BodyData) o;
        return id == bodyData.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
