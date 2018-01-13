package com.sahni.rahul.ieee_niec.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sahni.rahul.ieee_niec.R;
import com.sahni.rahul.ieee_niec.helpers.ContentUtils;
import com.sahni.rahul.ieee_niec.interfaces.OnRecyclerViewItemClickListener;

import java.util.ArrayList;

/**
 * Created by sahni on 05-Sep-17.
 */

public class InterestAdapter extends RecyclerView.Adapter<InterestAdapter.InterestViewHolder> {

    private Context mContext;
    private ArrayList<String> mArrayList;
    private int type;
    private OnRecyclerViewItemClickListener listener;

    public InterestAdapter(Context mContext, ArrayList<String> mArrayList, int type, OnRecyclerViewItemClickListener listener) {
        this.mContext = mContext;
        this.mArrayList = mArrayList;
        this.type = type;
        this.listener = listener;
    }

    @Override
    public InterestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.interest_item_layout, parent, false);
        return new InterestViewHolder(view, type, listener);
    }

    @Override
    public void onBindViewHolder(InterestViewHolder holder, int position) {
        String interest = mArrayList.get(position);
        holder.textView.setText(interest);
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }

    static class InterestViewHolder extends RecyclerView.ViewHolder{

        TextView textView;
        ImageView editImageView;

        public InterestViewHolder(final View itemView, int type, final OnRecyclerViewItemClickListener listener) {
            super(itemView);
            textView = itemView.findViewById(R.id.interest_item_text_view);
            editImageView = itemView.findViewById(R.id.remove_interest_image_view);
//            editImageView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if(listener != null){
//                        listener.onRemovedClicked(itemView);
//                    }
//                }
//            });

            if(type == ContentUtils.SHOW_INTEREST){
                editImageView.setVisibility(View.GONE);
            } else {
                editImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(listener != null){
                            listener.onItemClicked(itemView);
                        }
                    }
                });
            }
        }
    }
}
