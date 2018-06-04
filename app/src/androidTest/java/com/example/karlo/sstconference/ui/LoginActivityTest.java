package com.example.karlo.sstconference.ui;

import android.arch.lifecycle.MutableLiveData;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.view.Gravity;

import com.example.karlo.sstconference.BaseTest;
import com.example.karlo.sstconference.R;
import com.example.karlo.sstconference.commons.Status;
import com.example.karlo.sstconference.database.LocalDatabase;
import com.example.karlo.sstconference.database.user.UserDataSource;
import com.example.karlo.sstconference.models.User;
import com.example.karlo.sstconference.modules.home.HomeViewModel;
import com.example.karlo.sstconference.modules.login.LoginActivity;
import com.example.karlo.sstconference.modules.login.LoginViewModel;

import net.globulus.easyprefs.EasyPrefs;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.inject.Inject;

import io.reactivex.Maybe;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerMatchers.isClosed;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.mockito.Mockito.when;

public class LoginActivityTest extends BaseTest {

    @Inject
    LoginViewModel loginViewModel;

    @Inject
    HomeViewModel homeViewModel;

    @Inject
    UserDataSource userDataSource;

    @Rule
    public final ActivityTestRule<LoginActivity> mRule = new ActivityTestRule<>(LoginActivity.class, false, false);

    private MutableLiveData<User> userData = new MutableLiveData<>();
    private MutableLiveData<Status> status = new MutableLiveData<>();

    @Before
    public void init() {
        getApp().getComponent().inject(this);
        when(userDataSource.getUser()).thenReturn(Maybe.empty());
        when(loginViewModel.getUser()).thenReturn(userData);
        when(loginViewModel.getStatus()).thenReturn(status);
        when(homeViewModel.getUser()).thenReturn(userData);
        when(homeViewModel.getStatus()).thenReturn(status);
        mRule.launchActivity(null);
        clearDatabaseAndPrefs();
    }

    @After
    public void clearData() {
        clearDatabaseAndPrefs();
    }

    @Test
    public void testErrorMessagesLoginFragment() {
        userData.postValue(null);
        clickOnLoginButton();

        onView(withText(R.string.no_email_error)).check(matches(isDisplayed()));
        onView(withText(R.string.no_password_error)).check(matches(isDisplayed()));

        onView(Matchers.allOf(ViewMatchers.withId(R.id.login_email_et), isDescendantOfA(withId(R.id.fragment_login))))
                .perform(replaceText(MAIL));

        clickOnLoginButton();

        onView(withText(R.string.no_password_error)).check(matches(isDisplayed()));

        onView(allOf(withId(R.id.login_password_et), isDescendantOfA(withId(R.id.fragment_login))))
                .perform(replaceText(PASSWORD));

        clickOnLoginButton();
    }

    @Test
    public void testErrorMessagesRegisterFragment() {
        userData.postValue(null);
        onView(withId(R.id.create_account)).perform(click());

        clickOnRegisterButton();

        onView(withText(R.string.no_name_error)).check(matches(isDisplayed()));
        onView(withText(R.string.no_email_error)).check(matches(isDisplayed()));
        onView(withText(R.string.no_password_error)).check(matches(isDisplayed()));

        onView(Matchers.allOf(ViewMatchers.withId(R.id.register_name_et), isDescendantOfA(withId(R.id.fragment_register))))
                .perform(replaceText(DISPLAY_NAME));

        clickOnRegisterButton();

        onView(withText(R.string.no_email_error)).check(matches(isDisplayed()));
        onView(withText(R.string.no_password_error)).check(matches(isDisplayed()));

        onView(Matchers.allOf(ViewMatchers.withId(R.id.register_email_et), isDescendantOfA(withId(R.id.fragment_register))))
                .perform(replaceText(MAIL));

        clickOnRegisterButton();

        onView(withText(R.string.no_password_error)).check(matches(isDisplayed()));

        onView(allOf(withId(R.id.register_password_et), isDescendantOfA(withId(R.id.fragment_register))))
                .perform(replaceText(PASSWORD));

        clickOnRegisterButton();
    }

    @Test
    public void testGuestModeLink() {
        userData.postValue(null);

        try {
            onView(withId(R.id.skip_login_button)).perform(click());
        } catch (Exception e) {
            pressBack();
            sleep(500);
            onView(withId(R.id.skip_login_button)).perform(click());
        }

        onView(withId(R.id.program_link)).check(matches(isDisplayed()));

        onView(withId(R.id.drawer))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());

        onView(withId(R.id.navigation_view))
                .perform(NavigationViewActions.navigateTo(R.id.login));

        onView(withId(R.id.login_button)).check(matches(isDisplayed()));
    }

    @Test
    public void testSignupAndDisplayName() {
        userData.postValue(getUser());

        sleep(1000);

        onView(withId(R.id.drawer))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());

        onView(withId(R.id.user_name)).check(matches(withText(DISPLAY_NAME)));
        onView(withId(R.id.user_email)).check(matches(withText(MAIL)));
    }

    private void clickOnLoginButton() {
        onView(withId(R.id.login_button)).perform(click());
    }

    private void clickOnRegisterButton() {
        onView(withId(R.id.signup_button)).perform(click());
    }

    private void clearDatabaseAndPrefs() {
        EasyPrefs.clearAll(getApp());
        LocalDatabase.getDatabase(getApp()).clearAllTables();
    }
}
