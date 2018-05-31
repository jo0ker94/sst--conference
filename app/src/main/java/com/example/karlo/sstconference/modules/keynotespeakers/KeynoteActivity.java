package com.example.karlo.sstconference.modules.keynotespeakers;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.karlo.sstconference.App;
import com.example.karlo.sstconference.R;
import com.example.karlo.sstconference.models.keynote.KeynoteSpeaker;
import com.example.karlo.sstconference.pager.CardFragment;
import com.example.karlo.sstconference.pager.CardFragmentPagerAdapter;
import com.example.karlo.sstconference.pager.ShadowTransformer;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class KeynoteActivity extends AppCompatActivity
        implements KeynoteView,
        CardFragment.OnArrowClick {

    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    @Inject
    KeynoteViewModel mViewModel;

    private Unbinder mUnbinder;

    private CardFragmentPagerAdapter mPagerAdapter;
    private List<KeynoteCardFragment> mCards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keynote);
        ((App) getApplication()).getComponent().inject(this);
        mUnbinder = ButterKnife.bind(this);
        setUpToolbar();
        setUpObservers();
    }

    private void setUpObservers() {
        mViewModel.getSpeakers().observe(this, keynoteSpeakers -> {
            if (keynoteSpeakers != null && !keynoteSpeakers.isEmpty()) {
                showKeynoteSpeakers(keynoteSpeakers);
            }
        });
    }

    @Override
    public void showKeynoteSpeakers(List<KeynoteSpeaker> keynoteSpeakers) {
        mCards = new ArrayList<>();
        int i = 0;
        for (KeynoteSpeaker speaker : keynoteSpeakers) {
            mCards.add(KeynoteCardFragment.newInstance(speaker, i, this));
            i++;
        }
        mPagerAdapter = new CardFragmentPagerAdapter<>(getSupportFragmentManager(), dpToPixels(2), mCards);
        ShadowTransformer fragmentCardShadowTransformer = new ShadowTransformer(mViewPager, mPagerAdapter);
        fragmentCardShadowTransformer.enableScaling(true);

        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.addOnPageChangeListener(new ViewPagerPageChangeListener());
    }

    public float dpToPixels(int dp) {
        return dp * getResources().getDisplayMetrics().density;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setUpToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.keynote_speakers));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

    @Override
    public void loadingData(boolean loading) {
        mProgressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showError(Throwable error) {
        Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onArrowClick(int position) {
        mViewPager.setCurrentItem(position, true);
    }


    private class ViewPagerPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            mCards.get(mViewPager.getCurrentItem()).showArrows();
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}
