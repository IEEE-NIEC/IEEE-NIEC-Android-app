package com.sahni.rahul.ieee_niec.fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.sahni.rahul.ieee_niec.R;
import com.sahni.rahul.ieee_niec.adapter.InterestAdapter;
import com.sahni.rahul.ieee_niec.helpers.ContentUtils;
import com.sahni.rahul.ieee_niec.interfaces.OnUserDetailsDialogInteractionListener;
import com.sahni.rahul.ieee_niec.models.User;
import com.sahni.rahul.ieee_niec.networking.ApiService;
import com.sahni.rahul.ieee_niec.networking.PostUserDetailsResponse;
import com.sahni.rahul.ieee_niec.networking.RetrofitClient;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sahni on 03-Sep-17.
 */

public class UserDetailsDialogFragment extends DialogFragment {

    private OnUserDetailsDialogInteractionListener mListener;
    private User mUser;
    private RecyclerView mInterestRecyclerView;
    private InterestAdapter mInterestAdapter;
    private ArrayList<String> mInterestArrayList;

    public static UserDetailsDialogFragment newInstance(User user) {

        Bundle args = new Bundle();
        args.putParcelable(ContentUtils.USER_KEY, user);
        UserDetailsDialogFragment fragment = new UserDetailsDialogFragment();
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
                    mListener.onUserDetailsDialogInteraction(null, UserDetailsDialogFragment.this);
                }
            }
        });


        TextInputEditText nameEditText = view.findViewById(R.id.name_edit_text);
        TextInputEditText emailEditText = view.findViewById(R.id.email_edit_text);
        final EditText interestEditText = view.findViewById(R.id.interest_edit_text);
        ImageView addInterestImageView = view.findViewById(R.id.add_interest_image_view);
        final TextInputEditText mobileEditText = view.findViewById(R.id.mobile_edit_text);
        Button saveButton = view.findViewById(R.id.save_button);
        mInterestRecyclerView= view.findViewById(R.id.interest_recycler_view);
        mInterestArrayList = new ArrayList<>();
        mInterestAdapter = new InterestAdapter(getActivity(), mInterestArrayList);
        mInterestRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        mInterestRecyclerView.setAdapter(mInterestAdapter);

        addInterestImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String interest = interestEditText.getText().toString();
                if(interest.trim().equals("")){
                    interestEditText.setError("Please enter valid word");
                } else {
                    interestEditText.setText("");
                    mInterestArrayList.add(interest);
                    mInterestAdapter.notifyDataSetChanged();
                }
            }
        });


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mobileNo = mobileEditText.getText().toString();
                if(mobileNo.trim().equals("")){
                    mobileEditText.setError("Enter valid mobile number");
                } else {
                    mUser.setMobileNumber(mobileNo);
                    mUser.setInterestArrayList(mInterestArrayList);

                    /**
                     * code for sending data to server and getting result
                     */

                    saveDetailsInServer();


                    if(mListener != null){
                        Bundle bundle = new Bundle();
                        bundle.putParcelable(ContentUtils.USER_KEY, mUser);
                        mListener.onUserDetailsDialogInteraction(bundle, UserDetailsDialogFragment.this);
                    }
                }
            }
        });
        nameEditText.setText(mUser.getName());
        nameEditText.setEnabled(false);
        emailEditText.setText(mUser.getEmailId());
        emailEditText.setEnabled(false);

        return view;
    }

    private void saveDetailsInServer() {
        RetrofitClient.getInstance()
                .create(ApiService.class)
                .postUserDetails(mUser)
                .enqueue(new Callback<PostUserDetailsResponse>() {
                    @Override
                    public void onResponse(Call<PostUserDetailsResponse> call, Response<PostUserDetailsResponse> response) {

                    }

                    @Override
                    public void onFailure(Call<PostUserDetailsResponse> call, Throwable t) {

                    }
                });
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
}
