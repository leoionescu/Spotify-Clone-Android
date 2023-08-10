package com.mangoplay.yeezymusic.items;

import com.mangoplay.yeezymusic.objects.Playlist;

public class ItemMusic {

    private String image;
    private String title;
    private String description;
    private Playlist playlist;

    public ItemMusic(String image, String title, String description, Playlist playlist) {
        this.image = image;
        this.title = title;
        this.description = description;
        this.playlist = playlist;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Playlist getPlaylist() {
        return playlist;
    }

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }
}
