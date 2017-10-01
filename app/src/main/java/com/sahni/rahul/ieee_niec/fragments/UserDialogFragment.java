package com.sahni.rahul.ieee_niec.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sahni.rahul.ieee_niec.R;
import com.sahni.rahul.ieee_niec.adapter.InterestAdapter;
import com.sahni.rahul.ieee_niec.helpers.ContentUtils;
import com.sahni.rahul.ieee_niec.interfaces.OnUserDetailsDialogInteractionListener;
import com.sahni.rahul.ieee_niec.models.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by sahni on 10-Sep-17.
 */

public class UserDialogFragment extends DialogFragment {

    private static final String TAG = "UserDialogFragment";
    private User mUser;

    private ImageView mUserImageView;
    private TextView mNameTextView;
    private TextView mEmailTextView;
    private TextView mMobileTextView;
    private RecyclerView mInterestRecyclerView;
    private ArrayList<String> mInterestArrayList;
    private InterestAdapter mInterestAdapter;
    private OnUserDetailsDialogInteractionListener mListener;



    public static UserDialogFragment newInstance(User user) {

        Bundle args = new Bundle();
        args.putParcelable(ContentUtils.USER_KEY, user);
        UserDialogFragment fragment = new UserDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUser = getArguments().getParcelable(ContentUtils.USER_KEY);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_profile,container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Toolbar toolbar = view.findViewById(R.id.user_toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_cancel);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mListener != null){
                    mListener.onUserDetailsDialogInteraction(null, UserDialogFragment.this);
                }
            }
        });

        AppBarLayout appBarLayout = view.findViewById(R.id.user_app_bar);
        final TextView toolbarTextView = view.findViewById(R.id.toolbar_text_view);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int scrollRange = appBarLayout.getTotalScrollRange();
                if(scrollRange + verticalOffset == 0){
                    toolbarTextView.setText(mUser.getName());
                } else {
                    toolbarTextView.setText("");
                }
            }
        });

        FloatingActionButton fab = view.findViewById(R.id.edit_fab);
        fab.setVisibility(View.GONE);

        ImageView overFlowMenu = view.findViewById(R.id.overflow_menu);
        overFlowMenu.setVisibility(View.GONE);

        mUserImageView = view.findViewById(R.id.user_image_view);
        mNameTextView = view.findViewById(R.id.user_name_text_view);
        mEmailTextView = view.findViewById(R.id.user_email_text_view);
        mMobileTextView = view.findViewById(R.id.user_mobile_text_view);
        mInterestRecyclerView = view.findViewById(R.id.interest_recycler_view);
        mInterestArrayList = new ArrayList<>();
        mInterestRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        mInterestAdapter = new InterestAdapter(getActivity(), mInterestArrayList, ContentUtils.SHOW_INTEREST, null);
        mInterestRecyclerView.setAdapter(mInterestAdapter);

        displayDetails();

    }

    private void displayDetails() {
        if(mUser.getImageUrl() != null) {
            if(!mUser.getImageUrl().isEmpty()) {
                Picasso.with(getActivity())
                        .load(mUser.getImageUrl())
                        .placeholder(R.drawable.user)
                        .error(R.drawable.user)
                        .into(mUserImageView);
            }
        }


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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OnUserDetailsDialogInteractionListener){
            mListener = (OnUserDetailsDialogInteractionListener) context;
        }
    }
}
