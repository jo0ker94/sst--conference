package com.example.karlo.sstconference.datasource;

import com.example.karlo.sstconference.database.user.LocalUserDataSource;
import com.example.karlo.sstconference.database.user.UserDao;
import com.example.karlo.sstconference.models.User;
import com.example.karlo.sstconference.servertasks.interfaces.UserApi;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import io.reactivex.Maybe;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserDataSourceTest extends BaseDataSourceTest {

    @Mock
    private UserDao dao;

    @Mock
    private UserApi api;

    @InjectMocks
    private LocalUserDataSource dataSource;

    @Test
    public void testGetSaveAndDelete() {
        User user = getUser();

        when(dao.getUser()).thenReturn(Maybe.just(user));

        dataSource.deleteUser(user);
        verify(dao).deleteUser(user);

        dataSource.getUser();
        verify(dao).getUser();

        dataSource.getUser()
                .subscribe(responseUser ->
                        assertEquals(responseUser.getDisplayName(), user.getDisplayName()));
    }
}