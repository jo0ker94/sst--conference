package com.example.karlo.sstconference.datasource;

import com.example.karlo.sstconference.database.comment.CommentDao;
import com.example.karlo.sstconference.database.comment.LocalCommentDataSource;
import com.example.karlo.sstconference.models.program.Comment;
import com.example.karlo.sstconference.servertasks.interfaces.ProgramApi;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CommentDataSourceTest extends BaseDataSourceTest {

    @Mock
    private CommentDao dao;

    @Mock
    private ProgramApi api;

    @InjectMocks
    private LocalCommentDataSource dataSource;

    @Test
    public void testGetSaveAndDelete() {
        List<Comment> comments = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            comments.add(new Comment(i,
                    getStringFormat(TEXT, i),
                    getStringFormat(USER_ID, i),
                    PARENT_ID,
                    getStringFormat(AUTHOR, i),
                    getStringFormat(TIMESTAMP, i)));
        }

        List<Comment> apiComments = new ArrayList<>(comments);
        Comment comment = getComment(123);
        apiComments.add(comment);

        when(dao.getComments(PARENT_ID)).thenReturn(Maybe.just(comments));
        when(api.getComments(PARENT_ID)).thenReturn(Observable.just(apiComments));

        dataSource.insertOrUpdateComment(comment);
        verify(dao).insertComment(comment);

        dataSource.deleteComment(comments.get(0));
        verify(dao).deleteComment(comments.get(0));

        dataSource.getComments(PARENT_ID);
        verify(dao).getComments(PARENT_ID);
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
    public void testUpdateComments() {
        List<Comment> comments = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            comments.add(new Comment(i,
                    getStringFormat(TEXT, i),
                    getStringFormat(USER_ID, i),
                    PARENT_ID,
                    getStringFormat(AUTHOR, i),
                    getStringFormat(TIMESTAMP, i)));
        }

        when(api.updateComments(String.valueOf(12), comments)).thenReturn(Completable.complete());

        dataSource.updateComments(comments);
        verify(api).updateComments(String.valueOf(12), comments);
    }
}
