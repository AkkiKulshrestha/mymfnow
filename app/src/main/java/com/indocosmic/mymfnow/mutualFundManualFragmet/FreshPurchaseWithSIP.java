package com.indocosmic.mymfnow.mutualFundManualFragmet;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.Editable;
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
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.indocosmic.mymfnow.R;
import com.indocosmic.mymfnow.utils.CommonMethods;
import com.indocosmic.mymfnow.utils.MyValidator;
import com.indocosmic.mymfnow.utils.NumberTextWatcherForThousand;

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
public class FreshPurchaseWithSIP extends Fragment implements View.OnClickListener,AdapterView.OnItemSelectedListener,TextWatcher {

    EditText edt_FirstinvestmentDate, edt_SIPStartDate, edt_SIPEndDate,edt_Folio_Number,edt_broker_code,edt_SIP_Amount;
    AutoCompleteTextView edt_SchemeName;
    ArrayAdapter<String> autoCompleteAdapter;
    private DatePickerDialog FirstinvestmentDate;
    private DatePickerDialog SIPStartDate;
    private DatePickerDialog SIPEndDate;

    String Str_SIP_Start_Date,Str_SIP_End_Date;
    private SimpleDateFormat dateFormatter;
    Spinner spn_frequency;
    View rootView;
    LinearLayout layout_parent;

    Button btnSubmitFreshPurchaseWithSip,btnResetFreshPurchaseWithSip;
    private static final String[] Frequency_Items = {"Select","Daily", "Weekely", "Fortnightly", "Monthly", "Quarterly"};

    private static final String TAG = "FreshPurchaseWithSIP";

    public FreshPurchaseWithSIP() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_fresh_purchase_with_si, container, false);

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        init();

        setDateTimeField();

        return rootView;
    }


    private void init() {

        edt_SchemeName = (AutoCompleteTextView) rootView.findViewById(R.id.edt_SchemeName);
        edt_SIP_Amount = (EditText)rootView.findViewById(R.id.edt_SIP_Amount);
        edt_FirstinvestmentDate = (EditText) rootView.findViewById(R.id.edt_FirstinvestmentDate);
        edt_SIPStartDate = (EditText) rootView.findViewById(R.id.edt_SIPStartDate);
        edt_SIPEndDate = (EditText) rootView.findViewById(R.id.edt_SIPEndDate);

        edt_Folio_Number = (EditText) rootView.findViewById(R.id.edt_Folio_Number);
        edt_broker_code = (EditText) rootView.findViewById(R.id.edt_broker_code);
        spn_frequency = (Spinner) rootView.findViewById(R.id.spn_frequency);

       // layout_parent = (LinearLayout) rootView.findViewById(R.id.layout_parent);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, Frequency_Items);
        spn_frequency.setAdapter(adapter);

        btnSubmitFreshPurchaseWithSip = (Button) rootView.findViewById(R.id.btnSubmitFreshPurchaseWithSip);
        btnResetFreshPurchaseWithSip = (Button) rootView.findViewById(R.id.btnResetFreshPurchaseWithSip);

        edt_SchemeName.addTextChangedListener(this);
        btnSubmitFreshPurchaseWithSip.setOnClickListener(this);
        btnResetFreshPurchaseWithSip.setOnClickListener(this);
        spn_frequency.setOnItemSelectedListener(this);
    }

    private void setDateTimeField() {

        edt_FirstinvestmentDate.setOnClickListener(this);
        edt_SIPStartDate.setOnClickListener(this);
        edt_SIPEndDate.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();

        FirstinvestmentDate = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                edt_FirstinvestmentDate.setText(dateFormatter.format(newDate.getTime()));
                Str_SIP_Start_Date = (dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        SIPStartDate = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                edt_SIPStartDate.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        SIPEndDate = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                edt_SIPEndDate.setText(dateFormatter.format(newDate.getTime()));
                Str_SIP_End_Date = dateFormatter.format(newDate.getTime());
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

    }


    @Override
    public void onClick(View view) {

        int id = view.getId();
        if (id==R.id.edt_FirstinvestmentDate) {
            FirstinvestmentDate.show();
        }if (id == R.id.edt_SIPStartDate) {
            SIPStartDate.show();
        }else if (id == R.id.edt_SIPEndDate) {

            SIPEndDate.show();
        }else if(id==R.id.btnSubmitFreshPurchaseWithSip) {
            if (validateSIPData()) {
                ApiForFetchingSIPData();
            }

        }else if (id == R.id.btnResetFreshPurchaseWithSip) {

            CommonMethods.clearForm((ViewGroup) rootView.findViewById(R.id.layout_parent));
        }
    }

    private boolean validateSIPData() {
        boolean result = true;

        if (!MyValidator.isValidField(edt_SchemeName)) {
            edt_SchemeName.requestFocus();
            edt_SchemeName.setError("Please Enter Scheme Name");
            result = false;
        }

        if (!MyValidator.isValidField(edt_SIP_Amount)) {
            edt_SIP_Amount.requestFocus();
            edt_SIP_Amount.setError("Please Enter SIP Amount");
            result = false;
        }

        if (!MyValidator.isValidField(edt_FirstinvestmentDate)){
            edt_SchemeName.requestFocus();
            edt_SchemeName.setError("Select first Investment Date");
            result = false;
        }


        if (!MyValidator.isValidSpinner(spn_frequency)) {
            result = false;
        }

//       if (MyValidator.CheckDates(Str_SIP_Start_Date,Str_SIP_End_Date,edt_SIPEndDate)){
//           Toast.makeText(getActivity(), "result in date="+result, Toast.LENGTH_SHORT).show();
//           result = false;
//       }

        if (!MyValidator.isValidField(edt_Folio_Number)) {
            edt_Folio_Number.requestFocus();
            edt_Folio_Number.setError("Please Enter Folio Number");
            result = false;
        }

        return result;
    }

    private void ApiForFetchingSIPData() {

    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

        if (editable.length() >=2) {
            loadSuggestions();
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


                            autoCompleteAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line,
                                    arrList);

                            edt_SchemeName.setAdapter(autoCompleteAdapter);


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
}

