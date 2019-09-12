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
public class Lessons {

    private String code;
    private String course_code;
    private double course_credit;
    private String course_gp;
    private String course_name;
    private List<Exam_grades> exam_grades;
    private boolean passed;
    private int score;
    private String score_text;
    public void setCode(String code) {
         this.code = code;
     }
     public String getCode() {
         return code;
     }

    public void setCourse_code(String course_code) {
         this.course_code = course_code;
     }
     public String getCourse_code() {
         return course_code;
     }

    public void setCourse_credit(double course_credit) {
         this.course_credit = course_credit;
     }
     public double getCourse_credit() {
         return course_credit;
     }

    public void setCourse_gp(String course_gp) {
         this.course_gp = course_gp;
     }
     public String getCourse_gp() {
         return course_gp;
     }

    public void setCourse_name(String course_name) {
         this.course_name = course_name;
     }
     public String getCourse_name() {
         return course_name;
     }

    public void setExam_grades(List<Exam_grades> exam_grades) {
         this.exam_grades = exam_grades;
     }
     public List<Exam_grades> getExam_grades() {
         return exam_grades;
     }

    public void setPassed(boolean passed) {
         this.passed = passed;
     }
     public boolean getPassed() {
         return passed;
     }

    public void setScore(int score) {
         this.score = score;
     }
     public int getScore() {
         return score;
     }

    public void setScore_text(String score_text) {
         this.score_text = score_text;
     }
     public String getScore_text() {
         return score_text;
     }

}