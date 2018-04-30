package com.indocosmic.mymfnow.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.indocosmic.mymfnow.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PortfolioAnalysis extends Fragment {


    public PortfolioAnalysis() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_portfolio_analysis, container, false);
    }

}
