package com.indocosmic.mymfnow.mutualFundManualFragmet;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.indocosmic.mymfnow.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SystematicWithdrawalPlan extends Fragment {


    public SystematicWithdrawalPlan() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_systematic_withdrawal_plan, container, false);
    }

}