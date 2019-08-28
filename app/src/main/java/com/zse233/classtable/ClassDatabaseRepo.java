package com.zse233.classtable;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.zse233.classtable.database.ClassTableDao;
import com.zse233.classtable.database.ClassTableDatabase;

import java.util.List;

public class ClassDatabaseRepo {
    private LiveData<List<MyClassTable>> myClassTables;
    private ClassTableDao Dao;

    public ClassDatabaseRepo(Context context) {
        ClassTableDatabase classTableDatabase = ClassTableDatabase.getClassTableDatabase(context.getApplicationContext());
        Dao = classTableDatabase.getClassTableDao();
        myClassTables = Dao.getAllClassTable();
    }

    class InsertClass extends AsyncTask<MyClassTable, Void, Void> {

        @Override
        protected Void doInBackground(MyClassTable... myClassTables) {
            Dao.insertClassTable(myClassTables);
            return null;
        }
    }

    class UpdateClass extends AsyncTask<MyClassTable, Void, Void> {

        @Override
        protected Void doInBackground(MyClassTable... myClassTables) {
            Dao.updateClassTable(myClassTables);
            return null;
        }
    }

    class DeleteClass extends AsyncTask<MyClassTable, Void, Void> {

        @Override
        protected Void doInBackground(MyClassTable... myClassTables) {
            Dao.deleteClassTable(myClassTables);
            return null;
        }
    }

    class ClearAll extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            Dao.clearAll();
            return null;
        }
    }

    public void insert(MyClassTable... myClassTables) {
        new InsertClass().execute(myClassTables);
    }

    public void update(MyClassTable... myClassTables) {
        new UpdateClass().execute(myClassTables);
    }

    public void delete(MyClassTable... myClassTables) {
        new DeleteClass().execute(myClassTables);
    }

    public void clear() {
        new ClearAll().execute();
    }

    public LiveData<List<MyClassTable>> getAllLive() {
        return myClassTables;
    }
}
