package com.indocosmic.mymfnow.mutualFundManualFragmet;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.indocosmic.mymfnow.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class FreshPurchaseWithSIP extends Fragment implements View.OnClickListener {

    EditText edt_FirstinvestmentDate, edt_SIPStartDate, edt_SIPEndDate;

    private DatePickerDialog FirstinvestmentDate;
    private DatePickerDialog SIPStartDate;
    private DatePickerDialog SIPEndDate;

    private SimpleDateFormat dateFormatter;
    private ArrayAdapter<String> adapter;
    View rootView;

    private static final String[] ITEMS = {"Daily", "Weekely", "Fortnightly", "Monthly", "Quarterly"};


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

        edt_FirstinvestmentDate = (EditText) rootView.findViewById(R.id.edt_FirstinvestmentDate);
        edt_SIPStartDate = (EditText) rootView.findViewById(R.id.edt_SIPStartDate);
        edt_SIPEndDate = (EditText) rootView.findViewById(R.id.edt_SIPEndDate);
       // frequencySpinner = (MaterialSpinner) rootView.findViewById(R.id.frequencySpinner);
        //adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, ITEMS);
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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
        }
    }

}

