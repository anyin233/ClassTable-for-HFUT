package com.zse233.classtable.ui;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zse233.classtable.R;
import com.zse233.classtable.ScoreAdaptor;
import com.zse233.classtable.ScoreDatabaseRepo;
import com.zse233.classtable.scoredatabase.Score;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScoreFragment extends Fragment {

    private RecyclerView recyclerView;
    private ScoreDatabaseRepo repo;
    private List<Score> scoreList;
    public ScoreFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        repo = new ScoreDatabaseRepo(getContext());
        recyclerView = getActivity().findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new ScoreAdaptor(scoreList, getLayoutInflater(), getContext()));
        return inflater.inflate(R.layout.fragment_score, container, false);
    }
}
