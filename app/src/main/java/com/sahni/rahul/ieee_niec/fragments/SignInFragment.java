package com.sahni.rahul.ieee_niec.fragments;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.sahni.rahul.ieee_niec.R;
import com.sahni.rahul.ieee_niec.helpers.ContentUtils;
import com.sahni.rahul.ieee_niec.models.User;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignInFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    private static final int RC_SIGN_IN = 1 ;
    private static final int SIGNED_IN = 2 ;
    private static final int REQUEST_RESOLVE_ERROR = 100;
    private GoogleApiClient mGoogleApiClient;
    private GoogleSignInOptions mGoogleSignInOptions;
    private static final String TAG = "SignInFragment";
    private boolean mResolvingError = false;
    private static final String ERROR_CODE_KEY = "error_code_key";


    public SignInFragment() {
        // Required empty public constructor
    }

    public static SignInFragment newInstance(){
        return new SignInFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_in, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

         SignInButton signInButton = view.findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_WIDE);

        if(mGoogleSignInOptions == null){
            mGoogleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();
        }

        if(mGoogleApiClient == null){
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addApi(Auth.GOOGLE_SIGN_IN_API, mGoogleSignInOptions)
//                .enableAutoManage(getActivity(), this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
        }


        if(isUserSigned()){
            signInButton.setEnabled(false);
            Toast.makeText(getActivity(), "Welcome!", Toast.LENGTH_SHORT).show();
        } else {

            signInButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    signIn();
                }
            });
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    private void signIn() {
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
        } else if (requestCode == REQUEST_RESOLVE_ERROR) {
            mResolvingError = false;
            if (resultCode == RESULT_OK) {
                // Make sure the app is not already connected or attempting to connect
                if (!mGoogleApiClient.isConnecting() &&
                        !mGoogleApiClient.isConnected()) {
                    mGoogleApiClient.connect();
                }
            }
        }
    }


    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            Toast.makeText(getActivity(),"Welcome "+acct.getDisplayName(),Toast.LENGTH_SHORT).show();
//            User user = new User(acct.getDisplayName(), acct.getEmail(), acct.getId());



//            saveDetails(user);

//            setSignInStatus(SIGNED_IN, acct.getId());
//            mStatusTextView.setText(getString(R.string.signed_in_fmt, acct.getDisplayName()));
//            updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
//            updateUI(false);
        }
    }

    private void saveDetails(User user) {
        Gson gson = new Gson();
        String userString = gson.toJson(user);
        Log.i(TAG, "saveDetails: "+userString);
        SharedPreferences.Editor editor = getActivity().getSharedPreferences(ContentUtils.SHARED_PREF,
                Context.MODE_PRIVATE).edit();
        editor.putString(ContentUtils.USER_KEY, userString);
        editor.apply();
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (mResolvingError) {
            // Already attempting to resolve an error.
            return;
        } else if (connectionResult.hasResolution()) {
            try {
                mResolvingError = true;
                connectionResult.startResolutionForResult(getActivity(), REQUEST_RESOLVE_ERROR);
            } catch (IntentSender.SendIntentException e) {
                // There was an error with the resolution intent. Try again.
                mGoogleApiClient.connect();
            }
        } else {
            showErrorDialog(connectionResult.getErrorCode());
            mResolvingError = true;
        }
    }


    private void showErrorDialog(int errorCode){
        ErrorDialog dialog = new ErrorDialog();
        Bundle bundle = new Bundle();
        bundle.putInt(ERROR_CODE_KEY,errorCode);
        dialog.setArguments(bundle);
        dialog.show(getChildFragmentManager(), null);
    }

    private boolean isUserSigned(){

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(ContentUtils.SHARED_PREF, Context.MODE_PRIVATE);
        String signInStatus = sharedPreferences.getString(ContentUtils.USER_KEY, ContentUtils.SIGNED_OUT);

        return !signInStatus.equals(ContentUtils.SIGNED_OUT);
    }

//    private void setSignInStatus(int status, String userId){
//        SharedPreferences.Editor editor = getActivity().getSharedPreferences(ContentUtils.SHARED_PREF,
//                Context.MODE_PRIVATE).edit();
//
//        if(status == SIGNED_IN){
//
//            editor.putString(ContentUtils.USER_ID, userId);
//        } else {
//
//            editor.putString(ContentUtils.USER_ID, ContentUtils.SIGNED_OUT);
//        }
//
//        editor.apply();
//    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    private void onDialogDismissed(){
        mResolvingError = false;
    }


    public static class ErrorDialog extends DialogFragment{

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            int errorCode = this.getArguments().getInt(ERROR_CODE_KEY);
            return GoogleApiAvailability.getInstance().getErrorDialog(getActivity(), errorCode ,REQUEST_RESOLVE_ERROR);
        }

        @Override
        public void onDismiss(DialogInterface dialog) {
            ((SignInFragment)getParentFragment()).onDialogDismissed();
        }
    }

}
