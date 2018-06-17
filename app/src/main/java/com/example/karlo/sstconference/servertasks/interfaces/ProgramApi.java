package com.example.karlo.sstconference.servertasks.interfaces;

import com.example.karlo.sstconference.models.program.Comment;
import com.example.karlo.sstconference.models.program.Topic;
import com.example.karlo.sstconference.models.program.Track;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ProgramApi {

    @GET("2017/conferenceProgram/tracks.json")
    Observable<List<Track>> getTracks();

    @GET("2017/conferenceProgram/topics.json")
    Observable<List<Topic>> getTopics();

    @GET("2017/comments/{id}.json")
    Observable<List<Comment>> getComments(@Path("id") int id);

    @PUT("2017/comments/{parentId}.json")
    Completable updateComments(@Path("parentId") String parentId, @Body List<Comment> comment);

}
