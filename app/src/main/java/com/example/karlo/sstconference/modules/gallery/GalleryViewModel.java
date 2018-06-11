package com.example.karlo.sstconference.modules.gallery;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;

import com.example.karlo.sstconference.R;
import com.example.karlo.sstconference.commons.Constants;
import com.example.karlo.sstconference.commons.Status;
import com.example.karlo.sstconference.database.gallery.GalleryDataSource;
import com.example.karlo.sstconference.helpers.DatabaseHelper;
import com.example.karlo.sstconference.models.Image;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class GalleryViewModel extends AndroidViewModel {

    private MutableLiveData<Status> mStatus = new MutableLiveData<>();
    private MutableLiveData<List<Image>> mImageUrl;
    private List<Image> mImages = new ArrayList<>();

    private StorageReference mStorageReference;
    private FirebaseDatabase mFirebaseDatabase;

    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    private GalleryDataSource mDataSource;

    public GalleryViewModel(@NonNull Application application) {
        super(application);
    }

    public void setDataSource(GalleryDataSource dataSource, FirebaseStorage storage, FirebaseDatabase firebaseDatabase) {
        this.mDataSource = dataSource;
        this.mStorageReference = storage.getReference();
        this.mFirebaseDatabase = firebaseDatabase;
    }

    private void downloadImages() {
        mCompositeDisposable.add(mDataSource
                .getImages()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(strings -> {
                            mImages.clear();
                            mImages.addAll(strings);
                            mImageUrl.setValue(strings);
                        },
                        throwable -> mStatus.setValue(Status.error(throwable.getMessage()))));
    }

    public LiveData<Status> getStatus() {
        return mStatus;
    }

    public LiveData<List<Image>> getImages() {
        if (mImageUrl == null) {
            mImageUrl = new MutableLiveData<>();
            downloadImages();
        }
        return mImageUrl;
    }

    public void uploadImage(Uri filePath) {
        if(filePath != null) {
            String name = UUID.randomUUID().toString();
            StorageReference ref = mStorageReference.child("images/" + name);

            Bitmap bmp = null;
            try {
                bmp = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            if (bmp != null) {
                bmp.compress(Bitmap.CompressFormat.JPEG, 30, baos);
            }
            UploadTask uploadTask = ref.putBytes(baos.toByteArray());
            uploadTask
                    .addOnSuccessListener(taskSnapshot -> {
                        mStatus.setValue(Status.message(getApplication().getResources().getString(R.string.uploaded)));
                        saveImageToServer(name);
                    })
                    .addOnFailureListener(e -> mStatus.setValue(Status.message(e.getMessage())))
                    .addOnProgressListener(taskSnapshot -> {
                        double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                .getTotalByteCount());
                        mStatus.setValue(Status.progress((int) progress));
                    });
        }
    }

    private void saveImageToServer(String name) {
        String path = String.format(getApplication().
                        getResources().getString(R.string.image_uri),
                Constants.FIREBASE_STORAGE_URL,
                Constants.IMAGES_ENDPOINT,
                name);

        Image image = new Image(mImages.size(), path);

        DatabaseHelper.getImagesReference(mFirebaseDatabase)
                .child(String.valueOf(mImages.size()))
                .setValue(image);

        mImages.add(image);
        mImageUrl.setValue(mImages);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mCompositeDisposable.clear();
    }
}
