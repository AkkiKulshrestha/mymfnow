package com.indocosmic.mymfnow.riskProfile;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.indocosmic.mymfnow.Home;
import com.indocosmic.mymfnow.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class RiskProfile extends AppCompatActivity {


    LinearLayout llParent;
    Button btnSubmit;
    int radiopos,i;
    int txtcounter=0;
    RadioGroup radioGroup;
    String str_risk_profile;

    int answer1 =-1,answer2 = -1,answer3 = -1,answer4 = -1,answer5 = -1,answer6 = -1,answer7 = -1,answer8 =-1;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_risk_profile);


        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Risk Profile");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        init();
        fetch_questionList_volley();
    }



    private void init() {

        llParent = (LinearLayout)findViewById(R.id.llParent);
        btnSubmit = (Button)findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (answer1 == -1 || answer2 == -1 || answer4 == -1 || answer3 == -1 || answer5 == -1 || answer6 == -1 || answer7 == -1 || answer8 == -1)
                {
                    Toast.makeText(RiskProfile.this, "Please Answer All the Questions", Toast.LENGTH_SHORT).show();
                    return;
                }
                else
                {
                    APIForRiskProfile();
                }
            }
        });
    }

    private void APIForRiskProfile() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://www.mymfnow.com/api/robo/getRiskProfile",
                new com.android.volley.Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("rdata", "merarespnse: "+response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            str_risk_profile = jsonObject.getString("risk_profile");

                            createRiskDialog();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Server Problem", Toast.LENGTH_SHORT).show();
                    }
                }) {

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("answer1", String.valueOf(answer1));
                params.put("answer2", String.valueOf(answer2));
                params.put("answer3", String.valueOf(answer3));
                params.put("answer4", String.valueOf(answer4));
                params.put("answer5", String.valueOf(answer5));
                params.put("answer6", String.valueOf(answer6));
                params.put("answer7", String.valueOf(answer7));
                params.put("answer8", String.valueOf(answer8));
                Log.d("total_param", "getParams: "+params);
                return params;
            }
        };

        int socketTimeout = 50000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }


    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(),Home.class);
        startActivity(i);
        overridePendingTransition(R.animator.left_right,R.animator.right_left);
        finish();
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getApplicationContext().getAssets().open("master_risk_questionery.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    private void fetch_questionList_volley() {
        llParent.removeAllViews();

                       try {

                           JSONObject jsonObjectResponse = new JSONObject(loadJSONFromAsset());


                            JSONArray jsonArrayResponse = jsonObjectResponse.getJSONArray("QuestionsList");


                            for ( i=0;i<jsonArrayResponse.length();i++) {

                                JSONObject jsonObject = jsonArrayResponse.getJSONObject(i);

                                String QuestionNo=jsonObject.getString("QuestionNo");
                                String Question= jsonObject.getString("Question");
                                String Option1 = jsonObject.getString("Option1");
                                String Option2 = jsonObject.getString("Option2");
                                String Option3 = jsonObject.getString("Option3");
                                String Option4 = jsonObject.getString("Option4");


                                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                final View rowView = inflater.inflate(R.layout.risk_layout_menu, null);


                                TextView questions = (TextView)rowView.findViewById(R.id.questions);
                                questions.setText(QuestionNo+". " +Question);

                                radioGroup = (RadioGroup) rowView.findViewById(R.id.radiogroup);

                                RadioButton radio_option1 = (RadioButton) rowView.findViewById(R.id.option1);
                                radio_option1.setText(Option1);
                                RadioButton radio_option2 = (RadioButton) rowView.findViewById(R.id.option2);
                                radio_option2.setText(Option2);
                                RadioButton radio_option3 = (RadioButton) rowView.findViewById(R.id.option3);
                                radio_option3.setText(Option3);
                                RadioButton radio_option4 = (RadioButton) rowView.findViewById(R.id.option4);
                                radio_option4.setText(Option4);

                                txtcounter++;
                                rowView.setId(txtcounter);


                                llParent.addView(rowView);

                                addOnClickFeatures();
                            }



                        } catch (Exception e) {
                            e.printStackTrace();
                        }
           }

    private void addOnClickFeatures() {
        for (int j=0;j<txtcounter;j++) {

            final View row = (View) llParent.findViewById(j + 1);

            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @SuppressLint("ResourceType")
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    View radioButton = group.findViewById(checkedId);
                    radiopos = group.indexOfChild(radioButton);

                    if (row.getId() == 1) {
                        answer1 = radiopos+1;
                    }
                    if (row.getId() == 2) {
                        answer2 = radiopos+1;
                    }
                    if (row.getId() == 3) {
                        answer3 = radiopos+1;
                    }
                    if (row.getId() == 4) {
                        answer4 = radiopos+1;
                    }
                    if (row.getId() == 5) {
                        answer5 = radiopos+1;
                    }
                    if (row.getId() == 6) {
                        answer6 = radiopos+1;
                    }
                    if (row.getId() == 7) {
                        answer7 = radiopos+1;
                    }if (row.getId() == 8) {
                        answer8 = radiopos+1;
                    }

                }
            });
        }
    }


    private void createRiskDialog() {

        final Dialog dialog11 = new Dialog(this);
        dialog11.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog11.setCanceledOnTouchOutside(false);
        dialog11.setContentView(R.layout.popup_answer_risk);

        TextView risk_profile=(TextView)dialog11.findViewById(R.id.txt_risk_profile);
        Button btnOk=(Button) dialog11.findViewById(R.id.btnOk);

        risk_profile.setText(str_risk_profile);

        dialog11.show();
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog11.dismiss();
                Intent i = new Intent(getApplicationContext(),Home.class);
                startActivity(i);
                overridePendingTransition(R.animator.left_right,R.animator.right_left);
                finish();
                // overridePendingTransition(R.animator.left_right, R.animator.right_left);
            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        Intent i = new Intent(getApplicationContext(),Home.class);
        startActivity(i);
        overridePendingTransition(R.animator.left_right,R.animator.right_left);
       // finish();
        return true;
    }



}