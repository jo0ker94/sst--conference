package com.example.karlo.sstconference.ui;

import android.arch.lifecycle.MutableLiveData;
import android.support.test.rule.ActivityTestRule;

import com.example.karlo.sstconference.BaseTest;
import com.example.karlo.sstconference.R;
import com.example.karlo.sstconference.commons.Status;
import com.example.karlo.sstconference.models.keynote.KeynoteSpeaker;
import com.example.karlo.sstconference.modules.keynotespeakers.KeynoteActivity;
import com.example.karlo.sstconference.modules.keynotespeakers.KeynoteViewModel;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import javax.inject.Inject;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.AllOf.allOf;
import static org.mockito.Mockito.when;

public class KeynoteActivityTest extends BaseTest {

    @Inject
    KeynoteViewModel viewModel;

    @Rule
    public final ActivityTestRule<KeynoteActivity> mRule = new ActivityTestRule<>(KeynoteActivity.class, false, false);

    private MutableLiveData<List<KeynoteSpeaker>> speakers = new MutableLiveData<>();
    private MutableLiveData<Status> status = new MutableLiveData<>();

    @Before
    public void init() {
        getApp().getComponent().inject(this);
        when(viewModel.getSpeakers()).thenReturn(speakers);
        when(viewModel.getStatus()).thenReturn(status);
        mRule.launchActivity(null);
        clearDatabaseAndPrefs();
    }

    @After
    public void clearData() {
        clearDatabaseAndPrefs();
    }

    @Test
    public void testFirstPage() {
        speakers.postValue(getKeynoteSpeakers(3));

        onView(allOf(withId(R.id.text_name), isDescendantOfA(withId(R.id.fragment_0))))
                .check(matches(withText(getStringFormat(NAME, 0))));
        onView(allOf(withId(R.id.text_email), isDescendantOfA(withId(R.id.fragment_0))))
                .check(matches(withText(getStringFormat(MAIL, 0))));
        onView(allOf(withId(R.id.text_facility), isDescendantOfA(withId(R.id.fragment_0))))
                .check(matches(withText(getStringFormat(FACILITY, 0))));
        onView(allOf(withId(R.id.text_title), isDescendantOfA(withId(R.id.fragment_0))))
                .check(matches(withText(getStringFormat(TITLE, 0))));
        onView(allOf(withId(R.id.text_abstract), isDescendantOfA(withId(R.id.fragment_0))))
                .check(matches(withText(getStringFormat(ABSTRACT, 0))));
        pressBack();
    }

    @Test
    public void testSecondPage() {
        speakers.postValue(getKeynoteSpeakers(3));

        onView(withId(R.id.fragment_0)).perform(swipeLeft());

        onView(allOf(withId(R.id.text_name), isDescendantOfA(withId(R.id.fragment_1))))
                .check(matches(withText(getStringFormat(NAME, 1))));
        onView(allOf(withId(R.id.text_email), isDescendantOfA(withId(R.id.fragment_1))))
                .check(matches(withText(getStringFormat(MAIL, 1))));
        onView(allOf(withId(R.id.text_facility), isDescendantOfA(withId(R.id.fragment_1))))
                .check(matches(withText(getStringFormat(FACILITY, 1))));
        onView(allOf(withId(R.id.text_title), isDescendantOfA(withId(R.id.fragment_1))))
                .check(matches(withText(getStringFormat(TITLE, 1))));
        onView(allOf(withId(R.id.text_abstract), isDescendantOfA(withId(R.id.fragment_1))))
                .check(matches(withText(getStringFormat(ABSTRACT, 1))));
    }

    @Test
    public void testThirdPage() {
        speakers.postValue(getKeynoteSpeakers(3));

        onView(withId(R.id.fragment_0)).perform(swipeLeft());
        onView(withId(R.id.fragment_1)).perform(swipeLeft());

        onView(allOf(withId(R.id.text_name), isDescendantOfA(withId(R.id.fragment_2))))
                .check(matches(withText(getStringFormat(NAME, 2))));
        onView(allOf(withId(R.id.text_email), isDescendantOfA(withId(R.id.fragment_2))))
                .check(matches(withText(getStringFormat(MAIL, 2))));
        onView(allOf(withId(R.id.text_facility), isDescendantOfA(withId(R.id.fragment_2))))
                .check(matches(withText(getStringFormat(FACILITY, 2))));
        onView(allOf(withId(R.id.text_title), isDescendantOfA(withId(R.id.fragment_2))))
                .check(matches(withText(getStringFormat(TITLE, 2))));
        onView(allOf(withId(R.id.text_abstract), isDescendantOfA(withId(R.id.fragment_2))))
                .check(matches(withText(getStringFormat(ABSTRACT, 2))));
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
        onView(Matchers.allOf(withId(R.id.progress_bar), isDescendantOfA(withId(R.id.main_content))))
                .check(matches(isDisplayed()));

        status.postValue(Status.loading(false));
        sleep(500);
        onView(Matchers.allOf(withId(R.id.progress_bar), isDescendantOfA(withId(R.id.main_content))))
                .check(matches(not(isDisplayed())));
    }
}
