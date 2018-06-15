package com.example.karlo.sstconference.viewmodel;

import android.app.Application;
import android.arch.lifecycle.Observer;

import com.example.karlo.sstconference.database.gallery.GalleryDataSource;
import com.example.karlo.sstconference.models.Image;
import com.example.karlo.sstconference.modules.gallery.GalleryViewModel;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GalleryViewModelTest extends BaseViewModelTest {

    @Mock
    private Application application;

    @Mock
    private FirebaseDatabase firebaseDatabase;

    @Mock
    private FirebaseStorage firebaseStorage;

    @Mock
    private GalleryDataSource dataSource;

    @InjectMocks
    private GalleryViewModel galleryViewModel;

    @Test
    public void testGetImages() {
        List<Image> links = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            links.add(new Image(i,
                    getStringFormat(IMAGE, i)));
        }
        Observer observer = mock(Observer.class);

        when(dataSource.getImages()).thenReturn(io.reactivex.Observable.just(links));

        galleryViewModel.getImages().observeForever(observer);

        sleep(500);

        verify(observer).onChanged(links);
    }
}