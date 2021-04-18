package com.example.proyectofinal_deint_v1.data.model.model.products.Exercise;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class Muscle {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;

    public Muscle(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Muscle muscle = (Muscle) o;
        return id == muscle.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return getName();
    }
}
