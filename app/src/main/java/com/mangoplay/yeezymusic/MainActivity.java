package com.mangoplay.yeezymusic;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mangoplay.yeezymusic.objects.MyPlaylists;
import com.mangoplay.yeezymusic.objects.Playlist;
import com.mangoplay.yeezymusic.objects.Queue;
import com.mangoplay.yeezymusic.objects.Settings;
import com.mangoplay.yeezymusic.objects.Track;
import com.mangoplay.yeezymusic.objects.User;
import com.mangoplay.yeezymusic.ui.home.HomeFragment;
import com.mangoplay.yeezymusic.ui.player.PlayerFragment;
import com.mangoplay.yeezymusic.ui.playlist.PlaylistFragment;
import com.mangoplay.yeezymusic.ui.settings.SettingsActivity;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.spotify.android.appremote.api.SpotifyAppRemote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;


import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelSlideListener;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState;

import java.util.Set;


public class MainActivity extends AppCompatActivity {

    private static final String CLIENT_ID = "03367b5b788c4d5992b0b65eba038d10";
    private static final String REDIRECT_URI = "http://com.example.spotifyclone/callback";
    private static float lastOffset;
    private SpotifyAppRemote mSpotifyAppRemote;
    public static Context context = null;
    public static BottomNavigationView navView;
    public static PlayerFragment playerFragment;
    public static HomeFragment homeFragment;
    public static PlaylistFragment playlistFragment;
    private static NavController navController;
    public static Playlist playlistToShow;
    public static Activity mainActivity;
    public static Bundle bundle;
    public static int selectedMenuItem;
    public static SlidingUpPanelLayout slidingLayout;
    public static ImageButton playSlideUp;
    public static Drawable playDrawable;
    public static Drawable pauseDrawable;
    public static ImageButton playSlideUpTest;
    public static Drawable playerBackground;
    public static int averageColor;
    public static String location;
    public static Settings settings = null;
    public static Resources r;
    public static LinearLayout dragView;
    public static Intent settingsIntent;
    public static AdView mAdView;
    public static AdRequest adRequest;
    public static InterstitialAd mInterstitialAd;
    public static long timeOfLastAd = 0;
    public static long timeBetweenAds = 60000;
    public static boolean showAd = false; //if the first add was shown
    public static boolean showAds = false; //if the app shows ads or not


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        long start = System.currentTimeMillis();
        System.out.println("mainActivity started");
        location = "Home";
        mainActivity = this;
        getWindow().setFormat(PixelFormat.RGBA_8888);
        setContentView(R.layout.activity_main);
        navView = findViewById(R.id.nav_view);
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);
        context = getApplicationContext();
        r = getResources();
        settingsIntent = new Intent(context, SettingsActivity.class);
        SlidingUpPanelLayout slidingLayout = (SlidingUpPanelLayout)findViewById(R.id.sliding_layout);
        MainActivity.slidingLayout = slidingLayout;
        mAdView = findViewById(R.id.adView);
        if(isNetworkAvailable()) {
            getTimeBetweenAds(); ////doesn t work //////////////////////////
            if(showAds) {
                loadBanner();
                loadAd();

            }
            setUpSlidingLayout(slidingLayout);
            init();
        } else {
            showNoConnectionMessage();
        }
        long stop = System.currentTimeMillis();
        System.out.println("main activity time: " + (stop - start));
        playSlideUpTest = findViewById(R.id.play_slide_up);
    }

    private void getTimeBetweenAds() {
        new Thread(() -> {
            System.out.println("getting timeBetweenAds");
            readFirebaseStorage("timeBetweenAds.txt");
//            System.out.println("time between ads after firebase storage: " + timeBetweenAds);
        }).start();
    }

    private void readFirebaseStorage(String fileName) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference pathReference = storageRef.child(fileName);
        pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                System.out.println("success uri: " + uri);
                StringRequest stringRequest = new StringRequest(Request.Method.GET, uri.toString(),
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    System.out.println("timeBetweenAds response: " + response.toString());
                                    timeBetweenAds = Integer.parseInt(response);
                                    logTimeBetweenAdsReadSuccessfull();
                                } catch (NullPointerException e){
                                    System.out.println("timeBetweenAds failed, remains 60000");
                                    logTimeBetweenAdsReadFailed();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        logTimeBetweenAdsReadFailed();
                    }
                });
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(stringRequest);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("timeBetweenAds failed");
                e.printStackTrace();
            }
        });
    }

    private void loadBanner() {
        adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }

    private void init() {

        if(!HomeFragment.playlistsCreated){
            MainActivity.homeFragment.createHomePlaylists();
            HomeFragment.playlistsCreated = true;
        }
        MyPlaylists.readPlaylists();
//        Library.readArtists();
//        Library.readAlbums();
        settings = Settings.load();
        settings.numberOfOpens++;
        logNumberOfOpens(settings);
        settings.save();


        TextView textSlideUp = findViewById(R.id.name_slide_up);
//        int px = Math.round(TypedValue.applyDimension(
//                TypedValue.COMPLEX_UNIT_DIP, 40,r.getDisplayMetrics()));
//        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, px);
        ViewGroup.LayoutParams params = textSlideUp.getLayoutParams();
//        params.constrainedWidth = true;
//        textSlideUp.setLayoutParams(params);
//        textSlideUp.setSingleLine(true);
//        textSlideUp.setHorizontallyScrolling(true);
//        textSlideUp.setFocusable(true);
//        textSlideUp.setFocusableInTouchMode(true);
        textSlideUp.setMarqueeRepeatLimit(-1);
        textSlideUp.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        textSlideUp.setSelected(true);
        textSlideUp.post(new Runnable() {
            @Override
            public void run() {
                System.out.println("in thread");

                int top = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2,r.getDisplayMetrics()));
                int left = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5,r.getDisplayMetrics()));

                ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(textSlideUp.getWidth(), textSlideUp.getHeight());
                params.setMargins(left, top, left, 0);
                textSlideUp.setLayoutParams(params);

                ConstraintLayout constraintLayout = findViewById(R.id.parent_layout);
                ConstraintSet constraintSet = new ConstraintSet();
                constraintSet.clone(constraintLayout);

                constraintSet.connect(R.id.name_slide_up,ConstraintSet.END,R.id.play_slide_up,ConstraintSet.START,left);
                constraintSet.connect(R.id.name_slide_up,ConstraintSet.START,R.id.image_slide_up,ConstraintSet.END,left);
                constraintSet.connect(R.id.name_slide_up,ConstraintSet.TOP,ConstraintSet.PARENT_ID,ConstraintSet.TOP,top);
                constraintSet.applyTo(constraintLayout);
