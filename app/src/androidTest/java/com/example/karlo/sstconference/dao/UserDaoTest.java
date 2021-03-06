package com.example.karlo.sstconference.dao;

import com.example.karlo.sstconference.database.user.UserDao;
import com.example.karlo.sstconference.models.User;

import org.junit.Before;
import org.junit.Test;

import io.reactivex.MaybeObserver;
import io.reactivex.disposables.Disposable;

import static junit.framework.Assert.assertEquals;

public class UserDaoTest  extends BaseDaoTest {

    private UserDao mDao;

    @Before
    public void setDao() {
        mDao = localDatabase.userModel();
    }

    @Test
    public void testInsertAndGetOne() {
        mDao.insertUser(getUser());

        mDao.getUser()
                .toObservable()
                .subscribe(user -> {
                    assertEquals(user.getDisplayName(), DISPLAY_NAME);
                    assertEquals(user.getImageUrl(), IMAGE);
                    assertEquals(user.getMail(), MAIL);
                    assertEquals(user.getUserId(), USER_ID);
                });
    }

    public void testInsertAndGetMany() {

    }

    @Test
    public void testDeleteItem() {
        User user = getUser();
        mDao.insertUser(user);

        mDao.getUser()
                .toObservable()
                .subscribe(responseUser -> {
                    assertEquals(user.getDisplayName(), DISPLAY_NAME);
                    assertEquals(user.getImageUrl(), IMAGE);
                    assertEquals(user.getMail(), MAIL);
                    assertEquals(user.getUserId(), USER_ID);
                });

        mDao.deleteUser(user);

        final boolean[] success = {false};
        mDao.getUser()
                .subscribe(new MaybeObserver<User>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onSuccess(User user) {
                        success[0] = true;
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        assertEquals(success[0], false);
                    }
                });
    }

    @Test
    public void testUpdateItem() {
        User user = getUser();
        mDao.insertUser(user);

        user.setDisplayName(NAME);
        mDao.insertUser(user);

        mDao.getUser()
                .toObservable()
                .subscribe(responseUser -> {
                    assertEquals(responseUser.getUserId(), USER_ID);
                    assertEquals(responseUser.getDisplayName(), NAME);
                    assertEquals(responseUser.getImageUrl(), IMAGE);
                    assertEquals(responseUser.getMail(), MAIL);
                    assertEquals(responseUser.getUserId(), USER_ID);
                });
    }
}
