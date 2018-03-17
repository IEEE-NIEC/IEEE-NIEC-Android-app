package com.sahni.rahul.ieee_niec.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.sahni.rahul.ieee_niec.R;
import com.sahni.rahul.ieee_niec.helpers.ContentUtils;
import com.sahni.rahul.ieee_niec.models.Feed;

public class ShowFeedImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_feed_image);

        final Intent intent = getIntent();
//        String imageUrl = getIntent().getStringExtra(ContentUtils.IMAGE_URL_KEY);
        Feed feed = intent.getParcelableExtra(ContentUtils.FEED_KEY);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("");
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        final ImageView imageView = findViewById(R.id.show_feed_image_view);
        final ProgressBar progressBar = findViewById(R.id.show_feed_progress);

        ConstraintLayout layout = findViewById(R.id.feed_bottom_sheet);
        final FrameLayout foregroundLayout = findViewById(R.id.foreground_frame_layout);
        TextView feedDetailsTextView = findViewById(R.id.feed_details_text_view);
        TextView feedTitleTextView = findViewById(R.id.feed_title);
        TextView registerTextView = findViewById(R.id.register_text_view);

        feedTitleTextView.setText(feed.getFeedTitle());
        feedDetailsTextView.setText(feed.getFeedDetails());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            postponeEnterTransition();
        }

        final String registerUrl = feed.getRegisterUrl();
        if(registerUrl == null || registerUrl.equals("")){
            registerTextView.setVisibility(View.GONE);
        } else {
            registerTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri registerUri = Uri.parse(registerUrl);
                    Intent webIntent = new Intent(Intent.ACTION_VIEW, registerUri);
                    if(webIntent.resolveActivity(getPackageManager()) != null){
                        startActivity(webIntent);
                    }
                }
            });
        }

        final BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(layout);
        bottomSheetBehavior.setPeekHeight(ContentUtils.convertDpToPixel(45, this));
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                if(slideOffset > 0){
                    foregroundLayout.setBackgroundColor(ContextCompat.getColor(ShowFeedImageActivity.this,
                            R.color.cardview_shadow_start_color));
                } else {
                    foregroundLayout.setBackgroundColor(ContextCompat.getColor(ShowFeedImageActivity.this,
                            android.R.color.transparent));
                }
            }
        });

        RequestBuilder<Drawable> requestBuilder = Glide.with(this)
                .load(feed.getFeedImageUrl());

        requestBuilder.listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                progressBar.setVisibility(View.GONE);
                imageView.setImageResource(R.drawable.place);
                return true;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                progressBar.setVisibility(View.GONE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    startPostponedEnterTransition();
                }
                return false;
            }
        }).into(imageView);


    }

}
