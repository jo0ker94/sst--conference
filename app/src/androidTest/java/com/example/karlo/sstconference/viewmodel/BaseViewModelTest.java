package com.example.karlo.sstconference.viewmodel;

import android.arch.core.executor.testing.InstantTaskExecutorRule;

import com.example.karlo.sstconference.BaseTest;

import org.junit.Rule;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

public class BaseViewModelTest extends BaseTest {

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

}
