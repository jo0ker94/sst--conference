package com.example.karlo.sstconference.datasource;

import com.example.karlo.sstconference.database.comment.CommentDao;
import com.example.karlo.sstconference.database.comment.LocalCommentDataSource;
import com.example.karlo.sstconference.database.program.LocalProgramDataSource;
import com.example.karlo.sstconference.database.topic.LocalTopicDataSource;
import com.example.karlo.sstconference.database.topic.TopicDao;
import com.example.karlo.sstconference.database.track.LocalTrackDataSource;
import com.example.karlo.sstconference.database.track.TrackDao;
import com.example.karlo.sstconference.models.program.Comment;
import com.example.karlo.sstconference.models.program.Topic;
import com.example.karlo.sstconference.models.program.Track;
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

public class ProgramDataSourceTest extends BaseDataSourceTest {

    @Mock
    private TrackDao trackDao;

    @Mock
    private TopicDao topicDao;

    @Mock
    private CommentDao commentDao;

    @Mock
    private ProgramApi api;

    @InjectMocks
    private LocalTopicDataSource topicDataSource;

    @InjectMocks
    private LocalTrackDataSource trackDataSource;

    @InjectMocks
    private LocalCommentDataSource commentDataSource;

    @Test
    public void testGetSaveAndDeleteTracks() {

        List<Track> tracks = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            tracks.add(new Track(i,
                    getStringFormat(START_DATE, i),
                    getStringFormat(END_DATE, i),
                    i,
                    getStringFormat(TITLE, i),
                    getListOfPeople()));
        }

        LocalProgramDataSource dataSource =
                new LocalProgramDataSource(topicDataSource, trackDataSource, commentDataSource);

        List<Track> apiTracks = new ArrayList<>(tracks);
        Track track = getTrack(123);
        apiTracks.add(track);

        when(trackDao.getTracks()).thenReturn(Maybe.just(tracks));
        when(api.getTracks()).thenReturn(Observable.just(apiTracks));

        dataSource.insertOrUpdateTrack(track);
        verify(trackDao).insertTrack(track);

        dataSource.deleteTrack(tracks.get(0));
        verify(trackDao).deleteTrack(tracks.get(0));

        dataSource.getTracks();
        verify(trackDao).getTracks();
        verify(api).getTracks();

        dataSource.getTracks()
                .flatMap(Observable::fromIterable)
                .distinct(Track::getId)
                .toList()
                .subscribe(trackList -> {
                    for (int i = 0; i < trackList.size(); i++) {
                        assertEquals(trackList.get(i), apiTracks.get(i));
                    }
                });
    }

    @Test
    public void testGetSaveAndDeleteTopics() {
        List<Topic> topics = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            topics.add(new Topic(i,
                    PARENT_ID,
                    getStringFormat(TITLE, i),
                    getListOfPeople(),
                    i));
        }

        LocalProgramDataSource dataSource =
                new LocalProgramDataSource(topicDataSource, trackDataSource, commentDataSource);

        List<Topic> apiTopics = new ArrayList<>(topics);
        Topic topic = getTopic(123);
        apiTopics.add(topic);

        when(topicDao.getTopics()).thenReturn(Maybe.just(topics));
        when(api.getTopics()).thenReturn(Observable.just(apiTopics));

        dataSource.insertOrUpdateTopic(topic);
        verify(topicDao).insertTopic(topic);

        dataSource.deleteTopic(topics.get(0));
        verify(topicDao).deleteTopic(topics.get(0));

        dataSource.getTopics();
        verify(topicDao).getTopics();
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

    @Test
    public void testGetSaveAndDeleteComments() {
        List<Comment> comments = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            comments.add(new Comment(i,
                    getStringFormat(TEXT, i),
                    getStringFormat(USER_ID, i),
                    PARENT_ID,
                    getStringFormat(AUTHOR, i),
                    getStringFormat(TIMESTAMP, i)));
        }

        LocalProgramDataSource dataSource =
                new LocalProgramDataSource(topicDataSource, trackDataSource, commentDataSource);

        List<Comment> apiComments = new ArrayList<>(comments);
        Comment comment = getComment(123);
        apiComments.add(comment);

        when(commentDao.getComments(PARENT_ID)).thenReturn(Maybe.just(comments));
        when(api.getComments(PARENT_ID)).thenReturn(Observable.just(apiComments));

        dataSource.insertOrUpdateComment(comment);
        verify(commentDao).insertComment(comment);

        dataSource.deleteComment(comments.get(0));
        verify(commentDao).deleteComment(comments.get(0));

        dataSource.getComments(PARENT_ID);
        verify(commentDao).getComments(PARENT_ID);
        verify(api).getComments(PARENT_ID);

        dataSource.getComments(PARENT_ID)
                .flatMap(Observable::fromIterable)
                .distinct(Comment::getId)
                .toList()
                .subscribe(commentList -> {
                    for (int i = 0; i < commentList.size(); i++) {
                        assertEquals(commentList.get(i), apiComments.get(i));
                    }
                });
    }

    @Test
    public void testGet() {

    }

    @Test
    public void testSave() {

    }

    @Test
    public void testDelete() {

    }
}