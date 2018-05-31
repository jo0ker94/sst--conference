package com.example.karlo.sstconference.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.karlo.sstconference.R;
import com.example.karlo.sstconference.models.program.Comment;
import com.example.karlo.sstconference.utility.DateUtility;

import java.util.Date;
import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {

    private Activity mActivity;

    private List<Comment> mItems;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    public CommentsAdapter(Activity activity, List<Comment> items, OnItemClickListener listener) {
        mActivity = activity;
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
        holder.mTitle.setText(comment.getText());
        holder.mTimestamp.setText(getPostedTime(comment.getTimestamp()));
        holder.mUser.setText(comment.getAuthor());
        holder.mTitle.getRootView().setOnClickListener(view -> mListener.onItemClick(holder.itemView, position));
        holder.mTitle.getRootView().setOnLongClickListener(view -> {
            mListener.onItemLongClick(holder.itemView, position);
            return true;
        });
    }

    private String getPostedTime(String timeString) {
        Date timestamp = DateUtility.stringToIsoDate(timeString);
        long diff = DateUtility.getDateDifferenceInSeconds(timestamp, DateUtility.getNowInGMT());
        if (diff < DateUtility.MINUTE) {
            return mActivity.getString(R.string.just_posted);
        } else if (diff > DateUtility.MINUTE && diff < DateUtility.HOUR) {
            int value = (int) (diff / DateUtility.MINUTE);
            return String.format(mActivity.getResources().getQuantityString(R.plurals.minutes, value), value);
        } else if (diff > DateUtility.HOUR && diff < DateUtility.DAY) {
            int value = (int) (diff / DateUtility.HOUR);
            return String.format(mActivity.getResources().getQuantityString(R.plurals.hours, value), value);
        } else if (diff > DateUtility.DAY && diff < DateUtility.MONTH) {
            int value = (int) (diff / DateUtility.DAY);
            return String.format(mActivity.getResources().getQuantityString(R.plurals.days, value), value);
        } else if (diff > DateUtility.MONTH) {
            int value = (int) (diff / DateUtility.MONTH);
            return String.format(mActivity.getResources().getQuantityString(R.plurals.months, value), value);
        } else {
            return timeString;
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public Comment getItem(int position) {
        return mItems.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTitle, mTimestamp, mUser;

        public ViewHolder(View view) {
            super(view);
            mTitle = (TextView) view.findViewById(R.id.title);
            mTimestamp = (TextView) view.findViewById(R.id.timestamp);
            mUser = (TextView) view.findViewById(R.id.user);
        }
    }
}