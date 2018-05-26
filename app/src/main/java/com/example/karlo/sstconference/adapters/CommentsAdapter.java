package com.example.karlo.sstconference.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.karlo.sstconference.R;
import com.example.karlo.sstconference.models.User;
import com.example.karlo.sstconference.models.program.Comment;
import com.example.karlo.sstconference.utility.DateUtility;

import java.util.Date;
import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {

    private Activity mActivity;

    private List<Comment> mItems;
    private List<User> mUsers;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    public CommentsAdapter(Activity activity, List<Comment> items, List<User> users, OnItemClickListener listener) {
        mActivity = activity;
        mItems = items;
        mUsers = users;
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
        holder.timestamp.setText(getPostedTime(comment.getTimestamp()));
        holder.user.setText(getUserName(comment.getUserId()));
        holder.title.getRootView().setOnClickListener(view -> mListener.onItemClick(holder.itemView, position));
        holder.title.getRootView().setOnLongClickListener(view -> {
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
        } else if (diff > DateUtility.DAY) {
            int value = (int) (diff / DateUtility.DAY);
            return String.format(mActivity.getResources().getQuantityString(R.plurals.days, value), value);
        } else {
            return timeString;
        }
    }

    private String getUserName(String userId) {
        if (mUsers != null && !mUsers.isEmpty()) {
            for (User user : mUsers) {
                if (user.getUserId().equals(userId)) {
                    return user.getDisplayName();
                }
            }
        }
        return "";
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public Comment getItem(int position) {
        return mItems.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title, timestamp, user;

        public ViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            timestamp = (TextView) view.findViewById(R.id.timestamp);
            user = (TextView) view.findViewById(R.id.user);
        }
    }
}