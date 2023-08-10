package com.mangoplay.yeezymusic.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mangoplay.yeezymusic.CircleTransform;
import com.mangoplay.yeezymusic.MainActivity;
import com.mangoplay.yeezymusic.R;
import com.mangoplay.yeezymusic.objects.Playlist;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ItemMusicBigAdapter extends RecyclerView.Adapter<ItemMusicBigAdapter.ItemMusicBigViewHolder>{
    List<Playlist> playlists;
    List<Drawable> drawables;
    String title;

    public ItemMusicBigAdapter(List<Playlist> playlists, String title) {
        this.playlists = playlists;
//        GetDrawablesList getDrawablesList = new GetDrawablesList();
//        getDrawablesList.setPlaylists(playlists);
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
    public ItemMusicBigViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_music_big, null, false);
        return new ItemMusicBigViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemMusicBigViewHolder holder, int position) {
//        System.out.println("music big adapter bind for position: " + position);
//        Picasso.with(holder.context).load(musicList.get(position).getImage()).into(holder.image);
        try {
            holder.title.setText(playlists.get(holder.getAdapterPosition()).getTitle());
        } catch (NullPointerException e){
            e.printStackTrace();
            System.out.println("position: " + position);
            System.out.println("holder position: " + holder.getAdapterPosition());
        }
        try {
            holder.description.setText(playlists.get(holder.getAdapterPosition()).getDescription());
        } catch (NullPointerException e){
            e.printStackTrace();
            System.out.println("nullpointerException: " + "position: " + position);
            System.out.println("nullpointerException: " + "holder position: " + holder.getAdapterPosition());
            System.out.println("nullpointerException: " + "playlists size " + playlists.size());
            System.out.println("nullpointerException: " + "title" + title);
//            System.out.println("nullpointerException: " + "position: " + position);
            throw new NullPointerException();
        }
        if(playlists.get(0).isArtist){
            Picasso.with(holder.context).load(playlists.get(holder.getAdapterPosition()).getPictureBig()).transform(new CircleTransform()).into(holder.image);
            holder.image.setBackgroundResource(R.color.black);
            holder.description.setVisibility(View.GONE);
            holder.title.setTextSize(20);
            holder.title.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        } else {
//            System.out.println("position: " + playlists.get(position).getTitle() + " : " + playlists.get(position).getPictureBig());
            if(playlists.get(position).getPictureBig() != null) Picasso.with(holder.context).load(playlists.get(holder.getAdapterPosition()).getPictureBig()).into(holder.image);
                else holder.image.setImageDrawable(playlists.get(position).getDrawable());
//            holder.image.setImageDrawable(drawables.get(position));
            if(playlists.get(0).getDescription() == null || playlists.get(0).getDescription().length() == 0){
                holder.description.setVisibility(View.GONE);
                holder.title.setTextSize(20);
            }
        }
        if(MainActivity.getLocation().equals("Genre")){
            GridLayoutManager.LayoutParams params = (GridLayoutManager.LayoutParams)holder.itemView.getLayoutParams();
            int vertical = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5,MainActivity.r.getDisplayMetrics()));
            int horizontal = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5,MainActivity.r.getDisplayMetrics()));
//            if(params != null) {
//                params.setMargins(horizontal, vertical, horizontal, vertical);
//                holder.itemView.setLayoutParams(params);
//            }
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.tryToShowAd();
                System.out.println("click");
                MainActivity.playlistToShow = playlists.get(position);
                MainActivity.setLocation("Playlist");
                MainActivity.navigate(R.id.navigation_playlist);
            }
        });
    }

    @Override
    public int getItemCount() {
        return playlists.size();
    }

    public class ItemMusicBigViewHolder extends RecyclerView.ViewHolder {
        Context context;
        TextView title;
        TextView description;
        ImageView image;
        public ItemMusicBigViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            title = itemView.findViewById(R.id.item_music_big_title);
            description = itemView.findViewById(R.id.item_music_big_description);
            image = itemView.findViewById(R.id.item_music_big_image);


        }
    }
}
