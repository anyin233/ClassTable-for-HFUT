package com.zse233.classtable;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ClassViewMode extends AndroidViewModel {
    private ClassDatabaseRepo classDatabaseRepo;

    public ClassViewMode(@NonNull Application application) {
        super(application);
        classDatabaseRepo = new ClassDatabaseRepo(application);
    }

    public LiveData<List<MyClassTable>> getAllLive() {
        return classDatabaseRepo.getAllLive();
    }

    public void insert(MyClassTable... myClassTables) {
        classDatabaseRepo.insert(myClassTables);
    }

    public void update(MyClassTable... myClassTables) {
        classDatabaseRepo.update(myClassTables);
    }

    public void delete(MyClassTable... myClassTables) {
        classDatabaseRepo.delete(myClassTables);
    }

    public void clear() {
        classDatabaseRepo.clear();
    }

    public int getCount() {
        return classDatabaseRepo == null ? 0 : getAllLive().getValue().size();
    }
}
