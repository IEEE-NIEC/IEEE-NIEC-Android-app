package com.sahni.rahul.ieee_niec.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sahni.rahul.ieee_niec.R;
import com.sahni.rahul.ieee_niec.adapter.ImageSliderPagerAdapter;
import com.sahni.rahul.ieee_niec.helpers.ContentUtils;
import com.sahni.rahul.ieee_niec.models.Information;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator;

/**
 * A simple {@link Fragment} subclass.
 */
public class InformationImageSliderFragment extends DialogFragment {

    private static final String TAG = "ImageBottomFragment";
    private ArrayList<String> mArrayList = new ArrayList<>();


    public InformationImageSliderFragment() {
        // Required empty public constructor
    }

    public static InformationImageSliderFragment newInstance(Information info){
        InformationImageSliderFragment fragment = new InformationImageSliderFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ContentUtils.INFO_KEY, info);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        Information info = bundle.getParcelable(ContentUtils.INFO_KEY);

        if (info != null) {
            for(String url : info.getImageUrlArrayList()){
                Log.i(TAG, "Url ="+url);
                if(url != null){
                    mArrayList.add(url);
                }
            }
//            mArrayList = info.getImageUrlArrayList();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_image_slider_bottom_sheet, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i(TAG,"onViewCreated: size = "+mArrayList.size());
        ViewPager viewPager = view.findViewById(R.id.image_slider_view_pager);
        CircleIndicator circleIndicator = view.findViewById(R.id.circle_indicator);
        ImageSliderPagerAdapter pagerAdapter = new ImageSliderPagerAdapter(getChildFragmentManager(), mArrayList);
        viewPager.setAdapter(pagerAdapter);
        circleIndicator.setViewPager(viewPager);
        pagerAdapter.registerDataSetObserver(circleIndicator.getDataSetObserver());
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
