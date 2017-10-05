package com.sahni.rahul.ieee_niec.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sahni.rahul.ieee_niec.R;
import com.sahni.rahul.ieee_niec.adapter.FeedPagerAdapter;
import com.sahni.rahul.ieee_niec.adapter.HomeItemsAdapter;
import com.sahni.rahul.ieee_niec.helpers.ContentUtils;
import com.sahni.rahul.ieee_niec.interfaces.OnHomeFragmentInteractionListener;
import com.sahni.rahul.ieee_niec.interfaces.OnHomeItemClickListener;
import com.sahni.rahul.ieee_niec.models.Feed;
import com.sahni.rahul.ieee_niec.models.Items;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator;


public class HomeFragment extends Fragment implements OnHomeItemClickListener{

    private static String TAG = "HomeFragment";
    private RecyclerView mRecyclerView;
    private HomeItemsAdapter mHomeItemsAdapter;
    private ArrayList<Items> mItemsArrayList;

    private ViewPager mFeedViewPager;
    private FeedPagerAdapter mFeedAdapter;
    private ArrayList<Feed> mFeedArrayList;

    private DatabaseReference mFeedReference;


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

        Toolbar toolbar = view.findViewById(R.id.home_toolbar);
        AppBarLayout appBarLayout = view.findViewById(R.id.home_app_bar);
        final CollapsingToolbarLayout toolbarLayout = view.findViewById(R.id.home_collapsing_toolbar);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int scrollRange = appBarLayout.getTotalScrollRange();
                if(scrollRange + verticalOffset == 0){
                    toolbarLayout.setTitle("Home");
                } else {
                    toolbarLayout.setTitle("");
                }
            }
        });

        Log.i(TAG, "Inside HomeFragment");
        Log.i(TAG, "onViewCreated");

//        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        mFeedReference = FirebaseDatabase.getInstance().getReference().child("feeds");
//        mFeedReference.keepSynced(true);


        DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActivity(), drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

//        drawer.setFitsSystemWindows(true);

        mFeedViewPager = view.findViewById(R.id.home_slider_view_pager);
        mFeedArrayList = new ArrayList<>();
//        mFeedArrayList.add("https://firebasestorage.googleapis.com/v0/b/firebase-ieee-niec.appspot.com/o/profile_image%2FDSC_6436.jpg?alt=media&token=d6ae57a4-856b-4e39-b793-2c646c2e12cb");
//        mFeedArrayList.add("https://firebasestorage.googleapis.com/v0/b/firebase-ieee-niec.appspot.com/o/profile_image%2F110649531844816796256?alt=media&token=2ab6bfee-a16e-449b-8633-e4420e547b75");
//        mSliderImageUrlArrayList.add("https://image.tmdb.org/t/p/w300/8uO0gUM8aNqYLs1OsTBQiXu0fEv.jpg");
//        mSliderImageUrlArrayList.add("https://img.youtube.com/vi/CmRih_VtVAs/0.jpg");
        mFeedAdapter = new FeedPagerAdapter(getChildFragmentManager(), mFeedArrayList);
        mFeedViewPager.setAdapter(mFeedAdapter);

        CircleIndicator circleIndicator =view.findViewById(R.id.circle_indicator);
        circleIndicator.setViewPager(mFeedViewPager);
        mFeedAdapter.registerDataSetObserver(circleIndicator.getDataSetObserver());

        mFeedReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i(TAG,"onDataChange: child count = "+ dataSnapshot.getChildrenCount());
                mFeedArrayList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Feed feed = snapshot.getValue(Feed.class);
                    mFeedArrayList.add(feed);

                }
//                mFeedViewPager.
                mFeedAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mRecyclerView = view.findViewById(R.id.item_recycler_view);
        mItemsArrayList = new ArrayList<>();
        mItemsArrayList.add(new Items(ContentUtils.EVENTS, R.drawable.ic_events, R.drawable.evnets_art));
        mItemsArrayList.add(new Items(ContentUtils.ACHIEVEMENTS, R.drawable.ic_achieve, R.drawable.achieve_art));
        mItemsArrayList.add(new Items(ContentUtils.PROJECTS, R.drawable.ic_projectt,R.drawable.colorfulightbulb1));
        mItemsArrayList.add(new Items(ContentUtils.ABOUT_IEEE, R.drawable.ic_ieeenew1, R.drawable.technology1));
        mItemsArrayList.add(new Items(ContentUtils.IEEE_RESOURCES, R.drawable.ic_resources, R.drawable.resources_new));
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
        Log.i(TAG, "onDetach");
        super.onDetach();
        listener = null;
        mFeedReference = null;
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
