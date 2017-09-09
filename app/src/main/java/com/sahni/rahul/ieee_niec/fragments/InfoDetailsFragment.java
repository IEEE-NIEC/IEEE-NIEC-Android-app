package com.sahni.rahul.ieee_niec.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sahni.rahul.ieee_niec.R;
import com.sahni.rahul.ieee_niec.helpers.ContentUtils;
import com.sahni.rahul.ieee_niec.interfaces.OnInfoDetailsFragmentInteractionListener;
import com.sahni.rahul.ieee_niec.interfaces.OnInfoFragmentInteractionListener;
import com.sahni.rahul.ieee_niec.models.Information;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 */
public class InfoDetailsFragment extends Fragment {

    private Information mInfo;
    private OnInfoDetailsFragmentInteractionListener mListener;


    public InfoDetailsFragment() {
        // Required empty public constructor
    }

    public static InfoDetailsFragment newInstance(Information info){
        InfoDetailsFragment fragment = new InfoDetailsFragment();
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

        ImageView imageView = view.findViewById(R.id.info_details_image_view);
        TextView titleTextView = view.findViewById(R.id.info_details_title_text_view);
        TextView descriptionTextView = view.findViewById(R.id.info_details_description_text_view);
        Picasso.with(getActivity())
                .load(mInfo.getImageUrlArrayList().get(0))
                .into(imageView);
        titleTextView.setText(mInfo.getTitle());
        descriptionTextView.setText(mInfo.getDescription());

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
