package com.sahni.rahul.ieee_niec.activity;

import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
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
import com.sahni.rahul.ieee_niec.R;
import com.sahni.rahul.ieee_niec.helpers.ContentUtils;
import com.sahni.rahul.ieee_niec.models.User;

import static com.sahni.rahul.ieee_niec.helpers.ContentUtils.STATE_RESOLVING_ERROR;

public class SignInActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener
        ,GoogleApiClient.ConnectionCallbacks {

    private static final String TAG = "SignInActivity";
    private static final int RC_GET_USER_DETAILS = 600;
    private GoogleApiClient mGoogleApiClient;
    private int RC_SIGN_IN = 1;
    private ProgressBar mProgressBar;
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

        ImageView closeImageView = findViewById(R.id.close_image_view);
        closeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();


        mSignInButton = findViewById(R.id.sign_in_button);
        mSignInButton.setSize(SignInButton.SIZE_WIDE);
        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
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
        } else if(requestCode == RC_GET_USER_DETAILS){
            if(resultCode == RESULT_OK){
                User user = data.getParcelableExtra(ContentUtils.USER_KEY);
                saveDetails(user);
            } else {
                mProgressBar.setVisibility(View.INVISIBLE);
                mSignInButton.setEnabled(true);
                Snackbar.make(mProgressBar, "Sign in failed!, Try Again", Snackbar.LENGTH_SHORT).show();
            }
        }
    }


    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            mSignInButton.setEnabled(false);

            final GoogleSignInAccount acct = result.getSignInAccount();
            firebaseAuthWithGoogle(acct);
        } else {
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            checkForUserDetailsOnline(firebaseUser);


                        } else {
                            mProgressBar.setVisibility(View.INVISIBLE);
                            mSignInButton.setEnabled(true);
                            Toast.makeText(SignInActivity.this, "Login failed, try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }

    private void checkForUserDetailsOnline(final FirebaseUser firebaseUser) {

        mUsersCollection.document(firebaseUser.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                User user = document.toObject(User.class);
                                Toast.makeText(SignInActivity.this, "Welcome back, " + user.getName() + "!", Toast.LENGTH_SHORT).show();
                                saveDetailsLocally(user);

                            } else {
                                User user = new User(
                                        firebaseUser.getUid(), firebaseUser.getDisplayName(),
                                        firebaseUser.getEmail(), firebaseUser.getPhotoUrl().toString()
                                );

                                getAdditionalDetailsFromUser(user);
                            }
                        } else {
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
        Auth.GoogleSignInApi.signOut(mGoogleApiClient)
                .setResultCallback(
                        new ResultCallback<Status>() {
                            @Override
                            public void onResult(@NonNull Status status) {
                                FirebaseAuth.getInstance().signOut();
                                ContentUtils.deleteUserDataFromSharedPref(SignInActivity.this);
                                Snackbar.make(mProgressBar, "Goodbye!", Snackbar.LENGTH_LONG).show();
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
                                        }
                                    });
                        }
                    }
                });
    }


    private void saveDetails(final User user) {
        mProgressBar.setVisibility(View.VISIBLE);
        mSignInButton.setEnabled(false);

        mUsersCollection.document(user.getuId())
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(SignInActivity.this, "Welcome, " + user.getName() + "!", Toast.LENGTH_SHORT).show();
                        saveDetailsLocally(user);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mProgressBar.setVisibility(View.INVISIBLE);
                        mSignInButton.setEnabled(true);
                        Snackbar.make(mProgressBar, "Sign in failed!, Try Again", Snackbar.LENGTH_SHORT).show();

                    }
                });
    }



    private void saveDetailsLocally(User user) {
        ContentUtils.saveUserDataInSharedPref(user, this);
        Intent intent = new Intent();
        intent.putExtra(ContentUtils.USER_KEY, user);
        setResult(RESULT_OK, intent);
        finish();


    }

    private void getAdditionalDetailsFromUser(User user) {
        Intent intent = new Intent(this, GetUserDetailsActivity.class);
        intent.putExtra(ContentUtils.USER_KEY, user);
        startActivityForResult(intent, RC_GET_USER_DETAILS);
    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
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
