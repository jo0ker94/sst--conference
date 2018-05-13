package com.example.karlo.learningapplication.modules.program;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.karlo.learningapplication.R;
import com.example.karlo.learningapplication.commons.Constants;
import com.example.karlo.learningapplication.models.program.Person;
import com.example.karlo.learningapplication.models.program.Topic;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TopicDetailsFragment extends BaseProgramFragment {

    @BindView(R.id.topic_title)
    TextView mTitle;
    @BindView(R.id.topic_lecturers)
    TextView mLecturers;

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
    }

    private void setUpViews() {
        Topic topic = getArguments().getParcelable(Constants.DATA);
        if (topic != null) {
            mTitle.setText(topic.getTitle());
            mLecturers.setText(getLecturers(topic.getLecturers()));
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

    /*private void setUpObservers() {
        mViewModel.getTopics().observe(this, topics -> {
            if (topics != null && !topics.isEmpty()) {
                showTopics(topics, this);
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

    public void showTopics(List<Topic> topics, TopicAdapter.OnItemClickListener listener) {
        mTopics.addAll(topics);
        TopicAdapter adapter = new TopicAdapter(topics, listener);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(adapter);
    }*/
}
