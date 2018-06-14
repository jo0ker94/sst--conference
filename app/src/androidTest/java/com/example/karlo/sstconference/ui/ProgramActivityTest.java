package com.example.karlo.sstconference.ui;

import android.arch.lifecycle.MutableLiveData;
import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.view.KeyEvent;
import android.widget.CheckBox;

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
import com.example.karlo.sstconference.utility.DateUtility;

import net.globulus.easyprefs.EasyPrefs;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.action.ViewActions.pressKey;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.swipeDown;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.action.ViewActions.swipeRight;
import static android.support.test.espresso.action.ViewActions.swipeUp;
import static android.support.test.espresso.action.ViewActions.typeTextIntoFocusedView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.hasSibling;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withChild;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.instanceOf;
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
    public void testSwipe() {
        launchProgramActivity();
        List<Track> trackList = getTracks(15);
        trackList.get(1).setStartDate("2017-10-19T11:25:00+02:00");
        trackList.get(2).setStartDate("2017-10-20T11:25:00+02:00");
        tracks.postValue(trackList);

        onView(withText(getStringFormat(TITLE, 0))).check(matches(isDisplayed()));
        onView(withChild(withText(getStringFormat(TITLE, 0)))).perform(swipeLeft());
        sleep(500);
        onView(withText(getStringFormat(TITLE, 1))).check(matches(isDisplayed()));
        onView(withChild(withText(getStringFormat(TITLE, 1)))).perform(swipeLeft());
        sleep(500);
        onView(withText(getStringFormat(TITLE, 2))).check(matches(isDisplayed()));
        onView(withChild(withText(getStringFormat(TITLE, 2)))).perform(swipeLeft());
        sleep(500);
    }

    @Test
    public void testOneTopicIsDisplayed() {
        launchProgramActivity();
        tracks.postValue(getTracks(15));
        topics.postValue(getTopics(1));
        comments.postValue(getComments(5));

        onView(withText(getStringFormat(TITLE, 0))).perform(click());

        sleep(1000);

        onView(withId(R.id.topic_title)).check(matches(withText(getStringFormat(TITLE, 0))));
        onView(withId(R.id.topic_lecturers)).check(matches(withText(containsString(getStringFormat(NAME, 0)))));

        getRecyclerViewItem(R.id.recycler_view, 0).check(matches(hasDescendant(withText(getStringFormat(TEXT, 0)))));
        getRecyclerViewItem(R.id.recycler_view, 0).check(matches(hasDescendant(withText(getStringFormat(AUTHOR, 0)))));
    }

    @Test
    public void testTopicDetails() {
        launchTopicDetails();
        List<Comment> commentList = getComments(10);
        Date now = DateUtility.getNowInGMT();
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(now);
        calendar.add(Calendar.MINUTE, 5);
        Date minutes = calendar.getTime();

        calendar.setTime(now);
        calendar.add(Calendar.HOUR, 5);
        Date hour = calendar.getTime();

        calendar.setTime(now);
        calendar.add(Calendar.DATE, 5);
        Date day = calendar.getTime();

        calendar.setTime(now);
        calendar.add(Calendar.MONTH, 5);
        Date month = calendar.getTime();

        calendar.setTime(now);
        calendar.add(Calendar.YEAR, 1);
        Date year = calendar.getTime();

        commentList.get(0).setTimestamp(DateUtility.getDateInIsoFormat(minutes));
        commentList.get(1).setTimestamp(DateUtility.getDateInIsoFormat(hour));
        commentList.get(2).setTimestamp(DateUtility.getDateInIsoFormat(day));
        commentList.get(3).setTimestamp(DateUtility.getDateInIsoFormat(month));
        commentList.get(4).setTimestamp(DateUtility.getDateInIsoFormat(year));

        comments.postValue(commentList);

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
    public void testUsersCommentsDelete() {
        launchTopicDetails();
        Comment userComment = getComment();
        Comment otherComment = getComment(1);
        otherComment.setUserId(getStringFormat(USER_ID, 1));
        otherComment.setAuthor(getStringFormat(AUTHOR, 1));
        otherComment.setText(getStringFormat(TEST_MESSAGE, 1));
        List<Comment> commentList = new ArrayList<>();
        commentList.add(userComment);
        commentList.add(otherComment);

        comments.postValue(commentList);
        user.postValue(getUser());
        when(viewModel.deleteComment(userComment)).thenReturn(Completable.complete());

        sleep(1000);

        onView(withId(R.id.topic_title)).check(matches(withText(TITLE)));
        onView(withId(R.id.topic_lecturers)).check(matches(withText(containsString(getStringFormat(NAME, 0)))));

        getRecyclerViewItem(R.id.recycler_view, 0).check(matches(hasDescendant(withText(TEXT))));
        getRecyclerViewItem(R.id.recycler_view, 0).check(matches(hasDescendant(withText(AUTHOR))));
        getRecyclerViewItem(R.id.recycler_view, 1).check(matches(hasDescendant(withText(getStringFormat(TEST_MESSAGE, 1)))));
        getRecyclerViewItem(R.id.recycler_view, 1).check(matches(hasDescendant(withText(getStringFormat(AUTHOR, 1)))));

        getRecyclerViewItem(R.id.recycler_view, 1).perform(click());
        onView(withText(getStringFormat(USER_ID, 1))).inRoot(new ToastMatcher())
                .check(matches(isDisplayed()));

        getRecyclerViewItem(R.id.recycler_view, 0).perform(longClick());
        onView(withText(getString(R.string.delete_comment_message))).check(matches(isDisplayed()));
        onView(withText(getString(R.string.yes))).perform(click());
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
        pressBack();
    }

    @Test
    public void testTrackAsTopic() {
        launchProgramActivity();
        Topic topic = getTopic();
        topic.setType(1);
        List<Topic> topicList = new ArrayList<>();
        topicList.add(topic);
        Track track = getTrack();
        List<Track> trackList = new ArrayList<>();
        trackList.add(track);
        tracks.postValue(trackList);
        topics.postValue(topicList);
        comments.postValue(getComments(5));

        sleep(1000);

        onView(withText(TITLE)).check(matches(isDisplayed()));
        onView(withText(TITLE)).perform(click());

        sleep(1000);

        onView(withId(R.id.topic_title)).check(matches(withText(TITLE)));
        onView(withId(R.id.topic_lecturers)).check(matches(withText(getTimeString())));
    }

    @Test
    public void testCommentDelete() {
        launchProgramActivity();
        Topic topic = getTopic();
        topic.setType(1);
        List<Topic> topicList = new ArrayList<>();
        topicList.add(topic);
        Track track = getTrack();
        List<Track> trackList = new ArrayList<>();
        trackList.add(track);
        tracks.postValue(trackList);
        topics.postValue(topicList);
        comments.postValue(getComments(5));

        sleep(1000);

        onView(withText(TITLE)).check(matches(isDisplayed()));
        onView(withText(TITLE)).perform(click());

        sleep(1000);

        onView(withId(R.id.topic_title)).check(matches(withText(TITLE)));
        onView(withId(R.id.topic_lecturers)).check(matches(withText(getTimeString())));
    }

    @Test
    public void testSubscribe() {
        launchTopicDetails();
        comments.postValue(getComments(3));
        user.postValue(getUser());
        tracks.postValue(getTracks(1));
        EasyPrefs.putGuestMode(mRule.getActivity(), false);
        sleep(500);

        onView(withId(R.id.subscribe)).perform(click());

        sleep(500);

        status.postValue(Status.message("Subscribed!"));
        sleep(500);
        onView(withText("Subscribed!")).inRoot(new ToastMatcher())
                .check(matches(isDisplayed()));
    }

    @Test
    public void testAddComment() {
        launchTopicDetails();
        comments.postValue(getComments(6));
        user.postValue(getUser());
        when(viewModel.addComment(getComment())).thenReturn(Completable.complete());

        onView(withId(R.id.recycler_view)).perform(swipeUp());
        onView(withId(R.id.recycler_view)).perform(swipeDown());

        //onView(withId(R.id.et_comment)).perform(typeTextIntoFocusedView(TEST_MESSAGE), pressImeActionButton());
        //onView(withId(R.id.recycler_view)).perform(swipeDown());

        sleep(1000);
    }

    @Test
    public void testSubscribeForGuest() {
        launchTopicDetails();
        comments.postValue(getComments(3));
        EasyPrefs.putGuestMode(mRule.getActivity(), true);

        sleep(500);

        onView(withId(R.id.subscribe)).perform(click());

        sleep(500);

        onView(withText(getString(R.string.only_for_logged_in))).check(matches(isDisplayed()));
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
    public void testNoDataMessage() {
        launchTopicDetails();
        user.postValue(getUser());
        status.postValue(Status.noData(true));

        sleep(500);

        onView(withId(R.id.no_comments)).check(matches(withText(getString(R.string.no_comments))));
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
                childAtPosition(withId(R.id.toolbar),0),
                isDisplayed()))
                .check(matches(withText(getStringFormat(TITLE, 0))));
    }

    private String getTimeString() {
        String sTime = DateUtility.getTimeFromIsoDate(START_DATE);
        String eTime = DateUtility.getTimeFromIsoDate(END_DATE);
        return String.format(getString(R.string.time_format), sTime, eTime);
    }
}
