package com.zse233.classtable;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.RemoteViews;

import com.zhuangfei.timetable.model.Schedule;
import com.zhuangfei.timetable.model.ScheduleSupport;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Implementation of App Widget functionality.
 */
public class TableWidget extends AppWidgetProvider {
    private final String UPDATE = "com.zse233.classtable.widget.UPDATE";

    private static int id;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        SharedPreferences shp = context.getSharedPreferences("first_day", MODE_PRIVATE);
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String format_date = dateFormat.format(date);
        String startDay = shp.getString("start", format_date);
        Date start;
        try {
            start = dateFormat.parse(startDay);
        } catch (ParseException e) {
            start = new Date();
            Log.d("TError", "" + e.getMessage());
        }
        int week_now = (int) ((date.getTime() - start.getTime()) / (1000 * 60 * 60 * 24));
        week_now /= 7;
        ++week_now;
        if (week_now < 1) {
            week_now = 1;
        }
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.table_widget);
        Calendar calendar = Calendar.getInstance();
        int weekday;
        if (calendar.get(Calendar.DAY_OF_WEEK) == 1) {
            weekday = 6;
        } else {
            weekday = calendar.get(Calendar.DAY_OF_WEEK) - 2;
        }
        ClassDatabaseRepo repo = new ClassDatabaseRepo(context);
        List<MyClassTable> allClassList = repo.getAllLive();
        List<Schedule> schedules = new ArrayList<>();
        for (MyClassTable mClass : allClassList) {
            schedules.add(mClass.getSchedule());
        }
        List<Schedule> tmpList = ScheduleSupport.getHaveSubjectsWithDay(
                schedules, week_now, weekday);
        for (Schedule schedule : tmpList) {
            switch (schedule.getStart()) {
                case 1: {
                    views.setTextViewText(R.id.class1, schedule.getName());
                    views.setTextViewText(R.id.room1, schedule.getRoom());
                    break;
                }
                case 3: {
                    views.setTextViewText(R.id.class2, schedule.getName());
                    views.setTextViewText(R.id.room2, schedule.getRoom());
                    break;
                }
                case 5: {
                    views.setTextViewText(R.id.class3, schedule.getName());
                    views.setTextViewText(R.id.room3, schedule.getRoom());
                    break;
                }
                case 7: {
                    views.setTextViewText(R.id.class4, schedule.getName());
                    views.setTextViewText(R.id.room4, schedule.getRoom());
                    break;
                }
                case 9: {
                    views.setTextViewText(R.id.class5, schedule.getName());
                    views.setTextViewText(R.id.room5, schedule.getRoom());
                    break;
                }
                default: {
                    continue;
                }
            }
        }
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            id = appWidgetId;
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created

        super.onEnabled(context);
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        final String action = intent.getAction();
        int a = 0;
        if (UPDATE.equals(action)) {
            updateAppWidget(context, AppWidgetManager.getInstance(context), id);
        }

    }
}

