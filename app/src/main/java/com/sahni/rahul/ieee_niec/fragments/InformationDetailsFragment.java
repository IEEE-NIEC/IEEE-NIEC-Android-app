package com.sahni.rahul.ieee_niec.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sahni.rahul.ieee_niec.R;
import com.sahni.rahul.ieee_niec.glide.GlideApp;
import com.sahni.rahul.ieee_niec.helpers.ContentUtils;
import com.sahni.rahul.ieee_niec.interfaces.OnInfoDetailsFragmentInteractionListener;
import com.sahni.rahul.ieee_niec.interfaces.OnInfoFragmentInteractionListener;
import com.sahni.rahul.ieee_niec.models.Information;

/**
 * A simple {@link Fragment} subclass.
 */
public class InformationDetailsFragment extends Fragment {

    public static final String TAG = "InformationDetailsFrag";
    private Information mInfo;
    private OnInfoDetailsFragmentInteractionListener mListener;

    public InformationDetailsFragment() {
        // Required empty public constructor
    }

    public static InformationDetailsFragment newInstance(Information info){
        InformationDetailsFragment fragment = new InformationDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ContentUtils.INFO_KEY, info);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle != null){
            mInfo = bundle.getParcelable(ContentUtils.INFO_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_info_details, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar = view.findViewById(R.id.info_details_toolbar);
        DrawerLayout drawerLayout = getActivity().findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActivity(), drawerLayout, toolbar,R.string.navigation_drawer_open, R.string.navigation_drawer_close
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

//        toolbar.setTitle(mInfo.getTitle());

        AppBarLayout appBarLayout = view.findViewById(R.id.info_details_appbar);
//        appBarLayout.setExpanded(false);
        final CollapsingToolbarLayout toolbarLayout = view.findViewById(R.id.info_details_collapsing_toolbar);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                Log.d(TAG, "onOffsetChanged: vertical offset="+verticalOffset);
                int scrollRange = appBarLayout.getTotalScrollRange();
                if(scrollRange + verticalOffset == 0){
                    toolbarLayout.setTitle(mInfo.getTitle());
                } else {
                    toolbarLayout.setTitle("");
                }
            }
        });

        ImageView imageView = view.findViewById(R.id.info_details_image_view);
        TextView titleTextView = view.findViewById(R.id.info_details_title_text_view);
        TextView descriptionTextView = view.findViewById(R.id.info_details_description_text_view);

        if(mInfo.getImageUrlArrayList() != null){
            String firstImageUrl = mInfo.getImageUrlArrayList().get(0);
            if(firstImageUrl != null &&
                    (!firstImageUrl.equals("null"))) {
                imageView.setVisibility(View.VISIBLE);
                GlideApp.with(this)
                        .load(mInfo.getImageUrlArrayList().get(0))
                        .placeholder(R.drawable.place)
                        .error(R.drawable.place)
                        .into(imageView);
            } else {
//                appBarLayout.scrollTo(0,0);
                Log.d(TAG, "scroll range:"+appBarLayout.getTotalScrollRange());
//                appBarLayout.setExpanded(false);
                toolbar.setTitle(mInfo.getTitle());
//                toolbarLayout.setTitle(mInfo.getTitle());
                imageView.setVisibility(View.GONE);
//                appBarLayout.setExpanded(false);
            }
        } else {
            Log.d(TAG, "scroll range:"+appBarLayout.getTotalScrollRange());
//            appBarLayout.scrollTo(0,0);
//            appBarLayout.setExpanded(false);
            toolbar.setTitle(mInfo.getTitle());
            imageView.setVisibility(View.GONE);
            toolbar.setTitle(mInfo.getTitle());
//            toolbarLayout.setTitle(mInfo.getTitle());
//            appBarLayout.setExpanded(false);
        }
        titleTextView.setText(mInfo.getTitle());
        String description = mInfo.getDescription();
        description = ContentUtils.formatString(description);
        if(description.contains("\\n")){
            Log.d(TAG, "contains endl \\n ");
        }
        descriptionTextView.setText(description);
        Log.i(TAG, "desc: "+mInfo.getDescription());

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mListener != null){
                    mListener.onInfoDetailsInteraction(mInfo);
                }

            }
        });
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OnInfoDetailsFragmentInteractionListener){
            mListener = (OnInfoDetailsFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()+" must implement " +
                    OnInfoFragmentInteractionListener.class.toString());

        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
