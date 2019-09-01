package com.zse233.classtable;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

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
                ClassTableRepo classTableRepo = new ClassTableRepo();
                ClassDatabaseRepo classDatabaseRepo = new ClassDatabaseRepo(getApplicationContext());
                classViewMode.clear();
                String userKey = classTableRepo.requestUserKey(username.getText().toString(), password.getText().toString());//请求userkey
                List<MyClassTable> classes = classTableRepo.parse(classTableRepo.requestClassTable(userKey, 0));
                for (MyClassTable myClassTable : classes) {
                    classDatabaseRepo.insert(myClassTable);
                }
                editor.putString("start", classTableRepo.requireStartDay(userKey));
                editor.apply();//将开学日期写入文件
                Toast toast_1 = Toast.makeText(getApplicationContext(), R.string.getting_done, Toast.LENGTH_SHORT);
                toast_1.show();
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, MainActivity.class);
                startActivity(intent);
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
