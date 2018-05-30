package com.example.karlo.sstconference.database.committee;

import com.example.karlo.sstconference.models.ConferenceChair;
import com.example.karlo.sstconference.models.committee.CommitteeMember;

import java.util.List;

import io.reactivex.Observable;

public interface CommitteeDataSource {

    Observable<List<CommitteeMember>> getSteeringCommittee();

    Observable<List<CommitteeMember>> getProgramCommittee();

    Observable<List<CommitteeMember>> getOrganizingCommittee();

    Observable<List<ConferenceChair>> getConferenceChairs();

    void insertOrUpdateCommitteeMember(CommitteeMember committeeMember);

    void deleteCommitteeMember(CommitteeMember committeeMember);

    void insertConferenceChair(ConferenceChair conferenceChair);

    void deleteConferenceChair(ConferenceChair conferenceChair);
}
