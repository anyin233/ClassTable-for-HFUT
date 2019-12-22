package com.zse233.classtable.ui;


import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.squareup.picasso.Picasso;
import com.zhuangfei.timetable.model.Schedule;
import com.zhuangfei.timetable.model.ScheduleSupport;
import com.zse233.classtable.ClassDatabaseRepo;
import com.zse233.classtable.HelloAdaptor;
import com.zse233.classtable.MyClassTable;
import com.zse233.classtable.R;
import com.zse233.classtable.misc.MiscClass;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class HelloFragment extends Fragment {
    private RecyclerView recyclerView;
    private ClassDatabaseRepo repo;
    private ImageView imageView;
    int week_now = 0;
    private Toolbar toolbar;
    private TextView one, detail;
    private String imageDetail = "";


    public HelloFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_hello, container, false);

    }

    @Override
    public void onStart() {
        super.onStart();
        imageView = getActivity().findViewById(R.id.homebg);
        toolbar = getActivity().findViewById(R.id.toolbar);
        one = getActivity().findViewById(R.id.oneSentence);
        detail = getActivity().findViewById(R.id.imageDetail);

        toolbar.setVisibility(View.GONE);
        MiscClass.atScheduled(false);
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        int weekDay = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if(weekDay == 0){
            weekDay = 7;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String format_date = dateFormat.format(date);
        SharedPreferences shp = getActivity().getSharedPreferences("first_day", MODE_PRIVATE);
        String startDay = shp.getString("start", format_date);
        Date start;
        try {
            start = dateFormat.parse(startDay);
        } catch (ParseException e) {
            start = new Date();
            Log.d("TError", "" + e.getMessage());
        }
        week_now = (int) ((date.getTime() - start.getTime()) / (1000 * 60 * 60 * 24));
        week_now /= 7;
        ++week_now;
        if (week_now < 1) {
            week_now = 1;
        }
        repo = new ClassDatabaseRepo(getContext());
        List<MyClassTable> myClassTables = repo.getAllLive();
        List<Schedule> schedules = new ArrayList<>();
        for(MyClassTable myClassTable:myClassTables){
            schedules.add(myClassTable.getSchedule());
        }
        List<Schedule> curCourse = ScheduleSupport.getHaveSubjectsWithDay(schedules,week_now,weekDay-1);
        if(curCourse == null){
            curCourse = new ArrayList<>();
        }
        recyclerView = getActivity().findViewById(R.id.hello_recycler);
        recyclerView.setAdapter(new HelloAdaptor(curCourse,getLayoutInflater()));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        new SetHomeBg().execute();
    }

    private class SetHomeBg extends AsyncTask<Void, Void, Void> {
        private String imageurl = null;

        private String oneWord = null;

        @Override
        protected Void doInBackground(Void... voids) {
            try {

                Request request = new Request.Builder()
                        .url("http://cn.bing.com/HPImageArchive.aspx?format=js&idx=0&n=1")
                        .build();
                Request request1 = new Request.Builder()
                        .url("https://v1.hitokoto.cn/?c=b&encode=text")
                        .build();
                OkHttpClient client = new OkHttpClient.Builder().build();
                Response response = client.newCall(request).execute();
                String json = response.body().string();
                JSONObject body = JSON.parseObject(json);
                JSONArray images = body.getJSONArray("images");
                JSONObject detail = images.getJSONObject(0);
                imageurl = detail.getString("url");//获取每日一图
                imageDetail = detail.getString("copyright");//获取每日一图的信息
                Response words = client.newCall(request1).execute();
                oneWord = words.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (imageView != null && detail != null && imageurl != null) {
                Picasso.get()
                        .load("https://cn.bing.com" + imageurl)
                        .fit()
                        .centerCrop()
                        .into(imageView);
                detail.setText(imageDetail);
            }
            if (oneWord != null && one != null) {
                one.setText(oneWord);
            } else if (one != null) {
                one.setText(R.string.oneWordDefault);
            }
        }
    }

}
