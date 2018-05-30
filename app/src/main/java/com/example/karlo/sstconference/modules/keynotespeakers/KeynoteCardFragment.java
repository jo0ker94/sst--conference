package com.example.karlo.sstconference.modules.keynotespeakers;

import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.karlo.sstconference.R;
import com.example.karlo.sstconference.commons.Constants;
import com.example.karlo.sstconference.models.keynote.KeynoteSpeaker;
import com.example.karlo.sstconference.pager.CardAdapter;
import com.example.karlo.sstconference.pager.CardFragment;
import com.squareup.picasso.Picasso;

public class KeynoteCardFragment extends CardFragment {

    private CardView mCardView;

    public static KeynoteCardFragment newInstance(KeynoteSpeaker speaker) {
        KeynoteCardFragment fragment = new KeynoteCardFragment();
        Bundle args = new Bundle();
        args.putParcelable(Constants.DATA, speaker);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_keynote, container, false);

        mCardView = (CardView) rootView.findViewById(R.id.cardView);
        mCardView.setMaxCardElevation(mCardView.getCardElevation() * CardAdapter.MAX_ELEVATION_FACTOR);

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

        return rootView;
    }

    @Override
    public CardView getCardView() {
        return mCardView;
    }
}