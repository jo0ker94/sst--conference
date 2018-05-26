package com.example.karlo.sstconference.models.converters;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class IntegerListConverter {

    public static final Gson gson = new Gson();

    @android.arch.persistence.room.TypeConverter
    public static List<Integer> stringToIntegerList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }
        Type listType = new TypeToken<List<Integer>>() {}.getType();
        return gson.fromJson(data, listType);
    }

    @android.arch.persistence.room.TypeConverter
    public static String integerListToString(List<Integer> personList) {
        return gson.toJson(personList);
    }
}
