package com.example.musicproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.gson.Gson;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    //Elements
    ConstraintLayout parent_layout;
    TextView songtitle;
    private static final String TAG = "MainActivity";
    private MediaPlayer mMediaplayer;
    private Button mShuffle;
    private ImageView mPlayPause, mPlaypause, mCoverimage;
    private ConstraintLayout ToolBar;
    private TextView songname, songartist, songnameoverlay, songartistoverlay, durationtext, progresstext;
    private ImageView msongimage, mArrow, mNext, mPrevious;
    private ConstraintLayout main, overlay;
    private SeekBar mSeekBar;

    //vars
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mArtists = new ArrayList<>();
    private ArrayList<String> mImages = new ArrayList<>();
    private Random generator = new Random();
    private musicplayer mMusicPlayer = new musicplayer();
    private String currentSong = "";
    private String currentImage = "";
    private Context context = this;
    private String songplayed = "";
    private String currentbutton = "";
    private int mPosition;
    private int mDuration;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: started.");
        mSeekBar = findViewById(R.id.seekBar);




        initImageBitmaps();

        //Sets all the variables to their respective elements
        parent_layout = findViewById(R.id.parent_layout);
        songtitle = findViewById(R.id.songtitle);
        mPlayPause = findViewById(R.id.playpause);
        mShuffle = findViewById(R.id.shuffle);
        ToolBar  = findViewById(R.id.toolbar);
        main = findViewById(R.id.mainconstraint);
        overlay = findViewById(R.id.overlay);
        mPlaypause = findViewById(R.id.playpause1);
        mCoverimage = findViewById(R.id.coverimage);
        mArrow = findViewById(R.id.arrow);
        mNext = findViewById(R.id.next);
        mPrevious = findViewById(R.id.previous);
        durationtext = findViewById(R.id.duration);
        progresstext = findViewById(R.id.progress);


        ImageView img = findViewById(R.id.imageView);
        Glide.with(context)
                .load(R.drawable.ncs)
                .into(img);

        //Play pause button
        mPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG, "onClick: playpause");
                if (currentbutton == "pause"){
                    mPlayPause.setImageResource(android.R.drawable.ic_media_pause);
                    currentbutton = "play";
                    Log.d(TAG, "onClick: music was played");
                    doMusicPlayerAction(currentbutton);
                }
                else if (currentbutton == "play"){
                    mPlayPause.setImageResource(android.R.drawable.ic_media_play);
                    currentbutton = "pause";
                    Log.d(TAG, "onClick: music was paused");
                    doMusicPlayerAction(currentbutton);
                }
            }
        });



        //Shuffle Button
        mShuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSeekBar.setProgress(0);
                ToolBar.setVisibility(View.VISIBLE);
                String image = "";
                String author = "";
                Log.d(TAG, "onClick: shuffle");
                currentbutton = "play";
                mPlayPause.setImageResource(android.R.drawable.ic_media_pause);
                int randomIndex;
                int randomIndex2 = 0;
                randomIndex = generator.nextInt(mNames.size());

                if (currentSong == songplayed){

                    randomIndex2 = generator.nextInt(mNames.size());
                    songplayed = mNames.get(randomIndex2);
                    image = mImages.get(randomIndex2);
                    author = mArtists.get(randomIndex2);
                    setPosition(randomIndex2);
                }
                else if (songplayed == mNames.get(randomIndex)){
                    randomIndex2 = generator.nextInt(mNames.size());
                    songplayed = mNames.get(randomIndex2);
                    image = mImages.get(randomIndex2);
                    author = mArtists.get(randomIndex2);
                    setPosition(randomIndex2);
                }
                else{
                    songplayed = mNames.get(randomIndex);
                    image = mImages.get(randomIndex);
                    author = mArtists.get(randomIndex);
                    setPosition(randomIndex);
                }

                mMusicPlayer.shufflebutton(songplayed, context, author);
                Log.d(TAG, "shuffled song is:  " + songplayed);
                setCurrent(image);
            }
        });

        //Tool bar onclick
        ToolBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                overlay.setVisibility(View.INVISIBLE);
                overlay.animate()
                        .translationY(main.getHeight())
                        .alpha(0.0f)
                        .setDuration(0)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                main.animate()
                                        .translationY(main.getHeight())
                                        .alpha(0.0f)
                                        .setDuration(400)
                                        .setListener(new AnimatorListenerAdapter() {
                                            @Override
                                            public void onAnimationEnd(Animator animation) {
                                                super.onAnimationEnd(animation);
                                                main.setVisibility(View.GONE);
                                                overlay.setVisibility(View.VISIBLE);
                                                overlay.animate()
                                                        .translationY(0)
                                                        .alpha(1.0f)
                                                        .setDuration(400)
                                                        .setListener(new AnimatorListenerAdapter() {
                                                            @Override
                                                            public void onAnimationEnd(Animator animation) {
                                                                super.onAnimationEnd(animation);

                                                            }
                                                        });

                                            }
                                        });
                            }
                        });
                if (currentbutton == "pause"){
                    mPlaypause.setImageResource(R.drawable.play);

                }
                else if (currentbutton == "play"){
                    mPlaypause.setImageResource(R.drawable.pause);

                }

                Glide.with(context)
                        .load(currentImage)
                        .into(mCoverimage);
            }
        });





        //-----------OVERLAY------------------

        mPlaypause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG, "onClick: playpause");
                if (currentbutton == "pause"){
                    mPlaypause.setImageResource(R.drawable.pause);
                    currentbutton = "play";
                    Log.d(TAG, "onClick: music was played");
                    doMusicPlayerAction(currentbutton);
                }
                else if (currentbutton == "play"){
                    mPlaypause.setImageResource(R.drawable.play);
                    currentbutton = "pause";
                    Log.d(TAG, "onClick: music was paused");
                    doMusicPlayerAction(currentbutton);
                }

            }
        });


        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //do smthn when seekbar is changed
                if (fromUser){
                    mSetProgress(progress);
                    mMusicPlayer.mSeekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (currentSong != null){
                    try{
                        Message message = new Message();
                        message.what = mMusicPlayer.getcurrentpos();
                        handler.sendMessage(message);
                        Thread.sleep(1000);
                    }
                    catch (InterruptedException e){
                        e.printStackTrace();
                    }

                }
            }
        }).start();

        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playNextSong();
                mPlaypause.setImageResource(R.drawable.pause);

            }
        });

        mPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playPreviousSong();
                mPlaypause.setImageResource(R.drawable.pause);
            }
        });

        //Go back button
        mArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Animations
                main.setVisibility(View.INVISIBLE);
                main.animate()
                        .translationY(overlay.getHeight())
                        .alpha(0.0f)
                        .setDuration(0)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                overlay.animate()
                                        .translationY(overlay.getHeight())
                                        .alpha(0.0f)
                                        .setDuration(400)
                                        .setListener(new AnimatorListenerAdapter() {
                                            @Override
                                            public void onAnimationEnd(Animator animation) {
                                                super.onAnimationEnd(animation);
                                                overlay.setVisibility(View.GONE);
                                                main.setVisibility(View.VISIBLE);
                                                main.animate()
                                                        .translationY(0)
                                                        .alpha(1.0f)
                                                        .setDuration(400)
                                                        .setListener(new AnimatorListenerAdapter() {
                                                            @Override
                                                            public void onAnimationEnd(Animator animation) {
                                                                super.onAnimationEnd(animation);

                                                            }
                                                        });

                                            }
                                        });
                            }
                        });


                //resets pause and play buttons
                if (currentbutton == "pause"){
                    mPlayPause.setImageResource(android.R.drawable.ic_media_play);

                }
                else if (currentbutton == "play"){
                    mPlayPause.setImageResource(android.R.drawable.ic_media_pause);

                }


            }
        });



    }

    //Handler for seekbar
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage (Message msg){
            mSetProgress(msg.what);
        }
    };


    //sets the song names, images and artists
    private void initImageBitmaps(){
        Log.d(TAG, "initImageBitmaps: preparing bitmaps");

        //Song 0
        mImages.add("https://imgur.com/uQAk85E.jpg");
        mNames.add("Why We Lose");
        mArtists.add("Cartoon");

        //song 1
        mImages.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS11zW0ocR9xj4US9Gro6vUafH8v6ZOKDa6OA&usqp=CAU");
        mNames.add("Energy");
        mArtists.add("Elektronomia");

        //song 2
        mImages.add("https://media-cdn.tripadvisor.com/media/photo-s/03/8a/e5/f3/the-ritz-carlton-dubai.jpg");
        mNames.add("Limitless");
        mArtists.add("Elektronomia");

        //song 3
        mImages.add("https://i.pinimg.com/originals/be/55/9f/be559feb5737bdac23bc2a702546dee3.jpg");
        mNames.add("Faded");
        mArtists.add("Alan Walker");

        //song 4
        mImages.add("https://cdn.hipwallpaper.com/i/84/74/hWNVgT.jpg");
        mNames.add("Blank");
        mArtists.add("Disfigure");

        //song 5
        mImages.add("https://images.unsplash.com/photo-1573922590302-53ab2b651641?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&w=1000&q=80");
        mNames.add("Invincible");
        mArtists.add("DEAF KEV");

        //song 6
        mImages.add("https://images.pexels.com/photos/2835562/pexels-photo-2835562.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500");
        mNames.add("Spectre");
        mArtists.add("Alan Walker");

        //song 7
        mImages.add("https://imgur.com/pIozSDv.jpg");
        mNames.add("Sky High");
        mArtists.add("Elektronomia");

        //song 8
        mImages.add("https://imgur.com/Yy4So6a.jpg");
        mNames.add("Symbolism");
        mArtists.add("Electro-Light");

        //song 9
        mImages.add("https://i.imgur.com/awrYaw2.jpg");
        mNames.add("Feel Like Horrible");
        mArtists.add("Different Heaven & Sian Area");

        //song 10
        mImages.add("https://i.imgur.com/KCb1Jdb.jpeg");
        mNames.add("Can't Wait");
        mArtists.add("Jim Yosef");



        initRecyclerView();
    }

    //Calls the recyclerviewadapter
    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView: init recyclerview");
        RecyclerView recyclerView = findViewById(R.id.songlist);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(mNames, mArtists, mImages, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }

    //Sets the current song and plays it
    public void setCurrentSong(String songName, String author) {

        mSetProgress(0); //resets the seekbar

        ToolBar = findViewById(R.id.toolbar);
        if (ToolBar.getVisibility() == View.VISIBLE){

        }
        else if (ToolBar.getVisibility() == View.GONE){
            ToolBar.setVisibility(View.INVISIBLE);
            ToolBar.animate()
                    .translationY(ToolBar.getHeight())
                    .alpha(0.0f)
                    .setDuration(0)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            ToolBar.setVisibility(View.VISIBLE);
                            ToolBar.animate()
                                    .translationY(0)
                                    .alpha(1.0f)
                                    .setDuration(400)
                                    .setListener(new AnimatorListenerAdapter() {
                                        @Override
                                        public void onAnimationEnd(Animator animation) {
                                            super.onAnimationEnd(animation);

                                        }
                                    });
                        }
                    });
        }

        ToolBar.setVisibility(View.VISIBLE);
        currentSong = songName;
        mMusicPlayer.musica(currentSong, this);
        setSongTitle(currentSong, author);
        currentbutton = "play";
        mPlayPause.setImageResource(android.R.drawable.ic_media_pause);


        setToolBar(currentSong, author);
    }

    //Sets the images
    public void setCurrent(String imagename) {

        ImageView img2 = (ImageView) findViewById(R.id.imageView2);

        currentImage = imagename;


        Glide.with(this)
                .load(currentImage)
                .into(img2);
    }

    //Sets the position of the array
    public void setPosition(int position){
        mPosition = position;
    }

    //Changes the textviews to match the song
    public void setSongTitle(String song, String author){
        songnameoverlay = findViewById(R.id.songnameoverlay);
        songnameoverlay.setText(song);
        songartistoverlay =findViewById(R.id.artistoverlay);
        songartistoverlay.setText(author);
    }


    //Sets duration of the seekbar
    public void setDuration(int duration){
        mDuration = duration;
        mSeekBar.setMax(mDuration);

        //Converts the milis into minutes and seconds
        long minutes = (mDuration / 1000)  / 60;
        int seconds = (int)((mDuration / 1000) % 60);

        String mDur2 = "";

        if (seconds < 10){
            mDur2 = String.valueOf("0"+seconds);
        }
        else{
            mDur2 = String.valueOf(seconds);
        }

        String mDur = String.valueOf(minutes);

        durationtext.setText(mDur + ":"+mDur2);
    }

    public void mSetProgress(int progress){
        mSeekBar.setProgress(progress);

        long minutes = (progress / 1000)  / 60;
        int seconds = (int)((progress / 1000) % 60);

        String mprog2 ="";

        if (seconds < 10){
            mprog2 = String.valueOf("0"+seconds);
        }
        else{
            mprog2 = String.valueOf(seconds);
        }

        String mprog = String.valueOf(minutes);
        progresstext.setText(mprog + ":" + mprog2);
    }

    //Sets the toolbar
    public void setToolBar(String song, String author){
        songname = findViewById(R.id.name);
        songartist = findViewById(R.id.author);



        songname.animate()
                .alpha(0.0f)
                .setDuration(150)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        songname.setText(song);
                        songname.animate()
                                .alpha(1.0f)
                                .setDuration(150)
                                .setListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        super.onAnimationEnd(animation);

                                    }
                                });
                    }
                });

        songartist.animate()
                .alpha(0.0f)
                .setDuration(150)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        songartist.setText(author);
                        songartist.animate()
                                .alpha(1.0f)
                                .setDuration(150)
                                .setListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        super.onAnimationEnd(animation);

                                    }
                                });
                    }
                });


    }

    //Calls musicplayer on button press
    public void doMusicPlayerAction(String action) {
        mMusicPlayer.musicbuttons(action);
    }


    //Play next song
    public void playNextSong(){
        if (mPosition == 10 ){
            mPosition = 0;
        }
        else{
            mPosition = mPosition + 1;
        }

        setCurrentSong(mNames.get(mPosition), mArtists.get(mPosition));
        setCurrent(mImages.get(mPosition));
        Glide.with(context)
                .load(currentImage)
                .into(mCoverimage);
    }


    //Play previous song
    public void playPreviousSong(){
        if (mPosition == 0){
            mPosition =10;
        }
        else{
            mPosition = mPosition -1;
        }

        setCurrentSong(mNames.get(mPosition), mArtists.get(mPosition));
        setCurrent(mImages.get(mPosition));
        Glide.with(context)
                .load(currentImage)
                .into(mCoverimage);
    }

}