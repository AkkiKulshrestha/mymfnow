package com.indocosmic.mymfnow.utils;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by SIR.WilliamRamsay on 03-Dec-15.
 */
public class MyValidator {
    private static final String EMAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final String PHONE_REGEX = "\\d{3}-\\d{7}";
    private static final String REQUIRED_MSG = "Field required";
    Bitmap bitmap=null;






    // validating email id
    public static boolean isValidEmail(EditText editText) {
        String email = editText.getText().toString().trim();
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()) {
            editText.setError("Enter valid Email");
            return false;
        }else if (email.length() == 0) {
            editText.setError("Enter valid Email");
            return false;
        }
        editText.setError(null);
        return true;
    }

    // validating password
    public static boolean isValidPassword(EditText editText) {
        String pass = editText.getText().toString().trim();
        if (pass != null && pass.length() > 3) {
            editText.setError(null);
            return true;
        }
        editText.setError("Enter valid Password");
        return false;
    }




    public static boolean isValidField(EditText editText) {
        String txtValue = editText.getText().toString().trim();

        if (txtValue.length() == 0) {
            editText.setError(REQUIRED_MSG);
            return false;
        }
        editText.setError(null);
        return true;
    }

    public static boolean isValidAge(EditText editText) {
       Integer retValue=0;

        String txtValue = editText.getText().toString().trim();
        if(txtValue.length() == 0){
            editText.setError(REQUIRED_MSG);
            return  false;
        }

        if(txtValue.length() > 0 && txtValue.length()>2)
        {
            editText.setError("Age cannot be greater than 100 years");
            return  false;
        }

        if(txtValue.length() > 0 && Integer.valueOf(txtValue)==0){
            editText.setError("Age cannot be 0 year");
            return  false;
        }


        editText.setError(null);
        return true;
    }

    public static boolean isValidSpinner(Spinner spinner) {
        View view = spinner.getSelectedView();
        TextView textView = (TextView) view;
        if (spinner.getSelectedItemPosition() == 0) {
            textView.setError("None selected");
            return false;
        }
        textView.setError(null);
        return true;
    }

    public static boolean isValidMobile(EditText editText) {
        String mob = editText.getText().toString().trim();
        if (mob != null && mob.length() == 10) {
            editText.setError(null);
            return true;
        }
        editText.setError(REQUIRED_MSG + " Enter 10 digits");
        return false;
    }

    public static boolean isValidImage(@NonNull ImageView view) {
        Drawable drawable = view.getDrawable();
        boolean hasImage = (drawable != null);

        if (hasImage && (drawable instanceof BitmapDrawable)) {
            hasImage = ((BitmapDrawable)drawable).getBitmap() != null;
        }

        return hasImage;
    }

    public static boolean CheckDates(String StartDT,String EndDT)    {
        SimpleDateFormat dfDate  = new SimpleDateFormat("yyyy-MM-dd");
        boolean b = false;
        try {
            if(dfDate.parse(StartDT).before(dfDate.parse(EndDT)))
            {
                b = true;//If start date is before end date
            }
            else if(dfDate.parse(StartDT).equals(dfDate.parse(EndDT)))
            {
                b = false;//If two dates are equal
            }
            else
            {
                b = false; //If start date is after the end date
            }
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return b;
    }

}
