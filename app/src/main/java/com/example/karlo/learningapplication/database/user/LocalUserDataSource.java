package com.example.karlo.learningapplication.database.user;

import com.example.karlo.learningapplication.models.User;
import com.example.karlo.learningapplication.servertasks.interfaces.UserApi;

import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;

public class LocalUserDataSource implements UserDataSource {

    private final UserApi mApi;
    private final UserDao mUserDao;

    public LocalUserDataSource(UserDao userDao, UserApi userApi) {
        mUserDao = userDao;
        mApi = userApi;
    }

    @Override
    public Maybe<User> getUser() {
        return mUserDao.getUser();
    }

    @Override
    public Observable<String> getDisplayName(String id) {
        return mApi.getDisplayName(id);
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
    public void deleteUser(User user) {
        mUserDao.deleteUser(user);
    }
}