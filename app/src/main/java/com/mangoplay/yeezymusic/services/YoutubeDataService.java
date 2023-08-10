package com.mangoplay.yeezymusic.services;

import android.content.Context;

import com.mangoplay.yeezymusic.network.GetJSON;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class YoutubeDataService {

//    private static String searchTemplate = "https://www.googleapis.com/youtube/v3/search?part=snippet&q=%s&type=video&key=" + key;

    public static String getVideoId(String searchInput, Context context){
        searchInput = searchInput + " audio";
        searchInput = searchInput.replace(" ", "+");
//        String search = String.format(searchTemplate, searchInput);
//        System.out.println("search: " + search);
//        String videoId = null;
        VideoId videoId = new VideoId();

        String link = "https://www.youtube.com/results?search_query=" + searchInput;
        GetJSON.getVideoId(link, videoId, context);
        System.out.println("getting videoId");


        synchronized(videoId.lock) {
            while (videoId.getVideoId() == null) {
                try {
                    videoId.lock.wait();
                    int index = -1;
                    String response = videoId.getResponse();
                    index = response.indexOf("videoId");
                    if (index >= 0) {
//                    System.out.println(index);
                        String result = response.substring(index, index + 100);
//                    String result = inputLine.substring(index - 100, index + 100);
                        result = result.replace("\\x22", "\"");
                        result = result.substring("videoId".length() + 2);
                        result = result.substring(result.indexOf('"') + 1);
                        result = result.substring(0, result.indexOf('"'));
                        videoId.setVideoId(result);
                        System.out.println("videoId ready");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }


//        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
//        executor.scheduleAtFixedRate(() -> {
//            String str;
//                while (videoId.getVideoId() == null) {
//                    videoId.setCounter(videoId.getCounter() + 1);
//                }
//        }, 0, 100, TimeUnit.MILLISECONDS);

//        int counter = 0;
//        while(true){
//            if(videoId.getVideoId() == null) {
//                try {
//                    Thread.sleep(100);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            } else break;
//            counter++;
//            if(counter == 100) break;
//            System.out.println("counter in youtube service: " + counter + ", videoId: " + videoId.getVideoId());
//        }

//        if(videoId.getVideoId() == null)
//        {
//        try {
//            JSONObject object = GetJSON.getJSON(search);
//            JSONArray items = object.getJSONArray("items");
//            for(int i = 0; i < items.length();i++){
//                try{
//                JSONObject element = items.getJSONObject(i);
//                JSONObject id = element.getJSONObject("id");
//                videoId.setVideoId(id.getString("videoId"));
//                break;
//                } catch (Exception e){}
//            }
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        } catch (JSONException e) {
////            e.printStackTrace();
//        }
//        }
//        System.out.println("videoId: " + videoId);
        return videoId.getVideoId();
    }

    public static List<String> getShorts() { // call from secondary thread
        List<String> videoIds = new ArrayList<>();
        try {
            String link = "https://www.youtube.com/hashtag/shorts";
            URL url = new URL(link);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            StringBuilder stringBuilder = new StringBuilder();
            int count = 0;
            while ((inputLine = in.readLine()) != null) {
//            System.out.println(inputLine);
                int index = inputLine.indexOf("videoId");
                while (index > 0) {
                    int value = index + "\"videoId\"".length();
                    String videoId = inputLine.substring(value + 3, value + 14);
                    if (videoId.indexOf('"') < 0) {
                        videoIds.remove(videoId);
                        videoIds.add(videoId);
                    }
                    inputLine = inputLine.substring(value);
                    index = inputLine.indexOf("videoId");
                }
            }
//            for (String videoId : videoIds) {
//                System.out.println(videoId);
//            }
//            System.out.println("size: " + videoIds.size());
        } catch (IOException e){
            e.printStackTrace();
        }
        return videoIds;
    }
}
