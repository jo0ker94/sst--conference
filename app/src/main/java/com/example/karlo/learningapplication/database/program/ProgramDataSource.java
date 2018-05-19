package com.example.karlo.learningapplication.database.program;

import com.example.karlo.learningapplication.models.program.Topic;
import com.example.karlo.learningapplication.models.program.Track;

import java.util.List;

import io.reactivex.Observable;

public interface ProgramDataSource {

    Observable<List<Topic>> getTopics();

    void insertOrUpdateTopic(Topic topic);

    void deleteTopic(Topic topic);

    Observable<List<Track>> getTracks();

    void insertOrUpdateTrack(Track track);

    void deleteTrack(Track track);
}
