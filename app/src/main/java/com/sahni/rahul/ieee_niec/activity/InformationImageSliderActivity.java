package com.sahni.rahul.ieee_niec.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.sahni.rahul.ieee_niec.R;
import com.sahni.rahul.ieee_niec.custom.ZoomOutPageTransformer;
import com.sahni.rahul.ieee_niec.adapter.ImageSliderPagerAdapter;
import com.sahni.rahul.ieee_niec.helpers.ContentUtils;
import com.sahni.rahul.ieee_niec.models.Information;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator;

public class InformationImageSliderActivity extends AppCompatActivity {

    private static final String TAG ="InfoImageActivity";
    private ArrayList<String> mArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_image_slider);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setTitle("");
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Information information = getIntent().getParcelableExtra(ContentUtils.INFO_KEY);
        if (information != null) {
            for(String url : information.getImageList()){
                Log.i(TAG, "Url ="+url);
                if(url != null){
                    mArrayList.add(url);
                }
            }
        }

        ViewPager viewPager = findViewById(R.id.image_slider_view_pager);
        CircleIndicator circleIndicator = findViewById(R.id.circle_indicator);
        ImageSliderPagerAdapter pagerAdapter = new ImageSliderPagerAdapter(getSupportFragmentManager(), mArrayList);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        circleIndicator.setViewPager(viewPager);
        pagerAdapter.registerDataSetObserver(circleIndicator.getDataSetObserver());
        ContentUtils.syncIndicatorWithViewPager(viewPager, circleIndicator);


    }

}
