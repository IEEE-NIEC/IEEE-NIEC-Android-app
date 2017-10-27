package com.sahni.rahul.ieee_niec.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sahni.rahul.ieee_niec.R;
import com.sahni.rahul.ieee_niec.adapter.InterestAdapter;
import com.sahni.rahul.ieee_niec.helpers.ContentUtils;
import com.sahni.rahul.ieee_niec.interfaces.OnRemoveInterestClickListener;
import com.sahni.rahul.ieee_niec.models.FirestoreUser;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;

public class EditUserProfileActivity extends AppCompatActivity implements OnRemoveInterestClickListener {

    private static final String TAG = "EditUserProfileActivity";
    private static final int PICK_IMAGE_REQUEST = 1001;
    private FirestoreUser mUser;
    private RecyclerView mInterestRecyclerView;
    private InterestAdapter mInterestAdapter;
    private ArrayList<String> mInterestArrayList;


    private FirebaseAuth mFirebaseAuth;
    private StorageReference mProfileImageStorage;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseUser mFirebaseUser;

    private ProgressBar mImageProgressBar;
    private ImageView mProfileImageView;

    private boolean isImageChanged = false;
    private Uri changedImageUri;

    TextInputEditText nameInputEditText;
    TextInputEditText emailInputEditText;
    TextInputEditText mobileInputEditText;
    TextInputEditText aboutEditText;
    TextInputEditText interestInputEditText;

    TextInputLayout nameInputLayout;
    TextInputLayout mobileInputLayout;
    TextInputLayout aboutInputLayout;
    TextInputLayout interestInputLayout;

