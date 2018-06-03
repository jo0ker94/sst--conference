package com.example.karlo.sstconference.dao;

import com.example.karlo.sstconference.database.topic.TopicDao;
import com.example.karlo.sstconference.models.program.Topic;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;

public class TopicDaoTest extends BaseDaoTest {

    private TopicDao mDao;

    @Before
    public void setDao() {
        mDao = localDatabase.topicModel();
    }

    @Test
    public void testInsertAndGetOne() {
        mDao.insertTopic(getTopic());

        mDao.getTopics()
                .toObservable()
                .flatMap(io.reactivex.Observable::fromIterable)
                .firstElement()
                .subscribe(topic -> {
                    assertEquals(topic.getTitle(), TITLE);
                    assertEquals(topic.getParentId(), PARENT_ID);
                    assertEquals(topic.getType(), TYPE);
                });
    }

    @Test
    public void testInsertAndGetMany() {
        List<Topic> topics = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            Topic topic = new Topic(i,
                    PARENT_ID,
                    getStringFormat(TITLE, i),
                    getListOfPeople(),
                    i);

            topics.add(topic);
            mDao.insertTopic(topic);
        }

        mDao.getTopics()
                .subscribe(topicList -> {
                    for (int i = 0; i < topicList.size(); i++) {
                        assertEquals(topicList.get(i).getTitle(), topics.get(i).getTitle());
                        assertEquals(topicList.get(i).getParentId(), topics.get(i).getParentId());
                        assertEquals(topicList.get(i).getLecturers()
                                        .get(2).getName(),
                                topics.get(i).getLecturers()
                                        .get(2).getName());
                        assertEquals(topicList.get(i).getType(), topics.get(i).getType());
                    }
                });
    }

    @Test
    public void testDeleteItem() {
        Topic topic = getTopic();
        mDao.insertTopic(topic);

        mDao.getTopics()
                .toObservable()
                .flatMap(io.reactivex.Observable::fromIterable)
                .firstElement()
                .subscribe(responseTopic -> {
                    assertEquals(topic.getTitle(), TITLE);
                    assertEquals(topic.getParentId(), PARENT_ID);
                    assertEquals(topic.getType(), TYPE);
                });

        mDao.deleteTopic(topic);

        mDao.getTopics()
                .toObservable()
                .flatMap(io.reactivex.Observable::fromIterable)
                .toList()
                .subscribe(topics -> assertEquals(topics.isEmpty(), true));
    }
}
