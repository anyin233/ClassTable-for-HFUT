package com.zse233.classtable.ui;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.zse233.classtable.ClassTableRepo;
import com.zse233.classtable.R;
import com.zse233.classtable.ScoreAdaptor;
import com.zse233.classtable.ScoreDatabaseRepo;
import com.zse233.classtable.database.ClassTable;
import com.zse233.classtable.scoredatabase.Score;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScoreFragment extends Fragment {

    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private ScoreDatabaseRepo repo;
    private List<Score> scoreList;
    private String userKey;
    ClassTableRepo classTableRepo;
    public ScoreFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        SharedPreferences shp = getActivity().getSharedPreferences("first_day", Context.MODE_PRIVATE);
        userKey = shp.getString("Key","-1");
        classTableRepo = new ClassTableRepo();

        return inflater.inflate(R.layout.fragment_score, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        recyclerView = getActivity().findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        repo = new ScoreDatabaseRepo(getActivity().getApplicationContext());
        scoreList = repo.getAll();
        if(scoreList == null){
            scoreList = new ArrayList<>();
        }
        final ScoreAdaptor mScoreAdaptor = new ScoreAdaptor(scoreList, getLayoutInflater(), getContext());
        recyclerView.setAdapter(mScoreAdaptor);
        fab = getActivity().findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view,"开始获取成绩",Snackbar.LENGTH_SHORT).show();
                List<Score> scores = classTableRepo.parseScore(classTableRepo.requestScore(userKey,34));
                if(scores.size() != 0){
                    repo.clear();
                    for(Score score:scores){
                        repo.insert(score);
                    }
                    scoreList = scores;
                    Snackbar.make(view,"获取成功",Snackbar.LENGTH_SHORT).show();
                    mScoreAdaptor.update(scores);
                }else{
                    Snackbar.make(view,"获取失败",Snackbar.LENGTH_SHORT).show();
                }
            }
        });
        fab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return false;
            }
        });
    }
}
