package com.example.karlo.learningapplication.database.track;

import com.example.karlo.learningapplication.models.program.Track;

import java.util.List;

import io.reactivex.Observable;

public interface TrackDataSource {

    Observable<List<Track>> getTracks();

    void insertOrUpdateTrack(Track track);

    void deleteTrack(Track track);
}
