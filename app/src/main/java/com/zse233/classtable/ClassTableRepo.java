package com.zse233.classtable;

import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.util.Pair;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zse233.classtable.classbean.Business_data;
import com.zse233.classtable.scoredatabase.Score;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class ClassTableRepo {
    private String baseUrl = "http://jxglstu.hfut.edu.cn:7070/appservice/home";

    

    String requestUserKey(String username, String password) {
        String key = "-1";


        FormBody formBody = new FormBody.Builder()
                .add("password", Base64.encodeToString(password.getBytes(), Base64.DEFAULT).trim())
                .add("username", username)
                .add("identity", "0")
                .build();

        Request request = requestBuilder(formBody,"http://jxglstu.hfut.edu.cn:7070/appservice/home/appLogin/login.action");


        requestUser requestuser = new requestUser();
        try {
            key = requestuser.execute(request).get();
        } catch (InterruptedException | ExecutionException e) {
            Log.d("TError", "On Login " + e.getMessage());
        }

        return key;
    }

    String requestClassTable(String userkey, int semesterCode) {
        if (userkey.equals("-1")) {
            return null;
        }


        FormBody formBody = new FormBody.Builder()
                .add("userKey", userkey)
                .add("projectId", "2")
                .add("identity", "0")
                .add("weekIndex", String.valueOf(0))
                .add("semestercode", "0" + semesterCode)
                .build();

        Request request = requestBuilder(formBody,"http://jxglstu.hfut.edu.cn:7070/appservice/home/schedule/getWeekSchedule.action");

        requestClass requestclass = new requestClass();
        try {
            return requestclass.execute(request).get();
        } catch (InterruptedException | ExecutionException e) {
            Log.d("TError", "On Get ClassTable" + e.getMessage());
        }
        return null;
    }

    public String requestSemesterList(String userKey) throws IOException {
        Request request = getBuilder("/publicdata/getSemesterList.action?projectId=2&userKey=" + userKey);

        OkHttpClient client = new OkHttpClient.Builder().build();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            return Objects.requireNonNull(response.body()).string();
        }
        return "-1";
    }

    Pair<Integer, String> requireStartDay(String userkey) {//请求开学日期
        FormBody formBody = new FormBody.Builder()
                .add("userKey", userkey)
                .add("projectId", "2")
                .build();

        Request requestSemesterCode = requestBuilder(formBody,
                "http://jxglstu.hfut.edu.cn:7070/appservice/home/publicdata/getSemesterList.action?projectId=2&userKey=" + userkey);

        Request requestFirstDay = requestBuilder(formBody, "http://jxglstu.hfut.edu.cn:7070/appservice/home/publicdata/getSemesterAndWeekList.action");

        requestWeeklist requestweek = new requestWeeklist();
        try {
            return requestweek.execute(requestSemesterCode, requestFirstDay).get();
        } catch (ExecutionException | InterruptedException e) {
            Log.d("TError", "" + e.getMessage());
        }

        return Pair.create(0, "1970-01-01");
    }

    public String requestScore(String userKey,int semedtercode){
        if(userKey.equals("-1")){
            return "";
        }
        String scoreJson = "";

        String sb = "http://jxglstu.hfut.edu.cn:7070/appservice/home/" +
                "course/getSemesterScoreList.action?projectId=2&userKey=" +
                userKey +
                "&identity=0&semestercode=0" +
                semedtercode;
        Request request = new Request.Builder()
                .url(sb)
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
        } catch (InterruptedException | ExecutionException e) {
            Log.d("TError","@request score"+e.getMessage());
        }

        return scoreJson;
    }

    List<MyClassTable> parse(String testJson) {
        if (testJson == null) {
            return new ArrayList<>();
        }
        List<MyClassTable> courses = new ArrayList<>();
        JSONObject jsonObject = JSON.parseObject(testJson);
        JSONObject obj = jsonObject.getJSONObject("obj");
        JSONArray business = obj.getJSONArray("business_data");
        List<Business_data> classes = JSON.parseArray(business.toJSONString(), Business_data.class);

        for (Business_data aClass : classes) {
            int colorCode = Integer.parseInt(aClass.getActivity_id()) / 10000;
            MyClassTable course = new MyClassTable(aClass.getCourse_name(), aClass.getTeachers(), aClass.getStart_unit(), aClass.getEnd_unit(), aClass.getActivity_week(), aClass.getRooms(), aClass.getWeekday(), colorCode, aClass.getLesson_code(), Integer.parseInt(aClass.getActivity_id()));
            courses.add(course);
        }

        return courses;
    }

    public List<Score> parseScore(String Json){
        List<Score> scores = new ArrayList<>();
        JSONObject jsonObject = JSON.parseObject(Json);
        JSONObject obj = jsonObject.getJSONObject("obj");
        JSONObject business = obj.getJSONObject("business_data");
        JSONArray semesterLessons = business.getJSONArray("semester_lessons");
        if (semesterLessons.size() == 0) {
            return new ArrayList<>();
        }
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

    private Request getBuilder(String url) {
        return new Request.Builder()
                .url(baseUrl + url)
                .addHeader("Connection", "keep-alive")
                .addHeader("Accept", "application/json, text/plain, */*")
                .addHeader("User-Agent", "Mozilla/5.0 (Linux) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/76.0.3809.111 Mobile Safari/537.36")
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("X-Requested-With", "edu.hfut.eams.mobile")
                .addHeader("Accept-Encoding", "gzip, deflate")
                .get()
                .build();
    }

    static class requestClass extends AsyncTask<Request, Void, String> {


        @Override
        protected String doInBackground(Request... requests) {
            try {
                OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
                Response response = okHttpClient.newCall(requests[0]).execute();
                return Objects.requireNonNull(response.body()).string();
            } catch (IOException e) {
                Log.d("myTag", "登录错误");
            }
            return null;
        }
    }

    static class requestWeeklist extends AsyncTask<Request, Void, Pair<Integer, String>> {

        @Override
        protected Pair<Integer, String> doInBackground(Request... requests) {
            try {
                OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
                Response response = okHttpClient.newCall(requests[0]).execute();//请求当前学期
                String json = Objects.requireNonNull(response.body()).string();
                JSONObject js = JSON.parseObject(json);
                JSONObject obj = js.getJSONObject("obj");
                JSONObject business = obj.getJSONObject("business_data");
                String semCode = business.getString("cur_semester_code");

                Response response2 = okHttpClient.newCall(requests[1]).execute();//请求本学期开始日
                json = Objects.requireNonNull(response2.body()).string();
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
                return Pair.create(Integer.parseInt(semCode), first_day);
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
                json = Objects.requireNonNull(response.body()).string();
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

    static class RequestScore extends AsyncTask<Request, Void, String> {

        @Override
        protected String doInBackground(Request... requests) {
            OkHttpClient client = new OkHttpClient.Builder().build();
            try {
                Response response = client.newCall(requests[0]).execute();
                return Objects.requireNonNull(response.body()).string();

            } catch (IOException e) {
                Log.d("TError", "成绩获取出错");
                Log.d("TError", "" + e.getMessage());
                return "";
            }
        }
    }
}
