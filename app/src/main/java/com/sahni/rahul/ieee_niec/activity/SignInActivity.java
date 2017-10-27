package com.sahni.rahul.ieee_niec.activity;

import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.sahni.rahul.ieee_niec.R;
import com.sahni.rahul.ieee_niec.fragments.GetUserDetailsDialogFragment;
import com.sahni.rahul.ieee_niec.helpers.ContentUtils;
import com.sahni.rahul.ieee_niec.interfaces.OnUserDetailsDialogInteractionListener;
import com.sahni.rahul.ieee_niec.models.FirestoreUser;

import static com.sahni.rahul.ieee_niec.helpers.ContentUtils.STATE_RESOLVING_ERROR;

public class SignInActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener
        , OnUserDetailsDialogInteractionListener, GoogleApiClient.ConnectionCallbacks {

    private static final String TAG = "SignInActivity";
    private static final int RC_ACCPICK = 100;
    private GoogleApiClient mGoogleApiClient;
    private int RC_SIGN_IN = 1;
    private ProgressBar mProgressBar;
    private boolean isLoadUserDetailsDialog = false;
    //    private User mUser;
    private SignInButton mSignInButton;

    private AlertDialog mAlertDialog;

    private FirebaseAuth mAuth;
    private int REQUEST_RESOLVE_ERROR = 1001;

    private String mAction;
    private boolean mResolvingError = false;

    private CollectionReference mUsersCollection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            mResolvingError = savedInstanceState.getBoolean(STATE_RESOLVING_ERROR, false);
        }

        setContentView(R.layout.activity_sign_in);

        mUsersCollection = FirebaseFirestore.getInstance().collection("users");

        Intent intent = getIntent();
        mAction = intent.getStringExtra(ContentUtils.ACTION_SIGN);

        mAuth = FirebaseAuth.getInstance();

        mProgressBar = findViewById(R.id.sign_in_progress);
        mProgressBar.getIndeterminateDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
//                .enableAutoManage(this, this)
//                .set
                .build();


        mSignInButton = findViewById(R.id.sign_in_button);
        mSignInButton.setSize(SignInButton.SIZE_WIDE);
        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();


