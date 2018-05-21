package com.example.karlo.learningapplication.modules.subscribed;

import android.arch.lifecycle.MutableLiveData;

import com.example.karlo.learningapplication.commons.BaseViewModel;
import com.example.karlo.learningapplication.commons.Status;
import com.example.karlo.learningapplication.database.program.ProgramDataSource;
import com.example.karlo.learningapplication.database.user.UserDataSource;
import com.example.karlo.learningapplication.helpers.DatabaseHelper;
import com.example.karlo.learningapplication.models.User;
import com.example.karlo.learningapplication.models.program.Topic;

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

    @Inject
    public SubscriptionViewModel(UserDataSource userDataSource, ProgramDataSource topicDataSource) {
        this.mDataSource = topicDataSource;
        this.mUserDataSource = userDataSource;
        getUser();
    }

    private void getUser() {
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

    public void fetchSubscribedTopics() {
        mStatus.setValue(Status.loading(true));
        mCompositeDisposable.add(mDataSource
                .getTopics()
                .flatMap(Observable::fromIterable)
                .filter(topic -> mUser.getSubscribedEvents().contains(topic.getId()))
                .distinct(Topic::getTitle)
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

            DatabaseHelper.getUserReference()
                    .child(mUser.getUserId())
                    .setValue(mUser);

            mCompositeDisposable.add(mUserDataSource
                    .insertOrUpdateUser(mUser)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> mStatus.setValue(Status.delete(topic.getId()))));
        } else {
            mStatus.setValue(Status.error("Error!"));
        }
    }
}
