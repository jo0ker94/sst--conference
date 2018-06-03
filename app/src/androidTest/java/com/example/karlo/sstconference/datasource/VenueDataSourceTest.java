package com.example.karlo.sstconference.datasource;

import com.example.karlo.sstconference.database.venue.LocalVenueDataSource;
import com.example.karlo.sstconference.database.venue.VenueDao;
import com.example.karlo.sstconference.models.venue.Venue;
import com.example.karlo.sstconference.servertasks.interfaces.VenueApi;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import io.reactivex.Maybe;
import io.reactivex.Observable;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class VenueDataSourceTest extends BaseDataSourceTest {

    @Mock
    private VenueDao dao;

    @Mock
    private VenueApi api;

    @InjectMocks
    private LocalVenueDataSource dataSource;

    @Test
    public void testGetSaveAndDelete() {
        Venue venue = getVenue();
        Venue apiVenue = getVenue();
        apiVenue.getRegion().get(0)
                .setDescription("New description");

        when(dao.getVenue()).thenReturn(Maybe.just(venue));
        when(api.getVenue()).thenReturn(Observable.just(apiVenue));

        dataSource.insertOrUpdateVenue(apiVenue);
        verify(dao).insertVenue(apiVenue);

        dataSource.deleteVenue(venue);
        verify(dao).deleteVenue(venue);

        dataSource.getVenue();
        verify(dao).getVenue();
        verify(api).getVenue();

        dataSource.getVenue()
                .subscribe(responseVenue -> {
                    if (responseVenue.getRegion().get(0).getDescription().equals(REGION)) {
                        assertEquals(responseVenue.getRegion().get(0).getDescription(),
                                venue.getRegion().get(0).getDescription());
                    } else {
                        assertEquals(responseVenue.getRegion().get(0).getDescription(),
                                apiVenue.getRegion().get(0).getDescription());
                    }
                });
    }
}