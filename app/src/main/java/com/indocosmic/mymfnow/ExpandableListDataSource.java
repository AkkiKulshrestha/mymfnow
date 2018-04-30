package com.indocosmic.mymfnow;

import android.content.Context;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by msahakyan on 22/10/15.
 */
public class ExpandableListDataSource {

    /**
     * Returns fake data of films
     *
     * @param context
     * @return
     */

        public static Map<String, List<String>> getData(Context context) {

            Map<String, List<String>> expandableListData = new TreeMap<>();

            List<String> nav_drawer_labels = Arrays.asList(context.getResources().getStringArray(R.array.nav_drawer_labels));

            List<String> mutual_fund = Arrays.asList(context.getResources().getStringArray(R.array.elements_mutual_funds));
            List<String> mutual_fund_manual = Arrays.asList(context.getResources().getStringArray(R.array.elements_mutual_fund_manual));
            List<String> transact_online = Arrays.asList(context.getResources().getStringArray(R.array.elements_transact_online));
            List<String> my_goal = Arrays.asList(context.getResources().getStringArray(R.array.elements_my_goal));
            List<String> robo_advisor = Arrays.asList(context.getResources().getStringArray(R.array.elements_robo_advisor));
            List<String> calculator = Arrays.asList(context.getResources().getStringArray(R.array.elements_calculator));

            expandableListData.put(nav_drawer_labels.get(0), mutual_fund);
            expandableListData.put(nav_drawer_labels.get(1), mutual_fund_manual);
            expandableListData.put(nav_drawer_labels.get(2), transact_online);
            expandableListData.put(nav_drawer_labels.get(3), my_goal);
            expandableListData.put(nav_drawer_labels.get(4), robo_advisor);
            expandableListData.put(nav_drawer_labels.get(5), calculator);

            return expandableListData;
    }
}
