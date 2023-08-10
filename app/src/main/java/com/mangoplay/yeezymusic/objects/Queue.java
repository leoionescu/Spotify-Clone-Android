package com.mangoplay.yeezymusic.objects;

import com.mangoplay.yeezymusic.MainActivity;
import com.mangoplay.yeezymusic.services.Data;

import java.io.IOException;
import java.util.ArrayList;

public class Queue {
    public static Playlist playlist = new Playlist();
    public static Playlist generatedPlaylist = new Playlist();
    public static Playlist combinedPlaylist = new Playlist();
    public static Data data = new Data();

    public static void writeQueue(){
        try {
            data.writeData(playlist, MainActivity.context, "queue.bin");
            System.out.println("queue saved, size: " + playlist.getTracks().size());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readQueue(){
        try {
            playlist = Data.readData(MainActivity.context, "queue.bin");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (playlist == null){
            playlist = new Playlist();
            System.out.println("queue read, size: " + playlist.getTracks().size());
        }
    }

    public static void add(Track t){
        if(playlist.tracks.size() == 50)
            System.out.println("queue is too big");
        else {
            playlist.tracks.add(t);
            writeQueue();
        }
    }

    public static void addToGeneratedPlaylist(Track t){
        if(generatedPlaylist.tracks.size() == 50) generatedPlaylist.tracks.remove(generatedPlaylist.tracks.size() - 1);
        generatedPlaylist.tracks.add(t);
    }

    public static void refreshGeneratedPlaylist(){
        generatedPlaylist.tracks = new ArrayList<>();
    }

    public static Track getNext(){
        Track t;
        if(playlist.tracks.size() > 0){
            t = playlist.tracks.remove(0);
        } else if(generatedPlaylist.tracks.size() > 0){
            t = generatedPlaylist.tracks.remove(0);
        } else t = null;
        return t;
    }

    public static Playlist getCombinedPlaylist(){
//        try {
//            Playlist p = (Playlist)playlist.clone();
//            p.tracks.addAll(generatedPlaylist.tracks);
//            return p;
//        } catch (CloneNotSupportedException e) {
//            e.printStackTrace();
//        }
        combinedPlaylist.tracks = new ArrayList<>();
        combinedPlaylist.tracks.addAll(playlist.tracks);
        combinedPlaylist.tracks.addAll(generatedPlaylist.tracks);
        return combinedPlaylist;
    }
}
