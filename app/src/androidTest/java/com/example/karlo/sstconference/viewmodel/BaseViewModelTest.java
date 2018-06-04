package com.example.karlo.sstconference.viewmodel;

import android.arch.core.executor.testing.InstantTaskExecutorRule;

import com.example.karlo.sstconference.BaseTest;

import org.junit.Rule;

public class BaseViewModelTest extends BaseTest {

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

}
