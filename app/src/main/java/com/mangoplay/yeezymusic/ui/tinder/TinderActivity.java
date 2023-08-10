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
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;

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

        CardStackView cardStackView = findViewById(R.id.card_stack_view);
        CardStackAdapter adapter = new CardStackAdapter(tracks);
        cardStackView.setAdapter(adapter);
        CardStackLayoutManager manager = new CardStackLayoutManager(getApplicationContext(), new CardStackListener() {
            @Override
            public void onCardDragging(Direction direction, float ratio) {

            }

            @Override
            public void onCardSwiped(Direction direction) {

            }

            @Override
            public void onCardRewound() {

            }

            @Override
            public void onCardCanceled() {

            }

            @Override
            public void onCardAppeared(View view, int position) {
                currentPosition = position;
                System.out.println("card appeared at position: " + position);
                CardStackAdapter.CardStackViewHolder cardStackViewHolder = (CardStackAdapter.CardStackViewHolder) cardStackView.findViewHolderForAdapterPosition(position);
                try {
                    cardStackViewHolder.play();
                } catch (NullPointerException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onCardDisappeared(View view, int position) {
                System.out.println("card disappeared at position: " + position);
            }
        });
        cardStackView.setLayoutManager(manager);
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
