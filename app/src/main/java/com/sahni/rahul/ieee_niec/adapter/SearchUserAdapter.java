package com.sahni.rahul.ieee_niec.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.sahni.rahul.ieee_niec.R;
import com.sahni.rahul.ieee_niec.glide.GlideApp;
import com.sahni.rahul.ieee_niec.interfaces.OnRecyclerViewItemClickListener;
import com.sahni.rahul.ieee_niec.models.User;

import java.util.ArrayList;

/**
 * Created by sahni on 03-Sep-17.
 */

public class SearchUserAdapter extends RecyclerView.Adapter<SearchUserAdapter.SearchUserViewHolder> {

    private Context mContext;
    private ArrayList<User> mArrayList;
    private OnRecyclerViewItemClickListener mListener;

    public SearchUserAdapter(Context mContext, ArrayList<User> mArrayList, OnRecyclerViewItemClickListener listener) {
        this.mContext = mContext;
        this.mArrayList = mArrayList;
        this.mListener = listener;
    }

    @Override
    public SearchUserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.search_user_item_layout, parent, false);
        return new SearchUserViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(final SearchUserViewHolder holder, int position) {
        User user = mArrayList.get(position);
        holder.textView.setText(user.getName());

        RequestBuilder<Drawable> requestBuilder = Glide.with(mContext)
                .load(user.getImageUrl());

        requestBuilder.listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                holder.progressBar.setVisibility(View.GONE);
                holder.imageView.setImageResource(R.drawable.user);
                return true;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                holder.progressBar.setVisibility(View.GONE);
                return false;
            }
        }).into(holder.imageView);

//        if(user.getImageUrl() != null) {
//            if(!user.getImageUrl().isEmpty()) {
////                Picasso.with(mContext)
////                        .load(user.getImageUrl())
////                        .error(R.drawable.user)
////                        .placeholder(R.drawable.user)
////                        .into(holder.imageView);
//
//                GlideApp.with(mContext)
//                        .load(user.getImageUrl())
//                        .error(R.drawable.user)
//                        .placeholder(R.drawable.user)
//                        .into(holder.imageView);
//            }
//        }
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }

    static class SearchUserViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView textView;
        ProgressBar progressBar;

         SearchUserViewHolder(final View itemView, final OnRecyclerViewItemClickListener listener) {
            super(itemView);
            imageView = itemView.findViewById(R.id.search_user_image_view);
            textView = itemView.findViewById(R.id.search_user_text_view);
            progressBar = itemView.findViewById(R.id.user_image_progress_bar);
             itemView.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {
                     listener.onItemClicked(itemView);
                 }
             });
        }
    }
}
