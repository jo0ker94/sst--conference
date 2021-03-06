package com.example.karlo.sstconference.modules.subscribed;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.karlo.sstconference.App;
import com.example.karlo.sstconference.R;
import com.example.karlo.sstconference.adapters.RecyclerItemTouchHelper;
import com.example.karlo.sstconference.adapters.TopicAdapter;
import com.example.karlo.sstconference.commons.Constants;
import com.example.karlo.sstconference.models.program.Topic;
import com.example.karlo.sstconference.modules.program.ProgramActivity;
import com.example.karlo.sstconference.receivers.EventAlarmReceiver;
import com.example.karlo.sstconference.ui.SearchBarView;
import com.example.karlo.sstconference.utility.AlarmUtility;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SubscriptionActivity extends AppCompatActivity
        implements SubscriptionView,
        SearchBarView.SearchBarListener,
        TopicAdapter.OnItemClickListener,
        SearchBarView.TextChangedListener,
        RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    @BindView(R.id.searchListView)
    RecyclerView mRecyclerView;
    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.search_bar)
    SearchBarView mSearchBar;
    @BindView(R.id.no_result)
    TextView mNoResultText;

    @Inject
    SubscriptionViewModel mViewModel;

    private Unbinder mUnbinder;
    private TopicAdapter mAdapter;

    private List<Topic> mTopics = new ArrayList<>();
    private List<Topic> mFilteredTopics = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription);
        mUnbinder = ButterKnife.bind(this);
        ((App) getApplication()).getComponent().inject(this);
        mSearchBar.setSearchBarListener(this);
        mSearchBar.setOnTextChangedListener(this);
        mSearchBar.setEnabled(false);
        setUpToolbar();
        setUpObservers();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mViewModel.getUserAndFetchEvents();
    }

    private void setUpObservers() {
        mViewModel.getSubscribedTopics().observe(this, topics -> {
            if (topics != null && !topics.isEmpty()) {
                mTopics.clear();
                mFilteredTopics.clear();
                mTopics.addAll(topics);
                mFilteredTopics.addAll(topics);
                showTopics();
            } else {
                showNoSubscription();
            }
        });

        mViewModel.getStatus().observe(this, status -> {
            switch(status.getResponse()) {
                case LOADING:
                    loadingData(status.getState());
                    break;
                case MESSAGE:
                    showError(new Throwable(status.getMessage()));
                    break;
                case DELETED:
                    showError(new Throwable("Unsubscribed!"));
                    break;
                case ERROR:
                    showError(new Throwable(status.getMessage()));
                    if (mFilteredTopics.isEmpty()) {
                        showNoSubscription();
                    }
                    break;
            }
        });
    }

    public void showTopics() {
        mAdapter = new TopicAdapter(mFilteredTopics, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mRecyclerView);

    }

    @Override
    public void onItemClick(View view, int position) {
        showTopicDetails(mFilteredTopics.get(position));
    }

    private void showTopicDetails(Topic topic) {
        Intent intent = new Intent(SubscriptionActivity.this, ProgramActivity.class);
        intent.putExtra(Constants.INTENT_TOPIC_DETAILS, true);
        intent.putExtra(Constants.DATA, topic);
        startActivity(intent);
    }

    @Override
    public void onItemLongClick(View view, int position) {
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
            case R.id.search_icon:
                mSearchBar.showSearchBar(toolbar);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (mSearchBar.getVisibility() == View.VISIBLE) {
            mSearchBar.hideSearchBar(toolbar);
        } else {
            super.onBackPressed();
        }
    }

    private void setUpToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.subscribed_events);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void showNoResult() {
        mNoResultText.setText(R.string.no_result_found);
        mNoResultText.setVisibility(View.VISIBLE);
    }

    @Override
    public void showNoSubscription() {
        mNoResultText.setText(R.string.you_are_not_subscribed_on_any_event);
        mNoResultText.setVisibility(View.VISIBLE);
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
    public void onSearchButtonPressed() {
        filterTopics(mSearchBar.getText());
    }

    private void filterTopics(String text) {
        mFilteredTopics.clear();
        for (Topic topic : mTopics) {
            if (topic.getTitle().toLowerCase(Locale.getDefault()).contains(text.toLowerCase(Locale.getDefault()))) {
                mFilteredTopics.add(topic);
            }
        }
        if (mFilteredTopics.isEmpty()) {
            showNoResult();
        } else {
            mNoResultText.setVisibility(View.GONE);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

    @Override
    public void textChanged(String text) {
        filterTopics(text);
    }

    @Override
    public void afterTextChanged(String text) {
        filterTopics(text);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        final boolean[] revert = {false};
        Topic topic = mFilteredTopics.get(position);
        mFilteredTopics.remove(position);
        mAdapter.notifyItemRemoved(position);

        Snackbar snackbar = Snackbar
                .make(mRecyclerView, R.string.deleted_successfully, Snackbar.LENGTH_LONG)
                .setAction(getString(R.string.undo).toUpperCase(Locale.getDefault()), view -> {
                    revert[0] = true;
                    Snackbar.make(mRecyclerView, R.string.event_restored, Snackbar.LENGTH_SHORT)
                            .show();
                    mFilteredTopics.add(position, topic);
                    mAdapter.notifyItemInserted(position);
                });

        snackbar.show();
        snackbar.addCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                if (!revert[0]) {
                    mViewModel.deleteTopicSubscription(topic);
                    AlarmUtility.cancelAlarm(getApplicationContext(), topic.getId(), EventAlarmReceiver.class, null);
                }
            }

            @Override
            public void onShown(Snackbar snackbar) {
            }
        });
    }
}
