package com.mangoplay.yeezymusic.ui.playlist;

import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.mangoplay.yeezymusic.MainActivity;
import com.mangoplay.yeezymusic.R;
import com.mangoplay.yeezymusic.adapters.PlaylistRowAdapter;
import com.mangoplay.yeezymusic.objects.Playlist;
import com.mangoplay.yeezymusic.objects.Queue;

import java.util.Collections;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PlaylistFragment extends Fragment {

    View rootView;
    RecyclerView recyclerView;
    PlaylistRowAdapter itemsAdapter;
    Playlist playlist = null;
    ImageButton backButton;
    ImageButton optionsButton;
    TextView title;
    Button shuffleButton;
    public Button refreshView;


    public PlaylistFragment(){
        System.out.println("playlist constructor");
        MainActivity.playlistFragment = this;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        long start = System.currentTimeMillis();

        rootView = inflater.inflate(R.layout.fragment_playlist, container, false);

        recyclerView = rootView.findViewById(R.id.playlist_recycler_view);
        backButton = rootView.findViewById(R.id.back_button);
        optionsButton = rootView.findViewById(R.id.more_button);
        title = rootView.findViewById(R.id.title);
        shuffleButton = rootView.findViewById(R.id.shuffle_button);


        title.setText(MainActivity.playlistToShow.getTitle());


        try {
            if(MainActivity.playlistToShow.tracks.size() > 0) {
                playlist = (Playlist) MainActivity.playlistToShow.clone();
                itemsAdapter = new PlaylistRowAdapter(playlist, this);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setAdapter(itemsAdapter);
            } else {
                recyclerView.setVisibility(View.GONE);
                TextView text = rootView.findViewById(R.id.textView);
                text.setVisibility(View.VISIBLE);
                text.setText(MainActivity.playlistToShow.getTitle() + " is empty.");
            }
            recyclerView.setHasFixedSize(true);
            recyclerView.setItemViewCacheSize(20);
            recyclerView.setDrawingCacheEnabled(true);
            recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
//            new Thread(() -> {
//                playlist.tracks = MainActivity.playlistToShow.tracks;
//                GetDrawablesList getDrawablesList = new GetDrawablesList();
//                getDrawablesList.setTracks(playlist.tracks);
//                try {
//                    itemsAdapter.drawableList = getDrawablesList.execute().get();
//                } catch (ExecutionException e) {
//                    e.printStackTrace();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
////                itemsAdapter.notifyDataSetChanged();
//                System.out.println("dataset changed");
//            }).start();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        setListeners();

        refreshView = new Button(MainActivity.context);
        refreshView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("refreshing view");
                if(itemsAdapter != null)
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            itemsAdapter.notifyDataSetChanged();
                        }
                    });
//                itemHomeAdapter.notifyItemInserted(homeElements.size());
            }
        });

        long stop = System.currentTimeMillis();
        System.out.println("playlist fragment create view time: " + (stop - start));

        return rootView;
    }

    private void setListeners() {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("back");
                try {
                    itemsAdapter.stopThread = true;
                } catch (NullPointerException e){}
//                MainActivity.navigate(R.id.navigation_home);
                long start = System.currentTimeMillis();
                getActivity().onBackPressed();
                long stop = System.currentTimeMillis();
                System.out.println("on back pressed time: " + (stop - start));
//                MainActivity.goBack();
            }
        });
        optionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("options");
            }
        });

        recyclerView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                int y = getScrollPercentage(recyclerView);
                if(y >= 90){
                    shuffleButton.setVisibility(View.GONE);
                } else {
                    shuffleButton.setVisibility(View.VISIBLE);
                }
            }
        });

        shuffleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(playlist.tracks.size() > 0){
                    try {
                        Playlist clone = (Playlist)playlist.clone();
                        Collections.shuffle(clone.tracks);
                        try {
                            MainActivity.playerFragment.play(clone.tracks.get(0));
                            Queue.refreshGeneratedPlaylist();
                            for(int i = 1; i < clone.tracks.size(); i++){
                                Queue.addToGeneratedPlaylist(clone.tracks.get(i));
                            }
                        } catch (NullPointerException e){
                            new Thread(() -> {
                                int tries = 0;
                                while(tries < 100) {
                                    System.out.println("trying");
                                    MainActivity.playerFragment.play(clone.tracks.get(0));
                                    Queue.refreshGeneratedPlaylist();
                                    for (int i = 1; i < clone.tracks.size(); i++) {
                                        Queue.addToGeneratedPlaylist(clone.tracks.get(i));
                                    }
                                    tries++;
                                    try {
                                        Thread.sleep(100);
                                    } catch (InterruptedException interruptedException) {}
                                }
                                if(tries == 100){
                                    Toast.makeText(v.getContext(), "Sorry can't shuffle right now",Toast.LENGTH_SHORT).show();

                                }
                            }).start();

                        }


                    } catch (CloneNotSupportedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

//        setTitleMarquee();

        System.out.println("listeners set");
    }

    private void setTitleMarquee() {
        title.setMarqueeRepeatLimit(-1);
        title.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        title.setSelected(true);
        Resources r = getResources();
        title.post(new Runnable() {
            @Override
            public void run() {
                System.out.println("in thread");

                int top = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2,r.getDisplayMetrics()));
                int left = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100,r.getDisplayMetrics()));

                ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(title.getWidth(), title.getHeight());
                params.setMargins(left, top, left, 0);
                title.setLayoutParams(params);

                ConstraintLayout constraintLayout = rootView.findViewById(R.id.top_bar);
                ConstraintSet constraintSet = new ConstraintSet();
                constraintSet.clone(constraintLayout);

                constraintSet.connect(R.id.title,ConstraintSet.END,R.id.more_button,ConstraintSet.START,left);
                constraintSet.connect(R.id.title,ConstraintSet.START,R.id.back_button,ConstraintSet.END,left);
                constraintSet.connect(R.id.title,ConstraintSet.TOP,ConstraintSet.PARENT_ID,ConstraintSet.TOP,top);
                constraintSet.applyTo(constraintLayout);
//                textSlideUp.setLayoutParams(params);
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
