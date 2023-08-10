package com.mangoplay.yeezymusic.ui;

import android.content.Intent;
import android.os.Bundle;

import com.mangoplay.yeezymusic.MainActivity;
import com.mangoplay.yeezymusic.ui.shorts.ShortsActivity;
import com.mangoplay.yeezymusic.ui.tinder.TinderActivity;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
//        startActivity(new Intent(SplashScreenActivity.this, TinderActivity.class));
//        startActivity(new Intent(SplashScreenActivity.this, ShortsActivity.class));
        startActivity(new Intent(SplashScreenActivity.this, AuthenticationActiviity.class));
        finish();
    }
}

