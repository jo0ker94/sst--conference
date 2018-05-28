package com.example.karlo.sstconference.modules.program;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.karlo.sstconference.R;
import com.example.karlo.sstconference.commons.Constants;
import com.example.karlo.sstconference.models.program.Topic;
import com.example.karlo.sstconference.models.program.Track;
import com.example.karlo.sstconference.modules.program.fragments.TopicDetailsFragment;
import com.example.karlo.sstconference.modules.program.fragments.TopicListFragment;
import com.example.karlo.sstconference.modules.program.fragments.TrackListFragment;
import com.example.karlo.sstconference.modules.search.SearchActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ProgramActivity extends AppCompatActivity
        implements ProgramView,
        ProgramListener {

    public enum FragmentType { TRACK, TOPIC }

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;

    private TrackListFragment mTrackFragment;
    private TopicListFragment mTopicFragment;

    private Unbinder mUnbinder;

    private boolean mTopicDetails = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program);
        mUnbinder = ButterKnife.bind(this);

        mTrackFragment = new TrackListFragment();
        mTopicFragment = new TopicListFragment();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, mTrackFragment)
                .commit();

        setUpToolbar();
        getExtras();
    }

    private void getExtras() {
        if (getIntent().getExtras() != null) {
            mTopicDetails = getIntent().getExtras().getBoolean(Constants.INTENT_TOPIC_DETAILS, false);
            if (mTopicDetails) {
                Topic topic = getIntent().getExtras().getParcelable(Constants.DATA);
                showTopicDetails(topic);
            }
        }
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
                onBackPressed();
                return true;
            case R.id.searchMenu:
                goToSearch();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void goToSearch() {
        Intent intent = new Intent(ProgramActivity.this, SearchActivity.class);
        intent.putExtra(Constants.INTENT_FROM_PROGRAM, true);
        startActivity(intent);
    }

    private void setUpToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.conference_program);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void setTitle(String title) {
        toolbar.setTitle(title);
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
    public void switchFragment(FragmentType fragmentType, Track track) {
        Fragment fragment = (fragmentType == FragmentType.TRACK) ? mTrackFragment : mTopicFragment;
        if (fragmentType == FragmentType.TOPIC) {
            Bundle args = new Bundle();
            args.putParcelable(Constants.DATA, track);
            mTopicFragment.setArguments(args);
        }
        replaceFragment(fragment);
    }

    @Override
    public void showTopicDetails(Topic topic) {
        TopicDetailsFragment topicDetailsFragment = new TopicDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(Constants.DATA, topic);
        topicDetailsFragment.setArguments(args);
        replaceFragment(topicDetailsFragment);
    }

    @Override
    public void showTrackDetails(Track track) {
        TopicDetailsFragment topicDetailsFragment = new TopicDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(Constants.DATA, track);
        topicDetailsFragment.setArguments(args);
        replaceFragment(topicDetailsFragment);
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_top, R.anim.slide_in_bottom, R.anim.slide_in_top, R.anim.slide_in_bottom)
                    .replace(R.id.content, fragment)
                    .addToBackStack(fragment.getTag())
                    .commit();
    }

    @Override
    public void onBackPressed() {
        if (mTopicDetails) {
            finish();
        } else if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
}
