package com.example.karlo.learningapplication.database.user;

import com.example.karlo.learningapplication.models.User;

import io.reactivex.Maybe;

public interface UserDataSource {
    /**
     * Gets the user from the data source.
     *
     * @return the user from the data source.
     */
    Maybe<User> getUser();

    /**
     * Inserts the user into the data source, or, if this is an existing user, updates it.
     *
     * @param user the user to be inserted or updated.
     */
    void insertOrUpdateUser(User user);

    /**
     * Deletes all users from the data source.
     */
    void deleteUser(User user);

}
