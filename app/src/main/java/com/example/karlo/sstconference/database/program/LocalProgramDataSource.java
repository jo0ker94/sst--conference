package com.example.karlo.sstconference.database.program;

import com.example.karlo.sstconference.database.comment.CommentDataSource;
import com.example.karlo.sstconference.database.topic.TopicDataSource;
import com.example.karlo.sstconference.database.track.TrackDataSource;
import com.example.karlo.sstconference.models.program.Comment;
import com.example.karlo.sstconference.models.program.Topic;
import com.example.karlo.sstconference.models.program.Track;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;

public class LocalProgramDataSource implements ProgramDataSource {

    private TopicDataSource mTopicDataSource;
    private TrackDataSource mTrackDataSource;
    private CommentDataSource mCommentDataSource;

    @Inject
    public LocalProgramDataSource(TopicDataSource topicDataSource, TrackDataSource trackDataSource, CommentDataSource commentDataSource) {
        this.mTopicDataSource = topicDataSource;
        this.mTrackDataSource = trackDataSource;
        this.mCommentDataSource = commentDataSource;
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

    @Override
    public Observable<List<Comment>> getComments(int parentId) {
        return mCommentDataSource.getComments(parentId);
    }

    @Override
    public void insertOrUpdateComment(Comment comment) {
        mCommentDataSource.insertOrUpdateComment(comment);
    }

    @Override
    public Completable updateComments(List<Comment> comment) {
        return mCommentDataSource.updateComments(comment);
    }

    @Override
    public void deleteComment(Comment comment) {
        mCommentDataSource.deleteComment(comment);
    }
}
