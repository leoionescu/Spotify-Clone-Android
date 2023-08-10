package com.mangoplay.yeezymusic.ui.playlist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.mangoplay.yeezymusic.R;
import com.mangoplay.yeezymusic.adapters.ItemMusicSmallAdapter;
import com.mangoplay.yeezymusic.objects.MyPlaylists;
import com.mangoplay.yeezymusic.objects.Track;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AddToPlaylistFragment extends Fragment {

    View rootView;
    RecyclerView recyclerView;
    ItemMusicSmallAdapter itemsAdapter;
    public static Track track;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.add_to_playlist, container, false);

        setListeners();

        return rootView;
    }

    private void setListeners() {
        ImageButton backButton = rootView.findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("back");
                getActivity().onBackPressed();
            }
        });
        recyclerView = rootView.findViewById(R.id.playlist_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        itemsAdapter = new ItemMusicSmallAdapter(MyPlaylists.playlists);
        itemsAdapter.backButton = backButton;
        recyclerView.setAdapter(itemsAdapter);
    }
}
