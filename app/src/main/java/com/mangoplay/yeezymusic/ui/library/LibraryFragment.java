package com.mangoplay.yeezymusic.ui.library;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.mangoplay.yeezymusic.MainActivity;
import com.mangoplay.yeezymusic.adapters.SectionPagerAdapter;
import com.mangoplay.yeezymusic.ui.library.TabsFragments.*;

import com.mangoplay.yeezymusic.R;
import com.google.android.material.tabs.TabLayout;


public class LibraryFragment extends Fragment {

    View myFragment;

    ViewPager viewPager;
    TabLayout tabLayout;

    public LibraryFragment() {
        MainActivity.tryToShowAd();
    }

    public static LibraryFragment getInstance()    {
        return new LibraryFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        myFragment = inflater.inflate(R.layout.fragment_library, container, false);

        MainActivity.setLocation("Library");

        viewPager = myFragment.findViewById(R.id.viewPagerFragmentHome);
        tabLayout = myFragment.findViewById(R.id.tabLayoutFragmentHome);

        return myFragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setUpViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setUpViewPager(ViewPager viewPager) {
        SectionPagerAdapter adapter = new SectionPagerAdapter(getChildFragmentManager());

        adapter.addFragment(new com.mangoplay.yeezymusic.ui.library.TabsFragments.LibraryFragment(), "Library");
        adapter.addFragment(new ExploreFragment(), "Explore");

        viewPager.setAdapter(adapter);
    }
}