package com.example.karlo.learningapplication.modules.search;

import com.example.karlo.learningapplication.commons.CommonView;
import com.example.karlo.learningapplication.models.wiki.WikiResult;

/**
 * Created by Karlo on 31.3.2018..
 */

public interface SearchView extends CommonView {
    void showNoResult();
    void showResult(WikiResult result);
}
