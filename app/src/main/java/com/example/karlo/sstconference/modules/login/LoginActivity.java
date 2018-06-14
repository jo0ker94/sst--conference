package com.example.karlo.sstconference.modules.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.karlo.sstconference.App;
import com.example.karlo.sstconference.R;
import com.example.karlo.sstconference.commons.Constants;
import com.example.karlo.sstconference.models.LoginRequest;
import com.example.karlo.sstconference.modules.home.HomeActivity;
import com.example.karlo.sstconference.utility.AppConfig;
import com.example.karlo.sstconference.utility.NetworkUtility;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import net.globulus.easyprefs.EasyPrefs;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.CompositeDisposable;

public class LoginActivity extends AppCompatActivity implements
        LoginView,
        LoginFragment.LoginInterface,
        RegisterFragment.RegisterInterface {

    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;
    @BindView(R.id.pager)
    ViewPager mPager;

    private CompositeDisposable mDisposable = new CompositeDisposable();
    private GoogleSignInClient mGoogleSignInClient;

    private Unbinder mUnbinder;

    @Inject
    LoginViewModel mViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.NoActionBarTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ftue_fragment);
        mUnbinder = ButterKnife.bind(this);
        ((App) getApplication()).getComponent().inject(this);
        findViewById(android.R.id.content).setVisibility(View.INVISIBLE);
        PagerAdapter mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        setUpGoogleClient();
        setUpObservers();
        Log.i(getString(R.string.project_id), "Application started.");
        if (EasyPrefs.getGuestMode(this)) {
            AppConfig.USER_LOGGED_IN = false;
            goToHome();
        }
    }

    private void setUpObservers() {
        mViewModel.getUser().observe(this, user -> {
            if (user != null) {
                AppConfig.USER_LOGGED_IN = true;
                EasyPrefs.putGuestMode(this, false);
                goToHome();
            } else {
                findViewById(android.R.id.content).setVisibility(View.VISIBLE);
            }
        });
        mViewModel.getStatus().observe(this, status -> {
            switch(status.getResponse()) {
                case SIGNUP:
                    onSignUp(status.getLoginRequest());
                    break;
                case LOADING:
                    loadingData(status.getState());
                    break;
                case ERROR:
                    if (status.getMessage() != null) {
                        showError(new Throwable(status.getMessage()));
                    } else {
                        showError(new Throwable(getString(status.getInteger())));
                    }
                    break;
                case SUCCESS:
                    break;
            }
        });
    }

    private void setUpGoogleClient() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void goToHome() {
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDisposable.clear();
        mUnbinder.unbind();
    }

    @Override
    public void onLoggedIn() {
        Toast.makeText(this, R.string.success, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSignUp(LoginRequest loginRequest) {
        mViewModel.login(loginRequest);
    }

    @Override
    public void loadingData(boolean loading) {
        mProgressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showError(Throwable t) {
        Toast.makeText(this, t.getMessage(), Toast.LENGTH_SHORT).show();
    }

    private void signInGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, Constants.RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.RC_SIGN_IN) {
            mViewModel.signUpWithGoogle(data);
        }
    }

    @Override
    public void onLogin(String email, String password) {
        if (NetworkUtility.hasNetworkConnection(this)) {
            mViewModel.login(new LoginRequest(email, password));
        } else {
            NetworkUtility.showNoNetworkDialog(this);
        }
    }

    @Override
    public void onRegister(LoginRequest loginRequest) {
        if (NetworkUtility.hasNetworkConnection(this)) {
            mViewModel.signup(loginRequest);
        } else {
            NetworkUtility.showNoNetworkDialog(this);
        }
    }

    @Override
    public void signInWithGoogle() {
        if (NetworkUtility.hasNetworkConnection(this)) {
            signInGoogle();
        } else {
            NetworkUtility.showNoNetworkDialog(this);
        }
    }

    @Override
    public void goToLogin() {
        mPager.setCurrentItem(0);
    }

    @Override
    public void goToRegister() {
        mPager.setCurrentItem(1);
    }

    @Override
    public void forgotPassword(String email) {
        mViewModel.resetPassword(email);
    }

    @Override
    public void skipLogin() {
        EasyPrefs.putGuestMode(this, true);
        AppConfig.USER_LOGGED_IN = false;
        goToHome();
    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        private ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return position == 0 ? new LoginFragment() : new RegisterFragment();
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
