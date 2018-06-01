package com.example.karlo.sstconference.viewmodel;

import android.arch.core.executor.testing.InstantTaskExecutorRule;

import org.junit.Rule;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

public class BaseViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

}
