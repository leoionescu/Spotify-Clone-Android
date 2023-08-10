package com.mangoplay.yeezymusic.ui.playlist;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.mangoplay.yeezymusic.R;
import com.mangoplay.yeezymusic.adapters.AddSongAdapter;
import com.mangoplay.yeezymusic.objects.ForYou;
import com.mangoplay.yeezymusic.objects.History;
import com.mangoplay.yeezymusic.objects.MyPlaylists;
import com.mangoplay.yeezymusic.objects.Playlist;
import com.mangoplay.yeezymusic.objects.Track;
import com.mangoplay.yeezymusic.ui.library.TabsFragments.PlayListsFragment;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CreatePlaylistActivity extends AppCompatActivity {

    boolean changedName = false, changedDescription = false;
    public static PlayListsFragment playlistsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_playlist);

        setOnClickListeners();
    }

    private void setOnClickListeners() {
        Button create = findViewById(R.id.create);
        Button cancel = findViewById(R.id.cancel);
        EditText name = findViewById(R.id.playlist_name);
        EditText description = findViewById(R.id.description);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("cancel");
                finish();
            }
        });
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("create");
                Playlist playlist = new Playlist();
                playlist.setTitle(name.getText().toString());
                if(description.getText().length() > 0 && changedDescription) playlist.setDescription(description.getText().toString());
                MyPlaylists.playlists.add(1, playlist);

                setContentView(R.layout.add_songs);
                createAdSongsView(playlist);
            }
        });
        name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus && !changedName){
                    name.setText("");
                    changedName = true;
                } else if(!hasFocus && name.getText().length() == 0){
                    name.setText("Name");
                    changedName = false;
                }
            }
        });
        description.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus && !changedDescription){
                    description.setText("");
                    changedDescription = true;
                } else if(!hasFocus && description.getText().length() == 0){
                    description.setText("Description (Optional)");
                    changedDescription = false;
                }
            }
        });
        name.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_ENTER){
                    System.out.println("pressed enter");
                    create.callOnClick();
                }
                return false;
            }
        });

    }

    private void createAdSongsView(Playlist playlist) {
        RecyclerView recyclerView;
        AddSongAdapter itemsAdapter;
        recyclerView = findViewById(R.id.playlist_recycler_view);
        Playlist tracksToAdd = new Playlist();
        try {
            tracksToAdd = (Playlist) History.playlist.clone();
            List<Playlist> forYouPlaylists = new ArrayList<>(ForYou.forYouPlaylists);
            for(Playlist playlist1 : forYouPlaylists){
                tracksToAdd.tracks.addAll(playlist1.tracks);
            }
        } catch (CloneNotSupportedException e) {
            tracksToAdd = History.playlist;
            e.printStackTrace();
        }
        itemsAdapter = new AddSongAdapter(tracksToAdd.tracks);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(itemsAdapter);

        Button addSongsButton = findViewById(R.id.add_songs_button);
        ImageButton backButton = findViewById(R.id.back_button);

        addSongsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = 0;
                for(boolean b : itemsAdapter.checked) if(b) count++;
                System.out.println("Number of songs to add: " + count);
                for(int i = 0; i < itemsAdapter.checked.length; i++){
                    if(itemsAdapter.checked[i]){
                        playlist.tracks.add(itemsAdapter.tracks.get(i));
                    }
                }

                MyPlaylists.writePlaylists();
                setContentView(R.layout.activiy_playlist_created);
                Button continueButton = findViewById(R.id.continue_button);
                continueButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        playlistsFragment.refresh();
                        finish();
                    }
                });

            }
        });

        recyclerView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                int y = getScrollPercentage(recyclerView);
                if(y >= 90){
                    addSongsButton.setVisibility(View.GONE);
                } else {
                    addSongsButton.setVisibility(View.VISIBLE);
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("back button clicked");
                MyPlaylists.playlists.remove(playlist);
                finish();
            }
        });

    }

    public int getScrollPercentage(RecyclerView recyclerView){
        int offset = recyclerView.computeVerticalScrollOffset();
        int extent = recyclerView.computeVerticalScrollExtent();
        int range = recyclerView.computeVerticalScrollRange();

        int percentage = (int)(100.0 * offset / (float)(range - extent));

        return percentage;
    }
}
