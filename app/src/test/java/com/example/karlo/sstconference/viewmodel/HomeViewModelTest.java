package com.example.karlo.sstconference.viewmodel;

import android.arch.lifecycle.Observer;

import com.example.karlo.sstconference.commons.Status;
import com.example.karlo.sstconference.database.user.UserDataSource;
import com.example.karlo.sstconference.models.User;
import com.example.karlo.sstconference.modules.home.HomeViewModel;
import com.google.firebase.auth.FirebaseAuth;

import org.junit.Test;
import org.mockito.Mock;

import io.reactivex.Completable;

import static junit.framework.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class HomeViewModelTest extends BaseViewModelTest {

    @Mock
    private UserDataSource userDataSource;

    @Mock
    private FirebaseAuth firebaseAuth;

    @Test
    public void testGetUser() {
        User user = getUser();

        Observer observer = mock(Observer.class);

        when(userDataSource.getUser()).thenReturn(io.reactivex.Maybe.just(user));

        HomeViewModel homeViewModel = new HomeViewModel(userDataSource, firebaseAuth);

        homeViewModel.getUser().observeForever(observer);

        sleep(500);

        verify(observer).onChanged(user);
    }

    @Test
    public void testSignOut() {
        User user = getUser();

        HomeViewModel homeViewModel = new HomeViewModel(userDataSource, firebaseAuth);

        Observer observer = mock(Observer.class);

        when(userDataSource.getUser()).thenReturn(io.reactivex.Maybe.just(user));
        when(userDataSource.deleteUser(any(User.class))).thenReturn(Completable.complete());

        homeViewModel.getUser().observeForever(observer);
        sleep(1000);
        verify(observer).onChanged(user);
        homeViewModel.getStatus().observeForever(observer);
        homeViewModel.signOut();

        sleep(500);

        assertEquals(homeViewModel.getStatus().getValue().getResponse(), Status.Response.LOGOUT);
    }
}