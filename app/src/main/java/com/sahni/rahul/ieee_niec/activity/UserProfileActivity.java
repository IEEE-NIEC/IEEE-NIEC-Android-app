package com.sahni.rahul.ieee_niec.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sahni.rahul.ieee_niec.R;
import com.sahni.rahul.ieee_niec.adapter.InterestAdapter;
import com.sahni.rahul.ieee_niec.helpers.ContentUtils;
import com.sahni.rahul.ieee_niec.models.User;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import io.github.yavski.fabspeeddial.FabSpeedDial;

public class UserProfileActivity extends AppCompatActivity {

    private static final String TAG = "UserProfileActivity";
    private User mUser;

    private ImageView mUserImageView;
    private TextView mEmailTextView;
    private TextView mMobileTextView;
    private ArrayList<String> mInterestArrayList;
    private InterestAdapter mInterestAdapter;
    private ProgressBar mImageProgressBar;
    private TextView mAboutTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        mUser = getIntent().getParcelableExtra(ContentUtils.USER_KEY);

        Toolbar toolbar = findViewById(R.id.user_toolbar);
        toolbar.setTitle(mUser.getName());
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mUserImageView = findViewById(R.id.user_image_view);
//        mNameTextView = view.findViewById(R.id.user_name_text_view);
        mEmailTextView = findViewById(R.id.user_email_text_view);
        mMobileTextView = findViewById(R.id.user_mobile_text_view);
        mAboutTextView = findViewById(R.id.about_text_view);
        mImageProgressBar = findViewById(R.id.image_progress_bar);

        RecyclerView interestRecyclerView = findViewById(R.id.interest_recycler_view);
        mInterestArrayList = new ArrayList<>();
        interestRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mInterestAdapter = new InterestAdapter(this, mInterestArrayList, ContentUtils.SHOW_INTEREST, null);
        interestRecyclerView.setAdapter(mInterestAdapter);

        FabSpeedDial fabSpeedDial = findViewById(R.id.fab_speed_dial);
        fabSpeedDial.setVisibility(View.GONE);

        displayDetails();

        mEmailTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:"+mUser.getEmailId()));
                if(intent.resolveActivity(getPackageManager()) != null){
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
                if(intent.resolveActivity(getPackageManager()) != null){
                    startActivity(intent);
                }
            }
        });


    }

    private void displayDetails() {
        Picasso.with(this)
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


}
