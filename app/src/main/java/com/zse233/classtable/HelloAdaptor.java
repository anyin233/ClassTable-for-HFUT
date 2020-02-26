package com.zse233.classtable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zhuangfei.timetable.model.Schedule;

import java.util.List;


public class HelloAdaptor extends RecyclerView.Adapter<HelloAdaptor.HelloViewHolder> {
    List<Schedule> myClassTables;
    LayoutInflater inflater;

    public HelloAdaptor(List<Schedule> myClassTables,LayoutInflater inflater){
        this.myClassTables = myClassTables;
        this.inflater = inflater;
    }

    @NonNull
    @Override
    public HelloViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        HelloViewHolder viewHolder = new HelloViewHolder(inflater.inflate(R.layout.course_item,parent,false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HelloViewHolder holder, int position) {
        Schedule myClassTable = myClassTables.get(position);
        holder.teacher.setText(myClassTable.getTeacher());
        holder.coursePlace.setText(myClassTable.getRoom());
        holder.courseTime.setText("第"+(myClassTable.getStart()+1)/2+"节：");
        holder.courseName.setText(myClassTable.getName());
    }

    @Override
    public int getItemCount() {
        return myClassTables == null ? 0 : myClassTables.size();
    }

    class HelloViewHolder extends RecyclerView.ViewHolder {
        TextView courseName,coursePlace,courseTime,teacher;
        public HelloViewHolder(@NonNull View itemView) {
            super(itemView);
            courseName = itemView.findViewById(R.id.hello_course_name);
            courseTime = itemView.findViewById(R.id.hello_class_num);
            coursePlace = itemView.findViewById(R.id.hello_course_place);
            teacher = itemView.findViewById(R.id.hello_teacher);
        }
    }
}
