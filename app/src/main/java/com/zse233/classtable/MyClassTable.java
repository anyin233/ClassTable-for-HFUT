package com.zse233.classtable;

import android.content.Context;

import com.zhuangfei.timetable.model.Schedule;
import com.zhuangfei.timetable.model.ScheduleEnable;
import com.zse233.classtable.classbean.Rooms;
import com.zse233.classtable.classbean.Teachers;

import java.util.ArrayList;
import java.util.List;

public class MyClassTable implements ScheduleEnable {
    private String name; // 课程名称
    private List<String> teachers; // 教师姓名
    private int start; //开始时间
    private int end; //结束时间
    private List<Integer> weeklist; //课程周数
    private String room; //教室
    private int day; //课程日期(周几)
    private int colorRandom = 0; //颜色参数
    private String Id; //课程Id
    private int a_Id;
    private Context context;

    public MyClassTable(String name, List<Teachers> teachers, int start, int end, String weeklist, List<Rooms> room, int day, int colorRandom, String Id, int a_Id) {
        this.name = name;
        setTeachers(teachers);
        this.start = start;
        this.end = end;
        setWeeklist(weeklist);
        setRoom(room);
        this.day = day;
        this.colorRandom = colorRandom;
        this.Id = Id;
        this.a_Id = a_Id;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getTeachers() {
        return teachers;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public List<Integer> getWeeklist() {
        return weeklist;
    }

    public String getRoom() {
        return room;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getColorRandom() {
        return colorRandom;
    }

    public void setColorRandom(int colorRandom) {
        this.colorRandom = colorRandom;
    }

    public int getA_Id() {
        return a_Id;
    }

    public void setA_Id(int a_Id) {
        this.a_Id = a_Id;
    }

    @Override
    public Schedule getSchedule() {
        Schedule schedule = new Schedule();
        schedule.setColorRandom(getColorRandom());
        schedule.setDay(getDay());
        schedule.setName(getName());
        schedule.setRoom(getRoom());
        schedule.setStart(getStart());
        schedule.setStep(getEnd() - getStart() + 1);
        schedule.setTeacher(getTeachers().get(0));
        schedule.setWeekList(getWeeklist());
        if (getTeachers().size() > 1) {
            for (int i = 1; i < getTeachers().size(); ++i) {
                schedule.putExtras("Other Teacher " + String.valueOf(i + 1) + ": ", getTeachers().get(i));
            }
        }
        return schedule;
    }


    //重定义的setter

    public void setTeachers(List<Teachers> teachersList) {
        if (this.teachers == null) {
            this.teachers = new ArrayList<>();
        }
        for (Teachers teacher : teachersList) {
            this.teachers.add(teacher.getName());
        }
    }

    public void setWeeklist(String weeklist) {
        if (this.weeklist == null) {
            this.weeklist = new ArrayList<>();
        }
        int start = 0;
        int end = 1;
        for (int i = 0; i < weeklist.length(); ++i) {
            if (weeklist.charAt(i) == ',') {
                this.weeklist.add(Integer.valueOf(weeklist.substring(start, end)));
                start = i + 1;
                end = start + 1;
            } else {
                if (i == weeklist.length() - 1) {
                    this.weeklist.add(Integer.valueOf(weeklist.substring(start)));
                } else {
                    end = i + 1;
                }
            }
        }
        int a = 0;
    }

    public void setRoom(List<Rooms> rooms) {
        StringBuilder sb = new StringBuilder();
        for (Rooms room : rooms) {
            sb.append(room.getName())
                    .append(",");
        }
        this.room = sb.toString();
    }
}
