package com.example.karlo.sstconference.viewmodel;

import android.arch.lifecycle.Observer;

import com.example.karlo.sstconference.commons.Status;
import com.example.karlo.sstconference.database.venue.VenueDataSource;
import com.example.karlo.sstconference.models.enums.PlaceType;
import com.example.karlo.sstconference.models.nearbyplaces.Geometry;
import com.example.karlo.sstconference.models.nearbyplaces.LocationCoordinates;
import com.example.karlo.sstconference.models.nearbyplaces.NearbyPlaces;
import com.example.karlo.sstconference.models.nearbyplaces.Result;
import com.example.karlo.sstconference.models.venue.Venue;
import com.example.karlo.sstconference.modules.venue.VenueViewModel;
import com.example.karlo.sstconference.modules.venue.fragments.VenueFragment;
import com.example.karlo.sstconference.servertasks.interfaces.MapsApi;
import com.google.android.gms.maps.model.LatLng;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class VenueViewModelTest  extends BaseViewModelTest {

    @Mock
    private VenueDataSource dataSource;

    @Mock
    private MapsApi api;

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
        Observer observer = mock(Observer.class);

        when(api.getNearbyPlaces(any(String.class), any(Long.class), eq(PlaceType.BAR.toString()), any(String.class))).thenReturn(io.reactivex.Observable.just(getNearbyPlaces(PlaceType.BAR.toString())));
        when(api.getNearbyPlaces(any(String.class), any(Long.class), eq(PlaceType.RESTAURANT.toString()), any(String.class))).thenReturn(io.reactivex.Observable.just(getNearbyPlaces(PlaceType.RESTAURANT.toString())));
        when(api.getNearbyPlaces(any(String.class), any(Long.class), eq(PlaceType.CAFE.toString()), any(String.class))).thenReturn(io.reactivex.Observable.just(getNearbyPlaces(PlaceType.CAFE.toString())));
        when(api.getNearbyPlaces(any(String.class), any(Long.class), eq(PlaceType.MUSEUM.toString()), any(String.class))).thenReturn(io.reactivex.Observable.just(getNearbyPlaces(PlaceType.MUSEUM.toString())));
        when(api.getNearbyPlaces(any(String.class), any(Long.class), eq(PlaceType.CHURCH.toString()), any(String.class))).thenReturn(io.reactivex.Observable.just(getNearbyPlaces(PlaceType.CHURCH.toString())));
        when(api.getNearbyPlaces(any(String.class), any(Long.class), eq(PlaceType.LIBRARY.toString()), any(String.class))).thenReturn(io.reactivex.Observable.just(getNearbyPlaces(PlaceType.LIBRARY.toString())));
        when(api.getNearbyPlaces(any(String.class), any(Long.class), eq(PlaceType.ZOO.toString()), any(String.class))).thenReturn(io.reactivex.Observable.just(getNearbyPlaces(PlaceType.ZOO.toString())));

        viewModel.getMarkerGroup().observeForever(observer);
        LatLng latLng = new LatLng(LAT, LNG);
        viewModel.fetchAllPlaces(VenueFragment.VenueType.FOOD, latLng);
        viewModel.fetchAllPlaces(VenueFragment.VenueType.SIGHTS, latLng);

        sleep(2000);

        assertEquals(viewModel.getMarkerGroup().getValue().getBars().get(0).getTitle(), PlaceType.BAR.toString());
        assertEquals(viewModel.getMarkerGroup().getValue().getRestaurants().get(0).getTitle(), PlaceType.RESTAURANT.toString());
        assertEquals(viewModel.getMarkerGroup().getValue().getCafe().get(0).getTitle(), PlaceType.CAFE.toString());
        assertEquals(viewModel.getMarkerGroup().getValue().getMuseum().get(0).getTitle(), PlaceType.MUSEUM.toString());
        assertEquals(viewModel.getMarkerGroup().getValue().getChurch().get(0).getTitle(), PlaceType.CHURCH.toString());
        assertEquals(viewModel.getMarkerGroup().getValue().getLibrary().get(0).getTitle(), PlaceType.LIBRARY.toString());
        assertEquals(viewModel.getMarkerGroup().getValue().getZoo().get(0).getTitle(), PlaceType.ZOO.toString());

    }
}
