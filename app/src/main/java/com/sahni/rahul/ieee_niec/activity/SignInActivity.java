package com.sahni.rahul.ieee_niec.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.sahni.rahul.ieee_niec.R;
import com.sahni.rahul.ieee_niec.fragments.GetUserDetailsDialogFragment;
import com.sahni.rahul.ieee_niec.helpers.ContentUtils;
import com.sahni.rahul.ieee_niec.interfaces.OnUserDetailsDialogInteractionListener;
import com.sahni.rahul.ieee_niec.models.User;
import com.sahni.rahul.ieee_niec.models.UserStatus;
import com.sahni.rahul.ieee_niec.networking.ApiService;
import com.sahni.rahul.ieee_niec.networking.NetworkingUtils;
import com.sahni.rahul.ieee_niec.networking.PostUserDetailsResponse;
import com.sahni.rahul.ieee_niec.networking.RetrofitClient;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener
        , OnUserDetailsDialogInteractionListener {

    private static final String TAG = "SignInActivity";
    private GoogleApiClient mGoogleApiClient;
    private int RC_SIGN_IN = 1;
    private ProgressBar mProgressBar;
    private boolean isLoadUserDetailsDialog = false;
//    private User mUser;
    private SignInButton mSignInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mProgressBar = findViewById(R.id.sign_in_progress);
        mProgressBar.getIndeterminateDrawable().setColorFilter(Color.WHITE, android.graphics.PorterDuff.Mode.SRC_IN);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .enableAutoManage(this, this)
                .build();

        mSignInButton = findViewById(R.id.sign_in_button);
        mSignInButton.setSize(SignInButton.SIZE_WIDE);
        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });


    }


    private void signIn() {
        mProgressBar.setVisibility(View.VISIBLE);
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }


    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.getStatus());
        if (result.isSuccess()) {
            mSignInButton.setEnabled(false);
            // Signed in successfully, show authenticated UI.

            final GoogleSignInAccount acct = result.getSignInAccount();
            RetrofitClient.getInstance()
                    .create(ApiService.class)
                    .checkUserStatus(acct.getId())
                    .enqueue(new Callback<UserStatus>() {
                        @Override
                        public void onResponse(Call<UserStatus> call, Response<UserStatus> response) {
                            UserStatus userStatus = response.body();
                            Log.i(TAG, "handleSignInResult :"+response);
                            if(userStatus.getCode() == NetworkingUtils.USER_FOUND){
                                UserStatus.TempUser tempUser = userStatus.getUser();
                                ArrayList<String> interestList = ContentUtils.getInterestArrayList(tempUser.getInterest());
                                User user = new User
                                        (
                                                tempUser.getName(), tempUser.getImageUrl(),
                                                tempUser.getEmailId(), tempUser.getMobileNumber(),
                                                interestList, tempUser.getUserId()
                                        );
                                Toast.makeText(SignInActivity.this, "Welcome back, "+user.getName()+"!",Toast.LENGTH_SHORT).show();
                                saveDetailsLocally(user);
                            } else {

                                String imageUrl = null;
                                if (acct != null) {
                                    Uri imageUri = acct.getPhotoUrl();
                                    if (imageUri != null) {
                                        imageUrl = imageUri.toString();
                                    } else {
                                        imageUrl = "";
                                    }
                                    User user = new User(acct.getDisplayName(), imageUrl, acct.getEmail(), acct.getId());
                                    getAdditionalDetailsFromUser(user);

                                }


                            }
                        }
                        @Override
                        public void onFailure(Call<UserStatus> call, Throwable t) {
                            mSignInButton.setEnabled(true);
                            mProgressBar.setVisibility(View.INVISIBLE);
                            Log.i(TAG,t.getMessage());
                        }
                    });

        } else {
            Log.d(TAG, "handleSignInResult: " + result);
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }



    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(TAG, "onConnectionFailed: " + connectionResult);
    }

    private void saveDetails(final User user) {
        Gson gson = new Gson();
        Log.i(TAG, "saveDetails: "+gson.toJson(user));

        mProgressBar.setVisibility(View.VISIBLE);
        mSignInButton.setEnabled(false);
        RetrofitClient.getInstance()
                .create(ApiService.class)
                .postUserDetails(user)
                .enqueue(new Callback<PostUserDetailsResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<PostUserDetailsResponse> call, @NonNull Response<PostUserDetailsResponse> response) {
                        if(response.isSuccessful()){
                             if(response.body().getCode() == NetworkingUtils.SUCCESS){
                                 saveDetailsLocally(user);
                             } else {
                                 Log.i(TAG, "saveDetails: response not successful"+response.body().getMessage());
                             }
                        }
                    }

                    @Override
                    public void onFailure(Call<PostUserDetailsResponse> call, Throwable t) {
                        Log.i(TAG, "saveDetails: onFailure"+t.getMessage());
                        mProgressBar.setVisibility(View.INVISIBLE);
                        mSignInButton.setEnabled(true);
                    }
                });

    }



    @Override
    public void onUserDetailsDialogInteraction(Bundle userDetailsBundle, DialogFragment dialogFragment) {
        if(userDetailsBundle != null) {
            User user = userDetailsBundle.getParcelable(ContentUtils.USER_KEY);
            saveDetails(user);
            dialogFragment.dismiss();

        } else {
            dialogFragment.dismiss();
        }
    }

    private void saveDetailsLocally(User user){
        Gson gson = new Gson();
        String userString = gson.toJson(user);
        Log.i(TAG, "saveDetailsLocally: " + userString);
        SharedPreferences.Editor editor = getSharedPreferences(ContentUtils.SHARED_PREF,
                Context.MODE_PRIVATE).edit();
        editor.putString(ContentUtils.USER_KEY, userString);
        editor.apply();
        Intent intent = new Intent();
        intent.putExtra(ContentUtils.USER_KEY, user);
        setResult(RESULT_OK, intent);
        finish();

    }

    private void getAdditionalDetailsFromUser(User user){
        GetUserDetailsDialogFragment dialogFragment = GetUserDetailsDialogFragment.newInstance(user);
        dialogFragment.setEnterTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        dialogFragment.setExitTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        dialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogFragmentTheme);
        dialogFragment.show(getSupportFragmentManager(),"dialogFragment");
    }





}
