package com.indocosmic.mymfnow.robo_planning;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.indocosmic.mymfnow.R;

/**
 * Created by Akshay on 04-05-2018.
 */

public class RoboSavedPlans extends AppCompatActivity {

    LinearLayout ll_parent_saved_plans;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_robo_saved_plans);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("SAVED PLANS");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        init();

    }

    private void init() {

        ll_parent_saved_plans = (LinearLayout)findViewById(R.id.ll_parent_saved_plans);
        ll_parent_saved_plans.setVisibility(View.GONE);

    }



    @Override
    public boolean onSupportNavigateUp() {

        Intent i = new Intent(getApplicationContext(),RoboDashboard.class);
        startActivity(i);
        overridePendingTransition(R.animator.left_right,R.animator.right_left);
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {

        Intent i = new Intent(getApplicationContext(),RoboDashboard.class);
        startActivity(i);
        overridePendingTransition(R.animator.left_right,R.animator.right_left);
        finish();
    }
}
