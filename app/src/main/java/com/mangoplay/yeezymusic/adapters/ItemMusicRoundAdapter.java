package com.mangoplay.yeezymusic.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mangoplay.yeezymusic.CircleTransform;
import com.mangoplay.yeezymusic.items.ItemMusic;
import com.mangoplay.yeezymusic.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ItemMusicRoundAdapter extends RecyclerView.Adapter<ItemMusicRoundAdapter.ItemMusicRoundViewHolder>{
    ArrayList<ItemMusic> musicList;

    public ItemMusicRoundAdapter(ArrayList<ItemMusic> musicList) {
        this.musicList = musicList;
    }

    @NonNull
    @Override
    public ItemMusicRoundAdapter.ItemMusicRoundViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_music_small, null, false);
        return new ItemMusicRoundAdapter.ItemMusicRoundViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemMusicRoundAdapter.ItemMusicRoundViewHolder holder, int position) {
        Picasso.with(holder.context).load(musicList.get(position).getImage()).transform(new CircleTransform()).into(holder.image);
        if(musicList.get(position).getDescription().equals("")){
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0,60,0,0);
            holder.title.setLayoutParams(params);
        }
        holder.title.setText(musicList.get(position).getTitle());
        holder.description.setText(musicList.get(position).getDescription());
    }


    @Override
    public int getItemCount() {return musicList.size();
    }

    public class ItemMusicRoundViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        Context context;
        TextView title;
        TextView description;
        ImageView image;
        public ItemMusicRoundViewHolder(@NonNull View itemView) {
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

