package com.zse233.classtable;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class LoginActivity extends AppCompatActivity {
    EditText username, password;
    Button button;
    ClassViewMode classViewMode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = findViewById(R.id.username);
        password = findViewById(R.id.editText2);
        button = findViewById(R.id.button);
        classViewMode = new ClassViewMode(getApplication());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClassTableRepo classTableRepo = new ClassTableRepo();
                List<MyClassTable> classes = classTableRepo.parse(classTableRepo.requestClassTable(classTableRepo.requestUserKey(username.getText().toString(), password.getText().toString()), 0));
                for (MyClassTable myClassTable : classes) {
                    classViewMode.insert(myClassTable);
                }
            }
        });
    }
}
