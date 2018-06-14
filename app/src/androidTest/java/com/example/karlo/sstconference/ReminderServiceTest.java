package com.example.karlo.sstconference;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.Until;

import com.example.karlo.sstconference.commons.Constants;
import com.example.karlo.sstconference.database.topic.TopicDataSource;
import com.example.karlo.sstconference.modules.settings.SettingsActivity;
import com.example.karlo.sstconference.service.SendReminderService;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;

import io.reactivex.Observable;

import static com.google.android.gms.common.api.CommonStatusCodes.TIMEOUT;
import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class ReminderServiceTest extends BaseTest {

    //@Mock
    //private TopicDataSource dataSource;

    @Rule
    public final ActivityTestRule<SettingsActivity> mRule = new ActivityTestRule<>(SettingsActivity.class);

    @Before
    public void init() {
       //when(dataSource.getTopics()).thenReturn(Observable.just(getTopics(10)));
        clearDatabaseAndPrefs();
    }

    @After
    public void after() {
        Intent it = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        mRule.getActivity().sendBroadcast(it);
    }

    @Test
    public void testNotification() {
        Intent serviceIntent = new Intent(mRule.getActivity(), SendReminderService.class);
        serviceIntent.putExtra(Constants.ID, "0");
        mRule.getActivity().startService(serviceIntent);

        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        device.openNotification();
        device.wait(Until.hasObject(By.text(TITLE)), TIMEOUT);
        UiObject2 title = device.findObject(By.text(TITLE));
        UiObject2 text = device.findObject(By.text(getString(R.string.topic_starting_in_fifteen_minutes)));
        assertEquals(TITLE, title.getText());
        assertEquals(getString(R.string.topic_starting_in_fifteen_minutes), text.getText());
    }
}
