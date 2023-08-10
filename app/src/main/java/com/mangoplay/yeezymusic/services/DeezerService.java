package com.mangoplay.yeezymusic.services;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.mangoplay.yeezymusic.MainActivity;
import com.mangoplay.yeezymusic.network.GetJSON;
import com.mangoplay.yeezymusic.objects.Genre;
import com.mangoplay.yeezymusic.objects.Lock;
import com.mangoplay.yeezymusic.objects.Playlist;
import com.mangoplay.yeezymusic.objects.Track;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class DeezerService {

    static JSONObject charts = null;

    public static void getCharts(){
        Lock lock = new Lock();
        String source = "https://api.deezer.com/chart&limit=10";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, source, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject object) {
                        charts = object;
                        synchronized (lock.lock){
                            lock.lock.notify();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("error response for URL: " + source);
                    }
                });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(2000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.context);
        requestQueue.add(jsonObjectRequest);
        synchronized (lock.lock){
            try {
                lock.lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static Playlist search(String text){
        Playlist playlist = new Playlist("searchResult", "1", text, 0, null, null);
        Track track = null;
        text = text.replace(" ", "+");
        text = text.replace("(", "+");
        text = text.replace(")", "+");
        text = text.replace("'", "+");
        text = text.replace("-", "+");
        String searchLink = "https://api.deezer.com/search?q=" + text;
//        System.out.println("searchLink: " + searchLink);

        Lock lock = new Lock();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, searchLink, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject object) {
//                        System.out.println("search got response");
                        JSONArray data = null;
                        try {
                            data = object.getJSONArray("data");
                            for(int i = 0; i < data.length(); i++) {
                                playlist.tracks.add(getTrack(data, i));
                            }
                            if(data.length() == 0) System.out.println("no results");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        synchronized (lock.lock){
                            lock.lock.notify();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("search error response");
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


        return playlist;
    }

    public static Playlist searchTitleAndArtist(String title, String artist){
        Playlist playlist = new Playlist("searchResult", "1", title + artist, 0, null, null);
        Track track = null;
        title = title.replace(" ", "+");
        title = title.replace("(", "+");
        title = title.replace(")", "+");
        title = title.replace("'", "+");
        title = title.replace("-", "+");

        artist = artist.replace(" ", "+");
        artist = artist.replace("(", "+");
        artist = artist.replace(")", "+");
        artist = artist.replace("'", "+");
        artist = artist.replace("-", "+");
        String searchLink = "https://api.deezer.com/search?q=artist:\"" + artist + "\" track:\"" + title + "\"";
//        System.out.println("searchLink: " + searchLink);

        Lock lock = new Lock();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, searchLink, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject object) {
//                        System.out.println("search got response");
                        JSONArray data = null;
                        try {
                            data = object.getJSONArray("data");
                            for(int i = 0; i < data.length(); i++) {
                                playlist.tracks.add(getTrack(data, i));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        synchronized (lock.lock){
                            lock.lock.notify();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

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


        return playlist;
    }

    public static Track getTrack(JSONArray data, int position) throws JSONException {
        Track track = null;
        try {
            JSONObject element = data.getJSONObject(position);
            int id = element.getInt("id");
            String title = element.getString("title");
            String artist = element.getJSONObject("artist").getString("name");
            int artistId = element.getJSONObject("artist").getInt("id");
            JSONObject album = element.getJSONObject("album");
            int albumId = album.getInt("id");
            String albumTitle = album.getString("title");
            String coverSmall = album.getString("cover_small");
            String coverBig = album.getString("cover_big");
            track = new Track(title, artist, id, artistId, albumTitle, albumId, coverSmall, coverBig);
        } catch (Exception e) {
        }

        return track;
    }

    private static Track getTrackFromAlbum(JSONArray data, int position, int albumId, String albumTitle, String coverSmall, String coverBig) throws JSONException {
        Track track = null;
        try {
            JSONObject element = data.getJSONObject(position);
            int id = element.getInt("id");
            String title = element.getString("title");
            String artist = element.getJSONObject("artist").getString("name");
            int artistId = element.getJSONObject("artist").getInt("id");
            track = new Track(title, artist, id, artistId, albumTitle, albumId, coverSmall, coverBig);
        } catch (Exception e) {
        }

        return track;
    }


    public static Playlist getChart(){
        Playlist playlist = null;
        try {
            JSONObject object = GetJSON.getJSON("https://api.deezer.com/chart&limit=50");
            playlist = new Playlist("Chart","0","Best songs right now",1000000, null, null);
            JSONArray data = object.getJSONObject("tracks").getJSONArray("data");
            for(int i = 0; i < data.length(); i++){
                playlist.tracks.add(getTrack(data, i));
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    return playlist;
    }

    public static Playlist getPlaylist(String playlistId){
        final Playlist playlist = new Playlist();

            String source = "https://api.deezer.com/playlist/" + playlistId;
            Lock lock = new Lock();
//            JSONObject object = GetJSON.getJSON("https://api.deezer.com/playlist/" + playlistId);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.GET, source, null, new Response.Listener<JSONObject>() {


                        @Override
                        public void onResponse(JSONObject object) {

                            String title = null;
                            try {
                                title = object.getString("title");
                                String description = object.getString("description");
                                int fans = object.getInt("fans");
                                String pictureSmall = object.getString("picture_small");
                                String pictureBig = object.getString("picture_big");
//                                playlist = new Playlist(title, playlistId, description, fans, pictureSmall, pictureBig);
                                playlist.setTitle(title);
                                playlist.setId(playlistId);
                                playlist.setDescription(description);
                                playlist.setFans(fans);
                                playlist.setPictureSmall(pictureSmall);
                                playlist.setPictureBig(pictureBig);
                                JSONArray data = object.getJSONObject("tracks").getJSONArray("data");
                                for(int i = 0; i < data.length(); i++){
                                    playlist.tracks.add(getTrack(data, i));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            synchronized (lock.lock){
                                lock.lock.notify();
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            System.out.println("error response for URL: " + source);
                            synchronized (lock.lock){
                                lock.lock.notify();
                            }
                        }
                    });
//        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(2000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.context);
        requestQueue.add(jsonObjectRequest);

        synchronized (lock.lock){
            try {
                lock.lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return playlist;
    }

    public static Playlist getArtistPlaylist(String id, String artistName, String description, String pictureSmall, String pictureBig){
        final Playlist playlist = new Playlist();

        String source = "https://api.deezer.com/artist/" + id + "/top?limit=50";
        Lock lock = new Lock();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, source, null, new Response.Listener<JSONObject>() {


                    @Override
                    public void onResponse(JSONObject object) {
                        playlist.setTitle(artistName);
                        playlist.setId(id);
                        playlist.setDescription(description);
                        playlist.setFans(0);
                        playlist.setPictureSmall(pictureSmall);
                        playlist.setPictureBig(pictureBig);
                        playlist.setArtist(true);
                        JSONArray data = null;
                        try {
                            data = object.getJSONArray("data");
                            for(int i = 0; i < data.length(); i++){
                                playlist.tracks.add(getTrack(data, i));
                            }
                        } catch (JSONException e) {
                            System.out.println("error in getArtistPlaylist, id: " + id);
                            System.out.println(object.toString());
                    }
                        synchronized (lock.lock){
                            lock.lock.notify();
                        }
//                        System.out.println("response for: " + playlist.getTitle());
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("error response for URL: " + source);
                        synchronized (lock.lock){
                            lock.lock.notify();
                        }
                    }
                });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(2000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.context);
        requestQueue.add(jsonObjectRequest);

        synchronized (lock.lock){
            try {
                lock.lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return playlist;
    }

    public static Playlist getArtist(String artistId){
        final Playlist playlist = new Playlist();

            String source = "https://api.deezer.com/artist/" + artistId;
            Lock lock = new Lock();
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.GET, source, null, new Response.Listener<JSONObject>() {


                        @Override
                        public void onResponse(JSONObject object) {
                            new Thread(() -> {
                                String artistName = null;
                                try {
                                    artistName = object.getString("name");
                                    String pictureSmall = object.getString("picture_small");
                                    String pictureBig = object.getString("picture_big");
                                    String description = null;
                                    Playlist result = getArtistPlaylist(artistId, artistName, description, pictureSmall, pictureBig);
                                    playlist.setTitle(artistName);
                                    playlist.setId(artistId);
                                    playlist.setDescription(description);
                                    playlist.setFans(0);
                                    playlist.setPictureSmall(pictureSmall);
                                    playlist.setPictureBig(pictureBig);
                                    playlist.setArtist(true);
                                    playlist.tracks = result.tracks;
                                } catch (JSONException e) {
                                    System.out.println("error in getArtist, id: " + artistId);
                                }
                                synchronized (lock.lock) {
                                    lock.lock.notify();
                                }
                            }).start();
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            System.out.println("error response for URL: " + source);
                            synchronized (lock.lock){
                                lock.lock.notify();
                            }
                        }
                    });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(2000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.context);
        requestQueue.add(jsonObjectRequest);

        synchronized (lock.lock){
            try {
                lock.lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return playlist;
    }

    public static List<Playlist> getTopArtists(){
        List<Playlist> playlists = new ArrayList<>();
        JSONObject object = null;
        try {
            int counter = 0;
            while(charts == null) {
                Thread.sleep(10);
                counter++;
                if(counter == 3000) break;
            }
            object = charts;
            JSONArray data = object.getJSONObject("artists").getJSONArray("data");
            for(int i = 0; i < data.length(); i++){
                JSONObject element = data.getJSONObject(i);
                String id = element.getString("id");
                String artistName = element.getString("name");
                String description = null;
                String pictureSmall = element.getString("picture_small");
                String pictureBig = element.getString("picture_big");
                new Thread(() -> {
                    Playlist playlist = getArtistPlaylist(id, artistName, description, pictureSmall, pictureBig);
                    playlists.add(playlist);
//                    System.out.println("added for: " + playlist.getTitle());
                }).start();
            }
            while(data.length() != playlists.size()) {
//                System.out.println("sleeping");
                Thread.sleep(10);
            }
            while(playlists.get(playlists.size() - 1).getTitle() == null) Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return playlists;
    }

    public static Playlist getAlbumPlaylist(int id, String albumName, String description, String coverSmall, String coverBig){
        final Playlist playlist = new Playlist();
//            JSONObject object = GetJSON.getJSON("https://api.deezer.com/album/" + id + "/tracks");

            String source = "https://api.deezer.com/album/" + id + "/tracks";
            Lock lock = new Lock();

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.GET, source, null, new Response.Listener<JSONObject>() {


                        @Override
                        public void onResponse(JSONObject object) {

//                            playlist = new Playlist(albumName, null, description, 0, coverSmall, coverBig);
//                            playlist.isArtist = true;
//                            JSONArray data = object.getJSONArray("data");
//                            for(int i = 0; i < data.length(); i++){
//                                playlist.tracks.add(getTrackFromAlbum(data, i, id, albumName, coverSmall, coverBig));
//                            }


                            playlist.setTitle(albumName);
                            playlist.setId(String.valueOf(id));
                            playlist.setDescription(description);
                            playlist.setFans(0);
                            playlist.setPictureSmall(coverSmall);
                            playlist.setPictureBig(coverBig);
                            playlist.setArtist(true);
                            JSONArray data = null;
                            try {
                                data = object.getJSONArray("data");
                                for(int i = 0; i < data.length(); i++){
                                    playlist.tracks.add(getTrackFromAlbum(data, i, id, albumName, coverSmall, coverBig));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            synchronized (lock.lock) {
                                lock.lock.notify();
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            System.out.println("error response for URL: " + source);
                            System.out.println(error);
//                            Thread.currentThread().interrupt();
                        }
                    });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(2000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.context);
        requestQueue.add(jsonObjectRequest);

        synchronized (lock.lock){
            try {
                lock.lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return playlist;
    }

    public static Playlist getAlbum(String albumId){
        final Playlist playlist = new Playlist();

        String source = "https://api.deezer.com/album/" + albumId;
        Lock lock = new Lock();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, source, null, new Response.Listener<JSONObject>() {


                    @Override
                    public void onResponse(JSONObject object) {
                        new Thread(() -> {
                            String artistName = null;
                            try {
                                String albumTitle = object.getString("title");
                                String coverSmall = object.getString("cover_small");
                                String coverBig = object.getString("cover_big");
                                String description = null;
                                Playlist result = getAlbumPlaylist(Integer.parseInt(albumId), albumTitle, description, coverSmall, coverBig);
                                playlist.setTitle(albumTitle);
                                playlist.setId(albumId);
                                playlist.setDescription(description);
                                playlist.setFans(0);
                                playlist.setPictureSmall(coverSmall);
                                playlist.setPictureBig(coverBig);
                                playlist.setArtist(true);
                                playlist.tracks = result.tracks;
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            synchronized (lock.lock) {
                                lock.lock.notify();
                            }
                        }).start();
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("error response for URL: " + source);
                        synchronized (lock.lock){
                            lock.lock.notify();
                        }
                    }
                });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(2000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.context);
        requestQueue.add(jsonObjectRequest);

        synchronized (lock.lock){
            try {
                lock.lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return playlist;
    }

    public static List<Playlist> getTopAlbums(){
        List<Playlist> playlists = new ArrayList<>();
        JSONObject object = null;
        try {
            int counter = 0;
            while(charts == null) {
                Thread.sleep(10);
                counter++;
                if(counter == 3000) break;
            }
            object = charts;
            JSONArray data = object.getJSONObject("albums").getJSONArray("data");
            for(int i = 0; i < data.length(); i++){
                JSONObject element = data.getJSONObject(i);
                int id = element.getInt("id");
                String albumTitle = element.getString("title");
                String description = null;
                String pictureSmall = element.getString("cover_small");
                String pictureBig = element.getString("cover_big");
                new Thread(() -> {
                    playlists.add(getAlbumPlaylist(id, albumTitle, description, pictureSmall, pictureBig));
                }).start();
            }
            while(data.length() != playlists.size()) Thread.sleep(10);
            while(playlists.get(playlists.size() - 1).getTitle() == null) Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return playlists;
    }

    public static List<Playlist> getTopPlaylists(){
        List<Playlist> playlists = new ArrayList<>();
        JSONObject object = null;
        try {
            int counter = 0;
            while(charts == null) {
                Thread.sleep(10);
                counter++;
                if(counter == 3000) break;
            }
            object = charts;
            JSONArray data = object.getJSONObject("playlists").getJSONArray("data");
            for(int i = 0; i < data.length(); i++){
                String id = data.getJSONObject(i).getString("id");
                new Thread(() -> {
                    playlists.add(getPlaylist(id));
                }).start();
            }
            while(data.length() != playlists.size()) Thread.sleep(10);
            while(playlists.get(playlists.size() - 1).getTitle() == null) Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return playlists;
    }

    public static List<Genre> getGenres(){
        List<Genre> genres = new ArrayList<>();
//            JSONObject object = GetJSON.getJSON("https://api.deezer.com/editorial");

            String source = "https://api.deezer.com/editorial";
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.GET, source, null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject object) {

                            JSONArray data = null;
                            try {
                                data = object.getJSONArray("data");
                                for(int i = 1; i < data.length(); i++){
                                    JSONObject element = data.getJSONObject(i);
                                    String id = element.getString("id");
                                    String title = element.getString("name");
                                    String pictureSmall = element.getString("picture_small");
                                    String pictureBig = element.getString("picture_big");
                                    Genre genre = new Genre(id, title, pictureSmall, pictureBig);
                                    genres.add(genre);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            System.out.println("error response for URL: " + source);
                        }
                    });
//            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(2000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.context);
            requestQueue.add(jsonObjectRequest);

        return genres;
    }

    public static void getCountryIso(Lock lock, String[] list){
        String source = "https://api.deezer.com/infos";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, source, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject object) {
                        try {
//                            String countryIso = object.getString("country_iso");
                            list[0] = object.getString("country_iso");
                            synchronized (lock.lock){
                                lock.lock.notify();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("error response for URL: " + source);
                    }
                });
//            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(2000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.context);
        requestQueue.add(jsonObjectRequest);
    }

}
