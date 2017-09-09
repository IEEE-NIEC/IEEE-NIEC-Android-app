package com.sahni.rahul.ieee_niec.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

/**
 * Created by sahni on 28-Aug-17.
 */

public class ImageSliderPagerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<String> mArrayList;


    public ImageSliderPagerAdapter(FragmentManager fm, ArrayList<String> arrayList) {
        super(fm);
        this.mArrayList = arrayList;
    }

    @Override
    public Fragment getItem(int position) {
        return ImageSliderFragment.newInstance(mArrayList.get(position));
    }

    @Override
    public int getCount() {
        return mArrayList.size();
    }
}
