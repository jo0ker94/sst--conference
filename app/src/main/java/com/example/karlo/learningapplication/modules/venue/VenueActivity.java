package com.example.karlo.learningapplication.modules.venue;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.karlo.learningapplication.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class VenueActivity extends AppCompatActivity implements VenueView {

    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;
    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private Unbinder mUnbinder;
    private VenuePagerAdapter mVenuePagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venue);
        mUnbinder = ButterKnife.bind(this);
        setUpToolbar();
        setUpTab();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.searchMenu:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setUpToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setUpTab() {
        mVenuePagerAdapter = new VenuePagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mVenuePagerAdapter);
        mViewPager.setOffscreenPageLimit(5);
        mViewPager.setAdapter(mVenuePagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (mVenuePagerAdapter.getFragmentByPosition(position) != null) {
                    setTitle(mVenuePagerAdapter.getFragmentByPosition(position).getTitle());
                    mVenuePagerAdapter.getFragmentByPosition(position).onResume();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.getTabAt(0).setCustomView(R.layout.venue_tab_conference);
        mTabLayout.getTabAt(1).setCustomView(R.layout.venue_tab_region);
        mTabLayout.getTabAt(2).setCustomView(R.layout.venue_tab_food);
        mTabLayout.getTabAt(3).setCustomView(R.layout.venue_tab_sights);
        mTabLayout.getTabAt(4).setCustomView(R.layout.venue_tab_faculty);

        mViewPager.setCurrentItem(1);
        mViewPager.setCurrentItem(0);
        setTitle(R.string.conference);
    }

    public void setTitle(String title) {
        toolbar.setTitle(title);
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
