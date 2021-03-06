package com.example.karlo.sstconference.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.karlo.sstconference.R;
import com.example.karlo.sstconference.models.program.Topic;

import java.util.List;

public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.ViewHolder> {

    private List<Topic> mItems;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    public TopicAdapter(List<Topic> items, OnItemClickListener listener) {
        mItems = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_topic, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Topic topic = this.mItems.get(position);
        holder.title.setText(topic.getTitle());
        holder.title.getRootView().setOnClickListener(view -> mListener.onItemClick(holder.itemView, position));
        holder.title.getRootView().setOnLongClickListener(view -> {
            mListener.onItemLongClick(holder.itemView, position);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public Topic getItem(int position) {
        return mItems.get(position);
    }

    public class ViewHolder extends SwipeViewHolder {
        public TextView title;

        public ViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.topic_title);
        }
    }
}