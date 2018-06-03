package com.example.karlo.sstconference.dao;

import com.example.karlo.sstconference.database.gallery.GalleryDao;
import com.example.karlo.sstconference.models.Image;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;

public class GalleryDaoTest extends BaseDaoTest {

    private GalleryDao mDao;

    @Before
    public void setDao() {
        mDao = localDatabase.galleryModel();
    }

    @Test
    public void insertAndGetOne() {
        mDao.insertImage(getImage());

        mDao.getImages()
                .toObservable()
                .flatMap(io.reactivex.Observable::fromIterable)
                .firstElement()
                .subscribe(image -> assertEquals(image.getImageUrl(), IMAGE));
    }

    @Test
    public void insertAndGetMany() {
        List<Image> images = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            Image image = new Image(i,
                    getStringFormat(IMAGE, i));

            images.add(image);
            mDao.insertImage(image);
        }

        mDao.getImages()
                .subscribe(imageList -> {
                    for (int i = 0; i < imageList.size(); i++) {
                        assertEquals(imageList.get(i).getImageUrl(), images.get(i).getImageUrl());
                    }
                });
    }

    @Test
    public void deleteItem() {
        Image image = getImage();
        mDao.insertImage(image);

        mDao.getImages()
                .toObservable()
                .flatMap(io.reactivex.Observable::fromIterable)
                .firstElement()
                .subscribe(responseImage -> assertEquals(responseImage.getImageUrl(), IMAGE));

        mDao.deleteImage(image);

        mDao.getImages()
                .toObservable()
                .flatMap(io.reactivex.Observable::fromIterable)
                .toList()
                .subscribe(images -> assertEquals(images.isEmpty(), true));
    }
}
