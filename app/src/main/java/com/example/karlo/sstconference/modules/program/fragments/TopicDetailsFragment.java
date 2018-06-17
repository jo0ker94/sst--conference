package com.example.karlo.sstconference.modules.program.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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
import android.view.inputmethod.InputMethodManager;
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
import com.example.karlo.sstconference.utility.DateUtility;

import net.globulus.easyprefs.EasyPrefs;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
    @BindView(R.id.fab)
    FloatingActionButton mFab;
    @BindView(R.id.no_comments)
    TextView mNoComments;

    private List<Comment> mComments = new ArrayList<>();

    private User mUser;
    private Topic mTopic;
    private Track mTrack;

    private CommentsAdapter mAdapter;

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
                exitCommentMode();
                Comment comment = new Comment(
                        mComments.size(),
                        mCommentEditText.getText().toString(),
                        mUser.getUserId(),
                        mTopic.getId(),
                        mUser.getDisplayName(),
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
            if (getView() != null) {
                InputMethodManager inputMethodManager = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getView().getWindowToken(), 0);
            }
            return false;
        });
        mFab.setOnClickListener(view -> enterCommentMode());
    }

    private void enterCommentMode() {
        mFab.setVisibility(View.GONE);
        mCommentContainer.setVisibility(View.VISIBLE);
        if(mCommentEditText.requestFocus()) {
            InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
        }
    }

    private void exitCommentMode() {
        mFab.setVisibility(View.VISIBLE);
        mCommentContainer.setVisibility(View.GONE);
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
        mFab.setVisibility(EasyPrefs.getGuestMode(mActivity) ? View.GONE : View.VISIBLE);
    }

    private boolean userLoggedIn() {
        if (!EasyPrefs.getGuestMode(mActivity)) {
            return true;
        } else {
            Snackbar.make(mRecyclerView, R.string.only_for_logged_in, Snackbar.LENGTH_LONG)
                    .setAction(getString(R.string.login).toUpperCase(Locale.getDefault()), view -> {
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

        mViewModel.getComments().observe(this, comments -> {
            if (comments != null && !comments.isEmpty()) {
                mComments.clear();
                mComments.addAll(comments);
                showComments(this);
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
            mAdapter = new CommentsAdapter(mActivity, mComments, listener);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            mRecyclerView.setLayoutManager(layoutManager);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mRecyclerView.addOnScrollListener(new RecyclerViewScrollListener(this));
            }
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
        Toast.makeText(getContext(), mComments.get(position).getAuthor(), Toast.LENGTH_SHORT).show();
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

    @Override
    public void stoppedScrolling() {
        mFab.setVisibility(View.VISIBLE);
    }

    @Override
    public void scrolling() {
        exitCommentMode();
        mFab.setVisibility(View.GONE);
    }
}
