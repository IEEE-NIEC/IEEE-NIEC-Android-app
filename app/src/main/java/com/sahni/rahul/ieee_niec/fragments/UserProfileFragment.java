package com.sahni.rahul.ieee_niec.fragments;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.internal.NavigationMenu;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sahni.rahul.ieee_niec.R;
import com.sahni.rahul.ieee_niec.adapter.InterestAdapter;
import com.sahni.rahul.ieee_niec.helpers.ContentUtils;
import com.sahni.rahul.ieee_niec.interfaces.OnUserProfileInteractionListener;
import com.sahni.rahul.ieee_niec.models.FirestoreUser;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import io.github.yavski.fabspeeddial.FabSpeedDial;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserProfileFragment extends Fragment {

    private static final String TAG = "UserProfileFragment";
    private FirestoreUser mUser;

    private ImageView mUserImageView;
    //    private TextView mNameTextView;
    private TextView mEmailTextView;
    private TextView mMobileTextView;
    private RecyclerView mInterestRecyclerView;
    private ArrayList<String> mInterestArrayList;
    private InterestAdapter mInterestAdapter;
    private ProgressBar mImageProgressBar;
    private TextView mAboutTextView;

    private OnUserProfileInteractionListener mProfileInteractionListener;
//    private OnSignOutClickListener mSignOutListener;
//    private TextView mToolbarTextView;


    public UserProfileFragment() {
        // Required empty public constructor
    }

    public static UserProfileFragment newInstance(FirestoreUser user) {

        Bundle args = new Bundle();
        args.putParcelable(ContentUtils.USER_KEY, user);
        UserProfileFragment fragment = new UserProfileFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static UserProfileFragment newInstance() {
        return new UserProfileFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
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

        mImageProgressBar = view.findViewById(R.id.image_progress_bar);

        FabSpeedDial fabSpeedDial = view.findViewById(R.id.fab_speed_dial);

        mUserImageView = view.findViewById(R.id.user_image_view);
        mEmailTextView = view.findViewById(R.id.user_email_text_view);
        mMobileTextView = view.findViewById(R.id.user_mobile_text_view);
        mAboutTextView = view.findViewById(R.id.about_text_view);
        mInterestRecyclerView = view.findViewById(R.id.interest_recycler_view);
        mInterestArrayList = new ArrayList<>();
        mInterestRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        mInterestAdapter = new InterestAdapter(getActivity(), mInterestArrayList, ContentUtils.SHOW_INTEREST, null);
        mInterestRecyclerView.setAdapter(mInterestAdapter);

        displayDetails();

        fabSpeedDial.setMenuListener(new FabSpeedDial.MenuListener() {
            @Override
            public boolean onPrepareMenu(NavigationMenu navigationMenu) {
                return true;
            }

            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                int id = menuItem.getItemId();
                if (id == R.id.fab_edit) {
                    if (mProfileInteractionListener != null) {
                        mProfileInteractionListener.editProfile(mUser);
                    }
                } else if (id == R.id.fab_sign_out) {
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
                                    mProfileInteractionListener.signOut();
                                }
                            });
                    builder.create().show();
                } else if(id == R.id.fab_delete){

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                            .setTitle("Delete")
                            .setMessage("Are you sure you want to delete your account?")
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            })
                            .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
//                            Toast.makeText(getActivity(),"Sign out", Toast.LENGTH_SHORT).show();
                                    mProfileInteractionListener.deleteAccount();
                                }
                            });
                    builder.create().show();
                }
                return false;
            }

            @Override
            public void onMenuClosed() {

            }
        });

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnUserProfileInteractionListener) {
            mProfileInteractionListener = (OnUserProfileInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnUserProfileInteractionListener" +
                    "and OnSignOutClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mProfileInteractionListener = null;
//        mSignOutListener = null;
    }

    private void displayDetails() {
        if (mUser.getImageUrl() != null && !mUser.getImageUrl().isEmpty()) {
            Picasso.with(getActivity())
                    .load(mUser.getImageUrl())
                    .into(mUserImageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            mImageProgressBar.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onError() {
                            mImageProgressBar.setVisibility(View.INVISIBLE);
                            mUserImageView.setImageResource(R.drawable.user);
                        }
                    });
        }

        mEmailTextView.setText("" + mUser.getEmailId());
        mMobileTextView.setText("" + mUser.getMobileNo());
        mInterestArrayList.clear();
        mInterestArrayList.addAll(ContentUtils.getArrayListFromMap(mUser.getInterestMap()));
        mInterestAdapter.notifyDataSetChanged();
        mAboutTextView.setText(mUser.getAbout());
    }


}
