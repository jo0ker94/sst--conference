package com.example.karlo.sstconference.database.keynote;

import com.example.karlo.sstconference.models.keynote.KeynoteSpeaker;

import java.util.List;

import io.reactivex.Observable;

public interface KeynoteDataSource {

    Observable<List<KeynoteSpeaker>> getKeynoteSpeakers();

    void insertOrUpdateKeynoteSpeaker(KeynoteSpeaker keynoteSpeaker);

    void deleteKeynoteSpeaker(KeynoteSpeaker keynoteSpeaker);
}
