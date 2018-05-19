package com.example.karlo.learningapplication.modules.program;

import android.os.Bundle;
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
import com.example.karlo.learningapplication.helpers.DatabaseHelper;
import com.example.karlo.learningapplication.models.User;
import com.example.karlo.learningapplication.models.program.Comment;
import com.example.karlo.learningapplication.models.program.Person;
import com.example.karlo.learningapplication.models.program.Topic;
import com.example.karlo.learningapplication.utility.DateUtility;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TopicDetailsFragment extends BaseProgramFragment
        implements CommentsAdapter.OnItemClickListener {

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

    private List<User> mUsers = new ArrayList<>();
    private List<Comment> mComments = new ArrayList<>();

    private User mUser;
    private Topic mTopic;
    private CommentsAdapter mAdapter;

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
    }

    private void setUpListeners() {
        mCommentEditText.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE){
                Comment comment = new Comment(
                        mComments.size(),
                        mCommentEditText.getText().toString(),
                        mUser.getUserId(),
                        DateUtility.getNowInIsoFormat()
                );
                DatabaseHelper.getCommentsReference()
                        .child(String.valueOf(mTopic.getId()))
                        .child(String.valueOf(mComments.size()))
                        .setValue(comment);

                mCommentEditText.setText("");
                mComments.add(comment);
                showComments(this);
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
                case ERROR:
                    mListener.showError(new Throwable(status.getMessage()));
                    break;
            }
        });
    }

    public void showComments(CommentsAdapter.OnItemClickListener listener) {
        if (mAdapter == null) {
            mAdapter = new CommentsAdapter(getActivity(), mComments, mUsers, listener);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(getContext(), mComments.get(position).getUserId(), Toast.LENGTH_SHORT).show();
    }
}
