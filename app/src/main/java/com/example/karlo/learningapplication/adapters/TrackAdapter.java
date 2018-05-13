package com.example.karlo.learningapplication.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.karlo.learningapplication.R;
import com.example.karlo.learningapplication.models.program.Track;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.ViewHolder> {

    private List<Track> mItems;
    private OnItemClickListener mListener;

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
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Track track = this.mItems.get(position);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {
            Date sDate = format.parse(track.getStartDate());
            Date eDate = format.parse(track.getEndDate());
            holder.time.setText(String.format("(%s:%s - %s:%s)", sDate.getHours(), sDate.getMinutes(), eDate.getHours(), eDate.getMinutes()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.room.setText(getRoom(track.getRoom()));
        holder.title.setText(track.getTitle());

        holder.room.getRootView().setOnClickListener(view -> mListener.onItemClick(holder.itemView, position));
    }

    private String getRoom(int num) {
        switch (num) {
            case 0:
                return "Room A – Lipa";
            case 1:
                return "Room B – Kesten";
            case 2:
                return "Restaurant";
            default:
                return "Hall – 1st fl.";
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
        public TextView time, title, room;

        public ViewHolder(View view) {
            super(view);
            time = (TextView) view.findViewById(R.id.track_time);
            title = (TextView) view.findViewById(R.id.track_title);
            room = (TextView) view.findViewById(R.id.track_room);
        }
    }
}