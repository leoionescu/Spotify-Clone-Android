package com.mangoplay.yeezymusic.services;

public class VideoId {
    private String videoId = null;
    private String response = null;
    int counter = 0;
    public final Object lock = new Object();

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
