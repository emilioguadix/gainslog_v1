package com.example.proyectofinal_deint_v1.data.model.model.products.Exercise;

import java.util.Comparator;

public class ComparatorExercise implements Comparator<Exercise> {
    @Override
    public int compare(Exercise o1, Exercise o2) {
        return o1.getName().compareTo(o2.getName());
    }
}
