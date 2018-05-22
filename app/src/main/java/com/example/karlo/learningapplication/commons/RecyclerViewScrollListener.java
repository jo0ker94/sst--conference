package com.example.karlo.learningapplication.commons;

import android.support.v7.widget.RecyclerView;
import android.view.View;

public class RecyclerViewScrollListener extends RecyclerView.OnScrollListener implements View.OnScrollChangeListener {

    private OnRecyclerViewScrollListener mListener;

    public RecyclerViewScrollListener(OnRecyclerViewScrollListener listener) {
        this.mListener = listener;
    }

    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        switch (newState) {
            case RecyclerView.SCROLL_STATE_IDLE:
                mListener.stoppedScrolling();
                break;
            case RecyclerView.SCROLL_STATE_DRAGGING:
                mListener.scrolling();
                break;
            case RecyclerView.SCROLL_STATE_SETTLING:
                mListener.scrollSettling();
                break;
        }
    }

    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        if (dx > 0) {
            mListener.scrolledRight();
        } else if (dx < 0) {
            mListener.scrolledLeft();
        } else {
            mListener.noHorizontalScroll();
        }

        if (dy > 0) {
            mListener.scrolledDown();
        } else if (dy < 0) {
            mListener.scrolledUp();
        } else {
            mListener.noVerticalScroll();
        }
    }

    @Override
    public void onScrollChange(View view, int i, int i1, int i2, int i3) {

    }
}
