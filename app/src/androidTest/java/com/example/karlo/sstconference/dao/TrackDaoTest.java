package com.example.karlo.sstconference.dao;

import com.example.karlo.sstconference.database.track.TrackDao;
import com.example.karlo.sstconference.models.program.Person;
import com.example.karlo.sstconference.models.program.Track;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;

public class TrackDaoTest extends BaseDaoTest {

    private static String TITLE = "Title";
    private static String START_DATE = "12.12.2012";
    private static String END_DATE = "15.12.2012";
    private static int ROOM = 0;

    private static String NAME = "Some Name";

    private TrackDao mDao;

    @Before
    public void setDao() {
        mDao = localDatabase.trackModel();
    }

    @Test
    public void insertAndGetOne() {
        mDao.insertTrack(getTrack());

        mDao.getTracks()
                .toObservable()
                .flatMap(io.reactivex.Observable::fromIterable)
                .firstElement()
                .subscribe(track -> {
                    assertEquals(track.getTitle(), TITLE);
                    assertEquals(track.getStartDate(), START_DATE);
                    assertEquals(track.getEndDate(), END_DATE);
                    assertEquals(track.getRoom(), ROOM);
                });
    }

    @Test
    public void insertAndGetMany() {
        List<Track> trackList = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            Track track = new Track(i,
                    getStringFormat(START_DATE, i),
                    getStringFormat(END_DATE, i),
                    i,
                    getStringFormat(TITLE, i),
                    getListOfChairs());

            trackList.add(track);
            mDao.insertTrack(track);
        }

        mDao.getTracks()
                .subscribe(tracks -> {
                    for (int i = 0; i < tracks.size(); i++) {
                        assertEquals(tracks.get(i).getStartDate(), trackList.get(i).getStartDate());
                        assertEquals(tracks.get(i).getEndDate(), trackList.get(i).getEndDate());
                        assertEquals(tracks.get(i).getTitle(), trackList.get(i).getTitle());
                        assertEquals(tracks.get(i).getRoom(), trackList.get(i).getRoom());
                        assertEquals(tracks.get(i).getChairs()
                                        .get(5).getName(),
                                trackList.get(i).getChairs()
                                        .get(5).getName());
                    }
                });
    }

    @Test
    public void deleteItem() {
        Track track = getTrack();
        mDao.insertTrack(track);

        mDao.getTracks()
                .toObservable()
                .flatMap(io.reactivex.Observable::fromIterable)
                .firstElement()
                .subscribe(responseTrack -> {
                    assertEquals(responseTrack.getTitle(), TITLE);
                    assertEquals(responseTrack.getStartDate(), START_DATE);
                    assertEquals(responseTrack.getEndDate(), END_DATE);
                    assertEquals(responseTrack.getRoom(), ROOM);
                });

        mDao.deleteTrack(track);

        mDao.getTracks()
                .toObservable()
                .flatMap(io.reactivex.Observable::fromIterable)
                .toList()
                .subscribe(tracks -> assertEquals(tracks.isEmpty(), true));
    }

    private Track getTrack() {
        return new Track(0, START_DATE, END_DATE, ROOM, TITLE, getListOfChairs());
    }

    private List<Person> getListOfChairs() {
        List<Person> people = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            people.add(new Person(getStringFormat(NAME, i)));
        }
        return people;
    }
}
