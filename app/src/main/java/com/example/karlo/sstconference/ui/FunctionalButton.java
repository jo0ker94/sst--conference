package com.example.karlo.sstconference.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.SwitchCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.karlo.sstconference.R;

public class FunctionalButton extends RelativeLayout {

    private String mTitle;
    private TextView mTitleView;
    private SwitchCompat mSwitch;

    private ButtonStyle mType;

    public enum ButtonStyle { BUTTON, SWITCH }

    public FunctionalButton(Context context) {
        super(context);
        initView(null);
    }

    public FunctionalButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(attrs);
    }

    public FunctionalButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(attrs);
    }

    public FunctionalButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(attrs);
    }

    private void initView(AttributeSet attr) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.functional_button_layout, this, true);

        mTitleView = findViewById(R.id.title_view);
        mSwitch = findViewById(R.id.button_switch);
        ImageView imageView = findViewById(R.id.arrow);

        String titleText = null;
        boolean switched = false;
        if (attr != null) {
            TypedArray a = getContext().getTheme().obtainStyledAttributes(attr, R.styleable.FunctionalButton, 0, 0);
            try {
                titleText = a.getString(R.styleable.FunctionalButton_titleText);
                switched = a.getBoolean(R.styleable.FunctionalButton_swChecked, false);
                mType = ButtonStyle.values()[a.getInt(R.styleable.FunctionalButton_type, 1)];
            } finally {
                a.recycle();
            }
        }

        if (titleText != null) {
            mTitleView.setText(titleText);
        }

        switch (mType) {
            case BUTTON:
                mSwitch.setVisibility(GONE);
                imageView.setVisibility(VISIBLE);
                break;

            case SWITCH:
                mSwitch.setVisibility(VISIBLE);
                imageView.setVisibility(GONE);

                mSwitch.setChecked(switched);
                mSwitch.getRootView().setOnClickListener(view -> mSwitch.performClick());
                break;
        }

    }

    public SwitchCompat getSwitchButton() {
        return mSwitch;
    }

    private void setTitle(String title) {
        mTitle = title;
        mTitleView.setText(title);
    }

    public void setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener listener) {
        this.mSwitch.setOnCheckedChangeListener(listener);
    }

    public void setChecked(boolean checked) {
        this.mSwitch.setChecked(checked);
    }

    public boolean isChecked() {
        return this.mSwitch.isChecked();
    }
}
