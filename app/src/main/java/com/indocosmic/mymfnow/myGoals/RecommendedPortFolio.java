package com.indocosmic.mymfnow.myGoals;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.indocosmic.mymfnow.R;
import com.indocosmic.mymfnow.utils.CommonMethods;
import com.indocosmic.mymfnow.utils.ConnectionDetector;
import com.indocosmic.mymfnow.webservices.RestClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RecommendedPortFolio extends AppCompatActivity {

    ProgressDialog myDialog;
    String str_todays_amount,str_current_age,str_target_amount,str_sip_amount,str_years,risk_profile,allocation_amount,allocation_percentage;
    String scheme_name,category;
    String current_age,target_amount,sip_amount,years,res_risk_profile,short_term_debt,long_term_debt;
    int debt,equity;
    TextView txt_todays_amount;
    PieChart pieChart;
    LinearLayout ll_parent_recomendportfolio;
    int no_recomended_portfolio;
    Double int_allocation_percentage=0.0,int_allocation_amount=0.0;

    EditText edt_total_allocation_per,edt_total_allocation_amnt;

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
        ll_parent_recomendportfolio = (LinearLayout) findViewById(R.id.ll_parent_recomendportfolio);
        edt_total_allocation_per = (EditText)findViewById(R.id.edt_total_allocation_per);
        edt_total_allocation_amnt = (EditText)findViewById(R.id.edt_total_allocation_amnt);

        if (getIntent() != null) {

            Intent i = getIntent();

            str_current_age = i.getStringExtra("current_age");
            str_target_amount = i.getStringExtra("target_amount");
            str_sip_amount = i.getStringExtra("sip_amount");
            str_todays_amount = i.getStringExtra("todays_value");
            str_years = i.getStringExtra("years");
            risk_profile = i.getStringExtra("risk_profile");
            String til_text = "Recommended Portfolio \n(SIP Amount - Rs."+CommonMethods.NumberDisplayFormattingWithComma(str_sip_amount)+")";
            txt_todays_amount.setText(til_text);
            pieChart = (PieChart) findViewById(R.id.piechart);
            pieChart.setUsePercentValues(true);

        }
    }


    private void APIrecommendedPortfolio() {
        ll_parent_recomendportfolio.removeAllViews();
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


                                    JSONArray recommendedListArray = jsonResponse.getJSONArray("list");

                                    for (int i=0;i<recommendedListArray.length();i++) {

                                        JSONObject recommendedListObject = recommendedListArray.getJSONObject(i);


                                         scheme_name = recommendedListObject.getString("scheme_name");
                                         category = recommendedListObject.getString("category");
                                         allocation_percentage = recommendedListObject.getString("allocation_percentage");
                                         allocation_amount = recommendedListObject.getString("allocation_amount");

                                        //row_layout.setPadding(10,10,10,10);
                                        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                        final View rowView = inflater.inflate(R.layout.scheme_list_row, null);

                                        TextView row_scheme_name = (TextView)rowView.findViewById(R.id.row_scheme_name);
                                        row_scheme_name.setText(scheme_name);

                                        final TextView row_scheme_category = (TextView)rowView.findViewById(R.id.row_scheme_category);
                                        row_scheme_category.setText(category);

                                        final EditText row_edt_allocation_per = (EditText) rowView.findViewById(R.id.row_edt_allocation_per);
                                        row_edt_allocation_per.setText(allocation_percentage);


                                        final EditText row_edt_allocation_amount = (EditText) rowView.findViewById(R.id.row_edt_allocation_amount);
                                        row_edt_allocation_amount.setText(allocation_amount);

                                        final TextView row_changeScheme = (TextView)rowView.findViewById(R.id.row_changeScheme);

                                        row_changeScheme.setHint(String.valueOf(no_recomended_portfolio));


                                        final CheckBox row_chk_save = (CheckBox) rowView.findViewById(R.id.row_chk_save);


                                        int_allocation_percentage = int_allocation_percentage+Double.valueOf(allocation_percentage);
                                        int_allocation_amount = int_allocation_amount+Double.valueOf(allocation_amount);

                                        row_changeScheme.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                CommonMethods.DisplayToast(getApplicationContext(),row_changeScheme.getHint().toString());
                                                Log.d("Rows Category", row_scheme_category.getText().toString());
                                            }
                                        });

                                        ll_parent_recomendportfolio.addView(rowView);
                                        no_recomended_portfolio++;

                                        edt_total_allocation_per.setText(""+int_allocation_percentage);
                                        edt_total_allocation_amnt.setText(""+int_allocation_amount);
                                    }

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
        yvalues.add(new Entry(equity, 0));
        yvalues.add(new Entry(debt, 1));


        PieDataSet dataSet = new PieDataSet(yvalues, "");
        dataSet.setSliceSpace(2);
        dataSet.setValueTextSize(12);
        ArrayList<String> xVals = new ArrayList<String>();
        xVals.add("Equity");
        xVals.add("Debt");


        PieData data = new PieData(xVals, dataSet);
        data.setValueFormatter(new PercentFormatter());
        pieChart.setData(data);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setTransparentCircleRadius(30f);
        pieChart.setHoleRadius(30f);
        pieChart.invalidate();
        pieChart.setDescription(null);
        pieChart.setRotationEnabled(false);

        //dataSet.setColors(Color.pie_chart_blue);
        data.setValueTextSize(13f);
        data.setValueTextColor(Color.WHITE);

        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(getResources().getColor(R.color.graph_color_blue));
        colors.add(getResources().getColor(R.color.red_close));

        dataSet.setColors(colors);

        Legend legend = pieChart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
        legend.setCustom(colors,xVals);


        //pieChart.animateXY(1400, 1400);
    }


    @Override
    public boolean onSupportNavigateUp() {
        Intent i = new Intent(getApplicationContext(),CreateGoal.class);
        startActivity(i);
        overridePendingTransition(R.animator.left_right,R.animator.right_left);
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(),CreateGoal.class);
        startActivity(i);
        overridePendingTransition(R.animator.left_right,R.animator.right_left);
    }
}
