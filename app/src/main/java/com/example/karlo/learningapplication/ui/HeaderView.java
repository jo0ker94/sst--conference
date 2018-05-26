package com.example.karlo.learningapplication.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.karlo.learningapplication.R;

public class HeaderView extends RelativeLayout {

    private RelativeLayout mHeaderSection;
    private RelativeLayout mContentView;
    private boolean mExtended = false;
    private boolean mAnimationInProgress = false;

    public HeaderView(Context context) {
        super(context);
        initView(null);
    }

    public HeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(attrs);
    }

    public HeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(attrs);
    }

    public HeaderView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(attrs);
    }

    private void initView(AttributeSet attr) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.header_view_layout, this, true);

        mContentView = (RelativeLayout) findViewById(R.id.content);

        mHeaderSection = findViewById(R.id.header_section);
        TextView titleView = findViewById(R.id.title_text);
        ImageView arrowView = findViewById(R.id.expand_image);

        String titleText = null;
        String expandedTitleText = null;
        if (attr != null) {
            TypedArray a = getContext().getTheme().obtainStyledAttributes(attr, R.styleable.HeaderView, 0, 0);
            try {
                titleText = a.getString(R.styleable.HeaderView_titleText);
                expandedTitleText = a.getString(R.styleable.HeaderView_expandedTitleText);
            } finally {
                a.recycle();
            }
        }

        if (titleText != null) {
            titleView.setText(titleText);
        }

        String finalExpandedTitleText = expandedTitleText;
        String finalTitleText = titleText;
        mHeaderSection.setOnClickListener(view -> {
            if (!mAnimationInProgress) {
                mExtended = !mExtended;
                if (mExtended) {
                    showElements();
                } else {
                    hideElements();
                }
                if (finalExpandedTitleText != null)  {
                    titleView.setText(mExtended ? finalExpandedTitleText : finalTitleText);
                }
                arrowView.animate()
                        .rotationBy(-180)
                        .setDuration(500L)
                        .start();
            }
        });
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if(mContentView == null){
            super.addView(child, index, params);
        } else {
            mContentView.addView(child, index, params);
        }
    }

    private void showElements() {
        if (mContentView.getVisibility() == VISIBLE) {
            return;
        }
        mAnimationInProgress = true;
        mContentView.setAlpha(0.0f);
        mContentView.setVisibility(View.VISIBLE);
        mHeaderSection.bringToFront();
        mContentView.animate()
                .setDuration(500L)
                .alpha(1.0f)
                .translationY(0)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        mContentView.animate().setListener(null);
                        mAnimationInProgress = false;
                    }
                });
    }

    private void hideElements() {
        if (mContentView.getVisibility() == GONE) {
            return;
        }
        mAnimationInProgress = true;
        mHeaderSection.bringToFront();
        mContentView.animate()
                .setDuration(500L)
                .alpha(0.0f)
                .translationY(-mContentView.getHeight())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        mContentView.setVisibility(View.GONE);
                        mContentView.animate().setListener(null);
                        mAnimationInProgress = false;
                    }
                })
        ;
    }
}
