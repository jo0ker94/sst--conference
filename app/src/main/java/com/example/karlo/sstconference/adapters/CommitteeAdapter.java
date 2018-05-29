package com.example.karlo.sstconference.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.karlo.sstconference.R;
import com.example.karlo.sstconference.models.committee.CommitteeMember;

import java.util.List;

public class CommitteeAdapter extends RecyclerView.Adapter<CommitteeAdapter.ViewHolder> {

    private List<CommitteeMember> mItems;
    private CommitteeAdapter.OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    public CommitteeAdapter(List<CommitteeMember> items, CommitteeAdapter.OnItemClickListener listener) {
        mItems = items;
        mListener = listener;
    }

    @Override
    public CommitteeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_committee, parent, false);
        return new CommitteeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CommitteeAdapter.ViewHolder holder, int position) {
        CommitteeMember committee = this.mItems.get(position);
        holder.title.setText(committee.getName());
        holder.facility.setText(committee.getFacility());
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

    public CommitteeMember getItem(int position) {
        return mItems.get(position);
    }

    public class ViewHolder extends SwipeViewHolder {
        public TextView title, facility;

        public ViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.committee_name);
            facility = (TextView) view.findViewById(R.id.committee_facility);
        }
    }
}