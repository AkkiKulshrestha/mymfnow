package com.indocosmic.mymfnow.robo_planning;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.indocosmic.mymfnow.R;
import com.indocosmic.mymfnow.utils.CommonMethods;
import com.indocosmic.mymfnow.utils.ConnectionDetector;
import com.indocosmic.mymfnow.utils.Constant;
import com.indocosmic.mymfnow.utils.MyValidator;
import com.indocosmic.mymfnow.utils.NumberTextWatcherForThousand;
import com.indocosmic.mymfnow.webservices.RestClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Akshay on 04-05-2018.
 */

public class RoboSIP extends AppCompatActivity {

    EditText EdtAge,EdtAmountLumpsum,EdtNo_Of_Years;
    Spinner Spn_RiskProfile;
    Button BtnBuildMyPlan;
    String Selected_RiskType,StrAmount;
    public String[] ArrayListRiskToleranceType= new String[] {"Select Category","Conservative","Moderately Conservative", "Moderate", "Moderately Aggressive" , "Aggressive"};

    LinearLayout CardPlanCreated;
    ProgressDialog myDialog;
    JSONObject JSONObjectResponse;

    String AssetAllocationEquityPer,AssetAllocationDebtPer,AssetAllocationEquityAmt,AssetAllocationDebtAmt,OverViewEquityBalanced,OverViewEquityDiversified,OverViewEquityMidcap;

