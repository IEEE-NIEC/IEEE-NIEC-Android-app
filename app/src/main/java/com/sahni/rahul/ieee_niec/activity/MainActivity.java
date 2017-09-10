package com.sahni.rahul.ieee_niec.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.sahni.rahul.ieee_niec.R;
import com.sahni.rahul.ieee_niec.fragments.HomeFragment;
import com.sahni.rahul.ieee_niec.fragments.ImageSliderBottomSheetFragment;
import com.sahni.rahul.ieee_niec.fragments.InfoDetailsFragment;
import com.sahni.rahul.ieee_niec.fragments.InformationFragment;
import com.sahni.rahul.ieee_niec.fragments.SearchUserFragment;
import com.sahni.rahul.ieee_niec.fragments.UserDialogFragment;
import com.sahni.rahul.ieee_niec.fragments.UserFragment;
import com.sahni.rahul.ieee_niec.helpers.ContentUtils;
import com.sahni.rahul.ieee_niec.interfaces.OnHomeFragmentInteractionListener;
import com.sahni.rahul.ieee_niec.interfaces.OnInfoDetailsFragmentInteractionListener;
import com.sahni.rahul.ieee_niec.interfaces.OnInfoFragmentInteractionListener;
import com.sahni.rahul.ieee_niec.interfaces.OnSearchUserFragmentInteractionListener;
import com.sahni.rahul.ieee_niec.interfaces.OnUserDetailsDialogInteractionListener;
import com.sahni.rahul.ieee_niec.models.Information;
import com.sahni.rahul.ieee_niec.models.User;
import com.sahni.rahul.ieee_niec.models.UserSingInStatus;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnHomeFragmentInteractionListener
        , OnInfoFragmentInteractionListener, OnInfoDetailsFragmentInteractionListener,
                OnSearchUserFragmentInteractionListener, OnUserDetailsDialogInteractionListener {

    private static final String TAG = "MainActivity";
    private static final int MY_PROFILE_RC = 100;
    private static final int SEARCH_USER_RC = 101;
    private User mUser;
    private boolean loadUserFragment = false;
    private boolean loadSearchUserFragment = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.addDrawerListener(toggle);
//        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        displaySelectedFragment(R.id.nav_home);
        navigationView.setCheckedItem(R.id.nav_home);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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
                ft.replace(R.id.main_frame_layout, HomeFragment.newInstance(), "HomeFragment").commit();
                break;

            case R.id.nav_ieee:
//                startActivity(new Intent(this, SignInActivity.class));

//                ft.replace(R.id.main_frame_layout, SignInFragment.newInstance()).commit();
//                ft.addToBackStack(null);
//                UserSingInStatus singInStatus = isUserSigned();
//                if (singInStatus.getStatus()) {
//                    User user = singInStatus.getUser();
//                    Log.i(TAG, "displaySelectedFragment: name: " + user.getName() + " \n email: " + user.getEmailId() +
//                            "\n id: " + user.getUserId());
////                    ft.replace(R.id.main_frame_layout, UserFragment.newInstance(user)).commit();
//                    ft.replace(R.id.main_frame_layout, AccountFragment.newInstance(user)).commit();
//                    ft.addToBackStack(null);
//                } else {
//                    startActivityForResult(new Intent(this, SignInActivity.class), SIGN_IN_RC);
//                }
                break;


            case R.id.nav_my_profile:
                singInStatus = isUserSigned();
                if (singInStatus.getStatus()) {
                    User user = singInStatus.getUser();
                    Log.i(TAG, "displaySelectedFragment: name: " + user.getName() + " \n email: " + user.getEmailId() +
                            "\n id: " + user.getUserId());
//                    ft.replace(R.id.main_frame_layout, UserFragment.newInstance(user)).commit();
                    ft.replace(R.id.main_frame_layout, UserFragment.newInstance(user),"UserFragment").commit();
                    ft.addToBackStack(null);
                } else {
                    startActivityForResult(new Intent(this, SignInActivity.class), MY_PROFILE_RC);
                }
                break;

            case R.id.nav_search:
                singInStatus = isUserSigned();
                if (singInStatus.getStatus()) {
                    User user = singInStatus.getUser();
                    Log.i(TAG, "displaySelectedFragment: name: " + user.getName() + " \n email: " + user.getEmailId() +
                            "\n id: " + user.getUserId());
//                    ft.replace(R.id.main_frame_layout, UserFragment.newInstance(user)).commit();
                    ft.replace(R.id.main_frame_layout, SearchUserFragment.newInstance(),"SearchUserFragment").commit();
                    ft.addToBackStack(null);
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
        } else if(requestCode == SEARCH_USER_RC && resultCode == RESULT_OK){
            loadSearchUserFragment = true;
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (loadUserFragment) {


//            ft.replace(R.id.main_frame_layout, UserFragment.newInstance(mUser)).commit();
            ft.replace(R.id.main_frame_layout, UserFragment.newInstance(mUser),"UserFragment").commit();
            ft.addToBackStack(null);
        } else if(loadSearchUserFragment){
            ft.replace(R.id.main_frame_layout, SearchUserFragment.newInstance(),"SearchUserFragment").commit();
            ft.addToBackStack(null);
        }
        loadUserFragment = false;
        loadSearchUserFragment = false;

    }

    @Override
    public void onHomeFragmentInteraction(String itemTitle) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Log.i(TAG, itemTitle);

        if (itemTitle.equals(ContentUtils.EVENTS)) {
            ft.replace(R.id.main_frame_layout, InformationFragment.newInstance(ContentUtils.EVENTS),"InformationFragment");
            ft.commit();
            ft.addToBackStack(null);
        } else if (itemTitle.equals(ContentUtils.ACHIEVEMENTS)) {

        } else if (itemTitle.equals(ContentUtils.IEEE)) {

        } else if (itemTitle.equals((ContentUtils.PROJECTS))) {

        }
    }

    @Override
    public void onInfoFragmentInteraction(Information info) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.main_frame_layout, InfoDetailsFragment.newInstance(info),"InfoDetailsFragment").commit();
        ft.addToBackStack(null);
    }

    @Override
    public void onInfoDetailsInteraction(Information information) {
        ImageSliderBottomSheetFragment fragment = ImageSliderBottomSheetFragment.newInstance(information);
        fragment.show(getSupportFragmentManager(), "ImageSliderBottomSheetFragment");
    }

    @Override
    public void onSearchUserFragmentInteraction(User user) {
        UserDialogFragment userDialogFragment = UserDialogFragment.newInstance(user);
        userDialogFragment.setEnterTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        userDialogFragment.setExitTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        userDialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogFragmentTheme);
        userDialogFragment.show(getSupportFragmentManager(),"UserDialogFragment");
    }

    @Override
    public void onUserDetailsDialogInteraction(Bundle userDetailsBundle, DialogFragment dialogFragment) {
        dialogFragment.dismiss();
    }
}
