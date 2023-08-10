package com.mangoplay.yeezymusic.ui.library.TabsFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.mangoplay.yeezymusic.MainActivity;
import com.mangoplay.yeezymusic.R;
import com.mangoplay.yeezymusic.adapters.ItemGenreAdapter;
import com.mangoplay.yeezymusic.objects.Genre;
import com.mangoplay.yeezymusic.services.DeezerService;

import java.util.ArrayList;
import java.util.List;


public class GenresFragment extends Fragment {

    //    String[] genres = {"pop", "rock", "dance", "r&b", "house", "trap"};
    List<Genre> genres = new ArrayList<>();
    RecyclerView recyclerView;
    View rootView;
    ItemGenreAdapter itemsAdapter;
    static Button refreshView;

    public GenresFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (genres.size() == 0) {
            genres = DeezerService.getGenres();
            for (Genre genre : genres) {
                new Thread(() -> {
                    genre.getArtistList();
                }).start();
                break;
            }
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_genres, container, false);

        recyclerView = rootView.findViewById(R.id.genres_recycler);

        System.out.println("genres create view");



        refreshView = new Button(MainActivity.context);
        refreshView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                System.out.println("refreshing view");
//                for(Playlist p : artists)
//                    System.out.println("artistId from playlist: " + p.getId());
                if(itemsAdapter != null && recyclerView != null)
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(itemsAdapter.getItemCount() > 0) {
                                itemsAdapter.notifyDataSetChanged();
//                                System.out.println("notified data set changed");
                            } else {
                                itemsAdapter = new ItemGenreAdapter(genres);
                                recyclerView.setAdapter(itemsAdapter);
//                                System.out.println("created new items adapter");
                            }
                        }
                    });
//                itemHomeAdapter.notifyItemInserted(homeElements.size());
            }
        });


        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false));
        itemsAdapter = new ItemGenreAdapter(genres);

        if(checkGenresLoaded(1)) itemsAdapter = new ItemGenreAdapter(genres);
        else itemsAdapter = new ItemGenreAdapter(new ArrayList<>());

        recyclerView.setAdapter(itemsAdapter);

        new Thread(() -> {
            if(checkGenresLoaded(3000)) try {
                refreshView.callOnClick();
//                System.out.println("called genre refresh");
            } catch (Exception e){}
        }).start();
    }

    public boolean checkGenresLoaded(int maxTime) {
        int counter = 0;
        while (true) {
            boolean breakAtTheEnd = true;
            if(genres.size() == 0) breakAtTheEnd = false;
            List<Genre> playlists = new ArrayList<Genre>(genres);
            for (Genre genre : playlists) {
                if (genre.getTitle() == null) {
                    breakAtTheEnd = false;
                }
            }
            if (breakAtTheEnd) break;
            else {
                counter++;
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (counter >= maxTime) break;
        }
        if (counter >= maxTime) return false;
        else return true;
    }
}