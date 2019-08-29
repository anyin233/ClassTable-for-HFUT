package com.zse233.classtable.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.zse233.classtable.MyClassTable;

@Entity
public class ClassTable {
    @PrimaryKey
    private int Id;
    @ColumnInfo(name = "CLASS")
    private MyClassTable myClassTable;

    public ClassTable(MyClassTable myClassTable) {
        this.myClassTable = myClassTable;
        this.Id = myClassTable.getA_Id();
    }

    public void setId(int id) {
        Id = id;
    }

    public int getId() {
        return Id;
    }

    public MyClassTable getMyClassTable() {
        return myClassTable;
    }

    public void setMyClassTable(MyClassTable myClassTable) {
        this.myClassTable = myClassTable;
    }
}

