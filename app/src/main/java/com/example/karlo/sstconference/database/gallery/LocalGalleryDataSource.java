package com.example.karlo.sstconference.database.gallery;

import com.example.karlo.sstconference.models.Image;
import com.example.karlo.sstconference.servertasks.interfaces.Api;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class LocalGalleryDataSource implements GalleryDataSource {

    private Api mApi;
    private GalleryDao mDao;

    @Inject
    public LocalGalleryDataSource(GalleryDao mDao, Api mApi) {
        this.mDao = mDao;
        this.mApi = mApi;
    }

    @Override
    public Observable<List<Image>> getImages() {
        return Observable.concat(getImagesFromDatabase(),
                getImagesFromApi().onErrorResumeNext(Observable.empty()));
    }

    private Observable<List<Image>> getImagesFromDatabase() {
        return mDao.getImages()
                .toObservable();
    }

    private Observable<List<Image>> getImagesFromApi() {
        return mApi.getImages()
                .flatMap(Observable::fromIterable)
                .subscribeOn(Schedulers.newThread())
                .doOnNext(this::insertOrUpdateImage)
                .toList()
                .toObservable();
    }

    @Override
    public Completable insertOrUpdateImage(Image image) {
        return mApi.pushImageToServer(String.valueOf(image.getId()), image)
                .doOnComplete(() -> mDao.insertImage(image));
    }

    @Override
    public void deleteImage(Image image) {
        mDao.deleteImage(image);
    }
}
