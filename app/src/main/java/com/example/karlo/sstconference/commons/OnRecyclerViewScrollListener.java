package com.example.karlo.sstconference.commons;

public interface OnRecyclerViewScrollListener {
    void stoppedScrolling();
    void scrolling();
    void scrollSettling();

    void scrolledRight();
    void scrolledLeft();
    void noHorizontalScroll();

    void scrolledDown();
    void scrolledUp();
    void noVerticalScroll();
}