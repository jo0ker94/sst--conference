package com.example.karlo.learningapplication.modules.program.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.karlo.learningapplication.R;
import com.example.karlo.learningapplication.adapters.CommentsAdapter;
import com.example.karlo.learningapplication.commons.Constants;
import com.example.karlo.learningapplication.commons.OnRecyclerViewScrollListener;
import com.example.karlo.learningapplication.commons.RecyclerViewScrollListener;
import com.example.karlo.learningapplication.models.User;
import com.example.karlo.learningapplication.models.program.Comment;
import com.example.karlo.learningapplication.models.program.Person;
import com.example.karlo.learningapplication.models.program.Topic;
import com.example.karlo.learningapplication.utility.DateUtility;

import java.util.ArrayList;
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

    private List<User> mUsers = new ArrayList<>();
    private List<Comment> mComments = new ArrayList<>();

    private User mUser;
    private Topic mTopic;
    private CommentsAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    private Parcelable mListState;
    private int position = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_topic_details, container, false);
        mUnbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpViews();
        setUpListeners();
        setUpObservers();
        mTitle.setOnClickListener(v -> mViewModel.subscribeToTopic(mTopic));
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
        mTopic = getArguments().getParcelable(Constants.DATA);
        if (mTopic != null) {
            mTitle.setText(mTopic.getTitle());
            if (mTopic.getLecturers() != null) {
                mLecturers.setText(getLecturers(mTopic.getLecturers()));
            }
            if (mTopic.getId() != -1) {
                mViewModel.fetchComments(mTopic.getId());
                mCommentContainer.setVisibility(View.VISIBLE);
            } else {
                mCommentContainer.setVisibility(View.GONE);
            }
        }
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
                mComments.addAll(comments);
                if (!mUsers.isEmpty()) {
                    showComments(this);
                }
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
            }
        });
    }

    public void showComments(CommentsAdapter.OnItemClickListener listener) {
        if (mAdapter == null) {
            mAdapter = new CommentsAdapter(getActivity(), mComments, mUsers, listener);
            mLayoutManager = new LinearLayoutManager(getContext());
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.addOnScrollListener(new RecyclerViewScrollListener(this));
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(getContext(), mComments.get(position).getUserId(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemLongClick(View view, int position) {
        Comment comment = mComments.get(position);
        if (comment.getUserId().equalsIgnoreCase(mUser.getUserId())) {
            new AlertDialog.Builder(getActivity())
                    .setMessage(R.string.delete_comment_message)
                    .setPositiveButton(R.string.yes,
                            (dialogInterface, i) -> mCompositeDisposable.add(
                                    mViewModel.deleteComment(comment)
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(() -> {
                                                mComments.remove(comment);
                                                mAdapter.notifyDataSetChanged();
                                            })))
                    .setNegativeButton(R.string.no, null)
                    .show();
        }
    }

    private void scrollMode() {
        if (mCommentContainer.getVisibility() == View.VISIBLE) {
            mCommentContainer.setVisibility(View.GONE);
            mTopicContainer.setVisibility(View.GONE);
            if (mListState != null) {
                mLayoutManager.onRestoreInstanceState(mListState);
            }
        }
    }

    private void exitScrollMode() {
        if (mCommentContainer.getVisibility() == View.GONE) {
            mCommentContainer.setVisibility(View.VISIBLE);
            mTopicContainer.setVisibility(View.VISIBLE);
            mRecyclerView.scrollToPosition(position);
        }
    }

    @Override
    public void stoppedScrolling() {
        //if (!mRecyclerView.canScrollVertically(-1)) {
        //    position = 0;
        //} else {
        //    position = mLayoutManager.findLastCompletelyVisibleItemPosition();
        //}
        //mListState = mLayoutManager.onSaveInstanceState();
        //exitScrollMode();
    }

    @Override
    public void scrolling() {
        //scrollMode();
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
