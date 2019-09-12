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
public class Business_data {

    private List<Semester_lessons> semester_lessons;
    private int total_credits;
    private String total_gp;
    public void setSemester_lessons(List<Semester_lessons> semester_lessons) {
         this.semester_lessons = semester_lessons;
     }
     public List<Semester_lessons> getSemester_lessons() {
         return semester_lessons;
     }

    public void setTotal_credits(int total_credits) {
         this.total_credits = total_credits;
     }
     public int getTotal_credits() {
         return total_credits;
     }

    public void setTotal_gp(String total_gp) {
         this.total_gp = total_gp;
     }
     public String getTotal_gp() {
         return total_gp;
     }

}