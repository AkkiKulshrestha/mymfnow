package com.indocosmic.mymfnow.myGoals;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.indocosmic.mymfnow.Home;
import com.indocosmic.mymfnow.R;
import com.indocosmic.mymfnow.adapters.MyGoalsItemAdapter;
import com.indocosmic.mymfnow.robo_planning.RoboLumpsum;
import com.indocosmic.mymfnow.utils.CommonMethods;
import com.indocosmic.mymfnow.utils.ConnectionDetector;
import com.indocosmic.mymfnow.utils.MyValidator;
import com.indocosmic.mymfnow.webservices.RestClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CreateGoal extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemSelectedListener{

    TextView txt_mygoal_name;
    String str_mygoal_name,selected_RiskType,todays_value,target_amount,years,sip_amount;

    TextInputEditText goal_name,goal_age,goal_amount_to_consider,years_to_save_goal,inflation_percent;

    TextInputEditText money_required_today,years_after_amount_return;

    EditText txt_todays_amount,txt_inflation_amount,txt_years_to_save,txt_mothely_sip;
    Spinner spn_risk_profile;
    Button btnCreateGoal,btnAchieveGoal;
    ProgressDialog myDialog;

    public String[] ArrayListRiskToleranceType= new String[] {"Select","Conservative","Moderately Conservative", "Moderate", "Moderately Aggressive" , "Aggressive"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_goal);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Create Goal");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        init();
    }

    private void init() {
        txt_mygoal_name =(TextView)findViewById(R.id.txt_mygoal_name);

        if (getIntent() != null) {
            Intent i = getIntent();
            str_mygoal_name = i.getStringExtra("mygoal_name");
            txt_mygoal_name.setText(str_mygoal_name);
        }

        goal_name = (TextInputEditText) findViewById(R.id.goal_name);
        goal_age = (TextInputEditText) findViewById(R.id.goal_age);
        goal_amount_to_consider = (TextInputEditText) findViewById(R.id.goal_amount_to_consider);
        years_to_save_goal = (TextInputEditText) findViewById(R.id.years_to_save_goal);
        inflation_percent = (TextInputEditText) findViewById(R.id.inflation_percent);
        btnCreateGoal = (Button) findViewById(R.id.btnCreateGoal);
        btnAchieveGoal = (Button) findViewById(R.id.btnAchieveGoal);

        money_required_today = (TextInputEditText) findViewById(R.id.money_required_today);
        years_after_amount_return = (TextInputEditText) findViewById(R.id.years_after_amount_return);

        txt_todays_amount = (EditText) findViewById(R.id.txt_todays_amount);
        txt_inflation_amount = (EditText) findViewById(R.id.txt_inflation_amount);
        txt_years_to_save = (EditText) findViewById(R.id.txt_years_to_save);
        txt_mothely_sip = (EditText) findViewById(R.id.txt_mothely_sip);

        spn_risk_profile = (Spinner) findViewById(R.id.spn_risk_profile);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, ArrayListRiskToleranceType);
        spn_risk_profile.setAdapter(adapter);
        btnCreateGoal.setOnClickListener(this);
        btnAchieveGoal.setOnClickListener(this);
        spn_risk_profile.setOnItemSelectedListener(this);


        if (str_mygoal_name.equalsIgnoreCase("Wealth Creation")) {
            goal_amount_to_consider.setVisibility(View.VISIBLE);
            years_to_save_goal.setVisibility(View.VISIBLE);
        }else {
            money_required_today.setVisibility(View.VISIBLE);
            years_after_amount_return.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btnCreateGoal) {
            if(validateBuildPlan()) {
                ApiForFetchingGoals();
              }
        } else if (id == R.id.btnAchieveGoal) {

            Intent i = new Intent(getApplicationContext(),RecommendedPortFolio.class);
            i.putExtra("current_age",goal_age.getText().toString().trim());
            i.putExtra("target_amount",target_amount);
            i.putExtra("sip_amount",sip_amount);
            i.putExtra("todays_value",todays_value);
            i.putExtra("years",years);
            i.putExtra("risk_profile",selected_RiskType);

            startActivity(i);
            overridePendingTransition(R.animator.move_left,R.animator.move_right);
           }
        }

    private void ApiForFetchingGoals() {
        myDialog = new ProgressDialog(this);
        myDialog.setMessage("Please wait...");
        myDialog.setCancelable(false);
        myDialog.setCanceledOnTouchOutside(false);
        myDialog.show();

        String URL_Create_Goal = RestClient.ROOT_URL + "/robo/getSIPAmount";
        try {
            Log.d("URL",URL_Create_Goal);

            ConnectionDetector cd = new ConnectionDetector(this);
            boolean isInternetPresent = cd.isConnectingToInternet();
            if (isInternetPresent) {

                StringRequest stringRequest = new StringRequest(Request.Method.POST,
                        URL_Create_Goal,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                myDialog.dismiss();
                                Log.d("Response",response);
                                try {
                                   JSONObject jsonResponse = new JSONObject(response);

                                   todays_value = jsonResponse.getString("todays_value");
                                   years = jsonResponse.getString("years");
                                   String sip_amount_original = jsonResponse.getString("sip_amount_original");
                                   sip_amount = jsonResponse.getString("sip_amount");
                                   target_amount = jsonResponse.getString("target_amount");

                                    txt_todays_amount.setText(todays_value);
                                    txt_inflation_amount.setText(target_amount);
                                    txt_years_to_save.setText(years);
                                    txt_mothely_sip.setText(sip_amount);

                                    btnAchieveGoal.setVisibility(View.VISIBLE);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        myDialog.dismiss();
                        VolleyLog.d("volley", "Error: " + error.getMessage());
                        error.printStackTrace();

                    }
                }) {

                    @Override
                    public String getBodyContentType() {
                        return "application/x-www-form-urlencoded; charset=UTF-8";
                    }
                    @Override
                    public Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("goal_name", goal_name.getText().toString());
                        params.put("current_age", goal_age.getText().toString());
                        params.put("todays_value", goal_amount_to_consider.getText().toString());
                        params.put("years", years_to_save_goal.getText().toString());
                        params.put("inflation_rate", inflation_percent.getText().toString());
                        params.put("risk_profile", selected_RiskType);
                        Log.d("ParrasCreateGoal",params.toString() );
                        return params;
                    }

                };


                int socketTimeout = 50000;//30 seconds - change to what you want
                RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                stringRequest.setRetryPolicy(policy);
                RequestQueue requestQueue = Volley.newRequestQueue(this);
                requestQueue.add(stringRequest);



            } else {
                CommonMethods.DisplayToast(this, "Please check your internet connection");
            }
        } catch (Exception e) {

            e.printStackTrace();

        }
    }

    private boolean validateBuildPlan() {
        boolean result = true;


        if (!MyValidator.isValidField(goal_name)) {
            goal_name.requestFocus();
            result = false;
        }

        if (!MyValidator.isValidSpinner(spn_risk_profile)) {
            result = false;
        }

        if(!goal_age.getText().toString().equalsIgnoreCase("")){
            String Strgoal_age = goal_age.getText().toString();
            if(Strgoal_age!=null && Integer.valueOf(Strgoal_age)==0){
                goal_age.setError("Age cannot be zero.");
                goal_age.requestFocus();
                result = false;
            }

        }

        if(!goal_amount_to_consider.getText().toString().equalsIgnoreCase("")){
            String Strgoal_amount_to_consider = goal_amount_to_consider.getText().toString();
            if(Strgoal_amount_to_consider!=null && Integer.valueOf(Strgoal_amount_to_consider)==0){
                goal_amount_to_consider.setError("Enter Proper Investment Amount");
                goal_amount_to_consider.requestFocus();
                result = false;
            }

        }

        if(!years_to_save_goal.getText().toString().equalsIgnoreCase("")){
            String Stryears_to_save_goal = years_to_save_goal.getText().toString();
            if(Stryears_to_save_goal!=null && Integer.valueOf(Stryears_to_save_goal)==0){
                years_to_save_goal.setError("Enter Proper Amount");
                years_to_save_goal.requestFocus();
                result = false;
            }

        }


        if(!inflation_percent.getText().toString().equalsIgnoreCase("")){
            String Strinflation_percent = inflation_percent.getText().toString();
            if(Strinflation_percent!=null && Integer.valueOf(Strinflation_percent)==0){
                inflation_percent.setError("Enter Proper inflation");
                inflation_percent.requestFocus();
                result = false;
            }

        }

        return result;
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        selected_RiskType = spn_risk_profile.getSelectedItem().toString();
        Log.d("Selected_RiskType-->",""+selected_RiskType);
        if(selected_RiskType.equalsIgnoreCase("Moderately Conservative")){
            selected_RiskType = "Moderately_Conservative";

        }else if(selected_RiskType.equalsIgnoreCase("Moderately Aggressive")){
            selected_RiskType = "Moderately_Aggressive";
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent i = new Intent(getApplicationContext(),MyGoalsItemActivity.class);
        startActivity(i);
        overridePendingTransition(R.animator.left_right,R.animator.right_left);
        finish();
        return true;
    }

}
