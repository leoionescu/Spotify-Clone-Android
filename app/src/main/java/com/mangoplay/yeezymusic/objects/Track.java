package com.mangoplay.yeezymusic.objects;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.mangoplay.yeezymusic.MainActivity;
import com.mangoplay.yeezymusic.R;
import com.mangoplay.yeezymusic.network.GetDrawable;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.concurrent.ExecutionException;

import androidx.appcompat.content.res.AppCompatResources;

public class Track implements Serializable {
    private String title;
    private String artist;
    private int id;
    private int artistId;
    private int albumId;
    private String albumTitle;
    private String coverSmall;
    private String coverBig;
    private byte[] drawable = null;
    private String drawableSize;


    public Track(String title, String artist, int id, int artistId, String albumTitle, int albumId, String coverSmall, String coverBig) {
        this.artist = artist;
        this.title = title;
        this.id = id;
        this.artistId = artistId;
        this.albumId = albumId;
        this.albumTitle = albumTitle;
        this.coverSmall = coverSmall;
        this.coverBig = coverBig;
    }

    @Override
    public String toString() {
        return "Track{" +
                "title='" + title + '\'' +
                ", artist='" + artist + '\'' +
                ", id=" + id +
                ", artistId=" + artistId +
                ", albumId=" + albumId +
                ", albumTitle='" + albumTitle + '\'' +
                ", coverSmall='" + coverSmall + '\'' +
                ", coverBig='" + coverBig + '\'' +
                '}';
    }

    public Drawable getDrawable() {
        Drawable image = AppCompatResources.getDrawable(MainActivity.context, R.drawable.unknown);
        try {
            image = new BitmapDrawable(MainActivity.context.getResources(), BitmapFactory.decodeByteArray(drawable, 0, drawable.length));
        } catch (NullPointerException e){
            GetDrawable getDrawable = new GetDrawable(this, true);
            try {
                image = getDrawable.execute().get();
            } catch (ExecutionException executionException) {
                executionException.printStackTrace();
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
        }
        return image;
    }

    public void setDrawable(Drawable drawable, String drawableSize) {
        Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
        byte[] bitmapdata = stream.toByteArray();
        this.drawable = bitmapdata;
        this.drawableSize = drawableSize;
    }

    public String getName(){
        return title + " " + artist;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public int getArtistId(){return artistId;}

    public int getAlbumId(){return albumId;}

    public String getCoverSmall() {
        return coverSmall;
    }

    public String getCoverBig() {
        return coverBig;
    }

    public String getDrawableSize() {
        return drawableSize;
    }

    public int getId() {
        return id;
    }

    public void save(String fileName) throws IOException {
        File dir = new File( "mydir");
        if(!dir.exists()){
            dir.mkdir();
        }
//        String fileName= "Test.txt";
        File file = new File(dir, fileName);
        FileOutputStream fos = MainActivity.context.openFileOutput(fileName, Context.MODE_PRIVATE);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(this);
        oos.close();
    }

    public static Track read(String fileName) throws IOException{
        Track track = null;
        FileInputStream fin = null;
        try {
            fin = MainActivity.context.openFileInput(fileName);
            ObjectInputStream ois = new ObjectInputStream(fin);
            track = (Track) ois.readObject();
            //ois.close();
            ois.close();
            fin.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return track;
    }
}
