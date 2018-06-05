package com.example.karlo.sstconference.ui;

import android.arch.lifecycle.MutableLiveData;
import android.support.test.rule.ActivityTestRule;

import com.example.karlo.sstconference.BaseTest;
import com.example.karlo.sstconference.R;
import com.example.karlo.sstconference.commons.Status;
import com.example.karlo.sstconference.models.program.Topic;
import com.example.karlo.sstconference.modules.subscribed.SubscriptionActivity;
import com.example.karlo.sstconference.modules.subscribed.SubscriptionViewModel;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import javax.inject.Inject;

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

public class SubscribedActivityTest extends BaseTest {

    @Inject
    SubscriptionViewModel viewModel;

    @Rule
    public final ActivityTestRule<SubscriptionActivity> mRule = new ActivityTestRule<>(SubscriptionActivity.class, false, false);

    private MutableLiveData<List<Topic>> topics = new MutableLiveData<>();
    private MutableLiveData<Status> status = new MutableLiveData<>();

    @Before
    public void init() {
        getApp().getComponent().inject(this);
        when(viewModel.getSubscribedTopics()).thenReturn(topics);
        when(viewModel.getStatus()).thenReturn(status);
        mRule.launchActivity(null);
        clearDatabaseAndPrefs();
    }

    @After
    public void clearData() {
        clearDatabaseAndPrefs();
    }

    @Test
    public void testSubscribedEvents() {
        topics.postValue(getTopics(6));

        checkIfRecyclerViewItemHasText(R.id.searchListView, 0, getStringFormat(TITLE, 0));
        checkIfRecyclerViewItemHasText(R.id.searchListView, 1, getStringFormat(TITLE, 1));
        checkIfRecyclerViewItemHasText(R.id.searchListView, 2, getStringFormat(TITLE, 2));
        checkIfRecyclerViewItemHasText(R.id.searchListView, 3, getStringFormat(TITLE, 3));
        checkIfRecyclerViewItemHasText(R.id.searchListView, 4, getStringFormat(TITLE, 4));
        checkIfRecyclerViewItemHasText(R.id.searchListView, 5, getStringFormat(TITLE, 5));
    }

    @Test
    public void testDeleteSubscription() {
        topics.postValue(getTopics(3));

        getRecyclerViewItem(R.id.searchListView, 1).perform(swipeLeft());
        sleep(500);
        onView(withText(getString(R.string.deleted_successfully))).check(matches(isDisplayed()));

        checkIfRecyclerViewItemHasText(R.id.searchListView, 0, getStringFormat(TITLE, 0));
        checkIfRecyclerViewItemHasText(R.id.searchListView, 1, getStringFormat(TITLE, 2));
    }

    @Test
    public void testUndoDeleteSubscription() {
        topics.postValue(getTopics(3));

        getRecyclerViewItem(R.id.searchListView, 1).perform(swipeLeft());
        sleep(500);
        onView(withText(getString(R.string.deleted_successfully))).check(matches(isDisplayed()));
        onView(withText(getString(R.string.undo).toUpperCase())).perform(click());
        onView(withText(getString(R.string.event_restored))).check(matches(isDisplayed()));

        checkIfRecyclerViewItemHasText(R.id.searchListView, 0, getStringFormat(TITLE, 0));
        checkIfRecyclerViewItemHasText(R.id.searchListView, 1, getStringFormat(TITLE, 1));
        checkIfRecyclerViewItemHasText(R.id.searchListView, 2, getStringFormat(TITLE, 2));
    }

    @Test
    public void testNoSubscribedEventsMessage() {
        topics.postValue(null);

        onView(withId(R.id.no_result))
                .check(matches(withText(getString(R.string.you_are_not_subscribed_on_any_event))));
    }

    @Test
    public void testProgressBar() {
        status.postValue(Status.loading(true));
        sleep(500);
        onView(allOf(withId(R.id.progress_bar), isDescendantOfA(withId(R.id.subscribed_container))))
                .check(matches(isDisplayed()));

        status.postValue(Status.loading(false));
        sleep(500);
        onView(allOf(withId(R.id.progress_bar), isDescendantOfA(withId(R.id.subscribed_container))))
                .check(matches(not(isDisplayed())));
    }

    @Test
    public void testErrorMessage() {
        status.postValue(Status.error(TEST_MESSAGE));

        sleep(500);

        onView(withText(TEST_MESSAGE)).inRoot(new ToastMatcher())
                .check(matches(isDisplayed()));
    }
}