package com.example.karlo.sstconference.database.user;

import com.example.karlo.sstconference.models.User;
import com.example.karlo.sstconference.servertasks.interfaces.UserApi;

import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;

public class LocalUserDataSource implements UserDataSource {

    private final UserApi mApi;
    private final UserDao mUserDao;

    @Inject
    public LocalUserDataSource(UserDao userDao, UserApi userApi) {
        mUserDao = userDao;
        mApi = userApi;
    }

    @Override
    public Maybe<User> getUser() {
        return mUserDao.getUser();
    }

    @Override
    public Observable<User> getUserFromServer(String id) {
        return mApi.getUser(id);
    }

    @Override
    public Observable<Map<String, User>> getUsers() {
        return mApi.getUsers();
    }

    @Override
    public Completable insertOrUpdateUser(User user) {
        return Completable.fromAction(() -> mUserDao.insertUser(user));
    }

    @Override
    public Completable deleteUser(User user) {
        return Completable.fromAction(() -> mUserDao.deleteUser(user));
    }
}