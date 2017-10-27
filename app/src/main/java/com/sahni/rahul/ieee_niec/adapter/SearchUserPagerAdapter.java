package com.sahni.rahul.ieee_niec.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.sahni.rahul.ieee_niec.fragments.SearchByNameOrInterestFragment;
import com.sahni.rahul.ieee_niec.helpers.ContentUtils;

/**
 * Created by sahni on 06-Oct-17.
 */

public class SearchUserPagerAdapter extends FragmentPagerAdapter {

    private String tabTitle[] = {"By name", "By interest"};
    public Fragment fragments[] = new Fragment[tabTitle.length];

    public SearchUserPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return position == 0 ? SearchByNameOrInterestFragment.newInstance(ContentUtils.SEARCH_BY_NAME)
                : SearchByNameOrInterestFragment.newInstance(ContentUtils.SEARCH_BY_INTEREST);
    }

    @Override
    public int getCount() {
        return tabTitle.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitle[position];
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment currentFragment = (Fragment) super.instantiateItem(container, position);
        fragments[position] = currentFragment;
        return currentFragment;
    }
}
