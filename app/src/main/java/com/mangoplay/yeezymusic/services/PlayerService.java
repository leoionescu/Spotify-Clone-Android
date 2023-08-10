package com.mangoplay.yeezymusic.services;

import com.mangoplay.yeezymusic.MainActivity;
import com.mangoplay.yeezymusic.network.GetJSON;
import com.mangoplay.yeezymusic.network.GetRequest;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.ExecutionException;


public class PlayerService {
    private static SimpleExoPlayer player = new SimpleExoPlayer.Builder(MainActivity.context).build();

    public static void play(String artist, String song) throws IOException, ExecutionException, InterruptedException, JSONException {
        System.out.println("artist: " + artist);
        System.out.println("song: " + song);
        String source = "https:" + getPlayableLink(artist, song);
        System.out.println(source);
        MediaItem mediaItem = MediaItem.fromUri(source);
        player.setMediaItem(mediaItem);
        player.prepare();
        player.play();
    }

//    public static void play(Track track) throws IOException, ExecutionException, InterruptedException, JSONException {
//        System.out.println("artist: " + track.getArtist());
//        System.out.println("song: " + track.getTitle());
//        String source = "https:" + getPlayableLink(track.getArtist(), track.getTitle());
//        System.out.println(source);
//        MediaItem mediaItem = MediaItem.fromUri(source);
//        player.setMediaItem(mediaItem);
//        player.prepare();
//        player.play();
//    }

    private static String getPlayableLink(String artist, String song) throws IOException, ExecutionException, InterruptedException, JSONException {
        artist = artist.replace(' ',  '+');
        song = song.replace(' ',  '+');
        String url = "https://www.jango.com/music/" + artist +"/" + song + "/";
        System.out.println("jango link: " + url);
        GetRequest getRequest = new GetRequest(url);
        String link = getRequest.execute().get();
        System.out.println("getRequest: " + link);
        JSONObject jsonObject = GetJSON.getJSON(link);
        return jsonObject.getString("url");
    }
}
