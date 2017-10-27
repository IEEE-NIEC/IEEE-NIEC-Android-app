package com.sahni.rahul.ieee_niec.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sahni.rahul.ieee_niec.R;
import com.sahni.rahul.ieee_niec.adapter.InterestAdapter;
import com.sahni.rahul.ieee_niec.helpers.ContentUtils;
import com.sahni.rahul.ieee_niec.interfaces.OnUserDetailsDialogInteractionListener;
import com.sahni.rahul.ieee_niec.models.FirestoreUser;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import io.github.yavski.fabspeeddial.FabSpeedDial;

/**
 * Created by sahni on 10-Sep-17.
 */

public class UserDialogFragment extends DialogFragment {

    private static final String TAG = "UserDialogFragment";
    private FirestoreUser mUser;

    private ImageView mUserImageView;
    private TextView mEmailTextView;
    private TextView mMobileTextView;
    private RecyclerView mInterestRecyclerView;
    private ArrayList<String> mInterestArrayList;
    private InterestAdapter mInterestAdapter;
    private OnUserDetailsDialogInteractionListener mListener;
    private ProgressBar mImageProgressBar;
    private TextView mAboutTextView;


    public static UserDialogFragment newInstance(FirestoreUser user) {

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
        return inflater.inflate(R.layout.fragment_user_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Toolbar toolbar = view.findViewById(R.id.user_toolbar);
        toolbar.setTitle(mUser.getName());
        mImageProgressBar = view.findViewById(R.id.image_progress_bar);
        toolbar.setNavigationIcon(R.drawable.ic_cancel);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onUserDetailsDialogInteraction(null, UserDialogFragment.this);
                }
            }
        });


        mUserImageView = view.findViewById(R.id.user_image_view);
//        mNameTextView = view.findViewById(R.id.user_name_text_view);
        mEmailTextView = view.findViewById(R.id.user_email_text_view);
        mMobileTextView = view.findViewById(R.id.user_mobile_text_view);
        mAboutTextView = view.findViewById(R.id.about_text_view);

        mInterestRecyclerView = view.findViewById(R.id.interest_recycler_view);
        mInterestArrayList = new ArrayList<>();
        mInterestRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        mInterestAdapter = new InterestAdapter(getActivity(), mInterestArrayList, ContentUtils.SHOW_INTEREST, null);
        mInterestRecyclerView.setAdapter(mInterestAdapter);

        FabSpeedDial fabSpeedDial = view.findViewById(R.id.fab_speed_dial);
        fabSpeedDial.setVisibility(View.GONE);

        displayDetails();

        mEmailTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:"+mUser.getEmailId()));
                if(intent.resolveActivity(getActivity().getPackageManager()) != null){
                    startActivity(intent);
                } else{
                    Snackbar.make(mEmailTextView, "Please install email app to continue", BaseTransientBottomBar.LENGTH_INDEFINITE)
                            .setAction("DISMISS", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                }
                            }).show();
                }
            }
        });

        mMobileTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+ mUser.getMobileNo()));
                if(intent.resolveActivity(getActivity().getPackageManager()) != null){
                    startActivity(intent);
                }
            }
        });

    }

    private void displayDetails() {
        Picasso.with(getActivity())
                .load(mUser.getImageUrl())
                .into(mUserImageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        mImageProgressBar.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onError() {
                        Log.i(TAG, "displayDetails: error loading image");
                        mImageProgressBar.setVisibility(View.INVISIBLE);
                        mUserImageView.setImageResource(R.drawable.user);
                    }
                });


//        mNameTextView.setText("" + mUser.getName());
        mEmailTextView.setText("" + mUser.getEmailId());
        mMobileTextView.setText("" + mUser.getMobileNo());
        mInterestArrayList.clear();
        mInterestArrayList.addAll(ContentUtils.getArrayListFromMap(mUser.getInterestMap()));
//        if(mUser.getInterestArrayList().isEmpty()){
//            Log.i(TAG,)
//        }
        mInterestAdapter.notifyDataSetChanged();
        mAboutTextView.setText(mUser.getAbout());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnUserDetailsDialogInteractionListener) {
            mListener = (OnUserDetailsDialogInteractionListener) context;
        }
    }

    @Override
    public int getTheme() {
        return R.style.DialogFragmentTheme;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow()
                .getAttributes().windowAnimations = R.style.DialogFragmentTheme;
    }
}
