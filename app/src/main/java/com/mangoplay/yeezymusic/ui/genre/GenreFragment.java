package com.mangoplay.yeezymusic.ui.genre;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mangoplay.yeezymusic.MainActivity;
import com.mangoplay.yeezymusic.R;
import com.mangoplay.yeezymusic.adapters.ItemMusicBigAdapter;
import com.mangoplay.yeezymusic.objects.Genre;
import com.mangoplay.yeezymusic.objects.Playlist;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class GenreFragment extends Fragment {

    View rootView;
    TextView title;
    ImageButton backButton;
    ImageButton moreButton;
    RecyclerView recyclerView;
    ItemMusicBigAdapter itemsAdapter;
    public static Genre genre;
    public static Button refreshView;
    ProgressBar loadingCircle;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_genre, container, false);
        title = rootView.findViewById(R.id.title);
        backButton = rootView.findViewById(R.id.back_button);
        moreButton = rootView.findViewById(R.id.more_button);
        recyclerView = rootView.findViewById(R.id.recycler_view);
        loadingCircle = rootView.findViewById(R.id.progress_loader);



//        System.out.println("genre view create");

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        title.setText(genre.getTitle());

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
        recyclerView.addItemDecoration(new GenreItemDecoration(MainActivity.context));
        recyclerView.setLayoutManager(gridLayoutManager);
//        if(genresLoaded(1,1) > 0) {
//            itemsAdapter = new ItemMusicBigAdapter(genre.artists);
//        } else
        itemsAdapter = new ItemMusicBigAdapter(new ArrayList<>(), "GenreFragment");
        recyclerView.setAdapter(itemsAdapter);

        refreshView = new Button(MainActivity.context);
        refreshView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                System.out.println("refreshing view genre fragment");
                if(itemsAdapter != null && recyclerView != null)
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if (itemsAdapter.getItemCount() > 0) {
                                    itemsAdapter.notifyDataSetChanged();
//                                System.out.println("notified data set changed");
                                } else {
                                    itemsAdapter = new ItemMusicBigAdapter(genre.artists, genre.getTitle());
                                    recyclerView.setAdapter(itemsAdapter);
                                    loadingCircle.setVisibility(View.GONE);
//                                System.out.println("created new items adapter");
                                }
                            } catch (NullPointerException e) {}
                        }
                    });
//                itemHomeAdapter.notifyItemInserted(homeElements.size());
            }
        });

        new Thread(() -> {
            for(int i = 1; i <= genre.ids.size(); i++){
                if(genresLoaded(3000,i) >= i) try{
                    refreshView.callOnClick();
                    for(Playlist playlist : genre.artists){
                        System.out.println(playlist.toString());
                    }
                } catch (Exception e){}
            }

//            if(genresLoaded(3000, 10) >= 10) try {
//                refreshView.callOnClick();
//                if(genresLoaded(3000, 50) >= 48) try{
//                    refreshView.callOnClick();
//                } catch (Exception e){}
////                System.out.println("called artists refresh");
//            } catch (Exception e){}
        }).start();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                System.out.println("back");
//                MainActivity.navigate(R.id.navigation_home);
                long start = System.currentTimeMillis();
                itemsAdapter = null;
                getActivity().onBackPressed();
                long stop = System.currentTimeMillis();
//                System.out.println("on back pressed time: " + (stop - start));
//                MainActivity.goBack();
            }
        });

        moreButton.setVisibility(View.GONE);

    }

    public int genresLoaded(int maxTime, int minValue){
        long start = System.currentTimeMillis();

        int counter = 0;
        int loaded;
        while(true){
            loaded = 0;
            List<Playlist> playlists = new ArrayList<Playlist>(genre.artists);
            for (Playlist p : playlists){
                if(p != null) if(p.getTitle() != null && p.getPictureBig() != null && p.tracks.size() > 0){
                    loaded++;
                }
            }
//            System.out.println("loaded: " + loaded + "artists size: " + playlists.size());
            if(loaded >= minValue) break;
            else {
                counter++;
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if(counter >= maxTime) break;
        }
        long stop = System.currentTimeMillis();
//        System.out.println("get genres artists time: " + (stop - start));
//        for(Playlist p : artists) System.out.println(p.toString());
        if(counter == maxTime) return loaded;
        else return loaded;
    }
}
