package com.example.karlo.sstconference.modules.keynotespeakers;

import com.example.karlo.sstconference.base.BaseView;
import com.example.karlo.sstconference.models.keynote.KeynoteSpeaker;

import java.util.List;

public interface KeynoteView extends BaseView {
    void showKeynoteSpeakers(List<KeynoteSpeaker> keynoteSpeakers);
}
