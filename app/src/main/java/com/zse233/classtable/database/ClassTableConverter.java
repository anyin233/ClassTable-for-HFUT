package com.zse233.classtable.database;

import androidx.room.TypeConverter;

import com.alibaba.fastjson.JSON;
import com.zse233.classtable.MyClassTable;

public class ClassTableConverter {
    @TypeConverter
    public static MyClassTable revert(String myClassTableStr) {
        return JSON.parseObject(myClassTableStr, MyClassTable.class);
    }

    @TypeConverter
    public static String converter(MyClassTable myClassTable) {
        return JSON.toJSONString(myClassTable);
    }

}
