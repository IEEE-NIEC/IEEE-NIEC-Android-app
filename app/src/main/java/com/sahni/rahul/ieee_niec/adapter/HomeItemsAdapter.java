package com.sahni.rahul.ieee_niec.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sahni.rahul.ieee_niec.interfaces.OnHomeItemClickListener;
import com.sahni.rahul.ieee_niec.models.Items;
import com.sahni.rahul.ieee_niec.R;

import java.util.ArrayList;

/**
 * Created by sahni on 27-Aug-17.
 */

public class HomeItemsAdapter extends RecyclerView.Adapter<HomeItemsAdapter.ItemViewHolder> {

    private Context context;
    private ArrayList<Items> arrayList;
    private OnHomeItemClickListener listener;

    public HomeItemsAdapter(Context context, ArrayList<Items> arrayList, OnHomeItemClickListener listener) {
        this.context = context;
        this.arrayList = arrayList;
        this.listener = listener;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.home_recycler_layout, parent, false);
        return new ItemViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        Items items = arrayList.get(position);
        holder.imageView.setImageDrawable(ContextCompat.getDrawable(context, items.getDrawableId()));
        holder.bgImageView.setImageDrawable(ContextCompat.getDrawable(context, items.getBgDrawableId()));
        holder.textView.setText(items.getTitle());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView textView;
        ImageView bgImageView;


        public ItemViewHolder(View itemView, final OnHomeItemClickListener listener) {
            super(itemView);
            imageView = itemView.findViewById(R.id.item_image_view);
            textView = itemView.findViewById(R.id.item_text_view);
            bgImageView = itemView.findViewById(R.id.item_bg_image_view);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        listener.onHomeItemClicked(view);
                    }
                }
            });
        }
    }
}
