package com.example.karlo.learningapplication.adapters;

import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.karlo.learningapplication.R;
import com.example.karlo.learningapplication.models.ConferenceChair;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ConferenceChairsAdapter extends RecyclerView.Adapter<ConferenceChairsAdapter.ViewHolder> {

    private List<ConferenceChair> mItems;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
        void openMailDialog(String mail);
    }

    public ConferenceChairsAdapter(List<ConferenceChair> items, OnItemClickListener listener) {
        mItems = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_conference_chairs, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ConferenceChair chair = this.mItems.get(position);
        holder.tvTitle.setText(chair.getChairTitle());
        holder.tvName.setText(chair.getName());
        holder.tvEmail.setText(chair.getEmail());
        holder.tvEmail.setPaintFlags(holder.tvEmail.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        holder.tvEmail.setOnClickListener(view -> mListener.openMailDialog(chair.getEmail()));
        Picasso.get()
                .load(chair.getImageUrl())
                .resize(100, 100)
                .centerCrop()
                .placeholder(R.drawable.no_img)
                .into(holder.imageView);
        holder.tvTitle.getRootView().setOnClickListener(view -> mListener.onItemClick(view, holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public ConferenceChair getItem(int position) {
        return mItems.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTitle, tvName, tvEmail;
        public ImageView imageView;

        public ViewHolder(View view) {
            super(view);
            tvTitle = (TextView) view.findViewById(R.id.tvTitle);
            tvName = (TextView) view.findViewById(R.id.tvName);
            tvEmail = (TextView) view.findViewById(R.id.tvEmail);
            imageView = (ImageView) view.findViewById(R.id.imageView);
        }
    }
}