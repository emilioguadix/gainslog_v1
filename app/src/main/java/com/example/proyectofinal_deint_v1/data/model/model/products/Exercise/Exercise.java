package com.example.proyectofinal_deint_v1.data.model.model.products.Exercise;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Exercise implements Serializable {

    @SerializedName("id")
    private int id;//id
    @SerializedName("typeExercise")
    private int typeExercise;
    private List<Muscle> mainMuscles;
    @SerializedName("name")
    private String name;//id
    @SerializedName("description")
    private String description;
    @SerializedName("material")
    private String material;
    @SerializedName("notes")
    private String notes;
    @SerializedName("favorite")
    private boolean favorite;
    @SerializedName("userCreated")
    private boolean userCreated;
    @SerializedName("urlVideo")
    private String urlVideo;

    //Constructor

    public Exercise() {
        mainMuscles = new ArrayList<>();
    }


    //region Propiedades
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTypeExercise() {
        return typeExercise;
    }

    public void setTypeExercise(int typeExercise) {
        this.typeExercise = typeExercise;
    }

    public List<Muscle> getMainMuscles() {
        return mainMuscles;
    }

    public void setMainMuscles(List<Muscle> mainMuscles) {
        this.mainMuscles = mainMuscles;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public boolean isUserCreated() {
        return userCreated;
    }

    public void setUserCreated(boolean userCreated) {
        this.userCreated = userCreated;
    }

    public String getUrlVideo() {
        return urlVideo;
    }

    public void setUrlVideo(String urlVideo) {
        this.urlVideo = urlVideo;
    }

    //endregion


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Exercise exercise = (Exercise) o;
        return Objects.equals(name, exercise.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Exercise{" +
                "id=" + id +
                ", typeExercise=" + typeExercise +
                ", mainMuscles=" + mainMuscles +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", material='" + material + '\'' +
                ", notes='" + notes + '\'' +
                ", favorite=" + favorite +
                ", userCreated=" + userCreated +
                ", urlVideo='" + urlVideo + '\'' +
                '}';
    }
}
