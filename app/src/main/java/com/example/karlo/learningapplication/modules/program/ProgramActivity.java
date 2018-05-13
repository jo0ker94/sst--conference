package com.example.karlo.learningapplication.modules.program;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.karlo.learningapplication.App;
import com.example.karlo.learningapplication.R;
import com.example.karlo.learningapplication.commons.Constants;
import com.example.karlo.learningapplication.models.program.Track;
import com.example.karlo.learningapplication.pager.CardFragmentPagerAdapter;
import com.example.karlo.learningapplication.pager.ShadowTransformer;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProgramActivity extends AppCompatActivity
        implements ProgramView,
        ProgramCardFragment.OnArrowClick {

    private static final String FIRST_DAY = "18T";
    private static final String SECOND_DAY = "19T";
    private static final String THIRD_DAY = "20T";

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.program_pager)
    ViewPager mViewPager;
    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;

    @Inject
    ProgramViewModel mViewModel;

    private List<ProgramCardFragment> mCards;
    private CardFragmentPagerAdapter mPagerAdapter;
    private List<Track> mTracks = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program);
        ButterKnife.bind(this);
        ((App) getApplication()).getComponent().inject(this);
        setUpToolbar();
        setUpPager();
        setUpObservers();
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
                    loadingData(status.getState());
                    break;
                case ERROR:
                    showError(new Throwable(status.getMessage()));
                    break;
            }
        });
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setUpToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.conference_program);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setUpPager() {
        String[] dates = getResources().getStringArray(R.array.conference_dates);
        mCards = new ArrayList<>();
        for (int i = 0; i < dates.length; i++) {
            ProgramCardFragment cardFragment = new ProgramCardFragment();
            Bundle args = new Bundle();
            args.putString(Constants.DATE, dates[i]);
            args.putInt(Constants.POSITION, i);
            args.putInt(Constants.SIZE, dates.length);
            cardFragment.setArguments(args);
            mCards.add(cardFragment);
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
    public void showTracks(List<Track> tracks) {
        mTracks.addAll(tracks);
        mCards.get(mViewPager.getCurrentItem())
                .showTracks(this, filteredTracks(0));
    }

    private List<Track> filteredTracks(int position) {
        List<Track> filtered = new ArrayList<>();
        String date = FIRST_DAY;
        switch (position) {
            case 0:
                date = FIRST_DAY;
                break;
            case 1:
                date = SECOND_DAY;
                break;
            case 2:
                date = THIRD_DAY;
                break;

        }
        for (Track track : mTracks) {
            if (track.getStartDate().contains(date)) {
                filtered.add(track);
            }
        }
        return filtered;
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
        mCards.get(mViewPager.getCurrentItem())
                .showTracks(this, filteredTracks(position));
    }

    private class ViewPagerPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            mCards.get(mViewPager.getCurrentItem())
                    .showTracks(getApplicationContext(), filteredTracks(position));
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    /*@Override
    public void onItemClick(View view, int position) {
        //showChairDetails(mAdapter.getItem(position));
        presenter.fetchTopics(position);
    }

    @Override
    public void openMailDialog(String mail) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "SST 2017");
        intent.setData(Uri.parse("mailto:" + mail));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void openDialDialog(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        startActivity(intent);
    }

    private void showChairDetails(ConferenceChair chair) {
        View view = getLayoutInflater().inflate(R.layout.conference_chair_details, null);
        ImageView chairImage = view.findViewById(R.id.chairImageView);
        TextView chairTitle = view.findViewById(R.id.tvTitle);
        TextView chairName = view.findViewById(R.id.tvName);
        TextView chairEmail = view.findViewById(R.id.tvEmail);
        TextView chairPhone = view.findViewById(R.id.tvPhone);
        TextView chairFacility = view.findViewById(R.id.tvFacility);
        Picasso.get()
                .load(chair.getImageUrl())
                .fit()
                .centerInside()
                .placeholder(R.drawable.no_img)
                .into(chairImage);
        chairTitle.setText(chair.getChairTitle());
        chairName.setText(chair.getName());
        chairEmail.setText(chair.getEmail());
        chairPhone.setText(chair.getPhoneNumber());
        chairFacility.setText(chair.getFacility());

        chairEmail.setPaintFlags(chairEmail.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        chairPhone.setPaintFlags(chairPhone.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        chairEmail.setOnClickListener(emailView -> openMailDialog(chair.getEmail()));
        chairPhone.setOnClickListener(phoneView -> openDialDialog(chair.getPhoneNumber()));

        new AlertDialog.Builder(this)
                .setView(view)
                .show();
    }


    @Override
    public void showData(List<ConferenceChair> chairs) {
        //mAdapter = new ConferenceChairsAdapter(chairs, this);
        //LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        //mRecyclerView.setLayoutManager(layoutManager);
        //mRecyclerView.setAdapter(mAdapter);
    }


    @Override
    public void showTopics(List<Topic> topics) {
        //TopicAdapter adapter = new TopicAdapter(topics, null);
        //LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        //mRecyclerView.setLayoutManager(layoutManager);
        //mRecyclerView.setAdapter(adapter);
    }*/
}
