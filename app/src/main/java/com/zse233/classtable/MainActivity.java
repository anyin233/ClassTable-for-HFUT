package com.zse233.classtable;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.zhuangfei.timetable.TimetableView;
import com.zhuangfei.timetable.listener.ISchedule;
import com.zhuangfei.timetable.listener.IWeekView;
import com.zhuangfei.timetable.model.Schedule;
import com.zhuangfei.timetable.view.WeekView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    TimetableView timetableView;
    WeekView weekView;
    ConstraintLayout layout;

    List<MyClassTable> classes;
    int week = -1;
    int week_now = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        timetableView = findViewById(R.id.id_timetableView);
        //viewMode = ViewModelProviders.of(this).get(ClassViewMode.class);
        //ClassDatabaseRepo classDatabaseRepo =new ClassDatabaseRepo(getApplicationContext());
        /*classes = classDatabaseRepo.getAllLive();
        if(classes == null){
            List<MyClassTable> empty = new ArrayList<>();
            timetableView.source(empty)
                    .curWeek(1)
                    .showView();
        }else{
            timetableView.source(classes)
                    .curWeek(1)
                    .showView();
        }*/
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd");
        String format_date = dateFormat.format(date);
        SharedPreferences shp = getSharedPreferences("first_day", MODE_PRIVATE);
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
        if (week_now < 1) {
            week_now = 1;
        }
        initTimetableView();

    }

    @Override
    protected void onStart() {
        super.onStart();
        ClassDatabaseRepo classDatabaseRepo = new ClassDatabaseRepo(getApplicationContext());
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
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.changeWeek: {
                weekView.isShow(true)
                        .setBackgroundColor(0xFFFFFF);
                return true;
            }
            case R.id.login: {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
            case R.id.end: {
                finish();
            }
        }
        return true;
    }

    private void initTimetableView() {
        //获取控件
        weekView = findViewById(R.id.id_weekview);
        timetableView = findViewById(R.id.id_timetableView);

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
                .callback(new ISchedule.OnItemClickListener() {
                    @Override
                    public void onItemClick(View v, List<Schedule> scheduleList) {
                        display(scheduleList);
                    }
                })
                .callback(new ISchedule.OnItemLongClickListener() {
                    @Override
                    public void onLongClick(View v, int day, int start) {
                        Toast.makeText(MainActivity.this,
                                "长按:周" + day + ",第" + start + "节",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .isShowNotCurWeek(false)
                .showView();


    }

    protected void display(List<Schedule> beans) {
        String str = "该位置课程:\n";
        for (Schedule bean : beans) {
            str += bean.getName() + "\n上课周次：" + bean.getWeekList().toString() + "\n";
        }
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }


}
