package com.example.karlo.learningapplication.modules.program;

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

import com.example.karlo.learningapplication.R;
import com.example.karlo.learningapplication.adapters.TrackAdapter;
import com.example.karlo.learningapplication.commons.Constants;
import com.example.karlo.learningapplication.models.program.Track;
import com.example.karlo.learningapplication.pager.CardAdapter;
import com.example.karlo.learningapplication.pager.CardFragment;

import java.util.List;

public class ProgramCardFragment extends CardFragment {

    private CardView mCardView;
    private RecyclerView mRecyclerView;

    private OnArrowClick mListener;

    private int mPosition;

    interface OnArrowClick {
        void onArrowClick(int position);
    }

    public void setListener(OnArrowClick mListener) {
        this.mListener = mListener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.viewpager_date, container, false);

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
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //if (getActivity() instanceof ProgramActivity) {
        //    mListener = (OnArrowClick) getActivity();
        //}
    }

    @Override
    public CardView getCardView() {
        return mCardView;
    }

    private class ArrowClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.left_arrow:
                    mListener.onArrowClick(mPosition - 1);
                    break;
                case R.id.right_arrow:
                    mListener.onArrowClick(mPosition + 1);
                    break;
            }
        }
    }
}
