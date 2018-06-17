package com.example.karlo.sstconference.servertasks.interfaces;

import com.example.karlo.sstconference.models.ConferenceChair;
import com.example.karlo.sstconference.models.Image;
import com.example.karlo.sstconference.models.committee.CommitteeMember;
import com.example.karlo.sstconference.models.keynote.KeynoteSpeaker;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by Karlo on 26.3.2018..
 */

public interface Api {

    @GET("2017/conferenceChairs.json")
    Observable<List<ConferenceChair>> getChairs();

    @GET("2017/committee/{type}.json")
    Observable<List<CommitteeMember>> getCommittee(@Path("type") String type);

    @GET("2017/images.json")
    Observable<List<Image>> getImages();

    @GET("2017/keynoteSpeakers.json")
    Observable<List<KeynoteSpeaker>> getKeynoteSpeakers();

    @PUT("2017/images/{id}.json")
    Completable pushImageToServer(@Path("id") String id, @Body Image image);

}
