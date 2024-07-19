package com.example.roomdb2;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Module.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ModuleDAO moduleDAO();
}
