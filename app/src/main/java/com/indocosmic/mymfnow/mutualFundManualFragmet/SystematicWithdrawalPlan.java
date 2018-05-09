package com.indocosmic.mymfnow.mutualFundManualFragmet;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.indocosmic.mymfnow.R;
import com.indocosmic.mymfnow.utils.CommonMethods;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class SystematicWithdrawalPlan extends Fragment implements View.OnClickListener{

    private SimpleDateFormat dateFormatter;
    Calendar newCalendar;
    View rootView;
    DatePickerDialog SwpSatrtDateDialog;
    DatePickerDialog SwpEndDateDialog;
    EditText edt_swp_start_date,edt_swp_end_date;
    Button btnSubmitSWPPurchase,btnResetSWPPurchase;
    String Str_SIP_End_Date;
    public SystematicWithdrawalPlan() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_systematic_withdrawal_plan, container, false);
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        init();
        setDateTimeField();
        return rootView;
    }


    private void init() {
        edt_swp_start_date =(EditText)rootView.findViewById(R.id.edt_swp_start_date);
        edt_swp_end_date =(EditText)rootView.findViewById(R.id.edt_swp_end_date);
        btnSubmitSWPPurchase =(Button) rootView.findViewById(R.id.btnSubmitSWPPurchase);
        btnResetSWPPurchase =(Button) rootView.findViewById(R.id.btnResetSWPPurchase);

        edt_swp_start_date.setOnClickListener(this);
        edt_swp_end_date.setOnClickListener(this);
        btnResetSWPPurchase.setOnClickListener(this);

    }

    private void setDateTimeField() {

        newCalendar = Calendar.getInstance();

        SwpSatrtDateDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                edt_swp_start_date.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        SwpEndDateDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                edt_swp_end_date.setText(dateFormatter.format(newDate.getTime()));
                Str_SIP_End_Date = dateFormatter.format(newDate.getTime());
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();
        if (id==R.id.edt_swp_start_date) {
            SwpSatrtDateDialog.show();
        }if (id == R.id.edt_swp_end_date) {
            SwpEndDateDialog.show();
        }
        if (id == R.id.btnResetSWPPurchase) {

            CommonMethods.clearForm((ViewGroup) rootView.findViewById(R.id.layout_parent_SWP));
        }
    }
}
