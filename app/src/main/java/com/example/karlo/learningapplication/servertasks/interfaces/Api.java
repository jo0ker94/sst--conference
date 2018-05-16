package com.example.karlo.learningapplication.servertasks.interfaces;

import com.example.karlo.learningapplication.models.ConferenceChair;
import com.example.karlo.learningapplication.models.User;
import com.example.karlo.learningapplication.models.program.Comment;
import com.example.karlo.learningapplication.models.program.Topic;
import com.example.karlo.learningapplication.models.program.Track;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Karlo on 26.3.2018..
 */

public interface Api {

    @GET("strings.json")
    io.reactivex.Observable<List<String>> getStrings();

    @GET("users/{id}/displayName.json")
    io.reactivex.Observable<String> getDisplayName(@Path("id") String id);

    @GET("users.json")
    io.reactivex.Observable<Map<String, User>> getUsers();

    @GET("2017/conferenceChairs.json")
    io.reactivex.Observable<List<ConferenceChair>> getChairs();

    @GET("2017/images.json")
    io.reactivex.Observable<List<String>> getImages();

    @GET("2017/conferenceProgram/tracks.json")
    io.reactivex.Observable<List<Track>> getTracks();

    @GET("2017/conferenceProgram/topics.json")
    io.reactivex.Observable<List<Topic>> getTopics();

    @GET("2017/comments/{id}.json")
    io.reactivex.Observable<List<Comment>> getComments(@Path("id") int id);

}
