package com.example.karlo.sstconference;

import android.content.res.Resources;
import android.net.Uri;
import android.os.IBinder;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Root;
import android.view.WindowManager;

import com.example.karlo.sstconference.database.LocalDatabase;
import com.example.karlo.sstconference.models.ConferenceChair;
import com.example.karlo.sstconference.models.Image;
import com.example.karlo.sstconference.models.User;
import com.example.karlo.sstconference.models.committee.CommitteeMember;
import com.example.karlo.sstconference.models.keynote.KeynoteSpeaker;
import com.example.karlo.sstconference.models.program.Comment;
import com.example.karlo.sstconference.models.program.Person;
import com.example.karlo.sstconference.models.program.Topic;
import com.example.karlo.sstconference.models.program.Track;
import com.example.karlo.sstconference.models.venue.Info;
import com.example.karlo.sstconference.models.venue.Venue;
import com.example.karlo.sstconference.models.venue.VenueMarker;

import net.globulus.easyprefs.EasyPrefs;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.ArrayList;
import java.util.List;

public class BaseTest {

    private static String FORMAT = "%s_%d";

    protected static String NAME = "Some Name";
    protected static String TITLE = "Title";
    protected static String MAIL = "mail@email.com";
    protected static String FACILITY = "Some Facility";
    protected static String IMAGE = "http://www.picture.com";
    protected static String NUMBER = "0123456789";
    protected static String PASSWORD = "test123";

    protected static String DISPLAY_NAME = "Display Name";
    protected static String GUEST = "Guest";
    protected static String TEXT = "This is comment.";
    protected static String USER_ID = "user_id";
    protected static String AUTHOR = "Mike Doe";
    protected static String TIMESTAMP = "12/12/2012";
    protected static String EMPTY = "";

    protected static String STEERING = "steering";
    protected static String PROGRAM = "program";
    protected static String ORGANIZING = "organizing";
    protected static String ABSTRACT = "So many words!";
    protected static String TEST_MESSAGE = "Test error message!";

    protected static String SNIPPET = "Snippet";
    protected static String LINK = "link";
    protected static String FACULTY = "faculty";
    protected static String HOTEL = "hotel";
    protected static String REGION = "region";

    protected static String START_DATE = "12.12.2012";
    protected static String END_DATE = "15.12.2012";

    protected static int PARENT_ID = 12;
    protected static int ROOM = 0;
    protected static int TYPE = 0;

    protected static double LAT = 124.123;
    protected static double LNG = 21.532;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    private Resources mResources;

    public BaseTest() {
        mResources = getApp().getResources();
    }

    protected String getString(int resId) {
        return mResources.getString(resId);
    }

    protected String getQuantityString(int resId, int quantity) {
        return mResources.getQuantityString(resId, quantity);
    }

    protected TestApp getApp() {
        return (TestApp) InstrumentationRegistry.getInstrumentation()
                .getTargetContext().getApplicationContext();
    }

    protected void sleep(int length) {
        try {
            Thread.sleep(length);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected void clearDatabaseAndPrefs() {
        EasyPrefs.clearAll(getApp());
        LocalDatabase.getDatabase(getApp()).clearAllTables();
    }

    protected String getStringFormat(String field, int position) {
        return String.format(FORMAT, field, position);
    }

    protected ConferenceChair getConferenceChair() {
        return getConferenceChair(0);
    }

    protected ConferenceChair getConferenceChair(int id) {
        return new ConferenceChair(id, TITLE, MAIL, FACILITY, IMAGE, NAME, NUMBER);
    }

    protected Comment getComment() {
        return getComment(0);
    }

    protected Comment getComment(int id) {
        return new Comment(id, TEXT, USER_ID, PARENT_ID, AUTHOR, TIMESTAMP);
    }

    protected CommitteeMember getSteeringCommitteeMember() {
        return new CommitteeMember(0, NAME, FACILITY, STEERING);
    }

    protected CommitteeMember getProgramCommitteeMember() {
        return getProgramCommitteeMember(0);
    }

    protected CommitteeMember getProgramCommitteeMember(int id) {
        return new CommitteeMember(id, NAME, FACILITY, PROGRAM);
    }

    protected CommitteeMember getOrganizingCommitteeMember() {
        return new CommitteeMember(0, NAME, FACILITY, ORGANIZING);
    }

    protected KeynoteSpeaker getKeynoteSpeaker() {
        return getKeynoteSpeaker(0);
    }

    protected KeynoteSpeaker getKeynoteSpeaker(int id) {
        return new KeynoteSpeaker(id, NAME, FACILITY, MAIL, IMAGE, TITLE, ABSTRACT);
    }

    protected Topic getTopic() {
        return getTopic(0);
    }

    protected Topic getTopic(int id) {
        return new Topic(id, PARENT_ID, TITLE, getListOfPeople(), TYPE);
    }

    protected Track getTrack() {
        return getTrack(0);
    }

    protected Track getTrack(int id) {
        return new Track(id, START_DATE, END_DATE, ROOM, TITLE, getListOfPeople());
    }

    protected User getUser() {
        return new User(USER_ID, MAIL, DISPLAY_NAME, Uri.parse(IMAGE), getSubscribedEvents());
    }

    protected Venue getVenue() {
        return new Venue(0, getInfoList(FACULTY), getInfoList(REGION), getInfoList(HOTEL));
    }

    protected Image getImage() {
        return getImage(0);
    }

    protected Image getImage(int id) {
        return new Image(id, IMAGE);
    }

    protected List<Info> getInfoList(String type) {
        List<Info> infos = new ArrayList<>();
        infos.add(getInfo(0, type));
        infos.add(getInfo(1, type));
        infos.add(getInfo(2, type));
        return infos;
    }

    protected Info getInfo(int id, String description) {
        return new Info(id, description, IMAGE, LINK, TITLE, getMarker());
    }

    protected VenueMarker getMarker() {
        return new VenueMarker(LAT, LNG, TITLE, SNIPPET);
    }

    protected List<Person> getListOfPeople() {
        List<Person> people = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            people.add(new Person(getStringFormat(NAME, i)));
        }
        return people;
    }

    protected List<Integer> getSubscribedEvents() {
        List<Integer> events = new ArrayList<>();
        events.add(1);
        events.add(12);
        events.add(4);
        events.add(5);
        events.add(22);
        events.add(14);
        return events;
    }

    public class ToastMatcher extends TypeSafeMatcher<Root> {

        @Override
        public void describeTo(Description description) {
            description.appendText("is toast");
        }

        @Override
        public boolean matchesSafely(Root root) {
            int type = root.getWindowLayoutParams().get().type;
            if (type == WindowManager.LayoutParams.TYPE_TOAST) {
                IBinder windowToken = root.getDecorView().getWindowToken();
                IBinder appToken = root.getDecorView().getApplicationWindowToken();
                return windowToken == appToken;
            }
            return false;
        }
    }
}
