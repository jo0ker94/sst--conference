package com.example.karlo.sstconference.utility;

import android.net.Uri;

import com.example.karlo.sstconference.models.ConferenceChair;
import com.example.karlo.sstconference.models.Image;
import com.example.karlo.sstconference.models.User;
import com.example.karlo.sstconference.models.committee.CommitteeMember;
import com.example.karlo.sstconference.models.keynote.KeynoteSpeaker;
import com.example.karlo.sstconference.models.nearbyplaces.Geometry;
import com.example.karlo.sstconference.models.nearbyplaces.LocationCoordinates;
import com.example.karlo.sstconference.models.nearbyplaces.NearbyPlaces;
import com.example.karlo.sstconference.models.nearbyplaces.Result;
import com.example.karlo.sstconference.models.program.Comment;
import com.example.karlo.sstconference.models.program.Person;
import com.example.karlo.sstconference.models.program.Topic;
import com.example.karlo.sstconference.models.program.Track;
import com.example.karlo.sstconference.models.venue.Info;
import com.example.karlo.sstconference.models.venue.Venue;
import com.example.karlo.sstconference.models.venue.VenueMarker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class MockObject {

    protected static String FORMAT = "%s_%d";

    protected static String NAME = "Some Name";
    protected static String TITLE = "Title";
    protected static String MAIL = "mail@email.com";
    protected static String FACILITY = "Some Facility";
    protected static String IMAGE = "https://cdn0.iconfinder.com/data/icons/PRACTIKA/256/user.png";
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

    protected static String START_DATE = "2017-10-18T11:25:00+02:00";
    protected static String END_DATE = "2017-10-18T14:25:00+02:00";

    protected static int PARENT_ID = 12;
    protected static int ROOM = 0;
    protected static int TYPE = 0;

    protected static double LAT = 45.56214079999999;
    protected static double LNG = 18.67980280000006;

    protected ConferenceChair getConferenceChair() {
        return getConferenceChair(0);
    }

    protected String getStringFormat(String field, int position) {
        return String.format(FORMAT, field, position);
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
        return getSteeringCommitteeMember(0);
    }

    protected CommitteeMember getSteeringCommitteeMember(int id) {
        return new CommitteeMember(id, NAME, FACILITY, STEERING);
    }

    protected CommitteeMember getProgramCommitteeMember() {
        return getProgramCommitteeMember(0);
    }

    protected CommitteeMember getProgramCommitteeMember(int id) {
        return new CommitteeMember(id, NAME, FACILITY, PROGRAM);
    }

    protected CommitteeMember getOrganizingCommitteeMember() {
        return getOrganizingCommitteeMember(0);
    }

    protected CommitteeMember getOrganizingCommitteeMember(int id) {
        return new CommitteeMember(id, NAME, FACILITY, ORGANIZING);
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

    protected List<Topic> getTopics(int count) {
        List<Topic> topics = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            topics.add(new Topic(i,
                    PARENT_ID,
                    getStringFormat(TITLE, i),
                    getListOfPeople(),
                    i));
        }
        return topics;
    }

    protected List<KeynoteSpeaker> getKeynoteSpeakers(int count) {
        List<KeynoteSpeaker> speakers = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            speakers.add(new KeynoteSpeaker(i,
                    getStringFormat(NAME, i),
                    getStringFormat(FACILITY, i),
                    getStringFormat(MAIL, i),
                    getStringFormat(IMAGE, i),
                    getStringFormat(TITLE, i),
                    getStringFormat(ABSTRACT, i)));
        }
        return speakers;
    }

    protected List<ConferenceChair> getConferenceChairs(int count) {
        List<ConferenceChair> conferenceChairs = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            conferenceChairs.add(new ConferenceChair(i,
                    getStringFormat(TITLE, i),
                    getStringFormat(MAIL, i),
                    getStringFormat(FACILITY, i),
                    getStringFormat(IMAGE, i),
                    getStringFormat(NAME, i),
                    getStringFormat(NUMBER, i)));
        }
        return conferenceChairs;
    }

    protected List<CommitteeMember> getCommitteeMembers(String type, int count) {
        List<CommitteeMember> committeeMembers = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            committeeMembers.add(new CommitteeMember(i,
                    getStringFormat(NAME, i),
                    getStringFormat(FACILITY, i),
                    type));
        }
        return committeeMembers;
    }

    protected List<Image> getImages(int count) {
        List<Image> images = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            images.add(new Image(i,
                    getStringFormat(IMAGE, i)));
        }
        return images;
    }

    protected List<Track> getTracks(int count) {
        List<Track> tracks = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            tracks.add(new Track(i,
                    getStringFormat(START_DATE, i),
                    getStringFormat(END_DATE, i),
                    i,
                    getStringFormat(TITLE, i),
                    getListOfPeople()));
        }
        return tracks;
    }

    protected List<Comment> getComments(int count) {
        List<Comment> comments = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            comments.add(new Comment(i,
                    getStringFormat(TEXT, i),
                    getStringFormat(USER_ID, i),
                    PARENT_ID,
                    getStringFormat(AUTHOR, i),
                    getStringFormat(TIMESTAMP, i)));
        }
        return comments;
    }

    protected List<User> getUsers(int count) {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            users.add(new User(getStringFormat(USER_ID, i),
                    getStringFormat(MAIL, i),
                    getStringFormat(DISPLAY_NAME, i),
                    Uri.parse(getStringFormat(IMAGE, i)),
                    getSubscribedEvents()));
        }
        return users;
    }

    protected List<MarkerOptions> getMarkersOptions(int count) {
        List<MarkerOptions> markers = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            markers.add(new MarkerOptions()
                    .position(new LatLng(LAT, LNG))
                    .title(getStringFormat(TITLE, i)));
        }
        return markers;
    }

    protected NearbyPlaces getNearbyPlaces(String type) {
        List<Result> results = new ArrayList<>();
        NearbyPlaces nearbyPlaces = new NearbyPlaces();
        Result result = new Result();
        result.setName(type);
        result.setVicinity("vicinity");
        Geometry geometry = new Geometry();
        LocationCoordinates locationCoordinates = new LocationCoordinates();
        locationCoordinates.setLat(LAT);
        locationCoordinates.setLng(LNG);
        geometry.setLocationCoordinates(locationCoordinates);
        result.setGeometry(geometry);
        results.add(result);
        nearbyPlaces.setResults(results);
        return nearbyPlaces;
    }
}