//                textSlideUp.setLayoutParams(params);
            }
        });
    }

    public static void navigate(int resId){
        long start = System.currentTimeMillis();
        MainActivity.selectedMenuItem = navView.getSelectedItemId();
        bundle = navController.saveState();
        navController.navigate(resId);
        long stop = System.currentTimeMillis();
        System.out.println("time to navigate: " + (stop - start));
        //        navController.navigate(resId, bundle);
    }

    public static void goBack(){
        navController.navigate(selectedMenuItem, bundle);
    }

    public static void goToMenuItem(){
        navController.navigate(navView.getSelectedItemId());

    }

    private void setUpSlidingLayout(SlidingUpPanelLayout slidingLayout) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        playerFragment = new PlayerFragment();
        transaction.replace(R.id.player_fragment, playerFragment).commit();

        dragView = (LinearLayout) findViewById(R.id.dragView);

        SlidingUpPanelLayout ogSlideUpLinearLayout = findViewById(R.id.sliding_layout);

        playSlideUp = findViewById(R.id.play_slide_up);
        TextView textSlideUp = findViewById(R.id.name_slide_up);
        ImageButton changeStateButton = findViewById(R.id.change_state_slide_up);
        ImageButton queueButton = findViewById(R.id.queue_slide_up);
        ImageButton imageSlideUp = findViewById(R.id.image_slide_up);
        View blackLine = findViewById(R.id.black_line);

        slidingLayout.addPanelSlideListener(new PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                if(slideOffset > 0.1){
                    changeStateButton.setVisibility(View.VISIBLE);
                    queueButton.setVisibility(View.VISIBLE);
                    playSlideUp.setVisibility(View.GONE);
                    textSlideUp.setVisibility(View.GONE);
                    imageSlideUp.setVisibility(View.GONE);
//                    dragView.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.player_background4));
//                    dragView.setBackground(playerBackground);

                    GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{averageColor,Color.parseColor("#0D0D0D")});
                    gradientDrawable.setDither(true);
                    dragView.setBackground(gradientDrawable);

                    if(isColorDark(averageColor)){
                        changeStateButton.setColorFilter(ContextCompat.getColor(context, R.color.gray), android.graphics.PorterDuff.Mode.MULTIPLY);
                        queueButton.setColorFilter(ContextCompat.getColor(context, R.color.gray), android.graphics.PorterDuff.Mode.MULTIPLY);
                    } else{
                        changeStateButton.setColorFilter(ContextCompat.getColor(context, R.color.gray_strong), android.graphics.PorterDuff.Mode.MULTIPLY);
                        queueButton.setColorFilter(ContextCompat.getColor(context, R.color.gray_strong), android.graphics.PorterDuff.Mode.MULTIPLY);
                    }

                    findViewById(R.id.nav_replacer).setVisibility(View.VISIBLE);

                    if(slideOffset > 0.95) {
                        blackLine.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.gray_strong));
                        navView.setVisibility(View.GONE);
                    }
                } else {
                    navView.setVisibility(View.VISIBLE);
                    playSlideUp.setVisibility(View.VISIBLE);
                    textSlideUp.setVisibility(View.VISIBLE);
                    imageSlideUp.setVisibility(View.VISIBLE);
                    dragView.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.color.gray_strong));
                    blackLine.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.gray));
                    changeStateButton.setVisibility(View.GONE);
                    queueButton.setVisibility(View.GONE);
                    findViewById(R.id.nav_replacer).setVisibility(View.GONE);
                }

                if(lastOffset - slideOffset > 0){
                    navView.setVisibility(View.VISIBLE);
                    blackLine.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.gray));
                }
                lastOffset = slideOffset;
            }

            @Override
            public void onPanelStateChanged(View panel, PanelState previousState, PanelState newState) {
//                System.out.println("state changed, state: " + newState);

//                if(newState == PanelState.DRAGGING)
//                if(previousState == PanelState.COLLAPSED){
//                    playSlideUp.setVisibility(View.GONE);
//                    textSlideUp.setVisibility(View.GONE);
//                    imageSlideUp.setVisibility(View.GONE);
//                    changeStateButton.setVisibility(View.VISIBLE);
//                    queueButton.setVisibility(View.VISIBLE);
////                    dragView.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.player_background4));
////                    dragView.setBackground(playerBackground);
//
//                    GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{averageColor, averageColor, Color.parseColor("#000000"), Color.parseColor("#000000")});
//                    gradientDrawable.setDither(true);
//                    dragView.setBackground(gradientDrawable);
//
//                    if(isColorDark(averageColor)){
//                        changeStateButton.setColorFilter(ContextCompat.getColor(context, R.color.gray), android.graphics.PorterDuff.Mode.MULTIPLY);
//                        queueButton.setColorFilter(ContextCompat.getColor(context, R.color.gray), android.graphics.PorterDuff.Mode.MULTIPLY);
//                    } else{
//                        changeStateButton.setColorFilter(ContextCompat.getColor(context, R.color.gray_strong), android.graphics.PorterDuff.Mode.MULTIPLY);
//                        queueButton.setColorFilter(ContextCompat.getColor(context, R.color.gray_strong), android.graphics.PorterDuff.Mode.MULTIPLY);
//                    }
//
//                        navView.setVisibility(View.GONE);
//                    findViewById(R.id.nav_replacer).setVisibility(View.VISIBLE);
//                    blackLine.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.gray_strong));
//
//                } else {
//                    playSlideUp.setVisibility(View.VISIBLE);
//                    textSlideUp.setVisibility(View.VISIBLE);
//                    imageSlideUp.setVisibility(View.VISIBLE);
//                    changeStateButton.setVisibility(View.GONE);
//                    queueButton.setVisibility(View.GONE);
//                    dragView.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.color.gray_strong));
//                    navView.setVisibility(View.VISIBLE);
//                    findViewById(R.id.nav_replacer).setVisibility(View.GONE);
//                    blackLine.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.gray));
//                } else if(previousState == PanelState.DRAGGING)
//                    if(newState == PanelState.COLLAPSED){
//                    dragView.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.color.gray_strong));
//                    navView.setVisibility(View.VISIBLE);
//                    blackLine.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.gray));
//                    playSlideUp.setVisibility(View.VISIBLE);
//                    textSlideUp.setVisibility(View.VISIBLE);
//                    imageSlideUp.setVisibility(View.VISIBLE);
//                    changeStateButton.setVisibility(View.GONE);
//                    queueButton.setVisibility(View.GONE);
//                    findViewById(R.id.nav_replacer).setVisibility(View.GONE);
//                    } else if(newState == PanelState.EXPANDED){
//                    changeStateButton.setVisibility(View.VISIBLE);
//                    queueButton.setVisibility(View.VISIBLE);
//                    findViewById(R.id.nav_replacer).setVisibility(View.VISIBLE);
//                    blackLine.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.gray_strong));
//                    playSlideUp.setVisibility(View.GONE);
//                    textSlideUp.setVisibility(View.GONE);
//                    imageSlideUp.setVisibility(View.GONE);
//                    dragView.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.player_background4));
//                    dragView.setBackground(playerBackground);

//                    System.out.println("average color: " + averageColor);
//                    GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{averageColor,Color.parseColor("#000000")});
//                    gradientDrawable.setDither(true);
//                    dragView.setBackground(gradientDrawable);
//
//                    navView.setVisibility(View.GONE);
//                }
            }
        });

        ImageButton stateChanger = findViewById(R.id.change_state_slide_up);
        stateChanger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slidingLayout.setPanelState(PanelState.COLLAPSED);
            }
        });

        ImageButton queue = findViewById(R.id.queue_slide_up);
        queue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                try {
    //                    Queue.playlist = (Playlist)History.playlist.clone();
//                } catch (CloneNotSupportedException e) {
//                    e.printStackTrace();
//                }
                System.out.println("queue clicked");
                System.out.println("queue size: " + Queue.playlist.tracks.size());
                MainActivity.setLocation("Queue");
                MainActivity.navigate(R.id.navigation_queue);
                slidingLayout.setPanelState(PanelState.COLLAPSED);
            }
        });

