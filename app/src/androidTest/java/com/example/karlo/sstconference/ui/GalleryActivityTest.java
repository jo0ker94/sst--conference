package com.example.karlo.sstconference.ui;

import android.Manifest;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.provider.Settings;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.uiautomator.UiDevice;

import com.example.karlo.sstconference.BaseTest;
import com.example.karlo.sstconference.R;
import com.example.karlo.sstconference.commons.Status;
import com.example.karlo.sstconference.models.Image;
import com.example.karlo.sstconference.modules.gallery.GalleryActivity;
import com.example.karlo.sstconference.modules.gallery.GalleryViewModel;

import net.globulus.easyprefs.EasyPrefs;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import javax.inject.Inject;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.action.ViewActions.swipeRight;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.when;

public class GalleryActivityTest extends BaseTest {

    @Inject
    GalleryViewModel viewModel;

    @Rule
    public final ActivityTestRule<GalleryActivity> mRule = new ActivityTestRule<>(GalleryActivity.class, false, false);

    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(Manifest.permission.CAMERA);

    private MutableLiveData<List<Image>> images = new MutableLiveData<>();
    private MutableLiveData<Status> status = new MutableLiveData<>();

    @Before
    public void init() {
        getApp().getComponent().inject(this);
        when(viewModel.getImages()).thenReturn(images);
        when(viewModel.getStatus()).thenReturn(status);
        mRule.launchActivity(null);
        clearDatabaseAndPrefs();
    }

    @After
    public void clearData() {
        clearDatabaseAndPrefs();
    }

    @Test
    public void testNothingDisplayed() {
        images.postValue(null);
        sleep(500);
        onView(withId(R.id.emptyData)).check(matches(withText(getString(R.string.there_are_no_uploaded_images_be_the_first_to_upload))));
    }

    @Test
    public void testEverythingDisplayed() {
        images.postValue(getImages(5));
        sleep(500);
        onView(withId(R.id.emptyData)).check(matches(not(isDisplayed())));
    }

    @Test
    public void testDisplayMode() {
        images.postValue(getImages(5));
        sleep(500);
        getRecyclerViewItem(R.id.imageListView, 0).perform(click());
        onView(allOf(withId(R.id.imageCard), isCompletelyDisplayed())).perform(swipeLeft());
        onView(allOf(withId(R.id.imageCard), isCompletelyDisplayed())).perform(swipeRight());

        onView(allOf(withId(R.id.imageCard), isCompletelyDisplayed())).perform(click());
        onView(allOf(withId(R.id.shareButton), isCompletelyDisplayed())).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.shareButton), isCompletelyDisplayed())).perform(click());
        Espresso.pressBack();
    }

    @Test
    public void testShareLink() {
        images.postValue(getImages(5));
        sleep(500);
        getRecyclerViewItem(R.id.imageListView, 0).perform(click());

        onView(allOf(withId(R.id.imageCard), isCompletelyDisplayed())).perform(click());
        onView(allOf(withId(R.id.shareButton), isCompletelyDisplayed())).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.shareButton), isCompletelyDisplayed())).perform(click());
        sleep(300);
        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        device.pressBack();

    }

    @Test
    public void testOnlyForLoggedInUsersMessage() {
        EasyPrefs.putGuestMode(mRule.getActivity(), true);
        mRule.getActivity().startActivity(new Intent(mRule.getActivity(), GalleryActivity.class));
        images.postValue(null);
        sleep(500);

        onView(withId(R.id.imageUpload)).perform(click());
        sleep(500);
        onView(withText(getString(R.string.only_for_logged_in))).check(matches(isDisplayed()));

        onView(withId(R.id.takeImage)).perform(click());
        sleep(500);
        onView(withText(getString(R.string.only_for_logged_in))).check(matches(isDisplayed()));
    }

    @Test
    public void testMenuItems() {
        images.postValue(null);
        sleep(500);

        onView(withId(R.id.imageUpload)).perform(click());
        sleep(500);

        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        device.pressBack();

        onView(withId(R.id.takeImage)).perform(click());
        sleep(500);
        device.pressBack();
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
        onView(withId(R.id.progress_bar)).check(matches(isDisplayed()));

        status.postValue(Status.loading(false));
        sleep(500);
        onView(withId(R.id.progress_bar)).check(matches(not(isDisplayed())));
    }
}
