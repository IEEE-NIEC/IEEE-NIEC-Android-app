package com.sahni.rahul.ieee_niec.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sahni.rahul.ieee_niec.R;
import com.sahni.rahul.ieee_niec.adapter.InterestAdapter;
import com.sahni.rahul.ieee_niec.helpers.ContentUtils;
import com.sahni.rahul.ieee_niec.interfaces.OnRemoveInterestClickListener;
import com.sahni.rahul.ieee_niec.models.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class EditUserProfileActivity extends AppCompatActivity implements OnRemoveInterestClickListener {

    private static final String TAG = "EditUserProfileActivity";
    private static final int PICK_IMAGE_REQUEST = 1001;
    private User mUser;
    private RecyclerView mInterestRecyclerView;
    private InterestAdapter mInterestAdapter;
    private ArrayList<String> mInterestArrayList;
    private ProgressBar mImageProgressBar;

    private FirebaseAuth mFirebaseAuth;
    private StorageReference mProfileImageStorage;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseUser mFirebaseUser;
    private ImageView mProfileImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_profile);
//
//        if(savedInstanceState == null){
//            Intent intent = getIntent();
//            mUser = intent.getParcelableExtra(ContentUtils.USER_KEY);
//        } else {
//            mUser = savedInstanceState.getParcelable(ContentUtils.USER_KEY);
//        }

        Intent intent = getIntent();
        mUser = intent.getParcelableExtra(ContentUtils.USER_KEY);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mProfileImageStorage = FirebaseStorage.getInstance().getReference().child("profile_image");

        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if(mFirebaseUser != null){
            Log.i(TAG, "User not null, name: "+mFirebaseUser.getDisplayName());
        } else {
            Log.i(TAG, "User is null");
        }

//        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
//                if(firebaseAuth != null){
//                    mFirebaseUser = firebaseUser;
//                } else {
//
//                }
//            }
//        };

        mProfileImageView = findViewById(R.id.profile_image_view);
        EditText nameEditText = findViewById(R.id.name_edit_text);
        EditText emailEditText = findViewById(R.id.email_edit_text);
        final EditText mobileEditText = findViewById(R.id.mobile_edit_text);
        final EditText interestEditText = findViewById(R.id.interest_edit_text);
        ImageView addInterestImageView = findViewById(R.id.add_interest_image_view);
        TextView changeTextView = findViewById(R.id.change_image_text_view);
        mImageProgressBar = findViewById(R.id.image_progress);

        mInterestRecyclerView = findViewById(R.id.interest_recycler_view);
        mInterestArrayList = new ArrayList<>();
        mInterestArrayList.addAll(mUser.getInterestArrayList());
        mInterestAdapter = new InterestAdapter(this, mInterestArrayList, ContentUtils.EDIT_INTEREST, this);
        mInterestRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mInterestRecyclerView.setAdapter(mInterestAdapter);

        Picasso.with(this)
                .load(mUser.getImageUrl())
                .error(R.drawable.user)
                .placeholder(R.drawable.user)
                .into(mProfileImageView);

        nameEditText.setText(mUser.getName());
        nameEditText.setEnabled(false);
        emailEditText.setText(mUser.getEmailId());
        emailEditText.setEnabled(false);
        mobileEditText.setText(mUser.getMobileNumber());
        Button addButton = findViewById(R.id.add_details_button);

        addInterestImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String interest = interestEditText.getText().toString();
                if(interest.trim().equals("")){
                    interestEditText.setError("Enter valid interest");
                } else {
                    mInterestArrayList.add(interest);
                    mInterestAdapter.notifyDataSetChanged();
                }
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mobileEditText.getText().toString().trim().equals("")){
                    mobileEditText.setError("Enter valid mobile number");
                } else if(mInterestArrayList.isEmpty()){
                    interestEditText.setError("Enter interest");
                } else {
                    View dialogView = getLayoutInflater().inflate(R.layout.progress_dialog_layout, null);
                    TextView progressTextView = dialogView.findViewById(R.id.progress_text_view);
                    progressTextView.setText("Saving details");
                    AlertDialog dialog = new AlertDialog.Builder(EditUserProfileActivity.this)
                            .setView(dialogView)
//                            .setCancelable(false)
                            .create();
                    dialog.show();

                }
            }
        });

        changeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mImageProgressBar.setVisibility(View.VISIBLE);
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG,"onActivityResult: ");
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK){
            Uri imageUri = data.getData();
            Log.i(TAG,"onActivityResult: "+ imageUri);

            mProfileImageStorage.child(mUser.getUserId())
                    .putFile(imageUri)
                    .addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            mImageProgressBar.setVisibility(View.GONE);
                            mUser.setImageUrl(taskSnapshot.getDownloadUrl().toString());
                            Picasso.with(EditUserProfileActivity.this)
                                    .load(mUser.getImageUrl())
                                    .into(mProfileImageView);
                        }
                    })
                    .addOnFailureListener(this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mImageProgressBar.setVisibility(View.GONE);
                            Toast.makeText(EditUserProfileActivity.this, "Couldn't change image", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    @Override
    public void onRemovedClicked(View view) {
        int position = mInterestRecyclerView.getChildAdapterPosition(view);
        mInterestArrayList.remove(position);
        mInterestAdapter.notifyDataSetChanged();
    }

//    @Override
//    public void onBackPressed() {
//        setResult(RESULT_CANCELED);
//        finish();
//    }

//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        Log.i(TAG, "onSaveInstanceState");
//        outState.putParcelable(ContentUtils.USER_KEY, mUser);
//        super.onSaveInstanceState(outState);
//    }
}
