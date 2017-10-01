package com.sahni.rahul.ieee_niec.fragments;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sahni.rahul.ieee_niec.R;
import com.sahni.rahul.ieee_niec.adapter.InterestAdapter;
import com.sahni.rahul.ieee_niec.helpers.ContentUtils;
import com.sahni.rahul.ieee_niec.interfaces.OnSignOutClickListener;
import com.sahni.rahul.ieee_niec.interfaces.OnUserProfileInteractionListener;
import com.sahni.rahul.ieee_niec.models.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserProfileFragment extends Fragment implements PopupMenu.OnMenuItemClickListener {

    private static final String TAG = "UserProfileFragment";
    private User mUser;

    private ImageView mUserImageView;
    private TextView mNameTextView;
    private TextView mEmailTextView;
    private TextView mMobileTextView;
    private RecyclerView mInterestRecyclerView;
    private ArrayList<String> mInterestArrayList;
    private InterestAdapter mInterestAdapter;

    private OnUserProfileInteractionListener mProfileInteractionListener;
    private OnSignOutClickListener mSignOutListener;


    public UserProfileFragment() {
        // Required empty public constructor
    }

    public static UserProfileFragment newInstance(User user) {

        Bundle args = new Bundle();
        args.putParcelable(ContentUtils.USER_KEY, user);
        UserProfileFragment fragment = new UserProfileFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static UserProfileFragment newInstance(){
        return new UserProfileFragment();
    }

//    public void update(User user){
//        Log.i(TAG, "update:");
//        mUser = user;
//        displayDetails();
//    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle != null) {
            mUser = getArguments().getParcelable(ContentUtils.USER_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        DrawerLayout drawerLayout = getActivity().findViewById(R.id.drawer_layout);
        final Toolbar toolbar = view.findViewById(R.id.user_toolbar);
        toolbar.setTitle(mUser.getName());
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActivity(), drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        AppBarLayout appBarLayout = view.findViewById(R.id.user_app_bar);
        final TextView toolbarTextView = view.findViewById(R.id.toolbar_text_view);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int scrollRange = appBarLayout.getTotalScrollRange();
                if(scrollRange + verticalOffset == 0){
                    toolbarTextView.setText(mUser.getName());
                } else {
                    toolbarTextView.setText("");
                }
            }
        });


        FloatingActionButton fab = view.findViewById(R.id.edit_fab);
        ImageView overflowMenuImageView = view.findViewById(R.id.overflow_menu);
        overflowMenuImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(getActivity(), view);
                popupMenu.setOnMenuItemClickListener(UserProfileFragment.this);
                popupMenu.inflate(R.menu.user_profile_menu);
                popupMenu.show();
            }
        });

        mUserImageView = view.findViewById(R.id.user_image_view);
        mNameTextView = view.findViewById(R.id.user_name_text_view);
        mEmailTextView = view.findViewById(R.id.user_email_text_view);
        mMobileTextView = view.findViewById(R.id.user_mobile_text_view);
        mInterestRecyclerView = view.findViewById(R.id.interest_recycler_view);
        mInterestArrayList = new ArrayList<>();
        mInterestRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        mInterestAdapter = new InterestAdapter(getActivity(), mInterestArrayList, ContentUtils.SHOW_INTEREST, null);
        mInterestRecyclerView.setAdapter(mInterestAdapter);

        displayDetails();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mProfileInteractionListener != null){
                    mProfileInteractionListener.onUserProfileInteraction(mUser);
                }
            }
        });

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OnUserProfileInteractionListener && context instanceof OnSignOutClickListener){
            mProfileInteractionListener = (OnUserProfileInteractionListener) context;
            mSignOutListener = (OnSignOutClickListener) context;
        } else {
            throw new RuntimeException(context.toString()+" must implement OnUserProfileInteractionListener" +
                    "and OnSignOutClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mProfileInteractionListener = null;
        mSignOutListener = null;
    }

    private void displayDetails() {
        if(mUser.getImageUrl() != null && !mUser.getImageUrl().isEmpty()) {
            Picasso.with(getActivity())
                    .load(mUser.getImageUrl())
                    .placeholder(R.drawable.user)
                    .error(R.drawable.user)
                    .into(mUserImageView);
        }


        mNameTextView.setText("" + mUser.getName());
        mEmailTextView.setText("" + mUser.getEmailId());
        mMobileTextView.setText("" + mUser.getMobileNumber());
        mInterestArrayList.clear();
        mInterestArrayList.addAll(mUser.getInterestArrayList());
//        if(mUser.getInterestArrayList().isEmpty()){
//            Log.i(TAG,)
//        }
        mInterestAdapter.notifyDataSetChanged();
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if(item.getItemId() == R.id.user_profile_sign_out){

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                    .setTitle("Sign Out")
                    .setMessage("Are you sure you want to sign out?")
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .setPositiveButton("Sign Out", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
//                            Toast.makeText(getActivity(),"Sign out", Toast.LENGTH_SHORT).show();
                            mSignOutListener.onSignOutClicked();
                        }
                    });
            builder.create().show();


        }
        return false;
    }
}
