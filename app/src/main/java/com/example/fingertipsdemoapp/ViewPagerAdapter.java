package com.example.fingertipsdemoapp;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private String tabs[];
    private ReturnView returnView;


    public ViewPagerAdapter(FragmentManager fm, ReturnView returnView, String tabs[]) {
        super(fm);
        this.returnView = returnView;
        this.tabs = tabs;
    }


    @Override
    public Fragment getItem(int position) {
        return returnView.getPagerFragment(position);
    }


    @Override
    public int getCount() {
        return tabs.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabs[position];

    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public interface ReturnView {
        Fragment getPagerFragment(int position);
    }
}
