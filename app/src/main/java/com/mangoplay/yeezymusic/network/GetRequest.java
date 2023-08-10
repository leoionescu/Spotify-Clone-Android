package com.mangoplay.yeezymusic.network;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class GetRequest extends AsyncTask<Void, Void, String> {

    private byte[] b;
    private String source = null;

    public GetRequest(String source) {
        this.source = source;
    }

    @Override
    protected String doInBackground(Void... Voids) {
        URL url = null;
        String stationId = null;
        String sessionId = null;
        try {
            url = new URL(source);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            StringBuilder stringBuilder = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                int index1 = inputLine.indexOf("station_id");
                int index2 = inputLine.indexOf("session_id");
                if (index1 > 0 && stationId == null) {
                    inputLine = inputLine.substring(index1);
                    inputLine = inputLine.substring(inputLine.indexOf('"') + 1);
                    inputLine = inputLine.substring(inputLine.indexOf('"') + 1);
                    stationId = inputLine.substring(0,inputLine.indexOf('"'));
                }
                if(index2 > 0 && sessionId == null){
                    inputLine = inputLine.substring(index2);
                    inputLine = inputLine.substring(inputLine.indexOf('"') + 1);
                    inputLine = inputLine.substring(0, inputLine.indexOf('"'));
                    sessionId = inputLine;
                    System.out.println(sessionId);
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "https://www.jango.com/streams/info?url=true&first_time=1&sid=" + sessionId + "&stid=" + stationId;
    }
}