//        ImageButton imageSlideUp = findViewById(R.id.image_slide_up);
        imageSlideUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slidingLayout.setPanelState(PanelState.EXPANDED);
            }
        });

        playDrawable = ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_baseline_play_circle_outline_24);
        pauseDrawable = ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_baseline_pause_circle_outline_24);

//        playSlideUp = findViewById(R.id.play_slide_up);
        playSlideUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("clicked play slide up");
                System.out.println(playerFragment.getTracker().getState().toString());
                if(playerFragment.getTracker().getState() == PlayerConstants.PlayerState.PLAYING){
                    System.out.println("in playing");
                    playerFragment.getPlayer().pause();
//                    playSlideUp.setBackground(playDrawable);
                    playSlideUp.setImageDrawable(playDrawable);
                    playerFragment.playButton.setBackground(playerFragment.playDrawable);
                } else {
                    System.out.println("in paused");
                    playerFragment.getPlayer().play();
                    playerFragment.playButton.setBackground(playerFragment.pauseDrawable);
//                    playSlideUp.setBackground(pauseDrawable);
                    playSlideUp.setImageDrawable(pauseDrawable);
                }
            }
        });

    }

    public static void setMiniPlayer(Track track){
        System.out.println("set mini player called");
        ImageButton imageButton = mainActivity.findViewById(R.id.image_slide_up);
        TextView title = mainActivity.findViewById(R.id.name_slide_up);

        imageButton.setBackground(track.getDrawable());
        title.setText(track.getTitle() + " - " + track.getArtist());


//        title.setSelected(true);
    }

    @Override
    public void onBackPressed() {
        if(slidingLayout.getPanelState() == PanelState.EXPANDED) {
            System.out.println("collapsing");
            slidingLayout.setPanelState(PanelState.COLLAPSED);
        }
        else {
            super.onBackPressed();
//            super.onBackPressed();
            System.out.println("going back");
//            navController.navigate(selectedMenuItem, bundle);
//            navController.navigate(getFragmentManager().getFragments().get(0));
        }
    }

    public static int getAverageColor(Bitmap bitmap){
        long redBucket = 0;
        long greenBucket = 0;
        long blueBucket = 0;
        long pixelCount = 0;

        for (int y = 0; y < bitmap.getHeight(); y++)
        {
            for (int x = 0; x < bitmap.getWidth(); x++)
            {
                int c = bitmap.getPixel(x, y);

                pixelCount++;
                redBucket += Color.red(c);
                greenBucket += Color.green(c);
                blueBucket += Color.blue(c);
                // does alpha matter?
            }
        }

        int aaverageColor = Color.rgb(redBucket / pixelCount,
                greenBucket / pixelCount,
                blueBucket / pixelCount);
        System.out.println("average color in function: " + aaverageColor);
//        LinearGradient linearGradient = new LinearGradient(0,0,0,0,averageColor,Color.parseColor("#80000000"), Shader.TileMode.CLAMP);
        averageColor = aaverageColor;
        return aaverageColor;
    }

    public static int getDominantColor(Bitmap bitmap) {
        if (null == bitmap) return Color.TRANSPARENT;

        int redBucket = 0;
        int greenBucket = 0;
        int blueBucket = 0;
        int alphaBucket = 0;

        boolean hasAlpha = bitmap.hasAlpha();
        int pixelCount = bitmap.getWidth() * bitmap.getHeight();
        int[] pixels = new int[pixelCount];
        bitmap.getPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());

        for (int y = 0, h = bitmap.getHeight(); y < h; y++)
        {
            for (int x = 0, w = bitmap.getWidth(); x < w; x++)
            {
                int color = pixels[x + y * w]; // x + y * width
                redBucket += (color >> 16) & 0xFF; // Color.red
                greenBucket += (color >> 8) & 0xFF; // Color.greed
                blueBucket += (color & 0xFF); // Color.blue
                if (hasAlpha) alphaBucket += (color >>> 24); // Color.alpha
            }
        }

        return Color.argb(
                (hasAlpha) ? (alphaBucket / pixelCount) : 255,
                redBucket / pixelCount,
                greenBucket / pixelCount,
                blueBucket / pixelCount);
    }

    public boolean isColorDark(int color){
        double darkness = 1-(0.299*Color.red(color) + 0.587*Color.green(color) + 0.114*Color.blue(color))/255;
        if(darkness<0.5){
            return false; // It's a light color
        }else{
            return true; // It's a dark color
        }
    }

    public static void hideBottom(){
        slidingLayout.setPanelState(PanelState.HIDDEN);
        navView.setVisibility(View.GONE);
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) slidingLayout.getLayoutParams();
        params.setMargins(0,0,0,0);
        slidingLayout.setLayoutParams(params);
    }

    public static void showBottom(){
        slidingLayout.setPanelState(PanelState.COLLAPSED);
        navView.setVisibility(View.VISIBLE);
    }

    public static String getLocation(){
        return location;
    }

    public static void setLocation(String location){
        MainActivity.location = location;
    }

    public static void saveSettings(Settings settings){
//        if(MainActivity.settings != null){
//            if(MainActivity.settings.isShowVideo() != settings.isShowVideo()){
//                //change player
//
//            }
//        }
        MainActivity.settings = settings;
        playerFragment.applySettings();
        settings.save();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void showNoConnectionMessage(){
        new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.myDialog))
                .setTitle("No Internet Connection")
                .setMessage("You need to be connected to the internet to use this app.")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton("I am connected now", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if(isNetworkAvailable()) {
                                setUpSlidingLayout(slidingLayout);
                                init();
                        } else showNoConnectionMessage();
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public static void showUserIsBannedMessage(Context context1){
        System.out.println("showing user is banned message");
        new AlertDialog.Builder(new ContextThemeWrapper(context1, R.style.myDialog))
                .setTitle("User is banned")
                .setMessage("We are sorry, but this user has been banned from using our services.")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton("Quit app", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        mainActivity.finishAndRemoveTask();
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public static void loadAd(){
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(MainActivity.context, MainActivity.r.getString(R.string.interstitial_ad_unit_id), adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
//                    super.onAdLoaded(interstitialAd);
                mInterstitialAd = interstitialAd;
                System.out.println("onAdLoaded");
                if(showAd){
                    showAd = false;
                    showAd();
                }
            }
        });
    }

    public static void tryToShowAd(){
        System.out.println("try to show ad");
        System.out.println("timeBetweenAds: " + timeBetweenAds);
        long currentTime = System.currentTimeMillis();
        if(currentTime - timeOfLastAd > timeBetweenAds){
            showAd();
        }
    }

    public static void showAd(){
        if (mInterstitialAd != null) {
            mInterstitialAd.show(mainActivity);
            System.out.println("The interstitial ad is showing");
            mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
                @Override
                public void onAdDismissedFullScreenContent() {
                    // Called when fullscreen content is dismissed.
                    System.out.println("The ad was dismissed.");
                    loadAd();
                }

                @Override
                public void onAdFailedToShowFullScreenContent(AdError adError) {
                    // Called when fullscreen content failed to show.
                    System.out.println("The ad failed to show.");
                    loadAd();
                }

                @Override
                public void onAdShowedFullScreenContent() {
                    // Called when fullscreen content is shown.
                    // Make sure to set your reference to null so you don't
                    // show it a second time.
                    mInterstitialAd = null;
                    timeOfLastAd = System.currentTimeMillis();
                    System.out.println("The ad was shown.");
                    loadAd();
                }
            });
        } else {
            System.out.println("The interstitial ad wasn't ready yet.");
        }
    }

    void logTimeBetweenAdsReadSuccessfull(){
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "time_between_ads_successfull");
        mFirebaseAnalytics.logEvent("time_between_ads_successfull", bundle);
    }

    void logTimeBetweenAdsReadFailed(){
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "time_between_ads_failed");
        mFirebaseAnalytics.logEvent("time_between_ads_failed", bundle);
    }

    void logNumberOfOpens(Settings settings){
        int numberOfOpens = settings.numberOfOpens;
        System.out.println("numberOfOpens: " + numberOfOpens);
        System.out.println("reviewedTheApp: " + settings.reviewedTheApp);
        if(numberOfOpens >= 5) {
            FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
            Bundle bundle = new Bundle();
            String name = "";
            switch (numberOfOpens) {
                case 5:
                    name = "number_of_opens_" + "5";
                    if(!settings.reviewedTheApp) {
                        showReviewMessage();
                        settings.reviewedTheApp = true;
                        settings.save();
                    }
                    break;
                case 10:
                    name = "number_of_opens_" + "10";
                    break;
                case 20:
                    name = "number_of_opens_" + "20";
                    break;
                case 30:
                    name = "number_of_opens_" + "30";
                    if(!settings.reviewedTheApp) {
                        showReviewMessage();
                        settings.reviewedTheApp = true;
                        settings.save();
                    }
                    break;
                case 50:
                    name = "number_of_opens_" + "50";
                    if(!settings.reviewedTheApp) {
                        showReviewMessage();
                        settings.reviewedTheApp = true;
                        settings.save();
                    }
                    break;
                case 100:
                    name = "number_of_opens_" + "100";
                    if(!settings.reviewedTheApp) {
                        showReviewMessage();
                        settings.reviewedTheApp = true;
                        settings.save();
                    }
                    break;
            }
            if(name.length() > 0) {
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, name);
                mFirebaseAnalytics.logEvent(name, bundle);
            }
        }
    }

    void showReviewMessage(){
        System.out.println("showing review message");
        new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.myDialog))
                .setTitle("Rate our app")
                .setMessage("If you love our app, please take a moment to rate it.")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finishAndRemoveTask();
                    }
                }).setPositiveButton("Rate", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.out.println("review");
                        final String myappPackageName = getPackageName(); // getPackageName() from Context or Activity object
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + myappPackageName)));
                        } catch (android.content.ActivityNotFoundException anfe) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + myappPackageName)));
                        }
                    }
        })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setIcon(R.drawable.ic_baseline_favorite_24)
                .show();
    }
}