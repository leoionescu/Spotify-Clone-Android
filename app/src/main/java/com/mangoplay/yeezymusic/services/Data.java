package com.mangoplay.yeezymusic.services;

import android.content.Context;

import com.mangoplay.yeezymusic.objects.Playlist;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

public class Data {
    public void writeData(Playlist playlist, Context context, String fileName)throws IOException {
        File dir = new File( "mydir");
        if(!dir.exists()){
            dir.mkdir();
        }
//        String fileName= "Test.txt";
        File file = new File(dir, fileName);
        FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(playlist);
        oos.close();
    }

    public static Playlist readData(Context context, String fileName) throws  ClassNotFoundException {
        Playlist playlist = null;
        FileInputStream fin = null;
        try {
            fin = context.openFileInput(fileName);
            ObjectInputStream ois = new ObjectInputStream(fin);
            playlist = (Playlist) ois.readObject();
            //ois.close();
            ois.close();
            fin.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return playlist;
    }

    public static void writePlaylists(List<Playlist> playlists, Context context, String fileName) throws IOException {
        File dir = new File( "mydir");
        if(!dir.exists()){
            dir.mkdir();
        }
//        String fileName= "Test.txt";
        File file = new File(dir, fileName);
        FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        try {
            oos.writeObject(playlists);
        }catch(ConcurrentModificationException e){
            System.out.println("concurrent modification exception when writing playlists");
            List<Playlist> copy = new ArrayList<>(playlists);
            try {
                oos.writeObject(playlists);
            } catch (Exception e2){
                e2.printStackTrace();
            }
        }
//        for(Playlist p : playlists) {
//            oos.writeObject(p);
//        }
        oos.close();
    }

    public static List<Playlist> readPlaylists(Context context, String fileName) throws ClassNotFoundException, IOException {
        List<Playlist> playlists = new ArrayList<>();
        FileInputStream fin = null;
        fin = context.openFileInput(fileName);
        ObjectInputStream ois = new ObjectInputStream(fin);
        playlists = (List<Playlist>) ois.readObject();
        //ois.close();
        ois.close();
        fin.close();

        return playlists;
    }

    public static void saveYoutubePlayer(YouTubePlayer youTubePlayer, Context context, String fileName) throws IOException {
        File dir = new File( "mydir");
        if(!dir.exists()){
            dir.mkdir();
        }
//        String fileName= "Test.txt";
        File file = new File(dir, fileName);
        FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(youTubePlayer);
        oos.close();
    }

    public static YouTubePlayer readYoutubePlayer(Context context, String fileName){
        YouTubePlayer youTubePlayer = null;
        FileInputStream fin = null;
        try {
            fin = context.openFileInput(fileName);
            ObjectInputStream ois = new ObjectInputStream(fin);
            youTubePlayer = (YouTubePlayer) ois.readObject();
            //ois.close();
            ois.close();
            fin.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return youTubePlayer;
    }
}
