package com.zse233.classtable;

import android.content.Intent;
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

import java.util.List;

public class MainActivity extends AppCompatActivity {
    TimetableView timetableView;
    WeekView weekView;

    ConstraintLayout layout;

    List<MyClassTable> classes;
    int week = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        timetableView = findViewById(R.id.id_timetableView);
        ClassTableRepo classTableRepo = new ClassTableRepo();
        String userKey = classTableRepo.requestUserKey("2018214388", "YYW20011001yyw");
        classes = classTableRepo.prase(classTableRepo.requestClassTable(userKey, 12));
        Log.d("TError", userKey);
        timetableView.source(classes)
                .curWeek(1)
                .showView();

        initTimetableView();

    }

    @Override
    protected void onStart() {
        super.onStart();
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
        weekView.curWeek(1)
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

        timetableView.curWeek(1)
                .curTerm("大三下学期")
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
