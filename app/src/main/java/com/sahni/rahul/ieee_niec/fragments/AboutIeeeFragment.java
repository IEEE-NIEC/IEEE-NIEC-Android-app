package com.sahni.rahul.ieee_niec.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sahni.rahul.ieee_niec.R;
import com.sahni.rahul.ieee_niec.adapter.AboutIeeePagerAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutIeeeFragment extends Fragment {


    public AboutIeeeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about_ieee, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar = view.findViewById(R.id.about_ieee_toolbar);
        TabLayout tabLayout = view.findViewById(R.id.about_ieee_tab_layout);
        ViewPager viewPager = view.findViewById(R.id.about_ieee_view_pager);

        DrawerLayout drawer = getActivity().findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActivity(), drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        toolbar.setTitle("About IEEE");

        AboutIeeePagerAdapter pagerAdapter = new AboutIeeePagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}
