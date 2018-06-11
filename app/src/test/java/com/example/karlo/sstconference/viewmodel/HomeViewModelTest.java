package com.example.karlo.sstconference.viewmodel;

import android.arch.lifecycle.Observer;

import com.example.karlo.sstconference.database.program.ProgramDataSource;
import com.example.karlo.sstconference.database.user.UserDataSource;
import com.example.karlo.sstconference.models.User;
import com.example.karlo.sstconference.modules.home.HomeViewModel;

import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class HomeViewModelTest extends BaseViewModelTest {

    @Mock
    private UserDataSource userDataSource;

    @Mock
    private ProgramDataSource programDataSource;

    @Test
    public void testGetUser() {
        User user = getUser();

        Observer observer = mock(Observer.class);

        when(userDataSource.getUser()).thenReturn(io.reactivex.Maybe.just(user));

        HomeViewModel homeViewModel = new HomeViewModel(userDataSource, programDataSource);

        homeViewModel.getUser().observeForever(observer);

        sleep(500);

        verify(observer).onChanged(user);
    }
}