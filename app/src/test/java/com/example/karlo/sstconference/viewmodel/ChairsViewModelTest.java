package com.example.karlo.sstconference.viewmodel;

import android.arch.lifecycle.Observer;

import com.example.karlo.sstconference.database.chairs.ChairsDataSource;
import com.example.karlo.sstconference.models.ConferenceChair;
import com.example.karlo.sstconference.modules.chairs.ChairsViewModel;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ChairsViewModelTest extends BaseViewModelTest {

    @Mock
    private ChairsDataSource dataSource;

    @InjectMocks
    private ChairsViewModel chairsViewModel;

    @Test
    public void testGetChairs() {
        List<ConferenceChair> conferenceChairs = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            conferenceChairs.add(new ConferenceChair(i, TITLE, MAIL, FACILITY, IMAGE, NAME, NUMBER));
            //conferenceChairs.add(mock(ConferenceChair.class));
        }
        Observer observer = mock(Observer.class);

        when(dataSource.getConferenceChairs()).thenReturn(io.reactivex.Observable.just(conferenceChairs));

        chairsViewModel.getChairs().observeForever(observer);

        sleep(500);

        verify(observer).onChanged(conferenceChairs);
    }
}
