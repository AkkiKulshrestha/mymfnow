package com.indocosmic.mymfnow.mutualFundManualFragmet;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.indocosmic.mymfnow.R;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class FreshPurchase extends Fragment implements View.OnClickListener{


    TextInputLayout input_layout_Units,input_layout_amount;
    EditText edt_Amount, edt_Units,edt_purchaseDate;
    View rootView;
    RadioGroup radioGroup;
    int mYear, mMonth, mDay;

    public FreshPurchase() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_fresh_purchase, container, false);

        init();
        return rootView;
    }


    private void init() {
        radioGroup = (RadioGroup) rootView.findViewById(R.id.radioGroup);
        edt_Amount = (EditText) rootView.findViewById(R.id.edt_Amount);
        edt_Units = (EditText) rootView.findViewById(R.id.edt_Units);
        edt_purchaseDate = (EditText) rootView.findViewById(R.id.edt_purchaseDate);

        input_layout_amount = (TextInputLayout) rootView.findViewById(R.id.input_layout_amount);
        input_layout_Units = (TextInputLayout) rootView.findViewById(R.id.input_layout_Units);


        edt_purchaseDate.setOnClickListener(this);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                RadioButton rb=(RadioButton)rootView.findViewById(checkedId);

                if (rb.getText().equals("Amount")) {
                    input_layout_amount.setVisibility(View.VISIBLE);
                    input_layout_Units.setVisibility(View.GONE);
                }
                if (rb.getText().equals("Units")) {
                    input_layout_amount.setVisibility(View.GONE);
                    input_layout_Units.setVisibility(View.VISIBLE);
                }
               // Toast.makeText(getActivity(), "You Have Selected="+rb.getText(), Toast.LENGTH_SHORT).show();

            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.edt_purchaseDate:
               getPurchaseDate();
                break;
        }
    }

    private void getPurchaseDate() {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        edt_purchaseDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }


}
