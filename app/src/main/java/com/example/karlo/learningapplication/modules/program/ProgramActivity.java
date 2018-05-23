package com.example.karlo.learningapplication.modules.program;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.karlo.learningapplication.R;
import com.example.karlo.learningapplication.commons.Constants;
import com.example.karlo.learningapplication.models.program.Topic;
import com.example.karlo.learningapplication.models.program.Track;
import com.example.karlo.learningapplication.modules.program.fragments.TopicDetailsFragment;
import com.example.karlo.learningapplication.modules.program.fragments.TopicListFragment;
import com.example.karlo.learningapplication.modules.program.fragments.TrackListFragment;

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
        toolbar.setTitle(R.string.conference_program);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
            args.putString(Constants.NAME, track.getTitle());
            args.putInt(Constants.POSITION, track.getId());
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

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_top, R.anim.slide_in_bottom, R.anim.slide_in_top, R.anim.slide_in_bottom)
                    .replace(R.id.content, fragment)
                    .addToBackStack(fragment.getTag())
                    .commit();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
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
