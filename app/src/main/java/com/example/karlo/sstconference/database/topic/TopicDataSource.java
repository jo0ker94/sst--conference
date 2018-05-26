package com.example.karlo.sstconference.database.topic;

import com.example.karlo.sstconference.models.program.Topic;

import java.util.List;

import io.reactivex.Observable;

public interface TopicDataSource {

    Observable<List<Topic>> getTopics();

    void insertOrUpdateTopic(Topic topic);

    void deleteTopic(Topic topic);
}
