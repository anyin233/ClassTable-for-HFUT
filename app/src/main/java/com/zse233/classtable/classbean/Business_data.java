/**
 * Copyright 2019 bejson.com
 */
package com.zse233.classtable.classbean;

import java.util.List;

/**
 * Auto-generated: 2019-08-25 16:38:54
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Business_data {

    private String activity_id;
    private String activity_week;
    private String activity_weekstate;
    private String course_name;
    private String end_time;
    private int end_unit;
    private boolean is_conflict;
    private String lesson_code;
    private List<Rooms> rooms;
    private String start_time;
    private int start_unit;
    private int teachclass_stdcount;
    private List<Teachers> teachers;
    private int weekday;

    public void setActivity_id(String activity_id) {
        this.activity_id = activity_id;
    }

    public String getActivity_id() {
        return activity_id;
    }

    public void setActivity_week(String activity_week) {
        this.activity_week = activity_week;
    }

    public String getActivity_week() {
        return activity_week;
    }

    public void setActivity_weekstate(String activity_weekstate) {
        this.activity_weekstate = activity_weekstate;
    }

    public String getActivity_weekstate() {
        return activity_weekstate;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_unit(int end_unit) {
        this.end_unit = end_unit;
    }

    public int getEnd_unit() {
        return end_unit;
    }

    public void setIs_conflict(boolean is_conflict) {
        this.is_conflict = is_conflict;
    }

    public boolean getIs_conflict() {
        return is_conflict;
    }

    public void setLesson_code(String lesson_code) {
        this.lesson_code = lesson_code;
    }

    public String getLesson_code() {
        return lesson_code;
    }

    public void setRooms(List<Rooms> rooms) {
        this.rooms = rooms;
    }

    public List<Rooms> getRooms() {
        return rooms;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_unit(int start_unit) {
        this.start_unit = start_unit;
    }

    public int getStart_unit() {
        return start_unit;
    }

    public void setTeachclass_stdcount(int teachclass_stdcount) {
        this.teachclass_stdcount = teachclass_stdcount;
    }

    public int getTeachclass_stdcount() {
        return teachclass_stdcount;
    }

    public void setTeachers(List<Teachers> teachers) {
        this.teachers = teachers;
    }

    public List<Teachers> getTeachers() {
        return teachers;
    }

    public void setWeekday(int weekday) {
        this.weekday = weekday;
    }

    public int getWeekday() {
        return weekday;
    }

}