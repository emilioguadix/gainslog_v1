package com.example.proyectofinal_deint_v1.data.model.model.user;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Objects;

public class Request implements Serializable {
    //Campos
    @SerializedName("idUser")
    private int idUser;
    @SerializedName("idCoach")
    private int idCoach;
    @SerializedName("accepted")
    private int _accepted;
    @SerializedName("clientName")
    private String clientName;
    @SerializedName("clientEmail")
    private String clientEmail;
    @SerializedName("fb_id")
    private String fb_id_user;

    public Request() {
    }

    public int getIdCoach() {
        return idCoach;
    }

    public void setIdCoach(int idCoach) {
        this.idCoach = idCoach;
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public String getFb_id_user() {
        return fb_id_user;
    }

    public void setFb_id_user(String fb_id_user) {
        this.fb_id_user = fb_id_user;
    }

    public void setClientEmail(String clientEmail) {
        this.clientEmail = clientEmail;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public int get_accepted() {
        return _accepted;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public void set_accepted(int _accepted) {
        this._accepted = _accepted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Request request = (Request) o;
        return idUser == request.idUser &&
                idCoach == request.idCoach;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idUser, idCoach);
    }
}
