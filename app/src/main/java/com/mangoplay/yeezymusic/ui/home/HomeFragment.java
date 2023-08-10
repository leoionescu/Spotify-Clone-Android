package com.mangoplay.yeezymusic.ui.home;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.mangoplay.yeezymusic.MainActivity;
import com.mangoplay.yeezymusic.adapters.ItemHomeAdapter;
import com.mangoplay.yeezymusic.R;
import com.mangoplay.yeezymusic.adapters.PlaylistRowAdapter;
import com.mangoplay.yeezymusic.objects.CountryIso;
import com.mangoplay.yeezymusic.objects.ForYou;
import com.mangoplay.yeezymusic.objects.History;
import com.mangoplay.yeezymusic.objects.Lock;
import com.mangoplay.yeezymusic.objects.Playlist;
import com.mangoplay.yeezymusic.objects.Track;
import com.mangoplay.yeezymusic.services.DeezerService;
import com.mangoplay.yeezymusic.services.LastFmService;
import com.mangoplay.yeezymusic.ui.settings.SettingsActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeFragment extends Fragment {

    static View rootView = null;
    public LayoutInflater inflater;
    public ViewGroup container;
    ImageButton settingsButton;
    RecyclerView recyclerWelcome;
    RecyclerView recyclerHeardRecently;
    RecyclerView recycclerTopPlaylists;
    RecyclerView recyclerNewReleases;
    static ItemHomeAdapter itemHomeAdapter = null;
    boolean homeRecyclerReady = false;
    public Button refreshView;
    public static int constructorCounter = 0;
    public static boolean playlistsCreated = false;

    RecyclerView homeRecycler;
    static List<List<Playlist>> homeElements = new ArrayList<>();
    static List<String> titles = new ArrayList<>();

    static List<Track> recentlyPlayed = new ArrayList<>();
    static List<Playlist> topPlaylists = new ArrayList<>();
    static List<Playlist> charts = new ArrayList<>();
    static List<Playlist> topArtists = new ArrayList<>();
    static List<Playlist> topAlbums = new ArrayList<>();
    static List<Playlist> forYouPlaylists = new ArrayList<>();
    static Playlist forYouTracks;

    GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{Color.parseColor("#1DB954"), Color.parseColor("#000000")});

    boolean refresh = false;

    public static AdView mAdView1;
    public static AdView mAdView2;
    public static AdRequest adRequest;

//    ArrayList<ItemMusic> arrayWelcome;

    TextView recentlyPlayedText;
    public HomeFragment() {
        long start = System.currentTimeMillis();
        System.out.println("in home fragment constructor aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        HomeFragment.constructorCounter++;
        MainActivity.homeFragment = this;
        MainActivity.tryToShowAd();
        long stop = System.currentTimeMillis();
//        System.out.println("time in constructor: " + (stop - start));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflater = inflater;
        this.container = container;
        if(rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_home, container, false);

            System.out.println("playlist sizes: " + HomeFragment.recentlyPlayed.size() + " " + HomeFragment.charts.size() + " " + HomeFragment.topPlaylists.size());
            long startT = System.currentTimeMillis();

            if (recentlyPlayed.size() > 0 && titles.size() == 0) titles.add(0, "Played recently");
//        for(int i = 0 ; i < 1;i++){
//        }

            mAdView1 = rootView.findViewById(R.id.adView1);
            mAdView2 = rootView.findViewById(R.id.adView2);
            if(MainActivity.showAds) {
                loadBanner();
            } else {
                mAdView1.setVisibility(View.GONE);
                mAdView2.setVisibility(View.GONE);
            }

            homeRecycler = rootView.findViewById(R.id.home_recycler);
            homeRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
            homeRecyclerReady = true;
            if (itemHomeAdapter == null) {
                System.out.println("item home adapter is null");
                itemHomeAdapter = new ItemHomeAdapter(homeElements, titles, recentlyPlayed);
            }
            homeRecycler.setAdapter(itemHomeAdapter);
//        itemHomeAdapter.notifyDataSetChanged();
//            System.out.println("homeElements size: " + homeElements.size());
//            System.out.println("itemHomeAdpater size: " + itemHomeAdapter.getItemCount());

//        if(recentlyPlayed.size() == 0){
//            recentlyPlayedText.setVisibility(View.GONE);
//            recyclerHeardRecently.setVisibility(View.GONE);
//        }


            TextView welcome = rootView.findViewById(R.id.welcome);

            gradientDrawable.setDither(true);
            welcome.setBackground(gradientDrawable);

            refreshView = new Button(MainActivity.context);
            refreshView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("refreshing view home fragment");
                    if (itemHomeAdapter != null && homeRecyclerReady)
                        try {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    itemHomeAdapter.notifyDataSetChanged();
//                                itemHomeAdapter.refreshAdapter(homeElements.size());
//                                itemHomeAdapter.refreshAll();
                                }
                            });
                        } catch (NullPointerException e){
                            System.out.println("activity not attached yet");
                            refresh = true;
                        }
