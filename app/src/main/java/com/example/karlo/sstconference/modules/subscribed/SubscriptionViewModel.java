package com.example.karlo.sstconference.modules.subscribed;

import android.arch.lifecycle.MutableLiveData;

import com.example.karlo.sstconference.base.BaseViewModel;
import com.example.karlo.sstconference.commons.Status;
import com.example.karlo.sstconference.database.program.ProgramDataSource;
import com.example.karlo.sstconference.database.user.UserDataSource;
import com.example.karlo.sstconference.models.User;
import com.example.karlo.sstconference.models.program.Topic;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SubscriptionViewModel extends BaseViewModel {

    private MutableLiveData<List<Topic>> mTopics;

    private User mUser;
    private UserDataSource mUserDataSource;
    private ProgramDataSource mDataSource;
    private FirebaseDatabase mFirebaseDatabase;

    @Inject
    public SubscriptionViewModel(UserDataSource userDataSource, ProgramDataSource topicDataSource, FirebaseDatabase firebaseDatabase) {
        this.mDataSource = topicDataSource;
        this.mUserDataSource = userDataSource;
        this.mFirebaseDatabase = firebaseDatabase;
    }

    public void getUserAndFetchEvents() {
        mCompositeDisposable.add(mUserDataSource.getUser()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(user -> {
                    mUser = user;
                    fetchSubscribedTopics();
                }));
    }

    public MutableLiveData<List<Topic>> getSubscribedTopics() {
        if (mTopics == null) {
            mTopics = new MutableLiveData<>();
        }
        return mTopics;
    }

    private void fetchSubscribedTopics() {
        mStatus.setValue(Status.loading(true));
        mCompositeDisposable.add(mDataSource
                .getTopics()
                .flatMap(Observable::fromIterable)
                .filter(topic -> mUser.getSubscribedEvents().contains(topic.getId()))
                .distinct(Topic::getId)
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(topics -> {
                            mTopics.setValue(topics);
                            mStatus.setValue(Status.loading(false));
                        }
                        ,
                        throwable -> {
                            mStatus.setValue(Status.error(throwable.getMessage()));
                            mStatus.setValue(Status.loading(false));
                        }
                ));
    }

    public void deleteTopicSubscription(Topic topic) {
        List<Integer> events = mUser.getSubscribedEvents();
        if (events.contains(topic.getId())) {
            events.remove((Integer) topic.getId());
            mUser.setSubscribedEvents(events);

            mCompositeDisposable.add(
                    mUserDataSource
                            .insertOrUpdateUser(mUser)
                            .subscribe(() ->
                                    mStatus.postValue(Status.delete(topic.getId()))));
        } else {
            mStatus.setValue(Status.error("Error!"));
        }
    }
}
