package com.example.karlo.sstconference.modules.committee;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.karlo.sstconference.R;
import com.example.karlo.sstconference.adapters.CommitteeAdapter;
import com.example.karlo.sstconference.commons.Constants;
import com.example.karlo.sstconference.models.committee.CommitteeMember;
import com.example.karlo.sstconference.pager.CardAdapter;
import com.example.karlo.sstconference.pager.CardFragment;
import com.example.karlo.sstconference.ui.CustomRecyclerView;

import java.util.List;

public class CommitteeCardFragment extends CardFragment {

    private RelativeLayout mArrowsContainer;
    private CustomRecyclerView mRecyclerView;

    public static CommitteeCardFragment newInstance(int position, OnArrowClick listener) {
        CommitteeCardFragment fragment = new CommitteeCardFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.POSITION, position);
        fragment.setArguments(args);
        fragment.setListener(listener);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_committee_card, container, false);

        mCardView = (CardView) view.findViewById(R.id.cardView);
        mRecyclerView = (CustomRecyclerView) view.findViewById(R.id.recycler_view);

        ImageView leftArrow = (ImageView) view.findViewById(R.id.left_arrow);
        ImageView rightArrow = (ImageView) view.findViewById(R.id.right_arrow);

        mCardView.setMaxCardElevation(mCardView.getCardElevation() * CardAdapter.MAX_ELEVATION_FACTOR);

        mArrowsContainer = (RelativeLayout) view.findViewById(R.id.arrows);
        ArrowClickListener arrowClickListener = new ArrowClickListener();
        leftArrow.setOnClickListener(arrowClickListener);
        rightArrow.setOnClickListener(arrowClickListener);

        mPosition = getArguments().getInt(Constants.POSITION);
        if (mPosition == 0) {
            leftArrow.setVisibility(View.GONE);
        } else if (mPosition == 2) {
            rightArrow.setVisibility(View.GONE);
        } else {
            leftArrow.setVisibility(View.VISIBLE);
            rightArrow.setVisibility(View.VISIBLE);
        }

        mRecyclerView.setOnTouchListener(new TouchListener());

        return view;
    }

    public void showMembers(Context context, List<CommitteeMember> committeeMembers, CommitteeAdapter.OnItemClickListener listener) {
        CommitteeAdapter adapter = new CommitteeAdapter(committeeMembers, listener);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        showArrows();
    }

    protected void showArrows() {
        mArrowsContainer.setVisibility(View.VISIBLE);
        final Handler handler = new Handler();
        handler.postDelayed(() ->
                        mArrowsContainer.setVisibility(View.GONE),
                2000);
    }

    private class TouchListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            showArrows();
            return view.performClick();
        }
    }
}
