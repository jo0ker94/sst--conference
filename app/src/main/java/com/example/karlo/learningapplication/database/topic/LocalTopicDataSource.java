package com.example.karlo.learningapplication.database.topic;

import com.example.karlo.learningapplication.models.program.Topic;
import com.example.karlo.learningapplication.servertasks.interfaces.ProgramApi;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class LocalTopicDataSource implements TopicDataSource {

    private TopicDao mTopicDao;
    private ProgramApi mApi;

    public LocalTopicDataSource(TopicDao mTopicDao, ProgramApi mApi) {
        this.mTopicDao = mTopicDao;
        this.mApi = mApi;
    }

    @Override
    public Observable<List<Topic>> getTopics() {
        return Observable.concat(getTopicsFromDatabase(),
                getTopicsFromApi().onErrorResumeNext(Observable.empty()));
    }

    private Observable<List<Topic>> getTopicsFromDatabase() {
        return mTopicDao.getTopics()
                .toObservable();
    }

    private Observable<List<Topic>> getTopicsFromApi() {
        return mApi.getTopics()
                .flatMap(Observable::fromIterable)
                .subscribeOn(Schedulers.newThread())
                .doOnNext(this::insertOrUpdateTopic)
                .toList()
                .toObservable();
    }

    @Override
    public void insertOrUpdateTopic(Topic topic) {
        mTopicDao.insertTopic(topic);
    }

    @Override
    public void deleteTopic(Topic topic) {
        mTopicDao.deleteTopic(topic);
    }
}
