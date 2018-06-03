package com.example.karlo.sstconference.datasource;

import com.example.karlo.sstconference.BaseTest;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

public abstract class BaseDataSourceTest extends BaseTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    abstract public void testGetSaveAndDelete();

}
