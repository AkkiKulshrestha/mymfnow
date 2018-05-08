package com.indocosmic.mymfnow.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.indocosmic.mymfnow.R;
import com.indocosmic.mymfnow.models.MyGoalItemsModel;
import com.indocosmic.mymfnow.myGoals.CreateGoal;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Ind3 on 04-05-18.
 */

public class MyGoalsItemAdapter extends RecyclerView.Adapter<MyGoalsItemAdapter.MyGoalsItemViewHolder> {

    private List<MyGoalItemsModel> myGoalItemsModelList;
    private Context context;


    public MyGoalsItemAdapter(List<MyGoalItemsModel> myGoalItemsModelList, Context context) {
        this.myGoalItemsModelList = myGoalItemsModelList;
        this.context = context;
    }



    @NonNull
    @Override
    public MyGoalsItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mygoals_menu, parent, false);

        return new MyGoalsItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyGoalsItemViewHolder holder, int position) {

       final MyGoalItemsModel myGoalItemsModel = myGoalItemsModelList.get(position);

         Picasso.with(context).load(myGoalItemsModel.getImages()).into(holder.img_mygoalicon);
         holder.txt_mygoalicon_name.setText(myGoalItemsModel.getName());

        //Toast.makeText(context, ""+myGoalItemsModel.getName(), Toast.LENGTH_SHORT).show();

         holder.goalpaln_card.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Intent i = new Intent(context, CreateGoal.class);
                 i.putExtra("mygoal_name",myGoalItemsModel.getName());
                 context.startActivity(i);
                 ((Activity)  context).overridePendingTransition(R.animator.move_left,R.animator.move_right);
             }
         });

    }

    @Override
    public int getItemCount() {
        return myGoalItemsModelList.size();
    }


    public class MyGoalsItemViewHolder extends RecyclerView.ViewHolder {

         TextView txt_mygoalicon_name;
         ImageView img_mygoalicon;
         CardView goalpaln_card;

        public MyGoalsItemViewHolder(View view) {
            super(view);
            img_mygoalicon = (ImageView) view.findViewById(R.id.img_mygoalicon);
            txt_mygoalicon_name = (TextView) view.findViewById(R.id.txt_mygoalicon_name);
            goalpaln_card = (CardView) view.findViewById(R.id.goalpaln_card);

        }
    }

}