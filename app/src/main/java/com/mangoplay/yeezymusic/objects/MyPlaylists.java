package com.mangoplay.yeezymusic.objects;

import com.mangoplay.yeezymusic.MainActivity;
import com.mangoplay.yeezymusic.R;
import com.mangoplay.yeezymusic.services.Data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.content.res.AppCompatResources;

public class MyPlaylists {
    public static List<Playlist> playlists = new ArrayList<>();
    private Data data = new Data();

    public static void readPlaylists(){
        try {
            playlists = Data.readPlaylists(MainActivity.context, "myPlaylists.bin");
        } catch (ClassNotFoundException | IOException e) {
            System.out.println("IOException");
//            e.printStackTrace();
            System.out.println("class not found exception");
            Playlist liked = new Playlist();
            liked.setTitle("Liked");
            liked.setDescription("My favorite tracks");
            liked.setId("0");
//            liked.setDrawable(AppCompatResources.getDrawable(MainActivity.context, R.drawable.unknown));
            liked.setDrawable(AppCompatResources.getDrawable(MainActivity.context, R.drawable.liked_playlist));
//            liked.setDrawable(AppCompatResources.getDrawable(MainActivity.context, R.drawable.ic_baseline_favorite_border_24));
            playlists = new ArrayList<>();
            playlists.add(liked);
        }

    }

    public static void writePlaylists(){
        try {
            Data.writePlaylists(playlists,MainActivity.context, "myPlaylists.bin");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Playlist getPlaylistById(String id){
        for(Playlist p : playlists){
            if(p.getId().equals(id)) return p;
        }
        return null;
    }

}
