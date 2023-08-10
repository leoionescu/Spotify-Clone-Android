package com.mangoplay.yeezymusic.ui.library.TabsFragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.mangoplay.yeezymusic.MainActivity;
import com.mangoplay.yeezymusic.items.ItemMusic;
import com.mangoplay.yeezymusic.R;
import com.mangoplay.yeezymusic.adapters.ItemMusicSmallAdapter;
import com.mangoplay.yeezymusic.objects.MyPlaylists;
import com.mangoplay.yeezymusic.objects.Playlist;
import com.mangoplay.yeezymusic.ui.playlist.CreatePlaylistActivity;

import java.util.ArrayList;
import java.util.List;


public class PlayListsFragment extends Fragment {

    View fragmentPlayList;
    RecyclerView recyclerPlayList;
    ArrayList<ItemMusic> arrayPlayList;
    List<Playlist> playlists = new ArrayList<>();
    ItemMusicSmallAdapter itemMusicSmallAdapter;

    public PlayListsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        System.out.println("playlists onCreateView");
        fragmentPlayList = inflater.inflate(R.layout.fragment_play_lists, container, false);
        recyclerPlayList = fragmentPlayList.findViewById(R.id.playlist_recycler);

        MainActivity.setLocation("Playlists");

        arrayPlayList = new ArrayList<>();
        recyclerPlayList.setLayoutManager(new LinearLayoutManager(getContext()));
//        MyPlaylists.readPlaylists();
        playlists = MyPlaylists.playlists;
        //        fillArrayPlayList();
        itemMusicSmallAdapter = new ItemMusicSmallAdapter(playlists);
        recyclerPlayList.setAdapter(itemMusicSmallAdapter);
        setOnClickListeners();
        CreatePlaylistActivity.playlistsFragment = this;

        return fragmentPlayList;
    }

    private void setOnClickListeners() {
        LinearLayout createPlaylist = fragmentPlayList.findViewById(R.id.create_playlist);
        createPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("create playlist clicked");
                startActivity(new Intent(MainActivity.context, CreatePlaylistActivity.class));
            }
        });
    }

    public void refresh(){
        playlists = MyPlaylists.playlists;
        itemMusicSmallAdapter = new ItemMusicSmallAdapter(playlists);
        recyclerPlayList.setAdapter(itemMusicSmallAdapter);
    }

    private void fillArrayPlayList() {
        arrayPlayList.add(new ItemMusic("https://images-na.ssl-images-amazon.com/images/I/71v1pBcehfL._AC_SX342_.jpg","Evolve","Imagine Dragons", null));
        arrayPlayList.add(new ItemMusic("https://t2.genius.com/unsafe/220x220/https%3A%2F%2Fimages.genius.com%2Fbbea4553e61a94577e5d928cb49a5406.999x999x1.jpg","Under the moon","Foster the people", null));
        arrayPlayList.add(new ItemMusic("https://upload.wikimedia.org/wikipedia/en/thumb/f/f3/Trench_Twenty_One_Pilots.png/220px-Trench_Twenty_One_Pilots.png","Trench","Twenty one pilots", null));
        arrayPlayList.add(new ItemMusic("https://upload.wikimedia.org/wikipedia/en/0/01/OneRepublic_-_Human.png","Human","One Republic", null));
        arrayPlayList.add(new ItemMusic("https://www.elquintobeatle.com/wp-content/uploads/2019/10/coldplay-everyday-life-1-1068x1068.jpeg","Everyday life","Coldplay", null));
    }
}