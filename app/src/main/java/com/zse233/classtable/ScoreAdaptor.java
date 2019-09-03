package com.zse233.classtable;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ScoreAdaptor extends RecyclerView.Adapter<ScoreAdaptor.ScoreViewHolder> {


    @NonNull
    @Override
    public ScoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ScoreViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class ScoreViewHolder extends RecyclerView.ViewHolder{

        public ScoreViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
