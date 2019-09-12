/**
  * Copyright 2019 bejson.com 
  */
package com.zse233.classtable.scorebean;
import java.util.List;

/**
 * Auto-generated: 2019-09-09 14:36:40
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Semester_lessons {

    private String code;
    private List<Lessons> lessons;
    private String name;
    private String season;
    private int semester_credits;
    private String semester_gp;
    private String year;
    public void setCode(String code) {
         this.code = code;
     }
     public String getCode() {
         return code;
     }

    public void setLessons(List<Lessons> lessons) {
         this.lessons = lessons;
     }
     public List<Lessons> getLessons() {
         return lessons;
     }

    public void setName(String name) {
         this.name = name;
     }
     public String getName() {
         return name;
     }

    public void setSeason(String season) {
         this.season = season;
     }
     public String getSeason() {
         return season;
     }

    public void setSemester_credits(int semester_credits) {
         this.semester_credits = semester_credits;
     }
     public int getSemester_credits() {
         return semester_credits;
     }

    public void setSemester_gp(String semester_gp) {
         this.semester_gp = semester_gp;
     }
     public String getSemester_gp() {
         return semester_gp;
     }

    public void setYear(String year) {
         this.year = year;
     }
     public String getYear() {
         return year;
     }

}