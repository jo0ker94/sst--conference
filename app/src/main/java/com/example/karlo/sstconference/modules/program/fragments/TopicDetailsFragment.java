package com.example.karlo.sstconference.modules.program.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.karlo.sstconference.R;
import com.example.karlo.sstconference.adapters.CommentsAdapter;
import com.example.karlo.sstconference.commons.Constants;
import com.example.karlo.sstconference.listeners.OnRecyclerViewScrollListener;
import com.example.karlo.sstconference.listeners.RecyclerViewScrollListener;
import com.example.karlo.sstconference.models.User;
import com.example.karlo.sstconference.models.program.Comment;
import com.example.karlo.sstconference.models.program.Person;
import com.example.karlo.sstconference.models.program.Topic;
import com.example.karlo.sstconference.models.program.Track;
import com.example.karlo.sstconference.modules.login.LoginActivity;
import com.example.karlo.sstconference.receivers.EventAlarmReceiver;
import com.example.karlo.sstconference.utility.AlarmUtility;
import com.example.karlo.sstconference.utility.AppConfig;
import com.example.karlo.sstconference.utility.DateUtility;

import net.globulus.easyprefs.EasyPrefs;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class TopicDetailsFragment extends BaseProgramFragment
        implements CommentsAdapter.OnItemClickListener,
        OnRecyclerViewScrollListener {

    @BindView(R.id.topic_title)
    TextView mTitle;
    @BindView(R.id.topic_lecturers)
    TextView mLecturers;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.et_comment)
    EditText mCommentEditText;
    @BindView(R.id.comment_container)
    CardView mCommentContainer;
    @BindView(R.id.topic_container)
    CardView mTopicContainer;
    @BindView(R.id.no_comments)
    TextView mNoComments;

    private List<User> mUsers = new ArrayList<>();
    private List<Comment> mComments = new ArrayList<>();

    private User mUser;
    private Topic mTopic;
    private Track mTrack;

    private CommentsAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    private Parcelable mListState;
    private int position = 0;

    private CheckBox mSubscribedCheckBox;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_topic_details, container, false);
        mUnbinder = ButterKnife.bind(this, rootView);
        mTopic = getArguments().getParcelable(Constants.DATA);
        mViewModel.fetchTrack(mTopic.getParentId());
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpViews();
        setUpListeners();
        setUpObservers();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.subscribe_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.subscribe);
        mSubscribedCheckBox = (CheckBox) menuItem.getActionView();
        setUpBookmark();
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void setUpListeners() {
        mCommentEditText.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                Comment comment = new Comment(
                        mComments.size(),
                        mCommentEditText.getText().toString(),
                        mUser.getUserId(),
                        mTopic.getId(),
                        DateUtility.getNowInIsoFormat()
                );
                mCompositeDisposable.add(mViewModel.addComment(comment)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                            mCommentEditText.setText("");
                            mComments.add(comment);
                            showComments(this);
                        }));
            }
            return false;
        });
    }

    private void setUpViews() {
        if (mTopic != null && !mTopic.isTrack()) {
            mTitle.setText(mTopic.getTitle());
            if (mTopic.getLecturers() != null) {
                mLecturers.setText(getLecturers(mTopic.getLecturers()));
            } else {
                mLecturers.setVisibility(View.GONE);
            }
            mViewModel.fetchComments(mTopic.getId());

        } else if (mTopic != null && mTopic.isTrack() && mTrack != null) {
            mTitle.setText(mTopic.getTitle());
            mLecturers.setText(getTimeString());
            mViewModel.fetchComments(mTopic.getId());
        }
        mCommentContainer.setVisibility(AppConfig.USER_LOGGED_IN ? View.VISIBLE : View.GONE);
    }

    private boolean userLoggedIn() {
        if (AppConfig.USER_LOGGED_IN) {
            return true;
        } else {
            Snackbar.make(mRecyclerView, R.string.only_for_logged_in, Snackbar.LENGTH_LONG)
                    .setAction(getString(R.string.login).toUpperCase(), view -> {
                        EasyPrefs.putGuestMode(mActivity, false);
                        startActivity(new Intent(mActivity, LoginActivity.class));
                    })
                    .show();
            return false;
        }
    }

    private String getTimeString() {
        String sTime = DateUtility.getTimeFromIsoDate(mTrack.getStartDate());
        String eTime = DateUtility.getTimeFromIsoDate(mTrack.getEndDate());
        return String.format(getString(R.string.time_format), sTime, eTime);
    }

    private String getLecturers(List<Person> people) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < people.size(); i++) {
            stringBuilder.append(people.get(i).getName());
            if (i < people.size() - 2) {
                stringBuilder.append(", ");
            } else if (i < people.size() - 1 || (people.size() == 2 && i != people.size() - 1)) {
                stringBuilder.append(" and ");
            }
        }
        return stringBuilder.toString();
    }

    private void setUpObservers() {
        mViewModel.getUser().observe(this, user -> {
            if (user != null) {
                mUser = user;
                if (mSubscribedCheckBox != null) {
                    setUpBookmark();
                }
            }
        });

        mViewModel.getUsers().observe(this, users -> {
            if (users != null && !users.isEmpty()) {
                mUsers = users;
                if (!mComments.isEmpty()) {
                    showComments(this);
                }
            }
        });

        mViewModel.getComments().observe(this, comments -> {
            if (comments != null && !comments.isEmpty()) {
                mComments.clear();
                mComments.addAll(comments);
                if (!mUsers.isEmpty()) {
                    showComments(this);
                }
            }
        });

        mViewModel.getTracks().observe(this, tracks -> {
            if (tracks != null && tracks.size() == 1) {
                mTrack = tracks.get(0);
                setUpViews();
            }
        });

        mViewModel.getStatus().observe(this, status -> {
            switch(status.getResponse()) {
                case LOADING:
                    mListener.loadingData(status.getState());
                    break;
                case MESSAGE:
                case ERROR:
                    mListener.showError(new Throwable(status.getMessage()));
                    break;
                case NO_DATA:
                    if (status.getState()) {
                        showNoComments();
                    }
                    break;
            }
        });
    }

    private void setUpBookmark() {
        if (mUser != null) {
            mSubscribedCheckBox.setChecked(mUser.getSubscribedEvents().contains(mTopic.getId()));
            mSubscribedCheckBox.setOnCheckedChangeListener((compoundButton, isChecked) -> {
                if (isChecked) {
                    mViewModel.subscribeToTopic(mTopic);
                    Date date = DateUtility.stringToIsoDate(mTrack.getStartDate());
                    AlarmUtility.scheduleAlarm(mActivity, DateUtility.getReminderCalendarFromDate(date), mTopic.getId(), EventAlarmReceiver.class);

                } else {
                    mViewModel.deleteTopicSubscription(mTopic);
                    AlarmUtility.cancelAlarm(mActivity, mTopic.getId(), EventAlarmReceiver.class, null);
                }
            });
        } else {
            mSubscribedCheckBox.setOnClickListener(view -> userLoggedIn());
        }
    }

    public void showComments(CommentsAdapter.OnItemClickListener listener) {
        mRecyclerView.setVisibility(View.VISIBLE);
        mNoComments.setVisibility(View.GONE);
        if (mAdapter == null) {
            mAdapter = new CommentsAdapter(mActivity, mComments, mUsers, listener);
            mLayoutManager = new LinearLayoutManager(getContext());
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.addOnScrollListener(new RecyclerViewScrollListener(this));
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }

    private void showNoComments() {
        mRecyclerView.setVisibility(View.GONE);
        mNoComments.setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(getContext(), mComments.get(position).getUserId(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemLongClick(View view, int position) {
        Comment comment = mComments.get(position);
        if (comment.getUserId().equalsIgnoreCase(mUser.getUserId())) {
            new AlertDialog.Builder(mActivity)
                    .setMessage(R.string.delete_comment_message)
                    .setPositiveButton(R.string.yes,
                            (dialogInterface, i) -> mCompositeDisposable.add(
                                    mViewModel.deleteComment(comment)
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(() -> {
                                                mComments.remove(comment);
                                                mAdapter.notifyDataSetChanged();
                                                if (mComments.isEmpty()) {
                                                    showNoComments();
                                                }
                                            })))
                    .setNegativeButton(R.string.no, null)
                    .show();
        }
    }

    private void scrollMode() {
        if (mCommentContainer.getAlpha() == 1f) {
            mCommentContainer.animate()
                    .translationYBy(200f)
                    .setDuration(200)
                    .alpha(0f)
                    .start();
            mCommentContainer.setVisibility(View.GONE);
            //mTopicContainer.setVisibility(View.GONE);
            if (mListState != null) {
                mLayoutManager.onRestoreInstanceState(mListState);
            }
        }
    }

    private void exitScrollMode() {
        if (mCommentContainer.getAlpha() == 0f) {
            mCommentContainer.setVisibility(View.VISIBLE);
            mCommentContainer.animate()
                    .translationYBy(-200f)
                    .setDuration(200)
                    .alpha(1f)
                    .start();
            //mTopicContainer.setVisibility(View.VISIBLE);
            mRecyclerView.scrollToPosition(position);
        }
    }

    @Override
    public void stoppedScrolling() {
        if (!mRecyclerView.canScrollVertically(-1)) {
            position = 0;
        } else {
            position = mLayoutManager.findLastCompletelyVisibleItemPosition();
        }
        mListState = mLayoutManager.onSaveInstanceState();
        exitScrollMode();
    }

    @Override
    public void scrolling() {
        scrollMode();
    }

    @Override
    public void scrollSettling() {

    }

    @Override
    public void scrolledRight() {

    }

    @Override
    public void scrolledLeft() {

    }

    @Override
    public void noHorizontalScroll() {

    }

    @Override
    public void scrolledDown() {
    }

    @Override
    public void scrolledUp() {
    }

    @Override
    public void noVerticalScroll() {

    }
}
