package com.sahni.rahul.ieee_niec.fragments;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sahni.rahul.ieee_niec.R;
import com.sahni.rahul.ieee_niec.adapter.IeeeResourcesAdapter;
import com.sahni.rahul.ieee_niec.helpers.ContentUtils;
import com.sahni.rahul.ieee_niec.interfaces.OnRecyclerViewItemClickListener;
import com.sahni.rahul.ieee_niec.models.Resources;

import java.util.ArrayList;

public class IeeeResourcesFragment extends Fragment implements OnRecyclerViewItemClickListener {

    public static final String TAG = "IeeeResourcesFragment";
    private ArrayList<Resources> mArrayList;
    private RecyclerView mRecyclerView;


    public IeeeResourcesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ieee_resources, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar = view.findViewById(R.id.resources_toolbar);
        CollapsingToolbarLayout toolbarLayout = view.findViewById(R.id.ieee_resources_collapsing_toolbar);
        toolbar.setTitle("IEEE Resources");

        DrawerLayout drawer = getActivity().findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActivity(), drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        mRecyclerView = view.findViewById(R.id.resources_recycler_view);
        mArrayList = new ArrayList<>();

        mArrayList.add(new Resources("IEEE Collabratec™\tis an integrated online community where technology professionals can network, collaborate, and create — all in one central hub."
                ,R.drawable.ieee_collabratec,"https://ieee-collabratec.ieee.org/"));
        mArrayList.add(new Resources("IEEE ResumeLab is an online service that allows IEEE members to develop a resume or curriculum vitae using a wide array of resume templates."
                ,R.drawable.ieee_resume,"http://www.ieee.org/membership_services/membership/resumelab.html"));
        mArrayList.add(new Resources("Welcome to IEEE Transmitter™ Here you will find a collection of articles, videos, infographics, inspiration and more all curated by IEEE."
                ,R.drawable.ieee_transmitter,"https://transmitter.ieee.org/"));
        mArrayList.add(new Resources("IEEE Xplore® Digital Library is a powerful resource for discovery of and access to scientific and technical content published by the IEEE (Institute of Electrical and Electronics Engineers) and its publishing partners."
                ,R.drawable.ieee_xplore,"http://ieeexplore.ieee.org/Xplore/home.jsp"));

        IeeeResourcesAdapter adapter = new IeeeResourcesAdapter(getContext(), mArrayList, this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setPadding(0,0,0,ContentUtils.convertDpToPixel(56f, getActivity()));

//        Rect rect = new Rect();
//        mRecyclerView.getDrawingRect(rect);
//        Log.d(TAG, "Top ="+rect.top);
//        Log.d(TAG, "Bottom ="+rect.bottom);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Rect rect = new Rect();
                mRecyclerView.getDrawingRect(rect);
                Log.d(TAG, "Top ="+rect.top);
                Log.d(TAG, "Bottom ="+rect.bottom);
            }
        });
    }

    @Override
    public void onItemClicked(View view) {
        int position = mRecyclerView.getChildAdapterPosition(view);

        Uri uri = Uri.parse((mArrayList.get(position).getmUrl()));
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        PackageManager packageManager = getActivity().getPackageManager();
        if(intent.resolveActivity(packageManager) != null){
            startActivity(intent);
        } else{
            Snackbar.make(mRecyclerView, "Please install browser to continue", BaseTransientBottomBar.LENGTH_INDEFINITE)
                    .setAction("DISMISS", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    }).show();
        }
    }
}
