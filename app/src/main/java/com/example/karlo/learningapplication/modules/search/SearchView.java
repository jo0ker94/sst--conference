package com.example.karlo.learningapplication.modules.search;

import com.example.karlo.learningapplication.commons.BaseView;
import com.example.karlo.learningapplication.models.wiki.WikiResult;

/**
 * Created by Karlo on 31.3.2018..
 */

public interface SearchView extends BaseView {
    void showNoResult();
    void showResult(WikiResult result);
}
