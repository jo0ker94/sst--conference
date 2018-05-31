package com.example.karlo.sstconference.modules.committee;

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
import com.example.karlo.sstconference.adapters.CommitteeAdapter;
import com.example.karlo.sstconference.models.committee.CommitteeMember;
import com.example.karlo.sstconference.pager.CardFragment;
import com.example.karlo.sstconference.pager.CardFragmentPagerAdapter;
import com.example.karlo.sstconference.pager.ShadowTransformer;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CommitteeActivity extends AppCompatActivity
        implements CommitteeView,
        CommitteeAdapter.OnItemClickListener,
        CardFragment.OnArrowClick {

    private static final int ORGANIZING = 0;
    private static final int STEERING = 1;
    private static final int PROGRAM = 2;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    @Inject
    CommitteeViewModel mViewModel;

    private Unbinder mUnbinder;

    private CardFragmentPagerAdapter mPagerAdapter;
    private List<CommitteeCardFragment> mCards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_committee);
        mUnbinder = ButterKnife.bind(this);
        ((App) getApplication()).getComponent().inject(this);
        setUpToolbar();
        setUpObservers();
        setUpPages();
    }

    private void setUpObservers() {
        mViewModel.getStatus().observe(this, status -> {
            switch (status.getResponse()) {
                case ERROR:
                    if (status.getMessage() != null) {
                        showError(new Throwable(status.getMessage()));
                    } else {
                        showError(new Throwable(getString(status.getInteger())));
                    }
                    break;
                case LOADING:
                    loadingData(status.getState());
                    break;
            }
        });
        mViewModel.getOrganizing().observe(this, committeeMembers -> {
            if (committeeMembers != null && !committeeMembers.isEmpty()) {
                showOrganizingCommittee(committeeMembers);
            }
        });
        mViewModel.getProgram().observe(this, committeeMembers -> {
            if (committeeMembers != null && !committeeMembers.isEmpty()) {
                showProgramCommittee(committeeMembers);
            }
        });
        mViewModel.getSteering().observe(this, committeeMembers -> {
            if (committeeMembers != null && !committeeMembers.isEmpty()) {
                showSteeringCommittee(committeeMembers);
            }
        });
    }

    public void setUpPages() {
        mCards = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            mCards.add(CommitteeCardFragment.newInstance(i, this));
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
        toolbar.setTitle(R.string.organizing_committee);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
    }

    @Override
    public void onItemClick(View view, int position) {

    }

    @Override
    public void onItemLongClick(View view, int position) {

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
        mViewPager.setCurrentItem(position);
    }

    private class ViewPagerPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            switch (position) {
                case ORGANIZING:
                    toolbar.setTitle(R.string.organizing_committee);
                    break;
                case STEERING:
                    toolbar.setTitle(R.string.steering_committee);
                    break;
                case PROGRAM:
                    toolbar.setTitle(R.string.program_committee);
                    break;
            }
            mCards.get(mViewPager.getCurrentItem()).showArrows();
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    @Override
    public void showOrganizingCommittee(List<CommitteeMember> committeeMembers) {
        mCards.get(ORGANIZING).showMembers(this, committeeMembers, this);
    }

    @Override
    public void showSteeringCommittee(List<CommitteeMember> committeeMembers) {
        mCards.get(STEERING).showMembers(this, committeeMembers, this);
    }

    @Override
    public void showProgramCommittee(List<CommitteeMember> committeeMembers) {
        mCards.get(PROGRAM).showMembers(this, committeeMembers, this);
    }
}
