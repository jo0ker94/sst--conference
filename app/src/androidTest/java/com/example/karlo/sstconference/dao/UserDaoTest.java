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
    public void insertAndGetOne() {
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

    @Test
    public void deleteItem() {
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
}
