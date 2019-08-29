package com.zse233.classtable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.zhuangfei.timetable.model.Schedule;
import com.zhuangfei.timetable.model.ScheduleEnable;
import com.zse233.classtable.classbean.Rooms;
import com.zse233.classtable.classbean.Teachers;

import java.util.ArrayList;
import java.util.List;

@Entity
public class MyClassTable implements ScheduleEnable {
    private String name; // 课程名称
    private String teachers; // 教师姓名
    private int start; //开始时间
    private int end; //结束时间
    private String weeklist; //课程周数
    private String room; //教室
    private int day; //课程日期(周几)
    private int colorRandom = 0; //颜色参数
    private String Id; //课程Id
    @PrimaryKey
    private int a_Id;

    public MyClassTable() {

    }

    ;

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

    public String getTeachers() {
        return teachers;
    }

    public void setTeachers(String teachers) {
        this.teachers = teachers;
    }

    public String getWeeklist() {
        return weeklist;
    }

    public void setWeeklist(String weeklist) {
        this.weeklist = weeklist;
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
        schedule.setTeacher(procTeachers().size() == 0 ? "无" : procTeachers().get(0));
        schedule.setWeekList(procWeeklist());
        if (procTeachers().size() > 1) {
            for (int i = 1; i < procTeachers().size(); ++i) {
                schedule.putExtras("Other Teacher " + String.valueOf(i + 1) + ": ", procTeachers().get(i));
            }
        }
        return schedule;
    }


    //重定义的setter

    public void setTeachers(List<Teachers> teachersList) {
        StringBuilder sb = new StringBuilder();
        for (Teachers teacher : teachersList) {
            sb.append(teacher.getName()).append("+");
        }
    }

    private List<String> procTeachers() {
        if (teachers == null) {
            return new ArrayList<>();
        }
        List<String> teacherList = new ArrayList<>();
        int start = 0, end = 0;
        for (int i = 0; i < teachers.length(); ++i) {
            if (teachers.charAt(i) != '+') {
                ++end;
            } else {
                teacherList.add(teachers.substring(start, end));
                if (end != teachers.length() - 1) {
                    start = end + 1;
                    end = start;
                } else {
                    break;
                }
            }
        }
        return teacherList;
    }

    public List<Integer> procWeeklist() {
        if (weeklist == null) {
            return new ArrayList<>();
        }
        List<Integer> weekL = new ArrayList<>();
        int start = 0;
        int end = 1;
        for (int i = 0; i < weeklist.length(); ++i) {
            if (weeklist.charAt(i) == ',') {
                weekL.add(Integer.valueOf(weeklist.substring(start, end)));
                start = i + 1;
                end = start + 1;
            } else {
                if (i == weeklist.length() - 1) {
                    weekL.add(Integer.valueOf(weeklist.substring(start)));
                } else {
                    end = i + 1;
                }
            }
        }
        return weekL;
    }

    public void setRoom(List<Rooms> rooms) {
        StringBuilder sb = new StringBuilder();
        for (Rooms room : rooms) {
            sb.append(room.getName())
                    .append(",");
        }
        this.room = sb.toString();
    }

    public void setRoom(String room) {
        this.room = room;
    }
}
