package com.example.karlo.sstconference.database.gallery;

import com.example.karlo.sstconference.models.Image;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;

public interface GalleryDataSource {

    Observable<List<Image>> getImages();

    Completable insertOrUpdateImage(Image image);

    void deleteImage(Image image);
}
