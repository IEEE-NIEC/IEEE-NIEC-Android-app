package com.sahni.rahul.ieee_niec.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.sahni.rahul.ieee_niec.R;
import com.sahni.rahul.ieee_niec.fragments.AboutIeeeFragment;
import com.sahni.rahul.ieee_niec.fragments.HomeFragment;
import com.sahni.rahul.ieee_niec.fragments.IeeeResourcesFragment;
import com.sahni.rahul.ieee_niec.fragments.InformationDetailsFragment;
import com.sahni.rahul.ieee_niec.fragments.InformationFragment;
import com.sahni.rahul.ieee_niec.fragments.InformationImageSliderFragment;
import com.sahni.rahul.ieee_niec.fragments.SearchUserFragment;
import com.sahni.rahul.ieee_niec.fragments.ShowFeedImagesDialogFragment;
import com.sahni.rahul.ieee_niec.fragments.UserDialogFragment;
import com.sahni.rahul.ieee_niec.fragments.UserProfileFragment;
import com.sahni.rahul.ieee_niec.helpers.ContentUtils;
import com.sahni.rahul.ieee_niec.interfaces.OnHomeFragmentInteractionListener;
import com.sahni.rahul.ieee_niec.interfaces.OnHomeSliderInteractionListener;
import com.sahni.rahul.ieee_niec.interfaces.OnInfoDetailsFragmentInteractionListener;
import com.sahni.rahul.ieee_niec.interfaces.OnInfoFragmentInteractionListener;
import com.sahni.rahul.ieee_niec.interfaces.OnSearchUserFragmentInteractionListener;
import com.sahni.rahul.ieee_niec.interfaces.OnSignOutClickListener;
import com.sahni.rahul.ieee_niec.interfaces.OnUserDetailsDialogInteractionListener;
import com.sahni.rahul.ieee_niec.interfaces.OnUserProfileInteractionListener;
import com.sahni.rahul.ieee_niec.models.Information;
import com.sahni.rahul.ieee_niec.models.User;
import com.sahni.rahul.ieee_niec.models.UserSingInStatus;

