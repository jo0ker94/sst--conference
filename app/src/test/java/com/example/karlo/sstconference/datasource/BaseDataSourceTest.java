package com.example.karlo.sstconference.datasource;

import com.example.karlo.sstconference.BaseTest;

public abstract class BaseDataSourceTest extends BaseTest {

    abstract public void testGet();
    abstract public void testSave();
    abstract public void testDelete();

}
