package com.zse233.classtable.ui;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.zse233.classtable.ClassTableRepo;
import com.zse233.classtable.R;
import com.zse233.classtable.ScoreAdaptor;
import com.zse233.classtable.ScoreDatabaseRepo;
import com.zse233.classtable.dialog.ScoreTermDialog;
import com.zse233.classtable.misc.MiscClass;
import com.zse233.classtable.scoredatabase.Score;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScoreFragment extends Fragment {

    private ScoreDatabaseRepo repo;
    private List<Score> scoreList;
    private String userKey;
    private ClassTableRepo classTableRepo;
    private ScoreAdaptor mScoreAdaptor;

    public ScoreFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        SharedPreferences shp = Objects.requireNonNull(getActivity()).getSharedPreferences("first_day", Context.MODE_PRIVATE);
        userKey = shp.getString("Key", "-1");
        classTableRepo = new ClassTableRepo();

        return inflater.inflate(R.layout.fragment_score, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        Toolbar toolbar = Objects.requireNonNull(getActivity()).findViewById(R.id.toolbar);
        toolbar.setVisibility(View.VISIBLE);
        MiscClass.atScheduled(false);
        RecyclerView recyclerView = getActivity().findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        repo = new ScoreDatabaseRepo(getActivity().getApplicationContext());
        scoreList = repo.getAll();
        if (scoreList == null) {
            scoreList = new ArrayList<>();
        }
        mScoreAdaptor = new ScoreAdaptor(scoreList, getLayoutInflater(), getContext());
        recyclerView.setAdapter(mScoreAdaptor);
        FloatingActionButton fab = getActivity().findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {//TODO
                Snackbar.make(view, "开始获取成绩", Snackbar.LENGTH_LONG).show();
                Observable<List<Score>> observable = Observable.create(new ObservableOnSubscribe<List<Score>>() {
                    @Override
                    public void subscribe(ObservableEmitter<List<Score>> emitter) {
                        SharedPreferences shp = Objects.requireNonNull(getActivity()).getSharedPreferences("first_day", Context.MODE_PRIVATE);
                        int termCode = shp.getInt("curr_term", MiscClass.getTermCode());
                        List<Score> scores = classTableRepo.parseScore(classTableRepo.requestScore(userKey, termCode));
                        emitter.onNext(scores);
                        emitter.onComplete();
                    }
                });

                Observer<List<Score>> observer = new Observer<List<Score>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<Score> scores) {

                        repo.clear();
                        for (Score score : scores) {
                            repo.insert(score);
                        }
                        scoreList = scores;
                        Snackbar.make(view, "获取成功，所选学期已出" + scores.size() + "门成绩\n（长按可以选择学期）", Snackbar.LENGTH_LONG).show();
                        mScoreAdaptor.update(scores);

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                };
                observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                        .subscribe(observer);
            }
        });


        fab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ScoreTermDialog dialog = new ScoreTermDialog();
                dialog.show(getFragmentManager(), "term_chooser");
                return true;
            }
        });
    }

}
