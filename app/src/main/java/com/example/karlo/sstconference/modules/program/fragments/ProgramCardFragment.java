package com.example.karlo.sstconference.modules.program.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.karlo.sstconference.R;
import com.example.karlo.sstconference.adapters.TrackAdapter;
import com.example.karlo.sstconference.commons.Constants;
import com.example.karlo.sstconference.models.program.Track;
import com.example.karlo.sstconference.pager.CardAdapter;
import com.example.karlo.sstconference.pager.CardFragment;

import java.util.List;

public class ProgramCardFragment extends CardFragment {

    private RecyclerView mRecyclerView;


    public static ProgramCardFragment newInstance(String[] dates, int position, OnArrowClick listener) {
        ProgramCardFragment fragment = new ProgramCardFragment();
        Bundle args = new Bundle();
        args.putString(Constants.DATE, dates[position]);
        args.putInt(Constants.POSITION, position);
        args.putInt(Constants.SIZE, dates.length);
        fragment.setArguments(args);
        fragment.setListener(listener);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_program_card, container, false);

        mCardView = (CardView) view.findViewById(R.id.cardView);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        TextView dateText = (TextView) view.findViewById(R.id.date_text);
        ImageView leftArrow = (ImageView) view.findViewById(R.id.left_arrow);
        ImageView rightArrow = (ImageView) view.findViewById(R.id.right_arrow);

        mCardView.setMaxCardElevation(mCardView.getCardElevation() * CardAdapter.MAX_ELEVATION_FACTOR);

        String date = getArguments().getString(Constants.DATE);
        dateText.setText(date);

        ArrowClickListener arrowClickListener = new ArrowClickListener();
        leftArrow.setOnClickListener(arrowClickListener);
        rightArrow.setOnClickListener(arrowClickListener);

        mPosition = getArguments().getInt(Constants.POSITION);
        int size = getArguments().getInt(Constants.SIZE);

        if (mPosition == 0) {
            leftArrow.setVisibility(View.GONE);
        } else if (mPosition == size -1) {
            rightArrow.setVisibility(View.GONE);
        } else {
            leftArrow.setVisibility(View.VISIBLE);
            rightArrow.setVisibility(View.VISIBLE);
        }

        return view;
    }

    public void showTracks(Context context, List<Track> trackList, TrackAdapter.OnItemClickListener listener) {
        TrackAdapter adapter = new TrackAdapter(trackList, listener);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        if (mRecyclerView != null) {
            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.setAdapter(adapter);
        }
    }
}
