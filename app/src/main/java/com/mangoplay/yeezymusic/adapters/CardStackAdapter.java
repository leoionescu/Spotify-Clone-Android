package com.mangoplay.yeezymusic.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mangoplay.yeezymusic.R;
import com.mangoplay.yeezymusic.objects.Track;
import com.mangoplay.yeezymusic.services.YoutubeDataService;
import com.mangoplay.yeezymusic.ui.tinder.TinderActivity;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CardStackAdapter extends RecyclerView.Adapter<CardStackAdapter.CardStackViewHolder>{

    List<Track> tracks = new ArrayList<>();

    public CardStackAdapter(List<Track> tracks){
        this.tracks = tracks;
    }

    @NonNull
    @Override
    public CardStackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_stack, parent, false);
        return new CardStackViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardStackViewHolder holder, int position){
        holder.title.setText(tracks.get(position).getTitle());
        holder.artist.setText(tracks.get(position).getArtist());
        Glide.with(TinderActivity.context).load(tracks.get(position).getCoverBig()).into(holder.image);
        new Thread(() -> {
            String videoId = YoutubeDataService.getVideoId(tracks.get(position).getName(), TinderActivity.context);
            holder.youTubePlayerView.getYouTubePlayerWhenReady(youTubePlayer -> {
                holder.player = youTubePlayer;
                holder.player.cueVideo(videoId, 0);
                holder.player.addListener(new YouTubePlayerListener() {
                    @Override
                    public void onReady(YouTubePlayer youTubePlayer) {

                    }

                    @Override
                    public void onStateChange(YouTubePlayer youTubePlayer, PlayerConstants.PlayerState playerState) {
                        if(playerState == PlayerConstants.PlayerState.UNSTARTED){
                            holder.loadingCircle.setVisibility(View.GONE);
                            if(TinderActivity.currentPosition == position){
                                holder.play();
                            }
                        }
                    }

                    @Override
                    public void onPlaybackQualityChange(YouTubePlayer youTubePlayer, PlayerConstants.PlaybackQuality playbackQuality) {

                    }

                    @Override
                    public void onPlaybackRateChange(YouTubePlayer youTubePlayer, PlayerConstants.PlaybackRate playbackRate) {

                    }

                    @Override
                    public void onError(YouTubePlayer youTubePlayer, PlayerConstants.PlayerError playerError) {

                    }

                    @Override
                    public void onCurrentSecond(YouTubePlayer youTubePlayer, float v) {
                        System.out.println();
                    }

                    @Override
                    public void onVideoDuration(YouTubePlayer youTubePlayer, float v) {

                    }

                    @Override
                    public void onVideoLoadedFraction(YouTubePlayer youTubePlayer, float v) {

                    }

                    @Override
                    public void onVideoId(YouTubePlayer youTubePlayer, String s) {

                    }

                    @Override
                    public void onApiChange(YouTubePlayer youTubePlayer) {

                    }
                });
            });

        }).start();
    }

    @Override
    public int getItemCount(){
        return tracks.size();
    }

    public class CardStackViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        TextView artist;
        ImageView image;
        YouTubePlayerView youTubePlayerView;
        ProgressBar loadingCircle;
        YouTubePlayer player = null;

        public CardStackViewHolder(@NonNull View itemView){
            super(itemView);
            title = itemView.findViewById(R.id.item_name);
            artist = itemView.findViewById(R.id.item_city);
            image = itemView.findViewById(R.id.item_image);
            youTubePlayerView = itemView.findViewById(R.id.youtube_player_view);
            loadingCircle = itemView.findViewById(R.id.progress_loader);
        }

        public void play(){
            player.play();
            System.out.println("playing");
        }
    }
}

