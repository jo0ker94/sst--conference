package com.example.karlo.sstconference.ui;

import android.support.test.rule.ActivityTestRule;

import com.example.karlo.sstconference.BaseTest;
import com.example.karlo.sstconference.R;
import com.example.karlo.sstconference.modules.settings.SettingsActivity;

import net.globulus.easyprefs.EasyPrefs;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertEquals;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.core.IsNot.not;

public class SettingsActivityTest extends BaseTest {

    @Rule
    public final ActivityTestRule<SettingsActivity> mRule = new ActivityTestRule<>(SettingsActivity.class);

    @After
    public void clearData() {
        clearDatabaseAndPrefs();
    }

    @Test
    public void testIfOptionsAreDisplayedAndDefault() {
        onView(withId(R.id.show_notifications)).check(matches(isDisplayed()));
        onView(withId(R.id.change_map_radius)).check(matches(isDisplayed()));

        onView(allOf(withId(R.id.button_switch), isDescendantOfA(withId(R.id.show_notifications))))
                .check(matches(isChecked()));

        onView(withId(R.id.change_map_radius)).perform(click());

        onView(withText(String.format(getString(R.string.search_radius), 2000))).check(matches(isDisplayed()));

        assertEquals(EasyPrefs.getShowNotifications(mRule.getActivity()), true);
        assertEquals(EasyPrefs.getMapRadius(mRule.getActivity()), 2000);
    }

    @Test
    public void testChangeSettings() {
        onView(allOf(withId(R.id.button_switch), isDescendantOfA(withId(R.id.show_notifications))))
                .check(matches(isChecked()));

        onView(withId(R.id.show_notifications)).perform(click());

        onView(allOf(withId(R.id.button_switch), isDescendantOfA(withId(R.id.show_notifications))))
                .check(matches(not(isChecked())));

        onView(withId(R.id.change_map_radius)).perform(click());

        onView(withText(String.format(getString(R.string.search_radius), 2000))).check(matches(isDisplayed()));

        onView(withId(R.id.radius_picker)).perform(setNumberPickerValue(650));
        onView(withId(R.id.units_picker)).perform(setNumberPickerValue(1));

        onView(withText(getString(R.string.ok))).perform(click());

        onView(withId(R.id.change_map_radius)).perform(click());

        onView(withText(String.format(getString(R.string.search_radius), 650))).check(matches(isDisplayed()));

        assertEquals(EasyPrefs.getShowNotifications(mRule.getActivity()), false);
        assertEquals(EasyPrefs.getMapRadius(mRule.getActivity()), 650);
    }
}
