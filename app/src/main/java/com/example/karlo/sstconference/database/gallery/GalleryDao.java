package com.example.karlo.sstconference.database.gallery;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.karlo.sstconference.models.Image;

import java.util.List;

import io.reactivex.Maybe;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface GalleryDao {

    @Query("SELECT * FROM Image")
    Maybe<List<Image>> getImages();

    @Insert(onConflict = REPLACE)
    long insertImage(Image image);

    @Delete
    void deleteImage(Image image);
}
