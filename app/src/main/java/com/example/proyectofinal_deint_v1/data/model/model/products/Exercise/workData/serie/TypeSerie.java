package com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.workData.serie;

import android.content.Context;

import com.example.proyectofinal_deint_v1.R;

import java.util.ArrayList;
import java.util.List;

public class TypeSerie {
    public static final int CALENTAMIENTO = 0;
    public static final int APROXIMACION = 1;
    public static final int EFECTIVA = 2;
    public static final int FALLO = 3;

    public static String getTypeString(Context context, int type){
        switch (type){
            case 0:
                return context.getResources().getString(R.string.typeSerie0);
            case 1:
                return context.getResources().getString(R.string.typeSerie1);
            case 2:
                return context.getResources().getString(R.string.typeSerie2);
            case 3:
                return context.getResources().getString(R.string.typeSerie3);
        }
        return "";
    }

    public static List<String> getList(Context context){
        List<String> tmp = new ArrayList<>();
        for (int i = 0; i < 4; i++){
            tmp.add(getTypeString(context,i));
        }
        return tmp;
    }
}
