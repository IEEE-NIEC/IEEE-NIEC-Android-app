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

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.sahni.rahul.ieee_niec.R;
import com.sahni.rahul.ieee_niec.adapter.FeedPagerAdapter;
import com.sahni.rahul.ieee_niec.adapter.HomeItemsAdapter;
import com.sahni.rahul.ieee_niec.helpers.ContentUtils;
import com.sahni.rahul.ieee_niec.interfaces.OnHomeFragmentInteractionListener;
import com.sahni.rahul.ieee_niec.interfaces.OnRecyclerViewItemClickListener;
import com.sahni.rahul.ieee_niec.models.Feed;
import com.sahni.rahul.ieee_niec.models.HomeItems;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator;


public class HomeFragment extends Fragment implements OnRecyclerViewItemClickListener {

    private static String TAG = "HomeFragment";
    private RecyclerView mRecyclerView;
    private HomeItemsAdapter mHomeItemsAdapter;
    private ArrayList<HomeItems> mHomeItemsArrayList;

    private ViewPager mFeedViewPager;
    private FeedPagerAdapter mFeedAdapter;
    private ArrayList<Feed> mFeedArrayList;

//    private DatabaseReference mFeedReference;
    private CollectionReference mFeedReference;
    private ListenerRegistration mListenerRegistration;


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

        mFeedReference = FirebaseFirestore.getInstance().collection(ContentUtils.FIRESTORE_FEEDS);
//        mFeedReference.keepSynced(true);


        DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActivity(), drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        mRecyclerView = view.findViewById(R.id.item_recycler_view);
        mHomeItemsArrayList = new ArrayList<>();
        mHomeItemsArrayList.add(new HomeItems(ContentUtils.EVENTS, R.drawable.ic_events, R.drawable.evnets_art));
        mHomeItemsArrayList.add(new HomeItems(ContentUtils.ACHIEVEMENTS, R.drawable.ic_achieve, R.drawable.achieve_art));
        mHomeItemsArrayList.add(new HomeItems(ContentUtils.PROJECTS, R.drawable.ic_projectt,R.drawable.colorfulightbulb1));
        mHomeItemsArrayList.add(new HomeItems(ContentUtils.ABOUT_IEEE, R.drawable.ic_ieeenew1, R.drawable.technology1));
        mHomeItemsArrayList.add(new HomeItems(ContentUtils.IEEE_RESOURCES, R.drawable.ic_new_untitled_1, R.drawable.resources_new));
        mHomeItemsAdapter = new HomeItemsAdapter(getActivity(), mHomeItemsArrayList, this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mHomeItemsAdapter);
        mRecyclerView.setPadding(0,0,0, ContentUtils.convertDpToPixel(56f,getContext()));

        mFeedViewPager = view.findViewById(R.id.home_slider_view_pager);
        mFeedArrayList = new ArrayList<>();
        mFeedAdapter = new FeedPagerAdapter(getChildFragmentManager(), mFeedArrayList);
        mFeedViewPager.setAdapter(mFeedAdapter);

        CircleIndicator circleIndicator =view.findViewById(R.id.circle_indicator);
        circleIndicator.setViewPager(mFeedViewPager);
        mFeedAdapter.registerDataSetObserver(circleIndicator.getDataSetObserver());
        attachFeedSnapshotListener();
    }

    @Override
    public void onResume() {
        super.onResume();

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
//        mFeedReference = null;
    }

    private void attachFeedSnapshotListener(){
        mListenerRegistration = mFeedReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if(e != null){
                    Log.w(TAG, "attachFeedSnapshotListener: error: "+e);
                } else {
                    if(documentSnapshots != null && !documentSnapshots.isEmpty()){
                        mFeedArrayList.clear();
                        for(DocumentSnapshot documentSnapshot : documentSnapshots){
                            Feed feed = documentSnapshot.toObject(Feed.class);
                            mFeedArrayList.add(feed);
                        }
                        mFeedAdapter.notifyDataSetChanged();
                        mRecyclerView.setPadding(0,0,0,0);
                    }
                }
            }
        });
    }

    private void detachFeedSnapshotListener(){
        if(mListenerRegistration != null){
            mListenerRegistration.remove();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        detachFeedSnapshotListener();
    }

    @Override
    public void onItemClicked(View view) {
        if(listener != null){
            int position = mRecyclerView.getChildAdapterPosition(view);
            HomeItems homeItems = mHomeItemsArrayList.get(position);
            listener.onHomeFragmentInteraction(homeItems.getTitle());
        }
    }
}
