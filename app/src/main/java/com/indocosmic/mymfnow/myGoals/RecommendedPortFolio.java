package com.indocosmic.mymfnow.myGoals;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

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
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.indocosmic.mymfnow.R;
import com.indocosmic.mymfnow.utils.CommonMethods;
import com.indocosmic.mymfnow.utils.ConnectionDetector;
import com.indocosmic.mymfnow.webservices.RestClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RecommendedPortFolio extends AppCompatActivity {

    ProgressDialog myDialog;
    String str_todays_amount,str_current_age,str_target_amount,str_sip_amount,str_years,risk_profile;
    int debt,equity;
    TextView txt_todays_amount;
    PieChart pieChart;

    String current_age,target_amount,sip_amount,years,res_risk_profile,short_term_debt,long_term_debt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommended_port_folio);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Recommended PortFolio");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        init();

        APIrecommendedPortfolio();
    }



    private void init() {
        txt_todays_amount = (TextView) findViewById(R.id.txt_todays_amount);

        if (getIntent() != null) {

            Intent i = getIntent();

            str_current_age = i.getStringExtra("current_age");
            str_target_amount = i.getStringExtra("target_amount");
            str_sip_amount = i.getStringExtra("sip_amount");
            str_todays_amount = i.getStringExtra("todays_value");
            str_years = i.getStringExtra("years");
            risk_profile = i.getStringExtra("risk_profile");

            txt_todays_amount.setText("Recommended Portfolio (SIP Amount - Rs."+str_todays_amount+")");
            pieChart = (PieChart) findViewById(R.id.piechart);
            pieChart.setUsePercentValues(true);

        }
    }


    private void APIrecommendedPortfolio() {
        myDialog = new ProgressDialog(this);
        myDialog.setMessage("Please wait...");
        myDialog.setCancelable(false);
        myDialog.setCanceledOnTouchOutside(false);
        myDialog.show();

        String URL_Recomend_portfolio = RestClient.ROOT_URL + "/robo/getCategoryAllocationAndFunds";
        try {
            Log.d("URL",URL_Recomend_portfolio);

            ConnectionDetector cd = new ConnectionDetector(this);
            boolean isInternetPresent = cd.isConnectingToInternet();
            if (isInternetPresent) {

                StringRequest stringRequest = new StringRequest(Request.Method.POST,
                        URL_Recomend_portfolio,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                myDialog.dismiss();
                                Log.d("Response",response);
                                try {
                                    JSONObject jsonResponse = new JSONObject(response);

                                     current_age = jsonResponse.getString("current_age");
                                     target_amount = jsonResponse.getString("target_amount");
                                     sip_amount = jsonResponse.getString("sip_amount");
                                     years = jsonResponse.getString("years");
                                     res_risk_profile = jsonResponse.getString("risk_profile");
                                     short_term_debt = jsonResponse.getString("short_term_debt");
                                     long_term_debt = jsonResponse.getString("long_term_debt");
                                     debt = jsonResponse.getInt("debt");
                                     equity = jsonResponse.getInt("equity");

                                    drawPieChart();

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
                        params.put("current_age", str_current_age);
                        params.put("target_amount", str_target_amount);
                        params.put("sip_amount", str_sip_amount);
                        params.put("years", str_years);
                        params.put("risk_profile", risk_profile);
                        Log.d("ParrasRecomendGoal",params.toString() );
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

    private void drawPieChart() {

        ArrayList<Entry> yvalues = new ArrayList<Entry>();
        yvalues.add(new Entry(debt, 0));
        yvalues.add(new Entry(equity, 1));


        PieDataSet dataSet = new PieDataSet(yvalues, "");

        ArrayList<String> xVals = new ArrayList<String>();

        xVals.add("debt");
        xVals.add("equity");


        PieData data = new PieData(xVals, dataSet);
        data.setValueFormatter(new PercentFormatter());
        pieChart.setData(data);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setTransparentCircleRadius(25f);
        pieChart.setHoleRadius(25f);

        //dataSet.setColors(Color.pie_chart_blue);
        data.setValueTextSize(13f);
        data.setValueTextColor(Color.DKGRAY);

        pieChart.animateXY(1400, 1400);
    }


    @Override
    public boolean onSupportNavigateUp() {
        Intent i = new Intent(getApplicationContext(),CreateGoal.class);
        startActivity(i);
        overridePendingTransition(R.animator.left_right,R.animator.right_left);
        finish();
        return true;
    }

}
