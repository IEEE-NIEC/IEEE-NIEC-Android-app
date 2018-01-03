package com.sahni.rahul.ieee_niec.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
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
import com.sahni.rahul.ieee_niec.interfaces.OnSearchUserResultClickListener;


public class SearchUserFragment extends Fragment implements OnSearchUserResultClickListener {

    private static final String TAG = "SearchUserFragment";
    private ViewPager mViewPager;
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

        mViewPager = view.findViewById(R.id.search_user_view_pager);
        mPagerAdapter = new SearchUserPagerAdapter(getChildFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);
        tabLayout.setupWithViewPager(mViewPager);

        final EditText searchEditText = view.findViewById(R.id.search_edit_text);
        ImageView hamburgerIcon = view.findViewById(R.id.hamburger_icon);

        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    Log.i(TAG, "onEditorAction: query = " + textView.getText().toString());
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


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnSearchUserFragmentInteractionListener) {
//            mListener = (OnSearchUserFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString() + " must implement OnSearchUserFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        mListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i(TAG, "onDestroyView");

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, "onStart");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(TAG, "onSaveInstanceState");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG, "onStop");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.i(TAG, "onViewStateRestored");
    }

    @Override
    public void onSearchUserResultClicked(View view) {
//        int position = mUserRecyclerView.getChildAdapterPosition(view);
//        User user = mUserArrayList.get(position);
//        mListener.onSearchUserFragmentInteraction(user);
    }


}


