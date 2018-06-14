package com.example.karlo.sstconference.ui;

import android.arch.lifecycle.MutableLiveData;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.uiautomator.UiDevice;
import android.view.Gravity;

import com.example.karlo.sstconference.BaseTest;
import com.example.karlo.sstconference.R;
import com.example.karlo.sstconference.commons.Status;
import com.example.karlo.sstconference.models.User;
import com.example.karlo.sstconference.modules.chairs.ChairsViewModel;
import com.example.karlo.sstconference.modules.committee.CommitteeViewModel;
import com.example.karlo.sstconference.modules.home.HomeActivity;
import com.example.karlo.sstconference.modules.home.HomeViewModel;
import com.example.karlo.sstconference.modules.keynotespeakers.KeynoteViewModel;
import com.example.karlo.sstconference.modules.login.LoginViewModel;
import com.example.karlo.sstconference.modules.program.ProgramViewModel;
import com.example.karlo.sstconference.modules.search.SearchViewModel;
import com.example.karlo.sstconference.modules.subscribed.SubscriptionViewModel;
import com.example.karlo.sstconference.modules.venue.VenueViewModel;
import com.example.karlo.sstconference.utility.AppConfig;

import net.globulus.easyprefs.EasyPrefs;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.inject.Inject;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeUp;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerMatchers.isClosed;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withHint;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.when;

public class HomeActivityTest extends BaseTest {

    @Inject
    LoginViewModel loginViewModel;

    @Inject
    HomeViewModel homeViewModel;

    @Inject
    SearchViewModel searchViewModel;

    @Inject
    SubscriptionViewModel subscriptionViewModel;

    @Inject
    ProgramViewModel programViewModel;

    @Inject
    CommitteeViewModel committeeViewModel;

    @Inject
    ChairsViewModel chairsViewModel;

    @Inject
    KeynoteViewModel keynoteViewModel;

    @Inject
    VenueViewModel venueViewModel;


    @Rule
    public final ActivityTestRule<HomeActivity> mRule = new ActivityTestRule<>(HomeActivity.class, false, false);

    private MutableLiveData<User> loginUserData = new MutableLiveData<>();
    private MutableLiveData<User> userData = new MutableLiveData<>();
    private MutableLiveData<Status> status = new MutableLiveData<>();

    @Before
    public void init() {
        getApp().getComponent().inject(this);
        when(loginViewModel.getUser()).thenReturn(loginUserData);
        when(loginViewModel.getStatus()).thenReturn(status);
        when(homeViewModel.getUser()).thenReturn(userData);
        when(homeViewModel.getStatus()).thenReturn(status);
        when(searchViewModel.getStatus()).thenReturn(status);
        when(subscriptionViewModel.getStatus()).thenReturn(status);
        when(committeeViewModel.getStatus()).thenReturn(status);
        when(chairsViewModel.getStatus()).thenReturn(status);
        when(keynoteViewModel.getStatus()).thenReturn(status);
        when(venueViewModel.getStatus()).thenReturn(status);
        when(programViewModel.getStatus()).thenReturn(status);
        MutableLiveData empty = new MutableLiveData();
        when(programViewModel.getTopics()).thenReturn(empty);
        when(programViewModel.getTracks()).thenReturn(empty);
        when(programViewModel.getUser()).thenReturn(empty);
        when(programViewModel.getComments()).thenReturn(empty);
        when(programViewModel.getUsers()).thenReturn(empty);
        when(searchViewModel.getTopics()).thenReturn(empty);
        when(searchViewModel.getTracks()).thenReturn(empty);
        when(subscriptionViewModel.getSubscribedTopics()).thenReturn(empty);
        when(committeeViewModel.getSteering()).thenReturn(empty);
        when(committeeViewModel.getProgram()).thenReturn(empty);
        when(committeeViewModel.getOrganizing()).thenReturn(empty);
        when(chairsViewModel.getChairs()).thenReturn(empty);
        when(keynoteViewModel.getSpeakers()).thenReturn(empty);
        when(venueViewModel.getMarkerGroup()).thenReturn(empty);
        when(venueViewModel.getVenueDetails()).thenReturn(empty);

        EasyPrefs.putGuestMode(getApp(), false);
        mRule.launchActivity(null);
    }

    @After
    public void clearData() {
        clearDatabaseAndPrefs();
    }

    @Test
    public void testNavigationViewForLoggedUser() {
        openNavigationForUser(getUser());

        onView(withId(R.id.user_name)).check(matches(withText(DISPLAY_NAME)));
        onView(withId(R.id.user_email)).check(matches(withText(MAIL)));
    }

    @Test
    public void testNavigationViewForGuest() {
        openNavigationForUser(null);

        onView(withId(R.id.user_name)).check(matches(withText(GUEST)));
        onView(withId(R.id.user_email)).check(matches(withText(EMPTY)));
    }

    @Test
    public void testNavigationItemsLoggedUser() {
        openNavigationForUser(getUser());

        onView(withId(R.id.navigation_view))
                .perform(swipeUp());

        onView(withText(getString(R.string.logout))).check(matches(isDisplayed()));
    }

    @Test
    public void testNavigationItemsGuest() {
        openNavigationForUser(null);

        onView(withText(getString(R.string.login))).check(matches(isDisplayed()));
    }

