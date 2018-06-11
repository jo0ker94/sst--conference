package com.example.karlo.sstconference.viewmodel;

import android.arch.core.executor.testing.InstantTaskExecutorRule;

import com.example.karlo.sstconference.BaseTest;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;

import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;

public class BaseViewModelTest extends BaseTest {

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    @BeforeClass
    public static void before(){
        RxAndroidPlugins.reset();
        RxJavaPlugins.reset();
        RxJavaPlugins.setIoSchedulerHandler(scheduler -> Schedulers.trampoline());
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(schedulerCallable -> Schedulers.trampoline());
    }

    @AfterClass
    public static void after(){
        RxAndroidPlugins.reset();
        RxJavaPlugins.reset();
    }
}
