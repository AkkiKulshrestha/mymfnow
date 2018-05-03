package com.indocosmic.mymfnow;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;

public class CalculatorDashboard extends AppCompatActivity {

    ImageView back_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator_dashboard);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Calculator DashBoard");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

}
