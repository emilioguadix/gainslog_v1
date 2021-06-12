package com.example.proyectofinal_deint_v1.data.model.model.user;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class User {
    //Campos
    @SerializedName("fb_id")
    private String uId;
    @SerializedName("user_name")
    private String _name;
    @SerializedName("email")
    private String _email;
    private String _imgUser;
    @SerializedName("coach")
    private int coach;
    private int _typeUser;

    //Constructor
    public User(String uId, String _name, String _email, String _imgUser, int _typeUser) {
        this.uId = uId;
        this._name = _name;
        this._email = _email;
        this._imgUser = _imgUser;
        this._typeUser = _typeUser;
    }

    //region Propiedades
    public String getuId() {
        return uId;
    }
    public void setuId(String uId) {
        this.uId = uId;
    }
    public String get_name() {
        return _name;
    }
    public void set_name(String _name) {
        this._name = _name;
    }
    public String get_email() {
        return _email;
    }
    public void set_email(String _email) {
        this._email = _email;
    }
    public String get_imgUser() {
        return _imgUser;
    }
    public void set_imgUser(String _imgUser) {
        this._imgUser = _imgUser;
    }
    public int get_typeUser() {
        return _typeUser;
    }
    public void set_typeUser(int _typeUser) {
        this._typeUser = _typeUser;
    }

    public int getCoach() {
        return coach;
    }

    public void setCoach(int coach) {
        this.coach = coach;
    }
    //endregion

    //Métodos
    @Override
    public String toString() {
        return "User{" +
                "id=" + uId +
                ", _name='" + _name + '\'' +
                ", _email='" + _email + '\'' +
                ", _imgUser='" + _imgUser + '\'' +
                ", _typeUser=" + _typeUser +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return uId == user.uId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(uId);
    }
}
