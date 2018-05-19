package com.example.karlo.learningapplication.modules.program.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.karlo.learningapplication.R;
import com.example.karlo.learningapplication.adapters.TopicAdapter;
import com.example.karlo.learningapplication.commons.Constants;
import com.example.karlo.learningapplication.models.program.Topic;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TopicListFragment extends BaseProgramFragment
        implements TopicAdapter.OnItemClickListener {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private TopicAdapter mAdapter;
    private List<Topic> mTopics = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_topic_layout, container, false);
        mUnbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpObservers();
        mViewModel.fetchTopics(getArguments().getInt(Constants.POSITION));
    }

    private void setUpObservers() {
        mViewModel.getTopics().observe(this, topics -> {
            if (topics != null && !topics.isEmpty()) {
                mTopics.clear();
                mTopics.addAll(topics);
                showTopics(this);
            } else {
                Topic topic = new Topic(-1,
                        getArguments().getInt(Constants.POSITION),
                        getArguments().getString(Constants.NAME),
                        null);
                mListener.showTopicDetails(topic, true);
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

    public void showTopics(TopicAdapter.OnItemClickListener listener) {
        if (mAdapter == null) {
            mAdapter = new TopicAdapter(mTopics, listener);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        mListener.showTopicDetails(mTopics.get(position), false);
    }
}
