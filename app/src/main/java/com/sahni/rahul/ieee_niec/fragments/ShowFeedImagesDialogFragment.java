package com.sahni.rahul.ieee_niec.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.sahni.rahul.ieee_niec.R;
import com.sahni.rahul.ieee_niec.helpers.ContentUtils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShowFeedImagesDialogFragment extends DialogFragment {

    private String mImageUrl;


    public ShowFeedImagesDialogFragment() {
        // Required empty public constructor
    }

    public static ShowFeedImagesDialogFragment newInstance(String imageUrl) {

        Bundle args = new Bundle();
        args.putString(ContentUtils.IMAGE_URL_KEY, imageUrl);
        ShowFeedImagesDialogFragment fragment = new ShowFeedImagesDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImageUrl = getArguments().getString(ContentUtils.IMAGE_URL_KEY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_show_home_slider_image_dialog, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView imageView = view.findViewById(R.id.show_home_slider_image_view);
        final ProgressBar progressBar = view.findViewById(R.id.show_home_slider_progress);

        Picasso.with(getActivity())
                .load(mImageUrl)
                .error(R.drawable.place)
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                        progressBar.setVisibility(View.GONE);
                    }
                });

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
