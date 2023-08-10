package com.mangoplay.yeezymusic.objects;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;

import com.mangoplay.yeezymusic.MainActivity;
import com.mangoplay.yeezymusic.R;
import com.mangoplay.yeezymusic.services.Data;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.core.content.ContextCompat;

import static com.mangoplay.yeezymusic.ui.playlist.AddToPlaylistFragment.track;

public class ForYou {
    public static List<Playlist> forYouPlaylists = new ArrayList<>();

    public static void saveForYouPlaylists(List<Playlist> playlists){
        forYouPlaylists = playlists;
        List<Bitmap> frames = new ArrayList<>();
        frames.add(((BitmapDrawable) ContextCompat.getDrawable(MainActivity.context,R.drawable.daily_mix1)).getBitmap());
        frames.add(((BitmapDrawable) ContextCompat.getDrawable(MainActivity.context,R.drawable.daily_mix2)).getBitmap());
        frames.add(((BitmapDrawable) ContextCompat.getDrawable(MainActivity.context,R.drawable.daily_mix3)).getBitmap());
        frames.add(((BitmapDrawable) ContextCompat.getDrawable(MainActivity.context,R.drawable.daily_mix4)).getBitmap());
        frames.add(((BitmapDrawable) ContextCompat.getDrawable(MainActivity.context,R.drawable.daily_mix5)).getBitmap());
        frames.add(((BitmapDrawable) ContextCompat.getDrawable(MainActivity.context,R.drawable.daily_mix6)).getBitmap());
        List<String> titles = new ArrayList<>();
        for(int i = 1; i <= frames.size(); i++) titles.add("Daily mix " + i);
        for(Playlist playlist : playlists){
            Bitmap frame = frames.remove(0);
//            Bitmap trackCover = ((BitmapDrawable)track.getDrawable()).getBitmap();
            Bitmap trackCover = ((BitmapDrawable)playlist.getDrawable()).getBitmap();
            frame = Bitmap.createScaledBitmap(frame, trackCover.getWidth(), trackCover.getHeight(), true);
            Bitmap result = overlay(trackCover, frame);
            playlist.setDrawable(new BitmapDrawable(MainActivity.r, result));
            playlist.setTitle(titles.remove(0));
        }
        try {
            Data.writePlaylists(forYouPlaylists, MainActivity.context, "forYouPlaylists.bin");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Playlist> readForYouPlaylists(Context context){
        try {
            forYouPlaylists = Data.readPlaylists(context, "forYouPlaylists.bin");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return forYouPlaylists;
    }

    private static Bitmap overlay(Bitmap bmp1, Bitmap bmp2) {
        Bitmap bmOverlay = Bitmap.createBitmap(bmp1.getWidth(), bmp1.getHeight(), bmp1.getConfig());
        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(bmp1, new Matrix(), null);
        canvas.drawBitmap(bmp2, new Matrix(), null);
        return bmOverlay;
    }
}
