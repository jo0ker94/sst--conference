package com.example.karlo.sstconference.viewmodel;

import android.arch.lifecycle.Observer;

import com.example.karlo.sstconference.database.committee.CommitteeDataSource;
import com.example.karlo.sstconference.models.committee.CommitteeMember;
import com.example.karlo.sstconference.modules.committee.CommitteeViewModel;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CommitteeViewModelTest extends BaseViewModelTest {

    @Mock
    private CommitteeDataSource dataSource;

    @InjectMocks
    private CommitteeViewModel chairsViewModel;

    @Test
    public void testGetOrganizing() {
        List<CommitteeMember> committeeMembers = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            committeeMembers.add(new CommitteeMember(i,
                            getStringFormat(NAME, i),
                            getStringFormat(FACILITY, i),
                            ORGANIZING));
        }
        Observer observer = mock(Observer.class);

        when(dataSource.getOrganizingCommittee()).thenReturn(io.reactivex.Observable.just(committeeMembers));

        chairsViewModel.getOrganizing().observeForever(observer);

        sleep(500);

        verify(observer).onChanged(committeeMembers);
    }

    @Test
    public void testGetProgram() {
        List<CommitteeMember> committeeMembers = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            committeeMembers.add(new CommitteeMember(i,
                    getStringFormat(NAME, i),
                    getStringFormat(FACILITY, i),
                    PROGRAM));
        }
        Observer observer = mock(Observer.class);

        when(dataSource.getProgramCommittee()).thenReturn(io.reactivex.Observable.just(committeeMembers));

        chairsViewModel.getProgram().observeForever(observer);

        sleep(500);

        verify(observer).onChanged(committeeMembers);
    }

    @Test
    public void testGetSteering() {
        List<CommitteeMember> committeeMembers = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            committeeMembers.add(new CommitteeMember(i,
                    getStringFormat(NAME, i),
                    getStringFormat(FACILITY, i),
                    STEERING));
        }
        Observer observer = mock(Observer.class);

        when(dataSource.getSteeringCommittee()).thenReturn(io.reactivex.Observable.just(committeeMembers));

        chairsViewModel.getSteering().observeForever(observer);

        sleep(500);

        verify(observer).onChanged(committeeMembers);
    }
}