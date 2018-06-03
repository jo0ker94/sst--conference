package com.example.karlo.sstconference.dao;

import android.arch.persistence.room.Room;
import android.support.test.InstrumentationRegistry;

import com.example.karlo.sstconference.BaseTest;
import com.example.karlo.sstconference.database.LocalDatabase;

import org.junit.After;
import org.junit.Before;

public class BaseDaoTest extends BaseTest {

    protected LocalDatabase localDatabase;

    @Before
    public void initDb() {
        localDatabase = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(), LocalDatabase.class).build();
    }

    @After
    public void closeDb() {
        localDatabase.close();
    }
}
