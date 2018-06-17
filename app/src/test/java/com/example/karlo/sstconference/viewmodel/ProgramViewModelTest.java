package com.example.karlo.sstconference.viewmodel;

import android.arch.lifecycle.Observer;
import android.net.Uri;

import com.example.karlo.sstconference.commons.Status;
import com.example.karlo.sstconference.database.program.ProgramDataSource;
import com.example.karlo.sstconference.database.user.UserDataSource;
import com.example.karlo.sstconference.models.User;
import com.example.karlo.sstconference.models.program.Comment;
import com.example.karlo.sstconference.models.program.Topic;
import com.example.karlo.sstconference.models.program.Track;
import com.example.karlo.sstconference.modules.program.ProgramViewModel;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;

import static junit.framework.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ProgramViewModelTest extends BaseViewModelTest {

    @Mock
    private ProgramDataSource dataSource;

    @Mock
    private UserDataSource userDataSource;

    @InjectMocks
    private ProgramViewModel viewModel;

    @Test
    public void testGetUser() {
        User user = getUser();

        Observer observer = mock(Observer.class);

        when(userDataSource.getUser()).thenReturn(Maybe.just(user));

        viewModel.getUser().observeForever(observer);

        sleep(500);

        verify(observer).onChanged(user);
    }

    @Test
    public void testGetAllUsers() {
        Map<String, User> users = new HashMap<>();
        for (int i = 0; i < 15; i++) {
            users.put(getStringFormat(USER_ID, i),
                    new User(getStringFormat(USER_ID, i),
                            getStringFormat(MAIL, i),
                            getStringFormat(DISPLAY_NAME, i),
                            Uri.parse(getStringFormat(IMAGE.toString(), i)),
                            getSubscribedEvents()));
        }

        Observer observer = mock(Observer.class);

        when(userDataSource.getUsers()).thenReturn(Observable.just(users));

        viewModel.getUsers().observeForever(observer);

        sleep(500);

        verify(observer).onChanged(new ArrayList<>(users.values()));
    }

    @Test
    public void testGetTracks() {
        List<Track> tracks = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            tracks.add(new Track(i,
                    getStringFormat(START_DATE, i),
                    getStringFormat(END_DATE, i),
                    i,
                    getStringFormat(TITLE, i),
                    getListOfPeople()));
        }

        Observer observer = mock(Observer.class);

        when(dataSource.getTracks()).thenReturn(Observable.just(tracks));

        viewModel.getTracks().observeForever(observer);

        sleep(500);

        verify(observer).onChanged(tracks);

    }

    @Test
    public void testFetchTrack() {
        List<Track> tracks = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            tracks.add(new Track(i,
                    getStringFormat(START_DATE, i),
                    getStringFormat(END_DATE, i),
                    i,
                    getStringFormat(TITLE, i),
                    getListOfPeople()));
        }

        Observer observer = mock(Observer.class);

        when(dataSource.getTracks()).thenReturn(Observable.just(tracks));

        viewModel.getTracks().observeForever(observer);
        sleep(500);
        verify(observer).onChanged(tracks);

        viewModel.fetchTrack(5);

        sleep(1000);

        assertEquals(viewModel.getTracks().getValue().get(0), tracks.get(5));

    }

    @Test
    public void testGetTopics() {
        List<Topic> topics = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            topics.add(new Topic(i,
                    PARENT_ID,
                    getStringFormat(TITLE, i),
                    getListOfPeople(),
                    i));
        }

        Observer observer = mock(Observer.class);

        when(dataSource.getTopics()).thenReturn(Observable.just(topics));

        viewModel.getTopics().observeForever(observer);
        viewModel.fetchTopics(PARENT_ID);

        sleep(500);

        verify(observer).onChanged(topics);

    }

    @Test
    public void testDeleteTopicSubscription() {
        List<Topic> topics = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            topics.add(new Topic(i,
                    PARENT_ID,
                    getStringFormat(TITLE, i),
                    getListOfPeople(),
                    i));
        }
        setUpTopicDetails(topics);

        viewModel.deleteTopicSubscription(topics.get(1));

        sleep(1000);

        assertEquals(viewModel.getStatus().getValue().getMessage(), "Unsubscribed!");
    }

    @Test
    public void testDeleteTopicSubscriptionError() {
        List<Topic> topics = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            topics.add(new Topic(i,
                    PARENT_ID,
                    getStringFormat(TITLE, i),
                    getListOfPeople(),
                    i));
        }
        setUpTopicDetails(topics);

        viewModel.deleteTopicSubscription(topics.get(2));

        sleep(1000);

        assertEquals(viewModel.getStatus().getValue().getResponse(), Status.Response.ERROR);
    }

    @Test
    public void testTopicSubscription() {
        List<Topic> topics = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            topics.add(new Topic(i,
                    PARENT_ID,
                    getStringFormat(TITLE, i),
                    getListOfPeople(),
                    i));
        }
        setUpTopicDetails(topics);

        viewModel.subscribeToTopic(topics.get(2));

        sleep(1000);

        assertEquals(viewModel.getStatus().getValue().getMessage(), "Subscribed!");
    }

    @Test
    public void testTopicSubscriptionError() {
        List<Topic> topics = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            topics.add(new Topic(i,
                    PARENT_ID,
                    getStringFormat(TITLE, i),
                    getListOfPeople(),
                    i));
        }
        setUpTopicDetails(topics);

        viewModel.subscribeToTopic(topics.get(1));

        sleep(1000);

        assertEquals(viewModel.getStatus().getValue().getResponse(), Status.Response.ERROR);
    }

    @Test
    public void testAddComment() {
        List<Comment> comments = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            comments.add(new Comment(i,
                    getStringFormat(TEXT, i),
                    getStringFormat(USER_ID, i),
                    PARENT_ID,
                    getStringFormat(AUTHOR, i),
                    getStringFormat(TIMESTAMP, i)));
        }
        setUpGetComments(comments);
        Comment commentToAdd = getComment(21);
        comments.add(commentToAdd);

        when(dataSource.updateComments(comments)).thenReturn(Completable.complete());
        viewModel.addComment(commentToAdd)
        .subscribe(() -> verify(dataSource).insertOrUpdateComment(commentToAdd));
    }

    @Test
    public void testDeleteComment() {
        List<Comment> comments = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            comments.add(new Comment(i,
                    getStringFormat(TEXT, i),
                    getStringFormat(USER_ID, i),
                    PARENT_ID,
                    getStringFormat(AUTHOR, i),
                    getStringFormat(TIMESTAMP, i)));
        }
        setUpGetComments(comments);
        Comment commentToDelete = comments.get(5);
        comments.remove(commentToDelete);

        when(dataSource.updateComments(comments)).thenReturn(Completable.complete());
        viewModel.deleteComment(commentToDelete)
                .subscribe(() -> verify(dataSource).deleteComment(commentToDelete));
    }

    @Test
    public void testGetComments() {
        List<Comment> comments = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            comments.add(new Comment(i,
                    getStringFormat(TEXT, i),
                    getStringFormat(USER_ID, i),
                    PARENT_ID,
                    getStringFormat(AUTHOR, i),
                    getStringFormat(TIMESTAMP, i)));
        }
        setUpGetComments(comments);
    }

    private void setUpGetComments(List<Comment> comments) {
        Observer observer = mock(Observer.class);

        when(dataSource.getComments(PARENT_ID)).thenReturn(Observable.just(comments));

        viewModel.getComments().observeForever(observer);
        viewModel.fetchComments(PARENT_ID);

        sleep(500);

        verify(observer).onChanged(comments);
    }

    private void setUpTopicDetails(List<Topic> topics) {
        User user = getUser();

        Observer observer = mock(Observer.class);

        when(dataSource.getTopics()).thenReturn(Observable.just(topics));
        when(userDataSource.insertOrUpdateUser(any(User.class))).thenReturn(Completable.complete());
        when(userDataSource.getUser()).thenReturn(Maybe.just(user));

        viewModel.getUser().observeForever(observer);

        sleep(1000);

        verify(observer).onChanged(user);

        viewModel.getTopics().observeForever(observer);
        viewModel.fetchTopics(PARENT_ID);

        sleep(500);

        verify(observer).onChanged(topics);
    }
}