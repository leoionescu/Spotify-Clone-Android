package com.mangoplay.yeezymusic.network;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

import com.mangoplay.yeezymusic.objects.Track;

import java.io.InputStream;
import java.net.URL;

public class GetDrawable extends AsyncTask<Void, Void, Drawable> {

    private Track track;
    private boolean small = false;

    public GetDrawable(Track track, boolean small) {
        this.track = track;
        this.small = small;
    }

    @Override
    protected Drawable doInBackground(Void... voids) {
        String url = null;
        if(small){
            url = track.getCoverSmall();
        } else {
            url = track.getCoverBig();
        }
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            return null;
        }
    }
}
