package com.sahni.rahul.ieee_niec.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sahni.rahul.ieee_niec.R;

import java.util.ArrayList;

/**
 * Created by sahni on 05-Sep-17.
 */

public class InterestAdapter extends RecyclerView.Adapter<InterestAdapter.InterestViewHolder> {

    private Context mContext;
    ArrayList<String> mArrayList;

    public InterestAdapter(Context mContext, ArrayList<String> mArrayList) {
        this.mContext = mContext;
        this.mArrayList = mArrayList;
    }

    @Override
    public InterestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.interest_item_layout, parent, false);
        return new InterestViewHolder(view);
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

        public InterestViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.interest_item_text_view);
        }
    }
}
