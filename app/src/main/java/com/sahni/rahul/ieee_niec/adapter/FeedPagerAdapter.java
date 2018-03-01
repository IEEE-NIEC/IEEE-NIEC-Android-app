package com.sahni.rahul.ieee_niec.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.sahni.rahul.ieee_niec.fragments.FeedFragment;
import com.sahni.rahul.ieee_niec.fragments.NoFeedFragment;
import com.sahni.rahul.ieee_niec.models.Feed;

import java.util.ArrayList;

/**
 * Created by sahni on 29-Sep-17.
 */

public class FeedPagerAdapter extends FragmentStatePagerAdapter {

    private static final String TAG = "FeedPagerAdapter";
    private ArrayList<Feed> mFeedArrayList;

    public FeedPagerAdapter(FragmentManager fm, ArrayList<Feed> feedArrayList) {
        super(fm);
        this.mFeedArrayList = feedArrayList;
    }

    @Override
    public Fragment getItem(int position) {
        if(position == mFeedArrayList.size()){
            return NoFeedFragment.newInstance();
        } else {
            return FeedFragment.newInstance(mFeedArrayList.get(position));
        }
    }

    @Override
    public int getCount() {

        return mFeedArrayList.isEmpty()? 1 : mFeedArrayList.size()+1;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
