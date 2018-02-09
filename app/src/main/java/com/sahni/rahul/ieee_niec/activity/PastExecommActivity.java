package com.sahni.rahul.ieee_niec.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.sahni.rahul.ieee_niec.MyRecyclerDivider;
import com.sahni.rahul.ieee_niec.R;
import com.sahni.rahul.ieee_niec.adapter.ExecommRecyclerAdapter;
import com.sahni.rahul.ieee_niec.helpers.ContentUtils;
import com.sahni.rahul.ieee_niec.interfaces.OnExecommItemClickListener;
import com.sahni.rahul.ieee_niec.models.Execomm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class PastExecommActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, OnExecommItemClickListener {

    public static final String TAG = "PastExecommActivity";
    private DocumentReference pastExecommSessionDocument;
    private CollectionReference pastExecommMembersCollection;

    private RecyclerView execommRecyclerView;
    private ArrayList<Execomm> execommArrayList;
    private ExecommRecyclerAdapter execommRecyclerAdapter;
    private ArrayList<String> sessionList;
    private ArrayAdapter spinnerAdapter;
    private Spinner spinner;

    private TextView hintTextView;
    private ProgressBar progressBar;

    private CardView cardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_execomm);

        Toolbar toolbar = findViewById(R.id.past_toolbar);
        toolbar.setTitle("Past Team");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        progressBar = findViewById(R.id.past_progress_bar);
        hintTextView = findViewById(R.id.past_hint_text_view);
        cardView = findViewById(R.id.past_card_view);

        cardView.setVisibility(View.INVISIBLE);
        hintTextView.setVisibility(View.INVISIBLE);

        execommRecyclerView = findViewById(R.id.past_recycler_view);
        execommArrayList = new ArrayList<>();
        execommRecyclerAdapter = new ExecommRecyclerAdapter(this, execommArrayList, this);
        execommRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        execommRecyclerView.addItemDecoration(new MyRecyclerDivider(this, DividerItemDecoration.VERTICAL));
        execommRecyclerView.setAdapter(execommRecyclerAdapter);

        spinner = findViewById(R.id.past_spinner);
        sessionList = new ArrayList<>();
        spinnerAdapter = new ArrayAdapter<>(this, R.layout.my_spinner_item, sessionList);
        spinnerAdapter.setDropDownViewResource(R.layout.my_spinner_drop_down);
        spinner.setAdapter(spinnerAdapter);

        pastExecommSessionDocument = FirebaseFirestore.getInstance().
                collection(ContentUtils.FIRESTORE_PAST_EXECOMM).
                document(ContentUtils.FIRESTORE_PAST_EXECOMM_SESSION);
        pastExecommMembersCollection = FirebaseFirestore.getInstance().
                collection(ContentUtils.FIRESTORE_PAST_EXECOMM);
        spinner.setOnItemSelectedListener(this);

        getSessionData();
    }

    private void getSessionData(){
        pastExecommSessionDocument.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot documents = task.getResult();
                            if(documents != null){
                                sessionList.clear();
                                sessionList.addAll((ArrayList<String>) documents.
                                        get(ContentUtils.FIRESTORE_PAST_EXECOMM_SESSION_YEAR)
                                );
                                Collections.reverse(sessionList);
                                spinnerAdapter.notifyDataSetChanged();
                                spinner.setSelection(0);
                                Log.d(TAG, "year: "+sessionList.toString());
                            } else {
                                Log.d(TAG, "addOnCompleteListener: Session data fetch Completed: document is null");
                            }
                        } else {
                            Log.d(TAG, "Fail: Session data fetch failed"+task.getException());
                            Log.d(TAG, "addOnCompleteListener: Session data fetch incomplete");
                        }
                    }
                });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String year = sessionList.get(position);
        getExecommMembers(""+year);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void getExecommMembers(String year){
        reset();
        pastExecommMembersCollection.document(year).collection("execomm")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        QuerySnapshot snapshots = task.getResult();
                        if (task.isSuccessful()) {
                            if (!snapshots.isEmpty()) {
                                execommArrayList.addAll(snapshots.toObjects(Execomm.class));
                                Log.d(TAG, "getExecommMembers: "+execommArrayList.get(0).toString());
                                execommRecyclerAdapter.notifyDataSetChanged();
                                cardView.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.INVISIBLE);
                                hintTextView.setVisibility(View.INVISIBLE);
                            } else {
                                Log.d(TAG, "getExecommMembers: empty");
                                showError();
                            }
                        } else {
                            showError();
                            Log.d(TAG,"getExecommMembers: fail: "+task.getException());
                        }
                    }
                });
    }

    @Override
    public void onPhoneClicked(View view) {
        int position = execommRecyclerView.getChildAdapterPosition(view);
        Execomm execomm = execommArrayList.get(position);
        String phoneNo = execomm.getPhoneNo();
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:"+ phoneNo));
        if(intent.resolveActivity(getPackageManager()) != null){
            startActivity(intent);
        }
    }

    @Override
    public void onEmailClicked(View view) {
        int position = execommRecyclerView.getChildAdapterPosition(view);
        Execomm execomm = execommArrayList.get(position);
        String emailId = execomm.getEmailId();
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:" + emailId));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(this, "Please install email app to continue", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemClicked(View view) {

    }

    private void showError(){
        cardView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
        hintTextView.setVisibility(View.VISIBLE);
        hintTextView.setText(R.string.error);
    }

    private void reset(){
        execommArrayList.clear();
        execommRecyclerAdapter.notifyDataSetChanged();
        cardView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        hintTextView.setVisibility(View.INVISIBLE);
    }
}
