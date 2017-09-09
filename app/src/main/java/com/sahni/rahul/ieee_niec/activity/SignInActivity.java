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

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.sahni.rahul.ieee_niec.R;
import com.sahni.rahul.ieee_niec.fragments.UserDetailsDialogFragment;
import com.sahni.rahul.ieee_niec.helpers.ContentUtils;
import com.sahni.rahul.ieee_niec.interfaces.OnUserDetailsDialogInteractionListener;
import com.sahni.rahul.ieee_niec.models.User;
import com.sahni.rahul.ieee_niec.models.UserSingInStatus;
import com.sahni.rahul.ieee_niec.networking.ApiService;
import com.sahni.rahul.ieee_niec.networking.NetworkingUtils;
import com.sahni.rahul.ieee_niec.networking.PostUserDetailsResponse;
import com.sahni.rahul.ieee_niec.networking.RetrofitClient;

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
    private User mUser;

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

        final SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_WIDE);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

//        UserSingInStatus singInStatus = isUserSigned();
//
//        if(singInStatus.getStatus()){
//            signInButton.setEnabled(false);
//            Toast.makeText(this,"Welcome!", Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent();
//            intent.putExtra(ContentUtils.USER_KEY, singInStatus.getUser());
//            setResult(RESULT_OK, intent);
//            finish();
//        } else {
//            signInButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    signIn();
//                }
//            });
//        }


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
        mProgressBar.setVisibility(View.INVISIBLE);
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
//            Toast.makeText(this,"Welcome "+acct.getDisplayName(),Toast.LENGTH_SHORT).show();
//            Toast.makeText(getActivity(),"Welcome "+acct.getDisplayName(),Toast.LENGTH_SHORT).show();

//            User user;

            acct.


            String imageUrl = null;
            if (acct != null) {
                Uri imageUri = acct.getPhotoUrl();
                if (imageUri != null) {
                    imageUrl = imageUri.toString();
                }
                mUser = new User(acct.getDisplayName(), imageUrl, acct.getEmail(), acct.getId());
//                isLoadUserDetailsDialog = true;
                UserDetailsDialogFragment dialogFragment = UserDetailsDialogFragment.newInstance(mUser);
//                dialogFragment.setCancelable(false);
                dialogFragment.setEnterTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                dialogFragment.setExitTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                dialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogFragmentTheme);
                dialogFragment.show(getSupportFragmentManager(),"dialogFragment");
            }


//            mStatusTextView.setText(getString(R.string.signed_in_fmt, acct.getDisplayName()));
//            updateUI(true);
        } else {
            Log.d(TAG, "handleSignInResult: " + result);
//            result.getStatus()
            // Signed out, show unauthenticated UI.
//            updateUI(false);
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
//        if(isLoadUserDetailsDialog){
//            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//            ft.add(android.R.id.content, UserDetailsDialogFragment.newInstance(mUser))
//                    .addToBackStack(null)
//                    .commit();
//        }

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(TAG, "onConnectionFailed: " + connectionResult);
    }

    private void saveDetails(final User user) {
//        Gson gson = new Gson();
//        String userString = gson.toJson(user);
//        Log.i(TAG, "saveDetails: " + userString);
//        SharedPreferences.Editor editor = getSharedPreferences(ContentUtils.SHARED_PREF,
//                Context.MODE_PRIVATE).edit();
//        editor.putString(ContentUtils.USER_KEY, userString);
//        editor.apply();

        RetrofitClient.getInstance()
                .create(ApiService.class)
                .postUserDetails(user)
                .enqueue(new Callback<PostUserDetailsResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<PostUserDetailsResponse> call, @NonNull Response<PostUserDetailsResponse> response) {
                        if(response.isSuccessful()){
                             if(response.body().getCode() == NetworkingUtils.SUCCESS){
                                 Gson gson = new Gson();
                                 String userString = gson.toJson(user);
                                 Log.i(TAG, "saveDetails: " + userString);
                                 SharedPreferences.Editor editor = getSharedPreferences(ContentUtils.SHARED_PREF,
                                         Context.MODE_PRIVATE).edit();
                                 editor.putString(ContentUtils.USER_KEY, userString);
                                 editor.apply();

                                 Intent intent = new Intent();
                                 intent.putExtra(ContentUtils.USER_KEY, user);
                                 setResult(RESULT_OK, intent);
                                 finish();
                             } else {
                                 Log.i(TAG, "saveDetails: "+response.body().getMessage());
                             }
                        }
                    }

                    @Override
                    public void onFailure(Call<PostUserDetailsResponse> call, Throwable t) {
                        Log.i(TAG, "saveDetails: "+t.getMessage());
                    }
                });

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
    public void onUserDetailsDialogInteraction(Bundle userDetailsBundle, UserDetailsDialogFragment dialogFragment) {
        if(userDetailsBundle != null) {
            User user = userDetailsBundle.getParcelable(ContentUtils.USER_KEY);
            saveDetails(user);
            dialogFragment.dismiss();

        } else {
            dialogFragment.dismiss();
        }


    }
}
