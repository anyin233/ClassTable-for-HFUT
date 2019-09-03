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

        Request request = new Request.Builder()
                .url("http://jxglstu.hfut.edu.cn:7070/appservice/home/appLogin/login.action")
                .addHeader("Connection", "keep-alive")
                .addHeader("Accept", "application/json, text/plain, */*")
                .addHeader("User-Agent", "Mozilla/5.0 (Linux) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/76.0.3809.111 Mobile Safari/537.36")
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("X-Requested-With", "edu.hfut.eams.mobile")
                .addHeader("Accept-Encoding", "gzip, deflate")
                .post(formBody)
                .build();


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

        Request request = new Request.Builder()
                .url("http://jxglstu.hfut.edu.cn:7070/appservice/home/schedule/getWeekSchedule.action")
                .addHeader("Connection", "keep-alive")
                .addHeader("Accept", "application/json, text/plain, */*")
                .addHeader("User-Agent", "Mozilla/5.0 (Linux) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/76.0.3809.111 Mobile Safari/537.36")
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("X-Requested-With", "edu.hfut.eams.mobile")
                .addHeader("Accept-Encoding", "gzip, deflate")
                .post(formBody)
                .build();

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

        Request request = new Request.Builder()
                .url("http://jxglstu.hfut.edu.cn:7070/appservice/home/publicdata/getSemesterAndWeekList.action")
                .addHeader("Connection", "keep-alive")
                .addHeader("Accept", "application/json, text/plain, */*")
                .addHeader("User-Agent", "Mozilla/5.0 (Linux) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/76.0.3809.111 Mobile Safari/537.36")
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("X-Requested-With", "edu.hfut.eams.mobile")
                .addHeader("Accept-Encoding", "gzip, deflate")
                .post(formBody)
                .build();

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

    public List<MyClassTable> parse(String testJson) {
        if (testJson == null) {
            return new ArrayList<>();
        }
        List<MyClassTable> courses = new ArrayList<>();
        //JSONObject jsonObject = JSON.parseObject("{\"code\":200,\"msg\":\"查询成功！\",\"salt\":null,\"token\":null,\"obj\":{\"business_data\":[{\"activity_id\":\"70390\",\"activity_week\":\"1,2,3,4,5,6\",\"activity_weekstate\":\"1-6周\",\"course_name\":\"Java技术\",\"end_time\":\"12:10\",\"end_unit\":4,\"is_conflict\":false,\"lesson_code\":\"0521270X--004\",\"rooms\":[{\"campus_name\":\"翡翠湖校区\",\"code\":\"00000145\",\"floor_name\":\"翠六教\",\"name\":\"翠六教101\"}],\"start_time\":\"10:20\",\"start_unit\":3,\"teachclass_stdcount\":29,\"teachers\":[{\"code\":\"2004800102\",\"name\":\"路强\"}],\"weekday\":1},{\"activity_id\":\"70392\",\"activity_week\":\"1,2,3,4,5,6,7,8\",\"activity_weekstate\":\"1-8周\",\"course_name\":\"人工智能原理\",\"end_time\":\"15:50\",\"end_unit\":6,\"is_conflict\":false,\"lesson_code\":\"0521362B--001\",\"rooms\":[{\"campus_name\":\"翡翠湖校区\",\"code\":\"00000130\",\"floor_name\":\"翠四教\",\"name\":\"翠四教208\"}],\"start_time\":\"14:00\",\"start_unit\":5,\"teachclass_stdcount\":29,\"teachers\":[{\"code\":\"2013800025\",\"name\":\"李磊\"}],\"weekday\":1},{\"activity_id\":\"69778\",\"activity_week\":\"1,2,3,4,5,6\",\"activity_weekstate\":\"1-6周\",\"course_name\":\"程序设计艺术与方法\",\"end_time\":\"12:10\",\"end_unit\":4,\"is_conflict\":false,\"lesson_code\":\"0521280X--001\",\"rooms\":[{\"campus_name\":\"翡翠湖校区\",\"code\":\"00000053\",\"floor_name\":\"翠七教\",\"name\":\"翠七教101\"}],\"start_time\":\"10:20\",\"start_unit\":3,\"teachclass_stdcount\":132,\"teachers\":[{\"code\":\"1999800037\",\"name\":\"徐本柱\"},{\"code\":\"2015800164\",\"name\":\"曹力\"}],\"weekday\":2},{\"activity_id\":\"70393\",\"activity_week\":\"1,2,3,4,5,6\",\"activity_weekstate\":\"1-6周\",\"course_name\":\"大数据处理技术\",\"end_time\":\"15:50\",\"end_unit\":6,\"is_conflict\":false,\"lesson_code\":\"0521322B--001\",\"rooms\":[{\"campus_name\":\"翡翠湖校区\",\"code\":\"00000130\",\"floor_name\":\"翠四教\",\"name\":\"翠四教208\"}],\"start_time\":\"14:00\",\"start_unit\":5,\"teachclass_stdcount\":29,\"teachers\":[{\"code\":\"2003800064\",\"name\":\"吴共庆\"}],\"weekday\":2},{\"activity_id\":\"70392\",\"activity_week\":\"1,2,3,4,5,6,7,8\",\"activity_weekstate\":\"1-8周\",\"course_name\":\"人工智能原理\",\"end_time\":\"10:00\",\"end_unit\":2,\"is_conflict\":false,\"lesson_code\":\"0521362B--001\",\"rooms\":[{\"campus_name\":\"翡翠湖校区\",\"code\":\"00000130\",\"floor_name\":\"翠四教\",\"name\":\"翠四教208\"}],\"start_time\":\"08:10\",\"start_unit\":1,\"teachclass_stdcount\":29,\"teachers\":[{\"code\":\"2013800025\",\"name\":\"李磊\"}],\"weekday\":3},{\"activity_id\":\"69820\",\"activity_week\":\"1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16\",\"activity_weekstate\":\"1-16周\",\"course_name\":\"大学物理B（下）\",\"end_time\":\"12:10\",\"end_unit\":4,\"is_conflict\":false,\"lesson_code\":\"1000241B--020\",\"rooms\":[{\"campus_name\":\"翡翠湖校区\",\"code\":\"00000070\",\"floor_name\":\"翠七教\",\"name\":\"翠七教306\"}],\"start_time\":\"10:20\",\"start_unit\":3,\"teachclass_stdcount\":157,\"teachers\":[{\"code\":\"2004800141\",\"name\":\"李平(物理)\"}],\"weekday\":3},{\"activity_id\":\"70393\",\"activity_week\":\"1,2,3,4,5,6\",\"activity_weekstate\":\"1-6周\",\"course_name\":\"大数据处理技术\",\"end_time\":\"10:00\",\"end_unit\":2,\"is_conflict\":false,\"lesson_code\":\"0521322B--001\",\"rooms\":[{\"campus_name\":\"翡翠湖校区\",\"code\":\"00000130\",\"floor_name\":\"翠四教\",\"name\":\"翠四教208\"}],\"start_time\":\"08:10\",\"start_unit\":1,\"teachclass_stdcount\":29,\"teachers\":[{\"code\":\"2003800064\",\"name\":\"吴共庆\"}],\"weekday\":4},{\"activity_id\":\"73241\",\"activity_week\":\"3,4,5,6,7,8,9,10,11,12,13,14,15,16\",\"activity_weekstate\":\"3-16周\",\"course_name\":\"大学英语（4）\",\"end_time\":\"12:10\",\"end_unit\":4,\"is_conflict\":false,\"lesson_code\":\"1500041B--012\",\"rooms\":[{\"campus_name\":\"翡翠湖校区\",\"code\":\"00000175\",\"floor_name\":\"翠六教\",\"name\":\"翠六教408\"}],\"start_time\":\"10:20\",\"start_unit\":3,\"teachclass_stdcount\":42,\"teachers\":[{\"code\":\"2002800114\",\"name\":\"张培蓓\"}],\"weekday\":4},{\"activity_id\":\"73619\",\"activity_week\":\"3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18\",\"activity_weekstate\":\"3-18周\",\"course_name\":\"大学体育3\",\"end_time\":\"15:50\",\"end_unit\":6,\"is_conflict\":false,\"lesson_code\":\"5100061B--071\",\"rooms\":[],\"start_time\":\"14:00\",\"start_unit\":5,\"teachclass_stdcount\":36,\"teachers\":[{\"code\":\"2003800154\",\"name\":\"刘飞虎\"}],\"weekday\":4},{\"activity_id\":\"69820\",\"activity_week\":\"1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16\",\"activity_weekstate\":\"1-16周\",\"course_name\":\"大学物理B（下）\",\"end_time\":\"10:00\",\"end_unit\":2,\"is_conflict\":false,\"lesson_code\":\"1000241B--020\",\"rooms\":[{\"campus_name\":\"翡翠湖校区\",\"code\":\"00000070\",\"floor_name\":\"翠七教\",\"name\":\"翠七教306\"}],\"start_time\":\"08:10\",\"start_unit\":1,\"teachclass_stdcount\":157,\"teachers\":[{\"code\":\"2004800141\",\"name\":\"李平(物理)\"}],\"weekday\":5},{\"activity_id\":\"70390\",\"activity_week\":\"1,2,3,4,5,6\",\"activity_weekstate\":\"1-6周\",\"course_name\":\"Java技术\",\"end_time\":\"12:10\",\"end_unit\":4,\"is_conflict\":false,\"lesson_code\":\"0521270X--004\",\"rooms\":[{\"campus_name\":\"翡翠湖校区\",\"code\":\"00000145\",\"floor_name\":\"翠六教\",\"name\":\"翠六教101\"}],\"start_time\":\"10:20\",\"start_unit\":3,\"teachclass_stdcount\":29,\"teachers\":[{\"code\":\"2004800102\",\"name\":\"路强\"}],\"weekday\":5},{\"activity_id\":\"69778\",\"activity_week\":\"1,2,3,4,5,6\",\"activity_weekstate\":\"1-6周\",\"course_name\":\"程序设计艺术与方法\",\"end_time\":\"17:50\",\"end_unit\":8,\"is_conflict\":false,\"lesson_code\":\"0521280X--001\",\"rooms\":[{\"campus_name\":\"翡翠湖校区\",\"code\":\"00000053\",\"floor_name\":\"翠七教\",\"name\":\"翠七教101\"}],\"start_time\":\"16:00\",\"start_unit\":7,\"teachclass_stdcount\":132,\"teachers\":[{\"code\":\"1999800037\",\"name\":\"徐本柱\"},{\"code\":\"2015800164\",\"name\":\"曹力\"}],\"weekday\":5}],\"err_code\":\"00000\",\"err_msg\":\"\"},\"error\":null}");
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

    class requestClass extends AsyncTask<Request, Void, String> {


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

    class requestWeeklist extends AsyncTask<Request, Void, String> {

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

    class requestUser extends AsyncTask<Request, Void, String> {

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
}
