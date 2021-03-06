package com.sahni.rahul.ieee_niec.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.transition.TransitionInflater;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.sahni.rahul.ieee_niec.R;
import com.sahni.rahul.ieee_niec.fragments.AboutIeeeFragment;
import com.sahni.rahul.ieee_niec.fragments.ExecommFragment;
import com.sahni.rahul.ieee_niec.fragments.HomeFragment;
import com.sahni.rahul.ieee_niec.fragments.IeeeResourcesFragment;
import com.sahni.rahul.ieee_niec.fragments.InformationDetailsFragment;
import com.sahni.rahul.ieee_niec.fragments.InformationFragment;
import com.sahni.rahul.ieee_niec.fragments.SearchUserFragment;
import com.sahni.rahul.ieee_niec.fragments.UserProfileFragment;
import com.sahni.rahul.ieee_niec.helpers.ContentUtils;
import com.sahni.rahul.ieee_niec.interfaces.OnHomeFragmentInteractionListener;
import com.sahni.rahul.ieee_niec.interfaces.OnHomeSliderInteractionListener;
import com.sahni.rahul.ieee_niec.interfaces.OnInfoDetailsFragmentInteractionListener;
import com.sahni.rahul.ieee_niec.interfaces.OnInfoFragmentInteractionListener;
import com.sahni.rahul.ieee_niec.interfaces.OnSearchUserFragmentInteractionListener;
import com.sahni.rahul.ieee_niec.interfaces.OnUserProfileInteractionListener;
import com.sahni.rahul.ieee_niec.models.Feed;
import com.sahni.rahul.ieee_niec.models.Information;
import com.sahni.rahul.ieee_niec.models.User;