    @Test
    public void testGuestMessage() {
        userData.postValue(null);
        sleep(500);

        onView(withId(R.id.subscribed_link)).perform(click());
        onView(withText(getString(R.string.only_for_logged_in))).check(matches(isDisplayed()));
    }

    @Test
    public void testErrorMessage() {
        userData.postValue(getUser());
        status.postValue(Status.error(TEST_MESSAGE));

        sleep(500);

        onView(withText(TEST_MESSAGE)).inRoot(new ToastMatcher())
                .check(matches(isDisplayed()));
    }

    @Test
    public void testExitMessage() {
        userData.postValue(null);
        sleep(500);

        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        device.pressBack();

        onView(withText(getString(R.string.sure_you_want_to_quit))).check(matches(isDisplayed()));
        onView(withText(getString(R.string.no))).perform(click());
    }

    @Test
    public void testGoToSearch() {
        openNavigationForUser(getUser());

        onView(withId(R.id.navigation_view))
                .perform(NavigationViewActions.navigateTo(R.id.search));

        sleep(1000);
        checkToolbarTitle(getString(R.string.search));
    }

    @Test
    public void testLogoutLink() {
        openNavigationForUser(getUser());
        onView(withId(R.id.navigation_view))
                .perform(swipeUp());
        onView(withId(R.id.navigation_view))
                .perform(NavigationViewActions.navigateTo(R.id.logout));

        status.postValue(Status.logout());
        loginUserData.postValue(null);

        sleep(1000);
        onView(allOf(withId(R.id.login_button), isDescendantOfA(withId(R.id.fragment_login)))).check(matches(isDisplayed()));
    }

    @Test
    public void testGoToCommittee() {
        openNavigationForUser(getUser());

        onView(withId(R.id.navigation_view))
                .perform(NavigationViewActions.navigateTo(R.id.committee));

        sleep(1000);
        checkToolbarTitle(getString(R.string.organizing_committee));
    }

    @Test
    public void testGoToGallery() {
        openNavigationForUser(getUser());

        onView(withId(R.id.navigation_view))
                .perform(NavigationViewActions.navigateTo(R.id.images));

        sleep(1000);
        checkToolbarTitle(getString(R.string.gallery));
    }

    @Test
    public void testGoToEvents() {
        openNavigationForUser(getUser());

        onView(withId(R.id.navigation_view))
                .perform(NavigationViewActions.navigateTo(R.id.events));

        sleep(1000);
        checkToolbarTitle(getString(R.string.conference_program));
    }

    @Test
    public void testGoToVenue() {
        openNavigationForUser(getUser());

        onView(withId(R.id.navigation_view))
                .perform(NavigationViewActions.navigateTo(R.id.venue));

        sleep(1000);
        checkToolbarTitle(getString(R.string.conference));
    }

    @Test
    public void testGoToSpeakers() {
        openNavigationForUser(getUser());

        onView(withId(R.id.navigation_view))
                .perform(NavigationViewActions.navigateTo(R.id.speakers));

        sleep(1000);
        checkToolbarTitle(getString(R.string.keynote_speakers));
    }

    @Test
    public void testGoToSubscribed() {
        openNavigationForUser(getUser());

        onView(withId(R.id.navigation_view))
                .perform(NavigationViewActions.navigateTo(R.id.subscribed_events));

        sleep(1000);
        checkToolbarTitle(getString(R.string.subscribed_events));
    }

    @Test
    public void testGoToChairs() {
        openNavigationForUser(getUser());

        onView(withId(R.id.navigation_view))
                .perform(NavigationViewActions.navigateTo(R.id.conference_chairs));

        sleep(1000);
        checkToolbarTitle(getString(R.string.conference_chairs));
    }

    @Test
    public void testGoToSettings() {
        openNavigationForUser(getUser());

        onView(withId(R.id.navigation_view))
                .perform(swipeUp());
        onView(withId(R.id.navigation_view))
                .perform(NavigationViewActions.navigateTo(R.id.settings));

        sleep(1000);
        checkToolbarTitle(getString(R.string.settings));
    }

    @Test
    public void testGoToAbout() {
        openNavigationForUser(getUser());

        onView(withId(R.id.navigation_view))
                .perform(NavigationViewActions.navigateTo(R.id.about));

        sleep(1000);
        checkToolbarTitle(getString(R.string.about));
    }

    @Test
    public void testProgressBar() {
        userData.postValue(getUser());

        status.postValue(Status.loading(true));
        sleep(500);
        onView(allOf(withId(R.id.progress_bar), isDescendantOfA(withId(R.id.drawer))))
                .check(matches(isDisplayed()));

        status.postValue(Status.loading(false));
        sleep(500);
        onView(allOf(withId(R.id.progress_bar), isDescendantOfA(withId(R.id.drawer))))
                .check(matches(not(isDisplayed())));
    }

    @Test
    public void testLogout() {
        userData.postValue(getUser());
        status.postValue(Status.logout());
        loginUserData.postValue(null);

        sleep(500);
        onView(allOf(withId(R.id.login_button), isDescendantOfA(withId(R.id.fragment_login))))
                .check(matches(isDisplayed()));
    }

    private void openNavigationForUser(User user) {
        userData.postValue(user);

        sleep(500);

        onView(withId(R.id.drawer))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());
    }

    private void checkToolbarTitle(String title) {
        onView(allOf(withText(title),
                childAtPosition(withId(R.id.toolbar),0),
                isDisplayed()))
                .check(matches(withText(title)));
    }
}
