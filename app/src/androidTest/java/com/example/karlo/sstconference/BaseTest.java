package com.example.karlo.sstconference;

import android.content.res.Resources;
import android.os.IBinder;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Root;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.matcher.ViewMatchers;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.widget.NumberPicker;

import com.example.karlo.sstconference.database.LocalDatabase;
import com.example.karlo.sstconference.utility.MockObject;

import net.globulus.easyprefs.EasyPrefs;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.containsString;

public class BaseTest extends MockObject {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    private Resources mResources;

    public BaseTest() {
        mResources = getApp().getResources();
    }

    protected String getString(int resId) {
        return mResources.getString(resId);
    }

    protected String getQuantityString(int resId, int quantity) {
        return mResources.getQuantityString(resId, quantity);
    }

    protected TestApp getApp() {
        return (TestApp) InstrumentationRegistry.getInstrumentation()
                .getTargetContext().getApplicationContext();
    }

    protected void sleep(int length) {
        try {
            Thread.sleep(length);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected void clearDatabaseAndPrefs() {
        EasyPrefs.clearAll(getApp());
        LocalDatabase.getDatabase(getApp()).clearAllTables();
    }

    public class ToastMatcher extends TypeSafeMatcher<Root> {

        @Override
        public void describeTo(Description description) {
            description.appendText("is toast");
        }

        @Override
        public boolean matchesSafely(Root root) {
            int type = root.getWindowLayoutParams().get().type;
            if (type == WindowManager.LayoutParams.TYPE_TOAST) {
                IBinder windowToken = root.getDecorView().getWindowToken();
                IBinder appToken = root.getDecorView().getApplicationWindowToken();
                return windowToken == appToken;
            }
            return false;
        }
    }

    protected ViewAction setNumberPickerValue(final int value) {

        return new ViewAction() {
            @Override
            public Matcher getConstraints() {
                return ViewMatchers.isAssignableFrom(NumberPicker.class);
            }

            @Override
            public String getDescription() {
                return "Set the value of a NumberPicker";
            }

            @Override
            public void perform(UiController uiController, View view) {
                ((NumberPicker)view).setValue(value);
            }
        };
    }

    private static RecyclerViewMatcher withRecyclerView(final int recyclerViewId) {
        return new RecyclerViewMatcher(recyclerViewId);
    }

    protected static ViewInteraction getRecyclerViewItem(int recyclerViewId, int position) {
        onView(withId(recyclerViewId)).perform(scrollToPosition(position));
        return onView(withRecyclerView(recyclerViewId).atPosition(position));
    }

    protected static void checkIfRecyclerViewItemHasText(int recyclerViewId, int position, String text) {
        onView(withId(recyclerViewId)).perform(scrollToPosition(position));
        onView(withRecyclerView(recyclerViewId).atPosition(position)).
                check(matches(hasDescendant(withText(containsString(text)))));
    }

    protected static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
