package com.mangoplay.yeezymusic.ui.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.mangoplay.yeezymusic.R;

import androidx.appcompat.app.AppCompatActivity;

public class InformationActivity extends AppCompatActivity {
    LinearLayout terms;
    LinearLayout policy;
    ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        terms = findViewById(R.id.terms_and_conditions);
        policy = findViewById(R.id.policy);
        backButton = findViewById(R.id.back_button);

        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("terms and conditions clicked");
                startActivity(new Intent(getApplicationContext(), TermsAndConditions.class));
            }
        });

        policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("policy clicked");
                startActivity(new Intent(getApplicationContext(), PrivacyPolicy.class));
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("back");
                finish();
            }
        });
    }
}
