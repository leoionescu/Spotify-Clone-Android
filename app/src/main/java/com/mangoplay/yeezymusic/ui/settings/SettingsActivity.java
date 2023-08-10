package com.mangoplay.yeezymusic.ui.settings;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.mangoplay.yeezymusic.MainActivity;
import com.mangoplay.yeezymusic.R;
import com.mangoplay.yeezymusic.objects.Settings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

public class SettingsActivity extends AppCompatActivity {

    SwitchCompat switch1;
    ImageButton backButton;
    LinearLayout information;
    LinearLayout review;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        switch1 = findViewById(R.id.switch1);
        backButton = findViewById(R.id.back_button);
        information = findViewById(R.id.information);
        review = findViewById(R.id.review);
        setOnClickListeners();
//        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//
//            }
//        });
    }

    private void setOnClickListeners() {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("back button");
                onBackPressed();
            }
        });

        try {
            switch1.setChecked(MainActivity.settings.isShowVideo());
        } catch (NullPointerException e){
            e.printStackTrace();
        }

        information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("clicked information");
                startActivity(new Intent(getApplicationContext(), InformationActivity.class));
            }
        });

        review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("review");
                final String myappPackageName = getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + myappPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + myappPackageName)));
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("destroy");
        try {
            Settings settings = new Settings(switch1.isChecked());
            MainActivity.saveSettings(settings);
        } catch (Exception e){}
    }
}
