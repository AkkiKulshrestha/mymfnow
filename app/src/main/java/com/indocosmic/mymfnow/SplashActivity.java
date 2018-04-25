package com.indocosmic.mymfnow;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.indocosmic.mymfnow.utils.CommonMethods;
import com.indocosmic.mymfnow.utils.ConnectionDetector;
import com.indocosmic.mymfnow.utils.Constant;
import com.indocosmic.mymfnow.webservices.RestClient;

import org.json.JSONObject;

public class SplashActivity extends AppCompatActivity {



    int YourApkVersionCode;
    private int STORAGE_PERMISSION_CODE = 23;
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 1500;

    Dialog dialog;
    String Force_Update_flag = "false";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isTaskRoot())
        {
            final Intent intent = getIntent();
            final String intentAction = intent.getAction();
            if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && intentAction != null && intentAction.equals(Intent.ACTION_MAIN)) {
                finish();
                return;
            }
        }

        setContentView(R.layout.activity_splash);
        force_update();

    }

    private void force_update() {
        int currentVersionCode = getCurrentVersion();
        //Log.d("Current version = ",currentVersionCode);
        final String get_latest_version_info = RestClient.ROOT+"/appupdate/getLatestUpdateVersion";
        Log.d("URL --->", get_latest_version_info);
        try {
            ConnectionDetector cd = new ConnectionDetector(this);
            boolean isInternetPresent = cd.isConnectingToInternet();
            if (isInternetPresent) {
                StringRequest stringRequest = new StringRequest(Request.Method.GET, get_latest_version_info, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            String latestVersion = "";
                            JSONObject Jobj = new JSONObject(response);
                            String Id = Jobj.getString("Id");
                            String VersionCode = Jobj.getString("VersionCode");
                            String VersionName = Jobj.getString("VersionName");
                            Force_Update_flag = Jobj.getString("IsForceUpdate");
                            update_dialog(VersionCode);

                            Log.d("Latest version:",latestVersion);
                        } catch (Exception e) {
                            Log.d("Exception",e.toString());
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        volleyError.printStackTrace();
                        Log.d("Vollley Err", volleyError.toString());
                       /* if(volleyError.toString().equalsIgnoreCase("com.android.volley.ServerError")){
                            Intent i = new Intent(getApplicationContext(),MaintainancePage.class);
                            startActivity(i);
                            overridePendingTransition(R.animator.move_left,R.animator.move_right);
                        }*/
                    }
                });
               /* int socketTimeout = 50000;//30 seconds - change to what you want
                RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                stringRequest.setRetryPolicy(policy);*/
                RequestQueue requestQueue = Volley.newRequestQueue(this);
                requestQueue.add(stringRequest);
            }else {
                CommonMethods.DisplayToast(this,"No Internet Connection");
            }
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private int getCurrentVersion(){
        PackageManager pm = this.getPackageManager();
        PackageInfo pInfo = null;
        try {
            pInfo =  pm.getPackageInfo(this.getPackageName(),0);
        } catch (PackageManager.NameNotFoundException e1) {
            e1.printStackTrace();
        }
        String currentVersion = pInfo.versionName;
        YourApkVersionCode = pInfo.versionCode;
        Log.d("YourVersionCode",""+YourApkVersionCode);
        String version_code = String.valueOf(YourApkVersionCode);
        return YourApkVersionCode;
    }


    private void update_dialog(String versionCode) {
        if(versionCode!="" || versionCode!=null){
            int v_code = Integer.valueOf(versionCode);
            Force_Update_flag = "true";
            v_code = 2;

            if((YourApkVersionCode < v_code) && Force_Update_flag.equalsIgnoreCase("true")){
                Log.d("version code",""+v_code);
                Log.d("Your APK CODE ",""+YourApkVersionCode);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        final Dialog dialog = new Dialog(SplashActivity.this);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.setCancelable(false);
                        dialog.setContentView(R.layout.pop_up_app_update);
                        dialog.getWindow().setBackgroundDrawable(
                                new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        TextView title = (TextView)dialog.findViewById(R.id.title) ;
                        TextView Upgrade_text = (TextView)dialog.findViewById(R.id.Upgrade_text) ;
                        TextView tv_ok = (TextView)dialog.findViewById(R.id.tv_ok);
                        title.setTypeface(Constant.OpenSansBold(getApplicationContext()));
                        Upgrade_text.setTypeface(Constant.LatoRegular(getApplicationContext()));
                        tv_ok.setTypeface(Constant.LatoBold(getApplicationContext()));
                        dialog.show();
                        tv_ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                CommonMethods.deleteCache(getApplicationContext());
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
                                finish();
                            }
                        });
                    }
                }, SPLASH_TIME_OUT);
            }else
            {
                /*UtilitySharedPreferences.clearPref(this);*/
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(getApplicationContext(),MainPortalDashboard.class);
                        startActivity(i);


                    }
                }, SPLASH_TIME_OUT);

            }
        }else{
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent i = new Intent(getApplicationContext(),MainPortalDashboard.class);
                    startActivity(i);

                }
            }, SPLASH_TIME_OUT);
        }
    }


}
