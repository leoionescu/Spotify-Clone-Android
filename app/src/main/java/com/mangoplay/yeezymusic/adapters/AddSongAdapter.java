package com.mangoplay.yeezymusic.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mangoplay.yeezymusic.MainActivity;
import com.mangoplay.yeezymusic.R;
import com.mangoplay.yeezymusic.objects.Track;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AddSongAdapter extends RecyclerView.Adapter<AddSongAdapter.AddSongsViewHolder>{
    public List<Track> tracks = new ArrayList<>();
    public boolean[] checked;

    public AddSongAdapter(List<Track> tracks) {
       this.tracks = tracks;
       checked = new boolean[tracks.size()];
    }

    @NonNull
    @Override
    public AddSongsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_songs_row, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        return new AddSongsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddSongsViewHolder holder, int position) {
        Glide.with(MainActivity.context).load(tracks.get(position).getDrawable()).into(holder.image);
//        holder.image.setImageDrawable(tracks.get(position).getDrawable());
        holder.title.setText(tracks.get(position).getTitle());
        holder.artist.setText(tracks.get(position).getArtist());
        holder.aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checked[position] = isChecked;
            }
        });
    }


    @Override
    public int getItemCount() {
        return tracks.size();
    }

    public class AddSongsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView image;
        TextView title;
        TextView artist;
        Switch aSwitch;
        public AddSongsViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            title = itemView.findViewById(R.id.title);
            artist = itemView.findViewById(R.id.artist);
            aSwitch = itemView.findViewById(R.id.switch1);
        }

        //events
        @Override
        public void onClick(View view) {

        }

        public List<Track> getSelectedTracks(){
            List<Track> selectedTracks = new ArrayList<>();


            return selectedTracks;
        }
    }



}
