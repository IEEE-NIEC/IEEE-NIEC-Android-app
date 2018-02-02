package com.sahni.rahul.ieee_niec.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.sahni.rahul.ieee_niec.R;
import com.sahni.rahul.ieee_niec.adapter.InformationItemAdapter;
import com.sahni.rahul.ieee_niec.helpers.ContentUtils;
import com.sahni.rahul.ieee_niec.helpers.EndlessRecyclerViewScrollListener;
import com.sahni.rahul.ieee_niec.interfaces.OnInfoFragmentInteractionListener;
import com.sahni.rahul.ieee_niec.interfaces.OnRecyclerViewItemClickListener;
import com.sahni.rahul.ieee_niec.models.Information;

import java.util.ArrayList;

import static com.sahni.rahul.ieee_niec.helpers.ContentUtils.INFO_KEY;


public class InformationFragment extends Fragment implements OnRecyclerViewItemClickListener {

    private static final String TAG = "InformationFragment";
    private RecyclerView mInfoRecyclerView;
    private InformationItemAdapter mInfoAdapter;
    private ArrayList<Information> mInfoArrayList;
    private ProgressBar mProgressBar;
    private CardView mLoadMoreProgress;
    private TextView mNoInfoTextView;

    private OnInfoFragmentInteractionListener mListener;
    private String mInfoType;

    private AppBarLayout mAppBarLayout;

    private EventListener<QuerySnapshot> mEventListener;

    private static final int NO_OF_INFO_TO_FETCH = 10;
    private boolean isMoreDataAvailable = true;

//    int mScrollPosition = 0;

    private float mLastItemId;

    private CollectionReference mCollectionReference;

    private ListenerRegistration mListenerRegistration;

    private EndlessRecyclerViewScrollListener mScrollListener;

    private boolean isFetchingDataFirstTime = true;

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
//        mScrollPosition = bundle.getInt(SCROLL_POSITION_KEY);
//        Log.i(TAG, "onCreate: scroll position: "+mScrollPosition);
//        Log.i(TAG, "")
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");

        return inflater.inflate(R.layout.fragment_information, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final Toolbar toolbar = view.findViewById(R.id.information_toolbar);
        toolbar.setTitle(mInfoType);

        mAppBarLayout = view.findViewById(R.id.info_appbar);
        mProgressBar = view.findViewById(R.id.information_progress_bar);
        mLoadMoreProgress = view.findViewById(R.id.load_more_progress);
        mNoInfoTextView = view.findViewById(R.id.no_info_text_view);
        mNoInfoTextView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.INVISIBLE);
        mLoadMoreProgress.setVisibility(View.INVISIBLE);

        DrawerLayout drawerLayout = getActivity().findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActivity(), drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        switch (mInfoType) {
            case ContentUtils.EVENTS:
                mCollectionReference = FirebaseFirestore.getInstance().collection(ContentUtils.FIRESTORE_EVENTS);
                break;
            case ContentUtils.ACHIEVEMENTS:
                mCollectionReference = FirebaseFirestore.getInstance().collection(ContentUtils.FIRESTORE_ACHIEVEMENTS);
                break;
            case ContentUtils.PROJECTS:
                mCollectionReference = FirebaseFirestore.getInstance().collection(ContentUtils.FIRESTORE_PROJECTS);
                break;
        }

        if (mInfoArrayList != null) {
            isFetchingDataFirstTime = false;
            mAppBarLayout.setExpanded(false);

        } else {
            mInfoArrayList = new ArrayList<>();

        }

