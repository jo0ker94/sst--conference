package com.example.karlo.sstconference.ui;

import android.arch.lifecycle.MutableLiveData;
import android.support.test.rule.ActivityTestRule;

import com.example.karlo.sstconference.BaseTest;
import com.example.karlo.sstconference.R;
import com.example.karlo.sstconference.commons.Status;
import com.example.karlo.sstconference.models.program.Topic;
import com.example.karlo.sstconference.modules.search.SearchActivity;
import com.example.karlo.sstconference.modules.search.SearchViewModel;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import javax.inject.Inject;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.AllOf.allOf;
import static org.mockito.Mockito.when;

public class SearchActivityTest extends BaseTest {

    @Inject
    SearchViewModel viewModel;

    @Rule
    public final ActivityTestRule<SearchActivity> mRule = new ActivityTestRule<>(SearchActivity.class, false, false);

    private MutableLiveData<List<Topic>> topics = new MutableLiveData<>();
    private MutableLiveData<Status> status = new MutableLiveData<>();

    @Before
    public void init() {
        getApp().getComponent().inject(this);
        when(viewModel.getTopics()).thenReturn(topics);
        when(viewModel.getStatus()).thenReturn(status);
        mRule.launchActivity(null);
        clearDatabaseAndPrefs();
    }

    @After
    public void clearData() {
        clearDatabaseAndPrefs();
    }

    @Test
    public void testEverythingIsDisplayed() {
        topics.postValue(getTopics(20));
        checkIfRecyclerViewItemHasText(R.id.searchListView, 0, getStringFormat(TITLE, 0));
        onView(withId(R.id.searchListView)).perform(scrollToPosition(10));
        checkIfRecyclerViewItemHasText(R.id.searchListView, 10, getStringFormat(TITLE, 10));
    }

    @Test
    public void testSearch() {
        topics.postValue(getTopics(20));
        onView(withContentDescription(R.string.search)).perform(click());

        onView(allOf(withId(R.id.search_edit_text), isDescendantOfA(withId(R.id.search_bar))))
                .perform(replaceText("0"), closeSoftKeyboard());
        checkIfRecyclerViewItemHasText(R.id.searchListView, 0, getStringFormat(TITLE, 0));
        checkIfRecyclerViewItemHasText(R.id.searchListView, 1, getStringFormat(TITLE, 10));


        onView(allOf(withId(R.id.search_edit_text), isDescendantOfA(withId(R.id.search_bar))))
                .perform(replaceText("2"), closeSoftKeyboard());
        checkIfRecyclerViewItemHasText(R.id.searchListView, 0, getStringFormat(TITLE, 2));
        checkIfRecyclerViewItemHasText(R.id.searchListView, 1, getStringFormat(TITLE, 12));
    }

    @Test
    public void testClickOnItem() {
        topics.postValue(getTopics(5));
        onView(withContentDescription(R.string.search)).perform(click());

        onView(allOf(withId(R.id.search_edit_text), isDescendantOfA(withId(R.id.search_bar))))
                .perform(replaceText("0"), closeSoftKeyboard());
        checkIfRecyclerViewItemHasText(R.id.searchListView, 0, getStringFormat(TITLE, 0));
        getRecyclerViewItem(R.id.searchListView, 0).perform(click());

        sleep(1000);
        onView(allOf(withId(R.id.topic_title), isDescendantOfA(withId(R.id.topic_container))))
                .check(matches(withText(getStringFormat(TITLE, 0))));
        onView(allOf(withId(R.id.topic_lecturers), isDescendantOfA(withId(R.id.topic_container))))
                .check(matches(withText(containsString(getStringFormat(NAME, 0)))));
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
        onView(Matchers.allOf(withId(R.id.progress_bar), isDescendantOfA(withId(R.id.search_root_container))))
                .check(matches(isDisplayed()));

        status.postValue(Status.loading(false));
        sleep(500);
        onView(Matchers.allOf(withId(R.id.progress_bar), isDescendantOfA(withId(R.id.search_root_container))))
                .check(matches(not(isDisplayed())));
    }
}
