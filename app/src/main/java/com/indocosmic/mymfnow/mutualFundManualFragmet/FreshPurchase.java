package com.indocosmic.mymfnow.mutualFundManualFragmet;


import android.app.DatePickerDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.indocosmic.mymfnow.R;
import com.indocosmic.mymfnow.utils.CommonMethods;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class FreshPurchase extends Fragment implements View.OnClickListener,TextWatcher{


    TextInputLayout input_layout_Units,input_layout_amount;
    EditText edt_Amount, edt_Units,edt_purchaseDate,edt_NAV_Value;
    TextView txt_dividend_option;
    AutoCompleteTextView edt_SchemeName;
    Button btnSubmitFreshPurchase,btnResetFreshPurchase;
    View rootView;
    RadioGroup radioGroup,dividendRadioGroup;

    private DatePickerDialog PurchaseDate;
    private SimpleDateFormat dateFormatter;
    private static final String TAG = "FreshPurchase";
    ArrayAdapter<String> adapter;
    LinearLayout layout_parent_linear;
    Calendar newCalendar;
    public FreshPurchase() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_fresh_purchase, container, false);

        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        init();
        setDateTimeField();
        return rootView;
    }

    private void setDateTimeField() {


       edt_purchaseDate.setOnClickListener(this);

        newCalendar = Calendar.getInstance();

        PurchaseDate = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                edt_purchaseDate.setText(dateFormatter.format(newDate.getTime()));

                getNAV_Value();
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
       PurchaseDate.getDatePicker().setMaxDate(newCalendar.getTimeInMillis());
    }

    private void getNAV_Value() {

        StringRequest postRequest = new StringRequest(Request.Method.POST,"http://www.mymfnow.com/investor/mf/getNavForDate",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        String str_NavValue = response.replaceAll(".*,", "");

                        edt_NAV_Value.setText(str_NavValue);
                        edt_NAV_Value.setEnabled(false);
                        Log.d(TAG, "onResponse: "+response);
                        Log.d(TAG, "str_NavValue: "+str_NavValue);
                        Toast.makeText(getActivity(), "response="+response, Toast.LENGTH_LONG).show();
                        Toast.makeText(getActivity(), "str_NavValue="+str_NavValue, Toast.LENGTH_LONG).show();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //progressDialog.dismiss();
                        Toast.makeText(getActivity(), "Server Problem", Toast.LENGTH_SHORT).show();
                    }
                }){

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                // the POST parameters:
                params.put("scheme_amfi", edt_SchemeName.getText().toString());
                params.put("date",edt_purchaseDate.getText().toString());
                Log.d(TAG, "getParams: "+params);
                Log.d(TAG, "dataparams"+"http://www.mymfnow.com/api/investor/mf/autoSuggestMfSchemeWithDividend"+params.toString());
                return params;

            }
        };
        Volley.newRequestQueue(getActivity()).add(postRequest);

    }


    private void init() {
        radioGroup = (RadioGroup) rootView.findViewById(R.id.radioGroup);

        dividendRadioGroup = (RadioGroup) rootView.findViewById(R.id.dividendRadioGroup);

        txt_dividend_option = (TextView) rootView.findViewById(R.id.txt_dividend_option);

        edt_Amount = (EditText) rootView.findViewById(R.id.edt_Amount);
        edt_Units = (EditText) rootView.findViewById(R.id.edt_Units);
        edt_purchaseDate = (EditText) rootView.findViewById(R.id.edt_purchaseDate);
        edt_NAV_Value = (EditText) rootView.findViewById(R.id.edt_NAV_Value);

        edt_SchemeName = (AutoCompleteTextView) rootView.findViewById(R.id.edt_SchemeName);

        input_layout_amount = (TextInputLayout) rootView.findViewById(R.id.input_layout_amount);
        input_layout_Units = (TextInputLayout) rootView.findViewById(R.id.input_layout_Units);

        btnSubmitFreshPurchase = (Button) rootView.findViewById(R.id.btnSubmitFreshPurchase);
        btnResetFreshPurchase = (Button) rootView.findViewById(R.id.btnResetFreshPurchase);

        edt_purchaseDate.setOnClickListener(this);
        edt_SchemeName.addTextChangedListener(this);
        btnResetFreshPurchase.setOnClickListener(this);

        edt_SchemeName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getActivity(), "pos=", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                RadioButton rb = (RadioButton)rootView.findViewById(checkedId);

                if (rb.getText().equals("Amount")) {
                    input_layout_amount.setVisibility(View.VISIBLE);
                    input_layout_Units.setVisibility(View.GONE);
                }
                if (rb.getText().equals("Units")) {
                    input_layout_amount.setVisibility(View.GONE);
                    input_layout_Units.setVisibility(View.VISIBLE);
                }
            }
        });

        dividendRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                RadioButton rb = (RadioButton)rootView.findViewById(checkedId);
            }
        });
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.edt_purchaseDate:

            if (edt_SchemeName.getText().toString() == null || edt_SchemeName.getText().toString().isEmpty()) {
               //  edt_SchemeName.setError("Select Scheme First");
                Toast.makeText(getActivity(), "Select Scheme First", Toast.LENGTH_LONG).show();
            return;
            }else {
                    PurchaseDate.show();
                    break;
            }

            case R.id.btnResetFreshPurchase:
                CommonMethods.clearForm((ViewGroup) rootView.findViewById(R.id.layout_parent_linear));
                break;
        }
    }


    private void loadSuggestions() {

        StringRequest postRequest = new StringRequest(Request.Method.POST,"http://www.mymfnow.com/investor/mf/autoSuggestMfSchemeWithDividend",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {


                            JSONArray jsonArray = new JSONArray(response);
                            ArrayList<String> arrList=new ArrayList<String>();

                            for (int i=0;i<jsonArray.length();i++) {

                                String strValue= jsonArray.optString(i);

                                Log.d(TAG, "innerJson: "+strValue);
                                arrList.add(strValue);
                            }

                            Log.d(TAG, "onResponse: "+response);


                            adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line,
                                    arrList);

                            edt_SchemeName.setAdapter(adapter);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //progressDialog.dismiss();
                        Toast.makeText(getActivity(), "Server Problem", Toast.LENGTH_SHORT).show();
                    }
                }){

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                // the POST parameters:
                params.put("query",edt_SchemeName.getText().toString());
                Log.d(TAG, "getParams: "+params.toString());
                Log.d(TAG, "dataparams"+"http://www.mymfnow.com/api/investor/mf/autoSuggestMfSchemeWithDividend"+params.toString());
                return params;

            }
        };
        Volley.newRequestQueue(getActivity()).add(postRequest);

    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {


    }

    @Override
    public void afterTextChanged(Editable s) {

        if (s.length() >=2) {
            loadSuggestions();
        }
    }

}
