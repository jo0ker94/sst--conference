package com.example.karlo.sstconference.servertasks.interfaces;

import com.example.karlo.sstconference.models.program.Comment;
import com.example.karlo.sstconference.models.program.Topic;
import com.example.karlo.sstconference.models.program.Track;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ProgramApi {

    @GET("2017/conferenceProgram/tracks.json")
    io.reactivex.Observable<List<Track>> getTracks();

    @GET("2017/conferenceProgram/topics.json")
    io.reactivex.Observable<List<Topic>> getTopics();

    @GET("2017/comments/{id}.json")
    io.reactivex.Observable<List<Comment>> getComments(@Path("id") int id);

}
