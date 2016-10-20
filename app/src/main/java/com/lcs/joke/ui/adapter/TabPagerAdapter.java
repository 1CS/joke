package com.lcs.joke.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.lcs.joke.ui.fragment.JokeTextFragment;
import com.lcs.joke.ui.fragment.AboutFragment;
import com.lcs.joke.ui.fragment.JokePicFragment;

public class TabPagerAdapter extends FragmentPagerAdapter {
    private final static String[] TITLES = {"笑话太全", "爆笑趣图", "关于APP"};

    public TabPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) return new JokeTextFragment();
        if (position == 1) return new JokePicFragment();
        if (position == 2) return new AboutFragment();
        return null;
    }

    @Override
    public int getCount() {
        return TITLES.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TITLES[position];
    }
}
