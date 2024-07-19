package com.example.roomdb;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Person.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    public abstract PersonDAO personDAO();
}
