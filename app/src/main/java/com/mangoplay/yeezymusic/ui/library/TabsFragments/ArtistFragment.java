package com.mangoplay.yeezymusic.ui.library.TabsFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.mangoplay.yeezymusic.MainActivity;
import com.mangoplay.yeezymusic.adapters.ItemMusicSmallAdapter;
import com.mangoplay.yeezymusic.items.ItemMusic;
import com.mangoplay.yeezymusic.R;
import com.mangoplay.yeezymusic.objects.History;
import com.mangoplay.yeezymusic.objects.Playlist;
import com.mangoplay.yeezymusic.objects.Track;
import com.mangoplay.yeezymusic.services.DeezerService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class ArtistFragment extends Fragment {

    View fragmentArtist;
    RecyclerView recyclerArtist;
    ArrayList<ItemMusic> arrayArtist;
    public static List<Playlist> artists = new ArrayList<>();
    ItemMusicSmallAdapter itemMusicSmallAdapter;
    static Button refreshView;

    public ArtistFragment() {
        // Required empty public constructor
    }


//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        System.out.println("onCreateView ArtistFragment");
        fragmentArtist = inflater.inflate(R.layout.fragment_artist, container, false);
        recyclerArtist = fragmentArtist.findViewById(R.id.artists_recycler);

        System.out.println("artists create view");


        refreshView = new Button(MainActivity.context);
        refreshView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (itemMusicSmallAdapter != null && recyclerArtist != null)
                        requireActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (itemMusicSmallAdapter.getItemCount() > 0) {
//                                    itemMusicSmallAdapter.notifyDataSetChanged();
                                    itemMusicSmallAdapter.notifyItemInserted(itemMusicSmallAdapter.getItemCount());
                                    fragmentArtist.findViewById(R.id.progress_loader).setVisibility(View.GONE);
//                                System.out.println("notified data set changed");
                                } else {
                                    itemMusicSmallAdapter = new ItemMusicSmallAdapter(artists);
                                    recyclerArtist.setAdapter(itemMusicSmallAdapter);
//                                System.out.println("created new items adapter");
                                }
                            }
                        });
                } catch(IllegalStateException e) {}
//                itemHomeAdapter.notifyItemInserted(homeElements.size());
            }
        });


        return fragmentArtist;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerArtist.setLayoutManager(new LinearLayoutManager(getContext()));
        itemMusicSmallAdapter = new ItemMusicSmallAdapter(new ArrayList<>());

        if (artists.size() == 0) {
            getArtists();
        } else try {
            if (Integer.parseInt(artists.get(0).getId()) == History.playlist.tracks.get(0).getArtistId()) {
                refreshView.callOnClick();
            } else getArtists();
        } catch (NumberFormatException e){
            getArtists();
        }

        System.out.println("before setting adapter");

        recyclerArtist.setAdapter(itemMusicSmallAdapter);

        if(artists.size() > 0)  fragmentArtist.findViewById(R.id.progress_loader).setVisibility(View.GONE);


//        if(checkArtistsLoaded(1)) itemMusicSmallAdapter = new ItemMusicSmallAdapter(artists);
//            else itemMusicSmallAdapter = new ItemMusicSmallAdapter(new ArrayList<>());
//        recyclerArtist.setAdapter(itemMusicSmallAdapter);
//
//        new Thread(() -> {
//            if(checkArtistsLoaded(3000)) try {
//                refreshView.callOnClick();
//            } catch (Exception e){}
//        }).start();

        System.out.println("view created artists");

    }

    public static void getArtists() {
        new Thread(() -> {
            artists = new ArrayList<>();
        long start = System.currentTimeMillis();
        try {
            Playlist playlist = (Playlist)History.playlist.clone();
            if(playlist.tracks.size() > 0){
                    for(int i = 0; i < playlist.tracks.size(); i++){
                        Track t = playlist.tracks.get(i);
                        String artistId = String.valueOf(t.getArtistId());
                        boolean found = false;
                        for(int j = 0; j < i; j++){
                            if(String.valueOf(playlist.tracks.get(j).getArtistId()).equals(artistId)){
//                                System.out.println("found equal");
                                found = true;
                            }
                        }
                        boolean alreadyExists = found;
                        if(!alreadyExists) {
                            Playlist artist = DeezerService.getArtist(artistId);
                            if(artist.tracks.size() != 0 && artist.getPictureBig() != null && artist.getTitle() != null) {
                                artists.add(artist);
                                System.out.println("added artist: " + artist);
                            } else System.out.println("didn't add artist: " + artist);
//                            while(artist.tracks.size() == 0 || artist.getPictureBig() == null || artist.getTitle() == null) {
//                                System.out.println("thread blocked here");
//                                Thread.sleep(10);
//                            }
                            try {
                                refreshView.callOnClick();
                                System.out.println("refreshed view");
                            } catch (NullPointerException e){ }
                            if(artists.size() >= 20) break;
                        }
                    }
            }
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        long stop = System.currentTimeMillis();
        System.out.println("artists size: " + artists.size());
        System.out.println("getArtists time: " + (stop - start));

        }).start();
    }

    public boolean checkArtistsLoaded(int maxTime){
        long start = System.currentTimeMillis();

        int counter = 0;
        while(true){
            boolean breakAtTheEnd = true;
            if(artists.size() == 0 && History.playlist.tracks.size() > 0) breakAtTheEnd = false;
            List<Playlist> playlists = new ArrayList<Playlist>(artists);
            for (Playlist p : playlists){
                if(p.getTitle() == null || p.getPictureBig() == null){
                    breakAtTheEnd = false;
                }
            }
            if(breakAtTheEnd) break;
            else {
                counter++;
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if(counter >= maxTime) break;
        }
        long stop = System.currentTimeMillis();
//        System.out.println("check artists time: " + (stop - start));
//        for(Playlist p : artists) System.out.println(p.toString());
        if(counter == maxTime) return false;
        else return true;
    }
}