package com.example.karlo.sstconference.viewmodel;

import android.arch.lifecycle.Observer;

import com.example.karlo.sstconference.database.venue.VenueDataSource;
import com.example.karlo.sstconference.models.venue.Venue;
import com.example.karlo.sstconference.modules.venue.VenueViewModel;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class VenueViewModelTest  extends BaseViewModelTest {

    @Mock
    private VenueDataSource dataSource;

    @InjectMocks
    private VenueViewModel viewModel;

    @Test
    public void testGetVenue() {
        Venue venue = getVenue();

        Observer observer = mock(Observer.class);

        when(dataSource.getVenue()).thenReturn(io.reactivex.Observable.just(venue));

        viewModel.getVenueDetails().observeForever(observer);

        sleep(500);

        verify(observer).onChanged(venue);
    }

    @Test
    public void testGetMarkerGroup() {

    }
}
