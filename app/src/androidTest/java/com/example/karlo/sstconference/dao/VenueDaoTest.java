package com.example.karlo.sstconference.dao;

import com.example.karlo.sstconference.database.venue.VenueDao;
import com.example.karlo.sstconference.models.venue.Venue;

import org.junit.Before;
import org.junit.Test;

import io.reactivex.MaybeObserver;
import io.reactivex.disposables.Disposable;

import static junit.framework.Assert.assertEquals;

public class VenueDaoTest extends BaseDaoTest {

    private VenueDao mDao;

    @Before
    public void setDao() {
        mDao = localDatabase.venueModel();
    }

    @Test
    public void testInsertAndGetOne() {
        mDao.insertVenue(getVenue());

        mDao.getVenue()
                .toObservable()
                .subscribe(venue -> {
                    assertEquals(venue.getFaculty().get(0).getDescription(), FACULTY);
                    assertEquals(venue.getHotel().get(0).getDescription(), HOTEL);
                    assertEquals(venue.getRegion().get(0).getDescription(), REGION);
                });
    }

    public void testInsertAndGetMany() {

    }

    @Test
    public void testDeleteItem() {
        Venue venue = getVenue();
        mDao.insertVenue(venue);

        mDao.getVenue()
                .toObservable()
                .subscribe(responseVenue -> {
                            assertEquals(venue.getFaculty().get(2).getDescription(), FACULTY);
                            assertEquals(venue.getHotel().get(1).getDescription(), HOTEL);
                            assertEquals(venue.getRegion().get(1).getDescription(), REGION);
                        });

        mDao.deleteVenue(venue);

        final boolean[] success = {false};
        mDao.getVenue()
                .subscribe(new MaybeObserver<Venue>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onSuccess(Venue venue1) {
                        success[0] = true;
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        assertEquals(success[0], false);
                    }
                });
    }
}
