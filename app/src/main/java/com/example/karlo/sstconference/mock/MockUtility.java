package com.example.karlo.sstconference.mock;

import java.util.concurrent.atomic.AtomicBoolean;

public class MockUtility {

    private static AtomicBoolean sIsRunningTest;

    private MockUtility() { }

    public static synchronized boolean isRunningTest() {
        if (sIsRunningTest == null) {
            boolean istest;
            try {
                Class.forName("android.support.test.espresso.Espresso");
                istest = true;
            } catch (ClassNotFoundException e) {
                istest = false;
            }
            sIsRunningTest = new AtomicBoolean(istest);
        }
        return sIsRunningTest.get();
    }
}