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

public class LibraryFragment extends Fragment {

    View rootView;

    ViewPager viewPager;
    TabLayout tabLayout;

    public LibraryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_music, container, false);

        viewPager = rootView.findViewById(R.id.viewPagerTab1);
        tabLayout = rootView.findViewById(R.id.tabLayoutTab1);

        return rootView;
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

        adapter.addFragment(new PlayListsFragment(), "Playlists");
        adapter.addFragment(new ArtistFragment(), "Artists");
        adapter.addFragment(new AlbumsFragment(), "Albums");
        System.out.println("library created fragments");

        viewPager.setAdapter(adapter);
    }

}