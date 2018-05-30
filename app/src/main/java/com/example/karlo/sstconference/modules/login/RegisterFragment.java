package com.example.karlo.sstconference.modules.login;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.karlo.sstconference.R;
import com.example.karlo.sstconference.models.LoginRequest;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Karlo on 11.4.2018..
 */

public class RegisterFragment extends android.support.v4.app.Fragment {

    @BindView(R.id.login_display_name)
    TextInputLayout mName;
    @BindView(R.id.login_email)
    TextInputLayout mEmail;
    @BindView(R.id.login_password)
    TextInputLayout mPassword;
    @BindView(R.id.signup_button)
    Button mSignUpButton;
    @BindView(R.id.google_sign_in)
    RelativeLayout mSigninGoogleButton;
    @BindView(R.id.login_text)
    TextView mLoginPage;

    interface RegisterInterface {
        void onRegister(LoginRequest loginRequest);
        void signInWithGoogle();
        void goToLogin();
    }

    private RegisterInterface mListener;
    private Unbinder mUnbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.activity_register, container, false);
        mUnbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpListeners();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getActivity() instanceof LoginActivity) {
            mListener = (RegisterFragment.RegisterInterface) getActivity();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
    }

    private void setUpListeners() {
        mSignUpButton.setOnClickListener(view -> {
            if (fieldsValid()) {
                mListener.onRegister(new LoginRequest(mEmail.getEditText().getText().toString(),
                        mPassword.getEditText().getText().toString(),
                        mName.getEditText().getText().toString()));
            }
        });
        TextWatcher textWatcher = new TextWatcher();
        mEmail.getEditText().addTextChangedListener(textWatcher);
        mPassword.getEditText().addTextChangedListener(textWatcher);
        mName.getEditText().addTextChangedListener(textWatcher);
        mSigninGoogleButton.setOnClickListener(view -> mListener.signInWithGoogle());
        mLoginPage.setOnClickListener(view -> mListener.goToLogin());
    }

    private boolean fieldsValid() {
        boolean valid = true;
        if (TextUtils.isEmpty(mName.getEditText().getText().toString())) {
            mName.setError(getString(R.string.no_name_error));
            valid = false;
        } else {
            mName.setError(null);
        }
        if (TextUtils.isEmpty(mEmail.getEditText().getText().toString())) {
            mEmail.setError(getString(R.string.no_email_error));
            valid = false;
        } else {
            mEmail.setError(null);
        }
        if (TextUtils.isEmpty(mPassword.getEditText().getText().toString())) {
            mPassword.setError(getString(R.string.no_password_error));
            valid = false;
        } else {
            mPassword.setError(null);
        }
        return valid;
    }

    private class TextWatcher implements android.text.TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (TextUtils.isEmpty(mEmail.getEditText().getText().toString())
                    || TextUtils.isEmpty(mPassword.getEditText().getText().toString())
                    || TextUtils.isEmpty(mName.getEditText().getText().toString())) {
                mSignUpButton.setTextColor(getResources().getColor(R.color.select_comment_header_color));
                mSignUpButton.setBackground(getResources().getDrawable(R.drawable.clickable_white_button_round_corners));
            } else {
                mSignUpButton.setTextColor(getResources().getColor(R.color.white));
                mSignUpButton.setBackground(getResources().getDrawable(R.drawable.clickable_blue_button_round_corners));

            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }
}
