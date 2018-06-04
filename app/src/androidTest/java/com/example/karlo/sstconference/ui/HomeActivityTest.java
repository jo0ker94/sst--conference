package com.example.karlo.sstconference.ui;

import android.arch.lifecycle.MutableLiveData;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.rule.ActivityTestRule;
import android.view.Gravity;

import com.example.karlo.sstconference.BaseTest;
import com.example.karlo.sstconference.R;
import com.example.karlo.sstconference.commons.Status;
import com.example.karlo.sstconference.models.User;
import com.example.karlo.sstconference.modules.home.HomeActivity;
import com.example.karlo.sstconference.modules.home.HomeViewModel;
import com.example.karlo.sstconference.modules.login.LoginViewModel;

import net.globulus.easyprefs.EasyPrefs;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.inject.Inject;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.swipeUp;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerMatchers.isClosed;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
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
    public void testErrorMessage() {
        userData.postValue(getUser());
        status.postValue(Status.error(TEST_MESSAGE));

        sleep(500);

        onView(withText(TEST_MESSAGE)).inRoot(new ToastMatcher())
                .check(matches(isDisplayed()));
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
}
