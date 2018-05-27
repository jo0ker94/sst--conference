package com.example.karlo.sstconference.modules.program;

import com.example.karlo.sstconference.models.program.Topic;
import com.example.karlo.sstconference.models.program.Track;

public interface ProgramListener {
    void loadingData(boolean state);
    void showError(Throwable throwable);
    void switchFragment(ProgramActivity.FragmentType fragmentType, Track track);
    void showTopicDetails(Topic topic);
    void showTrackDetails(Track track);
}
