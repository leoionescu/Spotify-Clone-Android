package com.mangoplay.yeezymusic.ui.search;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mangoplay.yeezymusic.MainActivity;
import com.mangoplay.yeezymusic.adapters.PlaylistRowAdapter;
import com.mangoplay.yeezymusic.R;
import com.mangoplay.yeezymusic.objects.Playlist;
import com.mangoplay.yeezymusic.services.DeezerService;

public class SearchFragment extends Fragment {
    View rootView;
    RecyclerView recyclerView;
    static PlaylistRowAdapter itemsAdapter = null;
    static String search = null;
    Bundle bundle;

    public SearchFragment(){
        MainActivity.tryToShowAd();
    }


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_search, container, false);

        MainActivity.setLocation("Search");

        recyclerView = rootView.findViewById(R.id.search_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        EditText searchBar = (EditText)rootView.findViewById(R.id.search_bar);
        setSearchListener(searchBar);
        if(itemsAdapter != null){
            System.out.println("not null");
            recyclerView.setAdapter(itemsAdapter);
            searchBar.setText(search);
            }

        return rootView;
    }

    private void setSearchListener(EditText searchBar) {

        searchBar.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_ENTER){
                    System.out.println("pressed enter");
                    String text = searchBar.getText().toString();
                    if(text.length() > 0){
                        new Thread(() -> {
                            Playlist playlist = DeezerService.search(text.toString());
                            search = text.toString();///////
                            itemsAdapter = new PlaylistRowAdapter(playlist, null);
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    recyclerView.setAdapter(itemsAdapter);
                                }
                            });
                        }).start();
//                        System.out.println(playlist);
//                        MainActivity.playerFragment.play(track);
                    }
                    KeyboardDown();
                }
                return false;
            }
        });


//        searchBar.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                System.out.println("search text before: " + searchBar.getText());
//                System.out.println("key code: " + keyCode + ", event: " + event.toString());
//                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
//                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
//                    System.out.println("pressed enter");
//                    if(searchBar.getText().length() > 0){
//                        Playlist playlist = DeezerService.search(searchBar.getText().toString());
//                        search = searchBar.getText().toString();
//                        itemsAdapter = new PlaylistRowAdapter(playlist, null);
//                        recyclerView.setAdapter(itemsAdapter);
////                        System.out.println(playlist);
////                        MainActivity.playerFragment.play(track);
//                    }
//                    KeyboardDown();
//                } else if((event.getAction() == KeyEvent.ACTION_DOWN) &&
//                        (keyCode == KeyEvent.KEYCODE_DEL)){
//                    System.out.println("backspace pressed");
//                    Editable text = searchBar.getText();
//                    searchBar.setText(text.delete(text.length() - 1,text.length() - 1));
//                }
//                System.out.println("search text after: " + searchBar.getText());
//                return true;
//            }
//        });
        searchBar.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (searchBar.getText().toString().equals(new String("Artist, songs or podcasts"))) {
                    searchBar.setText("");
//                    System.out.println("text cleared");
                }
            }
        });
    }

    private void KeyboardDown() {
        InputMethodManager imm = (InputMethodManager) this.getActivity().getSystemService(this.getActivity().INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = this.getActivity().getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(this.getActivity());
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}