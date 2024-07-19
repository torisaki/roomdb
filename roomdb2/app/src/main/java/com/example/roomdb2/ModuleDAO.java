package com.example.roomdb2;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ModuleDAO {
    @Query("SELECT * FROM module")
    List<Module> getAll();
    @Query("SELECT * FROM module WHERE uid IN (:moduleId)")
    Module loadModuleById(int moduleId);
    @Query("SELECT * FROM module WHERE name LIKE :name")
    Module findByName(String name);
    @Insert
    void insert(Module module);
    @Update
    void update(Module module);
    @Delete
    void delete(Module module);
}
