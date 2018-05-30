package com.example.karlo.sstconference.modules.committee;

import android.arch.lifecycle.MutableLiveData;

import com.example.karlo.sstconference.base.BaseViewModel;
import com.example.karlo.sstconference.commons.Status;
import com.example.karlo.sstconference.database.committee.CommitteeDataSource;
import com.example.karlo.sstconference.models.ConferenceChair;
import com.example.karlo.sstconference.models.committee.CommitteeMember;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class CommitteeViewModel extends BaseViewModel {

    private MutableLiveData<List<CommitteeMember>> mSteering;
    private MutableLiveData<List<CommitteeMember>> mOrganizing;
    private MutableLiveData<List<CommitteeMember>> mProgram;
    private MutableLiveData<List<ConferenceChair>> mChairs;

    private CommitteeDataSource mCommitteeDataSource;

    @Inject
    public CommitteeViewModel(CommitteeDataSource committeeDataSource) {
        this.mCommitteeDataSource = committeeDataSource;
    }

    public MutableLiveData<List<CommitteeMember>> getSteering() {
        if (mSteering == null) {
            mSteering = new MutableLiveData<>();
            fetchSteeringCommittee();
        }
        return mSteering;
    }

    public MutableLiveData<List<CommitteeMember>> getOrganizing() {
        if (mOrganizing == null) {
            mOrganizing = new MutableLiveData<>();
            fetchOrganizingCommittee();
        }
        return mOrganizing;
    }

    public MutableLiveData<List<CommitteeMember>> getProgram() {
        if (mProgram == null) {
            mProgram = new MutableLiveData<>();
            fetchProgramCommittee();
        }
        return mProgram;
    }

    public MutableLiveData<List<ConferenceChair>> getChairs() {
        if (mChairs == null) {
            mChairs = new MutableLiveData<>();
            fetchConferenceChairs();
        }
        return mChairs;
    }

    public void fetchSteeringCommittee() {
        mStatus.setValue(Status.loading(true));
        mCompositeDisposable.add(
                mCommitteeDataSource.getSteeringCommittee()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(committeeMembers -> {
                                    mSteering.setValue(committeeMembers);
                                    mStatus.setValue(Status.loading(false));
                                },
                                throwable -> {
                                    mStatus.setValue(Status.error(throwable.getMessage()));
                                    mStatus.setValue(Status.loading(false));
                                })
        );
    }

    public void fetchOrganizingCommittee() {
        mStatus.setValue(Status.loading(true));
        mCompositeDisposable.add(
                mCommitteeDataSource.getOrganizingCommittee()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(committeeMembers -> {
                                    mOrganizing.setValue(committeeMembers);
                                    mStatus.setValue(Status.loading(false));
                                },
                                throwable -> {
                                    mStatus.setValue(Status.error(throwable.getMessage()));
                                    mStatus.setValue(Status.loading(false));
                                })
        );
    }

    public void fetchProgramCommittee() {
        mStatus.setValue(Status.loading(true));
        mCompositeDisposable.add(
                mCommitteeDataSource.getProgramCommittee()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(committeeMembers -> {
                                    mProgram.setValue(committeeMembers);
                                    mStatus.setValue(Status.loading(false));
                                },
                                throwable -> {
                                    mStatus.setValue(Status.error(throwable.getMessage()));
                                    mStatus.setValue(Status.loading(false));
                                })
        );
    }

    public void fetchConferenceChairs() {
        mStatus.setValue(Status.loading(true));
        mCompositeDisposable.add(
                mCommitteeDataSource.getConferenceChairs()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(chairs -> {
                                    mChairs.setValue(chairs);
                                    mStatus.setValue(Status.loading(false));
                                },
                                throwable -> {
                                    mStatus.setValue(Status.error(throwable.getMessage()));
                                    mStatus.setValue(Status.loading(false));
                                })
        );
    }
}
