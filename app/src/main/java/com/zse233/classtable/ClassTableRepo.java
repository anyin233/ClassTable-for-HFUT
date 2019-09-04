package com.zse233.classtable;

import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zse233.classtable.classbean.Business_data;

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

        Request request = requestBuilder(formBody,"http://jxglstu.hfut.edu.cn:7070/appservice/home/publicdata/getSemesterAndWeekList.action");

        requestWeeklist requestweek = new requestWeeklist();
        try {
            String day = requestweek.execute(request).get();
            return day;
        } catch (ExecutionException e) {
            Log.d("TError", "" + e.getMessage());
        } catch (InterruptedException e) {
            Log.d("TError", "" + e.getMessage());
        }

        return "1970-01-01";
    }

    String requestScore(String userKey,int semedtercode){
        if(userKey.equals("-1")){
            return "";
        }
        String scoreJson = "";
        StringBuilder sb = new StringBuilder();
        sb.append("http://jxglstu.hfut.edu.cn:7070/appservice/home/")
                .append("course/getSemesterScoreList.action?projectId=2&userKey=")
                .append(userKey)
                .append("&identity=0&semestercode=")
                .append(semedtercode);

        Request request = new Request.Builder()
                .url(sb.toString())
                .addHeader("Connection", "keep-alive")
                .addHeader("Accept", "application/json, text/plain, */*")
                .addHeader("User-Agent", "Mozilla/5.0 (Linux) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/76.0.3809.111 Mobile Safari/537.36")
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("X-Requested-With", "edu.hfut.eams.mobile")
                .addHeader("Accept-Encoding", "gzip, deflate")
                .build();

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
                Response response = okHttpClient.newCall(requests[0]).execute();
                String json = response.body().string();
                JSONObject js = JSON.parseObject(json);
                JSONObject obj = js.getJSONObject("obj");
                JSONObject business = obj.getJSONObject("business_data");
                JSONArray semester = business.getJSONArray("semesters");
                JSONObject cur = semester.getJSONObject(0);
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
