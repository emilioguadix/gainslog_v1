package com.example.proyectofinal_deint_v1.data.model.model.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.Exercise;
import com.example.proyectofinal_deint_v1.data.model.model.target.Target;

import java.sql.Date;
import java.util.List;

@Dao
public interface TargetDao {
    //Ponemos long, ya que si hay un error devolvera -1
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(Target target);

    @Delete
    void delete(Target target);

    @Query("UPDATE target SET nameTarget=:nameTarget, exercise=:exercise, expirationDate=:expirationDate, description=:description,overcome=:overcome WHERE nameTarget=:oldNameTarget AND logDate=:logDate")
    void update(Long logDate, String oldNameTarget, String nameTarget, String exercise, Long expirationDate, String description, boolean overcome);

    @Query("SELECT * FROM target")
    List<Target> get();

    @Query("SELECT * FROM target ORDER BY logDate ASC")
    List<Target> getShortByDateLog();

    @Query("SELECT * FROM target WHERE logDate=:logDate AND nameTarget=:nameTarget ")
    Target findById(Long logDate,String nameTarget);

    @Query("SELECT COUNT(*) FROM target")
    int getRowCount();
}
