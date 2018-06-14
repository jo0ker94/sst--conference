package com.example.karlo.sstconference.ui;

import android.arch.lifecycle.MutableLiveData;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.uiautomator.UiDevice;

import com.example.karlo.sstconference.BaseTest;
import com.example.karlo.sstconference.R;
import com.example.karlo.sstconference.commons.Status;
import com.example.karlo.sstconference.models.ConferenceChair;
import com.example.karlo.sstconference.modules.chairs.ChairsActivity;
import com.example.karlo.sstconference.modules.chairs.ChairsViewModel;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import javax.inject.Inject;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.AllOf.allOf;
import static org.mockito.Mockito.when;

public class ChairsActivityTest extends BaseTest {

    @Inject
    ChairsViewModel viewModel;

    @Rule
    public final ActivityTestRule<ChairsActivity> mRule = new ActivityTestRule<>(ChairsActivity.class, false, false);

    private MutableLiveData<List<ConferenceChair>> chairs = new MutableLiveData<>();
    private MutableLiveData<Status> status = new MutableLiveData<>();

    @Before
    public void init() {
        getApp().getComponent().inject(this);
        when(viewModel.getChairs()).thenReturn(chairs);
        when(viewModel.getStatus()).thenReturn(status);
        mRule.launchActivity(null);
        clearDatabaseAndPrefs();
    }

    @After
    public void clearData() {
        clearDatabaseAndPrefs();
    }

    @Test
    public void testEverythingDisplayed() {
        chairs.postValue(getConferenceChairs(6));

        checkIfRecyclerViewItemHasText(R.id.conference_chairs_recycler, 0, getStringFormat(TITLE, 0));
        checkIfRecyclerViewItemHasText(R.id.conference_chairs_recycler, 1, getStringFormat(TITLE, 1));
        checkIfRecyclerViewItemHasText(R.id.conference_chairs_recycler, 2, getStringFormat(TITLE, 2));
        checkIfRecyclerViewItemHasText(R.id.conference_chairs_recycler, 3, getStringFormat(TITLE, 3));
        checkIfRecyclerViewItemHasText(R.id.conference_chairs_recycler, 4, getStringFormat(TITLE, 4));
        checkIfRecyclerViewItemHasText(R.id.conference_chairs_recycler, 5, getStringFormat(TITLE, 5));
    }

    @Test
    public void testChairsDetails() {
        chairs.postValue(getConferenceChairs(3));

        getRecyclerViewItem(R.id.conference_chairs_recycler, 0).perform(click());

        onView(withId(R.id.tvTitle)).check(matches(withText(getStringFormat(TITLE, 0))));
        onView(withId(R.id.tvName)).check(matches(withText(getStringFormat(NAME, 0))));
        onView(withId(R.id.tvEmail)).check(matches(withText(getStringFormat(MAIL, 0))));
        onView(withId(R.id.tvPhone)).check(matches(withText(getStringFormat(NUMBER, 0))));
        onView(withId(R.id.tvFacility)).check(matches(withText(getStringFormat(FACILITY, 0))));
    }

    @Test
    public void testMailDialog() {
        chairs.postValue(getConferenceChairs(3));
        getRecyclerViewItem(R.id.conference_chairs_recycler, 0).perform(click());
        onView(withId(R.id.tvEmail)).perform(click());
    }

    @Test
    public void testDialDialog() {
        chairs.postValue(getConferenceChairs(3));
        getRecyclerViewItem(R.id.conference_chairs_recycler, 0).perform(click());
        onView(withId(R.id.tvPhone)).perform(click());
        pressBack();
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
        onView(Matchers.allOf(withId(R.id.progress_bar), isDescendantOfA(withId(R.id.chairs_container))))
                .check(matches(isDisplayed()));

        status.postValue(Status.loading(false));
        sleep(500);
        onView(Matchers.allOf(withId(R.id.progress_bar), isDescendantOfA(withId(R.id.chairs_container))))
                .check(matches(not(isDisplayed())));
    }
}

