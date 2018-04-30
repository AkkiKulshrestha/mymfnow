package com.indocosmic.mymfnow;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;


public class LoginScreen extends AppCompatActivity {

    LinearLayout LayoutTabUser,LayoutTabAdvisor,Layout_user_login,Layout_advisor_login;
    EditText Edt_PanNo;
    Button Btn_Login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();

    }

    private void init() {

        LayoutTabUser= (LinearLayout)findViewById(R.id.LayoutTabUser);

        LayoutTabAdvisor= (LinearLayout)findViewById(R.id.LayoutTabAdvisor);

        Layout_user_login= (LinearLayout)findViewById(R.id.Layout_user_login);
        Layout_advisor_login= (LinearLayout)findViewById(R.id.Layout_advisor_login);

        LayoutTabUser.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
        LayoutTabAdvisor.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));

        Layout_user_login.setVisibility(View.VISIBLE);
        Layout_advisor_login.setVisibility(View.GONE);

        LayoutTabUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutTabUser.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
                LayoutTabAdvisor.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                Layout_user_login.setVisibility(View.VISIBLE);
                Layout_advisor_login.setVisibility(View.GONE);


            }
        });

        LayoutTabAdvisor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutTabUser.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                LayoutTabAdvisor.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
                Layout_user_login.setVisibility(View.GONE);
                Layout_advisor_login.setVisibility(View.VISIBLE);

            }
        });

        Edt_PanNo = (EditText)findViewById(R.id.Edt_PanNo);
        Edt_PanNo.setFilters(new InputFilter[] {new InputFilter.AllCaps()});

        Btn_Login = (Button)findViewById(R.id.Btn_Login);
        Btn_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),CalculatorDashboard.class);
                startActivity(i);
            }
        });

    }

}
