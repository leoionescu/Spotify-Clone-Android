package com.mangoplay.yeezymusic.objects;

import com.mangoplay.yeezymusic.MainActivity;
import com.mangoplay.yeezymusic.services.Data;
import com.mangoplay.yeezymusic.ui.library.TabsFragments.AlbumsFragment;
import com.mangoplay.yeezymusic.ui.library.TabsFragments.ArtistFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Library {
    public static List<Playlist> artists = new ArrayList<>();
    public static List<Playlist> albums = new ArrayList<>();

    public static void writeArtists(){
        try {
            Data.writePlaylists(artists, MainActivity.context, "artists.bin");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readArtists(){
        try {
            artists = Data.readPlaylists(MainActivity.context,"artists.bin");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArtistFragment.artists = artists;
        if(artists.size() == 0){
            new Thread(() -> {
                ArtistFragment.getArtists();
                artists = ArtistFragment.artists;
            }).start();
        }
    }

    public static void writeAlbums(){
        try {
            Data.writePlaylists(albums, MainActivity.context, "albums.bin");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readAlbums(){
        try {
            albums = Data.readPlaylists(MainActivity.context,"albums.bin");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        AlbumsFragment.albums = albums;
        if(albums.size() == 0){
            new Thread(() -> {
                AlbumsFragment.getAlbums();
                albums = AlbumsFragment.albums;
            }).start();
        }
    }
}
