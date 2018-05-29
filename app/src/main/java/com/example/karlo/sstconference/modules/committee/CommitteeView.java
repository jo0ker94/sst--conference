package com.example.karlo.sstconference.modules.committee;

import com.example.karlo.sstconference.base.BaseView;
import com.example.karlo.sstconference.models.committee.CommitteeMember;

import java.util.List;

public interface CommitteeView extends BaseView {
    void showProgramCommittee(List<CommitteeMember> committeeMembers);
    void showSteeringCommittee(List<CommitteeMember> committeeMembers);
    void showOrganizingCommittee(List<CommitteeMember> committeeMembers);
}
