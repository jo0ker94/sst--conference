package com.example.karlo.sstconference.ui;

import android.arch.lifecycle.MutableLiveData;
import android.support.test.rule.ActivityTestRule;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;

import com.example.karlo.sstconference.BaseTest;
import com.example.karlo.sstconference.R;
import com.example.karlo.sstconference.commons.Status;
import com.example.karlo.sstconference.models.venue.MarkersGroup;
import com.example.karlo.sstconference.models.venue.Venue;
import com.example.karlo.sstconference.modules.venue.VenueActivity;
import com.example.karlo.sstconference.modules.venue.VenueViewModel;

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
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.when;

public class VenueActivityTest extends BaseTest {

    @Inject
    VenueViewModel viewModel;

    @Rule
    public final ActivityTestRule<VenueActivity> mRule = new ActivityTestRule<>(VenueActivity.class, false, false);

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
    public void testMapMarkerShowing() throws UiObjectNotFoundException {
        venue.postValue(getVenue());

        onView(allOf(withText(getString(R.string.show_map)), isDescendantOfA(withId(R.id.fragment_0)))).check(matches(isDisplayed()));
        onView(allOf(withText(getString(R.string.show_map)), isDescendantOfA(withId(R.id.fragment_0)))).perform(click());
        onView(allOf(withText(getString(R.string.hide_map)), isDescendantOfA(withId(R.id.fragment_0)))).check(matches(isDisplayed()));

        UiDevice device = UiDevice.getInstance(getInstrumentation());
        UiObject marker = device.findObject(new UiSelector().descriptionContains(TITLE));
        marker.click();
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