//                itemHomeAdapter.notifyItemInserted(homeElements.size());
                }
            });

            setOnClickListeners();

            long stopT = System.currentTimeMillis();

//            System.out.println("time for adapters: " + (stopT - startT));

        } else {
//            System.out.println("refreshing");
//            rootView = inflater.inflate(R.layout.fragment_home, container, false);
            itemHomeAdapter.refreshAdapter(0);
            setOnClickListeners();
            ViewGroup parentViewGroup = (ViewGroup) rootView.getParent();
            if (parentViewGroup != null) {
                parentViewGroup.removeAllViews();
                System.out.println("removing all views");
                refreshView(inflater, container);
            } else if(MainActivity.getLocation().equals("Home")) refreshView(inflater, container);
        }

        MainActivity.setLocation("Home");
        return rootView;
    }

    public void refreshView(@NonNull LayoutInflater inflater, ViewGroup container) {
        System.out.println("refreshView function home fragment");
        rootView = inflater.inflate(R.layout.fragment_home, container, false);

//        System.out.println("playlist sizes: " + HomeFragment.recentlyPlayed.size() + " " + HomeFragment.charts.size() + " " + HomeFragment.topPlaylists.size());
        long startT = System.currentTimeMillis();

        if (recentlyPlayed.size() > 0 && titles.size() == 0) titles.add(0, "Played recently");
//        for(int i = 0 ; i < 1;i++){
//        }

        mAdView1 = rootView.findViewById(R.id.adView1);
        mAdView2 = rootView.findViewById(R.id.adView2);
        if(MainActivity.showAds) {
            loadBanner();
        } else {
            mAdView1.setVisibility(View.GONE);
            mAdView2.setVisibility(View.GONE);
        }

        homeRecycler = rootView.findViewById(R.id.home_recycler);
        homeRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        homeRecyclerReady = true;
        if (itemHomeAdapter == null) {
            System.out.println("item home adapter is null");
            itemHomeAdapter = new ItemHomeAdapter(homeElements, titles, recentlyPlayed);
        }
        homeRecycler.setAdapter(itemHomeAdapter);
        itemHomeAdapter.notifyDataSetChanged();
//            System.out.println("homeElements size: " + homeElements.size());
//            System.out.println("itemHomeAdpater size: " + itemHomeAdapter.getItemCount());

//        if(recentlyPlayed.size() == 0){
//            recentlyPlayedText.setVisibility(View.GONE);
//            recyclerHeardRecently.setVisibility(View.GONE);
//        }


        TextView welcome = rootView.findViewById(R.id.welcome);

        gradientDrawable.setDither(true);
        welcome.setBackground(gradientDrawable);

        refreshView = new Button(MainActivity.context);
        refreshView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("refreshing view home fragment");
                if (itemHomeAdapter != null && homeRecyclerReady)
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            itemHomeAdapter.notifyDataSetChanged();
//                                itemHomeAdapter.refreshAdapter(homeElements.size());
//                                itemHomeAdapter.refreshAll();
                        }
                    });
//                itemHomeAdapter.notifyItemInserted(homeElements.size());
            }
        });

        rootView.findViewById(R.id.progress_loader).setVisibility(View.GONE);

        if(forYouTracks != null) {
            RecyclerView forYou = rootView.findViewById(R.id.for_you_tracks_recycler_view);
            forYou.setLayoutManager(new LinearLayoutManager(MainActivity.context));
            forYou.setAdapter(new PlaylistRowAdapter(forYouTracks, null));
        } else rootView.findViewById(R.id.title).setVisibility(View.GONE);

        setOnClickListeners();
    }

