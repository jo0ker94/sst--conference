package com.example.karlo.learningapplication.modules.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.karlo.learningapplication.R;
import com.example.karlo.learningapplication.commons.BaseActivity;
import com.example.karlo.learningapplication.commons.Constants;
import com.example.karlo.learningapplication.helpers.DatabaseHelper;
import com.example.karlo.learningapplication.models.LoginRequest;
import com.example.karlo.learningapplication.models.User;
import com.example.karlo.learningapplication.modules.home.HomeActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Karlo on 25.3.2018..
 */

public class LoginActivity extends BaseActivity<LoginView, LoginPresenter> implements
        LoginView,
        LoginFragment.LoginInterface,
        RegisterFragment.RegisterInterface {

    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;
    @BindView(R.id.pager)
    ViewPager mPager;

    private GoogleSignInClient mGoogleSignInClient;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ftue_fragment);

        ButterKnife.bind(this);

        PagerAdapter mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        setUpGoogleClient();
        checkIfLogged();
    }

    @Override
    public void attachView() {
        presenter.attachView(this);
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

    private void checkIfLogged() {
        List<User> profiles = DatabaseHelper.getAllObjects(User.class);
        if (profiles != null && profiles.size() > 0) {
            goToHome();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        checkIfLogged();
    }

    @NonNull
    @Override
    public LoginPresenter createPresenter() {
        return new LoginPresenter();
    }

    @Override
    public void onLoggedIn() {
        Toast.makeText(this, R.string.success, Toast.LENGTH_SHORT).show();
        checkIfLogged();
    }

    @Override
    public void onSignUp(LoginRequest loginRequest) {
        presenter.login(loginRequest);
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
            presenter.signUpWithGoogle(data);
        }
    }

    @Override
    public void onLogin(String email, String password) {
        presenter.login(new LoginRequest(email, password));
    }

    @Override
    public void onRegister(LoginRequest loginRequest) {
        presenter.signup(loginRequest);
    }

    @Override
    public void signInWithGoogle() {
        signInGoogle();
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
