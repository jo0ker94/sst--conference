package com.example.karlo.sstconference.dao;

import com.example.karlo.sstconference.database.keynote.KeynoteDao;
import com.example.karlo.sstconference.models.keynote.KeynoteSpeaker;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;

public class KeynoteDaoTest extends BaseDaoTest {

    private KeynoteDao mDao;

    @Before
    public void setDao() {
        mDao = localDatabase.keynoteModel();
    }

    @Test
    public void insertAndGetOne() {
        mDao.insertKeynoteSpeaker(getKeynoteSpeaker());

        mDao.getKeynoteSpeakers()
                .toObservable()
                .flatMap(io.reactivex.Observable::fromIterable)
                .firstElement()
                .subscribe(speaker -> {
                    assertEquals(speaker.getName(), NAME);
                    assertEquals(speaker.getTitle(), TITLE);
                    assertEquals(speaker.getEmail(), MAIL);
                    assertEquals(speaker.getFacility(), FACILITY);
                    assertEquals(speaker.getAbstractText(), ABSTRACT);
                });
    }

    @Test
    public void insertAndGetMany() {
        List<KeynoteSpeaker> keynoteSpeakers = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            KeynoteSpeaker speaker = new KeynoteSpeaker(i,
                    getStringFormat(NAME, i),
                    getStringFormat(FACILITY, i),
                    getStringFormat(MAIL, i),
                    getStringFormat(IMAGE, i),
                    getStringFormat(TITLE, i),
                    getStringFormat(ABSTRACT, i));

            keynoteSpeakers.add(speaker);
            mDao.insertKeynoteSpeaker(speaker);
        }

        mDao.getKeynoteSpeakers()
                .subscribe(speakers -> {
                    for (int i = 0; i < speakers.size(); i++) {
                        assertEquals(speakers.get(i).getName(), keynoteSpeakers.get(i).getName());
                        assertEquals(speakers.get(i).getTitle(), keynoteSpeakers.get(i).getTitle());
                        assertEquals(speakers.get(i).getEmail(), keynoteSpeakers.get(i).getEmail());
                        assertEquals(speakers.get(i).getFacility(), keynoteSpeakers.get(i).getFacility());
                        assertEquals(speakers.get(i).getAbstractText(), keynoteSpeakers.get(i).getAbstractText());
                    }
                });
    }

    @Test
    public void deleteItem() {
        KeynoteSpeaker keynoteSpeaker = getKeynoteSpeaker();
        mDao.insertKeynoteSpeaker(keynoteSpeaker);

        mDao.getKeynoteSpeakers()
                .toObservable()
                .flatMap(io.reactivex.Observable::fromIterable)
                .firstElement()
                .subscribe(speaker -> {
                    assertEquals(speaker.getName(), NAME);
                    assertEquals(speaker.getTitle(), TITLE);
                    assertEquals(speaker.getEmail(), MAIL);
                    assertEquals(speaker.getFacility(), FACILITY);
                    assertEquals(speaker.getAbstractText(), ABSTRACT);
                });

        mDao.deleteKeynoteSpeaker(keynoteSpeaker);

        mDao.getKeynoteSpeakers()
                .toObservable()
                .flatMap(io.reactivex.Observable::fromIterable)
                .toList()
                .subscribe(speakers -> assertEquals(speakers.isEmpty(), true));
    }
}
