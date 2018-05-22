package com.example.karlo.learningapplication.modules.program;

import android.arch.lifecycle.MutableLiveData;

import com.example.karlo.learningapplication.commons.BaseViewModel;
import com.example.karlo.learningapplication.commons.Status;
import com.example.karlo.learningapplication.database.program.ProgramDataSource;
import com.example.karlo.learningapplication.database.user.UserDataSource;
import com.example.karlo.learningapplication.helpers.DatabaseHelper;
import com.example.karlo.learningapplication.models.User;
import com.example.karlo.learningapplication.models.program.Comment;
import com.example.karlo.learningapplication.models.program.Topic;
import com.example.karlo.learningapplication.models.program.Track;
import com.example.karlo.learningapplication.servertasks.interfaces.Api;
import com.example.karlo.learningapplication.utility.DateUtility;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class ProgramViewModel extends BaseViewModel {

    private static final String TAG = "ProgramViewModel";

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
                            mStatus.setValue(Status.error(throwable.getMessage()));
                            mStatus.setValue(Status.loading(false));
                        }
                ));
    }

    public void fetchTracks() {
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

    public void fetchAllUsers() {
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

    public void fetchUser() {
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

    public void fetchData() {
        mCompositeDisposable.add(mUserDataSource
                .getUsers()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap((Function<Map<String, User>, ObservableSource<?>>) stringUserMap ->
                        Observable.concat(Observable.fromArray(
                                new ArrayList<>(stringUserMap.values())),
                                mApi.getComments(1)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                        ))
                .subscribe()
        );
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

    public Completable deleteComment(Comment comment) {
        return Completable.fromAction(() -> {
            mComments.remove(comment);
            DatabaseHelper.getCommentsReference()
                    .child(String.valueOf(comment.getParentId()))
                    .setValue(mComments);
        });
    }

    public Completable addComment(Comment comment) {
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
