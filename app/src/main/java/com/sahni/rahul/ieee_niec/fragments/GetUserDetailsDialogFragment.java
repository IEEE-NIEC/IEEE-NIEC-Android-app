package com.sahni.rahul.ieee_niec.fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.sahni.rahul.ieee_niec.R;
import com.sahni.rahul.ieee_niec.adapter.InterestAdapter;
import com.sahni.rahul.ieee_niec.helpers.ContentUtils;
import com.sahni.rahul.ieee_niec.interfaces.OnRemoveInterestClickListener;
import com.sahni.rahul.ieee_niec.interfaces.OnUserDetailsDialogInteractionListener;
import com.sahni.rahul.ieee_niec.models.FirestoreUser;

import java.util.ArrayList;

/**
 * Created by sahni on 03-Sep-17.
 */

public class GetUserDetailsDialogFragment extends DialogFragment implements OnRemoveInterestClickListener {

    private OnUserDetailsDialogInteractionListener mListener;
    private FirestoreUser mUser;
    private RecyclerView mInterestRecyclerView;
    private InterestAdapter mInterestAdapter;
    private ArrayList<String> mInterestArrayList;

    private static final String TAG = "GetUserDetailsDialog";

    public static GetUserDetailsDialogFragment newInstance(FirestoreUser user) {

        Bundle args = new Bundle();
        args.putParcelable(ContentUtils.USER_KEY, user);
        GetUserDetailsDialogFragment fragment = new GetUserDetailsDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUser = getArguments().getParcelable(ContentUtils.USER_KEY);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_details_dialog_layout, container, false);
        Toolbar toolbar = view.findViewById(R.id.dialog_fragment_toolbar);
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_close_clear_cancel);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mListener != null){
                    mListener.onUserDetailsDialogInteraction(null, GetUserDetailsDialogFragment.this);
                }
            }
        });


        final TextInputEditText nameEditText = view.findViewById(R.id.name_edit_text);
        final TextInputEditText emailEditText = view.findViewById(R.id.email_edit_text);
        final TextInputEditText interestEditText = view.findViewById(R.id.interest_edit_text);
        ImageView addInterestImageView = view.findViewById(R.id.add_interest_image_view);
        final TextInputEditText mobileEditText = view.findViewById(R.id.mobile_edit_text);
        final TextInputEditText aboutEditText = view.findViewById(R.id.about_edit_text);
//        final TextInputEditText professionPlaceEditText = view.findViewById(R.id.profession_place_edit_text);

        final TextInputLayout nameInputLayout = view.findViewById(R.id.name_input_layout);
        final TextInputLayout emailInputLayout = view.findViewById(R.id.email_input_layout);
        final TextInputLayout mobileInputLayout = view.findViewById(R.id.mobile_input_layout);
        final TextInputLayout aboutInputLayout = view.findViewById(R.id.about_input_layout);
//        final TextInputLayout placeInputLayout = view.findViewById(R.id.profession_place_input_layout);
        final TextInputLayout interestInputLayout = view.findViewById(R.id.interest_text_input_layout);

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


        Button saveButton = view.findViewById(R.id.save_button);
        mInterestRecyclerView= view.findViewById(R.id.interest_recycler_view);
        mInterestArrayList = new ArrayList<>();
        mInterestAdapter = new InterestAdapter(getActivity(), mInterestArrayList, ContentUtils.EDIT_INTEREST, this);
        mInterestRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
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

                    if(mListener != null){
                        Bundle bundle = new Bundle();
                        bundle.putParcelable(ContentUtils.USER_KEY, mUser);
                        mListener.onUserDetailsDialogInteraction(bundle, GetUserDetailsDialogFragment.this);
                    }
                }
            }
        });
        if(mUser != null) {
            if (!(mUser.getEmailId() == null || mUser.getEmailId().equals(""))) {
                emailEditText.setText(mUser.getEmailId());
                emailEditText.setEnabled(false);
                emailEditText.clearFocus();
            }
        }

        return view;
    }



    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // The only reason you might override this method when using onCreateView() is
        // to modify any dialog characteristics. For example, the dialog includes a
        // title by default, but your custom layout might not need it. So here you can
        // remove the dialog title, but you must call the superclass to get the Dialog.
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OnUserDetailsDialogInteractionListener){
            mListener = (OnUserDetailsDialogInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()+" must implement" +
                    "OnUserDetailsDialogInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onRemovedClicked(View view) {
        int position = mInterestRecyclerView.getChildAdapterPosition(view);
        mInterestArrayList.remove(position);
        mInterestAdapter.notifyItemRemoved(position);
    }
}
