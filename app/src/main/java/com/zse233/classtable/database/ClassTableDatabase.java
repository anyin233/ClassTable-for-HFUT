package com.zse233.classtable.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {ClassTable.class}, version = 1, exportSchema = false)
public abstract class ClassTableDatabase extends RoomDatabase {
    public abstract ClassTableDao getClassTableDao();

    private static ClassTableDatabase classTableDatabase;

    public synchronized static ClassTableDatabase getClassTableDatabase(Context context) {
        if (classTableDatabase == null) {
            classTableDatabase = Room.databaseBuilder(context.getApplicationContext(), ClassTableDatabase.class, "ClassTable")
                    .build();
        }
        return classTableDatabase;
    }
}
