package com.sahni.rahul.ieee_niec.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sahni.rahul.ieee_niec.R;
import com.sahni.rahul.ieee_niec.adapter.InterestAdapter;
import com.sahni.rahul.ieee_niec.helpers.ContentUtils;
import com.sahni.rahul.ieee_niec.models.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserFragment extends Fragment {

    private static final String TAG = "UserFragment";
    private User mUser;

    private ImageView mUserImageView;
    private TextView mNameTextView;
    private TextView mEmailTextView;
    private TextView mMobileTextView;
    private RecyclerView mInterestRecyclerView;
    private ArrayList<String> mInterestArrayList;
    private InterestAdapter mInterestAdapter;


    public UserFragment() {
        // Required empty public constructor
    }

    public static UserFragment newInstance(User user) {

        Bundle args = new Bundle();
        args.putParcelable(ContentUtils.USER_KEY, user);
        UserFragment fragment = new UserFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static UserFragment newInstance(){
        return new UserFragment();
    }

    public void update(User user){
        Log.i(TAG, "update:");
        mUser = user;
        displayDetails();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle != null) {
            mUser = getArguments().getParcelable(ContentUtils.USER_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_details, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        DrawerLayout drawerLayout = getActivity().findViewById(R.id.drawer_layout);
        Toolbar toolbar = view.findViewById(R.id.user_toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActivity(), drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        mUserImageView = view.findViewById(R.id.user_image_view);
        mNameTextView = view.findViewById(R.id.user_name_text_view);
        mEmailTextView = view.findViewById(R.id.user_email_text_view);
        mMobileTextView = view.findViewById(R.id.user_mobile_text_view);
        mInterestRecyclerView = view.findViewById(R.id.interest_recycler_view);
        mInterestArrayList = new ArrayList<>();
        mInterestRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        mInterestAdapter = new InterestAdapter(getActivity(), mInterestArrayList);
        mInterestRecyclerView.setAdapter(mInterestAdapter);

//        displayDetails();

    }

    private void displayDetails() {
        Picasso.with(getActivity())
                .load(mUser.getImageUrl())
                .placeholder(R.drawable.user)
                .error(R.drawable.user)
                .into(mUserImageView);

        mNameTextView.setText("" + mUser.getName());
        mEmailTextView.setText("" + mUser.getEmailId());
        mMobileTextView.setText("" + mUser.getMobileNumber());
        mInterestArrayList.clear();
        mInterestArrayList.addAll(mUser.getInterestArrayList());
//        if(mUser.getInterestArrayList().isEmpty()){
//            Log.i(TAG,)
//        }
        mInterestAdapter.notifyDataSetChanged();
    }

}
