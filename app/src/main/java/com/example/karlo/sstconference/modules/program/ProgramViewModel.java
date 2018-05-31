package com.example.karlo.sstconference.modules.program;

import android.arch.lifecycle.MutableLiveData;

import com.example.karlo.sstconference.base.BaseViewModel;
import com.example.karlo.sstconference.commons.Status;
import com.example.karlo.sstconference.database.program.ProgramDataSource;
import com.example.karlo.sstconference.database.user.UserDataSource;
import com.example.karlo.sstconference.helpers.DatabaseHelper;
import com.example.karlo.sstconference.models.User;
import com.example.karlo.sstconference.models.program.Comment;
import com.example.karlo.sstconference.models.program.Topic;
import com.example.karlo.sstconference.models.program.Track;
import com.example.karlo.sstconference.servertasks.interfaces.Api;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ProgramViewModel extends BaseViewModel {

    private static final String TAG = "ProgramViewModel";

    private static final String NO_COMMENTS = "Null is not a valid element";

    private MutableLiveData<User> mLiveUser;
    private MutableLiveData<List<User>> mUsers;
    private MutableLiveData<List<Track>> mTracks;
    private MutableLiveData<List<Topic>> mTopics;
    private MutableLiveData<List<Comment>> mLiveComments;

    private User mUser;
    private List<Comment> mComments;

    private Api mApi;
    private UserDataSource mUserDataSource;
    private ProgramDataSource mProgramDataSource;

    @Inject
    public ProgramViewModel(Api api, UserDataSource userDataSource, ProgramDataSource programDataSource) {
        this.mApi = api;
        this.mUserDataSource = userDataSource;
        this.mProgramDataSource = programDataSource;
    }

    public MutableLiveData<List<Track>> getTracks() {
        if (mTracks == null) {
            mTracks = new MutableLiveData<>();
            fetchTracks();
        }
        return mTracks;
    }

    public MutableLiveData<List<Topic>> getTopics() {
        if (mTopics == null) {
            mTopics = new MutableLiveData<>();
        }
        return mTopics;
    }

    public MutableLiveData<List<Comment>> getComments() {
        if (mLiveComments == null) {
            mLiveComments = new MutableLiveData<>();
        }
        return mLiveComments;
    }

    public MutableLiveData<User> getUser() {
        if (mLiveUser == null) {
            mLiveUser = new MutableLiveData<>();
            fetchUser();
        }
        return mLiveUser;
    }

    public MutableLiveData<List<User>> getUsers() {
        if (mUsers == null) {
            mUsers = new MutableLiveData<>();
            fetchAllUsers();
        }
        return mUsers;
    }

    public void fetchComments(int commentID) {
        mStatus.setValue(Status.loading(true));
        mCompositeDisposable.add(mApi
                .getComments(commentID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(comments -> {
                            mComments = new ArrayList<>(comments);
                            mLiveComments.setValue(comments);
                            mStatus.setValue(Status.loading(false));
                        }
                        ,
                        throwable -> {
                            mStatus.setValue(Status.loading(false));
                            if (throwable.getMessage().equalsIgnoreCase(NO_COMMENTS)) {
                                mStatus.setValue(Status.noData(true));
                            } else {
                                mStatus.setValue(Status.error(throwable.getMessage()));

                            }
                        }
                ));
    }

    public void fetchTrack(int id) {
        mStatus.setValue(Status.loading(true));
        mCompositeDisposable.add(mProgramDataSource
                .getTracks()
                .flatMap(Observable::fromIterable)
                .filter(track -> track.getId() == id)
                .distinct(Track::getId)
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(tracks -> {
                            mTracks.setValue(tracks);
                            mStatus.setValue(Status.loading(false));
                        }
                        ,
                        throwable -> {
                            mStatus.setValue(Status.error(throwable.getMessage()));
                            mStatus.setValue(Status.loading(false));
                        }
                ));
    }

    private void fetchTracks() {
        mStatus.setValue(Status.loading(true));
        mCompositeDisposable.add(mProgramDataSource
                .getTracks()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(tracks -> {
                            mTracks.setValue(tracks);
                            mStatus.setValue(Status.loading(false));
                        }
                        ,
                        throwable -> {
                            mStatus.setValue(Status.error(throwable.getMessage()));
                            mStatus.setValue(Status.loading(false));
                        }
                ));
    }

    public void fetchTopics(int position) {
        mStatus.setValue(Status.loading(true));
        mCompositeDisposable.add(mProgramDataSource
                .getTopics()
                .flatMap(Observable::fromIterable)
                .filter(topic -> topic.getParentId() == position)
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

    private void fetchAllUsers() {
        mCompositeDisposable.add(mUserDataSource
                .getUsers()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(users -> {
                            mUsers.setValue(new ArrayList<>(users.values()));
                            mStatus.setValue(Status.loading(false));
                        }
                        ,
                        throwable -> {
                            mStatus.setValue(Status.error(throwable.getMessage()));
                            mStatus.setValue(Status.loading(false));
                        }
                ));
    }

    private void fetchUser() {
        mCompositeDisposable.add(mUserDataSource
                .getUser()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(user -> {
                            mUser = user;
                            mLiveUser.setValue(user);
                        }
                ));
    }

    public void subscribeToTopic(Topic topic) {
        List<Integer> events = mUser.getSubscribedEvents();
        if (!events.contains(topic.getId())) {
            events.add(topic.getId());
            mUser.setSubscribedEvents(events);

            DatabaseHelper.getUserReference()
                    .child(mUser.getUserId())
                    .setValue(mUser);

            mCompositeDisposable.add(mUserDataSource
                    .insertOrUpdateUser(mUser)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> mStatus.setValue(Status.message("Subscribed!"))));
        } else {
            mStatus.setValue(Status.message("Already subscribed!"));
        }
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
                    .subscribe(() -> mStatus.setValue(Status.message("Unsubscribed!"))));
        } else {
            mStatus.setValue(Status.error("Error!"));
        }
    }

    public Completable deleteComment(Comment comment) {
        return Completable.fromAction(() -> {
            mComments.remove(comment);
            DatabaseHelper.getCommentsReference()
                    .child(String.valueOf(comment.getParentId()))
                    .setValue(mComments);
        });
    }

    public Completable addComment(Comment comment) {
        if (mComments == null) {
            mComments = new ArrayList<>();
        }
        return Completable.fromAction(() -> DatabaseHelper.getCommentsReference()
                .child(String.valueOf(comment.getParentId()))
                .child(String.valueOf(mComments.size()))
                .setValue(comment));
    }

    public class Response {
        private List<User> users;
        private List<Comment> comments;

        public List<User> getUsers() {
            return users;
        }

        public void setUsers(List<User> users) {
            this.users = users;
        }

        public List<Comment> getComments() {
            return comments;
        }

        public void setComments(List<Comment> comments) {
            this.comments = comments;
        }
    }
}
