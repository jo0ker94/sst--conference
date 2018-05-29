package com.example.karlo.sstconference.servertasks.interfaces;

import com.example.karlo.sstconference.models.ConferenceChair;
import com.example.karlo.sstconference.models.program.Comment;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Karlo on 26.3.2018..
 */

public interface Api {

    @GET("strings.json")
    Observable<List<String>> getStrings();

    @GET("2017/conferenceChairs.json")
    Observable<List<ConferenceChair>> getChairs();

    @GET("2017/images.json")
    Observable<List<String>> getImages();

    @GET("2017/comments/{id}.json")
    Observable<List<Comment>> getComments(@Path("id") int id);

}
