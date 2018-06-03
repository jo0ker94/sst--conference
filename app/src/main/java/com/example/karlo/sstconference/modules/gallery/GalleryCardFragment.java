package com.example.karlo.sstconference.modules.gallery;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.karlo.sstconference.R;
import com.example.karlo.sstconference.commons.Constants;
import com.example.karlo.sstconference.models.Image;
import com.example.karlo.sstconference.pager.CardAdapter;
import com.example.karlo.sstconference.pager.CardFragment;
import com.squareup.picasso.Picasso;


public class GalleryCardFragment extends CardFragment {

    private CardView mCardView;
    private ImageView mImageCard;
    private ImageView mShareButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.image_viewpager, container, false);

        mCardView = (CardView) view.findViewById(R.id.cardView);
        mImageCard = (ImageView) view.findViewById(R.id.imageCard);
        mShareButton = (ImageView) view.findViewById(R.id.shareButton);

        Image image = (Image) getArguments().getParcelable(Constants.DATA);

        Uri uri = Uri.parse(image.getImageUrl());

        mCardView.setMaxCardElevation(mCardView.getCardElevation() * CardAdapter.MAX_ELEVATION_FACTOR);

        Picasso.get()
                .load(uri)
                .fit()
                .centerCrop()
                .placeholder(R.drawable.no_img)
                .into(mImageCard);

        mImageCard.setOnClickListener(v -> {
            if (mShareButton.getAlpha() == 0f) {
                mShareButton.animate()
                        .alpha(1f)
                        .setDuration(200)
                        .start();
            } else {
                mShareButton.animate()
                        .alpha(0f)
                        .setDuration(200)
                        .start();
            }
        });

        mShareButton.setOnClickListener(v -> {
            if (mShareButton.getAlpha() > 0f) {
                share(uri);
            }
        });

        return view;
    }

    private void share(Uri res) {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_TEXT, res.toString());
        getActivity().startActivity(Intent.createChooser(share, getString(R.string.share_image)));
    }

    @Override
    public CardView getCardView() {
        return mCardView;
    }
}