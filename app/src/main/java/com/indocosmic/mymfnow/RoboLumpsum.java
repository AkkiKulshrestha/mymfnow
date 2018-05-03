package com.indocosmic.mymfnow;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.indocosmic.mymfnow.utils.CommonMethods;
import com.indocosmic.mymfnow.utils.ConnectionDetector;
import com.indocosmic.mymfnow.utils.Constant;
import com.indocosmic.mymfnow.utils.MyValidator;
import com.indocosmic.mymfnow.utils.NumberTextWatcherForThousand;
import com.indocosmic.mymfnow.webservices.RestClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Akshay on 30-04-2018.
 */

public class RoboLumpsum extends AppCompatActivity{

    EditText EdtAge,EdtAmountLumpsum,EdtNo_Of_Years;
    Spinner Spn_RiskProfile;
    Button BtnBuildMyPlan;
    String Selected_RiskType,StrAmount;
    public String[] ArrayListRiskToleranceType= new String[] {"Select Category","Conservative","Moderately Conservative", "Moderate", "Moderately Aggressive" , "Aggressive"};

    CardView CardPlanCreated;
    ProgressDialog myDialog;
    JSONObject JSONObjectResponse;

    String AssetAllocationEquityPer,AssetAllocationDebtPer,AssetAllocationEquityAmt,AssetAllocationDebtAmt,OverViewEquityBalanced,OverViewEquityDiversified,OverViewEquityMidcap;

