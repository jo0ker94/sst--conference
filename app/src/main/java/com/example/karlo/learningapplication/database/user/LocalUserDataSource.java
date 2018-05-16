package com.example.karlo.learningapplication.database.user;

import com.example.karlo.learningapplication.models.User;

import io.reactivex.Maybe;

public class LocalUserDataSource implements UserDataSource {

    private final UserDao mUserDao;

    public LocalUserDataSource(UserDao userDao) {
        mUserDao = userDao;
    }

    @Override
    public Maybe<User> getUser() {
        return mUserDao.getUser();
    }

    @Override
    public void insertOrUpdateUser(User user) {
        mUserDao.insertUser(user);
    }

    @Override
    public void deleteUser(User user) {
        mUserDao.deleteUser(user);
    }
}