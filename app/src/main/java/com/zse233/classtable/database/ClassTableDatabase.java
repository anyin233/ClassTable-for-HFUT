package com.zse233.classtable.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.zse233.classtable.MyClassTable;

@Database(entities = {MyClassTable.class}, version = 2, exportSchema = false)
public abstract class ClassTableDatabase extends RoomDatabase {
    public abstract ClassTableDao getClassTableDao();
    private static ClassTableDatabase classTableDatabase;
    public synchronized static ClassTableDatabase getClassTableDatabase(Context context) {
        if (classTableDatabase == null) {
            classTableDatabase = Room.databaseBuilder(context.getApplicationContext(), ClassTableDatabase.class, "ClassTable")
                    .allowMainThreadQueries()
                    .addMigrations(MIGRATION1_2)
                    .build();
        }
        return classTableDatabase;
    }

    static final Migration MIGRATION1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE MyClassTable "
                    + " ADD COLUMN key_id INTEGER");
        }
    };
}
