package com.zse233.classtable.scoredatabase;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Score.class},version = 1,exportSchema = false)
public abstract class ScoreDatabase extends RoomDatabase {
    private static ScoreDatabase INSTANCE;
    public abstract ScoreDao getScoreDao();

    public synchronized static ScoreDatabase getScoreDatabase(Context context){
        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context,ScoreDatabase.class,"score_database")
                    .allowMainThreadQueries()
                    .build();
        }
        return INSTANCE;
    }
}
