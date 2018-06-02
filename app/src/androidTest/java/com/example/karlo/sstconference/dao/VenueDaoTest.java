package com.example.karlo.sstconference.dao;

import com.example.karlo.sstconference.database.venue.VenueDao;
import com.example.karlo.sstconference.models.venue.Info;
import com.example.karlo.sstconference.models.venue.Venue;
import com.example.karlo.sstconference.models.venue.VenueMarker;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.MaybeObserver;
import io.reactivex.disposables.Disposable;

import static junit.framework.Assert.assertEquals;

public class VenueDaoTest extends BaseDaoTest {

    private static double LAT = 124.123;
    private static double LNG = 21.532;
    private static String SNIPPET = "Snippet";
    private static String TITLE = "Title";
    private static String LINK = "link";
    private static String IMAGE = "http://www.picture.com";
    private static String FACULTY = "faculty";
    private static String HOTEL = "hotel";
    private static String REGION = "region";

    private VenueDao mDao;

    @Before
    public void setDao() {
        mDao = localDatabase.venueModel();
    }

    @Test
    public void insertAndGetOne() {
        mDao.insertVenue(getVenue());

        mDao.getVenue()
                .toObservable()
                .subscribe(venue -> {
                    assertEquals(venue.getFaculty().get(0).getDescription(), FACULTY);
                    assertEquals(venue.getHotel().get(0).getDescription(), HOTEL);
                    assertEquals(venue.getRegion().get(0).getDescription(), REGION);
                });
    }


    @Test
    public void deleteItem() {
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

    private Venue getVenue() {
        return new Venue(0, getInfoList(FACULTY), getInfoList(REGION), getInfoList(HOTEL));
    }

    private List<Info> getInfoList(String type) {
        List<Info> infos = new ArrayList<>();
        infos.add(getInfo(0, type));
        infos.add(getInfo(1, type));
        infos.add(getInfo(2, type));
        return infos;
    }

    private Info getInfo(int id, String description) {
        return new Info(id, description, IMAGE, LINK, TITLE, getMarker());
    }

    private VenueMarker getMarker() {
        return new VenueMarker(LAT, LNG, TITLE, SNIPPET);
    }


}
