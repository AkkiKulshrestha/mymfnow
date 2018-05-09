package com.indocosmic.mymfnow.mutualFundManualFragmet;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSpinner;
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
public class AdditionalPurchase extends Fragment implements View.OnClickListener,RadioGroup.OnCheckedChangeListener{

    AppCompatSpinner spn_folio_number,spn_scheme_name;
    View rootView;
    RadioGroup radioGroup;
    TextInputLayout input_Layout_Amount,input_Layout_Units;
    private DatePickerDialog PurchaseScemeDate;
    EditText edt_purchaseDate;
    private SimpleDateFormat dateFormatter;
    Calendar newCalendar;
    Button btnSubmitAdditionalPurchase,btnResetAdditionalPurchase;
    public AdditionalPurchase() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_additional_purchase, container, false);

        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        init();
        setDateTimeField();
        return rootView;
    }

    private void setDateTimeField() {
        edt_purchaseDate.setOnClickListener(this);

        newCalendar = Calendar.getInstance();

        PurchaseScemeDate = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                edt_purchaseDate.setText(dateFormatter.format(newDate.getTime()));

            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        PurchaseScemeDate.getDatePicker().setMaxDate(newCalendar.getTimeInMillis());
    }

    private void init() {

        radioGroup = (RadioGroup) rootView.findViewById(R.id.radioGroup);
        edt_purchaseDate = (EditText) rootView.findViewById(R.id.edt_purchaseDate);
        btnSubmitAdditionalPurchase = (Button) rootView.findViewById(R.id.btnSubmitAdditionalPurchase);
        btnResetAdditionalPurchase = (Button) rootView.findViewById(R.id.btnResetAdditionalPurchase);

        input_Layout_Amount = (TextInputLayout) rootView.findViewById(R.id.input_Layout_Amount);
        input_Layout_Units = (TextInputLayout) rootView.findViewById(R.id.input_Layout_Units);


        btnResetAdditionalPurchase.setOnClickListener(this);
        edt_purchaseDate.setOnClickListener(this);
        radioGroup.setOnCheckedChangeListener(this);

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id== R.id.edt_purchaseDate){
            PurchaseScemeDate.show();
        }if (id == R.id.btnResetAdditionalPurchase){
            CommonMethods.clearForm((ViewGroup) rootView.findViewById(R.id.layout_parent_linear));
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
    }
}
