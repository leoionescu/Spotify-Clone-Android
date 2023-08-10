package com.mangoplay.yeezymusic.network;

import android.content.Context;
import android.os.AsyncTask;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mangoplay.yeezymusic.MainActivity;
import com.mangoplay.yeezymusic.services.VideoId;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

class GetJSONAsyncTask extends AsyncTask<Void, Void, byte[]> {

    private byte[] b;
    private String url;
    public GetJSONAsyncTask(String url) {
        this.url = url;
    }

    @Override
    protected byte[] doInBackground(Void... Voids) {
//        System.out.println("doInBackground started");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        InputStream is = null;
        try {
            URL Url = new URL(url);
            HttpURLConnection con = (HttpURLConnection) Url.openConnection();
//            conn.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Win98; en-US; rv:1.7.2) Gecko/20040803");
            con.connect();
            is = con.getInputStream();
            byte[] byteChunk = new byte[4096]; // Or whatever size you want to read in at a time.
            int n;

            while ( (n = is.read(byteChunk)) > 0 ) {
                baos.write(byteChunk, 0, n);
            }
            is.close();
        }
        catch (IOException e) {
            e.printStackTrace ();
            // Perform any other exception handling that's appropriate.
        }

//        System.out.println("in async class" + baos.toByteArray());
//        Log.d("myTag", baos.toByteArray().toString());



        this.b = baos.toByteArray();

        return baos.toByteArray();

    }

    public byte[] getB() {
        return b;
    }
}

class GetStringAsyncTask extends AsyncTask<Void, Void, String> {

    private byte[] b;
    private String url;
    public GetStringAsyncTask(String url) {
        this.url = url;
    }

    @Override
    protected String doInBackground(Void... Voids) {
        try {
            URL Url = new URL(url);
            HttpURLConnection con = null;
            con = (HttpURLConnection) Url.openConnection();
            con.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            StringBuilder stringBuilder = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                int index = -1;
                index = inputLine.indexOf("videoId");
                if(index >= 0) {
//                    System.out.println(index);
                    String result = inputLine.substring(index, index + 100);
//                    String result = inputLine.substring(index - 100, index + 100);
                    result = result.replace("\\x22", "\"");
                    System.out.println(result);
                    result = result.substring("videoId".length() + 2);
                    System.out.println(result);
                    result = result.substring(result.indexOf('"') + 1);
                    System.out.println(result);
                    result = result.substring(0, result.indexOf('"'));
                    System.out.println(result);
                    return result;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public byte[] getB() {
        return b;
    }
}


public class GetJSON {

    public static JSONObject getJSON(String source) throws ExecutionException, InterruptedException, UnsupportedEncodingException, JSONException {

        GetJSONAsyncTask getJSONAsyncTask = new GetJSONAsyncTask(source);
        JSONObject jsonObject = new JSONObject(new String(getJSONAsyncTask.execute().get(), "UTF-8"));
        return jsonObject;
    }

    public static String getString(String source) throws ExecutionException, InterruptedException, UnsupportedEncodingException {
        GetStringAsyncTask getStringAsyncTask = new GetStringAsyncTask(source);
        String result =  getStringAsyncTask.execute().get();
        return result;
    }

    public static void getVideoId(String source, VideoId videoId, Context context){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, source,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        videoId.setResponse(response);
                        long start = System.currentTimeMillis();
                        synchronized(videoId.lock) {
                            videoId.lock.notify();
                        }
                        long stop = System.currentTimeMillis();
                        System.out.println("time in onResponse: " + (stop - start));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("error getting videoId");
                synchronized(videoId.lock) {
                    videoId.lock.notify();
                }
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

}
