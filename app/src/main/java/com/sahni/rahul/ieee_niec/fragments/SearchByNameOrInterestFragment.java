package com.sahni.rahul.ieee_niec.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.sahni.rahul.ieee_niec.R;
import com.sahni.rahul.ieee_niec.adapter.SearchUserAdapter;
import com.sahni.rahul.ieee_niec.helpers.ContentUtils;
import com.sahni.rahul.ieee_niec.interfaces.OnSearchUserFragmentInteractionListener;
import com.sahni.rahul.ieee_niec.interfaces.OnSearchUserResultClickListener;
import com.sahni.rahul.ieee_niec.interfaces.UpdateSearchFragmentDetails;
import com.sahni.rahul.ieee_niec.models.User;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchByNameOrInterestFragment extends Fragment implements UpdateSearchFragmentDetails, OnSearchUserResultClickListener {

    public static final String TAG = "SearchByNameOrInterest";
    private String mSearchBy;

    private RecyclerView mSearchUserRecyclerView;
    private SearchUserAdapter mSearchUserAdapter;
    private ArrayList<User> mUserArrayList;
    private ProgressBar mProgressbar;
    private TextView mHintTextView;

    private CollectionReference mUserCollection;

    private OnSearchUserFragmentInteractionListener mListener;

    public SearchByNameOrInterestFragment() {
        // Required empty public constructor
    }

    public static SearchByNameOrInterestFragment newInstance(String searchBy) {

        Bundle args = new Bundle();
        args.putString(ContentUtils.SEARCH_BY, searchBy);
        SearchByNameOrInterestFragment fragment = new SearchByNameOrInterestFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_by_name_or_interest, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSearchBy = getArguments().getString(ContentUtils.SEARCH_BY);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mUserCollection = FirebaseFirestore.getInstance().collection("users");

        mHintTextView = view.findViewById(R.id.search_user_hint_text_view);
        mProgressbar = view.findViewById(R.id.search_user_progress_bar);
        mProgressbar.setVisibility(View.INVISIBLE);
        if (mSearchBy.equals(ContentUtils.SEARCH_BY_NAME)) {
            mHintTextView.setText("Search users by name");
        } else {
            mHintTextView.setText("Search users by interest");
        }
        mUserArrayList = new ArrayList<>();
        mSearchUserRecyclerView = view.findViewById(R.id.search_user_recycler_view);
        mSearchUserAdapter = new SearchUserAdapter(getContext(), mUserArrayList, this);
        mSearchUserRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mSearchUserRecyclerView.setAdapter(mSearchUserAdapter);

    }

    @Override
    public void update(final String query) {
        mUserArrayList.clear();
        mSearchUserAdapter.notifyDataSetChanged();
        mProgressbar.setVisibility(View.VISIBLE);
        mHintTextView.setVisibility(View.INVISIBLE);

        if (mSearchBy.equals(ContentUtils.SEARCH_BY_INTEREST)) {
            mUserCollection.whereEqualTo("interestMap." + query, true)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {

                        @Override
                        public void onSuccess(QuerySnapshot documentSnapshots) {
                            mProgressbar.setVisibility(View.INVISIBLE);
                            mHintTextView.setVisibility(View.VISIBLE);
                            if (documentSnapshots.isEmpty()) {
                                mHintTextView.setText("No user found");
                            } else {
                                mHintTextView.setVisibility(View.INVISIBLE);
                                for (DocumentSnapshot document : documentSnapshots.getDocuments()) {
                                    User user = document.toObject(User.class);
                                    mUserArrayList.add(user);
                                }
                                mSearchUserAdapter.notifyDataSetChanged();


                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.i(TAG, "update: onFailure: " + e.getMessage());
                            mProgressbar.setVisibility(View.INVISIBLE);
                            mHintTextView.setVisibility(View.VISIBLE);
                            mHintTextView.setText("Oops! , there was a problem from our side. Please try again later.");
                        }
                    });
        } else {
            mUserCollection.whereGreaterThanOrEqualTo("name", query)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {

                        @Override
                        public void onSuccess(QuerySnapshot documentSnapshots) {
                            mProgressbar.setVisibility(View.INVISIBLE);
                            mHintTextView.setVisibility(View.VISIBLE);
                            if (documentSnapshots.isEmpty()) {
                                mHintTextView.setText("No user found");
                            } else {
                                mHintTextView.setVisibility(View.INVISIBLE);
                                for (DocumentSnapshot document : documentSnapshots.getDocuments()) {
                                    User user = document.toObject(User.class);
                                    if (user.getName().contains(query)) {
                                        mUserArrayList.add(user);
                                    }
                                }
                                Log.i(TAG, "update: " + mUserArrayList.size());
                                if (mUserArrayList.isEmpty()) {
                                    mHintTextView.setVisibility(View.VISIBLE);
                                    mHintTextView.setText("No user found");
                                } else {
                                    mSearchUserAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.i(TAG, "update: onFailure: " + e.getMessage());
                        }
                    });

        }
    }

    @Override
    public void onSearchUserResultClicked(View view) {
        int position = mSearchUserRecyclerView.getChildAdapterPosition(view);
        User user = mUserArrayList.get(position);
        if(mListener != null){
            mListener.onSearchUserFragmentInteraction(user);
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i(TAG, "onAttach: "+context.toString());
        if(context instanceof OnSearchUserFragmentInteractionListener){
            mListener = (OnSearchUserFragmentInteractionListener) context;
        } else {
            Log.w(TAG, "onAttach: "+context.toString()+ " doesn't implement OnSearchUserFragmentInteractionListener");
        }
    }

}
