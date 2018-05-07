package com.indocosmic.mymfnow.myGoals;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.indocosmic.mymfnow.Home;
import com.indocosmic.mymfnow.R;
import com.indocosmic.mymfnow.adapters.MyGoalsItemAdapter;
import com.indocosmic.mymfnow.models.MyGoalItemsModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class MyGoalsItemActivity extends AppCompatActivity {

    RecyclerView recycler_item;
    GridLayoutManager gridLayoutManager;
    MyGoalsItemAdapter myGoalsItemAdapter;
    List<MyGoalItemsModel> myGoalItemsModelArrayList= new ArrayList<>();;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_goals_item);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Create your goal");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        init();
    }

    private void init() {
        recycler_item = (RecyclerView)findViewById(R.id.recycler_item);

        myGoalsItemAdapter = new MyGoalsItemAdapter(myGoalItemsModelArrayList,this);
        gridLayoutManager = new GridLayoutManager(getApplicationContext(),2);
        recycler_item.setLayoutManager(gridLayoutManager);
        recycler_item.setAdapter(myGoalsItemAdapter);


        prepareMovieData();
     }

    private void prepareMovieData() {

        MyGoalItemsModel goalItemsModel = new MyGoalItemsModel(R.drawable.wealth_creation, "Wealth Creation");
        myGoalItemsModelArrayList.add(goalItemsModel);

        goalItemsModel = new MyGoalItemsModel(R.drawable.retirement, "Retirement");
        myGoalItemsModelArrayList.add(goalItemsModel);

        goalItemsModel = new MyGoalItemsModel(R.drawable.education, "Education");
        myGoalItemsModelArrayList.add(goalItemsModel);

        goalItemsModel = new MyGoalItemsModel(R.drawable.marriage, "Marriage");
        myGoalItemsModelArrayList.add(goalItemsModel);

        goalItemsModel = new MyGoalItemsModel(R.drawable.buying_house, "Buying House");
        myGoalItemsModelArrayList.add(goalItemsModel);

        goalItemsModel = new MyGoalItemsModel(R.drawable.bussiness, "Bussiness");
        myGoalItemsModelArrayList.add(goalItemsModel);

        goalItemsModel = new MyGoalItemsModel(R.drawable.supporting_parents, "Supporting Parents");
        myGoalItemsModelArrayList.add(goalItemsModel);

        goalItemsModel = new MyGoalItemsModel(R.drawable.other_goals, "Other Goals");
        myGoalItemsModelArrayList.add(goalItemsModel);

   }

    @Override
    public boolean onSupportNavigateUp() {
        Intent i = new Intent(getApplicationContext(),MyGoalsActivity.class);
        startActivity(i);
        overridePendingTransition(R.animator.left_right,R.animator.right_left);
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(),MyGoalsActivity.class);
        startActivity(i);
        overridePendingTransition(R.animator.left_right,R.animator.right_left);
        finish();
    }
}