import static com.sahni.rahul.ieee_niec.R.id.nav_search;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnHomeFragmentInteractionListener
        , OnInfoFragmentInteractionListener, OnInfoDetailsFragmentInteractionListener,
        OnSearchUserFragmentInteractionListener, OnUserDetailsDialogInteractionListener
        , OnUserProfileInteractionListener, OnSignOutClickListener, OnHomeSliderInteractionListener {

    private static final String TAG = "MainActivity";
    private static final int MY_PROFILE_RC = 100;
    private static final int SEARCH_USER_RC = 101;
    private static final int EDIT_PROFILE_RC = 200;
    private User mUser;
    private boolean loadUserFragment = false;
    private boolean loadSearchUserFragment = false;

    private static final String HOME_FRAGMENT_TAG = "home_fragment";
    private static final String EVENTS_FRAGMENT_TAG = "events_fragment";
    private static final String ACHIEVEMENTS_FRAGMENT_TAG = "achieve_fragment_tag";
    private static final String PROJECTS_FRAGMENT_TAG = "projects_fragment_tag";
    private static final String USER_PROFILE_FRAGMENT_TAG = "user_profile_fragment";
    private static final String SEARCH_USER_FRAGMENT_TAG = "search_user_fragment";
    private static final String ABOUT_IEEE_FRAGMENT_TAG = "about_ieee_fragment";
    private static final String IEEE_RESOURCES_TAG = "ieee_resources_tag";

    private NavigationView mNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.addDrawerListener(toggle);
//        toggle.syncState();

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);
        displaySelectedFragment(R.id.nav_home);
        mNavigationView.setCheckedItem(R.id.nav_home);

    }

    @Override
    public void onBackPressed() {

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        HomeFragment homeFragment = (HomeFragment) fm.findFragmentByTag(HOME_FRAGMENT_TAG);
        UserProfileFragment userProfileFragment = (UserProfileFragment) fm.findFragmentByTag(USER_PROFILE_FRAGMENT_TAG);
        SearchUserFragment searchUserFragment = (SearchUserFragment) fm.findFragmentByTag(SEARCH_USER_FRAGMENT_TAG);
        InformationFragment eventsFragment = (InformationFragment) fm.findFragmentByTag(EVENTS_FRAGMENT_TAG);
        InformationFragment achievementsFragment = (InformationFragment) fm.findFragmentByTag(ACHIEVEMENTS_FRAGMENT_TAG);
        InformationFragment projectsFragment = (InformationFragment) fm.findFragmentByTag(PROJECTS_FRAGMENT_TAG);
        AboutIeeeFragment ieeeFragment = (AboutIeeeFragment) fm.findFragmentByTag(ABOUT_IEEE_FRAGMENT_TAG);
        IeeeResourcesFragment ieeeResourcesFragment = (IeeeResourcesFragment) fm.findFragmentByTag(IEEE_RESOURCES_TAG);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if ((userProfileFragment != null && userProfileFragment.isVisible())
                || (searchUserFragment != null && searchUserFragment.isVisible())
                || (eventsFragment != null && eventsFragment.isVisible())
                || (achievementsFragment != null && achievementsFragment.isVisible())
                || (projectsFragment != null && projectsFragment.isVisible())
                || (ieeeFragment != null && ieeeFragment.isVisible())
                || (ieeeResourcesFragment != null && ieeeResourcesFragment.isVisible())) {
            ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right,android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            ft.replace(R.id.main_frame_layout, HomeFragment.newInstance(), HOME_FRAGMENT_TAG).addToBackStack(null).commit();
            mNavigationView.setCheckedItem(R.id.nav_home);
        } else if (homeFragment != null && homeFragment.isVisible()) {
            finishAffinity();
        } else {
            try {
                super.onBackPressed();
            } catch (IllegalStateException e){
                Log.i(TAG, "onBackPressed: "+e.getMessage());
                ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right,android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                ft.replace(R.id.main_frame_layout, HomeFragment.newInstance(), HOME_FRAGMENT_TAG).addToBackStack(null).commit();
                mNavigationView.setCheckedItem(R.id.nav_home);

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        displaySelectedFragment(item.getItemId());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    void displaySelectedFragment(int menuItemId) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        UserSingInStatus singInStatus;
        switch (menuItemId) {

            case R.id.nav_home:
                ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right,android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                ft.replace(R.id.main_frame_layout, HomeFragment.newInstance(), HOME_FRAGMENT_TAG).addToBackStack(null).commit();
                mNavigationView.setCheckedItem(R.id.nav_home);
                break;

            case R.id.nav_events:
                ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right,android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                ft.replace(R.id.main_frame_layout, InformationFragment.newInstance(ContentUtils.EVENTS), EVENTS_FRAGMENT_TAG).addToBackStack(null).commit();
                mNavigationView.setCheckedItem(R.id.nav_events);
                break;

            case R.id.nav_achieve:
                ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right,android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                ft.replace(R.id.main_frame_layout, InformationFragment.newInstance(ContentUtils.ACHIEVEMENTS), ACHIEVEMENTS_FRAGMENT_TAG).addToBackStack(null).commit();
                mNavigationView.setCheckedItem(R.id.nav_achieve);
                break;

            case R.id.nav_project:
                ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right,android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                ft.replace(R.id.main_frame_layout, InformationFragment.newInstance(ContentUtils.PROJECTS), PROJECTS_FRAGMENT_TAG).addToBackStack(null).commit();
                mNavigationView.setCheckedItem(R.id.nav_project);
                break;

            case R.id.nav_ieee:
                ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right,android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                ft.replace(R.id.main_frame_layout, new AboutIeeeFragment(), ABOUT_IEEE_FRAGMENT_TAG).addToBackStack(null).commit();
                mNavigationView.setCheckedItem(R.id.nav_ieee);
                break;

            case R.id.nav_resource:
                ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right,android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                ft.replace(R.id.main_frame_layout, new IeeeResourcesFragment(), IEEE_RESOURCES_TAG).addToBackStack(null).commit();
                mNavigationView.setCheckedItem(R.id.nav_resource);
                break;


            case R.id.nav_my_profile:
                singInStatus = isUserSigned();
                if (singInStatus.getStatus()) {
                    User user = singInStatus.getUser();
                    Log.i(TAG, "displaySelectedFragment: name: " + user.getName() + " \n email: " + user.getEmailId() +
                            "\n id: " + user.getUserId());
//                    ft.replace(R.id.main_frame_layout, UserProfileFragment.newInstance(user)).commit();
                    ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right,android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                    ft.replace(R.id.main_frame_layout, UserProfileFragment.newInstance(user), USER_PROFILE_FRAGMENT_TAG).addToBackStack(null).commit();
                    mNavigationView.setCheckedItem(R.id.nav_my_profile);
                } else {
                    startActivityForResult(new Intent(this, SignInActivity.class), MY_PROFILE_RC);
                }
                break;

            case nav_search:
                singInStatus = isUserSigned();
                if (singInStatus.getStatus()) {
                    User user = singInStatus.getUser();
                    Log.i(TAG, "displaySelectedFragment: name: " + user.getName() + " \n email: " + user.getEmailId() +
                            "\n id: " + user.getUserId());
//                    ft.replace(R.id.main_frame_layout, UserProfileFragment.newInstance(user)).commit();
                    ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right,android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                    ft.replace(R.id.main_frame_layout, SearchUserFragment.newInstance(), SEARCH_USER_FRAGMENT_TAG).addToBackStack(null).commit();
                    ft.addToBackStack(null);
                    mNavigationView.setCheckedItem(R.id.nav_search);
                } else {
                    startActivityForResult(new Intent(this, SignInActivity.class), SEARCH_USER_RC);
                }


        }
    }


    private UserSingInStatus isUserSigned() {

        SharedPreferences sharedPreferences = getSharedPreferences(ContentUtils.SHARED_PREF, Context.MODE_PRIVATE);
        String signInStatus = sharedPreferences.getString(ContentUtils.USER_KEY, ContentUtils.SIGNED_OUT);
        Gson gson = new Gson();

        if (!signInStatus.equals(ContentUtils.SIGNED_OUT)) {
            return new UserSingInStatus(true, gson.fromJson(signInStatus, User.class));

        } else {
            return new UserSingInStatus(false, null);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MY_PROFILE_RC && resultCode == RESULT_OK) {
            mUser = data.getParcelableExtra(ContentUtils.USER_KEY);
            loadUserFragment = true;
            Log.i(TAG, "onActivityResult: name: " + mUser.getName() + " \n email: " + mUser.getEmailId() +
                    "\n id: " + mUser.getUserId());
        } else if (requestCode == SEARCH_USER_RC && resultCode == RESULT_OK) {
            loadSearchUserFragment = true;
        } else if (requestCode == EDIT_PROFILE_RC) {

        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (loadUserFragment) {
            ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right,android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            ft.replace(R.id.main_frame_layout, UserProfileFragment.newInstance(mUser), USER_PROFILE_FRAGMENT_TAG).addToBackStack(null).commit();
            mNavigationView.setCheckedItem(R.id.nav_my_profile);
        } else if (loadSearchUserFragment) {
            ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right,android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            ft.replace(R.id.main_frame_layout, SearchUserFragment.newInstance(), SEARCH_USER_FRAGMENT_TAG).addToBackStack(null).commit();
            mNavigationView.setCheckedItem(R.id.nav_search);
        }
        loadUserFragment = false;
        loadSearchUserFragment = false;

    }

    @Override
    public void onHomeFragmentInteraction(String itemTitle) {
        Log.i(TAG, "onHomeFragmentInteraction: clicked");
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Log.i(TAG, itemTitle);

        if (itemTitle.equals(ContentUtils.EVENTS)) {
//            ft.replace(R.id.main_frame_layout, InformationFragment.newInstance(ContentUtils.EVENTS), EVENTS_FRAGMENT_TAG);
//            ft.addToBackStack(null);
//            ft.commit();
            displaySelectedFragment(R.id.nav_events);

        } else if (itemTitle.equals(ContentUtils.ACHIEVEMENTS)) {
//            ft.replace(R.id.main_frame_layout, InformationFragment.newInstance(ContentUtils.ACHIEVEMENTS), ACHIEVEMENTS_FRAGMENT_TAG);
//            ft.addToBackStack(null);
//            ft.commit();
            displaySelectedFragment(R.id.nav_achieve);
        } else if (itemTitle.equals((ContentUtils.PROJECTS))) {
//            ft.replace(R.id.main_frame_layout, InformationFragment.newInstance(ContentUtils.PROJECTS), PROJECTS_FRAGMENT_TAG);
//            ft.addToBackStack(null);
//            ft.commit();
            displaySelectedFragment(R.id.nav_project);
        } else if (itemTitle.equals(ContentUtils.ABOUT_IEEE)) {
//            ft.replace(R.id.main_frame_layout,new AboutIeeeFragment(), ABOUT_IEEE_FRAGMENT_TAG);
//            ft.addToBackStack(null);
//            ft.commit();
            displaySelectedFragment(R.id.nav_ieee);
        } else if (itemTitle.equals(ContentUtils.IEEE_RESOURCES)) {
//            ft.replace(R.id.main_frame_layout,new IeeeResourcesFragment(), IEEE_RESOURCES_TAG);
//            ft.addToBackStack(null);
//            ft.commit();
            displaySelectedFragment(R.id.nav_resource);
        }
    }

    @Override
    public void onInfoFragmentInteraction(Information info, ImageView sharedImageView) {
        String transitionName = ViewCompat.getTransitionName(sharedImageView);
        Log.i(TAG, "onInfoFragmentInteraction: clicked");
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//        ft.setTransition()
//        ft.sha
//        ft.setCustomAnimations(R.anim.slide_up, 0);
//        ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right,android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        ft.replace(R.id.main_frame_layout, InformationDetailsFragment.newInstance(info, transitionName), "InformationDetailsFragment")
                .addSharedElement(sharedImageView, transitionName)
                .commit();
        ft.addToBackStack(null);
    }

    @Override
    public void onInfoDetailsInteraction(Information information) {
        InformationImageSliderFragment fragment = InformationImageSliderFragment.newInstance(information);
        fragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogFragmentTheme);
        fragment.show(getSupportFragmentManager(), "InformationImageSliderFragment");

    }

    @Override
    public void onSearchUserFragmentInteraction(User user) {
        UserDialogFragment userDialogFragment = UserDialogFragment.newInstance(user);
        userDialogFragment.setEnterTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        userDialogFragment.setExitTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        userDialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogFragmentTheme);
        userDialogFragment.show(getSupportFragmentManager(), "UserDialogFragment");
    }

    @Override
    public void onUserDetailsDialogInteraction(Bundle userDetailsBundle, DialogFragment dialogFragment) {
        dialogFragment.dismiss();
    }

    @Override
    public void onUserProfileInteraction(User user) {
        Intent intent = new Intent(this, EditUserProfileActivity.class);
        intent.putExtra(ContentUtils.USER_KEY, user);
        startActivity(intent);
    }

    @Override
    public void onSignOutClicked() {
//        FirebaseAuth.getInstance().signOut();
//        deleteUserData();
        Intent intent = new Intent(this, SignInActivity.class);
        intent.putExtra(ContentUtils.ACTION_SIGN, ContentUtils.SIGNED_OUT);
        startActivity(intent);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
//        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//        ft.
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onHomeSliderInteraction(String imageUrl) {
        Log.i(TAG, "onHomeSliderInteraction: " + imageUrl);
        ShowFeedImagesDialogFragment dialogFragment = ShowFeedImagesDialogFragment.newInstance(imageUrl);
        dialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogFragmentTheme);
        dialogFragment.show(getSupportFragmentManager(), "ShowFeedImagesDialogFragment");
    }
}
