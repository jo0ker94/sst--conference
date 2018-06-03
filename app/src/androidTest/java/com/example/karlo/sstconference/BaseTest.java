package com.example.karlo.sstconference;

import android.content.res.Resources;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;

import com.example.karlo.sstconference.models.ConferenceChair;
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

    protected static String DISPLAY_NAME = "Display Name";
    protected static String TEXT = "This is comment.";
    protected static String USER_ID = "user_id";
    protected static String AUTHOR = "Mike Doe";
    protected static String TIMESTAMP = "12/12/2012";

    protected static String STEERING = "steering";
    protected static String PROGRAM = "program";
    protected static String ORGANIZING = "organizing";
    protected static String ABSTRACT = "So many words!";

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

    private Resources mResources;

    public BaseTest() {
        mResources = InstrumentationRegistry.getContext().getResources();
    }

    protected String getString(int resId) {
        return mResources.getString(resId);
    }

    protected String getQuantityString(int resId, int quantity) {
        return mResources.getQuantityString(resId, quantity);
    }

    protected void sleep(int length) {
        try {
            Thread.sleep(length);
        } catch (InterruptedException e) {  e.printStackTrace();}
    }

    protected String getStringFormat(String field, int position) {
        return String.format(FORMAT, field, position);
    }

    protected ConferenceChair getConferenceChair() {
        return new ConferenceChair(0, TITLE, MAIL, FACILITY, IMAGE, NAME, NUMBER);
    }

    protected Comment getComment() {
        return new Comment(0, TEXT, USER_ID, PARENT_ID, AUTHOR, TIMESTAMP);
    }

    protected CommitteeMember getSteeringCommitteeMember() {
        return new CommitteeMember(0, NAME, FACILITY, STEERING);
    }

    protected CommitteeMember getProgramCommitteeMember() {
        return new CommitteeMember(0, NAME, FACILITY, PROGRAM);
    }

    protected CommitteeMember getOrganizingCommitteeMember() {
        return new CommitteeMember(0, NAME, FACILITY, ORGANIZING);
    }

    protected KeynoteSpeaker getKeynoteSpeaker() {
        return new KeynoteSpeaker(0, NAME, FACILITY, MAIL, IMAGE, TITLE, ABSTRACT);
    }

    protected Topic getTopic() {
        return new Topic(0, PARENT_ID, TITLE, getListOfPeople(), TYPE);
    }

    protected Track getTrack() {
        return new Track(0, START_DATE, END_DATE, ROOM, TITLE, getListOfPeople());
    }

    protected User getUser() {
        return new User(USER_ID, MAIL, DISPLAY_NAME, Uri.parse(IMAGE), getSubscribedEvents());
    }

    protected Venue getVenue() {
        return new Venue(0, getInfoList(FACULTY), getInfoList(REGION), getInfoList(HOTEL));
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
}
