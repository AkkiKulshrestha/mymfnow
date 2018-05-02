package com.indocosmic.mymfnow;


import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.indocosmic.mymfnow.adapters.ExpandableListAdapter;
import com.indocosmic.mymfnow.fragments.DashBoard;
import com.indocosmic.mymfnow.fragments.Portfolio;
import com.indocosmic.mymfnow.fragments.PortfolioAnalysis;
import com.indocosmic.mymfnow.fragments.SIPSchemes;
import com.indocosmic.mymfnow.mutualFundManualFragmet.FreshPurchase;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ExpandableListView expandableList;
    List<String> groupList;
    List<String> childList;
    Map<String, List<String>> dataCollection;

    FragmentManager fragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ImageButton menuLeft = (ImageButton) findViewById(R.id.menuLeft);
        ImageButton menuRight = (ImageButton) findViewById(R.id.menuRight);



        menuLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });

        menuRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer.isDrawerOpen(GravityCompat.END)) {
                    drawer.closeDrawer(GravityCompat.END);
                } else {
                    drawer.openDrawer(GravityCompat.END);
                }
            }
        });


        NavigationView navigationView1 = (NavigationView) findViewById(R.id.nav_view);
        NavigationView navigationView2 = (NavigationView) findViewById(R.id.nav_view2);
        navigationView1.setNavigationItemSelectedListener(this);
        navigationView2.setNavigationItemSelectedListener(this);

        createGroupList();

        createCollection();
        View header2 = navigationView2.getHeaderView(0);
        LinearLayout LayoutShare = (LinearLayout)header2.findViewById(R.id.LayoutShare);
        LayoutShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getApplicationContext();
                final String appPackageName = context.getPackageName();
                Intent sendIntent = new Intent();
                sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Download MYMFNOW -  https://play.google.com/store/apps/details?id=" + appPackageName);
                sendIntent.setType("text/plain");

                context.startActivity(sendIntent);
            }
        });


        expandableList = (ExpandableListView) findViewById(R.id.navigationmenu);

        if (savedInstanceState == null) {
            selectDashBoardFragment();
        }

        //Set up expandable List Adapter

        final ExpandableListAdapter expListAdapter = new ExpandableListAdapter(this, groupList, dataCollection);
        expandableList.setAdapter(expListAdapter);

        expandableList.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int previousGroup = -1;

            @Override
            public void onGroupExpand(int groupPosition) {
                if(groupPosition != previousGroup)
                    expandableList.collapseGroup(previousGroup);
                previousGroup = groupPosition;
            }
        });

        expandableList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int position, long l) {

                final String selected = (String) expListAdapter.getGroup(position);

                if (selected.equalsIgnoreCase("Calculator")) {

                    startActivity(new Intent(getApplicationContext(),CalculatorDashboard.class));
                }else if (selected.equalsIgnoreCase("Robo Advisor")) {

                    startActivity(new Intent(getApplicationContext(),RoboDashboard.class));
                }

                return true;
            }
        });


        expandableList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {

                final String selected = (String) expListAdapter.getChild(groupPosition, childPosition);

                Fragment fragment = null;
                Class fragmentClass = null;

                if (selected.equalsIgnoreCase("Portfolio")) {
                    fragmentClass = Portfolio.class;

                }else if (selected.equalsIgnoreCase("Portfolio Analysis")){
                    fragmentClass = PortfolioAnalysis.class;
                }else if (selected.equalsIgnoreCase("SIP Schemes")){

                    fragmentClass = SIPSchemes.class;
                }else if (selected.equalsIgnoreCase("Fresh Purchase")){
//                    Toast.makeText(getBaseContext(), selected, Toast.LENGTH_LONG)
//                            .show();
                    fragmentClass = FreshPurchase.class;
                }


                try {
                        fragment = (Fragment) fragmentClass.newInstance();

                } catch (Exception e) {
                    e.printStackTrace();
                }

                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.mainFragment, fragment).commit();

                drawer.closeDrawer(GravityCompat.START);
                drawer.closeDrawer(Gravity.END);
                return true;
            }
        });
    }

    private void selectDashBoardFragment() {

        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.mainFragment, new DashBoard())
                .addToBackStack(null).commit();

    }

    private void createGroupList() {
        groupList = new ArrayList<String>();
        groupList.add("Mutual Funds");
        groupList.add("Mutual Funds Manual");
        groupList.add("Transact Online");
        groupList.add("My Goals");
        groupList.add("Robo Advisor");
        groupList.add("Calculator");
    }

    private void createCollection() {

        // preparing laptops collection(child)
        String[] mutual_fund_model = { "Portfolio", "Portfolio Analysis","SIP Schemes","All Transactions",
                "Month Wise Transactions","Dividend History","Tax Report","Funds Performance" };

        String[] mutual_fund_model_manual = { "Fresh Purchase", "Fresh Purchase With SIP","Additional Purchase","Redemption",
                "Systematic Withdrawal Plan","Systematic Transfer Plan","Switch","Edit Transactions" };

        String[] transact_online_model = { "Lumpsum Investment", "Purchase With Existing Folio","NFO Purchase","SIP Investment",
                "Redemption","Switch","STP","SWP" };

        String[] my_goal_model = {};
        String[] robo_advisor_model = {};
        String[] calculator_model = {};


        dataCollection = new LinkedHashMap<String, List<String>>();

        for (String groupName : groupList) {
            if (groupName.equalsIgnoreCase("Mutual Funds")) {
                loadChild(mutual_fund_model);
            } else if (groupName.equalsIgnoreCase("Mutual Funds Manual")) {
                loadChild(mutual_fund_model_manual);
            } else if (groupName.equalsIgnoreCase("Transact Online")) {
                loadChild(transact_online_model);
            } else if (groupName.equalsIgnoreCase("My Goals")) {
                loadChild(my_goal_model);
            } else if (groupName.equalsIgnoreCase("Robo Advisor")) {
                loadChild(robo_advisor_model);
            } else if(groupName.equalsIgnoreCase("calculator_model")) {
                loadChild(calculator_model);
            }

            dataCollection.put(groupName, childList);
        }
    }

    private void loadChild(String[] laptopModels) {
        childList = new ArrayList<String>();
        for (String model : laptopModels)
            childList.add(model);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        String text = "";
        if (id == R.id.nav_myprofile) {
            text = "home";
        } else if (id == R.id.nav_mydashboard) {
            selectDashBoardFragment();
        } else if (id == R.id.nav_change_password) {
            text = "pool";
        }else if (id == R.id.nav_rateus) {
            try {
                Uri marketUri = Uri.parse("market://details?id=" + getPackageName());
                Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
                startActivity(marketIntent);
            }catch(ActivityNotFoundException e) {
                Uri marketUri = Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName());
                Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
                startActivity(marketIntent);
            }
        }
        Toast.makeText(this, "You have chosen " + text, Toast.LENGTH_LONG).show();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        drawer.closeDrawer(GravityCompat.END);
        return true;
    }



}
