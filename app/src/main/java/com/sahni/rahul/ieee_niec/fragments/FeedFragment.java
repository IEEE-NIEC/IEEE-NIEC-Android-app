package com.sahni.rahul.ieee_niec.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.sahni.rahul.ieee_niec.R;
import com.sahni.rahul.ieee_niec.helpers.ContentUtils;
import com.sahni.rahul.ieee_niec.interfaces.OnHomeSliderInteractionListener;
import com.sahni.rahul.ieee_niec.models.Feed;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;


public class FeedFragment extends Fragment {

//    private String mImageUrl;
    private Feed mFeed;
    private String TAG = "FeedFragment";
    private OnHomeSliderInteractionListener mListener;

    public FeedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i(TAG, "onAttach: "+context.getClass());
        if(context instanceof OnHomeSliderInteractionListener){
            mListener = (OnHomeSliderInteractionListener) context;
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i(TAG, "onDetach");
        mListener = null;
    }

    public static FeedFragment newInstance(Feed feed) {

        Bundle args = new Bundle();
        args.putParcelable(ContentUtils.FEED_KEY, feed);
        FeedFragment fragment = new FeedFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFeed = getArguments().getParcelable(ContentUtils.FEED_KEY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_slider, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView imageView = view.findViewById(R.id.home_slider_image_view);
        final ProgressBar progressBar = view.findViewById(R.id.home_slider_progress);
//        progressBar.getIndeterminateDrawable().setColorFilter(Color.WHITE, android.graphics.PorterDuff.Mode.SRC_IN);

        final String imageUrl = mFeed.getmFeedImageUrl();

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onHomeSliderInteraction(imageUrl);
            }
        });

        if(imageUrl != null) {
            Picasso.with(getActivity())
                    .load(imageUrl)
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
    }
}
