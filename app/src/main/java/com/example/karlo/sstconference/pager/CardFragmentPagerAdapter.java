package com.example.karlo.sstconference.pager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.widget.CardView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class CardFragmentPagerAdapter<T extends CardFragment> extends FragmentStatePagerAdapter implements CardAdapter {

    private List<T> mFragments;
    private float mBaseElevation;

    public CardFragmentPagerAdapter(FragmentManager fm, float baseElevation, List<T> fragments) {
        super(fm);
        this.mFragments = new ArrayList<>();
        this.mBaseElevation = baseElevation;

        if (!fragments.isEmpty()) {
            mFragments.addAll(fragments);
        }
    }

    @Override
    public float getBaseElevation() {
        return mBaseElevation;
    }

    @Override
    public CardView getCardViewAt(int position) {
        return mFragments.get(position).getCardView();
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Object fragment = super.instantiateItem(container, position);
        mFragments.set(position, (T) fragment);
        return fragment;
    }

    public void addCardFragment(T fragment) {
        mFragments.add(fragment);
    }

}