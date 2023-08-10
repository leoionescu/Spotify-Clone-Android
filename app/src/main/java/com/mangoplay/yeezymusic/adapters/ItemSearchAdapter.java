package com.mangoplay.yeezymusic.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mangoplay.yeezymusic.R;
import com.mangoplay.yeezymusic.items.ItemSearch;
import com.mangoplay.yeezymusic.services.PlayerService;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class ItemSearchAdapter extends RecyclerView.Adapter<ItemSearchAdapter.ItemSearchViewHolder> {
    ArrayList<ItemSearch> searchList;

    public ItemSearchAdapter(ArrayList<ItemSearch> searchList) {
        this.searchList = searchList;
    }

    @NonNull
    @Override
    public ItemSearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search, null, false);
        return new ItemSearchAdapter.ItemSearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemSearchViewHolder holder, int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("click");
                try {
                    PlayerService.play("Dua Lipa", "Levitating");
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return searchList.size();
    }

    public class ItemSearchViewHolder extends RecyclerView.ViewHolder{
        public ItemSearchViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
