package com.example.karlo.sstconference.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.karlo.sstconference.database.chairs.ChairsDao;
import com.example.karlo.sstconference.database.comment.CommentDao;
import com.example.karlo.sstconference.database.committee.CommitteeDao;
import com.example.karlo.sstconference.database.gallery.GalleryDao;
import com.example.karlo.sstconference.database.keynote.KeynoteDao;
import com.example.karlo.sstconference.database.topic.TopicDao;
import com.example.karlo.sstconference.database.track.TrackDao;
import com.example.karlo.sstconference.database.user.UserDao;
import com.example.karlo.sstconference.database.venue.VenueDao;
import com.example.karlo.sstconference.models.ConferenceChair;
import com.example.karlo.sstconference.models.Image;
import com.example.karlo.sstconference.models.User;
import com.example.karlo.sstconference.models.committee.CommitteeMember;
import com.example.karlo.sstconference.models.keynote.KeynoteSpeaker;
import com.example.karlo.sstconference.models.program.Comment;
import com.example.karlo.sstconference.models.program.Topic;
import com.example.karlo.sstconference.models.program.Track;
import com.example.karlo.sstconference.models.venue.Venue;

@Database(entities = {
        User.class,
        Topic.class,
        Track.class,
        Comment.class,
        Venue.class,
        CommitteeMember.class,
        ConferenceChair.class,
        KeynoteSpeaker.class,
        Image.class
}, version = 1, exportSchema = false)
public abstract class LocalDatabase extends RoomDatabase {

    private static LocalDatabase INSTANCE;

    public static LocalDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            synchronized (LocalDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            LocalDatabase.class, "sst_database.db")
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static void destroyInstance() {
        INSTANCE = null;
    }

    public abstract UserDao userModel();

    public abstract TopicDao topicModel();

    public abstract TrackDao trackModel();

    public abstract CommentDao commentModel();

    public abstract VenueDao venueModel();

    public abstract CommitteeDao committeeModel();

    public abstract ChairsDao chairsModel();

    public abstract KeynoteDao keynoteModel();

    public abstract GalleryDao galleryModel();
}
