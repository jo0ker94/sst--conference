package com.example.karlo.sstconference.ui;

import android.arch.lifecycle.MutableLiveData;
import android.content.Intent;
import android.support.test.rule.ActivityTestRule;

import com.example.karlo.sstconference.BaseTest;
import com.example.karlo.sstconference.R;
import com.example.karlo.sstconference.commons.Constants;
import com.example.karlo.sstconference.commons.Status;
import com.example.karlo.sstconference.models.User;
import com.example.karlo.sstconference.models.program.Comment;
import com.example.karlo.sstconference.models.program.Topic;
import com.example.karlo.sstconference.models.program.Track;
import com.example.karlo.sstconference.modules.program.ProgramActivity;
import com.example.karlo.sstconference.modules.program.ProgramViewModel;

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
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.when;

public class ProgramActivityTest extends BaseTest {

    @Inject
    ProgramViewModel viewModel;

    @Rule
    public final ActivityTestRule<ProgramActivity> mRule = new ActivityTestRule<>(ProgramActivity.class, false, false);

    private MutableLiveData<List<Topic>> topics = new MutableLiveData<>();
    private MutableLiveData<List<Track>> tracks = new MutableLiveData<>();
    private MutableLiveData<List<Comment>> comments = new MutableLiveData<>();
    private MutableLiveData<User> user = new MutableLiveData<>();
    private MutableLiveData<Status> status = new MutableLiveData<>();

    @Before
    public void init() {
        getApp().getComponent().inject(this);
        when(viewModel.getTracks()).thenReturn(tracks);
        when(viewModel.getTopics()).thenReturn(topics);
        when(viewModel.getComments()).thenReturn(comments);
        when(viewModel.getUser()).thenReturn(user);
        when(viewModel.getStatus()).thenReturn(status);
    }

    private void launchProgramActivity() {
        mRule.launchActivity(null);
        clearDatabaseAndPrefs();
    }

    private void launchTopicDetails() {
        Intent intent = new Intent(getApp(), ProgramActivity.class);
        intent.putExtra(Constants.INTENT_TOPIC_DETAILS, true);
        intent.putExtra(Constants.DATA, getTopic());
        mRule.launchActivity(intent);
        clearDatabaseAndPrefs();
    }

    @After
    public void clearData() {
        clearDatabaseAndPrefs();
    }

    @Test
    public void testTracksAreDisplayed() {
        launchProgramActivity();
        tracks.postValue(getTracks(3));

        onView(withText(getStringFormat(TITLE, 0))).check(matches(isDisplayed()));
        onView(withText(getStringFormat(TITLE, 1))).check(matches(isDisplayed()));
        onView(withText(getStringFormat(TITLE, 2))).check(matches(isDisplayed()));
    }

    @Test
    public void testTopicsAreDisplayed() {
        launchProgramActivity();
        tracks.postValue(getTracks(15));
        topics.postValue(getTopics(5));

        onView(withText(getStringFormat(TITLE, 0))).perform(click());

        checkToolbarTitle();
        getRecyclerViewItem(R.id.recycler_view, 0).check(matches(hasDescendant(withText(getStringFormat(TITLE, 0)))));
        getRecyclerViewItem(R.id.recycler_view, 1).check(matches(hasDescendant(withText(getStringFormat(TITLE, 1)))));
        getRecyclerViewItem(R.id.recycler_view, 2).check(matches(hasDescendant(withText(getStringFormat(TITLE, 2)))));
        getRecyclerViewItem(R.id.recycler_view, 3).check(matches(hasDescendant(withText(getStringFormat(TITLE, 3)))));
        getRecyclerViewItem(R.id.recycler_view, 4).check(matches(hasDescendant(withText(getStringFormat(TITLE, 4)))));
    }

