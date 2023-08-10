package com.mangoplay.yeezymusic.objects;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.mangoplay.yeezymusic.MainActivity;
import com.mangoplay.yeezymusic.services.DeezerService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Genre {
    String id;
    String title;
    String pictureSmall;
    String pictureBig;
    public List<Playlist> artists = new ArrayList<>();
    boolean artistsLoaded = false;
    public List<String> ids = new ArrayList<>();

    public Genre(String id, String title, String pictureSmall, String pictureBig) {
        this.id = id;
        this.title = title;
        this.pictureSmall = pictureSmall;
        this.pictureBig = pictureBig;
    }

    public String getTitle() {
        return title;
    }

    public String getPictureSmall() {
        return pictureSmall;
    }

    public void getArtistList(){
//        try {
//            JSONObject object = GetJSON.getJSON("https://api.deezer.com/genre/" + id + "/artists");
//            JSONArray data = object.getJSONArray("data");
//            for(int i = 0 ;i < data.length(); i++){
//                String artistId = data.getJSONObject(i).getString("id");
//                ids.add(artistId);
//            }

        String source = "https://api.deezer.com/genre/" + id + "/artists";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, source, null, new Response.Listener<JSONObject>() {


                    @Override
                    public void onResponse(JSONObject object) {
                        JSONArray data = null;
                        try {
                            data = object.getJSONArray("data");
                            for(int i = 0 ;i < data.length(); i++){
                                String artistId = data.getJSONObject(i).getString("id");
                                ids.add(artistId);
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
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(2000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.context);
        requestQueue.add(jsonObjectRequest);

        int counter = 0;
        while(ids.size() == 0){
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            counter++;
            if(counter == 3000) break;
        }
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        while(true){
            boolean end = true;
            List<String> idsCopy = new ArrayList<String>(ids);
            for(String id : idsCopy){
                if(id == null) end = false;
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(end) break;
            counter++;
            if(counter == 3000) break;
        }
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

//        System.out.println("artist list loaded for genre: " + title);
//        System.out.println("ids size: " + ids.size());


//            System.out.println("ids: " + ids.toString());
//            for(String artistId : ids){
//                new Thread(() -> {
//                    Playlist p = DeezerService.getArtist(artistId);
//                    if(p != null) {
//                        artists.add(p);
//                    }
//                }).start();
//            }
//            new Thread(() -> {
//                while(artists.size() < ids.size()){
//                    try {
//                        Thread.sleep(10);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//                artistsLoaded = true;
//            }).start();




//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        } catch (JSONException e) {
//            System.out.println("in artistList id: " + id);
//            e.printStackTrace();
//        }
    }

    public void getArtists(){
        for(int i = 0; i < ids.size(); i++){
            final String artistId = ids.get(i);
            final int index = i;
            if(i == 20) break;
                new Thread(() -> {
                    if(index > 24 && index != 0) {
                        try {
                            Thread.sleep(10000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
//                    System.out.println("position: " + index + ", artistsId: " + artistId);
                    Playlist p = DeezerService.getArtist(artistId);
                        artists.add(p);
                }).start();
            }
            new Thread(() -> {
                while(artists.size() < ids.size()){
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                artistsLoaded = true;
            }).start();
    }
}
