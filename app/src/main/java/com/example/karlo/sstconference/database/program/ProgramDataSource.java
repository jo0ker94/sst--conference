package com.example.karlo.sstconference.database.program;

import com.example.karlo.sstconference.models.program.Comment;
import com.example.karlo.sstconference.models.program.Topic;
import com.example.karlo.sstconference.models.program.Track;

import java.util.List;

import io.reactivex.Observable;

public interface ProgramDataSource {

    Observable<List<Topic>> getTopics();

    void insertOrUpdateTopic(Topic topic);

    void deleteTopic(Topic topic);

    Observable<List<Track>> getTracks();

    void insertOrUpdateTrack(Track track);

    void deleteTrack(Track track);

    Observable<List<Comment>> getComments(int parentId);

    void insertOrUpdateComment(Comment comment);

    void deleteComment(Comment comment);
}
