package com.mangoplay.yeezymusic.ui.shorts;

import android.net.sip.SipSession;
import android.os.Bundle;
import android.view.View;

import com.mangoplay.yeezymusic.R;
import com.mangoplay.yeezymusic.adapters.ShortsAdapter;
import com.mangoplay.yeezymusic.services.YoutubeDataService;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerTracker;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

public class ShortsActivity extends AppCompatActivity {
    List<String> videoIds = new ArrayList<>();
    YouTubePlayerView youTubePlayerView = null;
    static ViewPager2 viewPager2;
    static ShortsAdapter shortsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shorts);
        new Thread(() -> {
            videoIds = YoutubeDataService.getShorts();
            videoIds = videoIds.subList(0,10);
            if(videoIds.size() > 0) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        findViewById(R.id.progress_loader).setVisibility(View.GONE);
//                        youTubePlayerView = findViewById(R.id.youtube_player_view);
//                        youTubePlayerView.setVisibility(View.VISIBLE);
//                        youTubePlayerView = new YouTubePlayerView(getApplicationContext());
//                        youTubePlayerView.setVisibility(View.VISIBLE);
//                        youTubePlayerView.getYouTubePlayerWhenReady(youTubePlayer -> {
//                            String videoId = videoIds.get(0);
//                            System.out.println("videoId: " + videoId);
//                            youTubePlayer.loadVideo(videoId, 0);
//                        });
//                        System.out.println("loaded video");
                        viewPager2 = findViewById(R.id.viewpager);
                        shortsAdapter = new ShortsAdapter(videoIds);
                        viewPager2.setAdapter(shortsAdapter);
                        viewPager2.setClipToPadding(false);
                        viewPager2.setClipChildren(false);
                        viewPager2.setOffscreenPageLimit(2);
                        viewPager2.getChildAt(0).setOverScrollMode(View.OVER_SCROLL_NEVER);

                    }
                });
            }
        }).start();
    }

    public static void listen(){
        new Thread(() ->{
            System.out.println("first short playing");
            int currentPosition = viewPager2.getCurrentItem();
            int oldPosition = 0;
            while(true){
                currentPosition = viewPager2.getCurrentItem();
                if(currentPosition != oldPosition){
                    try {
                        shortsAdapter.youTubePlayers.get(oldPosition).pause();
                    } catch (IndexOutOfBoundsException | NullPointerException e) {}
                    try {
                        shortsAdapter.youTubePlayers.get(currentPosition).play();
                    } catch (NullPointerException e) {
                        currentPosition = oldPosition;
                    }
                    oldPosition = currentPosition;
                } else {
//                    System.out.println("sleeping");
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
