package com.example.karlo.sstconference.ui;

import android.arch.lifecycle.MutableLiveData;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiSelector;

import com.example.karlo.sstconference.BaseTest;
import com.example.karlo.sstconference.R;
import com.example.karlo.sstconference.commons.Status;
import com.example.karlo.sstconference.models.venue.MarkersGroup;
import com.example.karlo.sstconference.models.venue.Venue;
import com.example.karlo.sstconference.modules.venue.VenueActivity;
import com.example.karlo.sstconference.modules.venue.VenueViewModel;

import net.globulus.easyprefs.EasyPrefs;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.inject.Inject;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertEquals;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.when;

public class VenueActivityTest extends BaseTest {

    @Inject
    VenueViewModel viewModel;

    @Rule
    public final ActivityTestRule<VenueActivity> mRule = new ActivityTestRule<>(VenueActivity.class, false, false);

    @Rule public GrantPermissionRule permissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);

    private MutableLiveData<Venue> venue = new MutableLiveData<>();
    private MutableLiveData<MarkersGroup> markers = new MutableLiveData<>();
    private MutableLiveData<Status> status = new MutableLiveData<>();

    @Before
    public void init() {
        getApp().getComponent().inject(this);
        when(viewModel.getVenueDetails()).thenReturn(venue);
        when(viewModel.getStatus()).thenReturn(status);
        when(viewModel.getMarkerGroup()).thenReturn(markers);
        mRule.launchActivity(null);
        clearDatabaseAndPrefs();
    }

    @After
    public void clearData() {
        clearDatabaseAndPrefs();
    }

    @Test
    public void testMapMarkerShowing() {
        venue.postValue(getVenue());

        onView(allOf(withText(getString(R.string.show_map)), isDescendantOfA(withId(R.id.fragment_0)))).check(matches(isDisplayed()));
        onView(allOf(withText(getString(R.string.show_map)), isDescendantOfA(withId(R.id.fragment_0)))).perform(click());
        onView(allOf(withText(getString(R.string.hide_map)), isDescendantOfA(withId(R.id.fragment_0)))).check(matches(isDisplayed()));

        UiDevice device = UiDevice.getInstance(getInstrumentation());
        UiObject marker = device.findObject(new UiSelector().descriptionContains(getStringFormat(TITLE, 0)));
        try {
            marker.click();
        } catch (Exception e) {
            /*If emulator has location enabled it will zoom to his location, hence marker will not be seen*/
        }
    }

    @Test
    public void testConferenceSectionDisplayed() {
        venue.postValue(getVenue());

        onView(allOf(withText(TITLE), isDescendantOfA(withId(R.id.fragment_0)))).perform(click());
        onView(allOf(withId(R.id.text_view), isDescendantOfA(withId(R.id.fragment_0))))
                .check(matches(withText(HOTEL)));
    }

    @Test
    public void testRegionSectionDisplayed() {
        venue.postValue(getVenue());

        onView(withId(R.id.fragment_0)).perform(swipeLeft());

        onView(allOf(withText(TITLE), isDescendantOfA(withId(R.id.fragment_1)))).perform(click());
        onView(allOf(withId(R.id.text_view), isDescendantOfA(withId(R.id.fragment_1))))
                .check(matches(withText(REGION)));
    }

    @Test
    public void testFacultySectionDisplayed() {
        venue.postValue(getVenue());

        onView(withId(R.id.fragment_0)).perform(swipeLeft());
        sleep(300);
        onView(withId(R.id.fragment_1)).perform(swipeLeft());
        sleep(300);
        onView(withId(R.id.fragment_2)).perform(swipeLeft());
        sleep(300);
        onView(withId(R.id.fragment_3)).perform(swipeLeft());

        onView(allOf(withText(TITLE), isDescendantOfA(withId(R.id.fragment_4)))).perform(click());
        onView(allOf(withId(R.id.text_view), isDescendantOfA(withId(R.id.fragment_4))))
                .check(matches(withText(FACULTY)));
    }

    @Test
    public void testMarkersShowing() {
        MarkersGroup markersGroup = new MarkersGroup();
        markersGroup.setRestaurants(getMarkersOptions(4));
        markersGroup.setBars(getMarkersOptions(2));
        markersGroup.setCafe(getMarkersOptions(3));
        markersGroup.setZoo(getMarkersOptions(1));
        markersGroup.setChurch(getMarkersOptions(5));
        markersGroup.setLibrary(getMarkersOptions(4));
        markersGroup.setMuseum(getMarkersOptions(3));

        markers.postValue(markersGroup);

        onView(withId(R.id.fragment_0)).perform(swipeLeft());
        sleep(300);
        onView(withId(R.id.fragment_1)).perform(swipeLeft());
        sleep(300);

        onView(allOf(withText(getString(R.string.show_map)), isDescendantOfA(withId(R.id.fragment_2)))).perform(click());
        onView(allOf(withText(getString(R.string.hide_map)), isDescendantOfA(withId(R.id.fragment_2)))).check(matches(isDisplayed()));

        UiDevice device = UiDevice.getInstance(getInstrumentation());
        UiObject marker = device.findObject(new UiSelector().descriptionContains(getStringFormat(TITLE, 0)));
        try {
            marker.click();
        } catch (Exception e) {
            /*If emulator has location enabled it will zoom to his location, hence marker will not be seen*/
        }

        pressBack();
    }

    @Test
    public void testFoodSwitches() {
        venue.postValue(getVenue());

        onView(withId(R.id.fragment_0)).perform(swipeLeft());
        sleep(300);
        onView(withId(R.id.fragment_1)).perform(swipeLeft());
        sleep(300);

        onView(allOf(withText(getString(R.string.type_of_places)), isDescendantOfA(withId(R.id.fragment_2)))).perform(click());

        onView(withText(getString(R.string.restaurants))).perform(click());
        assertEquals(EasyPrefs.getShowRestaurants(mRule.getActivity()), false);
        onView(withText(getString(R.string.cafe))).perform(click());
        assertEquals(EasyPrefs.getShowCafe(mRule.getActivity()), true);
        onView(withText(getString(R.string.bars))).perform(click());
        assertEquals(EasyPrefs.getShowBars(mRule.getActivity()), true);

        onView(allOf(withText(getString(R.string.type_of_places)), isDescendantOfA(withId(R.id.fragment_2)))).perform(click());
    }

    @Test
    public void testSightsSwitches() {
        venue.postValue(getVenue());

        onView(withId(R.id.fragment_0)).perform(swipeLeft());
        sleep(300);
        onView(withId(R.id.fragment_1)).perform(swipeLeft());
        sleep(300);
        onView(withId(R.id.fragment_2)).perform(swipeLeft());
        sleep(300);

        onView(allOf(withText(getString(R.string.type_of_places)), isDescendantOfA(withId(R.id.fragment_3)))).perform(click());

        onView(withText(getString(R.string.museum))).perform(click());
        assertEquals(EasyPrefs.getShowMuseums(mRule.getActivity()), false);
        onView(withText(getString(R.string.library))).perform(click());
        assertEquals(EasyPrefs.getShowLibrary(mRule.getActivity()), true);
        onView(withText(getString(R.string.church))).perform(click());
        assertEquals(EasyPrefs.getShowChurch(mRule.getActivity()), true);
        onView(withText(getString(R.string.zoo))).perform(click());
        assertEquals(EasyPrefs.getShowZoo(mRule.getActivity()), true);
    }

    @Test
    public void testErrorMessage() {
        status.postValue(Status.error(TEST_MESSAGE));

        sleep(500);

        onView(withText(TEST_MESSAGE)).inRoot(new ToastMatcher())
                .check(matches(isDisplayed()));
    }

    @Test
    public void testProgressBar() {
        status.postValue(Status.loading(true));
        sleep(500);
        onView(Matchers.allOf(withId(R.id.progress_bar), isDescendantOfA(withId(R.id.venue_container))))
                .check(matches(isDisplayed()));

        status.postValue(Status.loading(false));
        sleep(500);
        onView(Matchers.allOf(withId(R.id.progress_bar), isDescendantOfA(withId(R.id.venue_container))))
                .check(matches(not(isDisplayed())));
    }
}