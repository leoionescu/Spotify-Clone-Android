package com.mangoplay.yeezymusic.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mangoplay.yeezymusic.MainActivity;
import com.mangoplay.yeezymusic.R;
import com.mangoplay.yeezymusic.objects.History;
import com.mangoplay.yeezymusic.objects.Playlist;
import com.mangoplay.yeezymusic.objects.Queue;
import com.mangoplay.yeezymusic.objects.Track;

import java.util.List;

public class ItemMusicNormalAdapter extends RecyclerView.Adapter<ItemMusicNormalAdapter.ItemMusicNormalViewHolder> {

    List<Track> tracks;
//    List<Drawable> drawables;

    public ItemMusicNormalAdapter(List<Track> tracks) {
        this.tracks = tracks;
//        GetDrawablesList getDrawablesList = new GetDrawablesList();
//        getDrawablesList.setTracks(tracks);
//        getDrawablesList.setSmall(false);
//        try {
//            drawables = getDrawablesList.execute().get();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }
    @NonNull
    @Override
    public ItemMusicNormalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_music_normal, null, false);
        return new ItemMusicNormalViewHolder(view) ;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemMusicNormalViewHolder holder, int position) {

        //in circle .transform(new CircleTransform())
        holder.title.setText(tracks.get(position).getTitle());
        holder.description.setText(tracks.get(position).getArtist());
        holder.image.setImageDrawable(tracks.get(position).getDrawable());
//        tracks.get(position).setDrawable(drawables.get(position), "small");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("clicked");
                try {
                    Playlist playlist = (Playlist)History.playlist.clone();
                    Queue.refreshGeneratedPlaylist();
                    for(int i = position + 1; i < playlist.tracks.size(); i++){
                        Queue.addToGeneratedPlaylist(playlist.tracks.get(i));
                    }
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
                MainActivity.playerFragment.play(tracks.get(position));

//                MainActivity.homeFragment.fillHeardRecently();
//                notifyDataSetChanged();
//                MainActivity.homeFragment.refreshView.callOnClick();

                MainActivity.homeFragment.refreshView(MainActivity.homeFragment.inflater, MainActivity.homeFragment.container);

                System.out.println("generated queue size: " + Queue.generatedPlaylist.tracks.size());
            }
        });
    }

    @Override
    public int getItemCount() {
        return tracks.size();
    }

    public class ItemMusicNormalViewHolder extends RecyclerView.ViewHolder {
        Context context;
        TextView title;
        TextView description;
        ImageView image;
        public ItemMusicNormalViewHolder(@NonNull View itemView) {
            super(itemView);
            context= itemView.getContext();
            title = itemView.findViewById(R.id.item_music_normal_title);
            description = itemView.findViewById(R.id.item_music_normal_description);
            image = itemView.findViewById(R.id.item_music_normal_image);
        }
    }
}
