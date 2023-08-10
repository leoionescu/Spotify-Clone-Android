package com.mangoplay.yeezymusic.adapters;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mangoplay.yeezymusic.CircleTransform;
import com.mangoplay.yeezymusic.MainActivity;
import com.mangoplay.yeezymusic.items.ItemMusic;
import com.mangoplay.yeezymusic.R;
import com.mangoplay.yeezymusic.objects.MyPlaylists;
import com.mangoplay.yeezymusic.objects.Playlist;
import com.mangoplay.yeezymusic.ui.playlist.AddToPlaylistFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ItemMusicSmallAdapter extends RecyclerView.Adapter<ItemMusicSmallAdapter.ItemMusicSmallViewHolder> {

    ArrayList<ItemMusic> musicList;
    List<Playlist> playlists = new ArrayList<>();
    public ImageButton backButton;

    public ItemMusicSmallAdapter(List<Playlist> playlists) {
        this.playlists = playlists;
    }

    @NonNull
    @Override
    public ItemMusicSmallViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_music_small, null, false);
        return new ItemMusicSmallViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemMusicSmallViewHolder holder, int position) {
//        Picasso.with(holder.context).load(musicList.get(position).getImage()).into(holder.image);
        //in circle .transform(new CircleTransform())
        if(playlists.get(0).isArtist){
            Picasso.with(holder.context).load(playlists.get(position).getPictureBig()).transform(new CircleTransform()).into(holder.image);
            holder.image.setBackgroundResource(R.color.black);
            if(holder.description.getVisibility() == View.VISIBLE) {
                holder.description.setVisibility(View.GONE);
                holder.title.setGravity(Gravity.CENTER_VERTICAL);
                holder.title.setTextSize(22);
//                holder.title.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            }
        } else if(playlists.get(position).tracks.size() > 0){
            holder.image.setImageDrawable(playlists.get(position).tracks.get(0).getDrawable());
        }
        else {
            holder.image.setImageDrawable(playlists.get(position).getDrawable());
        }


        holder.title.setText(playlists.get(position).getTitle());
        holder.description.setText(playlists.get(position).getDescription());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.getLocation().equals("Add to playlist")){
                    playlists.get(position).tracks.add(AddToPlaylistFragment.track);
                    Toast.makeText(v.getContext(), "Added to playlist",Toast.LENGTH_SHORT).show();
                    MyPlaylists.writePlaylists();
                    backButton.callOnClick();
                } else {
                    MainActivity.playlistToShow = playlists.get(position);
                    MainActivity.navigate(R.id.navigation_playlist);
                }
            }
        });
    }


    @Override
    public int getItemCount() {return playlists.size();
    }

    public class ItemMusicSmallViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        Context context;
        TextView title;
        TextView description;
        ImageView image;
        public ItemMusicSmallViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            title = itemView.findViewById(R.id.item_music_small_title);
            description = itemView.findViewById(R.id.item_music_small_description);
            image = itemView.findViewById(R.id.item_music_small_image);
        }

        //events
        @Override
        public void onClick(View view) {

        }
    }
}
