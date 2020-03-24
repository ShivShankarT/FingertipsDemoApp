package com.example.fingertipsdemoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import static androidx.viewpager.widget.PagerAdapter.POSITION_NONE;

/**
 * Created by esec-shiva on 30/8/17.
 */

public class ViewPager2Adapter extends FragmentStateAdapter {

    private String tabs[];
    private ReturnView returnView;


    public ViewPager2Adapter(AppCompatActivity fm, ReturnView returnView, String tabs[]) {
        super(fm);
        this.returnView = returnView;
        this.tabs = tabs;
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return  returnView.getPagerFragment(position);
    }

    @Override
    public int getItemCount() {
        return tabs.length;
    }


    public interface ReturnView {
        Fragment getPagerFragment(int position);
    }
}
