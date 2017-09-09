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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.sahni.rahul.ieee_niec.R;
import com.sahni.rahul.ieee_niec.adapter.InformationItemAdapter;
import com.sahni.rahul.ieee_niec.helpers.ContentUtils;
import com.sahni.rahul.ieee_niec.interfaces.OnInfoFragmentInteractionListener;
import com.sahni.rahul.ieee_niec.interfaces.OnInformationItemClickListener;
import com.sahni.rahul.ieee_niec.models.Information;
import com.sahni.rahul.ieee_niec.networking.ApiService;
import com.sahni.rahul.ieee_niec.networking.EventsResponse;
import com.sahni.rahul.ieee_niec.networking.RetrofitClient;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.sahni.rahul.ieee_niec.helpers.ContentUtils.INFO_KEY;


public class InformationFragment extends Fragment implements OnInformationItemClickListener {

    private RecyclerView mInfoRecyclerView;
    private InformationItemAdapter mInfoAdapter;
    private ArrayList<Information> mInfoArrayList;
    private ProgressBar mProgressBar;
    private OnInfoFragmentInteractionListener mListener;
    private String mInfoType;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_information, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar = view.findViewById(R.id.information_toolbar);
        mProgressBar = view.findViewById(R.id.information_progress_bar);

        DrawerLayout drawerLayout = getActivity().findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActivity(),drawerLayout,toolbar,R.string.navigation_drawer_open, R.string.navigation_drawer_close
        );

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        mInfoArrayList = new ArrayList<>();
        mInfoRecyclerView = view.findViewById(R.id.information_recycler_view);
        mInfoAdapter = new InformationItemAdapter(getActivity(), mInfoArrayList, this);
        mInfoRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL, false));
        mInfoRecyclerView.setAdapter(mInfoAdapter);

        fetchData();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
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
    }

    private void fetchData() {

        if(mInfoType.equals(ContentUtils.EVENTS)) {
            RetrofitClient.getInstance()
                    .create(ApiService.class)
                    .getEvents()
                    .enqueue(new Callback<EventsResponse>() {
                        @Override
                        public void onResponse(Call<EventsResponse> call, Response<EventsResponse> response) {
                            if (response.isSuccessful()) {
                                displayDetails(response.body());
                            }
                        }

                        @Override
                        public void onFailure(Call<EventsResponse> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });
        } else if(mInfoType.equals(ContentUtils.ACHIEVEMENTS)){

        } else if(mInfoType.equals(ContentUtils.PROJECTS)){

        }
    }

    private void displayDetails(EventsResponse response) {
        mProgressBar.setVisibility(View.GONE);
        mInfoArrayList.addAll(response.getInfoArrayList());
        mInfoAdapter.notifyDataSetChanged();
    }

    @Override
    public void onInformationItemClicked(View view) {
        if(mListener != null) {
            int position = mInfoRecyclerView.getChildAdapterPosition(view);
            Information info = mInfoArrayList.get(position);
            mListener.onInfoFragmentInteraction(info);
        }

    }
}
