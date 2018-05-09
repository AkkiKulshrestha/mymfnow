package com.indocosmic.mymfnow.mutualFundManualFragmet;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.indocosmic.mymfnow.R;
import com.indocosmic.mymfnow.utils.CommonMethods;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class Redemption extends Fragment implements View.OnClickListener,RadioGroup.OnCheckedChangeListener{

    private SimpleDateFormat dateFormatter;
    Calendar newCalendar;
    View rootView;
    RadioGroup radioGroup;
    TextInputLayout input_Layout_Amount,input_Layout_Units;
    DatePickerDialog RedemptionScemeDate;
    EditText edt_redemptionDate,edt_swp_amount;
    Button btnSubmitRedemptionPurchase,btnResetRedemptionPurchase;

    public Redemption() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_redemption, container, false);
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        init();
        setDateTimeField();
        return rootView;
    }


    private void init() {

        radioGroup = (RadioGroup) rootView.findViewById(R.id.radioGroup);
        edt_redemptionDate = (EditText) rootView.findViewById(R.id.edt_redemptionDate);
        edt_swp_amount = (EditText) rootView.findViewById(R.id.edt_swp_amount);
        btnSubmitRedemptionPurchase = (Button) rootView.findViewById(R.id.btnSubmitRedemptionPurchase);
        btnResetRedemptionPurchase = (Button) rootView.findViewById(R.id.btnResetRedemptionPurchase);

        input_Layout_Amount = (TextInputLayout) rootView.findViewById(R.id.input_Layout_Amount);
        input_Layout_Units = (TextInputLayout) rootView.findViewById(R.id.input_Layout_Units);

        edt_redemptionDate.setOnClickListener(this);
        btnResetRedemptionPurchase.setOnClickListener(this);
        radioGroup.setOnCheckedChangeListener(this);
    }

    private void setDateTimeField() {
        edt_redemptionDate.setOnClickListener(this);

        newCalendar = Calendar.getInstance();

        RedemptionScemeDate = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                edt_redemptionDate.setText(dateFormatter.format(newDate.getTime()));

            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        RedemptionScemeDate.getDatePicker().setMaxDate(newCalendar.getTimeInMillis());
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id== R.id.edt_redemptionDate){
            RedemptionScemeDate.show();
        }if (id == R.id.btnResetRedemptionPurchase){
            CommonMethods.clearForm((ViewGroup) rootView.findViewById(R.id.layout_parent_redemption));
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        RadioButton rb = (RadioButton)rootView.findViewById(checkedId);

        if (rb.getText().equals("Amount")) {
            input_Layout_Amount.setVisibility(View.VISIBLE);
            input_Layout_Units.setVisibility(View.GONE);
        }
        if (rb.getText().equals("Units")) {
            input_Layout_Amount.setVisibility(View.GONE);
            input_Layout_Units.setVisibility(View.VISIBLE);
        }
        if (rb.getText().equals("Sell All Units")) {
            input_Layout_Amount.setVisibility(View.GONE);
            input_Layout_Units.setVisibility(View.GONE);
        }
    }

}

