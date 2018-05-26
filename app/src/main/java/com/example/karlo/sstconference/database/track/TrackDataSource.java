package com.example.karlo.sstconference.database.track;

import com.example.karlo.sstconference.models.program.Track;

import java.util.List;

import io.reactivex.Observable;

public interface TrackDataSource {

    Observable<List<Track>> getTracks();

    void insertOrUpdateTrack(Track track);

    void deleteTrack(Track track);
}
