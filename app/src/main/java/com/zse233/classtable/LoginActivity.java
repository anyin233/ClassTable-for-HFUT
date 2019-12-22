package com.zse233.classtable;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class LoginActivity extends AppCompatActivity {
    EditText username, password;
    Button button;
    ClassViewMode classViewMode;
    SharedPreferences shp;
    SharedPreferences.Editor editor;
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
            public void onClick(View view) {
                Toast toast = Toast.makeText(getApplicationContext(), R.string.Getting, Toast.LENGTH_SHORT);
                toast.show();
                try {
                    wait(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                ClassTableRepo classTableRepo = new ClassTableRepo();
                ClassDatabaseRepo classDatabaseRepo = new ClassDatabaseRepo(getApplicationContext());

                String userKey = classTableRepo.requestUserKey(
                        username.getText().toString(),
                        password.getText().toString());//请求userkey
                if (userKey.equals("-1")) {
                    Snackbar snackbar = Snackbar.make(
                            view,
                            "登录失败，请检查当前网络状态或者账号密码有误",
                            Snackbar.LENGTH_LONG);
                    View view1 = snackbar.getView();
                    view1.setBackground(getDrawable(R.color.colorPrimaryDark));
                    snackbar.show();
                } else {
                    classDatabaseRepo.clear();
                    List<MyClassTable> classes = classTableRepo.parse(classTableRepo.requestClassTable(userKey, 0));
                    for (MyClassTable myClassTable : classes) {
                        classDatabaseRepo.insert(myClassTable);
                    }
                    editor.putString("start", classTableRepo.requireStartDay(userKey));
                    editor.putString("Key",userKey);
                    editor.apply();//将开学日期写入文件
                    Snackbar snackbar = Snackbar.make(view, "获取成功", Snackbar.LENGTH_LONG);
                    View view1 = snackbar.getView();
                    view1.setBackground(getDrawable(R.color.colorPrimaryDark));
                    snackbar.show();
                    Intent intent = new Intent();
                    intent.setClass(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }


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
