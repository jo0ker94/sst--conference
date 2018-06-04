package com.example.karlo.sstconference.dao;

import com.example.karlo.sstconference.database.chairs.ChairsDao;
import com.example.karlo.sstconference.models.ConferenceChair;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;

public class ChairsDaoTest extends BaseDaoTest {

    private ChairsDao mDao;

    @Before
    public void setDao() {
        mDao = localDatabase.chairsModel();
    }

    @Test
    public void testInsertAndGetOne() {
        mDao.insertConferenceChair(getConferenceChair());

        mDao.getConferenceChairs()
                .toObservable()
                .flatMap(io.reactivex.Observable::fromIterable)
                .firstElement()
                .subscribe(chair -> {
                    assertEquals(chair.getName(), NAME);
                    assertEquals(chair.getChairTitle(), TITLE);
                    assertEquals(chair.getEmail(), MAIL);
                    assertEquals(chair.getFacility(), FACILITY);
                    assertEquals(chair.getPhoneNumber(), NUMBER);
                });
    }

    @Test
    public void testInsertAndGetMany() {
        List<ConferenceChair> chairList = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            ConferenceChair chair = new ConferenceChair(i,
                    getStringFormat(TITLE, i),
                    getStringFormat(MAIL, i),
                    getStringFormat(FACILITY, i),
                    getStringFormat(IMAGE, i),
                    getStringFormat(NAME, i),
                    getStringFormat(NUMBER, i));

            chairList.add(chair);
            mDao.insertConferenceChair(chair);
        }

        mDao.getConferenceChairs()
                .subscribe(chairs -> {
                    for (int i = 0; i < chairs.size(); i++) {
                        assertEquals(chairs.get(i).getName(), chairList.get(i).getName());
                        assertEquals(chairs.get(i).getChairTitle(), chairList.get(i).getChairTitle());
                        assertEquals(chairs.get(i).getEmail(), chairList.get(i).getEmail());
                        assertEquals(chairs.get(i).getFacility(), chairList.get(i).getFacility());
                        assertEquals(chairs.get(i).getPhoneNumber(), chairList.get(i).getPhoneNumber());
                    }
                });
    }

    @Test
    public void testDeleteItem() {
        ConferenceChair conferenceChair = getConferenceChair();
        mDao.insertConferenceChair(conferenceChair);

        mDao.getConferenceChairs()
                .toObservable()
                .flatMap(io.reactivex.Observable::fromIterable)
                .firstElement()
                .subscribe(chair -> {
                    assertEquals(chair.getName(), NAME);
                    assertEquals(chair.getChairTitle(), TITLE);
                    assertEquals(chair.getEmail(), MAIL);
                    assertEquals(chair.getFacility(), FACILITY);
                    assertEquals(chair.getPhoneNumber(), NUMBER);
                });

        mDao.deleteConferenceChair(conferenceChair);

        mDao.getConferenceChairs()
                .toObservable()
                .flatMap(io.reactivex.Observable::fromIterable)
                .toList()
                .subscribe(chairs -> assertEquals(chairs.isEmpty(), true));
    }

    @Test
    public void testUpdateItem() {
        ConferenceChair conferenceChair = getConferenceChair();
        mDao.insertConferenceChair(conferenceChair);

        conferenceChair.setName(DISPLAY_NAME);
        mDao.insertConferenceChair(conferenceChair);

        mDao.getConferenceChairs()
                .toObservable()
                .flatMap(io.reactivex.Observable::fromIterable)
                .firstElement()
                .subscribe(chair -> {
                    assertEquals(chair.getId(), 0);
                    assertEquals(chair.getName(), DISPLAY_NAME);
                    assertEquals(chair.getChairTitle(), TITLE);
                    assertEquals(chair.getEmail(), MAIL);
                    assertEquals(chair.getFacility(), FACILITY);
                    assertEquals(chair.getPhoneNumber(), NUMBER);
                });
    }
}
