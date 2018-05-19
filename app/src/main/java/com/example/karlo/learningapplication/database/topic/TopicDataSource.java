package com.example.karlo.learningapplication.database.topic;

import com.example.karlo.learningapplication.models.program.Topic;

import java.util.List;

import io.reactivex.Observable;

public interface TopicDataSource {

    Observable<List<Topic>> getTopics();

    void insertOrUpdateTopic(Topic topic);

    void deleteTopic(Topic topic);
}
