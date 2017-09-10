package com.sahni.rahul.ieee_niec.fragments;


import android.content.Context;
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
import com.sahni.rahul.ieee_niec.helpers.ContentUtils;
import com.sahni.rahul.ieee_niec.interfaces.OnSearchUserFragmentInteractionListener;
import com.sahni.rahul.ieee_niec.interfaces.OnSearchUserResultClickListener;
import com.sahni.rahul.ieee_niec.models.User;
import com.sahni.rahul.ieee_niec.models.UserStatus;
import com.sahni.rahul.ieee_niec.networking.ApiService;
import com.sahni.rahul.ieee_niec.networking.NetworkingUtils;
import com.sahni.rahul.ieee_niec.networking.RetrofitClient;
import com.sahni.rahul.ieee_niec.networking.SearchResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SearchUserFragment extends Fragment implements OnSearchUserResultClickListener{

    private static final String TAG = "SearchUserFragment";
    private RecyclerView mUserRecyclerView;
    private SearchUserAdapter mUserAdapter;
    private ArrayList<User> mUserArrayList;
    private TextView mHintTextView;
    private ProgressBar mProgressBar;
    private OnSearchUserFragmentInteractionListener mListener;


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
        mUserAdapter = new SearchUserAdapter(getActivity(), mUserArrayList, this);
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
                    mUserArrayList.clear();
                    mSearchView.close(false);
                    mHintTextView.setVisibility(View.GONE);
                    mProgressBar.setVisibility(View.VISIBLE);
                    searchForUsers(query);
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

    private void searchForUsers(String query) {
        RetrofitClient.getInstance()
                .create(ApiService.class)
                .searchForUsers(query)
                .enqueue(new Callback<SearchResponse>() {
                    @Override
                    public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                        if (response.isSuccessful()) {
                            mProgressBar.setVisibility(View.GONE);
                            SearchResponse searchResponse = response.body();
                            displayDetails(searchResponse);
                        }
                    }

                    @Override
                    public void onFailure(Call<SearchResponse> call, Throwable t) {
                        mProgressBar.setVisibility(View.GONE);
                        Log.i(TAG, "onFailure: " + t.getMessage());

                    }
                });
    }

    private void displayDetails(SearchResponse searchResponse) {
        if (searchResponse.getCode() == NetworkingUtils.USER_FOUND) {
            ArrayList<UserStatus.TempUser> arrayList = searchResponse.getTempUserArrayList();
            ArrayList<User> userArrayList = new ArrayList<>();
            for (UserStatus.TempUser tempUser : arrayList) {

                User user = new User(
                        tempUser.getName(),
                        tempUser.getImageUrl(),
                        tempUser.getEmailId(),
                        tempUser.getMobileNumber(),
                        ContentUtils.getInterestArrayList(tempUser.getInterest()),
                        tempUser.getUserId()
                );
                userArrayList.add(user);
            }
            mHintTextView.setVisibility(View.GONE);
            mUserArrayList.addAll(userArrayList);
            mUserAdapter.notifyDataSetChanged();
        } else {
            mHintTextView.setVisibility(View.VISIBLE);
            mHintTextView.setText("No user found!");
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSearchUserFragmentInteractionListener) {
            mListener = (OnSearchUserFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnSearchUserFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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

    private void fetchDetails(String query) {

    }

    public void update() {
        Log.i(TAG, "update()");
    }

    @Override
    public void onSearchUserResultClicked(View view) {
        int position = mUserRecyclerView.getChildAdapterPosition(view);
        User user = mUserArrayList.get(position);
        mListener.onSearchUserFragmentInteraction(user);
    }


}


