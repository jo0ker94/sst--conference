package com.example.karlo.sstconference.listeners;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.View;

@RequiresApi(api = Build.VERSION_CODES.M)
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
        }
    }

    @Override
    public void onScrollChange(View view, int i, int i1, int i2, int i3) {

    }
}
