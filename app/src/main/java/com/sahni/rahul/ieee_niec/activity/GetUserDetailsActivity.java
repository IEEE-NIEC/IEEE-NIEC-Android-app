package com.sahni.rahul.ieee_niec.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.sahni.rahul.ieee_niec.R;
import com.sahni.rahul.ieee_niec.adapter.InterestAdapter;
import com.sahni.rahul.ieee_niec.helpers.ContentUtils;
import com.sahni.rahul.ieee_niec.interfaces.OnRemoveInterestClickListener;
import com.sahni.rahul.ieee_niec.models.User;

import java.util.ArrayList;

public class GetUserDetailsActivity extends AppCompatActivity implements OnRemoveInterestClickListener {

    private User mUser;
    private RecyclerView mInterestRecyclerView;
    private InterestAdapter mInterestAdapter;
    private ArrayList<String> mInterestArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_user_details);

        mUser = getIntent().getParcelableExtra(ContentUtils.USER_KEY);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("");
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });



        final TextInputEditText nameEditText = findViewById(R.id.name_edit_text);
        final TextInputEditText emailEditText = findViewById(R.id.email_edit_text);
        final TextInputEditText interestEditText = findViewById(R.id.interest_edit_text);
        ImageView addInterestImageView = findViewById(R.id.add_interest_image_view);
        final TextInputEditText mobileEditText = findViewById(R.id.mobile_edit_text);
        final TextInputEditText aboutEditText = findViewById(R.id.about_edit_text);
        final TextInputLayout nameInputLayout = findViewById(R.id.name_input_layout);
        final TextInputLayout emailInputLayout = findViewById(R.id.email_input_layout);
        final TextInputLayout mobileInputLayout = findViewById(R.id.mobile_input_layout);
        final TextInputLayout aboutInputLayout = findViewById(R.id.about_input_layout);
        final TextInputLayout interestInputLayout = findViewById(R.id.interest_text_input_layout);

        nameEditText.addTextChangedListener(new TextWatcher() {
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

        emailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().trim().equals("")){
                    emailInputLayout.setError(null);
                }
            }
        });

        mobileEditText.addTextChangedListener(new TextWatcher() {
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


        Button saveButton = findViewById(R.id.save_button);
        mInterestRecyclerView= findViewById(R.id.interest_recycler_view);
        mInterestArrayList = new ArrayList<>();
        mInterestAdapter = new InterestAdapter(this, mInterestArrayList, ContentUtils.EDIT_INTEREST, this);
        mInterestRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mInterestRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mInterestRecyclerView.setAdapter(mInterestAdapter);

        addInterestImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String interest = interestEditText.getText().toString();
                if(interest.trim().equals("")){
                    interestInputLayout.setError("Can't be blank!");
                } else {
                    interestInputLayout.setError(null);
                    interestEditText.setText("");
                    interestInputLayout.setHint("Add more!");
                    mInterestArrayList.add(interest);
                    mInterestAdapter.notifyItemInserted(mInterestArrayList.size());
                    mInterestRecyclerView.smoothScrollToPosition(mInterestArrayList.size()-1);
                }
            }
        });


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameEditText.getText().toString();
                String emailId = emailEditText.getText().toString();
                String mobileNo = mobileEditText.getText().toString();
                String about = aboutEditText.getText().toString();


                if(name.trim().equals("")){
                    nameInputLayout.setError("Can't be blank!");
                } else if(emailId.trim().equals("")){
                    emailInputLayout.setError("Can't be blank!");
                }else if(mobileNo.trim().equals("")){
                    mobileInputLayout.setError("Can't be blank!");
                } else if(about.trim().equals("")){
                    aboutInputLayout.setError("Tell us how awesome you are!");
                }else if(mInterestArrayList.isEmpty()){
                    interestInputLayout.setError("Show off some off your skills!");
                }
                else {
                    mUser.setName(name);
                    mUser.setEmailId(emailId);
                    mUser.setMobileNo(mobileNo);
                    mUser.setInterestMap(ContentUtils.getMapFromArrayList(mInterestArrayList));
                    mUser.setAbout(about);

//                    if(mListener != null){
//                        Bundle bundle = new Bundle();
//                        bundle.putParcelable(ContentUtils.USER_KEY, mUser);
//                        mListener.onUserDetailsDialogInteraction(bundle, GetUserDetailsDialogFragment.this);
//                    }

                    Intent intent = new Intent();
                    intent.putExtra(ContentUtils.USER_KEY, mUser);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
        if(mUser != null) {
            if (!(mUser.getEmailId() == null || mUser.getEmailId().equals(""))) {
                emailEditText.setText(mUser.getEmailId());
                emailEditText.setEnabled(false);
                emailEditText.clearFocus();
            }
            if (!(mUser.getName() == null || mUser.getName().equals(""))) {
                nameEditText.setText(mUser.getName());
            }
        }

    }

    @Override
    public void onRemovedClicked(View view) {
        int position = mInterestRecyclerView.getChildAdapterPosition(view);
        mInterestArrayList.remove(position);
        mInterestAdapter.notifyItemRemoved(position);
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }
}
