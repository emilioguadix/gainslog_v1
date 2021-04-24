package com.example.proyectofinal_deint_v1.data.model.model.products.Exercise;

public class TypeExercise {
    public static final int CARDIO = 0;
    public static final int FUERZA = 1;
    public static final int FLEXIBILIDAD = 2;

    public static boolean isTypeExercise(int type){
        return (type >= TypeExercise.CARDIO && type <= TypeExercise.FLEXIBILIDAD) ? true : false;
    }
}
