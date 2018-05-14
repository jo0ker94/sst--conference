package com.example.karlo.learningapplication.helpers;

import com.example.karlo.learningapplication.models.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by Karlo on 25.3.2018..
 */

public class DatabaseHelper {

    private final static FirebaseDatabase database = FirebaseDatabase.getInstance();

    public static DatabaseReference getUserReference() {
        return database.getReference("users");
    }

    public static DatabaseReference getImagesReference() {
        return database.getReference("2017/images/");
    }

    public static DatabaseReference getCommentsReference() {
        return database.getReference("2017/comments/");
    }

    public static <T extends RealmObject> int generateId(Class<T> classType) {
        Realm realm = Realm.getDefaultInstance();
        Number number = realm.where(classType).max("id");
        int id;
        if (number == null) {
            id = 1;
        } else {
            id = number.intValue() + 1;
        }
        realm.close();
        return id;
    }

    private static <E extends RealmObject> List<E> getObjectCopies(Realm realm, RealmResults<E> realmObjects) {
        return realm.copyFromRealm(realmObjects);
    }

    public static <T extends RealmObject> void saveRealmObject(T object) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(object);
        realm.commitTransaction();
    }

    public static <T extends RealmObject> void saveRealmObjects(List<T> objects) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(objects);
        realm.commitTransaction();
        realm.close();
    }

    public static <T extends RealmObject> T getObject(Class<T> className) {
        Realm realm = Realm.getDefaultInstance();
        T result = realm.where(className).findFirst();
        T objectCopy = realm.copyFromRealm(result);
        realm.close();
        return objectCopy;
    }

    public static <T extends RealmObject> List<T> getAllObjects(Class<T> className) {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<T> clientResults = realm.where(className)
                .findAll();
        List<T> objectCopies = getObjectCopies(realm, clientResults);
        realm.close();
        return objectCopies;
    }

    public static void deleteUser(String id) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        User object = realm.where(User.class).equalTo("userId", id).findFirst();
        object.deleteFromRealm();
        realm.commitTransaction();
        realm.close();
    }

    public static void deleteDB() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.deleteAll();
        realm.commitTransaction();
        realm.close();
    }
}
