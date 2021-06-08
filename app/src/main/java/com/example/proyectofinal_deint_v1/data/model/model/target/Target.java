package com.example.proyectofinal_deint_v1.data.model.model.target;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Calendar;

public class Target implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String nameTarget;
    @SerializedName("expirationDate")
    private Calendar expirationDate;
    @SerializedName("description")
    private String description;
    @SerializedName("overcome")
    private boolean overcome;
    @SerializedName("idUser")
    private int idUser;

    public Target(String nameTarget, Calendar expirationDate, String description,boolean overcome,int idUser) {
        this.nameTarget = nameTarget;
        this.expirationDate = expirationDate;
        this.description = description;
        this.idUser = idUser;
        this.overcome = overcome;
    }

    public Target() {
    }

    //region Propiedades
    public String getNameTarget() {
        return nameTarget;
    }

    public void setNameTarget(String nameTarget) {
        this.nameTarget = nameTarget;
    }

    public Calendar getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Calendar expirationDate) {
        this.expirationDate = expirationDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public boolean isOvercome() {
        return overcome;
    }

    public int getOvercome() {
       return (this.isOvercome()) ? 1 : 0;
    }

    public void setOvercome(String overcome) {
        this.overcome = overcome.equals("0") ? false : true;
    }
    //endregion

    @Override
    public String toString() {
        return "Target{" +
                ", nameTarget='" + nameTarget + '\'' +
                ", expirationDate=" + expirationDate +
                ", description='" + description + '\'' +
                ", overcome=" + overcome +
                '}';
    }
}
