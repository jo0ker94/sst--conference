package com.example.karlo.sstconference.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.karlo.sstconference.R;
import com.example.karlo.sstconference.models.program.Track;
import com.example.karlo.sstconference.utility.DateUtility;

import java.util.List;

public class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.ViewHolder> {

    private List<Track> mItems;
    private OnItemClickListener mListener;
    private Context mContext;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public TrackAdapter(List<Track> items, OnItemClickListener listener) {
        mItems = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_track, parent, false);
        mContext = view.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Track track = this.mItems.get(position);
        String sTime = DateUtility.getTimeFromIsoDate(track.getStartDate());
        String eTime = DateUtility.getTimeFromIsoDate(track.getEndDate());

        holder.mTime.setText(String.format(mContext.getString(R.string.time_format), sTime, eTime));
        holder.mRoom.setText(getRoom(track.getRoom()));
        holder.mTitle.setText(track.getTitle());

        holder.mRoom.getRootView().setOnClickListener(view -> mListener.onItemClick(holder.itemView, position));
    }

    private String getRoom(int num) {
        switch (num) {
            case 0:
                return mContext.getString(R.string.room_a);
            case 1:
                return mContext.getString(R.string.room_b);
            case 2:
                return mContext.getString(R.string.restaurant);
            default:
                return mContext.getString(R.string.hall_1st);
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public Track getItem(int position) {
        return mItems.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTime, mTitle, mRoom;

        public ViewHolder(View view) {
            super(view);
            mTime = (TextView) view.findViewById(R.id.track_time);
            mTitle = (TextView) view.findViewById(R.id.track_title);
            mRoom = (TextView) view.findViewById(R.id.track_room);
        }
    }
}