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
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sahni.rahul.ieee_niec.R;
import com.sahni.rahul.ieee_niec.adapter.InformationItemAdapter;
import com.sahni.rahul.ieee_niec.helpers.ContentUtils;
import com.sahni.rahul.ieee_niec.interfaces.OnInfoFragmentInteractionListener;
import com.sahni.rahul.ieee_niec.interfaces.OnInformationItemClickListener;
import com.sahni.rahul.ieee_niec.models.Information;
import com.sahni.rahul.ieee_niec.networking.EventsResponse;

import java.util.ArrayList;

import static com.sahni.rahul.ieee_niec.helpers.ContentUtils.INFO_KEY;


public class InformationFragment extends Fragment implements OnInformationItemClickListener {

    private static final String TAG = "InformationFragment";
    private RecyclerView mInfoRecyclerView;
    private InformationItemAdapter mInfoAdapter;
    private ArrayList<Information> mInfoArrayList;
    private ProgressBar mProgressBar;
    private OnInfoFragmentInteractionListener mListener;
    private String mInfoType;

    private Bundle mSaveStateBundle;
    private EventsResponse mEventsResponse;

    private DatabaseReference mEventsReference;
    private DatabaseReference mAchievementsReference;
    private DatabaseReference mProjectsReference;
    private DatabaseReference mDatabaseReference;


    public InformationFragment() {
        // Required empty public constructor
    }

    public static InformationFragment newInstance(String infoType) {
        InformationFragment fragment = new InformationFragment();
        Bundle args = new Bundle();
        args.putString(INFO_KEY, infoType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        mInfoType = bundle.getString(INFO_KEY);
        Log.i(TAG, "onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.i(TAG,"onCreateView");
        return inflater.inflate(R.layout.fragment_information, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i(TAG, "onViewCreated");
        final Toolbar toolbar = view.findViewById(R.id.information_toolbar);
        toolbar.setTitle(mInfoType);
        mProgressBar = view.findViewById(R.id.information_progress_bar);

        DrawerLayout drawerLayout = getActivity().findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActivity(),drawerLayout,toolbar,R.string.navigation_drawer_open, R.string.navigation_drawer_close
        );

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        mInfoArrayList = new ArrayList<>();
        mInfoRecyclerView = view.findViewById(R.id.information_recycler_view);
        mInfoRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL, false));
        mInfoAdapter = new InformationItemAdapter(getActivity(), mInfoArrayList, this);
        mInfoRecyclerView.setAdapter(mInfoAdapter);

//        mDatabaseReference = ;

        if(mInfoType.equals(ContentUtils.EVENTS)){
            mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("events");
        } else if(mInfoType.equals(ContentUtils.ACHIEVEMENTS)){
            mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("achievements");
        } else {
            mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("projects");
        }



        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mProgressBar.setVisibility(View.GONE);
                mInfoArrayList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Information info = snapshot.getValue(Information.class);
                    mInfoArrayList.add(info);
                }
                mInfoAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i(TAG, "Database error : "+databaseError.getMessage());
            }
        });

//        if(mInfoArrayList != null){
//
//            mInfoRecyclerView.setAdapter(mInfoAdapter);
//            ArrayList<Information> tempArrayList = new ArrayList<>();
//            tempArrayList.addAll(mInfoArrayList);
//            displayDetails(tempArrayList);
//        } else {
//            mInfoArrayList = new ArrayList<>();
//
//            mInfoRecyclerView.setAdapter(mInfoAdapter);
//            fetchData();
//        }


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i(TAG, "onAttach");
        if(context instanceof OnInfoFragmentInteractionListener){
            mListener = (OnInfoFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()+" must implement OnInformationItemClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        Log.i(TAG, "onDetach");
    }

//    private void fetchData() {
//
//        if(mInfoType.equals(ContentUtils.EVENTS)) {
//            RetrofitClient.getInstance()
//                    .create(ApiService.class)
//                    .getEvents()
//                    .enqueue(new Callback<EventsResponse>() {
//                        @Override
//                        public void onResponse(Call<EventsResponse> call, Response<EventsResponse> response) {
//                            if (response.isSuccessful()) {
//                                mEventsResponse = response.body();
//                                displayDetails(mEventsResponse.getInfoArrayList());
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<EventsResponse> call, Throwable t) {
//                            t.printStackTrace();
//                        }
//                    });
//        } else if(mInfoType.equals(ContentUtils.ACHIEVEMENTS)){
//
//        } else if(mInfoType.equals(ContentUtils.PROJECTS)){
//
//        }
//    }

//    private void displayDetails(ArrayList<Information> informationArrayList) {
//        Log.i(TAG, "displayDetails");
//        mProgressBar.setVisibility(View.GONE);
////        mInfoRecyclerView.setVisibility(View.VISIBLE);
//        mInfoArrayList.clear();
//        Log.i(TAG,"displayDetails: size ="+informationArrayList.size());
//        mInfoArrayList.addAll(informationArrayList);
//        mInfoAdapter.notifyDataSetChanged();
//    }



    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "onPause");
//        if(mSaveStateBundle == null && mEventsResponse != null){
//            mSaveStateBundle = new Bundle();
//            mSaveStateBundle.putParcelable(ContentUtils.INFO_KEY, mEventsResponse);
//        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG, "onStop");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i(TAG, "onDestroyView");
    }


    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.i(TAG, "onViewStateRestored");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(TAG, "onSaveInstanceState");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG, "onActivityCreated");
    }

    @Override
    public void onInformationItemClicked(View view, ImageView sharedImageView) {
        if(mListener != null) {
            int position = mInfoRecyclerView.getChildAdapterPosition(view);
            Information info = mInfoArrayList.get(position);
            mListener.onInfoFragmentInteraction(info, sharedImageView);
        }
    }
}
