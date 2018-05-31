package com.example.karlo.sstconference.modules.chairs;

import android.arch.lifecycle.MutableLiveData;

import com.example.karlo.sstconference.base.BaseViewModel;
import com.example.karlo.sstconference.commons.Status;
import com.example.karlo.sstconference.database.chairs.ChairsDataSource;
import com.example.karlo.sstconference.models.ConferenceChair;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ChairsViewModel extends BaseViewModel {

    private MutableLiveData<List<ConferenceChair>> mChairs;

    private ChairsDataSource mDataSource;

    @Inject
    public ChairsViewModel(ChairsDataSource dataSource) {
        this.mDataSource = dataSource;
    }

    public MutableLiveData<List<ConferenceChair>> getChairs() {
        if (mChairs == null) {
            mChairs = new MutableLiveData<>();
            fetchConferenceChairs();
        }
        return mChairs;
    }

    private void fetchConferenceChairs() {
        mStatus.setValue(Status.loading(true));
        mCompositeDisposable.add(
                mDataSource.getConferenceChairs()
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
