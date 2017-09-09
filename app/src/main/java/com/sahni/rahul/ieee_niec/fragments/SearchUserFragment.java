package com.sahni.rahul.ieee_niec.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lapism.searchview.SearchView;
import com.sahni.rahul.ieee_niec.R;
import com.sahni.rahul.ieee_niec.adapter.SearchUserAdapter;
import com.sahni.rahul.ieee_niec.models.User;

import java.util.ArrayList;


public class SearchUserFragment extends Fragment {

    private static final String TAG = "SearchUserFragment";
    private RecyclerView mUserRecyclerView;
    private SearchUserAdapter mUserAdapter;
    private ArrayList<User> mUserArrayList;
    private TextView mHintTextView;
    private ProgressBar mProgressBar;


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


//        final SearchHistoryTable mHistoryDatabase = new SearchHistoryTable(getActivity());

        final DrawerLayout drawer = getActivity().findViewById(R.id.drawer_layout);
        mProgressBar = view.findViewById(R.id.progress_bar);
        mProgressBar.setVisibility(View.INVISIBLE);
        mHintTextView = view.findViewById(R.id.hint_text_view);
        mUserRecyclerView = view.findViewById(R.id.user_recycler_view);
        mUserArrayList = new ArrayList<>();
        mUserAdapter = new SearchUserAdapter(getActivity(), mUserArrayList);
        mUserRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mUserRecyclerView.setAdapter(mUserAdapter);


        final SearchView mSearchView = view.findViewById(R.id.searchView); // from API 26
        if (mSearchView != null) {
//            mSearchView.setVersionMargins(SearchView.VersionMargins.TOOLBAR_SMALL);
            mSearchView.setHint("Search");
            mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    Log.i(TAG, "onQueryTextSubmit: " + query);
//                    mHistoryDatabase.addItem(new SearchItem(query));
                    mSearchView.close(false);
                    mHintTextView.setVisibility(View.GONE);
                    mProgressBar.setVisibility(View.VISIBLE);
                    fetchDetails(query);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });

            mSearchView.setNavigationIconClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i(TAG, "setNavigationIconClickListener:");
                    if (!drawer.isDrawerOpen(GravityCompat.START)) {
                        drawer.openDrawer(GravityCompat.START);
//                        mSearchView.clo

                    }
                }
            });


            mSearchView.setOnOpenCloseListener(new SearchView.OnOpenCloseListener() {
                @Override
                public boolean onOpen() {
//                    if (mFab != null) {
//                        new F.hide();
//                    }
                    return true;
                }

                @Override
                public boolean onClose() {
//                    if (mFab != null && !mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
//                        mFab.show();
//                    }
                    return true;
                }
            });
//
        }


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

    private void fetchDetails(String query) {

    }

    public void update(){
        Log.i(TAG, "update()");
    }
}


