package com.sahni.rahul.ieee_niec.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sahni.rahul.ieee_niec.R;
import com.sahni.rahul.ieee_niec.models.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by sahni on 03-Sep-17.
 */

public class SearchUserAdapter extends RecyclerView.Adapter<SearchUserAdapter.SearchUserViewHolder> {

    private Context mContext;
    private ArrayList<User> mArrayList;

    public SearchUserAdapter(Context mContext, ArrayList<User> mArrayList) {
        this.mContext = mContext;
        this.mArrayList = mArrayList;
    }

    @Override
    public SearchUserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.search_user_item_layout, parent, false);
        return new SearchUserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchUserViewHolder holder, int position) {
        User user = mArrayList.get(position);
        holder.textView.setText(user.getName());
        Picasso.with(mContext)
                .load(user.getImageUrl())
                .error(R.drawable.user)
                .placeholder(R.drawable.user)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }

    static class SearchUserViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView textView;

         SearchUserViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.search_user_image_view);
            textView = itemView.findViewById(R.id.search_user_text_view);
        }
    }
}
