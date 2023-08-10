package com.mangoplay.yeezymusic.network;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

import com.mangoplay.yeezymusic.objects.Playlist;
import com.mangoplay.yeezymusic.objects.Track;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class GetDrawablesList extends AsyncTask<Void, Void, List<Drawable>> {

    private List<Track> tracks = new ArrayList<>();
    private List<Playlist> playlists = new ArrayList<>();
    private boolean small= true;

    public GetDrawablesList() { }

    @Override
    protected List<Drawable> doInBackground(Void... Voids) {
        List<Drawable> drawables = new ArrayList<>();
        if(tracks.size() > 0) {
            for (Track t : tracks) {
                String url;
                if(small) {
                    url = t.getCoverSmall();
                }
                else {
                    url = t.getCoverBig();
                }
                try {
                    InputStream is = (InputStream) new URL(url).getContent();
                    Drawable d = Drawable.createFromStream(is, "src name");
                    drawables.add(d);
                } catch (Exception e) {
                    drawables.add(null);
                }
            }
            return drawables;
        } else if(playlists.size() > 0){
            for (Playlist p : playlists) {
                String url = null;
                try {
                    url = p.getPictureBig();
                } catch (NullPointerException e){
                    try {
                        url = p.getPictureSmall();
                    } catch (NullPointerException ex){}
                }
                try {
                    InputStream is = (InputStream) new URL(url).getContent();
                    Drawable d = Drawable.createFromStream(is, "src name");
                    drawables.add(d);
                } catch (Exception e) {
                    drawables.add(null);
                }
            }
            return drawables;
        }
        return null;
    }

    public void setTracks(List<Track> tracks) {
        this.tracks = tracks;
    }

    public void setPlaylists(List<Playlist> playlists) {
        this.playlists = playlists;
    }

    public void setSmall(boolean small) {
        this.small = small;
    }
}
