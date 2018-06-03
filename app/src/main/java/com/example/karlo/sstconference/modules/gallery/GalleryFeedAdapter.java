package com.example.karlo.sstconference.modules.gallery;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.karlo.sstconference.R;
import com.example.karlo.sstconference.models.Image;
import com.squareup.picasso.Picasso;

import java.util.List;

public class GalleryFeedAdapter extends  RecyclerView.Adapter<GalleryFeedAdapter.ViewHolder> {

    private List<Image> mItems;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public GalleryFeedAdapter(List<Image> items, OnItemClickListener listener) {
        mItems = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_feed, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Picasso.get()
                .load(mItems.get(position).getImageUrl())
                .fit()
                .centerCrop()
                .placeholder(R.drawable.no_img)
                .into(holder.imageView);

        holder.imageView.setOnClickListener(view -> mListener.onItemClick(holder.imageView, position));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;

        public ViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.imageView);
        }
    }
}
