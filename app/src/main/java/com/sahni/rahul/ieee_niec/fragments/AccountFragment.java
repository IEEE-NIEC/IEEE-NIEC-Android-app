package com.sahni.rahul.ieee_niec.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.sahni.rahul.ieee_niec.R;
import com.sahni.rahul.ieee_niec.adapter.AccountPagerAdapter;
import com.sahni.rahul.ieee_niec.helpers.ContentUtils;
import com.sahni.rahul.ieee_niec.models.User;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment implements BottomNavigationView.OnNavigationItemSelectedListener{


    private static final String TAG = "AccountFragment";
    private User mUser;
    private ViewPager viewPager;
    private AccountPagerAdapter pagerAdapter;

    public AccountFragment() {
        // Required empty public constructor
    }

    public static AccountFragment newInstance(User user) {

        Bundle args = new Bundle();
        args.putParcelable(ContentUtils.USER_KEY, user);
        AccountFragment fragment = new AccountFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUser = getArguments().getParcelable(ContentUtils.USER_KEY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account_fragmnet, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final BottomNavigationView mBottomNavigationView = view.findViewById(R.id.account_navigation);
        final Menu bottomMenu = mBottomNavigationView.getMenu();
        viewPager = view.findViewById(R.id.account_viewpager);
        pagerAdapter = new AccountPagerAdapter(getChildFragmentManager(), mUser);
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
//                MenuItem prevMenuItem = bottomMenu.getItem(position);
//                if (prevMenuItem != null) {
//                    prevMenuItem.setChecked(true);
//                }
//                else
//                {
//                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
//                }
//
//                bottomNavigationView.getMenu().getItem(position).setChecked(true);


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        mBottomNavigationView.setOnNavigationItemSelectedListener(this);
        mBottomNavigationView.setSelectedItemId(R.id.profile);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.profile:
                Log.i(TAG, "onNavigationItemSelected");
                if(viewPager.getCurrentItem() != 0) {
                    viewPager.setCurrentItem(0, true);
                }

                break;
            case R.id.search:
                if(viewPager.getCurrentItem() != 1){
                    viewPager.setCurrentItem(1, true);
                }

        }
        return true;
    }


}
