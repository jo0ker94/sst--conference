package com.example.karlo.sstconference.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.karlo.sstconference.R;
import com.example.karlo.sstconference.models.wiki.Item;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Karlo on 6.4.2018..
 */

public class WikiResultAdapter extends RecyclerView.Adapter<WikiResultAdapter.ViewHolder> {

    private List<Item> mItems;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public WikiResultAdapter(List<Item> items, OnItemClickListener listener) {
        mItems = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_result, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Item item = this.mItems.get(position);
        holder.tvTitle.setText(item.getTitle());
        holder.tvSnippet.setText(item.getSnippet());
        String image = item.getPagemap().getCseImage() != null ? item.getPagemap().getCseImage().get(0).getSrc() : null;
        Picasso.get()
                .load(image)
                .fit()
                .centerCrop()
                .placeholder(R.drawable.no_img)
                .into(holder.imageView);
        holder.tvTitle.getRootView().setOnClickListener(view -> mListener.onItemClick(view, holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public Item getItem(int position) {
        return mItems.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTitle, tvSnippet;
        public ImageView imageView;

        public ViewHolder(View view) {
            super(view);
            tvTitle = (TextView) view.findViewById(R.id.tvTitle);
            tvSnippet = (TextView) view.findViewById(R.id.tvSnippet);
            imageView = (ImageView) view.findViewById(R.id.imageView);
        }
    }
}
