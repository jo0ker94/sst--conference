package com.example.karlo.sstconference;

import android.content.res.Resources;
import android.os.IBinder;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Root;
import android.view.WindowManager;

import com.example.karlo.sstconference.database.LocalDatabase;
import com.example.karlo.sstconference.utility.MockObject;

import net.globulus.easyprefs.EasyPrefs;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

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
}
