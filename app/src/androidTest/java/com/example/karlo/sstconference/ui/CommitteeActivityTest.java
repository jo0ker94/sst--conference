package com.example.karlo.sstconference.ui;

import android.arch.lifecycle.MutableLiveData;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;

import com.example.karlo.sstconference.BaseTest;
import com.example.karlo.sstconference.R;
import com.example.karlo.sstconference.commons.Status;
import com.example.karlo.sstconference.models.committee.CommitteeMember;
import com.example.karlo.sstconference.modules.committee.CommitteeActivity;
import com.example.karlo.sstconference.modules.committee.CommitteeViewModel;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import javax.inject.Inject;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.when;

public class CommitteeActivityTest extends BaseTest {

    @Inject
    CommitteeViewModel viewModel;

    @Rule
    public final ActivityTestRule<CommitteeActivity> mRule = new ActivityTestRule<>(CommitteeActivity.class, false, false);

    private MutableLiveData<List<CommitteeMember>> steeringCommittee = new MutableLiveData<>();
    private MutableLiveData<List<CommitteeMember>> programCommittee = new MutableLiveData<>();
    private MutableLiveData<List<CommitteeMember>> organizingCommittee = new MutableLiveData<>();
    private MutableLiveData<Status> status = new MutableLiveData<>();

    @Before
    public void init() {
        getApp().getComponent().inject(this);
        when(viewModel.getOrganizing()).thenReturn(organizingCommittee);
        when(viewModel.getProgram()).thenReturn(programCommittee);
        when(viewModel.getSteering()).thenReturn(steeringCommittee);
        when(viewModel.getStatus()).thenReturn(status);
        mRule.launchActivity(null);
        clearDatabaseAndPrefs();
    }

    @After
    public void clearData() {
        clearDatabaseAndPrefs();
    }

    @Test
    public void testOrganizingCommittee() {
        organizingCommittee.postValue(getCommitteeMembers(ORGANIZING,6));

        getCardViewAtPosition(0).check(matches(hasDescendant(withText(getStringFormat(NAME, 0)))));
        getCardViewAtPosition(1).check(matches(hasDescendant(withText(getStringFormat(NAME, 1)))));
        getCardViewAtPosition(2).check(matches(hasDescendant(withText(getStringFormat(NAME, 2)))));
        getCardViewAtPosition(3).check(matches(hasDescendant(withText(getStringFormat(NAME, 3)))));
        getCardViewAtPosition(4).check(matches(hasDescendant(withText(getStringFormat(NAME, 4)))));
        getCardViewAtPosition(5).check(matches(hasDescendant(withText(getStringFormat(NAME, 5)))));
    }

    @Test
    public void testSteeringCommittee() {
        steeringCommittee.postValue(getCommitteeMembers(STEERING,4));

        onView(withId(R.id.fragment_0)).perform(swipeLeft());

        sleep(500);

        onView(allOf(withId(R.id.recycler_view), isDescendantOfA(withId(R.id.fragment_1)))).check(matches(isDisplayed()));

        getCardViewAtPosition(0).check(matches(hasDescendant(withText(getStringFormat(NAME, 0)))));
        getCardViewAtPosition(1).check(matches(hasDescendant(withText(getStringFormat(NAME, 1)))));
        getCardViewAtPosition(2).check(matches(hasDescendant(withText(getStringFormat(NAME, 2)))));
        getCardViewAtPosition(3).check(matches(hasDescendant(withText(getStringFormat(NAME, 3)))));
    }

    @Test
    public void testProgramCommittee() {
        programCommittee.postValue(getCommitteeMembers(PROGRAM,6));

        onView(withId(R.id.fragment_0)).perform(swipeLeft());
        onView(withId(R.id.fragment_1)).perform(swipeLeft());

        sleep(500);

        getCardViewAtPosition(0).check(matches(hasDescendant(withText(getStringFormat(NAME, 0)))));
        getCardViewAtPosition(1).check(matches(hasDescendant(withText(getStringFormat(NAME, 1)))));
        getCardViewAtPosition(2).check(matches(hasDescendant(withText(getStringFormat(NAME, 2)))));
        getCardViewAtPosition(3).check(matches(hasDescendant(withText(getStringFormat(NAME, 3)))));
        getCardViewAtPosition(4).check(matches(hasDescendant(withText(getStringFormat(NAME, 4)))));
        getCardViewAtPosition(5).check(matches(hasDescendant(withText(getStringFormat(NAME, 5)))));
    }

    @Test
    public void testErrorMessage() {
        status.postValue(Status.error(TEST_MESSAGE));

        sleep(500);

        onView(withText(TEST_MESSAGE)).inRoot(new ToastMatcher())
                .check(matches(isDisplayed()));
    }

    @Test
    public void testProgressBar() {
        status.postValue(Status.loading(true));
        sleep(500);
        onView(Matchers.allOf(withId(R.id.progress_bar), isDescendantOfA(withId(R.id.committee_container))))
                .check(matches(isDisplayed()));

        status.postValue(Status.loading(false));
        sleep(500);
        onView(Matchers.allOf(withId(R.id.progress_bar), isDescendantOfA(withId(R.id.committee_container))))
                .check(matches(not(isDisplayed())));
    }

    private ViewInteraction getCardViewAtPosition(int position) {
        return onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.recycler_view),
                                childAtPosition(
                                        withClassName(Matchers.is("android.widget.RelativeLayout")),
                                        0)),
                        position),
                        isDisplayed()));
    }
}

