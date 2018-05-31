package com.example.karlo.sstconference.database.chairs;

import com.example.karlo.sstconference.models.ConferenceChair;

import java.util.List;

import io.reactivex.Observable;

public interface ChairsDataSource {

    Observable<List<ConferenceChair>> getConferenceChairs();

    void insertConferenceChair(ConferenceChair conferenceChair);

    void deleteConferenceChair(ConferenceChair conferenceChair);
}
