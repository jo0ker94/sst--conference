package com.example.karlo.sstconference.modules.chairs;

import com.example.karlo.sstconference.base.BaseView;
import com.example.karlo.sstconference.models.ConferenceChair;

import java.util.List;

public interface ChairsView extends BaseView {
    void showConferenceChairs(List<ConferenceChair> chairList);
}
