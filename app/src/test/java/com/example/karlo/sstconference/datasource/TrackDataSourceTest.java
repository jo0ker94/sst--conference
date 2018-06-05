package com.example.karlo.sstconference.datasource;

import com.example.karlo.sstconference.database.track.LocalTrackDataSource;
import com.example.karlo.sstconference.database.track.TrackDao;
import com.example.karlo.sstconference.models.program.Track;
import com.example.karlo.sstconference.servertasks.interfaces.ProgramApi;

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

public class TrackDataSourceTest extends BaseDataSourceTest {

    @Mock
    private TrackDao dao;

    @Mock
    private ProgramApi api;

    @InjectMocks
    private LocalTrackDataSource dataSource;

    @Test
    public void testGetSaveAndDelete() {
        List<Track> tracks = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            tracks.add(new Track(i,
                    getStringFormat(START_DATE, i),
                    getStringFormat(END_DATE, i),
                    i,
                    getStringFormat(TITLE, i),
                    getListOfPeople()));
        }

        List<Track> apiTracks = new ArrayList<>(tracks);
        Track track = getTrack(123);
        apiTracks.add(track);

        when(dao.getTracks()).thenReturn(Maybe.just(tracks));
        when(api.getTracks()).thenReturn(Observable.just(apiTracks));

        dataSource.insertOrUpdateTrack(track);
        verify(dao).insertTrack(track);

        dataSource.deleteTrack(tracks.get(0));
        verify(dao).deleteTrack(tracks.get(0));

        dataSource.getTracks();
        verify(dao).getTracks();
        verify(api).getTracks();

        dataSource.getTracks()
                .flatMap(Observable::fromIterable)
                .distinct(Track::getId)
                .toList()
                .subscribe(trackList -> {
                    for (int i = 0; i < trackList.size(); i++) {
                        assertEquals(trackList.get(i), apiTracks.get(i));
                    }
                });
    }
}