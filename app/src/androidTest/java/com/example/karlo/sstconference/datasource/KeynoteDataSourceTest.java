package com.example.karlo.sstconference.datasource;

import com.example.karlo.sstconference.database.keynote.KeynoteDao;
import com.example.karlo.sstconference.database.keynote.LocalKeynoteDataSource;
import com.example.karlo.sstconference.models.keynote.KeynoteSpeaker;
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

public class KeynoteDataSourceTest extends BaseDataSourceTest {

    @Mock
    private KeynoteDao dao;

    @Mock
    private Api api;

    @InjectMocks
    private LocalKeynoteDataSource dataSource;

    @Test
    public void testGetSaveAndDelete() {
        List<KeynoteSpeaker> speakers = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            speakers.add(new KeynoteSpeaker(i,
                    getStringFormat(NAME, i),
                    getStringFormat(FACILITY, i),
                    getStringFormat(MAIL, i),
                    getStringFormat(IMAGE, i),
                    getStringFormat(TITLE, i),
                    getStringFormat(ABSTRACT, i)));
        }

        List<KeynoteSpeaker> apiSpeakers = new ArrayList<>(speakers);
        KeynoteSpeaker speaker = getKeynoteSpeaker(123);
        apiSpeakers.add(speaker);

        when(dao.getKeynoteSpeakers()).thenReturn(Maybe.just(speakers));
        when(api.getKeynoteSpeakers()).thenReturn(Observable.just(apiSpeakers));

        dataSource.insertOrUpdateKeynoteSpeaker(speaker);
        verify(dao).insertKeynoteSpeaker(speaker);

        dataSource.deleteKeynoteSpeaker(speakers.get(0));
        verify(dao).deleteKeynoteSpeaker(speakers.get(0));

        dataSource.getKeynoteSpeakers();
        verify(dao).getKeynoteSpeakers();
        verify(api).getKeynoteSpeakers();

        dataSource.getKeynoteSpeakers()
                .flatMap(Observable::fromIterable)
                .distinct(KeynoteSpeaker::getId)
                .toList()
                .subscribe(keynoteSpeakers -> {
                    for (int i = 0; i < keynoteSpeakers.size(); i++) {
                        assertEquals(keynoteSpeakers.get(i), apiSpeakers.get(i));
                    }
                });
    }
}