    @Test
    public void testTopicDetails() {
        launchTopicDetails();
        comments.postValue(getComments(10));

        sleep(1000);

        onView(withId(R.id.topic_title)).check(matches(withText(TITLE)));
        onView(withId(R.id.topic_lecturers)).check(matches(withText(containsString(getStringFormat(NAME, 0)))));

        getRecyclerViewItem(R.id.recycler_view, 0).check(matches(hasDescendant(withText(getStringFormat(TEXT, 0)))));
        getRecyclerViewItem(R.id.recycler_view, 0).check(matches(hasDescendant(withText(getStringFormat(AUTHOR, 0)))));
        getRecyclerViewItem(R.id.recycler_view, 1).check(matches(hasDescendant(withText(getStringFormat(TEXT, 1)))));
        getRecyclerViewItem(R.id.recycler_view, 1).check(matches(hasDescendant(withText(getStringFormat(AUTHOR, 1)))));
        getRecyclerViewItem(R.id.recycler_view, 2).check(matches(hasDescendant(withText(getStringFormat(TEXT, 2)))));
        getRecyclerViewItem(R.id.recycler_view, 2).check(matches(hasDescendant(withText(getStringFormat(AUTHOR, 2)))));
        getRecyclerViewItem(R.id.recycler_view, 3).check(matches(hasDescendant(withText(getStringFormat(TEXT, 3)))));
        getRecyclerViewItem(R.id.recycler_view, 3).check(matches(hasDescendant(withText(getStringFormat(AUTHOR, 3)))));
        getRecyclerViewItem(R.id.recycler_view, 4).check(matches(hasDescendant(withText(getStringFormat(TEXT, 4)))));
        getRecyclerViewItem(R.id.recycler_view, 4).check(matches(hasDescendant(withText(getStringFormat(AUTHOR, 4)))));
        getRecyclerViewItem(R.id.recycler_view, 5).check(matches(hasDescendant(withText(getStringFormat(TEXT, 5)))));
        getRecyclerViewItem(R.id.recycler_view, 5).check(matches(hasDescendant(withText(getStringFormat(AUTHOR, 5)))));
        getRecyclerViewItem(R.id.recycler_view, 6).check(matches(hasDescendant(withText(getStringFormat(TEXT, 6)))));
        getRecyclerViewItem(R.id.recycler_view, 6).check(matches(hasDescendant(withText(getStringFormat(AUTHOR, 6)))));
        getRecyclerViewItem(R.id.recycler_view, 7).check(matches(hasDescendant(withText(getStringFormat(TEXT, 7)))));
        getRecyclerViewItem(R.id.recycler_view, 7).check(matches(hasDescendant(withText(getStringFormat(AUTHOR, 7)))));
        getRecyclerViewItem(R.id.recycler_view, 8).check(matches(hasDescendant(withText(getStringFormat(TEXT, 8)))));
        getRecyclerViewItem(R.id.recycler_view, 8).check(matches(hasDescendant(withText(getStringFormat(AUTHOR, 8)))));
        getRecyclerViewItem(R.id.recycler_view, 9).check(matches(hasDescendant(withText(getStringFormat(TEXT, 9)))));
        getRecyclerViewItem(R.id.recycler_view, 9).check(matches(hasDescendant(withText(getStringFormat(AUTHOR, 9)))));
    }

    @Test
    public void testFlow() {
        launchProgramActivity();
        tracks.postValue(getTracks(3));
        topics.postValue(getTopics(5));
        comments.postValue(getComments(5));

        sleep(1000);

        onView(withText(getStringFormat(TITLE, 0))).check(matches(isDisplayed()));
        onView(withText(getStringFormat(TITLE, 0))).perform(click());

        sleep(1000);

        getRecyclerViewItem(R.id.recycler_view, 0).check(matches(hasDescendant(withText(getStringFormat(TITLE, 0)))));
        getRecyclerViewItem(R.id.recycler_view, 0).perform(click());

        sleep(1000);

        onView(withId(R.id.topic_title)).check(matches(withText(getStringFormat(TITLE, 0))));
        onView(withId(R.id.topic_lecturers)).check(matches(withText(containsString(getStringFormat(NAME, 0)))));

        getRecyclerViewItem(R.id.recycler_view, 0).check(matches(hasDescendant(withText(getStringFormat(TEXT, 0)))));
        getRecyclerViewItem(R.id.recycler_view, 0).check(matches(hasDescendant(withText(getStringFormat(AUTHOR, 0)))));
    }

    @Test
    public void testErrorMessage() {
        launchProgramActivity();
        status.postValue(Status.error(TEST_MESSAGE));

        sleep(500);

        onView(withText(TEST_MESSAGE)).inRoot(new ToastMatcher())
                .check(matches(isDisplayed()));
    }

    @Test
    public void testProgressBar() {
        launchProgramActivity();
        status.postValue(Status.loading(true));
        sleep(500);
        onView(Matchers.allOf(withId(R.id.progress_bar), isDescendantOfA(withId(R.id.program_container))))
                .check(matches(isDisplayed()));

        status.postValue(Status.loading(false));
        sleep(500);
        onView(Matchers.allOf(withId(R.id.progress_bar), isDescendantOfA(withId(R.id.program_container))))
                .check(matches(not(isDisplayed())));
    }

    private void checkToolbarTitle() {
        onView(allOf(withText(getStringFormat(TITLE, 0)),
                childAtPosition(
                        allOf(withId(R.id.toolbar),
                                childAtPosition(
                                        withId(R.id.program_container),
                                        0)),
                        0),
                isDisplayed()))
                .check(matches(withText(getStringFormat(TITLE, 0))));
    }
}
