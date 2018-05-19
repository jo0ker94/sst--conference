package com.example.karlo.learningapplication.models.program.converters;

import android.arch.persistence.room.TypeConverter;

import com.example.karlo.learningapplication.models.program.Person;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class PersonConverter {

    public static final Gson gson = new Gson();

    @TypeConverter
    public static List<Person> stringToPersonList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }
        Type listType = new TypeToken<List<Person>>() {}.getType();
        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String personListToString(List<Person> personList) {
        return gson.toJson(personList);
    }
}
