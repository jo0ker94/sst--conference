package com.example.karlo.sstconference.modules.program.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.karlo.sstconference.R;
import com.example.karlo.sstconference.adapters.TrackAdapter;
import com.example.karlo.sstconference.models.program.Track;
import com.example.karlo.sstconference.modules.program.ProgramActivity;
import com.example.karlo.sstconference.pager.CardFragment;
import com.example.karlo.sstconference.pager.CardFragmentPagerAdapter;
import com.example.karlo.sstconference.pager.ShadowTransformer;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TrackListFragment extends BaseProgramFragment
        implements CardFragment.OnArrowClick {

    @BindView(R.id.program_pager)
    ViewPager mViewPager;

    private List<ProgramCardFragment> mCards;
    private CardFragmentPagerAdapter mPagerAdapter;
    private List<Track> mTracks = new ArrayList<>();
    private List<Track> mFilteredTracks = new ArrayList<>();
    private List<String> mDays;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_track_layout, container, false);
        mUnbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpObservers();
        setUpPager();
    }

    private void setUpObservers() {
        mViewModel.getTracks().observe(this, tracks -> {
            if (tracks != null && !tracks.isEmpty()) {
                showTracks(tracks);
            }
        });

        mViewModel.getStatus().observe(this, status -> {
            switch(status.getResponse()) {
                case LOADING:
                    mListener.loadingData(status.getState());
                    break;
                case ERROR:
                    mListener.showError(new Throwable(status.getMessage()));
                    break;
            }
        });
    }

    private void setUpPager() {
        String[] dates = getResources().getStringArray(R.array.conference_dates);
        mCards = new ArrayList<>();
        for (int i = 0; i < dates.length; i++) {
            mCards.add(ProgramCardFragment.newInstance(dates, i, this));
        }

        mPagerAdapter = new CardFragmentPagerAdapter<>(getChildFragmentManager(), dpToPixels(2), mCards);
        ShadowTransformer fragmentCardShadowTransformer = new ShadowTransformer(mViewPager, mPagerAdapter);
        fragmentCardShadowTransformer.enableScaling(true);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.addOnPageChangeListener(new ViewPagerPageChangeListener());
    }

    public float dpToPixels(int dp) {
        return dp * getResources().getDisplayMetrics().density;
    }

    public void showTracks(List<Track> tracks) {
        mTracks.clear();
        mTracks.addAll(tracks);
        mCards.get(mViewPager.getCurrentItem())
                .showTracks(getContext(), filteredTracks(0), new OnItemClickListener());
    }

    private List<Track> filteredTracks(int position) {
        List<Track> filtered = new ArrayList<>();
        for (Track track : mTracks) {
            if (track.getStartDate().contains(getDate(position))) {
                filtered.add(track);
            }
        }
        mFilteredTracks.clear();
        mFilteredTracks.addAll(filtered);

        return filtered;
    }

    private String getDate(int position) {
        if (mDays == null) {
            mDays = new ArrayList<>();
        }
        if (mDays.isEmpty()) {
            String[] dates = getResources().getStringArray(R.array.conference_dates);
            for (String string : dates) {
                String start = string.split(" ")[0];
                if (!start.isEmpty() && !mDays.contains(start)) {
                    mDays.add(start);
                }
            }
        }
        switch (position) {
            case 0:
                return String.format("%sT", mDays.get(0));
            case 1:
                return String.format("%sT", mDays.get(1));
            case 2:
                return String.format("%sT", mDays.get(2));
        }
        return "";
    }

    @Override
    public void onResume() {
        super.onResume();
        mActivity.setTitle(mActivity.getString(R.string.conference_program));
        if (mViewPager != null) {
            onArrowClick(mViewPager.getCurrentItem());
        }
    }

    @Override
    public void onArrowClick(int position) {
        mViewPager.setCurrentItem(position, true);
        mCards.get(mViewPager.getCurrentItem())
                .showTracks(getContext(), filteredTracks(position), new OnItemClickListener());
    }

    private class OnItemClickListener implements TrackAdapter.OnItemClickListener {

        @Override
        public void onItemClick(View view, int position) {
            mListener.switchFragment(ProgramActivity.FragmentType.TOPIC, mFilteredTracks.get(position));
        }
    }

    private class ViewPagerPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            mCards.get(mViewPager.getCurrentItem())
                    .showTracks(mActivity, filteredTracks(position), new OnItemClickListener());
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}
