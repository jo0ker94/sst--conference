package com.example.karlo.sstconference.modules.committee;

import android.arch.lifecycle.ViewModelProvider;

import com.example.karlo.sstconference.database.committee.CommitteeDataSource;
import com.example.karlo.sstconference.servertasks.interfaces.Api;

import javax.inject.Inject;

public class CommitteeViewModelFactory implements ViewModelProvider.Factory {

    private final CommitteeDataSource mDataSource;

    @Inject
    public CommitteeViewModelFactory(CommitteeDataSource dataSource) {
        this.mDataSource = dataSource;
    }

    @Override
    public CommitteeViewModel create(Class modelClass) {
        return new CommitteeViewModel(mDataSource);
    }
}