package com.mangoplay.yeezymusic.objects;

import com.mangoplay.yeezymusic.MainActivity;
import com.mangoplay.yeezymusic.services.Data;

import java.io.IOException;

public class History {

    public static Playlist playlist = new Playlist();
    public static Data data = new Data();

    public static void writeHistory(){
        try {
            data.writeData(playlist, MainActivity.context, "history.bin");
            System.out.println("history saved, size: " + playlist.getTracks().size());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readData(){
        try {
            playlist = Data.readData(MainActivity.context, "history.bin");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (playlist == null){
            playlist = new Playlist();
            System.out.println("history read, size: " + playlist.getTracks().size());
        }
    }

    public static void add(Track t) {
        int index = playlist.findIndexOfTrack(t.getId());
        System.out.println("index: " + index);
        if (index == -1) {
            if (playlist.tracks.size() > 50) {
                playlist.tracks.remove(50);
            }
        } else {
            playlist.tracks.remove(index);
        }
        playlist.tracks.add(0, t);
        writeHistory();
        MainActivity.homeFragment.fillHeardRecently();
//        new Thread(() -> {
//            ArtistFragment.getArtists();
//            Library.artists = ArtistFragment.artists;
//            Library.writeArtists();
//        }).start();
//        new Thread(() -> {
//            AlbumsFragment.getAlbums();
//            Library.albums = AlbumsFragment.albums;
//            Library.writeAlbums();
//        }).start();
    }
}
