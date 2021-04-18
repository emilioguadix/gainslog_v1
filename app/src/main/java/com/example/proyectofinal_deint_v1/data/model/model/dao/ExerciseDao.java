package com.example.proyectofinal_deint_v1.data.model.model.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.Exercise;

import java.util.List;

@Dao
public interface ExerciseDao {

    //Ponemos long, ya que si hay un error devolvera -1
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(Exercise exercise);

    @Delete
    void delete(Exercise exercise);

    @Query("UPDATE exercise SET name=:name, typeExercise=:typeExercise, mainMuscles=:mainMuscles, material=:material, description=:description, notes=:notes, favorite=:favorite,userCreated=:userCreated WHERE name=:oldname ")
    void update(String oldname,String name,int typeExercise,String mainMuscles,String description,String material,String notes,boolean favorite,boolean userCreated);

    @Query("SELECT * FROM exercise")
    List<Exercise> get();

    @Query("SELECT * FROM exercise ORDER BY name ASC")
    List<Exercise> getSortByName();

    @Query("SELECT * FROM exercise WHERE name=:name ")
    Exercise findByName(String name);

    @Query("SELECT COUNT(*) FROM exercise")
    int getRowCount();
}
