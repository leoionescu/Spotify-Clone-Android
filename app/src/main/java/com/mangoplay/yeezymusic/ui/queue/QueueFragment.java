package com.mangoplay.yeezymusic.ui.queue;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mangoplay.yeezymusic.MainActivity;
import com.mangoplay.yeezymusic.R;
import com.mangoplay.yeezymusic.adapters.PlaylistRowAdapter;
import com.mangoplay.yeezymusic.objects.Queue;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class QueueFragment extends Fragment {

    View rootView;
    RecyclerView recyclerView;
    PlaylistRowAdapter itemsAdapter;
    private ImageButton backButton;
    private ImageButton optionsButton;
    private TextView title;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_playlist, container, false);

        MainActivity.setLocation("Queue");

        recyclerView = rootView.findViewById(R.id.playlist_recycler_view);
        backButton = rootView.findViewById(R.id.back_button);
        optionsButton = rootView.findViewById(R.id.more_button);
        optionsButton.setVisibility(View.GONE);
        title = rootView.findViewById(R.id.title);

        title.setText("Queue");

        itemsAdapter = new PlaylistRowAdapter(Queue.getCombinedPlaylist(), null);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(itemsAdapter);

        setListeners();

        return rootView;
    }

    private void setListeners() {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("back");
//                MainActivity.navigate(R.id.navigation_home);
//                MainActivity.slidingLayout.callOnClick();
                MainActivity.dragView.callOnClick();
//                MainActivity.slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
//                MainActivity.slidingLayout.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis() + 100, MotionEvent.ACTION_UP, 0, 0, 0));
                MainActivity.goBack();
            }
        });
        optionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("options");

            }
        });
        System.out.println("listeners set");
    }

}
