package com.mangoplay.yeezymusic.ui.library.TabsFragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mangoplay.yeezymusic.R;
import com.mangoplay.yeezymusic.adapters.SectionPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class ExploreFragment extends Fragment {
    View myFragment;

    ViewPager viewPager;
    TabLayout tabLayout;

    public ExploreFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myFragment = inflater.inflate(R.layout.fragment_podcasts, container, false);

        viewPager = myFragment.findViewById(R.id.viewPagerTab2);
        tabLayout = myFragment.findViewById(R.id.tabLayoutTab2);

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

        adapter.addFragment(new GenresFragment(), "Genres");
//        adapter.addFragment(new DownLoadsFragment(), "Downloads");
//        adapter.addFragment(new ProgramsFragment(), "Programs");

        viewPager.setAdapter(adapter);
    }
}