package com.zse233.classtable;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.zhuangfei.timetable.view.WeekView;

public class MainActivity extends AppCompatActivity {
    private AppBarConfiguration appBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final DrawerLayout drawer = findViewById(R.id.drawer);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.bringToFront();
        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.homeFragment, R.id.scoreFragment,R.id.helloFragment)
                .setDrawerLayout(drawer)
                .build();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(GravityCompat.START);
            }
        });
        final NavController navController = Navigation.findNavController(this, R.id.fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.score: {
                        navController.navigate(R.id.scoreFragment);
                        break;
                    }
                    case R.id.classTable: {
                        navController.navigate(R.id.homeFragment);
                        break;
                    }

                }
                drawer.closeDrawers();
                return true;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.changeWeek: {
                WeekView weekView;
                weekView = findViewById(R.id.id_weekview);
                if (weekView.isShowing()) {
                    weekView.isShow(false)
                            .setBackgroundColor(0);
                } else {
                    weekView.isShow(true)
                            .setBackgroundColor(0xFFFFFF);
                }
                return true;
            }
            case R.id.login: {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
            case R.id.end: {
                finish();
            }
        }
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.fragment);
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        NavController navController = Navigation.findNavController(this, R.id.fragment);
        if (navController.getCurrentDestination().getId() == R.id.scoreFragment) {
            navController.navigate(R.id.helloFragment);
        } else if(navController.getCurrentDestination().getId() == R.id.homeFragment){
            navController.navigate(R.id.helloFragment);
        } else if (navController.getCurrentDestination().getId() == R.id.helloFragment) {
            finish();
        }
    }
}
