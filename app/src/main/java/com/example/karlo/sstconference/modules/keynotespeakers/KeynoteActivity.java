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
import com.example.karlo.sstconference.pager.CardFragmentPagerAdapter;
import com.example.karlo.sstconference.pager.ShadowTransformer;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class KeynoteActivity extends AppCompatActivity implements KeynoteView {

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
        List<KeynoteCardFragment> cards = new ArrayList<>();
        for (KeynoteSpeaker speaker : keynoteSpeakers) {
            cards.add(KeynoteCardFragment.newInstance(speaker));
        }
        mPagerAdapter = new CardFragmentPagerAdapter<>(getSupportFragmentManager(), dpToPixels(2), cards);
        ShadowTransformer fragmentCardShadowTransformer = new ShadowTransformer(mViewPager, mPagerAdapter);
        fragmentCardShadowTransformer.enableScaling(true);

        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setOffscreenPageLimit(3);
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
}
