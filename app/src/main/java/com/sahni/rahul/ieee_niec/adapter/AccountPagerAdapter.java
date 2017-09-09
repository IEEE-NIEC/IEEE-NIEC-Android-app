package com.sahni.rahul.ieee_niec.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.sahni.rahul.ieee_niec.fragments.SearchUserFragment;
import com.sahni.rahul.ieee_niec.fragments.UserFragment;

/**
 * Created by sahni on 05-Sep-17.
 */

public class AccountPagerAdapter extends FragmentPagerAdapter {

    private static final int FRAGMENT_COUNT = 2;

    private Fragment fragments[] = new Fragment[FRAGMENT_COUNT];

    public AccountPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if(position == 0){
            return UserFragment.newInstance();
        } else {
           return SearchUserFragment.newInstance();
        }
    }

    @Override
    public int getCount() {
        return FRAGMENT_COUNT;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment currentFragment = (Fragment) super.instantiateItem(container, position);
        fragments[position] = currentFragment;
        return currentFragment;
    }

    public Fragment[] getFragments() {
        return fragments;
    }
}
