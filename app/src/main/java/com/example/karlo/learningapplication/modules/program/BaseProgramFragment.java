package com.example.karlo.learningapplication.modules.program;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.example.karlo.learningapplication.App;

import javax.inject.Inject;

import butterknife.Unbinder;

public abstract class BaseProgramFragment extends Fragment {

    @Inject
    ProgramViewModel mViewModel;

    protected ProgramListener mListener;
    protected Unbinder mUnbinder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Application activity = ((ProgramActivity) getActivity()).getApplication();
        ((App) activity).getComponent().inject(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getActivity() instanceof ProgramActivity) {
            mListener = (ProgramListener) getActivity();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
}
