package com.example.karlo.sstconference.modules.keynotespeakers;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.karlo.sstconference.R;
import com.example.karlo.sstconference.commons.Constants;
import com.example.karlo.sstconference.models.keynote.KeynoteSpeaker;
import com.example.karlo.sstconference.pager.CardAdapter;
import com.example.karlo.sstconference.pager.CardFragment;
import com.example.karlo.sstconference.ui.CustomScrollView;
import com.squareup.picasso.Picasso;

public class KeynoteCardFragment extends CardFragment {

    private RelativeLayout mArrowsContainer;

    public static KeynoteCardFragment newInstance(KeynoteSpeaker speaker, int position, OnArrowClick listener) {
        KeynoteCardFragment fragment = new KeynoteCardFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.POSITION, position);
        args.putParcelable(Constants.DATA, speaker);
        fragment.setArguments(args);
        fragment.setListener(listener);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_keynote, container, false);

        mCardView = (CardView) rootView.findViewById(R.id.cardView);
        mCardView.setMaxCardElevation(mCardView.getCardElevation() * CardAdapter.MAX_ELEVATION_FACTOR);

        CustomScrollView scrollView = (CustomScrollView) rootView.findViewById(R.id.scroll_container);
        TextView name = (TextView) rootView.findViewById(R.id.text_name);
        TextView email = (TextView) rootView.findViewById(R.id.text_email);
        TextView facility = (TextView) rootView.findViewById(R.id.text_facility);
        ImageView image = (ImageView) rootView.findViewById(R.id.image_view);
        TextView title = (TextView) rootView.findViewById(R.id.text_title);
        TextView textAbstract = (TextView) rootView.findViewById(R.id.text_abstract);

        KeynoteSpeaker keynoteSpeaker = (KeynoteSpeaker) getArguments().get(Constants.DATA);

        if (keynoteSpeaker != null) {
            name.setText(keynoteSpeaker.getName());
            email.setText(keynoteSpeaker.getEmail());
            facility.setText(keynoteSpeaker.getFacility());
            title.setText(keynoteSpeaker.getTitle());
            textAbstract.setText(keynoteSpeaker.getAbstractText());

            Picasso.get()
                    .load(keynoteSpeaker.getImageUrl())
                    .fit()
                    .centerCrop()
                    .placeholder(R.drawable.no_img)
                    .into(image);
        }

        mArrowsContainer = (RelativeLayout) rootView.findViewById(R.id.arrows);
        ImageView leftArrow = (ImageView) rootView.findViewById(R.id.left_arrow);
        ImageView rightArrow = (ImageView) rootView.findViewById(R.id.right_arrow);

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

        scrollView.setOnTouchListener(new TouchListener());

        return rootView;
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