package com.mangoplay.yeezymusic.objects;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.mangoplay.yeezymusic.MainActivity;
import com.mangoplay.yeezymusic.R;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.content.res.AppCompatResources;

public class Playlist implements Cloneable, Serializable {
    private String title;
    private String id;
    private String description;
    private int fans;
    private String pictureSmall;
    private String pictureBig;
    public List<Track> tracks = new ArrayList<>();
    private byte[] drawable = null;
    public boolean isOffline = false;
    public boolean isArtist = false;

    public Playlist(){
        tracks = new ArrayList<>();
        setDrawable(AppCompatResources.getDrawable(MainActivity.context, R.drawable.unknown));
    }

    public Playlist(String title, String id, String description, int fans, String pictureSmall, String pictureBig) {
        this.title = title;
        this.id = id;
        this.description = description;
        this.fans = fans;
        this.pictureSmall = pictureSmall;
        this.pictureBig = pictureBig;
    }

    @Override
    public String toString() {
        return "Playlist{" +
                "title='" + title + '\'' +
                ", id=" + id +
                ", description='" + description + '\'' +
                ", fans=" + fans +
                ", pictureSmall='" + pictureSmall + '\'' +
                ", pictureBig='" + pictureBig + '\'' +
                ", tracks=" + tracks +
                '}';
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getPictureSmall() {
        return pictureSmall;
    }

    public String getPictureBig() {
        return pictureBig;
    }

    public String getId() {
        return id;
    }

    public int getFans() {
        return fans;
    }

    public List<Track> getTracks() {
        return tracks;
    }

    public int findIndexOfTrack(int trackId){
        for(int i =0; i < tracks.size(); i++){
            if(tracks.get(i).getId() == trackId) return i;
        }
        return -1;
    }

    public Object clone() throws CloneNotSupportedException {
        super.clone();
        Playlist p = new Playlist(getTitle(), getId(), getDescription(), getFans(), getPictureSmall(), getPictureBig());
        p.tracks = new ArrayList<>();
        for(Track t : tracks){
            p.tracks.add(t);
        }
        return p;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Drawable getDrawable() {
        Drawable image = new BitmapDrawable(MainActivity.context.getResources(), BitmapFactory.decodeByteArray(drawable,0,drawable.length));
        return image;
    }

    public void setDrawable(Drawable drawable) {
        Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
        byte[] bitmapdata = stream.toByteArray();
        this.drawable = bitmapdata;
    }

    public void setPictureSmall(String pictureSmall) {
        this.pictureSmall = pictureSmall;
    }

    public void setPictureBig(String pictureBig) {
        this.pictureBig = pictureBig;
    }

    public void setFans(int fans) {
        this.fans = fans;
    }

    public void setArtist(boolean artist) {
        isArtist = artist;
    }


}
