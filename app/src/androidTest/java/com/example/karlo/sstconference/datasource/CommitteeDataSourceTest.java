package com.example.karlo.sstconference.datasource;

import com.example.karlo.sstconference.database.committee.CommitteeDao;
import com.example.karlo.sstconference.database.committee.LocalCommitteeDataSource;
import com.example.karlo.sstconference.models.committee.CommitteeMember;
import com.example.karlo.sstconference.servertasks.interfaces.Api;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Observable;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CommitteeDataSourceTest extends BaseDataSourceTest {

    @Mock
    private CommitteeDao dao;

    @Mock
    private Api api;

    @InjectMocks
    private LocalCommitteeDataSource dataSource;

    @Test
    public void testGetSaveAndDelete() {
        List<CommitteeMember> committeeMembers = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            committeeMembers.add(new CommitteeMember(i,
                    getStringFormat(NAME, i),
                    getStringFormat(FACILITY, i),
                    PROGRAM));
        }

        List<CommitteeMember> apiCommittee = new ArrayList<>(committeeMembers);
        CommitteeMember member = getProgramCommitteeMember(123);
        apiCommittee.add(member);

        when(dao.getProgramCommittee()).thenReturn(Maybe.just(committeeMembers));
        when(api.getCommittee(PROGRAM)).thenReturn(Observable.just(apiCommittee));

        dataSource.insertOrUpdateCommitteeMember(member);
        verify(dao).insertCommitteeMember(member);

        dataSource.deleteCommitteeMember(committeeMembers.get(0));
        verify(dao).deleteCommitteeMember(committeeMembers.get(0));

        dataSource.getProgramCommittee();
        verify(dao).getProgramCommittee();
        verify(api).getCommittee(PROGRAM);

        dataSource.getProgramCommittee()
                .flatMap(Observable::fromIterable)
                .distinct(CommitteeMember::getId)
                .toList()
                .subscribe(members -> {
                    for (int i = 0; i < members.size(); i++) {
                        assertEquals(members.get(i), apiCommittee.get(i));
                    }
                });
    }
}