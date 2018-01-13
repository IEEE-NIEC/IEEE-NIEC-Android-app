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
import com.sahni.rahul.ieee_niec.interfaces.OnExecommItemClickListener;
import com.sahni.rahul.ieee_niec.models.Execomm;

import java.util.ArrayList;

/**
 * Created by sahni on 03-Jan-18.
 */

public class ExecommRecyclerAdapter extends RecyclerView.Adapter<ExecommRecyclerAdapter.ExecommViewHolder>{

    private Context context;
    private ArrayList<Execomm> arrayList;
    private OnExecommItemClickListener listener;

    public ExecommRecyclerAdapter(Context context, ArrayList<Execomm> arrayList, OnExecommItemClickListener listener) {
        this.context = context;
        this.arrayList = arrayList;
        this.listener = listener;
    }

    @Override
    public ExecommViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.execomm_item_layout, parent, false);
        return new ExecommViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(final ExecommViewHolder holder, int position) {
        Execomm execomm = arrayList.get(position);
        holder.nameTextView.setText(execomm.getName());
        holder.designationTextView.setText(execomm.getDesignation());


        RequestBuilder<Drawable> requestBuilder = Glide.with(context)
                .load(execomm.getPhotoUrl());

        requestBuilder.listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                holder.progressBar.setVisibility(View.GONE);
                holder.userPhotoImageView.setImageResource(R.drawable.user);
                return true;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                holder.progressBar.setVisibility(View.GONE);
                return false;
            }
        }).into(holder.userPhotoImageView);


//        GlideApp.with(context)
//                .load(execomm.getPhotoUrl())
//                .placeholder(R.drawable.user)
//                .optionalCircleCrop()
////                .circleCrop()
//                .error(R.drawable.user)
//                .into(holder.userPhotoImageView);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    static class ExecommViewHolder extends RecyclerView.ViewHolder{

        TextView nameTextView;
        TextView designationTextView;
        ImageView phoneImageView;
        ImageView emailImageView;
        ImageView userPhotoImageView;
        ProgressBar progressBar;

        ExecommViewHolder(final View itemView, final OnExecommItemClickListener listener) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.execomm_name_text_view);
            designationTextView = itemView.findViewById(R.id.execomm_designation_text_view);
            phoneImageView = itemView.findViewById(R.id.execomm_phone_image_view);
            emailImageView = itemView.findViewById(R.id.execomm_email_image_view);
            userPhotoImageView = itemView.findViewById(R.id.execomm_image_view);
            progressBar = itemView.findViewById(R.id.execomm_progress_bar);
            phoneImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null) {
                        listener.onPhoneClicked(itemView);
                    }
                }
            });

            emailImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        listener.onEmailClicked(itemView);
                    }
                }
            });
        }
    }
}
