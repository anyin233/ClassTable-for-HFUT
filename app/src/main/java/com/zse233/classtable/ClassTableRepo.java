package com.zse233.classtable;

import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zse233.classtable.classbean.Business_data;
import com.zse233.classtable.scoredatabase.Score;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class ClassTableRepo {

    

    String requestUserKey(String username, String password) {
        String json, key = "-1";


        FormBody formBody = new FormBody.Builder()
                .add("password", Base64.encodeToString(password.getBytes(), Base64.DEFAULT).trim())
                .add("username", username)
                .add("identity", "0")
                .build();

        Request request = requestBuilder(formBody,"http://jxglstu.hfut.edu.cn:7070/appservice/home/appLogin/login.action");


        requestUser requestuser = new requestUser();
        try {
            key = requestuser.execute(request).get();
        } catch (InterruptedException e) {
            Log.d("TError", "On Login " + e.getMessage());
        } catch (ExecutionException e) {
            Log.d("TError", "On Login " + e.getMessage());
        }

        return key;
    }

    String requestClassTable(String userkey, int curweek) {
        if (userkey.equals("-1")) {
            return null;
        }


        FormBody formBody = new FormBody.Builder()
                .add("userKey", userkey)
                .add("projectId", "2")
                .add("identity", "0")
                .add("weekIndex", String.valueOf(curweek))
                .add("semestercode", "035")
                .build();

        Request request = requestBuilder(formBody,"http://jxglstu.hfut.edu.cn:7070/appservice/home/schedule/getWeekSchedule.action");

        requestClass requestclass = new requestClass();
        try {
            return requestclass.execute(request).get();
        } catch (InterruptedException e) {
            Log.d("TError", "On Get ClassTable" + e.getMessage());
        } catch (ExecutionException e) {
            Log.d("TError", "On Get ClassTable" + e.getMessage());
        }
        return null;
    }


    public String requireStartDay(String userkey) {//请求开学日期
        FormBody formBody = new FormBody.Builder()
                .add("userKey", userkey)
                .add("projectId", "2")
                .build();

        Request requestSemesterCode = requestBuilder(formBody,
                "http://jxglstu.hfut.edu.cn:7070/appservice/home/publicdata/getSemesterList.action?projectId=2&userKey=" + userkey);

        Request requestFirstDay = requestBuilder(formBody, "http://jxglstu.hfut.edu.cn:7070/appservice/home/publicdata/getSemesterAndWeekList.action");

        requestWeeklist requestweek = new requestWeeklist();
        try {
            String day = requestweek.execute(requestSemesterCode, requestFirstDay).get();
            return day;
        } catch (ExecutionException e) {
            Log.d("TError", "" + e.getMessage());
        } catch (InterruptedException e) {
            Log.d("TError", "" + e.getMessage());
        }

        return "1970-01-01";
    }

    public String requestScore(String userKey,int semedtercode){
        if(userKey.equals("-1")){
            return "";
        }
        String scoreJson = "";
        StringBuilder sb = new StringBuilder();
        sb.append("http://jxglstu.hfut.edu.cn:7070/appservice/home/")
                .append("course/getSemesterScoreList.action?projectId=2&userKey=")
                .append(userKey)
                .append("&identity=0&semestercode=0")
                .append(semedtercode);

        Request request = new Request.Builder()
                .url(sb.toString())
                .addHeader("Connection", "keep-alive")
                .addHeader("Accept", "application/json, text/plain, */*")
                .addHeader("User-Agent", "Mozilla/5.0 (Linux) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/76.0.3809.111 Mobile Safari/537.36")
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("X-Requested-With", "edu.hfut.eams.mobile")
                .addHeader("Accept-Encoding", "gzip, deflate")
                .get()
                .build();

        try{
            scoreJson = new RequestScore().execute(request).get();
        }catch(InterruptedException e){
            Log.d("TError","@request score"+e.getMessage());
        }catch (ExecutionException e){
            Log.d("TError","@request score"+e.getMessage());
        }

        return scoreJson;
    }

    public List<MyClassTable> parse(String testJson) {
        if (testJson == null) {
            return new ArrayList<>();
        }
        List<MyClassTable> courses = new ArrayList<>();
        JSONObject jsonObject = JSON.parseObject(testJson);
        JSONObject obj = jsonObject.getJSONObject("obj");
        JSONArray business = obj.getJSONArray("business_data");
        List<Business_data> classes = JSON.parseArray(business.toJSONString(), Business_data.class);

        for (Business_data aClass : classes) {
            int colorCode = Integer.valueOf(aClass.getActivity_id()) / 10000;
            MyClassTable course = new MyClassTable(aClass.getCourse_name(), aClass.getTeachers(), aClass.getStart_unit(), aClass.getEnd_unit(), aClass.getActivity_week(), aClass.getRooms(), aClass.getWeekday(), colorCode, aClass.getLesson_code(), Integer.valueOf(aClass.getActivity_id()));
            courses.add(course);
        }

        return courses;
    }

    public List<Score> parseScore(String Json){
        if(Json.equals("")){
            return new ArrayList<>();
        }
        List<Score> scores = new ArrayList<>();
        JSONObject jsonObject = JSON.parseObject(Json);
        JSONObject obj = jsonObject.getJSONObject("obj");
        JSONObject business = obj.getJSONObject("business_data");
        JSONArray semesterLessons = business.getJSONArray("semester_lessons");
        JSONObject lessons = semesterLessons.getJSONObject(0);
        JSONArray trueLessons = lessons.getJSONArray("lessons");
        for(int i=0;i < trueLessons.size();++i){
            JSONObject mScore = trueLessons.getJSONObject(i);
            String name = mScore.getString("course_name");
            String score = mScore.getString("score_text");
            StringBuilder sb = new StringBuilder();
            JSONArray scoreDetail = mScore.getJSONArray("exam_grades");
            for(int s=0;s<scoreDetail.size();++s){
                JSONObject detail = scoreDetail.getJSONObject(s);
                sb.append(detail.getString("type"))
                        .append("：")
                        .append(detail.getString("score_text"))
                        .append(" ");
            }
            Score score1 = new Score(score,name,sb.toString());
            scores.add(score1);
        }
        /*List<Lessons> lessonsTemp = JSON.parseArray(trueLessons.toJSONString(),Lessons.class);

        for(Lessons lessons1 : lessonsTemp){
            StringBuilder sb = new StringBuilder();
            for(Exam_grades grades : lessons1.getExam_grades()){
                sb.append(grades.getType()).append("：").append(scores);
            }
            Score score = new Score(lessons1.getScore_text(),lessons1.getCourse_name(),sb.toString());
            scores.add(score);
        }*/
        return scores;

    }
    static class requestClass extends AsyncTask<Request, Void, String> {


        @Override
        protected String doInBackground(Request... requests) {
            try {
                OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
                Response response = okHttpClient.newCall(requests[0]).execute();
                String json = response.body().string();
                return json;
            } catch (IOException e) {
                Log.d("myTag", "登录错误");
            }
            return null;
        }
    }

    static class requestWeeklist extends AsyncTask<Request, Void, String> {

        @Override
        protected String doInBackground(Request... requests) {
            try {
                OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
                Response response = okHttpClient.newCall(requests[0]).execute();//请求当前学期
                String json = response.body().string();
                JSONObject js = JSON.parseObject(json);
                JSONObject obj = js.getJSONObject("obj");
                JSONObject business = obj.getJSONObject("business_data");
                String semCode = business.getString("cur_semester_code");

                Response response2 = okHttpClient.newCall(requests[1]).execute();//请求本学期开始日
                json = response2.body().string();
                js = JSON.parseObject(json);
                obj = js.getJSONObject("obj");
                business = obj.getJSONObject("business_data");
                JSONArray semester = business.getJSONArray("semesters");
                int index = 0;
                JSONObject cur = semester.getJSONObject(index);
                while (!cur.getString("code").equals(semCode)) {
                    ++index;
                    cur = semester.getJSONObject(index);
                }//使用循环确定哪个数组是对应的当前学期
                JSONArray weeks = cur.getJSONArray("weeks");
                JSONObject first_week = weeks.getJSONObject(0);
                String first_day = first_week.getString("begin_on");
                return first_day;
            } catch (IOException e) {
                Log.d("myTag", "登录错误");
            }
            return null;
        }

    }

    static class requestUser extends AsyncTask<Request, Void, String> {

        @Override
        protected String doInBackground(Request... requests) {
            String json = " ";
            try {
                OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
                Response response = okHttpClient.newCall(requests[0]).execute();

                String key = "-1";
                json = response.body().string();
                JSONObject jsonObject = JSON.parseObject(json);
                if (jsonObject.getString("code").equals("200")) {
                    JSONObject obj = jsonObject.getJSONObject("obj");
                    key = obj.getString("userKey");
                }
                Log.d("TError", "" + response.code());
                return key;

            } catch (IOException e) {
                Log.d("TError", "登录错误" + json);
                Log.d("TError", " " + e.getMessage());
                return "-1";
            }
        }

    }

    static class RequestScore extends AsyncTask<Request,Void,String>{

        @Override
        protected String doInBackground(Request... requests) {
            OkHttpClient client = new OkHttpClient.Builder().build();
            try{
                Response response = client.newCall(requests[0]).execute();
                return response.body().string();

            }catch(IOException e){
                Log.d("TError","成绩获取出错");
                Log.d("TError",""+e.getMessage());
                return "";
            }
        }
    }

    private Request requestBuilder(FormBody formBody,String url){
        return new Request.Builder()
                .url(url)
                .addHeader("Connection", "keep-alive")
                .addHeader("Accept", "application/json, text/plain, */*")
                .addHeader("User-Agent", "Mozilla/5.0 (Linux) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/76.0.3809.111 Mobile Safari/537.36")
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("X-Requested-With", "edu.hfut.eams.mobile")
                .addHeader("Accept-Encoding", "gzip, deflate")
                .post(formBody)
                .build();
    }
}
