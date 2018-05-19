package com.example.karlo.learningapplication.database.program;

import com.example.karlo.learningapplication.database.topic.TopicDataSource;
import com.example.karlo.learningapplication.database.track.TrackDataSource;
import com.example.karlo.learningapplication.models.program.Topic;
import com.example.karlo.learningapplication.models.program.Track;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

public class LocalProgramDataSource implements ProgramDataSource {

    private TopicDataSource mTopicDataSource;
    private TrackDataSource mTrackDataSource;

    @Inject
    public LocalProgramDataSource(TopicDataSource topicDataSource, TrackDataSource trackDataSource) {
        this.mTopicDataSource = topicDataSource;
        this.mTrackDataSource = trackDataSource;
    }

    @Override
    public Observable<List<Topic>> getTopics() {
        return mTopicDataSource.getTopics();
    }

    @Override
    public void insertOrUpdateTopic(Topic topic) {
        mTopicDataSource.insertOrUpdateTopic(topic);
    }

    @Override
    public void deleteTopic(Topic topic) {
        mTopicDataSource.deleteTopic(topic);
    }

    @Override
    public Observable<List<Track>> getTracks() {
        return mTrackDataSource.getTracks();
    }

    @Override
    public void insertOrUpdateTrack(Track track) {
        mTrackDataSource.insertOrUpdateTrack(track);
    }

    @Override
    public void deleteTrack(Track track) {
        mTrackDataSource.deleteTrack(track);
    }
}