    String OverViewEquityLargecap,Projected_InvestedAmount,Projected_TimeHorizon,Projected_ExpectedReturnAmount,Projected_FinalMonthYear;
    String Projected_Minimum_Return_Per,Projected_Median_Return_Per,Projected_Maximum_Return_Per,Projected_Maximum_ReturnAmount,Projected_Minimum_Return_Amount;
    JSONArray projected_list,historical_list,scheme_list;
    Button BtnViewResult;
    WebView Wb_Plan;
    Dialog DialogResultRoboLumpsum;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_robo_lumsump);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("LUMPSUM ROBO");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        init();

    }

    private void init() {
        EdtAge= (EditText)findViewById(R.id.EdtAge);

        EdtAmountLumpsum= (EditText)findViewById(R.id.EdtAmountLumpsum);
        EdtAmountLumpsum.addTextChangedListener(new NumberTextWatcherForThousand(EdtAmountLumpsum));

        EdtNo_Of_Years= (EditText)findViewById(R.id.EdtNo_Of_Years);

         Spn_RiskProfile= (Spinner)findViewById(R.id.Spn_RiskProfile);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, ArrayListRiskToleranceType);

        // Setting the array adapter containing country list to the spinner widget
        Spn_RiskProfile.setAdapter(adapter);

        AdapterView.OnItemSelectedListener PeriodSelectedListener = new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> spinner, View container,
                                       int position, long id) {
                //  mTvCountry.setText(countries[position]);
                Selected_RiskType = Spn_RiskProfile.getSelectedItem().toString();
                Log.d("Selected_RiskType-->",""+Selected_RiskType);
                if(Selected_RiskType.equalsIgnoreCase("Moderately Conservative")){
                    Selected_RiskType = "Moderately_Conservative";

                }else if(Selected_RiskType.equalsIgnoreCase("Moderately Aggressive")){
                    Selected_RiskType = "Moderately_Aggressive";
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }

        };

        // Setting ItemClick Handler for Spinner Widget
        Spn_RiskProfile.setOnItemSelectedListener(PeriodSelectedListener);


         BtnBuildMyPlan= (Button)findViewById(R.id.BtnBuildMyPlan);
         BtnBuildMyPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateBuildPlan()){
                    StrAmount = NumberTextWatcherForThousand.trimCommaOfString(EdtAmountLumpsum.getText().toString());
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(EdtNo_Of_Years.getWindowToken(), 0);
                    ApiForBuildingPlan();
                }
            }
        });

        CardPlanCreated = (CardView)findViewById(R.id.CardPlanCreated);
        CardPlanCreated.setVisibility(View.GONE);

        BtnViewResult = (Button)findViewById(R.id.BtnViewResult);
        BtnViewResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowPlan();
            }
        });


    }

    private void ApiForBuildingPlan() {

        myDialog = new ProgressDialog(RoboLumpsum.this);
        myDialog.setMessage("Please wait...");
        myDialog.setCancelable(false);
        myDialog.setCanceledOnTouchOutside(false);
        myDialog.show();
        String URL_Robo_Lumpsum = RestClient.ROOT_URL + "/robo/getRoboAdvisor";
        try {
            Log.d("URL",URL_Robo_Lumpsum);

            ConnectionDetector cd = new ConnectionDetector(this);
            boolean isInternetPresent = cd.isConnectingToInternet();
            if (isInternetPresent) {

                StringRequest stringRequest = new StringRequest(Request.Method.POST,
                        URL_Robo_Lumpsum,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                myDialog.dismiss();
                                Log.d("Response",response);
                                try {
                                    JSONObjectResponse = new JSONObject(response);
                                     AssetAllocationEquityPer = JSONObjectResponse.getString("equity");
                                     AssetAllocationDebtPer = JSONObjectResponse.getString("debt");


                                     OverViewEquityBalanced = JSONObjectResponse.getString("equity_balanced");
                                     OverViewEquityDiversified = JSONObjectResponse.getString("equity_diversified");
                                     OverViewEquityMidcap = JSONObjectResponse.getString("equity_midcap");
                                     OverViewEquityLargecap = JSONObjectResponse.getString("equity_largecap");

                                     Projected_InvestedAmount = JSONObjectResponse.getString("invested_amount");
                                     Projected_TimeHorizon = JSONObjectResponse.getString("time_horizon");
                                     Projected_ExpectedReturnAmount = JSONObjectResponse.getString("projected_final_value");
                                     Projected_FinalMonthYear = JSONObjectResponse.getString("projected_final_month_year");
                                     Projected_Minimum_Return_Per = JSONObjectResponse.getString("minimum_return");
                                     Projected_Median_Return_Per = JSONObjectResponse.getString("median_return");
                                     Projected_Maximum_Return_Per = JSONObjectResponse.getString("maximum_return");
                                     projected_list = JSONObjectResponse.getJSONArray("projected_list");
                                    for (int k = 0;k<projected_list.length();k++){
                                        JSONObject jsonObject = projected_list.getJSONObject(k);
                                        if(k==projected_list.length()-1){
                                            Projected_Minimum_Return_Amount = jsonObject.getString("minimum_expected_amount");
                                            Projected_Maximum_ReturnAmount  = jsonObject.getString("maximum_expected_amount");
                                            Log.d("MinReturn",Projected_Minimum_Return_Amount);
                                            Log.d("MaxReturn",Projected_Maximum_ReturnAmount);
                                        }


                                    }


                                     historical_list = JSONObjectResponse.getJSONArray("historical_list");
                                     scheme_list = JSONObjectResponse.getJSONArray("scheme_list");

                                    Double asset_equity_amt = ((Double.valueOf(AssetAllocationEquityPer)/100)*100);
                                    AssetAllocationEquityAmt = String.format("%.1f", asset_equity_amt);

                                    Double asset_debt_amt = ((Double.valueOf(AssetAllocationDebtPer)/100)*100);
                                    AssetAllocationDebtAmt = String.format("%.1f", asset_debt_amt);
                                    Log.d("AssetAmtEquity",AssetAllocationEquityAmt);
                                    Log.d("AssetAmtDebt",AssetAllocationDebtAmt);
                                         /*   "projected_list":[{"invested_amount":1.0E8,"expected_amount":1.0E8,"minimum_expected_amount":1.0E8,"maximum_expected_amount":1.0E8,"year":"Year1"}
                                            "historical_list":[{"year":2008,"invested_amount":1.0E8,"growth_amount":1.0E8,"fd_amount":1.0E8}],
                                        "scheme_list":[{"scheme_name":"L\u0026T India Prudence Fund- Regular Plan - Growth","category":"Balanced Funds Equity Oriented","allocation_percentage":10.0,"allocation_amount":1.0E7},{"scheme_name":"Kotak Select Focus Fund - Growth","category":"Equity Funds Large Cap","allocation_percentage":10.0,"allocation_amount":1.0E7},{"scheme_name":"SBI Blue Chip Fund-Regular Plan Growth","category":"Equity Funds Large Cap","allocation_percentage":10.0,"allocation_amount":1.0E7},{"scheme_name":"Mirae Asset Emerging Bluechip Fund - Regular Plan - Growth Option","category":"Equity Funds Mid and Small Cap","allocation_percentage":20.0,"allocation_amount":2.0E7},{"scheme_name":"Reliance Small Cap Fund - Growth Plan - Growth Option","category":"Equity Funds Mid and Small Cap","allocation_percentage":20.0,"allocation_amount":2.0E7},{"scheme_name":"Aditya Birla Sun Life Advantage Fund - Regular Growth","category":"Equity Funds Diversified","allocation_percentage":15.0,"allocation_amount":1.5E7},{"scheme_name":"DSP BlackRock Opportunities Fund-Regular Plan - Growth","category":"Equity Funds Diversified","allocation_percentage":15.0,"allocation_amount":1.5E7}]}
*/
                                    ShowPlan();


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
                        params.put("current_age", EdtAge.getText().toString());
                        params.put("risk_profile", Selected_RiskType);
                        params.put("time_horizon", EdtNo_Of_Years.getText().toString());
                        params.put("invested_amount", StrAmount);
                        Log.d("ParrasRoboLumpsum",params.toString() );
                        return params;
                    }

                  /*  @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("username", etUname.getText().toString().trim());
                        params.put("password", etPass.getText().toString().trim());
                        return params;
                    }*/

                };



               /* Log.d("params",params.toString());
                JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, URL_Robo_Lumpsum,params,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d("Response",response.toString());
                                try {
                                    Boolean Status = response.getBoolean("Status");
                                    String Message = response.getString("Message");

                                    if(Status) {
                                        myDialog.dismiss();



                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                myDialog.dismiss();

                                //Toast.makeText(MainActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                            }
                        })*//*{

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("AccessKey",AccessKey);
                        headers.put("AccessId", UserId);
                        headers.put("DeviceType",DeviceType);
                        return headers;
                    }
                }*//* {

                    @Override
                    public String getBodyContentType() {
                        return "application/x-www-form-urlencoded; charset=UTF-8";
                    }
                };*/

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

    private void ShowPlan() {
        CardPlanCreated.setVisibility(View.VISIBLE);

        DialogResultRoboLumpsum = new Dialog(RoboLumpsum.this);
        DialogResultRoboLumpsum.requestWindowFeature(Window.FEATURE_NO_TITLE);
        DialogResultRoboLumpsum.setCanceledOnTouchOutside(true);
        DialogResultRoboLumpsum.setCancelable(true);
        DialogResultRoboLumpsum.setContentView(R.layout.layout_result_lumpsum_robo);
        DialogResultRoboLumpsum.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        TextView title = (TextView)DialogResultRoboLumpsum.findViewById(R.id.title) ;
        ImageView iv_close = (ImageView)DialogResultRoboLumpsum.findViewById(R.id.iv_close);

        Wb_Plan = (WebView)DialogResultRoboLumpsum.findViewById(R.id.Wb_Plan);

        Wb_Plan.addJavascriptInterface(new WebAppInterfaceMultiCardDataLumpSumRobo(), "Android");
        Wb_Plan.getSettings().setJavaScriptEnabled(true);
        // webView.loadUrl(getAssets().toString()sc_chart.html);
        Wb_Plan.loadUrl("file:///android_asset/www/new_lumpsum_robo_multi_card.html");


        title.setTypeface(Constant.OpenSansBold(getApplicationContext()));
        DialogResultRoboLumpsum.show();
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogResultRoboLumpsum.dismiss();

            }
        });

    }
        

    



    private class WebAppInterfaceMultiCardDataLumpSumRobo{

        /*,,;
        String ,,;
        JSONArray ,,scheme_list;*/

        @JavascriptInterface
        public String getAssetAllocationEquity() {
            return AssetAllocationEquityAmt;

        }

        @JavascriptInterface
        public String getAssetAllocationDebt() {
            return AssetAllocationDebtAmt;

        }

        @JavascriptInterface
        public String getOverViewEquityBalanced() {
            return OverViewEquityBalanced;

        }

        @JavascriptInterface
        public String getOverViewEquityDiversified() {
            return OverViewEquityDiversified;

        }

        @JavascriptInterface
        public String getOverViewEquityMidcap() {
            return OverViewEquityMidcap;

        }

        @JavascriptInterface
        public String getOverViewEquityLargecap() {
            return OverViewEquityLargecap;

        }

        @JavascriptInterface
        public String getProjected_InvestedAmount() {
            return Projected_InvestedAmount;

        }
        @JavascriptInterface
        public String getProjected_TimeHorizon() {
            return Projected_TimeHorizon;

        }
        @JavascriptInterface
        public String getProjected_ExpectedReturnAmount() {
            return Projected_ExpectedReturnAmount;

        }
        @JavascriptInterface
        public String getProjected_FinalMonthYear() {
            return Projected_FinalMonthYear;

        }
        @JavascriptInterface
        public String getProjected_Minimum_Return_Per() {
            return Projected_Minimum_Return_Per;

        }
        @JavascriptInterface
        public String getProjected_Median_Return_Per() {
            return Projected_Median_Return_Per;

        }
        @JavascriptInterface
        public String getProjected_Maximum_Return() {
            return Projected_Maximum_Return_Per;

        }

        @JavascriptInterface
        public String getProjected_Minimum_ReturnAmount() {
            return Projected_Minimum_Return_Amount;

        }

        @JavascriptInterface
        public String getProjected_Maximum_ReturnAmount() {
            return Projected_Maximum_ReturnAmount;

        }
        @JavascriptInterface
        public String getProjectedList() {
            return projected_list.toString();

        }
        @JavascriptInterface
        public String getHistoricalList() {
            return historical_list.toString();

        }


    }

        private boolean validateBuildPlan() {
        boolean result = true;

        if (!MyValidator.isValidField(EdtAge)) {
            EdtAge.requestFocus();
            result = false;
        }

        if (!MyValidator.isValidSpinner(Spn_RiskProfile)) {
            result = false;
        }

        if(!EdtAge.getText().toString().equalsIgnoreCase("")){
            String AgeSpouse = EdtAge.getText().toString();
            if(AgeSpouse!=null && Integer.valueOf(AgeSpouse)==0){
                EdtAge.setError("Age cannot be zero.");
                EdtAge.requestFocus();
                result = false;
            }

            if(AgeSpouse!=null && Integer.valueOf(AgeSpouse)<18){
                EdtAge.setError("Investments in mutual funds not allowed below 18 years age unless via a parent or guardian.");
                EdtAge.requestFocus();
                result = false;
            }

        }

        if (!MyValidator.isValidField(EdtAmountLumpsum)) {
            EdtAmountLumpsum.requestFocus();
            result = false;
        }

        if(!EdtAmountLumpsum.getText().toString().equalsIgnoreCase("")){
            String StrAmount = NumberTextWatcherForThousand.trimCommaOfString(EdtAmountLumpsum.getText().toString());

            if(StrAmount!=null && Integer.valueOf(StrAmount)==0){
                EdtAmountLumpsum.setError("Amount cannot be zero.");
                EdtAmountLumpsum.requestFocus();
                result = false;
            }

            if(StrAmount!=null && !StrAmount.equalsIgnoreCase("")){
                Double Amt = Double.valueOf(StrAmount);
                double remainder = Amt % 100;
                if(remainder!=0){
                    EdtAmountLumpsum.setError("Please enter the amount multiple of 100.");
                    EdtAmountLumpsum.requestFocus();
                    result = false;
                }
            }

        }

        if (!MyValidator.isValidField(EdtNo_Of_Years)) {
            EdtNo_Of_Years.requestFocus();
            result = false;
        }

        return result;

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
