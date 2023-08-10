package com.mangoplay.yeezymusic.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.exoplayer2.ThumbRating;
import com.mangoplay.yeezymusic.R;
import com.mangoplay.yeezymusic.ui.shorts.ShortsActivity;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerTracker;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ShortsAdapter extends RecyclerView.Adapter<ShortsAdapter.ShortsViewHolder> {
    boolean first = true;
    List<String> videoIds;
    public List<YouTubePlayer> youTubePlayers = new ArrayList<>();
    public List<Boolean> ready = new ArrayList<>();
    public List<YouTubePlayerTracker> youTubePlayerTrackers = new ArrayList<>();
//    List<YouTubePlayerView> youTubePlayerViews = new ArrayList<>();

    public ShortsAdapter(List<String> videoIds){
        this.videoIds = videoIds;
        for(String s : videoIds){
            ready.add(false);
            youTubePlayerTrackers.add(null);
            youTubePlayers.add(null);
        }
    }

    @NonNull
    @Override
    public ShortsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.short_viewpager_item, parent, false);
        return new ShortsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShortsViewHolder holder, int position){
        System.out.println("viewPager bind position: " + position);
        String videoId = videoIds.get(position);
//        youTubePlayerViews.add(holder.youTubePlayerView);
        holder.youTubePlayerView.getYouTubePlayerWhenReady(youTubePlayer -> {
            System.out.println("youtube player ready");
            youTubePlayer.loadVideo(videoId, 0);
//            youTubePlayer.addListener(new YouTubePlayerListener() {
//                @Override
//                public void onReady(YouTubePlayer youTubePlayer) {
//
//                }
//
//                @Override
//                public void onStateChange(YouTubePlayer youTubePlayer, PlayerConstants.PlayerState playerState) {
//                    if(playerState == PlayerConstants.PlayerState.UNSTARTED) {
//                        new Thread(() -> {
//                            try {
//                                Thread.sleep(1000);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//                            youTubePlayer.play();
//                            if (position != 0 && !first) youTubePlayer.pause();
//                            else {
//                                first = false;
//                                ShortsActivity.listen();
//                            }
//                        }).start();
//                    }
//
//                }
//
//                @Override
//                public void onPlaybackQualityChange(YouTubePlayer youTubePlayer, PlayerConstants.PlaybackQuality playbackQuality) {
//
//                }
//
//                @Override
//                public void onPlaybackRateChange(YouTubePlayer youTubePlayer, PlayerConstants.PlaybackRate playbackRate) {
//
//                }
//
//                @Override
//                public void onError(YouTubePlayer youTubePlayer, PlayerConstants.PlayerError playerError) {
//                    System.out.println("error");
//                }
//
//                @Override
//                public void onCurrentSecond(YouTubePlayer youTubePlayer, float v) {
//
//                }
//
//                @Override
//                public void onVideoDuration(YouTubePlayer youTubePlayer, float v) {
//
//                }
//
//                @Override
//                public void onVideoLoadedFraction(YouTubePlayer youTubePlayer, float v) {
//
//                }
//
//                @Override
//                public void onVideoId(YouTubePlayer youTubePlayer, String s) {
//
//                }
//
//                @Override
//                public void onApiChange(YouTubePlayer youTubePlayer) {
//
//                }
//            });
            youTubePlayers.set(position, youTubePlayer);
            YouTubePlayerTracker youTubePlayerTracker = new YouTubePlayerTracker();
            youTubePlayer.addListener(youTubePlayerTracker);
            this.youTubePlayerTrackers.set(position, youTubePlayerTracker);
//            youTubePlayerListeners.set(position, youTubePlayerListener);
        });
        int checkPosition = 1;
        try {
//            System.out.println("For position: " + checkPosition);
//            System.out.println("isSelected: " + youTubePlayerViews.get(checkPosition).isSelected());
//            System.out.println("isShown: " + youTubePlayerViews.get(checkPosition).isShown());
//            System.out.println("focusable: " + youTubePlayerViews.get(checkPosition).getFocusable());
        }catch (NullPointerException | IndexOutOfBoundsException e){}
    }

    @Override
    public int getItemCount(){
        return videoIds.size();
    }

    public class ShortsViewHolder extends RecyclerView.ViewHolder{

        YouTubePlayerView youTubePlayerView;
        int layoutPosition;
        public ShortsViewHolder(@NonNull View itemView){
            super(itemView);
            youTubePlayerView = itemView.findViewById(R.id.youtube_player_view);
            layoutPosition = this.getLayoutPosition();
        }
    }
}
