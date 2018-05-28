package com.example.karlo.sstconference.modules.program.fragments;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.example.karlo.sstconference.App;
import com.example.karlo.sstconference.modules.program.ProgramActivity;
import com.example.karlo.sstconference.modules.program.ProgramListener;
import com.example.karlo.sstconference.modules.program.ProgramViewModel;

import javax.inject.Inject;

import butterknife.Unbinder;
import io.reactivex.disposables.CompositeDisposable;

public abstract class BaseProgramFragment extends Fragment {

    @Inject
    ProgramViewModel mViewModel;

    protected CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    protected ProgramListener mListener;
    protected Unbinder mUnbinder;

    protected ProgramActivity mActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Application activity = ((ProgramActivity) getActivity()).getApplication();
        ((App) activity).getComponent().inject(this);
        mActivity = (ProgramActivity) getActivity();
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
        mCompositeDisposable.clear();
    }
}
