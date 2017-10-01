package com.sahni.rahul.ieee_niec.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sahni.rahul.ieee_niec.R;

public class NoFeedFragment extends Fragment {


    public NoFeedFragment() {
        // Required empty public constructor
    }

    public static NoFeedFragment newInstance() {
        return new NoFeedFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_no_feed, container, false);
    }

}
