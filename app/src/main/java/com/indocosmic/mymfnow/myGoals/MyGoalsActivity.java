package com.indocosmic.mymfnow.myGoals;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

import com.indocosmic.mymfnow.Home;
import com.indocosmic.mymfnow.R;

public class MyGoalsActivity extends AppCompatActivity implements View.OnClickListener{

    CardView create_goal_card,track_your_goal_card,online_investment_card;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_goals);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("My Goals DashBoard");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        init();

    }

    private void init() {

        create_goal_card = (CardView) findViewById(R.id.create_goal_card);
        track_your_goal_card = (CardView) findViewById(R.id.track_your_goal_card);
        online_investment_card = (CardView) findViewById(R.id.online_investment_card);

        create_goal_card.setOnClickListener(this);
        track_your_goal_card.setOnClickListener(this);
        online_investment_card.setOnClickListener(this);
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
    public void onClick(View view) {
        int id = view.getId();
        switch(id) {

            case R.id.create_goal_card:
                startActivity(new Intent(getApplicationContext(),MyGoalsItemActivity.class));
                overridePendingTransition(R.animator.move_left,R.animator.move_right);
                break;
        }
    }
}
