package com.mangoplay.yeezymusic.services;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.exoplayer2.ThumbRating;
import com.google.gson.JsonArray;
import com.mangoplay.yeezymusic.MainActivity;
import com.mangoplay.yeezymusic.objects.Lock;
import com.mangoplay.yeezymusic.objects.Playlist;
import com.mangoplay.yeezymusic.objects.Track;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class LastFmService {

    static String apiKey = "a1cec701b89c6ab425b8c137968b3434";
    String sharedSecret = "2d0aede2d9a2a9015ead26b882fac416";


    public static Playlist getSimilarTracks(Track track, int nbOfTracks){ //called from secondary thread
        String artist = track.getArtist();
        String trackName = track.getTitle();
        String string = "https://ws.audioscrobbler.com/2.0/?method=track.getsimilar&artist=" + artist + "&track=" + trackName + "&api_key=" + apiKey + "&format=json";
        string = string.replace(" ", "%20");
        String similarTracksLink = string.replace("'", "%27");
        Lock lock = new Lock();
        Playlist playlist = new Playlist();
        playlist.setTitle(trackName + " - " + artist);
        System.out.println("similarTracksURL: " + similarTracksLink);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, similarTracksLink, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject object) {
                        System.out.println("onResponse similarTracks");
                        List<Thread> threads = new ArrayList<>();
                        new Thread(() -> {
                        try {
                            JSONObject similarTracks = object.getJSONObject("similartracks");
                            JSONArray track = similarTracks.getJSONArray("track");
                            int size = 0;
                            if(nbOfTracks <= track.length()) size = nbOfTracks;
                            else size = track.length();
                            for(int i = 0; i < size; i++){
//                                System.out.println("i : " + i);
                                JSONObject element = track.getJSONObject(i);
                                String title = element.getString("name");
                                String artist = element.getJSONObject("artist").getString("name");

//                                        Playlist playlist1 = DeezerService.search(title + " " + artist);
                                        Thread thread = new Thread(() -> {
                                            Playlist playlist1 = DeezerService.searchTitleAndArtist(title, artist);
                                            try {
                                                playlist.tracks.add(playlist1.tracks.get(0));
//                                            System.out.println("new search result: " + playlist1.tracks.get(0).toString());
                                            } catch (IndexOutOfBoundsException e){ }
                                        });
                                        threads.add(thread);
//                                        thread.start();
//                                if(playlist.tracks.size() == nbOfTracks) {
//                                    synchronized (lock.lock){
//                                        lock.lock.notify();
//                                    }
//                                    break;
//                                }
                            }
                            System.out.println(trackName + " number of threads: " + threads.size());

                            int nbOfThreads = 2;
                            int nbOfRunningThreads = 0;
                            List<Thread> runningThreads = new ArrayList<>();
                            for(int i = 0; i < nbOfThreads; i++){
                                try {
                                    threads.get(0).start();
                                    runningThreads.add(threads.remove(0));
                                } catch (IndexOutOfBoundsException e){}
                            }

                            while(threads.size() > 0){
                                for(int i = 0; i < runningThreads.size(); i++) {
                                        if(!runningThreads.get(i).isAlive()){
                                            try {
                                                threads.get(0).start();
                                                runningThreads.set(i, threads.remove(0));
                                            } catch (IndexOutOfBoundsException e){
                                                break;
                                            }
                                        }
                                }
                                try {
                                    Thread.sleep(10);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }

                            for(int i = 0; i < threads.size(); i++){
                                try {
                                    System.out.println(trackName + " joining thread");
                                    threads.get(i).join();
                                    System.out.println(trackName + " joined thread");
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            synchronized (lock.lock){
                                lock.lock.notify();
                            }
                        } catch (JSONException e) {
                            System.out.println("jsonException");
                            e.printStackTrace();
                            synchronized (lock.lock){
                                lock.lock.notify();
                            }
                        }
                        }).start();
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("error response for URL: " + similarTracksLink);
                        System.out.println(error);
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.context);
        requestQueue.add(jsonObjectRequest);



        synchronized (lock.lock){
            try {
                lock.lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("returned similar tracks for: " + track.getTitle());
        return playlist;
    }

    public static Playlist search(String text){
        String string = "http://ws.audioscrobbler.com/2.0/?method=track.search&track=" + text + "&api_key=" + apiKey + "&format=json";
        String searchLink = string.replace(" ", "%20");
        return null;
    }
}
