package com.example.karlo.learningapplication.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.karlo.learningapplication.R;

public class SwipeViewHolder extends RecyclerView.ViewHolder {

    public RelativeLayout viewForeground, viewBackground;

    public SwipeViewHolder(View view) {
        super(view);
        viewForeground = (RelativeLayout) view.findViewById(R.id.view_foreground);
        viewBackground = (RelativeLayout) view.findViewById(R.id.view_background);
    }
}
