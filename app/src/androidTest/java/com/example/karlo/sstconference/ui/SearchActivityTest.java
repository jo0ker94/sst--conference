package com.example.karlo.sstconference.ui;

import android.support.test.rule.ActivityTestRule;

import com.example.karlo.sstconference.BaseTest;
import com.example.karlo.sstconference.R;
import com.example.karlo.sstconference.modules.search.SearchActivity;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.core.AllOf.allOf;

public class SearchActivityTest extends BaseTest {

    @Rule
    public final ActivityTestRule<SearchActivity> mRule = new ActivityTestRule<>(SearchActivity.class);

    @Test
    public void testEverythingIsDisplayed() {
        sleep(1000);
        checkIfRecyclerViewItemHasText(R.id.searchListView, 0, "Temporal");
        onView(withId(R.id.searchListView)).perform(scrollToPosition(50));
        checkIfRecyclerViewItemHasText(R.id.searchListView, 46, "Induction Motor");
    }

    @Test
    public void testSearch() {
        sleep(2000);
        onView(withContentDescription(R.string.search)).perform(click());

        onView(allOf(withId(R.id.search_edit_text), isDescendantOfA(withId(R.id.search_bar))))
                .perform(replaceText("Petri"), closeSoftKeyboard());
        checkIfRecyclerViewItemHasText(R.id.searchListView, 0, "Petri Net Modelling");


        onView(allOf(withId(R.id.search_edit_text), isDescendantOfA(withId(R.id.search_bar))))
                .perform(replaceText("Detection"), closeSoftKeyboard());
        checkIfRecyclerViewItemHasText(R.id.searchListView, 0, "Detection of Faults");
        checkIfRecyclerViewItemHasText(R.id.searchListView, 1, "Spam Detection Based");
        checkIfRecyclerViewItemHasText(R.id.searchListView, 2, "Real-time Audio and Video Artifacts");
    }

    @Test
    public void testClickOnItem() {
        sleep(1000);
        onView(withContentDescription(R.string.search)).perform(click());

        onView(allOf(withId(R.id.search_edit_text), isDescendantOfA(withId(R.id.search_bar))))
                .perform(replaceText("Petri"), closeSoftKeyboard());
        checkIfRecyclerViewItemHasText(R.id.searchListView, 0, "Petri Net Modelling");
        getRecyclerViewItem(R.id.searchListView, 0).perform(click());

        sleep(1000);
        onView(allOf(withId(R.id.topic_title), isDescendantOfA(withId(R.id.topic_container))))
                .check(matches(withText(containsString("Petri"))));
        onView(allOf(withId(R.id.topic_lecturers), isDescendantOfA(withId(R.id.topic_container))))
                .check(matches(withText(containsString("Hanife"))));
    }
}
