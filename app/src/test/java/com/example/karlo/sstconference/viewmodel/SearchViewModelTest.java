package com.example.karlo.sstconference.viewmodel;

import android.arch.lifecycle.Observer;

import com.example.karlo.sstconference.database.program.ProgramDataSource;
import com.example.karlo.sstconference.models.program.Topic;
import com.example.karlo.sstconference.models.program.Track;
import com.example.karlo.sstconference.modules.search.SearchViewModel;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SearchViewModelTest extends BaseViewModelTest {

    @Mock
    private ProgramDataSource dataSource;

    @InjectMocks
    private SearchViewModel viewModel;

    @Test
    public void testGetTracks() {
        List<Track> tracks = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            tracks.add(new Track(i,
                    getStringFormat(START_DATE, i),
                    getStringFormat(END_DATE, i),
                    i,
                    getStringFormat(TITLE, i),
                    getListOfPeople()));
        }

        Observer observer = mock(Observer.class);

        when(dataSource.getTracks()).thenReturn(Observable.just(tracks));

        viewModel.getTracks().observeForever(observer);

        sleep(500);

        verify(observer).onChanged(tracks);

    }

    @Test
    public void testGetTopics() {
        List<Topic> topics = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            topics.add(new Topic(i,
                    PARENT_ID,
                    getStringFormat(TITLE, i),
                    getListOfPeople(),
                    i));
        }

        Observer observer = mock(Observer.class);

        when(dataSource.getTopics()).thenReturn(Observable.just(topics));

        viewModel.getTopics().observeForever(observer);

        sleep(500);

        verify(observer).onChanged(topics);

    }
}