//                startActivityForResult(AccountPicker.newChooseAccountIntent(null,
//                        null, new String[]{GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE}, true, null, null, null, null),
//                        RC_ACCPICK);
            }
        });

        if (mAction != null && mAction.equals(ContentUtils.SIGNED_OUT)) {
            mSignInButton.setEnabled(false);
            View dialogView = getLayoutInflater().inflate(R.layout.progress_dialog_layout, null);
            TextView progressTextView = dialogView.findViewById(R.id.progress_text_view);
            progressTextView.setText("Signing out...");
            mAlertDialog = new AlertDialog.Builder(this)
                    .setView(dialogView)
                    .setCancelable(false)
                    .create();
            mAlertDialog.show();

        } else if (mAction != null && mAction.equals(ContentUtils.DELETE_ACCOUNT)) {
            mSignInButton.setEnabled(false);
            View dialogView = getLayoutInflater().inflate(R.layout.progress_dialog_layout, null);
            TextView progressTextView = dialogView.findViewById(R.id.progress_text_view);
            progressTextView.setText("Deleting...");
            mAlertDialog = new AlertDialog.Builder(this)
                    .setView(dialogView)
                    .setCancelable(false)
                    .create();
            mAlertDialog.show();
        }


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
        Log.i(TAG, "onActivityResult: checking result code");
        if (requestCode == RC_SIGN_IN) {
            Log.i(TAG, "onActivityResult: result code matched");
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
        Log.d(TAG, "handleSignInResult:" + result.getStatus());
        if (result.isSuccess()) {
            mSignInButton.setEnabled(false);

            final GoogleSignInAccount acct = result.getSignInAccount();
            firebaseAuthWithGoogle(acct);
//            getUserDetails(acct);

            Log.i(TAG, "handleSignInResult: name = " + acct.getDisplayName());
            Log.i(TAG, "handleSignInResult: id token = " + acct.getIdToken());
            Log.i(TAG, "handleSignInResult: photo url = " + acct.getPhotoUrl());

        } else {
            Log.d(TAG, "handleSignInResult: " + result);
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.i(TAG, "firebaseAuthWithGoogle: task complete");
                        if (task.isSuccessful()) {
                            Log.i(TAG, "firebaseAuthWithGoogle: task successful");
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            getUserDetails(firebaseUser);


                        } else {
                            Log.i(TAG, "firebaseAuthWithGoogle: task unsuccessful");
                            mProgressBar.setVisibility(View.INVISIBLE);
                            mSignInButton.setEnabled(true);
                            Toast.makeText(SignInActivity.this, "Login failed, try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i(TAG, "firebaseAuthWithGoogle: " + e);
                    }
                });
    }

    private void getUserDetails(final FirebaseUser firebaseUser) {

        mUsersCollection.document(firebaseUser.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                FirestoreUser user = document.toObject(FirestoreUser.class);
                                Log.i(TAG, "getUserDetails: success: " + user.getEmailId());
                                saveDetails(user);
                                Toast.makeText(SignInActivity.this, "Welcome back, " + user.getName() + "!", Toast.LENGTH_SHORT).show();

                            } else {
                                Log.d(TAG, "No such document");

                                FirestoreUser user = new FirestoreUser(
                                        firebaseUser.getUid(), firebaseUser.getDisplayName(),
                                        firebaseUser.getEmail(), firebaseUser.getPhotoUrl().toString()
                                );

                                getAdditionalDetailsFromUser(user);
                            }
                        } else {
                            Log.i(TAG, "getUserDetails: failed: " + task.getException());
                            mProgressBar.setVisibility(View.INVISIBLE);
                            mSignInButton.setEnabled(true);
                            Snackbar.make(mProgressBar, "Sign in failed!, Try Again", Snackbar.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        if (mAuth == null) {
            mAuth = FirebaseAuth.getInstance();
        }
        super.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();

    }

    private void signOut() {
//        mGoogleApiClient.connect();
        Auth.GoogleSignInApi.signOut(mGoogleApiClient)
                .setResultCallback(
                        new ResultCallback<Status>() {
                            @Override
                            public void onResult(@NonNull Status status) {
                                FirebaseAuth.getInstance().signOut();
                                ContentUtils.deleteUserDataFromSharedPref(SignInActivity.this);
//                                deleteUserData();
                                Snackbar.make(mProgressBar, "Goodbye!", Snackbar.LENGTH_LONG).show();
//                                Toast.makeText(SignInActivity.this, "Signed Out, Goodbye!", Toast.LENGTH_SHORT).show();
                                mAlertDialog.dismiss();
                                mSignInButton.setEnabled(true);
                            }
                        }
                );
    }

    private void deleteAccount() {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient)
                .setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        if (status.isSuccess()) {
                            mUsersCollection.document(firebaseUser.getUid())
                                    .delete()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            ContentUtils.deleteUserDataFromSharedPref(SignInActivity.this);
                                            Snackbar.make(mProgressBar, "Goodbye!", Snackbar.LENGTH_LONG).show();
                                            mAlertDialog.dismiss();
                                            mSignInButton.setEnabled(true);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.i(TAG, "deleteAccount: couldn't delete: " + e.getMessage());
                                        }
                                    });
                        }
                    }
                });
    }


    private void saveDetails(final FirestoreUser user) {
        Gson gson = new Gson();
        Log.i(TAG, "saveDetails: " + gson.toJson(user));

        mProgressBar.setVisibility(View.VISIBLE);
        mSignInButton.setEnabled(false);

        mUsersCollection.document(user.getuId())
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(TAG, "saveDetails: Success");
                        saveDetailsLocally(user);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i(TAG, "saveDetails: Failed: " + e.getMessage());
                        mProgressBar.setVisibility(View.INVISIBLE);
                        mSignInButton.setEnabled(true);
                        Snackbar.make(mProgressBar, "Sign in failed!, Try Again", Snackbar.LENGTH_SHORT).show();

                    }
                });
//
    }


    @Override
    public void onUserDetailsDialogInteraction(Bundle userDetailsBundle, DialogFragment dialogFragment) {
        if (userDetailsBundle != null) {
            FirestoreUser user = userDetailsBundle.getParcelable(ContentUtils.USER_KEY);
            saveDetails(user);
            dialogFragment.dismiss();

        } else {
            dialogFragment.dismiss();
        }
    }

    private void saveDetailsLocally(FirestoreUser user) {
        ContentUtils.saveUserDataInSharedPref(user, this);
        Intent intent = new Intent();
        intent.putExtra(ContentUtils.USER_KEY, user);
        setResult(RESULT_OK, intent);
        finish();


    }

    private void getAdditionalDetailsFromUser(FirestoreUser user) {
        GetUserDetailsDialogFragment dialogFragment = GetUserDetailsDialogFragment.newInstance(user);
        dialogFragment.setEnterTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        dialogFragment.setExitTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        dialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogFragmentTheme);
        dialogFragment.show(getSupportFragmentManager(), "dialogFragment");
    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
//        super.onBackPressed();
    }

    private void deleteUserData() {
        SharedPreferences.Editor editor = getSharedPreferences(ContentUtils.SHARED_PREF, MODE_PRIVATE).edit();
        editor.clear();
        editor.apply();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (mAction != null && mAction.equals(ContentUtils.SIGNED_OUT)) {
            signOut();
        } else if (mAction != null && mAction.equals(ContentUtils.DELETE_ACCOUNT)) {
            deleteAccount();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (mResolvingError) {
            return;
        } else if (connectionResult.hasResolution()) {
            try {
                mResolvingError = true;
                connectionResult.startResolutionForResult(this, REQUEST_RESOLVE_ERROR);
            } catch (IntentSender.SendIntentException e) {
                mGoogleApiClient.connect();
            }
        } else {
            GoogleApiAvailability.getInstance().getErrorDialog(this, connectionResult.getErrorCode(), REQUEST_RESOLVE_ERROR).show();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(STATE_RESOLVING_ERROR, mResolvingError);
    }
}
