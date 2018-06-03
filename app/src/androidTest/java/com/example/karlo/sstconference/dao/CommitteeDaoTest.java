package com.example.karlo.sstconference.dao;

import com.example.karlo.sstconference.database.committee.CommitteeDao;
import com.example.karlo.sstconference.models.committee.CommitteeMember;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;

public class CommitteeDaoTest extends BaseDaoTest {

    private CommitteeDao mDao;

    @Before
    public void setDao() {
        mDao = localDatabase.committeeModel();
    }

    @Test
    public void testInsertAndGetOne() {
        mDao.insertCommitteeMember(getSteeringCommitteeMember());
        mDao.insertCommitteeMember(getOrganizingCommitteeMember());
        mDao.insertCommitteeMember(getProgramCommitteeMember());

        mDao.getSteeringCommittee()
                .toObservable()
                .flatMap(io.reactivex.Observable::fromIterable)
                .firstElement()
                .subscribe(committee -> {
                    assertEquals(committee.getName(), NAME);
                    assertEquals(committee.getFacility(), FACILITY);
                    assertEquals(committee.getType(), STEERING);
                });

        mDao.getOrganizingCommittee()
                .toObservable()
                .flatMap(io.reactivex.Observable::fromIterable)
                .firstElement()
                .subscribe(committee -> {
                    assertEquals(committee.getName(), NAME);
                    assertEquals(committee.getFacility(), FACILITY);
                    assertEquals(committee.getType(), ORGANIZING);
                });

        mDao.getProgramCommittee()
                .toObservable()
                .flatMap(io.reactivex.Observable::fromIterable)
                .firstElement()
                .subscribe(committee -> {
                    assertEquals(committee.getName(), NAME);
                    assertEquals(committee.getFacility(), FACILITY);
                    assertEquals(committee.getType(), PROGRAM);
                });
    }

    @Test
    public void testInsertAndGetMany() {
        List<CommitteeMember> steeringMembers = new ArrayList<>();
        List<CommitteeMember> programMembers = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            CommitteeMember member = new CommitteeMember(i,
                    getStringFormat(NAME, i),
                    getStringFormat(FACILITY, i),
                    (i%2 == 0) ? STEERING : PROGRAM);

            if ((i%2 == 0)) {
                steeringMembers.add(member);
            } else {
                programMembers.add(member);
            }
            mDao.insertCommitteeMember(member);
        }

        mDao.getSteeringCommittee()
                .subscribe(committee -> {
                    for (int i = 0; i < committee.size(); i++) {
                        assertEquals(committee.get(i).getName(), steeringMembers.get(i).getName());
                        assertEquals(committee.get(i).getFacility(), steeringMembers.get(i).getFacility());
                        assertEquals(committee.get(i).getType(), steeringMembers.get(i).getType());
                    }
                });

        mDao.getProgramCommittee()
                .subscribe(committee -> {
                    for (int i = 0; i < committee.size(); i++) {
                        assertEquals(committee.get(i).getName(), programMembers.get(i).getName());
                        assertEquals(committee.get(i).getFacility(), programMembers.get(i).getFacility());
                        assertEquals(committee.get(i).getType(), programMembers.get(i).getType());
                    }
                });
    }

    @Test
    public void testDeleteItem() {
        CommitteeMember committeeMember = getProgramCommitteeMember();
        mDao.insertCommitteeMember(committeeMember);

        mDao.getProgramCommittee()
                .toObservable()
                .flatMap(io.reactivex.Observable::fromIterable)
                .firstElement()
                .subscribe(committee -> {
                    assertEquals(committee.getName(), NAME);
                    assertEquals(committee.getFacility(), FACILITY);
                    assertEquals(committee.getType(), PROGRAM);
                });

        mDao.deleteCommitteeMember(committeeMember);

        mDao.getProgramCommittee()
                .toObservable()
                .flatMap(io.reactivex.Observable::fromIterable)
                .toList()
                .subscribe(committeeMembers -> assertEquals(committeeMembers.isEmpty(), true));
    }
}
