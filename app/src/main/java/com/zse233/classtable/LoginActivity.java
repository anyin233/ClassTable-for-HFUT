package com.zse233.classtable;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.ArraySet;
import android.util.Pair;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.android.material.snackbar.Snackbar;
import com.zse233.classtable.misc.LoginErrorThrowable;
import com.zse233.classtable.misc.MiscClass;
import com.zse233.classtable.term.TermInfo;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class LoginActivity extends AppCompatActivity {
    EditText username, password;
    Button button;
    ClassViewMode classViewMode;
    SharedPreferences shp;
    SharedPreferences.Editor editor;
    Set<String> termName = new ArraySet<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = findViewById(R.id.username);
        password = findViewById(R.id.editText2);
        button = findViewById(R.id.button);
        classViewMode = ViewModelProviders.of(this).get(ClassViewMode.class);
        shp = getSharedPreferences("first_day", MODE_PRIVATE);
        editor = shp.edit();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Toast toast = Toast.makeText(getApplicationContext(), R.string.Getting, Toast.LENGTH_SHORT);
                toast.show();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                assert imm != null;
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                Observable.create(new ObservableOnSubscribe<TermInfo>() {
                    private Throwable error = new LoginErrorThrowable();

                    @Override
                    public void subscribe(ObservableEmitter<TermInfo> emitter) throws IOException {
                        ClassTableRepo classTableRepo = new ClassTableRepo();
                        ClassDatabaseRepo classDatabaseRepo = new ClassDatabaseRepo(getApplicationContext());

                        String userKey = classTableRepo.requestUserKey(
                                username.getText().toString(),
                                password.getText().toString());//请求userkey

                        if (userKey.equals("-1")) {
                            emitter.onError(error);
                        } else {
                            Pair<Integer, String> startDayPair = classTableRepo.requireStartDay(userKey);
                            classDatabaseRepo.clear();
                            List<MyClassTable> classes = classTableRepo.parse(classTableRepo.requestClassTable(userKey, startDayPair.first));
                            MiscClass.setTermCode(startDayPair.first);
                            for (MyClassTable myClassTable : classes) {
                                classDatabaseRepo.insert(myClassTable);
                            }
                            editor.putString("start", startDayPair.second);
                            editor.putString("Key", userKey);
                            String json = classTableRepo.requestSemesterList(userKey);
                            JSONObject semesterList = JSON.parseObject(json);
                            JSONArray list = semesterList.getJSONObject("obj")
                                    .getJSONObject("business_data")
                                    .getJSONArray("semesters");
                            for (int i = 0; i < list.size(); ++i) {
                                JSONObject semester = list.getJSONObject(i);
                                TermInfo termInfo = new TermInfo(semester.getInteger("code"), semester.getString("name"));
                                emitter.onNext(termInfo);
                            }
                            emitter.onComplete();

                        }
                    }
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<TermInfo>() {

                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(TermInfo termInfo) {
                                editor.putInt(termInfo.getName(), termInfo.getCode());
                                termName.add(termInfo.getName());
                            }


                            @Override
                            public void onError(Throwable e) {
                                Snackbar snackbar = Snackbar.make(
                                        view,
                                        "登录失败，请检查当前网络状态或者账号密码有误",
                                        Snackbar.LENGTH_LONG);
                                View view1 = snackbar.getView();
                                view1.setBackground(getDrawable(R.color.colorPrimaryDark));
                                snackbar.show();
                            }

                            @Override
                            public void onComplete() {

                                editor.putStringSet("terms", termName);
                                editor.apply();
                                Toast.makeText(getApplicationContext(), "获取成功", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent();
                                intent.setClass(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                            }

                        });

            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.setClass(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}
