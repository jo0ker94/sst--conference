package com.example.karlo.sstconference.database.committee;

import com.example.karlo.sstconference.models.committee.CommitteeMember;
import com.example.karlo.sstconference.models.enums.CommitteeType;
import com.example.karlo.sstconference.servertasks.interfaces.Api;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class LocalCommitteeDataSource implements CommitteeDataSource {

    private Api mApi;
    private CommitteeDao mDao;

    public LocalCommitteeDataSource(CommitteeDao mDao, Api mApi) {
        this.mApi = mApi;
        this.mDao = mDao;
    }

    @Override
    public Observable<List<CommitteeMember>> getSteeringCommittee() {
        return Observable.concat(getSteeringFromDatabase(),
                getCommitteeFromApi(CommitteeType.STEERING).onErrorResumeNext(Observable.empty()));
    }

    @Override
    public Observable<List<CommitteeMember>> getProgramCommittee() {
        return Observable.concat(getProgramFromDatabase(),
                getCommitteeFromApi(CommitteeType.PROGRAM).onErrorResumeNext(Observable.empty()));
    }

    @Override
    public Observable<List<CommitteeMember>> getOrganizingCommittee() {
        return Observable.concat(getOrganizingFromDatabase(),
                getCommitteeFromApi(CommitteeType.ORGANIZING).onErrorResumeNext(Observable.empty()));
    }

    private Observable<List<CommitteeMember>> getSteeringFromDatabase() {
        return mDao.getSteeringCommittee()
                .toObservable();
    }

    private Observable<List<CommitteeMember>> getProgramFromDatabase() {
        return mDao.getProgramCommittee()
                .toObservable();
    }

    private Observable<List<CommitteeMember>> getOrganizingFromDatabase() {
        return mDao.getOrganizingCommittee()
                .toObservable();
    }

    private Observable<List<CommitteeMember>> getCommitteeFromApi(CommitteeType type) {
        return mApi.getCommittee(type.toString())
                .flatMap(Observable::fromIterable)
                .subscribeOn(Schedulers.newThread())
                .doOnNext(committeeMember -> {
                    committeeMember.setType(type.toString());
                    insertOrUpdateCommitteeMember(committeeMember);
                })
                .toList()
                .toObservable();
    }

    @Override
    public void insertOrUpdateCommitteeMember(CommitteeMember committeeMember) {
        mDao.insertCommitteeMember(committeeMember);
    }

    @Override
    public void deleteCommitteeMember(CommitteeMember committeeMember) {
        mDao.deleteCommitteeMember(committeeMember);
    }


}
