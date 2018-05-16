package com.example.karlo.learningapplication.modules.program;

import com.example.karlo.learningapplication.models.program.Topic;
import com.example.karlo.learningapplication.models.program.Track;

public interface ProgramListener {
    void loadingData(boolean state);
    void showError(Throwable throwable);
    void switchFragment(ProgramActivity.FragmentType fragmentType, Track track);
    void showTopicDetails(Topic topic, boolean forward);
}
