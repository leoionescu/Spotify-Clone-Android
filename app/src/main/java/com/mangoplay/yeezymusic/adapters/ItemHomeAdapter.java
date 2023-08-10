package com.mangoplay.yeezymusic.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.mangoplay.yeezymusic.R;
import com.mangoplay.yeezymusic.objects.History;
import com.mangoplay.yeezymusic.objects.Playlist;
import com.mangoplay.yeezymusic.objects.Track;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ItemHomeAdapter extends RecyclerView.Adapter<ItemHomeAdapter.ItemHomeViewHolder> {

    public List<List<Playlist>> elements;
    List<String> titles;
    List<Track> playedRecently;
    Button refreshAdapter;
    ItemMusicNormalAdapter itemMusicNormalAdapter;
    List<ItemMusicBigAdapter> itemMusicBigAdapters = new ArrayList<>();
    RecyclerView recyclerView;

    public ItemHomeAdapter(List<List<Playlist>> elements, List<String> titles, List<Track> playedRecently) {
        this.elements = elements;
        this.titles = titles;
        this.playedRecently = playedRecently;
    }

    @NonNull
    @Override
    public ItemHomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_row, null, false);
        return new ItemHomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHomeAdapter.ItemHomeViewHolder holder, int position) {
//        System.out.println("home adapter bind for position: " + position);
        holder.title.setText(titles.get(position));
        if(playedRecently.size() == 0) {
            ItemMusicBigAdapter itemMusicBigAdapter = new ItemMusicBigAdapter(elements.get(position), titles.get(position));
            holder.recyclerView.setAdapter(itemMusicBigAdapter);
            itemMusicBigAdapters.add(itemMusicBigAdapter);
        } else {
            if(position == 0){
                itemMusicNormalAdapter = new ItemMusicNormalAdapter(playedRecently);
                holder.recyclerView.setAdapter(itemMusicNormalAdapter);
                recyclerView = holder.recyclerView;
            } else {
                ItemMusicBigAdapter itemMusicBigAdapter = new ItemMusicBigAdapter(elements.get(position - 1), titles.get(position - 1));
                holder.recyclerView.setAdapter(itemMusicBigAdapter);
                itemMusicBigAdapters.add(itemMusicBigAdapter);
            }
        }
    }

    public void refreshAdapter(int position) {
        if (position == 0 && playedRecently.size() > 0) {
            System.out.println("refresh adapter");
            playedRecently = History.playlist.tracks;
            System.out.println("first track: " + playedRecently.get(0).getName());
            itemMusicNormalAdapter = new ItemMusicNormalAdapter(playedRecently);
            recyclerView.setAdapter(itemMusicNormalAdapter);
        } else if (position != 0) {
            itemMusicBigAdapters.get(position - 1).notifyDataSetChanged();
        }
    }

        public void refreshAll(){
            for(ItemMusicBigAdapter item : itemMusicBigAdapters){
                System.out.println("notified data set changed");
                item.notifyDataSetChanged();
            }
        }


    @Override
    public int getItemCount() {
        if(playedRecently.size() == 0)
        return elements.size();
        else return elements.size() + 1;
    }

    public class ItemHomeViewHolder extends RecyclerView.ViewHolder {
        Context context;
        TextView title;
        RecyclerView recyclerView;
        public ItemHomeViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            title = itemView.findViewById(R.id.title);
            recyclerView = itemView.findViewById(R.id.home_row_recycler);
            recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        }
    }
}
