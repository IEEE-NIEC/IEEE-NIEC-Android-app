package com.sahni.rahul.ieee_niec.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.request.RequestOptions;
import com.sahni.rahul.ieee_niec.R;
import com.sahni.rahul.ieee_niec.glide.GlideApp;
import com.sahni.rahul.ieee_niec.interfaces.OnRecyclerViewItemClickListener;
import com.sahni.rahul.ieee_niec.models.Resources;

import java.util.ArrayList;

/**
 * Created by sahni on 01-Oct-17.
 */

public class IeeeResourcesAdapter extends RecyclerView.Adapter<IeeeResourcesAdapter.ResourcesViewHolder> {

    private Context mContext;
    private ArrayList<Resources> mArrayList;
    private OnRecyclerViewItemClickListener mListener;

    public IeeeResourcesAdapter(Context mContext, ArrayList<Resources> mArrayList, OnRecyclerViewItemClickListener listener) {
        this.mContext = mContext;
        this.mArrayList = mArrayList;
        this.mListener = listener;
    }

    @Override
    public ResourcesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.ieee_resources_item_layout, parent, false);
        return new ResourcesViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(final ResourcesViewHolder holder, int position) {
        Resources resources = mArrayList.get(position);
        holder.textView.setText(resources.getmTitle());
        holder.imageView.setImageResource(resources.getmImageResId());
        try {
            Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), resources.getmImageResId());
            Palette.from(bitmap)
                    .generate(new Palette.PaletteAsyncListener() {
                        @Override
                        public void onGenerated(@NonNull Palette palette) {
                            Palette.Swatch vibrantSwatch = palette.getVibrantSwatch();
                            if(vibrantSwatch != null){
                                holder.imageView.setBackgroundColor(vibrantSwatch.getRgb());
                            }
                        }
                    });
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }

    static class ResourcesViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView textView;

        public ResourcesViewHolder(final View itemView, final OnRecyclerViewItemClickListener listener) {
            super(itemView);
            imageView = itemView.findViewById(R.id.resources_image_view);
            textView = itemView.findViewById(R.id.resources_text_view);
            itemView.setOnClickListener(new View.OnClickListener() {
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
