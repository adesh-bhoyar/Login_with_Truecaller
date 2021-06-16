package com.adintech.mytruecaller;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.truecaller.android.sdk.TrueProfile;

public class HomeActivity extends AppCompatActivity {

    //variables
    private TrueProfile trueProfile;
    private TextView name, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //set title
        setTitle("Home");

        //get data
        if (getIntent().hasExtra("profile"))
            trueProfile = getIntent().getParcelableExtra("profile");

        //get views
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);

        if (trueProfile != null) {
            name.setText("Welcome " + trueProfile.firstName);
            email.setText(trueProfile.email);
        }
    }
}