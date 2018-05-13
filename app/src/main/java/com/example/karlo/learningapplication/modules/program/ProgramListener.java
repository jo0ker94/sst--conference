package com.example.karlo.learningapplication.modules.program;

import com.example.karlo.learningapplication.models.program.Topic;

public interface ProgramListener {
    void loadingData(boolean state);
    void showError(Throwable throwable);
    void switchFragment(ProgramActivity.FragmentType fragmentType, int position);
    void showTopicDetails(Topic topic);
}