    ImageView mAddInterestImageView;
    TextView mChangeTextView;
    private CollectionReference mUsersCollection;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_profile);

        Intent intent = getIntent();
        mUser = intent.getParcelableExtra(ContentUtils.USER_KEY);

        Toolbar toolbar = findViewById(R.id.edit_profile_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setTitle("Edit profile");
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        mFirebaseAuth = FirebaseAuth.getInstance();
        mProfileImageStorage = FirebaseStorage.getInstance().getReference().child("profile_image");
        mUsersCollection = FirebaseFirestore.getInstance().collection("users");

        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if(mFirebaseUser != null){
            Log.i(TAG, "User not null, name: "+mFirebaseUser.getDisplayName());
        } else {
            Log.i(TAG, "User is null");
        }


        mInterestRecyclerView = findViewById(R.id.interest_recycler_view);
        mInterestArrayList = new ArrayList<>();
        mInterestAdapter = new InterestAdapter(this, mInterestArrayList, ContentUtils.EDIT_INTEREST, this);
        mInterestRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mInterestRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mInterestRecyclerView.setAdapter(mInterestAdapter);

        mProfileImageView = findViewById(R.id.profile_image_view);
        nameInputEditText = findViewById(R.id.name_edit_text);
        emailInputEditText = findViewById(R.id.email_edit_text);
        mobileInputEditText = findViewById(R.id.mobile_edit_text);
        aboutEditText = findViewById(R.id.about_edit_text);
        interestInputEditText = findViewById(R.id.interest_edit_text);
        mAddInterestImageView = findViewById(R.id.add_interest_image_view);
        mChangeTextView = findViewById(R.id.change_image_text_view);
        mImageProgressBar = findViewById(R.id.image_progress);
        mImageProgressBar.setVisibility(View.VISIBLE);

        Button saveDetailsButton = findViewById(R.id.save_button);

        nameInputLayout = findViewById(R.id.name_input_layout);
        mobileInputLayout = findViewById(R.id.mobile_input_layout);
        aboutInputLayout = findViewById(R.id.about_input_layout);
        interestInputLayout = findViewById(R.id.interest_text_input_layout);


        nameInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().trim().equals("")){
                    nameInputLayout.setError(null);
                }
            }
        });

        mobileInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().trim().equals("")){
                    mobileInputLayout.setError(null);
                }
            }
        });

        aboutEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().trim().equals("")){
                    aboutInputLayout.setError(null);
                }
            }
        });

        mAddInterestImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String interest = interestInputEditText.getText().toString();
                if(interest.trim().equals("")){
                    interestInputLayout.setError("Can't be blank!");
                } else {
                    interestInputLayout.setError(null);
                    mInterestArrayList.add(interest);
                    mInterestAdapter.notifyItemInserted(mInterestArrayList.size()-1);
                }
            }
        });

        saveDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mobileNo = mobileInputEditText.getText().toString().trim();
                String name = nameInputEditText.getText().toString().trim();
                String about = aboutEditText.getText().toString().trim();
                if(name.equals("")){
                    nameInputLayout.setError("Can't be blank!");
                }else if(mobileNo.equals("")){
                    mobileInputLayout.setError("Can't be blank!");
                } else if(about.equals("")){
                    aboutInputLayout.setError("Tell us how awesome you are!");
                } else if(mInterestArrayList.isEmpty()){
                    interestInputLayout.setError("Show off some of your skills!");
                } else {
                    View dialogView = getLayoutInflater().inflate(R.layout.progress_dialog_layout, null);
                    TextView progressTextView = dialogView.findViewById(R.id.progress_text_view);
                    progressTextView.setText("Saving details");
                    AlertDialog dialog = new AlertDialog.Builder(EditUserProfileActivity.this)
                            .setView(dialogView)
                            .setCancelable(false)
                            .create();
                    dialog.show();
                    mUser.setName(name);
                    mUser.setMobileNo(mobileNo);
                    mUser.setAbout(about);
                    mUser.setInterestMap(ContentUtils.getMapFromArrayList(mInterestArrayList));
                    saveDetails(mUser, dialog);

                }
            }
        });

        mChangeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mImageProgressBar.setVisibility(View.VISIBLE);

                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setFixAspectRatio(true)
                        .start(EditUserProfileActivity.this);
            }
        });

        displayDetails();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG,"onActivityResult: ");
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                isImageChanged = true;
                changedImageUri = result.getUri();
                mImageProgressBar.setVisibility(View.VISIBLE);

                Picasso.with(EditUserProfileActivity.this)
                        .load(changedImageUri)
                        .into(mProfileImageView, new Callback() {
                            @Override
                            public void onSuccess() {
                                mImageProgressBar.setVisibility(View.INVISIBLE);
                            }

                            @Override
                            public void onError() {
                                mImageProgressBar.setVisibility(View.INVISIBLE);
                            }
                        });


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Log.i(TAG, "onActivityResult: Error"+error.getMessage());

            } else if(resultCode == RESULT_CANCELED){
                mImageProgressBar.setVisibility(View.INVISIBLE);
                Snackbar.make(mProfileImageView,"Cancelled!",Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onRemovedClicked(View view) {
        int position = mInterestRecyclerView.getChildAdapterPosition(view);
        mInterestArrayList.remove(position);
        mInterestAdapter.notifyItemRemoved(position);
    }


    private void displayDetails(){

        mInterestArrayList.addAll(ContentUtils.getArrayListFromMap(mUser.getInterestMap()));
        mInterestAdapter.notifyDataSetChanged();

        Picasso.with(this)
                .load(mUser.getImageUrl())
                .error(R.drawable.user)
                .placeholder(R.drawable.user)
                .into(mProfileImageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        mImageProgressBar.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onError() {
                        mImageProgressBar.setVisibility(View.INVISIBLE);
                    }
                });

        nameInputEditText.setText(mUser.getName());

        emailInputEditText.setText(mUser.getEmailId());
        emailInputEditText.setEnabled(false);
        mobileInputEditText.setText(mUser.getMobileNo());
        aboutEditText.setText(mUser.getAbout());
    }

    private void saveDetails(final FirestoreUser user, final Dialog progressDialog) {

        if(isImageChanged){

            mProfileImageStorage.child(user.getuId())
                    .putFile(changedImageUri)
                    .addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            mImageProgressBar.setVisibility(View.GONE);
                            user.setImageUrl(taskSnapshot.getDownloadUrl().toString());
                            pushUpdatedDataToServer(user, progressDialog);
                        }
                    })
                    .addOnFailureListener(this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            e.printStackTrace();
                            Log.i(TAG,"onActivityResult: failed: "+e.getMessage());
                            progressDialog.dismiss();
                            mImageProgressBar.setVisibility(View.GONE);
                            Snackbar.make(mProfileImageView,"Couldn't change image",Snackbar.LENGTH_SHORT).show();
//                            Toast.makeText(EditUserProfileActivity.this, "Couldn't change image", Toast.LENGTH_SHORT).show();
                        }
                    });


        } else {

            pushUpdatedDataToServer(user, progressDialog);
        }
    }

    private void pushUpdatedDataToServer(final FirestoreUser user, final Dialog progressDialog){


        mUsersCollection.document(user.getuId())
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(TAG, "saveDetails: Success");
                        ContentUtils.saveUserDataInSharedPref(user, EditUserProfileActivity.this);
                        progressDialog.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(EditUserProfileActivity.this)
                                .setTitle("Saved")
                                .setMessage("Details saved successfully!")
                                .setCancelable(false)
                                .setNeutralButton("okay", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                        Intent intent = new Intent();
                                        intent.putExtra(ContentUtils.USER_KEY, user);
                                        setResult(RESULT_OK, intent);
                                        finish();
                                    }
                                });
                        builder.create().show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i(TAG, "saveDetails: Failed: " + e.getMessage());
                        Snackbar.make(mProfileImageView, "Couldn't save data", Snackbar.LENGTH_SHORT).show();
                        progressDialog.dismiss();

                    }
                });


    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Alert")
                .setMessage("Details will not be saved!")
                .setCancelable(false)
                .setPositiveButton("exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        setResult(RESULT_CANCELED);
                        finish();
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        builder.create().show();
    }
}
