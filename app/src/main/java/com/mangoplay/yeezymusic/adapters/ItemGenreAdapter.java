package com.mangoplay.yeezymusic.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mangoplay.yeezymusic.MainActivity;
import com.mangoplay.yeezymusic.R;
import com.mangoplay.yeezymusic.items.ItemMusic;
import com.mangoplay.yeezymusic.objects.Genre;
import com.mangoplay.yeezymusic.ui.genre.GenreFragment;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

public class ItemGenreAdapter extends RecyclerView.Adapter<ItemGenreAdapter.ItemWelcomeViewHoler>{

    ArrayList<ItemMusic> musicList;
    List<Genre> genres;

    public ItemGenreAdapter(List<Genre> genres) {
        this.genres = genres;
        for(Genre genre : genres){
            if(genre.ids.size() == 0){
                new Thread(() -> {
                    genre.getArtistList();
                }).start();
            }
        }
    }

    @NonNull
    @Override
    public ItemGenreAdapter.ItemWelcomeViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_genre, null, false);
        return new ItemWelcomeViewHoler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemGenreAdapter.ItemWelcomeViewHoler holder, int position) {
        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                holder.image.setImageBitmap(bitmap);
                int color = MainActivity.getDominantColor((bitmap));
                holder.frameLayout.setBackgroundColor(color);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        Picasso.with(holder.context).load(genres.get(position).getPictureSmall()).into(target);
        holder.title.setText(genres.get(position).getTitle());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("genre clicked: " + genres.get(position).getTitle());
                System.out.println("number of artists: " + genres.get(position).artists.size());
                GenreFragment.genre = genres.get(position);
                if(GenreFragment.genre.artists.size() == 0) GenreFragment.genre.getArtists();
                MainActivity.setLocation("Genre");
                MainActivity.navigate(R.id.navigation_genre_fragment);
            }
        });
    }

    @Override
    public int getItemCount() {
        return genres.size();
    }

    public class ItemWelcomeViewHoler extends RecyclerView.ViewHolder{
        Context context;
        TextView title;
        ImageView image;
        FrameLayout frameLayout;
        public ItemWelcomeViewHoler(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            image = itemView.findViewById(R.id.image);
            frameLayout = itemView.findViewById(R.id.frame_layout);
            context = itemView.getContext();
        }
    }
}
