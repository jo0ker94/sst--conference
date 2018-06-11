package com.example.karlo.sstconference.viewmodel;

import android.arch.lifecycle.Observer;

import com.example.karlo.sstconference.database.keynote.KeynoteDataSource;
import com.example.karlo.sstconference.models.keynote.KeynoteSpeaker;
import com.example.karlo.sstconference.modules.keynotespeakers.KeynoteViewModel;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class KeynoteViewModelTest extends BaseViewModelTest {

    @Mock
    private KeynoteDataSource dataSource;

    @InjectMocks
    private KeynoteViewModel keynoteViewModel;

    @Test
    public void testGetSpeakers() {
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
        Observer observer = mock(Observer.class);

        when(dataSource.getKeynoteSpeakers()).thenReturn(io.reactivex.Observable.just(speakers));

        keynoteViewModel.getSpeakers().observeForever(observer);

        sleep(500);

        verify(observer).onChanged(speakers);
    }
}