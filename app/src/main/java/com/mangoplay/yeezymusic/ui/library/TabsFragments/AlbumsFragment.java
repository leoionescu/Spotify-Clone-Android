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
import com.mangoplay.yeezymusic.items.ItemMusic;
import com.mangoplay.yeezymusic.R;
import com.mangoplay.yeezymusic.adapters.ItemMusicSmallAdapter;
import com.mangoplay.yeezymusic.objects.History;
import com.mangoplay.yeezymusic.objects.Playlist;
import com.mangoplay.yeezymusic.objects.Track;
import com.mangoplay.yeezymusic.services.DeezerService;

import java.util.ArrayList;
import java.util.List;

public class AlbumsFragment extends Fragment {

    View fragmentAlbums;
    RecyclerView recyclerAlbums;
    ArrayList<ItemMusic> arrayAlbums;
    public static List<Playlist> albums = new ArrayList<>();
    ItemMusicSmallAdapter itemMusicSmallAdapter;
    static Button refreshView;

    public AlbumsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentAlbums= inflater.inflate(R.layout.fragment_albums, container, false);
        recyclerAlbums = fragmentAlbums.findViewById(R.id.albums_recycler);

        System.out.println("albums create view");

        refreshView = new Button(MainActivity.context);
        refreshView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemMusicSmallAdapter != null && recyclerAlbums != null)
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(itemMusicSmallAdapter.getItemCount() > 0) {
//                                itemMusicSmallAdapter.notifyDataSetChanged();
                                itemMusicSmallAdapter.notifyItemInserted(itemMusicSmallAdapter.getItemCount());
                                fragmentAlbums.findViewById(R.id.progress_loader).setVisibility(View.GONE);
                            } else {
                                itemMusicSmallAdapter = new ItemMusicSmallAdapter(albums);
                                recyclerAlbums.setAdapter(itemMusicSmallAdapter);
                            }
                        }
                    });
//                itemHomeAdapter.notifyItemInserted(homeElements.size());
            }
        });


        return fragmentAlbums;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerAlbums.setLayoutManager(new LinearLayoutManager(getContext()));
        itemMusicSmallAdapter = new ItemMusicSmallAdapter(new ArrayList<>());

        if(albums.size() == 0){
            getAlbums();
        } else try {
            if (Integer.parseInt(albums.get(0).getId()) == History.playlist.tracks.get(0).getAlbumId()) {
                refreshView.callOnClick();
            } else {
                getAlbums();
            }
        } catch (NumberFormatException e){
            getAlbums();
        }

        recyclerAlbums.setAdapter(itemMusicSmallAdapter);

        if(albums.size() > 0)  fragmentAlbums.findViewById(R.id.progress_loader).setVisibility(View.GONE);

    }

    public static void getAlbums() {
        new Thread(() -> {
            System.out.println("in getAlbums");
            albums = new ArrayList<>();
            long start = System.currentTimeMillis();
            try {
                Playlist playlist = (Playlist)History.playlist.clone();
                if(playlist.tracks.size() > 0){
                    for(int i = 0; i < playlist.tracks.size(); i++){
                        Track t = playlist.tracks.get(i);
                        String albumId = String.valueOf(t.getAlbumId());
                        boolean found = false;
                        for(int j = 0; j < i; j++){
                            if(String.valueOf(playlist.tracks.get(j).getAlbumId()).equals(albumId)){
//                                System.out.println("found equal");
                                found = true;
                            }
                        }
                        boolean alreadyExists = found;
                        if(!alreadyExists) {
                            Playlist album = DeezerService.getAlbum(albumId);
                            if(album.tracks.size() != 0 && album.getPictureBig() != null && album.getTitle() != null) {
                                albums.add(album);
                                System.out.println("added album");
                            }
//                            while(album.tracks.size() == 0 || album.getPictureBig() == null || album.getTitle() == null) {
//                                Thread.sleep(10);
//                            }
                            try {
                                refreshView.callOnClick();
                                System.out.println("refreshed view");
                            } catch (NullPointerException e){ }
                            if(albums.size() >= 20) break;
                        }
                    }
                }
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            long stop = System.currentTimeMillis();
//        System.out.println("getArtists time: " + (stop - start));

        }).start();

    }

    public boolean checkAlbumsLoaded(int maxTime){
        int counter = 0;
        while(true){
            boolean breakAtTheEnd = true;
            if(albums.size() == 0 && History.playlist.tracks.size() > 0) breakAtTheEnd = false;
            List<Playlist> playlists = new ArrayList<Playlist>(albums);
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
        if(counter >= maxTime) return false;
        else return true;
    }
}