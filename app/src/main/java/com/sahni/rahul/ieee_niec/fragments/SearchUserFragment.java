package com.sahni.rahul.ieee_niec.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.sahni.rahul.ieee_niec.R;
import com.sahni.rahul.ieee_niec.adapter.SearchUserPagerAdapter;


public class SearchUserFragment extends Fragment{

    private static final String TAG = "SearchUserFragment";
    private SearchUserPagerAdapter mPagerAdapter;


    public SearchUserFragment() {
        // Required empty public constructor
    }

    public static SearchUserFragment newInstance() {
        return new SearchUserFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_user, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final DrawerLayout drawer = getActivity().findViewById(R.id.drawer_layout);

        TabLayout tabLayout = view.findViewById(R.id.search_tab_layout);

        ViewPager viewPager = view.findViewById(R.id.search_user_view_pager);
        mPagerAdapter = new SearchUserPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(mPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        final EditText searchEditText = view.findViewById(R.id.search_edit_text);
        ImageView hamburgerIcon = view.findViewById(R.id.hamburger_icon);

        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    String query = textView.getText().toString();
                    if(!query.trim().equals("")){
                        for(int j = 0; j < mPagerAdapter.getCount(); j++){
                            ((SearchByNameOrInterestFragment) mPagerAdapter.fragments[j]).update(query);
                        }
                    }
                }
                return false;
            }
        });

        hamburgerIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(! drawer.isDrawerOpen(GravityCompat.START)){
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });


    }


}


