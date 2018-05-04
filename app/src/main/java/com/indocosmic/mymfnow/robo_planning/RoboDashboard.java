package com.indocosmic.mymfnow.robo_planning;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;

import com.indocosmic.mymfnow.Home;
import com.indocosmic.mymfnow.R;

/**
 * Created by Akshay on 30-04-2018.
 */

public class RoboDashboard extends AppCompatActivity{

    CardView CardLumpsumRobo,CardSIP_Robo,CardRoboSavedPlans,CardRoboOnlineInvestment;

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
                overridePendingTransition(R.animator.move_left,R.animator.move_right);
                finish();
            }
        });

        CardSIP_Robo = (CardView) findViewById(R.id.CardSIP_Robo);
        CardSIP_Robo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),RoboSIP.class);
                startActivity(i);
                overridePendingTransition(R.animator.move_left,R.animator.move_right);
                finish();
            }
        });

        CardRoboSavedPlans = (CardView)findViewById(R.id.CardRoboSavedPlans);
        CardRoboSavedPlans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),RoboSavedPlans.class);
                startActivity(i);
                overridePendingTransition(R.animator.move_left,R.animator.move_right);
                finish();
            }
        });

        CardRoboOnlineInvestment = (CardView)findViewById(R.id.CardRoboOnlineInvestment);
        CardRoboOnlineInvestment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),RoboOnlineInvestment.class);
                startActivity(i);
                overridePendingTransition(R.animator.move_left,R.animator.move_right);
                finish();
            }
        });
    }


    @Override
    public boolean onSupportNavigateUp() {
        Intent i = new Intent(getApplicationContext(),Home.class);
        startActivity(i);
        overridePendingTransition(R.animator.left_right,R.animator.right_left);
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {

        Intent i = new Intent(getApplicationContext(),Home.class);
        startActivity(i);
        overridePendingTransition(R.animator.left_right,R.animator.right_left);
        finish();
    }
}
