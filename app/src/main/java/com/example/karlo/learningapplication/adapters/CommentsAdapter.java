package com.example.karlo.learningapplication.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.karlo.learningapplication.R;
import com.example.karlo.learningapplication.models.program.Comment;

import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {

    private List<Comment> mItems;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public CommentsAdapter(List<Comment> items, OnItemClickListener listener) {
        mItems = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Comment comment = this.mItems.get(position);
        holder.title.setText(comment.getText());
        holder.timestamp.setText(comment.getTimestamp());
        holder.title.getRootView().setOnClickListener(view -> mListener.onItemClick(holder.itemView, position));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public Comment getItem(int position) {
        return mItems.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title, timestamp;

        public ViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            timestamp = (TextView) view.findViewById(R.id.timestamp);
        }
    }
}