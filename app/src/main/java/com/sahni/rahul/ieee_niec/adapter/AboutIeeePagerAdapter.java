package com.sahni.rahul.ieee_niec.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.sahni.rahul.ieee_niec.fragments.IeeeCsFragments;
import com.sahni.rahul.ieee_niec.fragments.IeeeFragment;
import com.sahni.rahul.ieee_niec.fragments.IeeePesFragment;
import com.sahni.rahul.ieee_niec.fragments.IeeeWieFragment;

/**
 * Created by sahni on 01-Oct-17.
 */

public class AboutIeeePagerAdapter extends FragmentPagerAdapter {

    private String pageTitle[] = {"IEEE", "WIE", "PES", "CS"};

    public AboutIeeePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if(position == 0){
            return new IeeeFragment();
        } else if(position == 1) {
            return new IeeeWieFragment();
        } else if(position == 2) {
            return new IeeePesFragment();
        } else {
            return new IeeeCsFragments();
        }
    }

    @Override
    public int getCount() {
        return pageTitle.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return pageTitle[position];
    }
}