//    @Override
//    public void onAttach(@NonNull Context context) {
//        super.onAttach(context);
//        System.out.println("onAttach");
//        if (context instanceof MainActivity && refresh){
//            refreshView.callOnClick();
//        }
//    }

    private void setOnClickListeners() {
        settingsButton = rootView.findViewById(R.id.settings_button);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("settings clicked");
                startActivity(new Intent(MainActivity.context, SettingsActivity.class));
            }
        });
    }

    public void createHomePlaylists(){


        History.readData();
        fillHeardRecently();

        new Thread(() -> {
            long startT = System.currentTimeMillis();
            DeezerService.getCharts();

            forYouPlaylists = ForYou.readForYouPlaylists(MainActivity.context);
            if(forYouPlaylists.size() > 0) {
                titles.add("Playlists For You");
                homeElements.add(forYouPlaylists);
                refreshView.callOnClick();
            }

            fillCharts();
//            System.out.println("charts first element: " + charts.get(0).getTitle());

            try {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        rootView.findViewById(R.id.progress_loader).setVisibility(View.GONE);
                    }
                });
            } catch (NullPointerException e){
//                    System.out.println("view already created");
            }

            try {
                fillTopPlaylists();
            } catch (NullPointerException e) {}
            try {
                fillTopArtists();
            } catch (NullPointerException e) {}
            try {
                fillTopAlbums();
            } catch (NullPointerException e){}

            fillForYouTracks();

            long stopT = System.currentTimeMillis();
        System.out.println("Create home playlists time: " + (stopT - startT));
            fillForYouPlaylists();
        }).start();



    }

    private void fillForYouTracks() {
        int numberOfForYouTracks = 50;
        forYouTracks = new Playlist();
        RecyclerView forYouTracksRecyclerView = rootView.findViewById(R.id.for_you_tracks_recycler_view);

        for(Playlist playlist : forYouPlaylists){
            forYouTracks.tracks.addAll(playlist.tracks);
            Collections.shuffle(forYouTracks.tracks);
            if(forYouTracks.tracks.size() > numberOfForYouTracks) forYouTracks.tracks = forYouTracks.tracks.subList(0, numberOfForYouTracks);
        }

        PlaylistRowAdapter itemsAdapter = new PlaylistRowAdapter(forYouTracks, null);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                rootView.findViewById(R.id.title).setVisibility(View.VISIBLE);
                forYouTracksRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                forYouTracksRecyclerView.setAdapter(itemsAdapter);
            }
        });

    }

    private void fillForYouPlaylists() {
        Lock lock = new Lock();
        int nbOfElements = 5;
        int i = 0;
        List<Playlist> playlists = new ArrayList<>();
        for(Track track : History.playlist.tracks){
            new Thread(() -> {
                System.out.println("track: " + track.getTitle());
                Playlist playlist = LastFmService.getSimilarTracks(track, 60);
                playlist.setDrawable(track.getDrawable());
//                if(track.getDrawableSize().equals("small")) {
                    CustomTarget<Drawable> customTarget = new CustomTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            new Thread(() -> {
//                                track.setDrawable(resource, "big");
                                playlist.setDrawable(resource);
                                playlists.add(playlist);
                                if(playlists.size() >= nbOfElements || playlists.size() == History.playlist.tracks.size()) {
                                    synchronized (lock.lock){
                                        lock.lock.notify();
                                        System.out.println("forYouPlaylists thread unlocked");
                                    }
                                }
                                System.out.println("added playlist");
                            }).start();
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {

                        }
                    };
                    try {
                        Glide.with(MainActivity.context).load(playlist.tracks.get(0).getCoverBig()).into(customTarget);
                    } catch (NullPointerException | IndexOutOfBoundsException e){
                        System.out.println("forYouPlaylists exception");
                        playlists.add(playlist);
                        if(playlists.size() >= nbOfElements || playlists.size() == History.playlist.tracks.size()) {
                            synchronized (lock.lock){
                                lock.lock.notify();
                                System.out.println("forYouPlaylists thread unlocked");
                            }
                        }
                        System.out.println("added playlist");
                    }
//                }

            }).start();
            i++;
            if(nbOfElements == i) break;
//            break; // don t forget to removeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee
        }
        synchronized (lock.lock){
            try {
                System.out.println("forYouPlaylists thread locked");
                lock.lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        playlists.removeIf(playlist -> playlist.tracks.size() == 0);
        ForYou.saveForYouPlaylists(playlists);
        System.out.println("forYouPlaylists size: " + playlists.size());
    }

    private void fillTopPlaylists() {
        topPlaylists = DeezerService.getTopPlaylists();
        topPlaylists.removeIf(playlist -> playlist.getTitle() == null);
        if(topPlaylists.size() > 0) {
            titles.add("Top Playlists");
            homeElements.add(HomeFragment.topPlaylists);
            refreshView.callOnClick();
        }
//        System.out.println("playlist size for topPlaylist in fillTopPlaylists: " + topPlaylists);

//        int counter = 0;
//        while(true){
//            boolean breakAtTheEnd = true;
//            List<Playlist> topPlaylistsCopy = new ArrayList<Playlist>(topPlaylists);
//            for (Playlist p : topPlaylistsCopy){
//                if(p != null) {
//                    if (p.getTitle() == null) {
//                        breakAtTheEnd = false;
//                    }
//                } else breakAtTheEnd = false;
//            }
//            if(breakAtTheEnd) break;
//            else {
//                counter++;
//                try {
//                    Thread.sleep(10);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//            if(counter == 3000) break;
//        }
    }

    private void fillCharts() {
        System.out.println("fillCharts");
        String[] ids = {"3155776842", "1313621735", "1111142221", "1111141961", "1111143121", "1109890291"};
//        newReleases.add(DeezerService.getChart());

        Lock bigLock = new Lock();

        new Thread(() -> {
            Lock lock = new Lock();
            String[] list = new String[1];
            DeezerService.getCountryIso(lock, list);
            synchronized (lock.lock){
                try {
                    lock.lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            String countryIso = new String(list[0]);
            System.out.println("countryIso: " + countryIso);
            String id = CountryIso.getPlaylistId(countryIso);
            System.out.println("id: " + id);
            Playlist playlist = DeezerService.getPlaylist(id);
            charts.add(0, playlist);
            int counter = 0;
            while(charts.size() < ids.length){
                try {
                    Thread.sleep(10);
                    counter++;
                    if(counter == 2000) break;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if(charts.size() >= ids.length){
                charts.removeIf(chart -> chart.tracks.size() == 0);
                if(charts.size() > 0) {
                    titles.add("Charts");
                    homeElements.add(HomeFragment.charts);
                    refreshView.callOnClick();
                }
            }
            synchronized (bigLock.lock){
                bigLock.lock.notify();
            }
        }).start();

        for(String id : ids) {
            new Thread(() -> {
                Playlist playlist = DeezerService.getPlaylist(id);
                charts.add(playlist);
            }).start();
        }

        synchronized (bigLock.lock){
            try {
                bigLock.lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


//        int counter = 0;
//        while(charts.size() < ids.length) {
//            try {
//                Thread.sleep(10);
//                counter++;
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            if(counter == 1000) break;
//        }
//        counter = 0;
//        while(true){
//            boolean breakAtTheEnd = true;
//            for (Playlist p : charts){
//                if(p != null) {
//                    if (p.getTitle() == null) {
//                        breakAtTheEnd = false;
//                    }
//                } else breakAtTheEnd = false;
//            }
//            if(breakAtTheEnd) break;
//            else {
//                counter++;
//                try {
//                    Thread.sleep(10);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//            if(counter == 3000) break;
//        }
    }

    private void fillTopArtists(){
            topArtists = DeezerService.getTopArtists();
            topArtists.removeIf(artist -> artist.getTitle() == null);
            if(topArtists.size() > 0) {
                titles.add("Top Artists");
                homeElements.add(HomeFragment.topArtists);
//            System.out.println("topArtists first element: " + topArtists.get(0).getTitle());
                refreshView.callOnClick();
            }
//        int counter = 0;
//        while(true){
//            boolean breakAtTheEnd = true;
//            List<Playlist> topArtistsCopy = new ArrayList<Playlist>(topArtists);
//            for (Playlist p : topArtistsCopy){
//                if(p != null) {
//                    if (p.getTitle() == null) {
//                        breakAtTheEnd = false;
//                    }
//                } else breakAtTheEnd = false;
//            }
//            if(breakAtTheEnd) break;
//            else {
//                counter++;
//                try {
//                    Thread.sleep(10);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//            if(counter == 3000) break;
//        }


//        System.out.println("number of artists: " + topArtists.size());
//        for(Playlist p : topArtists){
//            System.out.println(p.getTitle());
//        }
    }

    private void fillTopAlbums(){
            topAlbums = DeezerService.getTopAlbums();
            topAlbums.removeIf(album -> album.getTitle() == null);
            if(topAlbums.size() > 0) {
                titles.add("Top Albums");
                homeElements.add(HomeFragment.topAlbums);
//            System.out.println("topAlbums first element: " + topAlbums.get(0).getTitle());
                refreshView.callOnClick();
            }
//        int counter = 0;
//        while(true){
//            boolean breakAtTheEnd = true;
//            List<Playlist> topAlbumsCopy = new ArrayList<Playlist>(topAlbums);
//            for (Playlist p : topAlbumsCopy){
//                if(p != null) {
//                    if (p.getTitle() == null) {
//                        breakAtTheEnd = false;
//                    }
//                }
//            }
//            if(breakAtTheEnd) break;
//            else {
//                counter++;
//                try {
//                    Thread.sleep(10);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//            if(counter == 3000) break;
//        }

//        System.out.println("number of albums: " + topAlbums.size());
//        for(Playlist p : topAlbums){
//            System.out.println(p.getTitle());
//        }
    }

    public void fillHeardRecently() {
        recentlyPlayed = new ArrayList<>();
        for(Track track : History.playlist.tracks){
            recentlyPlayed.add(track);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        System.out.println("onDestroy");
        if (rootView != null) {
            ViewGroup parentViewGroup = (ViewGroup) rootView.getParent();
            if (parentViewGroup != null) {
//                parentViewGroup.removeAllViews();
//                parentViewGroup.removeView(rootView);
            }
        }
    }

    private void loadBanner() {
        adRequest = new AdRequest.Builder().build();
        mAdView1.loadAd(adRequest);
        adRequest = new AdRequest.Builder().build();
        mAdView2.loadAd(adRequest);
    }
}