import static com.sahni.rahul.ieee_niec.R.id.nav_search;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnHomeFragmentInteractionListener
        , OnInfoFragmentInteractionListener, OnInfoDetailsFragmentInteractionListener,
        OnSearchUserFragmentInteractionListener,OnUserProfileInteractionListener, OnHomeSliderInteractionListener {

    private static final String TAG = "MainActivity";
    private static final int MY_PROFILE_RC = 100;
    private static final int SEARCH_USER_RC = 101;
    private static final int EDIT_PROFILE_RC = 200;
    private static final int PERMISSION_CAMERA_REQUEST_CODE = 600;
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
    private static final String EXECOMM_FRAGMENT_TAG = "execomm_fragment";
    private static final String FRAGMENT_TAG_KEY = "fragment_tag_key";


    private NavigationView mNavigationView;

    private String currentFragmentTag = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationView = findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);

        Log.d(TAG, "Token ="+ FirebaseInstanceId.getInstance().getToken());

        Intent intent = getIntent();
        String dataPayloadType = null;
        if(intent != null){
            dataPayloadType = intent.getStringExtra(ContentUtils.NOTIFICATION_DATA_PAYLOAD_KEY);
        }

        if(dataPayloadType != null){
            switch (dataPayloadType) {
                case ContentUtils.FEEDS_DATA_PAYLOAD:
                    displaySelectedFragment(R.id.nav_home);
                    break;
                case ContentUtils.EVENTS_DATA_PAYLOAD:
                    displaySelectedFragment(R.id.nav_events);
                    break;
                case ContentUtils.ACHIEVEMENT_DATA_PAYLOAD:
                    displaySelectedFragment(R.id.nav_achieve);
                    break;
                case ContentUtils.PROJECT_DATA_PAYLOAD:
                    displaySelectedFragment(R.id.nav_project);
                    break;
                case ContentUtils.EXECOMM_DATA_PAYLOAD:
                    displaySelectedFragment(R.id.nav_execomm);
                    break;
                default:
                    displaySelectedFragment(R.id.nav_home);
            }
        } else {

            if (savedInstanceState != null) {
                String fragmentTag = savedInstanceState.getString(FRAGMENT_TAG_KEY);
                if (fragmentTag != null) {
                    switch (fragmentTag) {
                        case HOME_FRAGMENT_TAG:
                            displaySelectedFragment(R.id.nav_home);
                            break;
                        case EVENTS_FRAGMENT_TAG:
                            displaySelectedFragment(R.id.nav_events);
                            break;
                        case ACHIEVEMENTS_FRAGMENT_TAG:
                            displaySelectedFragment(R.id.nav_achieve);
                            break;
                        case PROJECTS_FRAGMENT_TAG:
                            displaySelectedFragment(R.id.nav_project);
                            break;
                        case ABOUT_IEEE_FRAGMENT_TAG:
                            displaySelectedFragment(R.id.nav_ieee);
                            break;
                        case IEEE_RESOURCES_TAG:
                            displaySelectedFragment(R.id.nav_resource);
                            break;
                        case USER_PROFILE_FRAGMENT_TAG:
                            displaySelectedFragment(R.id.nav_my_profile);
                            break;
                        case SEARCH_USER_FRAGMENT_TAG:
                            displaySelectedFragment(R.id.nav_search);
                            break;
                        case EXECOMM_FRAGMENT_TAG:
                            displaySelectedFragment(R.id.nav_execomm);
                            break;

                    }
                }
            } else {
                displaySelectedFragment(R.id.nav_home);
                mNavigationView.setCheckedItem(R.id.nav_home);

            }
        }


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
        ExecommFragment execommFragment = (ExecommFragment) fm.findFragmentByTag(EXECOMM_FRAGMENT_TAG);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if ((userProfileFragment != null && userProfileFragment.isVisible())
                || (searchUserFragment != null && searchUserFragment.isVisible())
                || (eventsFragment != null && eventsFragment.isVisible())
                || (achievementsFragment != null && achievementsFragment.isVisible())
                || (projectsFragment != null && projectsFragment.isVisible())
                || (ieeeFragment != null && ieeeFragment.isVisible())
                || (ieeeResourcesFragment != null && ieeeResourcesFragment.isVisible())
                || (execommFragment != null && execommFragment.isVisible())) {
            ft.setCustomAnimations(R.anim.slide_back_from_left, R.anim.fade_translate_down);
            ft.replace(R.id.main_frame_layout, HomeFragment.newInstance(), HOME_FRAGMENT_TAG).addToBackStack(null).commit();
            mNavigationView.setCheckedItem(R.id.nav_home);
            currentFragmentTag = HOME_FRAGMENT_TAG;
        } else if (homeFragment != null && homeFragment.isVisible()) {
            finishAffinity();
        } else {
            try {
                super.onBackPressed();
            } catch (Exception e) {
                ft.setCustomAnimations(R.anim.slide_back_from_left, R.anim.fade_translate_down);
                ft.replace(R.id.main_frame_layout, HomeFragment.newInstance(), HOME_FRAGMENT_TAG).addToBackStack(null).commit();
                mNavigationView.setCheckedItem(R.id.nav_home);
                currentFragmentTag = HOME_FRAGMENT_TAG;
            }
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.nav_about_app){
            startActivity(new Intent(this, AboutAppActivity.class));
        } else if(id == R.id.nav_join_ieee){
            Uri uri = Uri.parse(ContentUtils.JOIN_IEEE_URL);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            PackageManager packageManager = getPackageManager();
            if(intent.resolveActivity(packageManager) != null){
                startActivity(intent);
            } else{
                Toast.makeText(this, "Please install browser to continue", Toast.LENGTH_SHORT).show();
            }
        } else if(id == R.id.nav_scan_qr){
            checkPermissionAndStartCamera();
        }
        else {
            displaySelectedFragment(id);
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    void displaySelectedFragment(int menuItemId) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        FirebaseUser firebaseUser;

        switch (menuItemId) {

            case R.id.nav_home:
                if(!currentFragmentTag.equals(HOME_FRAGMENT_TAG)) {
                    ft.setCustomAnimations(R.anim.fade_translate_up, R.anim.slide_to_left);
                    ft.replace(R.id.main_frame_layout, HomeFragment.newInstance(), HOME_FRAGMENT_TAG).addToBackStack(null).commit();
                    mNavigationView.setCheckedItem(R.id.nav_home);
                    currentFragmentTag = HOME_FRAGMENT_TAG;
                }
                break;

            case R.id.nav_events:
                if(!currentFragmentTag.equals(EVENTS_FRAGMENT_TAG)) {
                    ft.setCustomAnimations(R.anim.fade_translate_up, R.anim.slide_to_left);
                    ft.replace(R.id.main_frame_layout, InformationFragment.newInstance(ContentUtils.EVENTS), EVENTS_FRAGMENT_TAG).addToBackStack(null).commit();
                    mNavigationView.setCheckedItem(R.id.nav_events);
                    currentFragmentTag = EVENTS_FRAGMENT_TAG;
                }
                break;

            case R.id.nav_achieve:
                if(!currentFragmentTag.equals(ACHIEVEMENTS_FRAGMENT_TAG)) {
                    ft.setCustomAnimations(R.anim.fade_translate_up, R.anim.slide_to_left);
                    ft.replace(R.id.main_frame_layout, InformationFragment.newInstance(ContentUtils.ACHIEVEMENTS), ACHIEVEMENTS_FRAGMENT_TAG).addToBackStack(null).commit();
                    mNavigationView.setCheckedItem(R.id.nav_achieve);
                    currentFragmentTag = ACHIEVEMENTS_FRAGMENT_TAG;
                }
                break;

            case R.id.nav_project:
                if(!currentFragmentTag.equals(PROJECTS_FRAGMENT_TAG)) {
                    ft.setCustomAnimations(R.anim.fade_translate_up, R.anim.slide_to_left);
                    ft.replace(R.id.main_frame_layout, InformationFragment.newInstance(ContentUtils.PROJECTS), PROJECTS_FRAGMENT_TAG).addToBackStack(null).commit();
                    mNavigationView.setCheckedItem(R.id.nav_project);
                    currentFragmentTag = PROJECTS_FRAGMENT_TAG;
                }
                break;

            case R.id.nav_ieee:
                if(!currentFragmentTag.equals(ABOUT_IEEE_FRAGMENT_TAG)) {
                    ft.setCustomAnimations(R.anim.fade_translate_up, R.anim.slide_to_left);
                    ft.replace(R.id.main_frame_layout, new AboutIeeeFragment(), ABOUT_IEEE_FRAGMENT_TAG).addToBackStack(null).commit();
                    mNavigationView.setCheckedItem(R.id.nav_ieee);
                    currentFragmentTag = ABOUT_IEEE_FRAGMENT_TAG;
                }
                break;

            case R.id.nav_resource:
                if(!currentFragmentTag.equals(IEEE_RESOURCES_TAG)) {
                    ft.setCustomAnimations(R.anim.fade_translate_up, R.anim.slide_to_left);
                    ft.replace(R.id.main_frame_layout, new IeeeResourcesFragment(), IEEE_RESOURCES_TAG).addToBackStack(null).commit();
                    mNavigationView.setCheckedItem(R.id.nav_resource);
                    currentFragmentTag = IEEE_RESOURCES_TAG;
                }
                break;


            case R.id.nav_my_profile:
                if(!currentFragmentTag.equals(USER_PROFILE_FRAGMENT_TAG)) {
                    firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                    if (firebaseUser != null) {
                        mUser = ContentUtils.getUserDataFromSharedPref(this);
                        if (mUser != null) {
                            ft.setCustomAnimations(R.anim.fade_translate_up, R.anim.slide_to_left);
                            ft.replace(R.id.main_frame_layout, UserProfileFragment.newInstance(mUser), USER_PROFILE_FRAGMENT_TAG).addToBackStack(null).commit();
                            mNavigationView.setCheckedItem(R.id.nav_my_profile);
                            currentFragmentTag = USER_PROFILE_FRAGMENT_TAG;
                        } else {
                            startActivityForResult(new Intent(this, SignInActivity.class), MY_PROFILE_RC);
                        }
                    } else {
                        startActivityForResult(new Intent(this, SignInActivity.class), MY_PROFILE_RC);
                    }
                }
                break;

            case nav_search:
                if(!currentFragmentTag.equals(SEARCH_USER_FRAGMENT_TAG)) {
                    firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                    if (firebaseUser != null) {
                        mUser = ContentUtils.getUserDataFromSharedPref(this);
                        if (mUser != null) {
                            ft.setCustomAnimations(R.anim.fade_translate_up, R.anim.slide_to_left);
                            ft.replace(R.id.main_frame_layout, SearchUserFragment.newInstance(), SEARCH_USER_FRAGMENT_TAG).addToBackStack(null).commit();
                            ft.addToBackStack(null);
                            mNavigationView.setCheckedItem(R.id.nav_search);
                            currentFragmentTag = SEARCH_USER_FRAGMENT_TAG;
                        } else {
                            startActivityForResult(new Intent(this, SignInActivity.class), SEARCH_USER_RC);
                        }
                    } else {
                        startActivityForResult(new Intent(this, SignInActivity.class), SEARCH_USER_RC);
                    }
                }
                break;

            case R.id.nav_execomm:
                if(!currentFragmentTag.equals(EXECOMM_FRAGMENT_TAG)){
                ft.setCustomAnimations(R.anim.fade_translate_up,R.anim.slide_to_left);
                ft.replace(R.id.main_frame_layout, ExecommFragment.newInstance(), EXECOMM_FRAGMENT_TAG).addToBackStack(null).commit();
                ft.addToBackStack(null);
                mNavigationView.setCheckedItem(R.id.nav_execomm);
                currentFragmentTag = EXECOMM_FRAGMENT_TAG;
                }
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((requestCode == MY_PROFILE_RC || requestCode == EDIT_PROFILE_RC) && resultCode == RESULT_OK) {
            mUser = data.getParcelableExtra(ContentUtils.USER_KEY);
            loadUserFragment = true;
            loadSearchUserFragment = false;
        } else if (requestCode == SEARCH_USER_RC && resultCode == RESULT_OK) {
            loadSearchUserFragment = true;
            loadUserFragment = false;
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (loadUserFragment) {
            ft.setCustomAnimations(R.anim.fade_translate_up,R.anim.slide_to_left);
            ft.replace(R.id.main_frame_layout, UserProfileFragment.newInstance(mUser), USER_PROFILE_FRAGMENT_TAG).addToBackStack(null).commit();
            mNavigationView.setCheckedItem(R.id.nav_my_profile);
            currentFragmentTag = USER_PROFILE_FRAGMENT_TAG;
        } else if (loadSearchUserFragment) {
            ft.setCustomAnimations(R.anim.fade_translate_up,R.anim.slide_to_left);
            ft.replace(R.id.main_frame_layout, SearchUserFragment.newInstance(), SEARCH_USER_FRAGMENT_TAG).addToBackStack(null).commit();
            mNavigationView.setCheckedItem(R.id.nav_search);
            currentFragmentTag = SEARCH_USER_FRAGMENT_TAG;
        }
        loadUserFragment = false;
        loadSearchUserFragment = false;
    }

    @Override
    public void onHomeFragmentInteraction(String itemTitle) {
        switch (itemTitle) {
            case ContentUtils.EVENTS:
                displaySelectedFragment(R.id.nav_events);
                break;
            case ContentUtils.ACHIEVEMENTS:
                displaySelectedFragment(R.id.nav_achieve);
                break;
            case (ContentUtils.PROJECTS):
                displaySelectedFragment(R.id.nav_project);
                break;
            case ContentUtils.ABOUT_IEEE:
                displaySelectedFragment(R.id.nav_ieee);
                break;
            case ContentUtils.IEEE_RESOURCES:
                displaySelectedFragment(R.id.nav_resource);
                break;
        }
    }

    @Override
    public void onInfoFragmentInteraction(View sharedView, Information info) {

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
        ft.setCustomAnimations(R.anim.fade_translate_up,R.anim.slide_to_left,
                R.anim.slide_back_from_left, R.anim.fade_translate_down)
                .replace(R.id.main_frame_layout, InformationDetailsFragment.newInstance(info, null),
                        "InformationDetailsFragment")
                .addToBackStack("InformationDetailsFragment")
                .commit();
        } else {
            String transitionName = ViewCompat.getTransitionName(sharedView);
            InformationDetailsFragment fragment = InformationDetailsFragment.newInstance(info, transitionName);
            fragment.setSharedElementEnterTransition(TransitionInflater.from(this).
                    inflateTransition(android.R.transition.move));
            ft.addSharedElement(sharedView, transitionName).
                    replace(R.id.main_frame_layout, fragment,
                            "InformationDetailsFragment")
                    .addToBackStack("InformationDetailsFragment")
                    .commit();
        }
    }

    @Override
    public void onInfoDetailsInteraction(View view, Information information) {
        Intent intent = new Intent(this, InformationImageSliderActivity.class);
        intent.putExtra(ContentUtils.INFO_KEY, information);
        startActivity(intent);
    }

    @Override
    public void onSearchUserFragmentInteraction(User user, View sharedView) {

        String transitionName = ViewCompat.getTransitionName(sharedView);
        Intent intent = new Intent(this, UserProfileActivity.class);
        intent.putExtra(ContentUtils.USER_KEY, user);
        intent.putExtra(ContentUtils.TRANSITION_NAME, transitionName);
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat
                .makeSceneTransitionAnimation(this, sharedView, transitionName);
        startActivity(intent, optionsCompat.toBundle());
    }


    @Override
    public void editProfile(User user) {
        Intent intent = new Intent(this, EditUserProfileActivity.class);
        intent.putExtra(ContentUtils.USER_KEY, user);
        startActivityForResult(intent, EDIT_PROFILE_RC);
    }

    @Override
    public void signOut() {
        Intent intent = new Intent(this, SignInActivity.class);
        intent.putExtra(ContentUtils.ACTION_SIGN, ContentUtils.SIGNED_OUT);
        startActivity(intent);
    }

    @Override
    public void deleteAccount() {
        Intent intent = new Intent(this, SignInActivity.class);
        intent.putExtra(ContentUtils.ACTION_SIGN, ContentUtils.DELETE_ACCOUNT);
        startActivity(intent);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(FRAGMENT_TAG_KEY, currentFragmentTag);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onHomeSliderInteraction(View view, Feed feed) {
        Intent intent = new Intent(this, ShowFeedImageActivity.class);
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(this, view, ViewCompat.getTransitionName(view));
        intent.putExtra(ContentUtils.FEED_KEY, feed);
        startActivity(intent, options.toBundle());
    }

    private void checkPermissionAndStartCamera(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

            switch (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)){

                case PackageManager.PERMISSION_DENIED:
                    Log.i(TAG, "onCreate: requesting permission");
                    if(ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.CAMERA)){
                        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                                .setTitle("Permission required")
                                .setMessage("Camera permission is required to scan QR Code")
                                .setNeutralButton("Okay", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        ActivityCompat.requestPermissions(MainActivity.this,
                                                new String[]{Manifest.permission.CAMERA}, PERMISSION_CAMERA_REQUEST_CODE);
                                    }
                                })
                                .setCancelable(false);
                        builder.create().show();
                    } else {
                        ActivityCompat.requestPermissions(this,
                                new String[]{Manifest.permission.CAMERA}, PERMISSION_CAMERA_REQUEST_CODE);
                    }
                    break;


                case PackageManager.PERMISSION_GRANTED:
                    Log.i(TAG, "onCreate: permission granted");
                    scanQRCode();
                    break;

            }
        } else {
            scanQRCode();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == PERMISSION_CAMERA_REQUEST_CODE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                scanQRCode();
            } else {
                Log.i(TAG, "onRequestPermissionsResult: permission denied");
//                Toast.makeText(this, "Please give camera permission to scan QR Code", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void scanQRCode(){
        startActivity(new Intent(this, ScanQrCodeActivity.class));
    }
}
