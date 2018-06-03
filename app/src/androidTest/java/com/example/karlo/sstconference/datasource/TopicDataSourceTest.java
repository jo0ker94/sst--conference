package com.example.karlo.sstconference.datasource;

import com.example.karlo.sstconference.database.topic.LocalTopicDataSource;
import com.example.karlo.sstconference.database.topic.TopicDao;
import com.example.karlo.sstconference.models.program.Topic;
import com.example.karlo.sstconference.servertasks.interfaces.ProgramApi;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Observable;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TopicDataSourceTest extends BaseDataSourceTest {

    @Mock
    private TopicDao dao;

    @Mock
    private ProgramApi api;

    @InjectMocks
    private LocalTopicDataSource dataSource;

    @Test
    public void testGetSaveAndDelete() {
        List<Topic> topics = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            topics.add(new Topic(i,
                    PARENT_ID,
                    getStringFormat(TITLE, i),
                    getListOfPeople(),
                    i));
        }

        List<Topic> apiTopics = new ArrayList<>(topics);
        Topic topic = getTopic(123);
        apiTopics.add(topic);

        when(dao.getTopics()).thenReturn(Maybe.just(topics));
        when(api.getTopics()).thenReturn(Observable.just(apiTopics));

        dataSource.insertOrUpdateTopic(topic);
        verify(dao).insertTopic(topic);

        dataSource.deleteTopic(topics.get(0));
        verify(dao).deleteTopic(topics.get(0));

        dataSource.getTopics();
        verify(dao).getTopics();
        verify(api).getTopics();

        dataSource.getTopics()
                .flatMap(Observable::fromIterable)
                .distinct(Topic::getId)
                .toList()
                .subscribe(topicList -> {
                    for (int i = 0; i < topicList.size(); i++) {
                        assertEquals(topicList.get(i), apiTopics.get(i));
                    }
                });
    }
}