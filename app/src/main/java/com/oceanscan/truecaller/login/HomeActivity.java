package com.oceanscan.truecaller.login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.truecaller.android.sdk.TrueProfile;

public class HomeActivity extends AppCompatActivity {
    TrueProfile trueProfile;
    TextView name, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setTitle("Home");

        if (getIntent().hasExtra("profile"))
            trueProfile = getIntent().getParcelableExtra("profile");

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        if (trueProfile != null) {
            name.setText("Welcome " + trueProfile.firstName);
            email.setText(trueProfile.email);
        }


    }
}
