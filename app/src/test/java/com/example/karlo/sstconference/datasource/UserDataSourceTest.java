package com.example.karlo.sstconference.datasource;

import com.example.karlo.sstconference.database.user.LocalUserDataSource;
import com.example.karlo.sstconference.database.user.UserDao;
import com.example.karlo.sstconference.models.User;
import com.example.karlo.sstconference.servertasks.interfaces.UserApi;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;

import static junit.framework.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
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

        dataSource.deleteUser(user).subscribe(() -> verify(dao).deleteUser(user));

        dataSource.getUser();
        verify(dao).getUser();

        dataSource.getUser()
                .subscribe(responseUser ->
                        assertEquals(responseUser.getDisplayName(), user.getDisplayName()));
    }

    @Test
    public void testGetUsers() {
        Map<String, User> userMap = new HashMap<>();
        List<String> ids = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            ids.add(getStringFormat(USER_ID, i));
        }
        List<User> users = getUsers(10);

        for (int i = 0; i < 10; i++) {
            userMap.put(ids.get(i), users.get(i));
        }

        when(api.getUsers()).thenReturn(Observable.just(userMap));

        dataSource.getUsers();
        verify(api).getUsers();

        dataSource.getUsers()
                .subscribe(responseMap -> {
                    List<User> userValues = new ArrayList<>(responseMap.values());
                    Collections.sort(userValues, new UserComparator());
                    assertEquals(userValues, users);
                });
    }

    @Test
    public void testGetUserFromServer() {
        User user = getUser();

        when(api.getUser(USER_ID)).thenReturn(Observable.just(user));

        dataSource.getUserFromServer(USER_ID);
        verify(api).getUser(USER_ID);

        dataSource.getUserFromServer(USER_ID)
                .subscribe(responseUser ->
                        assertEquals(responseUser.getDisplayName(), user.getDisplayName()));
    }

    public class UserComparator implements Comparator<User> {
        @Override
        public int compare(User o1, User o2) {
            return o1.getUserId().compareTo(o2.getUserId());
        }
    }

}