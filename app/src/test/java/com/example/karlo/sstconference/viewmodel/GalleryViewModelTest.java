package com.example.karlo.sstconference.viewmodel;

import android.app.Application;
import android.arch.lifecycle.Observer;
import android.content.res.Resources;

import com.example.karlo.sstconference.R;
import com.example.karlo.sstconference.commons.Constants;
import com.example.karlo.sstconference.database.gallery.GalleryDataSource;
import com.example.karlo.sstconference.models.Image;
import com.example.karlo.sstconference.modules.gallery.GalleryViewModel;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;

import static junit.framework.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
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

    @Test
    public void testSaveImageToServer() {
        Observer observer = mock(Observer.class);
        Resources resources = Mockito.mock(Resources.class);
        when(dataSource.insertOrUpdateImage(any(Image.class))).thenReturn(Completable.complete());
        when(dataSource.getImages()).thenReturn(Observable.empty());
        when(application.getResources()).thenReturn(resources);
        when(resources.getString(R.string.image_uri)).thenReturn("%s%s%s?alt=media");
        when(application.getResources().getString(R.string.image_uri)).thenReturn("%s%s%s?alt=media");

        String expectedString = String.format(application.getResources().getString(R.string.image_uri), Constants.FIREBASE_STORAGE_URL,
                Constants.IMAGES_ENDPOINT, IMAGE);

        galleryViewModel.getImages().observeForever(observer);

        galleryViewModel.saveImageToServer(IMAGE);

        sleep(500);

        assertEquals(galleryViewModel.getImages().getValue().get(0).getImageUrl(), expectedString);
    }
}