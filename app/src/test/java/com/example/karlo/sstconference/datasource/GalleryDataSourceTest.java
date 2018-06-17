package com.example.karlo.sstconference.datasource;

import com.example.karlo.sstconference.database.gallery.GalleryDao;
import com.example.karlo.sstconference.database.gallery.LocalGalleryDataSource;
import com.example.karlo.sstconference.models.Image;
import com.example.karlo.sstconference.servertasks.interfaces.Api;
import com.google.gson.JsonElement;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static junit.framework.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GalleryDataSourceTest extends BaseDataSourceTest {

    @Mock
    private GalleryDao dao;

    @Mock
    private Api api;

    @InjectMocks
    private LocalGalleryDataSource dataSource;

    @Test
    public void testGetSaveAndDelete() {
        List<Image> images = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            images.add(new Image(i,
                    getStringFormat(IMAGE, i)));
        }

        List<Image> apiImages = new ArrayList<>(images);
        Image image = getImage(123);
        apiImages.add(image);

        when(dao.getImages()).thenReturn(Maybe.just(images));
        when(api.getImages()).thenReturn(Observable.just(apiImages));
        when(api.pushImageToServer("123", image)).thenReturn(Completable.complete());

        dataSource.insertOrUpdateImage(image);
        verify(api).pushImageToServer("123", image);

        dataSource.insertOrUpdateImage(image).subscribe(() ->
                verify(dao).insertImage(image));

        dataSource.deleteImage(images.get(0));
        verify(dao).deleteImage(images.get(0));

        dataSource.getImages();
        verify(dao).getImages();
        verify(api).getImages();

        dataSource.getImages()
                .flatMap(Observable::fromIterable)
                .distinct(Image::getId)
                .toList()
                .subscribe(imageList -> {
                    for (int i = 0; i < imageList.size(); i++) {
                        assertEquals(imageList.get(i), apiImages.get(i));
                    }
                });
    }
}