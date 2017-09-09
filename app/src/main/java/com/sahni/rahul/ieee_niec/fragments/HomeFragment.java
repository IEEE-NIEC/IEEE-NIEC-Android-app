package com.sahni.rahul.ieee_niec.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.sahni.rahul.ieee_niec.adapter.HomeItemsAdapter;
import com.sahni.rahul.ieee_niec.helpers.ContentUtils;
import com.sahni.rahul.ieee_niec.interfaces.OnHomeFragmentInteractionListener;
import com.sahni.rahul.ieee_niec.interfaces.OnHomeItemClickListener;
import com.sahni.rahul.ieee_niec.models.Items;

import java.util.ArrayList;


public class HomeFragment extends Fragment implements OnHomeItemClickListener{

    private static String TAG = "HomeFragment";
    private RecyclerView mRecyclerView;
    private HomeItemsAdapter mHomeItemsAdapter;
    private ArrayList<Items> mItemsArrayList;


    private OnHomeFragmentInteractionListener listener;

    public HomeFragment() {
        // Required empty public constructor
    }


    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();

        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.home_toolbar);

        Log.i(TAG, "Inside HomeFragment");

        DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActivity(), drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        mRecyclerView = view.findViewById(R.id.item_recycler_view);
        mItemsArrayList = new ArrayList<>();
        mItemsArrayList.add(new Items(ContentUtils.EVENTS, R.drawable.ic_events, R.drawable.evnets_art));
        mItemsArrayList.add(new Items(ContentUtils.ACHIEVEMENTS, R.drawable.ic_achieve, R.drawable.achieve_art));
        mItemsArrayList.add(new Items(ContentUtils.PROJECTS, R.drawable.ic_projectt,R.drawable.evnets_art));
        mItemsArrayList.add(new Items(ContentUtils.IEEE, R.drawable.ic_ieeenew, R.drawable.evnets_art));
        mHomeItemsAdapter = new HomeItemsAdapter(getActivity(), mItemsArrayList, this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mHomeItemsAdapter);

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof OnHomeFragmentInteractionListener){
            listener = (OnHomeFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement" +
                    "OnHomeFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onHomeItemClicked(View view) {
        if(listener != null){
            int position = mRecyclerView.getChildAdapterPosition(view);
            Items items = mItemsArrayList.get(position);
            listener.onHomeFragmentInteraction(items.getTitle());
        }
    }


}