        mInfoRecyclerView = view.findViewById(R.id.information_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mInfoRecyclerView.setLayoutManager(layoutManager);
        mInfoAdapter = new InformationItemAdapter(getActivity(), mInfoArrayList, this);
        mInfoRecyclerView.setAdapter(mInfoAdapter);
        mScrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                Log.i(TAG, "onLoadMore: total items :" + totalItemsCount);
                if (isMoreDataAvailable) {
                    Log.i(TAG, "onLoadMore: fetching more data");
                    fetchMoreData();
                }
            }
        };
        mInfoRecyclerView.addOnScrollListener(mScrollListener);
        if(isFetchingDataFirstTime){
            fetchDataFirstTime();
        } else {

            /**
             * bit of a hack to remove bug which hides some part of recycler view's last item.
             * This bug is only encountered when CollapsingToolbarLayout is used with RecyclerView
             */
//            mInfoRecyclerView.setPadding(0,0,0, ContentUtils.convertDpToPixel(56f,getContext()));
        }
    }

    private void fetchDataFirstTime() {
        mProgressBar.setVisibility(View.VISIBLE);
        mInfoRecyclerView.setPadding(0,0,0,0);
        mScrollListener.resetState();
        attachCollectionSnapshotListener();
    }

    private void fetchMoreData() {
        mInfoRecyclerView.setPadding(0,0,0,0);
        mLoadMoreProgress.setVisibility(View.VISIBLE);
        mCollectionReference.orderBy("id", Query.Direction.DESCENDING)
                .limit(NO_OF_INFO_TO_FETCH)
                .startAfter(mLastItemId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();
                            if (!querySnapshot.isEmpty()) {
                                for (DocumentSnapshot documents : querySnapshot.getDocuments()) {
                                    Information information = documents.toObject(Information.class);
                                    mInfoArrayList.add(information);
                                }
                                Information info  = mInfoArrayList.get(mInfoArrayList.size()-1);
                                mLastItemId = info.getId();
                                mInfoAdapter.notifyDataSetChanged();
                            } else {
                                isMoreDataAvailable = false;
                                Log.i(TAG, "fetchData: No result available");
                            }
                        } else {
                            Log.i(TAG, "fetchData: Error in getting result");
                        }
                        mProgressBar.setVisibility(View.INVISIBLE);
                        mLoadMoreProgress.setVisibility(View.INVISIBLE);
                    }
                });

    }

    private void attachCollectionSnapshotListener(){
        if(mEventListener == null){
            mEventListener = new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                    if(e == null){
                        if (documentSnapshots != null && !documentSnapshots.isEmpty()) {
                            mInfoArrayList.clear();
                            for (DocumentSnapshot documents : documentSnapshots.getDocuments()) {
                                Information information = documents.toObject(Information.class);
                                mInfoArrayList.add(information);
                            }
                            Information info  = mInfoArrayList.get(mInfoArrayList.size()-1);
                            mLastItemId = info.getId();
                            mProgressBar.setVisibility(View.INVISIBLE);
                            mInfoAdapter.notifyDataSetChanged();
                        } else {
                            isMoreDataAvailable = false;
                            mNoInfoTextView.setText("Please check your internet connection or try again later");
                            mNoInfoTextView.setVisibility(View.VISIBLE);
                            mProgressBar.setVisibility(View.GONE);
//                            mNoInfoTextView.setText("No "+mInfoType+ " found!");
                        }
                    } else {
                        mNoInfoTextView.setVisibility(View.VISIBLE);
                        mProgressBar.setVisibility(View.GONE);
                        mNoInfoTextView.setText("Error getting "+mInfoType+"!. Please try again later");
                        Log.i(TAG, "fetchDataFirstTime: error reading data: "+e.getMessage());
                    }
                }
            };

          mListenerRegistration = mCollectionReference.orderBy("id", Query.Direction.DESCENDING)
//                .endAt(NO_OF_INFO_TO_FETCH)
                    .limit(NO_OF_INFO_TO_FETCH)
                    .addSnapshotListener(mEventListener);
        }
    }

    private void detachCollectionSnapshotListener(){
        if(mListenerRegistration != null){
            mListenerRegistration.remove();
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i(TAG, "onAttach");
        if (context instanceof OnInfoFragmentInteractionListener) {
            mListener = (OnInfoFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnInfoFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        Log.i(TAG, "onDetach");
    }


    @Override
    public void onPause() {
        super.onPause();
        detachCollectionSnapshotListener();
    }


    @Override
    public void onItemClicked(View view) {
        if (mListener != null) {
            int position = mInfoRecyclerView.getChildAdapterPosition(view);
            Information info = mInfoArrayList.get(position);
            mListener.onInfoFragmentInteraction(info);
        }
    }
}
