package com.sahni.rahul.ieee_niec.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.sahni.rahul.ieee_niec.fragments.ImageSliderFragment;

import java.util.ArrayList;

/**
 * Created by sahni on 28-Aug-17.
 */

public class ImageSliderPagerAdapter extends FragmentStatePagerAdapter {

    private static final String TAG = "ImageSliderPagerAdapter";
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

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        return super.instantiateItem(container, position);

    }
}