    String OverViewEquityLargecap,Projected_SIPAmount,Projected_TimeHorizon,Projected_ExpectedReturnAmount,Projected_FinalMonthYear;
    String Projected_Minimum_Return_Per,Projected_Median_Return_Per,Projected_Maximum_Return_Per,Projected_TotalAmount,Projected_Invested_Amount;
    JSONArray projected_list,historical_list,scheme_list;
    Button BtnViewResult;
    WebView Wb_Plan;
    Dialog DialogResultRoboLumpsum,DialogSimilarPlanChangeScheme;
    ArrayList scheme_list_array;
    LinearLayout ll_parent_portfolio;
    int object_no_portfolio=1;
    View rowView;
    JSONArray  list_similar_schemes;
    LinearLayout ll_parent_change_schemas_plans;
    ArrayList SimilarSchemasList;
    int dynamic_row_pos = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_robo_sip);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("SIP ROBO");
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

        CardPlanCreated = (LinearLayout)findViewById(R.id.CardPlanCreated);
        CardPlanCreated.setVisibility(View.GONE);

        BtnViewResult = (Button)findViewById(R.id.BtnViewResult);
        BtnViewResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowPlan();
            }
        });


        ll_parent_portfolio = (LinearLayout)findViewById(R.id.ll_parent_portfolio);

    }

    private void ApiForBuildingPlan() {

        myDialog = new ProgressDialog(RoboSIP.this);
        myDialog.setMessage("Please wait...");
        myDialog.setCancelable(false);
        myDialog.setCanceledOnTouchOutside(false);
        myDialog.show();
        String URL_Robo_Lumpsum = RestClient.ROOT_URL + "/robo/getSIPRoboAdvisor";
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

                                    Projected_SIPAmount = JSONObjectResponse.getString("sip_amount");
                                    Projected_TimeHorizon = JSONObjectResponse.getString("time_horizon");
                                    Projected_ExpectedReturnAmount = JSONObjectResponse.getString("projected_final_growth_amount");
                                    Projected_FinalMonthYear = JSONObjectResponse.getString("projected_final_month_year");
                                    Projected_Minimum_Return_Per = JSONObjectResponse.getString("minimum_return");
                                    Projected_Median_Return_Per = JSONObjectResponse.getString("median_return");
                                    Projected_Maximum_Return_Per = JSONObjectResponse.getString("maximum_return");
                                    projected_list = JSONObjectResponse.getJSONArray("projected_list");
                                    for (int k = 0;k<projected_list.length();k++){
                                        JSONObject jsonObject = projected_list.getJSONObject(k);
                                        if(k==projected_list.length()-1){
                                            Projected_Invested_Amount = jsonObject.getString("invested_amount");
                                            Projected_TotalAmount  = jsonObject.getString("total_amount");
                                            Log.d("MinReturn",Projected_Invested_Amount);
                                            Log.d("MaxReturn",Projected_TotalAmount);
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
                        params.put("sip_amount", StrAmount);
                        Log.d("ParrasRoboSip",params.toString() );

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

        DialogResultRoboLumpsum = new Dialog(RoboSIP.this);
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
        Wb_Plan.loadUrl("file:///android_asset/www/new_sip_robo_multi_card.html");


        title.setTypeface(Constant.OpenSansBold(getApplicationContext()));
        DialogResultRoboLumpsum.show();
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogResultRoboLumpsum.dismiss();

            }
        });


        if(scheme_list!=null){
            try {
                scheme_list_array = new ArrayList();
                ll_parent_portfolio.removeAllViews();
                object_no_portfolio = 1;
                if(scheme_list.length()==0){
                    TextView TV_No_list = new TextView(this);
                    TV_No_list.setText("No Record to Display.");
                    ll_parent_portfolio.addView(TV_No_list);
                }else {

                    for (int i = 0; i < scheme_list.length(); i++) {
                        JSONObject jsonObject = scheme_list.getJSONObject(i);

                        String scheme_name = jsonObject.getString("scheme_name");
                        String category = jsonObject.getString("category");
                        String allocation_percentage = jsonObject.getString("allocation_percentage");
                        String allocation_amount = jsonObject.getString("allocation_amount");

                        //row_layout.setPadding(10,10,10,10);
                        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        final View rowView = inflater.inflate(R.layout.scheme_list_row, null);


                        TextView row_scheme_name = (TextView)rowView.findViewById(R.id.row_scheme_name);
                        row_scheme_name.setText(scheme_name);

                        final TextView row_changeScheme = (TextView)rowView.findViewById(R.id.row_changeScheme);
                        row_changeScheme.setHint(String.valueOf(object_no_portfolio));



                        final TextView row_scheme_category = (TextView)rowView.findViewById(R.id.row_scheme_category);
                        row_scheme_category.setText(category);

                        final EditText row_edt_allocation_per = (EditText) rowView.findViewById(R.id.row_edt_allocation_per);
                        row_edt_allocation_per.setText(allocation_percentage);

                        final EditText row_edt_allocation_amount = (EditText) rowView.findViewById(R.id.row_edt_allocation_amount);
                        row_edt_allocation_amount.setText(allocation_amount);


                        final CheckBox row_chk_save = (CheckBox) rowView.findViewById(R.id.row_chk_save);

                        row_changeScheme.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //CommonMethods.DisplayToast(getApplicationContext(),row_changeScheme.getHint().toString());
                                Log.d("Rows Category", row_scheme_category.getText().toString());
                                CallApiToGetSimilarSchemes(row_changeScheme.getHint().toString(),row_scheme_category.getText().toString());
                            }
                        });

                        ll_parent_portfolio.addView(rowView);

                        object_no_portfolio++;




                    }

                    //parent_layout_loan.addView(row_layout);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }



    }

    private void CallApiToGetSimilarSchemes(final String sequence_no, final String CategoryToSearch) {
        Log.d("sequence_no",sequence_no);
        Log.d("CategoryToSearch",CategoryToSearch);

        final String CategoryUriFormatted = CommonMethods.UrlFormatString(CategoryToSearch);

        String URL_Robo_Lumpsum = RestClient.ROOT_URL + "/robo/getRoboSchemes?category="+CategoryUriFormatted;
        try {
            Log.d("URL",URL_Robo_Lumpsum);

            ConnectionDetector cd = new ConnectionDetector(this);
            boolean isInternetPresent = cd.isConnectingToInternet();
            if (isInternetPresent) {

                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_Robo_Lumpsum,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("Response",response);
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                     list_similar_schemes = jsonObject.getJSONArray("list");
                                    Log.d("JsonArray",list_similar_schemes.toString());
                                    ShowPopupSimilarSchemas(sequence_no);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

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
                        params.put("category", CategoryUriFormatted);
                        Log.d("ParrasRoboSip",params.toString() );
                        return params;
                    }

                    /*@Override
                      protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("username", etUname.getText().toString().trim());
                        params.put("password", etPass.getText().toString().trim());
                        return params;
                    }*/

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

    @SuppressWarnings("ResourceType")
    private void ShowPopupSimilarSchemas(String sequence_no) {

        DialogSimilarPlanChangeScheme = new Dialog(RoboSIP.this);
        DialogSimilarPlanChangeScheme.requestWindowFeature(Window.FEATURE_NO_TITLE);
        DialogSimilarPlanChangeScheme.setCanceledOnTouchOutside(true);
        DialogSimilarPlanChangeScheme.setCancelable(true);
        DialogSimilarPlanChangeScheme.setContentView(R.layout.popup_change_scheme_robo);
        DialogSimilarPlanChangeScheme.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        TextView title = (TextView)DialogSimilarPlanChangeScheme.findViewById(R.id.title) ;
        ImageView iv_close = (ImageView)DialogSimilarPlanChangeScheme.findViewById(R.id.iv_close);
        LinearLayout ll_headers = (LinearLayout)DialogSimilarPlanChangeScheme.findViewById(R.id.ll_headers);
        ll_parent_change_schemas_plans = (LinearLayout)DialogSimilarPlanChangeScheme.findViewById(R.id.ll_parent_change_schemas_plans);
        title.setTypeface(Constant.OpenSansBold(getApplicationContext()));
        DialogSimilarPlanChangeScheme.show();
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogSimilarPlanChangeScheme.dismiss();

            }
        });

        Button Btn_close = (Button)DialogSimilarPlanChangeScheme.findViewById(R.id.Btn_close);
        Btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogSimilarPlanChangeScheme.dismiss();

            }
        });

        Button Btn_submit = (Button)DialogSimilarPlanChangeScheme.findViewById(R.id.Btn_submit);

        if(scheme_list!=null){
            try {
                SimilarSchemasList = new ArrayList();
                ll_parent_change_schemas_plans.removeAllViews();

                if(list_similar_schemes.length()==0){
                    ll_headers.setVisibility(View.GONE);
                    TextView TV_No_list = new TextView(this);
                    TV_No_list.setText("No Record to Display.");
                    TV_No_list.setPadding(10,10,10,10);
                    ll_parent_change_schemas_plans.addView(TV_No_list);
                }else {
                    ll_headers.setVisibility(View.VISIBLE);
                    for (int i = 0; i < list_similar_schemes.length(); i++) {
                        JSONObject jsonObject = list_similar_schemes.getJSONObject(i);

                        String scheme_name = jsonObject.getString("scheme_name");
                        String category = jsonObject.getString("category");

                        //row_layout.setPadding(10,10,10,10);
                        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        rowView = inflater.inflate(R.layout.similar_scheme_list_row, null);
                        rowView.setId(dynamic_row_pos);

                        TextView row_scheme_name = (TextView)rowView.findViewById(R.id.row_scheme_name);
                        row_scheme_name.setText(scheme_name);

                        final TextView row_scheme_category = (TextView)rowView.findViewById(R.id.row_scheme_category);
                        row_scheme_category.setText(category);



                        final RadioButton Rb_schme_id = (RadioButton)rowView.findViewById(R.id.Rb_schme_id);
                        Rb_schme_id.setId(dynamic_row_pos);

                        ll_parent_change_schemas_plans.addView(rowView);

                        dynamic_row_pos++;




                    }

                    //parent_layout_loan.addView(row_layout);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        Btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int j=0;j<dynamic_row_pos;j++){
                    View row_views= (View)ll_parent_change_schemas_plans.findViewById(j+1);

                    //final RadioGroup row_rg_select = (RadioGroup)row_views.findViewById(R.id.row_rg_select);
                    RadioButton rb_btn = (RadioButton)row_views
                            .findViewById(j+1);
                    if(rb_btn.isChecked()) {
                        Log.d("Selected Plan", ""+rb_btn.getId());
                    }
                    Log.d("dynamic_row_pos", ""+dynamic_row_pos);

                }
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
        public String getProjected_SIPAmount() {
            return Projected_SIPAmount;

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
        public String getProjected_Invested_Amount() {
            return Projected_Invested_Amount;

        }

        @JavascriptInterface
        public String getProjected_TotalAmount() {
            return Projected_TotalAmount;

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