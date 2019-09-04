package com.zse233.classtable;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zse233.classtable.scoredatabase.Score;

import java.util.List;

public class ScoreAdaptor extends RecyclerView.Adapter<ScoreAdaptor.ScoreViewHolder> {
    private List<Score> scores;
    private LayoutInflater inflater;
    private Context context;

    public ScoreAdaptor(List<Score> scores, LayoutInflater inflater, Context context) {
        this.scores = scores;
        this.inflater = inflater;
        this.context = context;
    }

    @NonNull
    @Override
    public ScoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ScoreViewHolder holder = new ScoreViewHolder(inflater.inflate(R.layout.recycler_item,parent,false));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ScoreViewHolder holder, int position) {
        holder.score.setText(scores.get(position).getScore());
        holder.courseName.setText(scores.get(position).getCourseName());
        holder.detail.setText(scores.get(position).getDetail());
    }

    @Override
    public int getItemCount() {
        return scores == null ? 0 : scores.size();
    }

    class ScoreViewHolder extends RecyclerView.ViewHolder{
        TextView courseName,score,detail;
        public ScoreViewHolder(@NonNull View itemView) {
            super(itemView);
            courseName = itemView.findViewById(R.id.subject);
            score = itemView.findViewById(R.id.score_all);
            detail = itemView.findViewById(R.id.score_detail);
        }
    }
}
