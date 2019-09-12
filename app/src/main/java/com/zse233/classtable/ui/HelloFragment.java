package com.zse233.classtable.ui;


import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhuangfei.timetable.model.Schedule;
import com.zhuangfei.timetable.model.ScheduleSupport;
import com.zse233.classtable.ClassDatabaseRepo;
import com.zse233.classtable.HelloAdaptor;
import com.zse233.classtable.MyClassTable;
import com.zse233.classtable.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class HelloFragment extends Fragment {
    RecyclerView recyclerView;
    ClassDatabaseRepo repo;
    int week_now = 0;

    public HelloFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_hello, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        int weekDay = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if(weekDay == 0){
            weekDay = 7;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String format_date = dateFormat.format(date);
        SharedPreferences shp = getActivity().getSharedPreferences("first_day", MODE_PRIVATE);
        String startDay = shp.getString("start", format_date);
        Date start;
        try {
            start = dateFormat.parse(startDay);
        } catch (ParseException e) {
            start = new Date();
            Log.d("TError", "" + e.getMessage());
        }
        week_now = (int) ((date.getTime() - start.getTime()) / (1000 * 60 * 60 * 24));
        week_now /= 7;
        ++week_now;
        if (week_now < 1) {
            week_now = 1;
        }
        repo = new ClassDatabaseRepo(getContext());
        List<MyClassTable> myClassTables = repo.getAllLive();
        List<Schedule> schedules = new ArrayList<>();
        for(MyClassTable myClassTable:myClassTables){
            schedules.add(myClassTable.getSchedule());
        }
        List<Schedule> curCourse = ScheduleSupport.getHaveSubjectsWithDay(schedules,week_now,weekDay-1);
        if(curCourse == null){
            curCourse = new ArrayList<>();
        }
        recyclerView = getActivity().findViewById(R.id.hello_recycler);
        recyclerView.setAdapter(new HelloAdaptor(curCourse,getLayoutInflater()));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}
