package com.example.karlo.sstconference.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.SwitchCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.karlo.sstconference.R;

public class SwitchButton extends RelativeLayout {

    private String mTitle;
    private TextView mTitleView;
    private SwitchCompat mSwitchButton;


    public SwitchButton(Context context) {
        super(context);
        initView(null);
    }

    public SwitchButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(attrs);
    }

    public SwitchButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(attrs);
    }

    public SwitchButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(attrs);
    }

    private void initView(AttributeSet attr) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.switch_button_layout, this, true);

        mTitleView = findViewById(R.id.title_view);
        mSwitchButton = findViewById(R.id.button_switch);

        String titleText = null;
        if (attr != null) {
            TypedArray a = getContext().getTheme().obtainStyledAttributes(attr, R.styleable.SwitchButton, 0, 0);
            try {
                titleText = a.getString(R.styleable.SwitchButton_titleText);
            } finally {
                a.recycle();
            }
        }

        if (titleText != null) {
            mTitleView.setText(titleText);
        }

        mSwitchButton.getRootView().setOnClickListener(view -> mSwitchButton.performClick());
    }

    public SwitchCompat getSwitchButton() {
        return mSwitchButton;
    }

    private void setTitle(String title) {
        mTitle = title;
        mTitleView.setText(title);
    }

    public void setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener listener) {
        this.mSwitchButton.setOnCheckedChangeListener(listener);
    }

    public void setChecked(boolean checked) {
        this.mSwitchButton.setChecked(checked);
    }

    public boolean isChecked() {
        return this.mSwitchButton.isChecked();
    }
}
