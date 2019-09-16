package com.zse233.classtable.scoredatabase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ScoreDao {
    @Insert
    public void insert(Score...scores);

    @Update
    public void update(Score...scores);

    @Delete
    public void delete(Score...scores);

    @Query("DELETE FROM SCORE")
    public void clear();

    @Query("SELECT * FROM SCORE ORDER BY ID DESC")
    List<Score> getAll();
}
