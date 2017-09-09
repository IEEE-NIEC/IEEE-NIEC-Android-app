package com.sahni.rahul.ieee_niec.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sahni.rahul.ieee_niec.R;
import com.sahni.rahul.ieee_niec.adapter.ImageSliderPagerAdapter;
import com.sahni.rahul.ieee_niec.helpers.ContentUtils;
import com.sahni.rahul.ieee_niec.models.Information;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImageSliderBottomSheetFragment extends BottomSheetDialogFragment {

    private ArrayList<String> mArrayList;


    public ImageSliderBottomSheetFragment() {
        // Required empty public constructor
    }

    public static ImageSliderBottomSheetFragment newInstance(Information info){
        ImageSliderBottomSheetFragment fragment = new ImageSliderBottomSheetFragment();
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
            mArrayList = info.getImageUrlArrayList();
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
        ViewPager viewPager = view.findViewById(R.id.image_slider_view_pager);
        ImageSliderPagerAdapter pagerAdapter = new ImageSliderPagerAdapter(getChildFragmentManager(), mArrayList);
        viewPager.setAdapter(pagerAdapter);
    }
}
