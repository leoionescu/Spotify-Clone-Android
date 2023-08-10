package com.mangoplay.yeezymusic.adapters;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mangoplay.yeezymusic.MainActivity;
import com.mangoplay.yeezymusic.R;
import com.mangoplay.yeezymusic.network.GetDrawable;
import com.mangoplay.yeezymusic.objects.MyPlaylists;
import com.mangoplay.yeezymusic.objects.Playlist;
import com.mangoplay.yeezymusic.objects.Queue;
import com.mangoplay.yeezymusic.objects.Track;
import com.mangoplay.yeezymusic.services.SuperclassExclusionStrategy;
import com.mangoplay.yeezymusic.services.YoutubeDataService;
import com.mangoplay.yeezymusic.ui.playlist.AddToPlaylistFragment;
import com.mangoplay.yeezymusic.ui.playlist.PlaylistFragment;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PlaylistRowAdapter extends RecyclerView.Adapter<PlaylistRowAdapter.PlaylistRowViewHolder> {

    Playlist playlist;
    public List<Drawable> drawableList = new ArrayList<>();
    public PlaylistFragment playlistFragment = null;
    public boolean stopThread = false;

    public PlaylistRowAdapter(Playlist playlist, PlaylistFragment playlistFragment) {
        long start = System.currentTimeMillis();

        this.playlist = playlist;
        this.playlistFragment = playlistFragment;

        playlist.tracks.removeIf(Objects::isNull);

        long stop = System.currentTimeMillis();
//        System.out.println("playlist row adapter constructor time: " + (stop - start));

//        System.out.println("drawable list length: " + drawableList.size());
    }

    @NonNull
    @Override
    public PlaylistRowAdapter.PlaylistRowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.playlist_row, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        return new PlaylistRowAdapter.PlaylistRowViewHolder(view);
//        return new PlaylistRowAdapter.PlaylistRowViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull PlaylistRowAdapter.PlaylistRowViewHolder holder, int position) {
        try {

//                holder.image.setImageDrawable(drawableList.get(position));

//                Target target = new Target() {
//                    @Override
//                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//                        Drawable drawable = new BitmapDrawable(MainActivity.r,  bitmap);
//                        holder.image.setImageDrawable(drawable);
//                        playlist.tracks.get(position).setDrawable(drawable, "small");
//                    }
//
//                    @Override
//                    public void onBitmapFailed(Drawable errorDrawable) {
//
//                    }
//
//                    @Override
//                    public void onPrepareLoad(Drawable placeHolderDrawable) {
//
//                    }
//                };

//                Picasso.with(MainActivity.context).load(playlist.tracks.get(position).getCoverSmall()).into(target);
//                Picasso.with(MainActivity.context).load(playlist.tracks.get(position).getCoverSmall()).into(holder.image);

            Glide.with(MainActivity.context).load(playlist.tracks.get(position).getCoverSmall()).centerCrop().into(holder.image);

//                new Thread(() -> {
//                    int counter = 0;
//                    while(true){
//                        try{
//                            Drawable drawable = holder.image.getDrawable();
//                            playlist.tracks.get(position).setDrawable(drawable, "small");
//                        } catch (Exception e){}
//                        try {
//                            Thread.sleep(10);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                        counter++;
//                        if(counter >= 3000) break;
//                    }
//                }).start();

//            }
//        else {
//                holder.image.setImageResource(R.color.gray_strong);
//            }

//        holder.image.setImageDrawable(MainActivity.context.getDrawable(R.drawable.unknown));
            holder.title.setText(playlist.tracks.get(position).getTitle());
            holder.artist.setText(playlist.tracks.get(position).getArtist());


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("clicked item " + position);
//                    System.out.println("track: " + playlist.tracks.get(position).toString());
                    try {
                        playlist.tracks.get(position).setDrawable(holder.image.getDrawable(), "small");
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                    MainActivity.playerFragment.play(playlist.tracks.get(position));
                    Queue.refreshGeneratedPlaylist();
                    for(int i = position + 1; i < playlist.tracks.size(); i++){
                        Queue.addToGeneratedPlaylist(playlist.tracks.get(i));
                    }
                    System.out.println("generated queue size: " + Queue.generatedPlaylist.tracks.size());
                }
            });

            holder.moreButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("more button pressed, position " + position);
                    showBottomSheetDialog(position);
                }
            });

        } catch (IndexOutOfBoundsException e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return playlist.tracks.size();
    }

    public class PlaylistRowViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView title;
        TextView artist;
        ImageButton moreButton;

        public PlaylistRowViewHolder(@NonNull View itemView) {
            super(itemView);
//            context = itemView.getContext();
                image = itemView.findViewById(R.id.image);
                title = itemView.findViewById(R.id.title);
                artist = itemView.findViewById(R.id.artist);
                moreButton = itemView.findViewById(R.id.more_button);
        }
    }

    private void showBottomSheetDialog(int position){
        System.out.println("position for bottom sheet: " + position);
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(MainActivity.mainActivity);
        bottomSheetDialog.setContentView(R.layout.track_bottom_sheet_menu_layout);
        TextView title = bottomSheetDialog.findViewById(R.id.title);
        TextView artist = bottomSheetDialog.findViewById(R.id.artist);
        ImageView cover = bottomSheetDialog.findViewById(R.id.cover);
        title.setText(playlist.tracks.get(position).getTitle());
        artist.setText(playlist.tracks.get(position).getArtist());

        try {
            if(playlist.tracks.get(position).getDrawableSize().equals("big")) {
                Drawable d = playlist.tracks.get(position).getDrawable();
            } else throw new NullPointerException();
        } catch (NullPointerException e){
            System.out.println("drawable is null");
            GetDrawable getDrawable = new GetDrawable(playlist.tracks.get(position), false);
            try {
                Drawable d = getDrawable.execute().get();
                playlist.tracks.get(position).setDrawable(d, "big");
            } catch (ExecutionException executionException) {
                executionException.printStackTrace();
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
        }
        cover.setImageDrawable(playlist.tracks.get(position).getDrawable());

        Track track = playlist.tracks.get(position);
        bottomSheetDialog.getBehavior().setState(BottomSheetBehavior.STATE_EXPANDED);
        bottomSheetDialog.show();

        LinearLayout download = bottomSheetDialog.findViewById(R.id.download);
        LinearLayout queue = bottomSheetDialog.findViewById(R.id.queue);
        LinearLayout playlist = bottomSheetDialog.findViewById(R.id.playlist);
        LinearLayout like = bottomSheetDialog.findViewById(R.id.like);
        LinearLayout share = bottomSheetDialog.findViewById(R.id.share);
        LinearLayout removeFromQueue = bottomSheetDialog.findViewById(R.id.remove_queue);
        Button cancel = bottomSheetDialog.findViewById(R.id.cancel);

        System.out.println("location: " + MainActivity.getLocation());

        if(MainActivity.getLocation().equals("Queue")) {
            System.out.println("changing visibility");
            queue.setVisibility(View.GONE);
            removeFromQueue.setVisibility(View.VISIBLE);

            removeFromQueue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("playlist size: " + Queue.getCombinedPlaylist().tracks.size());
                    System.out.println("remove from queue");
                    if(position < Queue.playlist.tracks.size()) {
                        Queue.playlist.tracks.remove(position);
                    } else {
                        Queue.generatedPlaylist.tracks.remove(position - Queue.playlist.tracks.size());
                    }
                    bottomSheetDialog.dismiss();
                    notifyItemRemoved(position);
//                    drawableList.remove(position);
                    notifyDataSetChanged();
//                    notifyItemRangeChanged(0, Queue.generatedPlaylist.tracks.size());
                    System.out.println("playlist size: " + Queue.getCombinedPlaylist().tracks.size());
                }
            });
        } else {
            queue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("queue");
                    Queue.add(track);
                    System.out.println("queue size: " + Queue.playlist.tracks.size());
                    bottomSheetDialog.dismiss();
                }
            });
        }



        download.setVisibility(View.GONE);

        Playlist liked = MyPlaylists.getPlaylistById("0");
        if(liked != null){
            ImageView imageViewLike = like.findViewById(R.id.imageViewLike);
            TextView textViewLike = like.findViewById(R.id.textViewLike);
            int index = liked.findIndexOfTrack(track.getId());
            if(index >= 0){
                imageViewLike.setImageResource(R.drawable.ic_baseline_favorite_24);
                textViewLike.setText("Remove from favorites");
            } else {
                imageViewLike.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                textViewLike.setText("Add to favorites");
            }

            like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(index > 0){
                        imageViewLike.setImageResource(R.drawable.ic_baseline_favorite_24);
                        textViewLike.setText("Remove from favorites");
                        System.out.println("remove from favorites");
                        liked.tracks.remove(index);
                        MyPlaylists.writePlaylists();
                        bottomSheetDialog.dismiss();
                    } else {
                        imageViewLike.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                        textViewLike.setText("Add to favorites");
                        System.out.println("add to favorites");
                        liked.tracks.add(track);
                        MyPlaylists.writePlaylists();
                        bottomSheetDialog.dismiss();
                    }
                }
            });

        }



        if(download.getVisibility() == View.VISIBLE)
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("download");
                downloadTrack(track);
                bottomSheetDialog.dismiss();
            }
        });


        playlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("playlist");
                bottomSheetDialog.dismiss();
                MainActivity.setLocation("Add to playlist");
                AddToPlaylistFragment.track = track;
                MainActivity.navigate(R.id.navigation_add_to_playlist);
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("share");
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("cancel");
                bottomSheetDialog.dismiss();
            }
        });

    }

    private void downloadTrack(Track track) {
        String song = track.getName();
        String videoId = YoutubeDataService.getVideoId(song, MainActivity.context);
        System.out.println("videoId: " + videoId);
        YouTubePlayerView youTubePlayerView = new YouTubePlayerView(MainActivity.context);
        youTubePlayerView.getYouTubePlayerWhenReady(youTubePlayer -> {

            youTubePlayer.mute();
            youTubePlayer.pause();
            youTubePlayer.loadVideo(videoId, 0);
            youTubePlayer.addListener(new YouTubePlayerListener() {

                boolean downloaded = false;

                @Override
                public void onReady(YouTubePlayer youTubePlayer) {

                }

                @Override
                public void onStateChange(YouTubePlayer youTubePlayer, PlayerConstants.PlayerState playerState) {

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

                }

                @Override
                public void onVideoDuration(YouTubePlayer youTubePlayer, float v) {

                }

                @Override
                public void onVideoLoadedFraction(YouTubePlayer youTubePlayer, float v) {
                    System.out.println("fraction loaded: " + v);
                    if(v > 0.2f && !downloaded){
                        downloaded = true;
                        System.out.println("loaded 100%");
                        MainActivity.playerFragment.setYouTubePlayerView(youTubePlayerView);
                        MainActivity.playerFragment.setPlayer(youTubePlayer);
                        MainActivity.playerFragment.addListeners(MainActivity.playerFragment.rootView);
//                            Data.saveYoutubePlayer(youTubePlayer,MainActivity.context, track.getName() + ".bin");
                        GsonBuilder gsonBuilder = new GsonBuilder();
                        gsonBuilder.addDeserializationExclusionStrategy(new SuperclassExclusionStrategy());
                        gsonBuilder.addSerializationExclusionStrategy(new SuperclassExclusionStrategy());
                        Gson gson = gsonBuilder.create();
                        String json = gson.toJson(youTubePlayer);
                        System.out.println(json);
//                        System.out.println("track saved");
                    }
                }

                @Override
                public void onVideoId(YouTubePlayer youTubePlayer, String s) {

                }

                @Override
                public void onApiChange(YouTubePlayer youTubePlayer) {

                }
            });
        });
    }
}