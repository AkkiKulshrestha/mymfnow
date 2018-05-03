package com.indocosmic.mymfnow;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;

/**
 * Created by Akshay on 30-04-2018.
 */

public class RoboDashboard extends AppCompatActivity{

    CardView CardLumpsumRobo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_robo_dashboard);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Robo DashBoard");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        init();

    }

    private void init() {

        //   back_btn = (ImageView) findViewById(R.id.back_btn);\

        CardLumpsumRobo = (CardView)findViewById(R.id.CardLumpsumRobo);
        CardLumpsumRobo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),RoboLumpsum.class);
                startActivity(i);
            }
        });
    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
