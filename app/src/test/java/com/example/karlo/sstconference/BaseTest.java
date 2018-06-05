package com.example.karlo.sstconference;

import com.example.karlo.sstconference.utility.MockObject;

import org.junit.Rule;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

public class BaseTest extends MockObject {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    protected void sleep(int length) {
        try {
            Thread.sleep(length);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
