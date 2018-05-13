package com.example.karlo.learningapplication.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.karlo.learningapplication.R;
import com.example.karlo.learningapplication.models.program.Person;
import com.example.karlo.learningapplication.models.program.Topic;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.ViewHolder> {

    private List<Topic> mItems;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public TopicAdapter(List<Topic> items, OnItemClickListener listener) {
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
        Topic topic = this.mItems.get(position);
        holder.room.setText(getLecturers(topic.getLecturers()));
        holder.room.setTextSize(14f);
        holder.title.setText(topic.getTitle());
        holder.time.setVisibility(View.GONE);

        holder.room.getRootView().setOnClickListener(view -> mListener.onItemClick(holder.itemView, position));
    }

    private String getLecturers(List<Person> people) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < people.size(); i++) {
            stringBuilder.append(people.get(i).getName());
            if (i < people.size() - 2) {
                stringBuilder.append(", ");
            } else if (i < people.size() - 1 || (people.size() == 2 && i != people.size() - 1)) {
                stringBuilder.append(" and ");
            }
        }
        return stringBuilder.toString();
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public Topic getItem(int position) {
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