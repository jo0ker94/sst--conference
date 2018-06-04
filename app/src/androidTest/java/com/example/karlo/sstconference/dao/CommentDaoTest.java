package com.example.karlo.sstconference.dao;

import com.example.karlo.sstconference.database.comment.CommentDao;
import com.example.karlo.sstconference.models.program.Comment;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;

public class CommentDaoTest extends BaseDaoTest {

    private CommentDao mDao;

    @Before
    public void setDao() {
        mDao = localDatabase.commentModel();
    }

    @Test
    public void testInsertAndGetOne() {
        mDao.insertComment(getComment());

        mDao.getComments(PARENT_ID)
                .toObservable()
                .flatMap(io.reactivex.Observable::fromIterable)
                .firstElement()
                .subscribe(comment -> {
                    assertEquals(comment.getText(), TEXT);
                    assertEquals(comment.getParentId(), PARENT_ID);
                    assertEquals(comment.getAuthor(), AUTHOR);
                    assertEquals(comment.getTimestamp(), TIMESTAMP);
                });
    }

    @Test
    public void testInsertAndGetMany() {
        List<Comment> commentList = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            Comment comment = new Comment(i,
                    getStringFormat(TEXT, i),
                    getStringFormat(USER_ID, i),
                    PARENT_ID,
                    getStringFormat(AUTHOR, i),
                    getStringFormat(TIMESTAMP, i));

            commentList.add(comment);
            mDao.insertComment(comment);
        }

        mDao.getComments(PARENT_ID)
                .subscribe(comments -> {
                    for (int i = 0; i < comments.size(); i++) {
                        assertEquals(comments.get(i).getText(), commentList.get(i).getText());
                        assertEquals(comments.get(i).getId(), commentList.get(i).getId());
                        assertEquals(comments.get(i).getParentId(), commentList.get(i).getParentId());
                        assertEquals(comments.get(i).getAuthor(), commentList.get(i).getAuthor());
                        assertEquals(comments.get(i).getTimestamp(), commentList.get(i).getTimestamp());
                    }
                });
    }

    @Test
    public void testDeleteItem() {
        Comment comment = getComment();
        mDao.insertComment(comment);

        mDao.getComments(PARENT_ID)
                .toObservable()
                .flatMap(io.reactivex.Observable::fromIterable)
                .firstElement()
                .subscribe(responseComment -> {
                    assertEquals(responseComment.getText(), TEXT);
                    assertEquals(responseComment.getParentId(), PARENT_ID);
                    assertEquals(responseComment.getAuthor(), AUTHOR);
                    assertEquals(responseComment.getTimestamp(), TIMESTAMP);
                });

        mDao.deleteComment(comment);

        mDao.getComments(PARENT_ID)
                .toObservable()
                .flatMap(io.reactivex.Observable::fromIterable)
                .toList()
                .subscribe(comments -> assertEquals(comments.isEmpty(), true));
    }

    @Test
    public void testUpdateItem() {
        Comment comment = getComment();
        mDao.insertComment(comment);

        comment.setAuthor(NAME);
        mDao.insertComment(comment);

        mDao.getComments(PARENT_ID)
                .toObservable()
                .flatMap(io.reactivex.Observable::fromIterable)
                .firstElement()
                .subscribe(responseComment -> {
                    assertEquals(responseComment.getId(), 0);
                    assertEquals(responseComment.getText(), TEXT);
                    assertEquals(responseComment.getParentId(), PARENT_ID);
                    assertEquals(responseComment.getAuthor(), NAME);
                    assertEquals(responseComment.getTimestamp(), TIMESTAMP);
                });
    }
}