package com.sahni.rahul.ieee_niec.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sahni.rahul.ieee_niec.BuildConfig;
import com.sahni.rahul.ieee_niec.custom.MyRecyclerDivider;
import com.sahni.rahul.ieee_niec.R;
import com.sahni.rahul.ieee_niec.adapter.SocialRecyclerAdapter;
import com.sahni.rahul.ieee_niec.helpers.ContentUtils;
import com.sahni.rahul.ieee_niec.interfaces.OnRecyclerViewItemClickListener;
import com.sahni.rahul.ieee_niec.models.Social;

import java.util.ArrayList;

public class AboutAppActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_app);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        final CollapsingToolbarLayout toolbarLayout = findViewById(R.id.toolbar_layout);
        AppBarLayout appBarLayout = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int totalScrollRange = appBarLayout.getTotalScrollRange();
                if(verticalOffset + totalScrollRange == 0){
                    toolbarLayout.setTitle("About");
                } else {
                    toolbarLayout.setTitle("");
                }
            }
        });

        FloatingActionButton shareFab = findViewById(R.id.share_fab);
        shareFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AboutAppActivity.this, "Share link not implemented yet!", Toast.LENGTH_SHORT).show();
            }
        });

        final ImageView gmailImageView = findViewById(R.id.gmail_image_view);
        ImageView phoneImageView = findViewById(R.id.phone_image_view);

        TextView versiontextView = findViewById(R.id.version_text_view);
        versiontextView.setText("Version "+ BuildConfig.VERSION_NAME);

        gmailImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:" + "rahul.sahni06@gmail.com"));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    Snackbar.make(gmailImageView, "Please install email app to continue", BaseTransientBottomBar.LENGTH_INDEFINITE)
                            .setAction("DISMISS", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                }
                            }).show();
                }
            }
        });

        phoneImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+ "8373932588"));
                if(intent.resolveActivity(getPackageManager()) != null){
                    startActivity(intent);
                }
            }
        });


        final RecyclerView recyclerView = findViewById(R.id.social_recycler_view);
        final ArrayList<Social> arrayList = new ArrayList<>();
        arrayList.add(new Social("Visit our site", R.drawable.ieee_niec_logo_low, ContentUtils.WEBSITE_URL));
        arrayList.add(new Social("Like us on facebook", R.drawable.fb, ContentUtils.FACEBOOK_URL));
        arrayList.add(new Social("Follow us on instagram", R.drawable.ic_instagram, ContentUtils.INSTAGRAM_URL));
        arrayList.add(new Social("Follow us on twitter", R.drawable.tw1, ContentUtils.TWITTER_URL));
        arrayList.add(new Social("Find us on Google+", R.drawable.google, ContentUtils.GOOGLE_PLUS_URL));
        SocialRecyclerAdapter adapter = new SocialRecyclerAdapter(this, arrayList, new OnRecyclerViewItemClickListener(){
            @Override
            public void onItemClicked(View view) {
                int position = recyclerView.getChildAdapterPosition(view);
                handleUrl((arrayList.get(position).getUrl()));
            }
        });

        recyclerView.addItemDecoration(new MyRecyclerDivider(this, DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
    }

    private void handleUrl(String url){
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        PackageManager packageManager = getPackageManager();
        if(intent.resolveActivity(packageManager) != null){
            startActivity(intent);
        } else{
           Toast.makeText(this, "Please install browser to continue", Toast.LENGTH_SHORT).show();
        }
    }
}
