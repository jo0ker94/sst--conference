package com.example.karlo.sstconference.dao;

import android.support.test.runner.AndroidJUnit4;

import com.example.karlo.sstconference.database.chairs.ChairsDao;
import com.example.karlo.sstconference.models.ConferenceChair;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class ChairsDaoTest extends BaseDaoTest {

    private static String FORMAT = "%s_%d";

    private static String NAME = "Some Name";
    private static String TITLE = "Title";
    private static String MAIL = "mail@email.com";
    private static String FACILITY = "Some Facility";
    private static String IMAGE = "http://www.picture.com";
    private static String NUMBER = "0123456789";

    private ChairsDao mDao;

    @Before
    public void setDao() {
        mDao = localDatabase.chairsModel();
    }

    @Test
    public void insertAndGetOne() {
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
    public void insertAndGetMany() {
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
    public void deleteItem() {
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

    private ConferenceChair getConferenceChair() {
        return new ConferenceChair(0, TITLE, MAIL, FACILITY, IMAGE, NAME, NUMBER);
    }

    private String getStringFormat(String field, int position) {
        return String.format(FORMAT, field, position);
    }

}
