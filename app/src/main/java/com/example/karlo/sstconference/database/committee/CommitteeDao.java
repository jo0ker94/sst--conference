package com.example.karlo.sstconference.database.committee;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.karlo.sstconference.models.ConferenceChair;
import com.example.karlo.sstconference.models.committee.CommitteeMember;

import java.util.List;

import io.reactivex.Maybe;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface CommitteeDao {

    @Query("SELECT * FROM CommitteeMember WHERE mType = 'steering'")
    Maybe<List<CommitteeMember>> getSteeringCommittee();

    @Query("SELECT * FROM CommitteeMember WHERE mType = 'organizing'")
    Maybe<List<CommitteeMember>> getOrganizingCommittee();

    @Query("SELECT * FROM CommitteeMember WHERE mType = 'program'")
    Maybe<List<CommitteeMember>> getProgramCommittee();

    @Query("SELECT * FROM ConferenceChair")
    Maybe<List<ConferenceChair>> getConferenceChairs();

    @Insert(onConflict = REPLACE)
    long insertCommitteeMember(CommitteeMember committeeMember);

    @Delete
    void deleteCommitteeMember(CommitteeMember committeeMember);

    @Insert(onConflict = REPLACE)
    long insertConferenceChair(ConferenceChair conferenceChair);

    @Delete
    void deleteConferenceChair(ConferenceChair conferenceChair);
}

