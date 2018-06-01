package com.example.karlo.sstconference;

import android.net.Uri;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.karlo.sstconference.database.user.UserDataSource;
import com.example.karlo.sstconference.models.User;
import com.example.karlo.sstconference.modules.login.LoginActivity;
import com.example.karlo.sstconference.modules.login.LoginViewModel;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.Assert.assertEquals;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.mockito.Mockito.when;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import io.reactivex.Completable;
import io.reactivex.Maybe;

@RunWith(AndroidJUnit4.class)
public class LoginActivityTest extends BaseTest {

    @Mock
    UserDataSource userDataSource;
    @Mock
    User user;

    @Rule
    public final ActivityTestRule<LoginActivity> mRule = new ActivityTestRule<>(LoginActivity.class);

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Test
    public void testLoginFragment() {

        when(user.getMail()).thenReturn("marko@markic.com");

        onView(allOf(withId(R.id.login_email_et), isDescendantOfA(withId(R.id.fragment_login))))
                .perform(replaceText(user.getMail()));
        onView(allOf(withId(R.id.login_password_et), isDescendantOfA(withId(R.id.fragment_login))))
                .perform(replaceText("password123"));

        sleep(2000);

        onView(withId(R.id.login_button)).perform(click());

        when(userDataSource.getUser()).thenReturn(Maybe.just(user));

        userDataSource.getUser().subscribe(user1 -> {
            assertEquals(user1, user);
        });

       //LoginViewModel viewModel = new LoginViewModel(userDataSource);

       //

       //Maybe.just(user)
       //        .test()
       //        .assertResult(user);
       //

        //assertEquals(viewModel.deleteUser(user), Completable.complete());

        sleep(2000);

    }

}
