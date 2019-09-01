package com.zse233.classtable.ui;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.zhuangfei.timetable.TimetableView;
import com.zhuangfei.timetable.listener.ISchedule;
import com.zhuangfei.timetable.listener.IWeekView;
import com.zhuangfei.timetable.model.Schedule;
import com.zhuangfei.timetable.view.WeekView;
import com.zse233.classtable.ClassDatabaseRepo;
import com.zse233.classtable.MyClassTable;
import com.zse233.classtable.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    TimetableView timetableView;
    WeekView weekView;
    ConstraintLayout layout;

    List<MyClassTable> classes;
    int week = -1;
    int week_now = 0;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Date date = new Date();
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
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        timetableView = getView().findViewById(R.id.id_timetableView);
        ClassDatabaseRepo classDatabaseRepo = new ClassDatabaseRepo(getActivity().getApplicationContext());
        classes = classDatabaseRepo.getAllLive();

        if (classes == null) {
            List<MyClassTable> empty = new ArrayList<>();
            timetableView.source(empty)
                    .curWeek(week_now)
                    .showView();
            timetableView.updateDateView();

        } else {
            timetableView.source(classes)
                    .curWeek(1)
                    .showView();
            timetableView.updateDateView();
        }
        timetableView.onDateBuildListener()
                .onHighLight();
        initTimetableView();
    }

    private void initTimetableView() {
        //获取控件
        weekView = getView().findViewById(R.id.id_weekview);
        timetableView = getView().findViewById(R.id.id_timetableView);

        //设置周次选择属性
        weekView.setBackgroundColor(0);
        weekView.curWeek(week_now)
                .callback(new IWeekView.OnWeekItemClickedListener() {
                    @Override
                    public void onWeekClicked(int week) {
                        int cur = timetableView.curWeek();
                        //更新切换后的日期，从当前周cur->切换的周week
                        timetableView.onDateBuildListener()
                                .onUpdateDate(cur, week);
                        timetableView.changeWeekOnly(week);
                        weekView.isShow(false).setBackgroundColor(0);
                    }
                })
                .isShow(false)//设置隐藏，默认显示
                .hideLeftLayout()
                .showView();

        timetableView.curWeek(week_now)
                .hideFlaglayout()
                .maxSlideItem(11)
                .isShowFlaglayout(false)
                .callback(new ISchedule.OnItemClickListener() {
                    @Override
                    public void onItemClick(View v, List<Schedule> scheduleList) {
                        display(scheduleList, v);
                    }
                })
                .callback(new ISchedule.OnItemLongClickListener() {
                    @Override
                    public void onLongClick(View v, int day, int start) {
                        Toast.makeText(getActivity(),
                                "长按:周" + day + ",第" + start + "节",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .isShowNotCurWeek(false)
                .showView();


    }

    protected void display(List<Schedule> beans, View view) {
        String str = "该位置课程: ";
        for (Schedule bean : beans) {
            String name;
            if (bean.getName().length() > 7) {
                name = bean.getName().substring(0, 7);
            } else {
                name = bean.getName();
            }
            str += name + " @ " + bean.getWeekList().toString() + "周\n";
        }
        Snackbar.make(view, str, Snackbar.LENGTH_LONG);
    }
}
