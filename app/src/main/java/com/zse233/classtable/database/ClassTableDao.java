package com.zse233.classtable.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.zse233.classtable.MyClassTable;

import java.util.List;

@Dao
public interface ClassTableDao {
    @Insert
    void insertClassTable(MyClassTable... myClassTables);

    @Update
    void updateClassTable(MyClassTable... myClassTables);

    @Delete
    void deleteClassTable(MyClassTable... myClassTables);

    @Query("DELETE FROM MyClassTable")
    void clearAll();

    @Query("SELECT * FROM MyClassTable")
    List<MyClassTable> getAllClassTable();
}
