package com.mangoplay.yeezymusic.ui.player;

import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.mangoplay.yeezymusic.MainActivity;
import com.mangoplay.yeezymusic.R;
import com.mangoplay.yeezymusic.network.GetJSON;
import com.mangoplay.yeezymusic.objects.History;
import com.mangoplay.yeezymusic.objects.Playlist;
import com.mangoplay.yeezymusic.objects.Queue;
import com.mangoplay.yeezymusic.objects.Track;
import com.mangoplay.yeezymusic.services.BlurBuilder;
import com.mangoplay.yeezymusic.services.DeezerService;
import com.mangoplay.yeezymusic.services.LastFmService;
import com.mangoplay.yeezymusic.services.YoutubeDataService;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerTracker;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Collections;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class PlayerFragment extends Fragment {

    public Button playButton;
    private Button nextButton;
    private Button previousButton;
    private Button repeatButton;
    private Button shuffleButton;
    private SeekBar seekBar;
    public Drawable playDrawable;
    public Drawable pauseDrawable;
    Drawable loopDrawable;
    Drawable loopOnceDrawable;
    private ImageView image;
    private TextView title;
    private TextView artist;
    private TextView currentTimeStamp;
    private TextView totalTimeStamp;
    private YouTubePlayerView youTubePlayerView;
    private String videoId;
    private YouTubePlayer player;
    private YouTubePlayerTracker tracker = new YouTubePlayerTracker();
    public View rootView;
    boolean loop = false;
    boolean loopInfinite = false;
    boolean shuffle = false;
    String mainTrackName = null;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_player, container, false);
        System.out.println("player fragment view created");
        init(rootView);
        addListeners(rootView);

        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void init(View rootView) {
        playDrawable = getContext().getDrawable(R.drawable.ic_baseline_play_circle_outline_24);
        pauseDrawable = getContext().getDrawable(R.drawable.ic_baseline_pause_circle_outline_24);
        loopDrawable = getContext().getDrawable(R.drawable.ic_baseline_repeat_24);
        loopOnceDrawable = getContext().getDrawable(R.drawable.ic_baseline_repeat_one_24);
        playButton = rootView.findViewById(R.id.playButton);
        previousButton = rootView.findViewById(R.id.previousButton);
        repeatButton = rootView.findViewById(R.id.repeatButton);
        shuffleButton = rootView.findViewById(R.id.shuffleButton);
        nextButton = rootView.findViewById(R.id.nextButton);
        seekBar = rootView.findViewById(R.id.seekBar);
        currentTimeStamp = rootView.findViewById(R.id.currentTimeStamp);
        totalTimeStamp = rootView.findViewById(R.id.totalTimeStamp);
        youTubePlayerView = rootView.findViewById(R.id.youtube_player_view);
        title = rootView.findViewById(R.id.textView);
        artist = rootView.findViewById(R.id.artist);
        image = rootView.findViewById(R.id.imageView);
        youTubePlayerView.enableBackgroundPlayback(true);
//        lifecycleOwner.getLifecycle().addObserver(youTubePlayerView);
        title.setSelected(true);
        artist.setSelected(true);
        applySettings();
    }

    public void addListeners(View rootView) {
        youTubePlayerView.getYouTubePlayerWhenReady(youTubePlayer -> {
            System.out.println("player ready");
            player = youTubePlayer;

            try {
                if (History.playlist.tracks.size() > 0) {
                    MainActivity.playerFragment.load(History.playlist.tracks.get(0));
                } else {
                    mainTrackName = getResources().getString(R.string.main_track_name);
                    if(!mainTrackName.equals("null") && mainTrackName.length() > 4){
                        try {
                            Track track = Track.read("mainTrack.bin");
                            if(track == null || track.getTitle() == null) throw new IOException();
                            MainActivity.playerFragment.load(track);
                            System.out.println("loaded mainTrack from file");
                        } catch (IOException e) {
                                new Thread(() -> {
                                    mainTrackName = mainTrackName.replace(" - Yeezy Music", "");
                                    mainTrackName = mainTrackName.replace(" - ", " ");
                                    mainTrackName = mainTrackName.replace("and ", "");
                                    System.out.println("mainTrackName: " + mainTrackName);
                                    String source = "https://api.deezer.com/search?q=" + mainTrackName;
                                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                                        (Request.Method.GET, source, null, new Response.Listener<JSONObject>() {

                                            @Override
                                            public void onResponse(JSONObject object) {
                                                JSONArray data = null;
                                                try {
                                                    data = object.getJSONArray("data");
                                                    System.out.println("data for mainTrack: " + data.toString());
                                                    Track track = DeezerService.getTrack(data, 0);
                                                    MainActivity.playerFragment.load(track);
                                                    track.save("mainTrack.bin");
                                                } catch (JSONException | IOException jsonException) {
                                                    jsonException.printStackTrace();
                                                }
                                            }
                                        }, new Response.ErrorListener() {

                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                System.out.println("error response for URL: " + source);
                                            }
                                        });
                                    RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.context);
                                    requestQueue.add(jsonObjectRequest);
                                }).start();
                        }

                    }
                }
            } catch (NullPointerException | IllegalStateException e) {
                e.printStackTrace();
            }

            player.addListener(tracker);
            player.addListener(new YouTubePlayerListener() {
                @Override
                public void onReady(YouTubePlayer youTubePlayer) {

                }

                @Override
                public void onStateChange(YouTubePlayer youTubePlayer, PlayerConstants.PlayerState playerState) {
                    System.out.println("state changed: " + playerState.toString());
                    if(playerState == PlayerConstants.PlayerState.ENDED){
                        System.out.println("done");
                        playButton.setBackground(playDrawable);
                        MainActivity.playSlideUp.setImageDrawable(MainActivity.playDrawable);
                        nextButton.callOnClick();
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
                    seekBar.setProgress((int)v);
                    currentTimeStamp.setText(getDuration((int)v));
//                    if((int)v == seekBar.getMax() && (int)v > 0){
//                        System.out.println("done");
//                        playButton.setBackground(playDrawable);
//                        MainActivity.playSlideUp.setImageDrawable(MainActivity.playDrawable);
//                        nextButton.callOnClick();
//                    }
                }

                @Override
                public void onVideoDuration(YouTubePlayer youTubePlayer, float v) {
                    int duration = (int)v;
                    seekBar.setMax(duration);
                    System.out.println("duration: " + v);
                    String d = getDuration(duration);
                    System.out.println("String duration: " + d);
                    totalTimeStamp.setText(d);
                }

                @Override
                public void onVideoLoadedFraction(YouTubePlayer youTubePlayer, float v) {
//                    System.out.println("fraction loaded: " + v);
                }

                @Override
                public void onVideoId(YouTubePlayer youTubePlayer, String s) {

                }

                @Override
                public void onApiChange(YouTubePlayer youTubePlayer) {

                }
            });
        } );

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("play button clicked");
                try {
                    if (tracker.getState() == PlayerConstants.PlayerState.PLAYING) {
                        player.pause();
                        playButton.setBackground(playDrawable);
                        MainActivity.playSlideUp.setImageDrawable(MainActivity.playDrawable);
                    } else {
                        player.play();
                        playButton.setBackground(pauseDrawable);
                        MainActivity.playSlideUp.setImageDrawable(MainActivity.pauseDrawable);
                    }
                } catch (NullPointerException e){}
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("clicked next button");
                if(loop){
                    loop = false;
                    player.seekTo(0);
                    player.play();
                    repeatButton.setBackground(loopDrawable);
                    repeatButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                    playButton.setBackground(pauseDrawable);
                    MainActivity.playSlideUp.setImageDrawable(MainActivity.pauseDrawable);
                } else if(loopInfinite){
                    player.seekTo(0);
                    player.play();
                    playButton.setBackground(pauseDrawable);
                    MainActivity.playSlideUp.setImageDrawable(MainActivity.pauseDrawable);
                } else {
                    try {
                        play(Objects.requireNonNull(Queue.getNext()));
                    } catch (NullPointerException e) {
                        System.out.println("queue is empty");
                    }
                }
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                try {
                    player.pause();
                    player.seekTo(seekBar.getProgress());
                    player.play();
                    playButton.setBackground(pauseDrawable);
                } catch (NullPointerException e) {}
            }
        });;

        repeatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!loop && !loopInfinite){
                    loop = true;
                    repeatButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                    repeatButton.setBackground(loopOnceDrawable);
                    System.out.println("loop once");
                } else {
                    if(loop){
                        loop = false;
                        loopInfinite = true;
                        repeatButton.setBackground(loopDrawable);
//                        repeatButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                        System.out.println("loop infinte");
                    } else if(loopInfinite){
                        loopInfinite = false;
                        repeatButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.gray)));
                        System.out.println("no loop");
                    }
                }
            }
        });

        shuffleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("shuffle button");
                if(!shuffle){
                    shuffle = true;
                    Collections.shuffle(Queue.generatedPlaylist.tracks);
                    shuffleButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                } else {
                    shuffleButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.gray)));
                    shuffle = false;
                }
            }
        });

    }

    public void play(Track track){

        currentTimeStamp.setText(getDuration(0));
        seekBar.setProgress(0);
        title.setText(track.getTitle());
        artist.setText(track.getArtist());
        MainActivity.setMiniPlayer(track);
        playButton.setBackground(pauseDrawable);
        MainActivity.playSlideUp.setImageDrawable(MainActivity.pauseDrawable);

        Bitmap bitmap = ((BitmapDrawable)track.getDrawable()).getBitmap();
        Bitmap background = BlurBuilder.blur(getContext(), bitmap);
        Drawable drawable = new BitmapDrawable(getResources(), background);
        MainActivity.playerBackground = drawable;
        MainActivity.averageColor = MainActivity.getDominantColor(background);

        CustomTarget<Drawable> customTarget = new CustomTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                track.setDrawable(resource, "big");
                image.setImageDrawable(resource);
                History.writeHistory();
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {

            }
        };

        image.setImageDrawable(track.getDrawable());
        Glide.with(MainActivity.context).load(track.getCoverBig()).into(customTarget);
        History.add(track);

        if (player != null) {
            player.seekTo(0);
            player.pause();
            new Thread(() -> {
                String song = track.getName();
                videoId = YoutubeDataService.getVideoId(song, MainActivity.context);
                if (videoId != null) {
                    if (youTubePlayerView == null) System.out.println("player is null");
                    if (rootView == null) System.out.println("rootview is null");
                    player.loadVideo(videoId, 0);

                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.context, "Sorry, can't play this song right now", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
//                Playlist similarTracks = LastFmService.getSimilarTracks(track, 10);
//                System.out.println("SimilarTracks size: " + similarTracks.tracks.size());
//                for(Track similarTrack : similarTracks.tracks){
//                    System.out.println(similarTrack);
//                }
            }).start();
        } else {
            youTubePlayerView.getYouTubePlayerWhenReady(youTubePlayer -> load(track));
        }
    }

    public void load(Track track){
        System.out.println("load in player fragment");
        currentTimeStamp.setText(getDuration(0));
        seekBar.setProgress(0);
        title.setText(track.getTitle());
        artist.setText(track.getArtist());
        MainActivity.setMiniPlayer(track);
        playButton.setBackground(playDrawable);
        MainActivity.playSlideUp.setImageDrawable(MainActivity.playDrawable);

        Bitmap bitmap = ((BitmapDrawable)track.getDrawable()).getBitmap();
        Bitmap background = BlurBuilder.blur(getContext(), bitmap);
        Drawable drawable = new BitmapDrawable(getResources(), background);
        MainActivity.playerBackground = drawable;
        MainActivity.averageColor = MainActivity.getDominantColor(background);

        CustomTarget<Drawable> customTarget = new CustomTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                track.setDrawable(resource, "big");
                image.setImageDrawable(resource);
            }

            @Override
            public void onLoadCleared(@Nullable  Drawable placeholder) {
            }
        };

        image.setImageDrawable(track.getDrawable());
        Glide.with(MainActivity.context).load(track.getCoverBig()).into(customTarget);
        History.add(track);

        if (player != null) {
            player.seekTo(0);
            player.pause();
            new Thread(() -> {
                String song = track.getName();
                videoId = YoutubeDataService.getVideoId(song, MainActivity.context);
                if (videoId != null) {
                    if (youTubePlayerView == null) System.out.println("player is null");
                    if (rootView == null) System.out.println("rootview is null");
                    player.cueVideo(videoId, 0);
                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.context, "Sorry, can't play this song right now", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }).start();
        } else {
            youTubePlayerView.getYouTubePlayerWhenReady(youTubePlayer -> play(track));
        }
    }

    public String getDuration(Integer duration){
        Integer minutes = duration / 60;
        Integer seconds = duration % 60;
        String Seconds = seconds.toString();
        if (seconds < 10) Seconds = "0" + Seconds;
        String Duration = minutes.toString() + ":" + Seconds;
        return Duration;
    }

    public YouTubePlayer getPlayer() {
        return player;
    }

    public void setPlayer(YouTubePlayer player) {
        this.player = player;
    }

    public void setYouTubePlayerView(YouTubePlayerView youTubePlayerView){
        this.youTubePlayerView = youTubePlayerView;
    }

    public YouTubePlayerTracker getTracker() {
        return tracker;
    }

    public void applySettings(){
        if(MainActivity.settings.isShowVideo()){
            image.setVisibility(View.INVISIBLE);
            youTubePlayerView.setVisibility(View.VISIBLE);
        } else {
            image.setVisibility(View.VISIBLE);
            youTubePlayerView.setVisibility(View.GONE);
        }
    }
}
