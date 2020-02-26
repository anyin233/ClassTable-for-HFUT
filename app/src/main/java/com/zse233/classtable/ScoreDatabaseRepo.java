package com.zse233.classtable;

import android.content.Context;
import android.os.AsyncTask;

import com.zse233.classtable.scoredatabase.Score;
import com.zse233.classtable.scoredatabase.ScoreDao;
import com.zse233.classtable.scoredatabase.ScoreDatabase;

import java.util.List;

public class ScoreDatabaseRepo {
    private List<Score> scores;
    private ScoreDao dao;

    public ScoreDatabaseRepo(Context context){
        ScoreDatabase database = ScoreDatabase.getScoreDatabase(context);
        dao = database.getScoreDao();
        scores = getAll();
    }

    class Insert extends AsyncTask<Score,Void,Void>{

        @Override
        protected Void doInBackground(Score... scores) {
            dao.insert(scores);
            return null;
        }
    }

    class Update extends AsyncTask<Score,Void,Void>{

        @Override
        protected Void doInBackground(Score... scores) {
            dao.update(scores);
            return null;
        }
    }

    class Delete extends AsyncTask<Score,Void,Void>{

        @Override
        protected Void doInBackground(Score... scores) {
            dao.delete(scores);
            return null;
        }
    }

    class Clear extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            dao.clear();
            return null;
        }
    }

    class GetAll extends AsyncTask<Void,Void,List<Score>>{

        @Override
        protected List<Score> doInBackground(Void... voids) {
            return dao.getAll();
        }
    }

    public void insert(Score...scores){
        new Insert().execute(scores);
    }

    public void update(Score...scores){
        new Update().execute(scores);
    }

    public void delete(Score...scores){
        new Delete().execute(scores);
    }

    public void clear(){
        new Clear().execute();
    }

    public List<Score> getAll(){
        return dao.getAll();
    }

}
