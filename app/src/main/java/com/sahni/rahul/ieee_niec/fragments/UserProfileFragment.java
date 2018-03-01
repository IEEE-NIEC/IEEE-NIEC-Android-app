package com.sahni.rahul.ieee_niec.fragments;


import android.animation.Animator;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.internal.NavigationMenu;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.github.rubensousa.gravitysnaphelper.GravityPagerSnapHelper;
import com.sahni.rahul.ieee_niec.R;
import com.sahni.rahul.ieee_niec.adapter.InterestAdapter;
import com.sahni.rahul.ieee_niec.helpers.ContentUtils;
import com.sahni.rahul.ieee_niec.interfaces.OnUserProfileInteractionListener;
import com.sahni.rahul.ieee_niec.models.User;

import java.util.ArrayList;

import io.github.yavski.fabspeeddial.FabSpeedDial;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserProfileFragment extends Fragment {

    private static final String TAG = "UserProfileFragment";
    private User mUser;

    private ImageView mUserImageView;
    private TextView mEmailTextView;
    private TextView mMobileTextView;
    private ArrayList<String> mInterestArrayList;
    private InterestAdapter mInterestAdapter;
    private ProgressBar mImageProgressBar;
    private TextView mAboutTextView;

    private OnUserProfileInteractionListener mProfileInteractionListener;

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

        final FabSpeedDial fabSpeedDial = view.findViewById(R.id.fab_speed_dial);
        final FrameLayout frameLayout = view.findViewById(R.id.fab_container);
        mUserImageView = view.findViewById(R.id.user_image_view);
        mEmailTextView = view.findViewById(R.id.user_email_text_view);
        mMobileTextView = view.findViewById(R.id.user_mobile_text_view);
        mAboutTextView = view.findViewById(R.id.about_text_view);
        RecyclerView interestRecyclerView = view.findViewById(R.id.interest_recycler_view);
        mInterestArrayList = new ArrayList<>();
        interestRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        mInterestAdapter = new InterestAdapter(getActivity(), mInterestArrayList, ContentUtils.SHOW_INTEREST, null);
        SnapHelper snapHelper = new GravityPagerSnapHelper(Gravity.START);
        snapHelper.attachToRecyclerView(interestRecyclerView);
        interestRecyclerView.setAdapter(mInterestAdapter);

        displayDetails();

        fabSpeedDial.setMenuListener(new FabSpeedDial.MenuListener() {
            @Override
            public boolean onPrepareMenu(NavigationMenu navigationMenu) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    int cx = fabSpeedDial.getRight();
                    int cy = fabSpeedDial.getBottom();
                    int radius = (int) Math.hypot(frameLayout.getWidth(), frameLayout.getHeight());
                    Animator animator = ViewAnimationUtils.createCircularReveal(frameLayout, cx, cy,
                            0, radius);
                    frameLayout.setVisibility(View.VISIBLE);
                    animator.setDuration(400);
                    animator.start();
                } else {
                    frameLayout.setVisibility(View.VISIBLE);
                }
                return true;
            }

            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                frameLayout.setVisibility(View.INVISIBLE);
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
                                    mProfileInteractionListener.deleteAccount();
                                }
                            });
                    builder.create().show();
                }
                return false;
            }

            @Override
            public void onMenuClosed() {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    int cx = fabSpeedDial.getRight();
                    int cy = fabSpeedDial.getBottom();
                    int radius = (int) Math.hypot(frameLayout.getWidth(), frameLayout.getHeight());
                    Animator animator = ViewAnimationUtils.createCircularReveal(frameLayout, cx, cy,
                            radius, 0);
                    animator.setDuration(400);
                    animator.start();
                    animator.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            frameLayout.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });
                } else {
                    frameLayout.setVisibility(View.INVISIBLE);
                }
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
    }

    private void displayDetails() {
        if (mUser.getImageUrl() != null && !mUser.getImageUrl().isEmpty()) {

            RequestBuilder<Drawable> requestBuilder = Glide.with(this)
                    .load(mUser.getImageUrl());
            requestBuilder
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            mImageProgressBar.setVisibility(View.INVISIBLE);
                            mUserImageView.setImageResource(R.drawable.user);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            mImageProgressBar.setVisibility(View.INVISIBLE);
                            return false;
                        }
                    })
                    .into(mUserImageView);

        }

        mEmailTextView.setText("" + mUser.getEmailId());
        mMobileTextView.setText("" + mUser.getMobileNo());
        mInterestArrayList.clear();
        mInterestArrayList.addAll(ContentUtils.getArrayListFromMap(mUser.getInterestMap()));
        mInterestAdapter.notifyDataSetChanged();
        mAboutTextView.setText(mUser.getAbout());
    }


}
