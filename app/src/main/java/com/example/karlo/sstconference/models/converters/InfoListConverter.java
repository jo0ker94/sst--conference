package com.example.karlo.sstconference.models.converters;

import android.arch.persistence.room.TypeConverter;

import com.example.karlo.sstconference.models.venue.Info;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class InfoListConverter {

    public static final Gson gson = new Gson();

    @TypeConverter
    public static List<Info> stringToInfoList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }
        Type listType = new TypeToken<List<Info>>() {}.getType();
        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String infoListToString(List<Info> personList) {
        return gson.toJson(personList);
    }
}
