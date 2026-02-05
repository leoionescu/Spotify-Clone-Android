package com.mangoplay.yeezymusic.ui.tinder;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.mangoplay.yeezymusic.R;
import com.mangoplay.yeezymusic.adapters.CardStackAdapter;
import com.mangoplay.yeezymusic.objects.ForYou;
import com.mangoplay.yeezymusic.objects.Playlist;
import com.mangoplay.yeezymusic.objects.Spot;
import com.mangoplay.yeezymusic.objects.Track;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import static com.mangoplay.yeezymusic.objects.MyPlaylists.playlists;

public class TinderActivity extends AppCompatActivity {
    public static Context context;
    public static int currentPosition;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tinder);
        TinderActivity.context = getApplicationContext();
        List<Spot> spots = new ArrayList<>();
        spots.add(new Spot("Yasaka Shrine", "Kyoto", "https://source.unsplash.com/Xq1ntWruZQI/600x800"));
        spots.add(new Spot("Fushimi Inari Shrine","Kyoto", "https://source.unsplash.com/NYyCqdBOKwc/600x800"));
        spots.add(new Spot("Bamboo Forest", "Kyoto", "https://source.unsplash.com/buF62ewDLcQ/600x800"));
        spots.add(new Spot("Brooklyn Bridge", "New York", "https://source.unsplash.com/THozNzxEP3g/600x800"));
        spots.add(new Spot("Empire State Building", "New York", "https://source.unsplash.com/USrZRcRS2Lw/600x800"));
        spots.add(new Spot("The statue of Liberty", "New York", "https://source.unsplash.com/PeFk7fzxTdk/600x800"));
        spots.add(new Spot("Louvre Museum", "Paris", "https://source.unsplash.com/LrMWHKqilUw/600x800"));
        spots.add(new Spot("Eiffel Tower", "Paris", "https://source.unsplash.com/HN-5Z6AmxrM/600x800"));
        spots.add(new Spot("Big Ben", "London", "https://source.unsplash.com/CdVAUADdqEc/600x800"));
        spots.add(new Spot("Great Wall of China", "China", "https://source.unsplash.com/AWh9C-QjhE4/600x800"));

        List<Track> tracks = createTrackList();
    }

    private List<Track> createTrackList() {
        List<Playlist> playlists = ForYou.readForYouPlaylists(context);
        List<Track> tracks = new ArrayList<>();
        for(Playlist playlist : playlists){
            tracks.addAll(playlist.tracks);
        }
        Collections.shuffle(tracks);
        return tracks;
    }

    public static int getCurrentPosition(){
        return currentPosition;
    }
}
