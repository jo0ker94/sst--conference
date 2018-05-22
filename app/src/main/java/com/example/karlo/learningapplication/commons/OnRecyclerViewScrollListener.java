package com.example.karlo.learningapplication.commons;

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