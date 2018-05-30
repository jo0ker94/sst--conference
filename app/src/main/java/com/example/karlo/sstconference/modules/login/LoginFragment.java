package com.example.karlo.sstconference.modules.login;

import android.app.AlertDialog;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Karlo on 11.4.2018..
 */

public class LoginFragment extends android.support.v4.app.Fragment {

    @BindView(R.id.login_email)
    TextInputLayout mEmail;
    @BindView(R.id.login_password)
    TextInputLayout mPassword;
    @BindView(R.id.login_button)
    Button mLoginButton;
    @BindView(R.id.google_sign_in)
    RelativeLayout mSigninGoogleButton;
    @BindView(R.id.forgot_password)
    TextView mForgotPassword;
    @BindView(R.id.create_account)
    TextView mCreateAccount;

    interface LoginInterface {
        void onLogin(String email, String password);
        void signInWithGoogle();
        void goToRegister();
        void forgotPassword(String email);
    }

    private LoginInterface mListener;
    private Unbinder mUnbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.activity_login, container, false);
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
            mListener = (LoginInterface) getActivity();
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
        mLoginButton.setOnClickListener(view -> {
            if (fieldsValid()) {
                mListener.onLogin(mEmail.getEditText().getText().toString(), mPassword.getEditText().getText().toString());
            }
        });
        mForgotPassword.setOnClickListener(view -> showForgotPasswordDialog());
        TextWatcher textWatcher = new TextWatcher();
        mEmail.getEditText().addTextChangedListener(textWatcher);
        mPassword.getEditText().addTextChangedListener(textWatcher);
        mSigninGoogleButton.setOnClickListener(view -> mListener.signInWithGoogle());
        mCreateAccount.setOnClickListener(view -> mListener.goToRegister());
    }

    private void showForgotPasswordDialog() {
        View view = getLayoutInflater().inflate(R.layout.forgot_password_layout, null);
        TextInputLayout textInputLayout = view.findViewById(R.id.forgot_password_email);
        new AlertDialog.Builder(getActivity())
                .setView(view)
                .setPositiveButton(R.string.submit, (dialogInterface, i) -> {
                    if (!TextUtils.isEmpty(textInputLayout.getEditText().getText().toString())) {
                        mListener.forgotPassword(textInputLayout.getEditText().getText().toString());
                    } else {
                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    private boolean fieldsValid() {
        boolean valid = true;
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
                    || TextUtils.isEmpty(mPassword.getEditText().getText().toString())) {
                mLoginButton.setTextColor(getResources().getColor(R.color.select_comment_header_color));
                mLoginButton.setBackground(getResources().getDrawable(R.drawable.clickable_white_button_round_corners));
            } else {
                mLoginButton.setTextColor(getResources().getColor(R.color.white));
                mLoginButton.setBackground(getResources().getDrawable(R.drawable.clickable_blue_button_round_corners));

            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }
}
