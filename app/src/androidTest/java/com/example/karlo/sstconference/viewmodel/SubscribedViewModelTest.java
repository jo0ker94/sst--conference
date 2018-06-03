package com.example.karlo.sstconference.viewmodel;

import android.arch.lifecycle.Observer;

import com.example.karlo.sstconference.database.program.ProgramDataSource;
import com.example.karlo.sstconference.database.user.UserDataSource;
import com.example.karlo.sstconference.models.User;
import com.example.karlo.sstconference.models.program.Topic;
import com.example.karlo.sstconference.modules.subscribed.SubscriptionViewModel;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SubscribedViewModelTest extends BaseViewModelTest {

    @Mock
    private ProgramDataSource dataSource;

    @Mock
    private UserDataSource userDataSource;

    @InjectMocks
    private SubscriptionViewModel viewModel;

    @Test
    public void testGetSubscribedTopics() {
        User user = getUser();

        List<Topic> topics = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            topics.add(new Topic(i,
                    PARENT_ID,
                    getStringFormat(TITLE, i),
                    getListOfPeople(),
                    i));
        }

        Observer observer = mock(Observer.class);

        when(userDataSource.getUser()).thenReturn(Maybe.just(user));
        when(dataSource.getTopics()).thenReturn(Observable.just(topics));

        viewModel.getSubscribedTopics().observeForever(observer);
        viewModel.getUserAndFetchEvents();

        List<Topic> subscribed = new ArrayList<>();
        for (int i = 0; i < topics.size(); i++) {
            if (user.getSubscribedEvents().contains(topics.get(i).getId())) {
                subscribed.add(topics.get(i));
            }
        }

        sleep(500);

        verify(observer).onChanged(subscribed);
    }


    @Test
    public void testDeleteSubscribedTopic() {
        User user = getUser();

        List<Topic> topics = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            topics.add(new Topic(i,
                    PARENT_ID,
                    getStringFormat(TITLE, i),
                    getListOfPeople(),
                    i));
        }

        Observer observer = mock(Observer.class);

        when(userDataSource.getUser()).thenReturn(Maybe.just(user));
        when(userDataSource.insertOrUpdateUser(user)).thenReturn(Completable.complete());
        when(dataSource.getTopics()).thenReturn(Observable.just(topics));

        viewModel.getSubscribedTopics().observeForever(observer);
        viewModel.getUserAndFetchEvents();

        List<Topic> subscribed = new ArrayList<>();
        Topic topicToDelete = null;
        for (int i = 0; i < topics.size(); i++) {
            if (user.getSubscribedEvents().contains(topics.get(i).getId())) {
                subscribed.add(topics.get(i));
                if (topics.get(i).getId() == 12) {
                    topicToDelete = topics.get(i);
                }
            }
        }

        sleep(500);

        verify(observer).onChanged(subscribed);

        viewModel.deleteTopicSubscription(topicToDelete);
        subscribed.remove(topicToDelete);
        viewModel.getUserAndFetchEvents();

        sleep(500);

        verify(observer).onChanged(subscribed);
    }
}