package com.example.karlo.sstconference.datasource;

import com.example.karlo.sstconference.database.chairs.ChairsDao;
import com.example.karlo.sstconference.database.chairs.LocalChairsDataSource;
import com.example.karlo.sstconference.models.ConferenceChair;
import com.example.karlo.sstconference.servertasks.interfaces.Api;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Observable;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ChairsDataSourceTest extends BaseDataSourceTest {

    @Mock
    private ChairsDao dao;

    @Mock
    private Api api;

    @InjectMocks
    private LocalChairsDataSource chairsDataSource;

    @Test
    public void testGetSaveAndDelete() {
        List<ConferenceChair> conferenceChairs = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            conferenceChairs.add(new ConferenceChair(i,
                    getStringFormat(NAME, i),
                    getStringFormat(MAIL, i),
                    getStringFormat(FACILITY, i),
                    getStringFormat(IMAGE, i),
                    getStringFormat(NAME, i),
                    getStringFormat(NUMBER,i)));
        }

        List<ConferenceChair> apiChairs = new ArrayList<>(conferenceChairs);
        ConferenceChair chair = getConferenceChair(123);
        apiChairs.add(chair);

        when(dao.getConferenceChairs()).thenReturn(Maybe.just(conferenceChairs));
        when(api.getChairs()).thenReturn(Observable.just(apiChairs));

        chairsDataSource.insertConferenceChair(chair);
        verify(dao).insertConferenceChair(chair);
        //conferenceChairs.add(chair);

        chairsDataSource.deleteConferenceChair(conferenceChairs.get(0));
        verify(dao).deleteConferenceChair(conferenceChairs.get(0));
        //conferenceChairs.remove(conferenceChairs.get(0));

        chairsDataSource.getConferenceChairs();
        verify(dao).getConferenceChairs();
        verify(api).getChairs();

        chairsDataSource.getConferenceChairs()
                .flatMap(Observable::fromIterable)
                .distinct(ConferenceChair::getId)
                .toList()
                .subscribe(chairs -> {
                    for (int i = 0; i < chairs.size(); i++) {
                        assertEquals(chairs.get(i), apiChairs.get(i));
                    }
                });
    }
}
