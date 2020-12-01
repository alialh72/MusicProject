package com.example.musicproject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.GenericLifecycleObserver;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.parceler.Parcel;

import java.io.IOException;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;


//This class handles everything to do with MediaPlayer()

public class musicplayer{
    String currentmusic;
    Context context;
    String buttonpressed;
    MediaPlayer mMediaplayer = new MediaPlayer();


    public void musica(String musicsong, Context context){
        this.context = context;
        currentmusic = musicsong;

        //Checks the current song and sets the track

        int song = 0;
        if (musicsong == "Why We Lose"){
            Log.d(TAG, "musica: why we lose");
            song = R.raw.whywelose;

        }

        else if (musicsong == "Energy"){
            Log.d(TAG, "musica: energy was clicked");
            song = R.raw.energy;

        }

        else if (musicsong == "Limitless"){
            Log.d(TAG, "musica: limitless was clicked");
            song = R.raw.limitless;

        }

        else if (musicsong == "Faded"){
            Log.d(TAG, "musica: faded was clicked");
            song = R.raw.faded;


        }

        else if (musicsong == "Spectre"){
            Log.d(TAG, "musica: sky high was clicked");
            song = R.raw.spectre;

        }

        else if (musicsong == "Invincible"){
            Log.d(TAG, "musica: sky high was clicked");
            song = R.raw.invincible;

        }

        else if (musicsong == "Sky High"){
            Log.d(TAG, "musica: sky high was clicked");
            song = R.raw.skyhigh;

        }

        else if (musicsong == "Blank"){
            Log.d(TAG, "musica: sky high was clicked");
            song = R.raw.blank;

        }

        else if (musicsong == "Symbolism"){
            Log.d(TAG, "musica: symbolism was clicked");
            song = R.raw.symbolism;

        }

        else if (musicsong == "Feel Like Horrible"){
            Log.d(TAG, "musica: feel like horrible was clicked");
            song = R.raw.feellikehorrible;
        }

        else if (musicsong == "Can't Wait"){
            Log.d(TAG, "musica: cant wait was clicked");
            song = R.raw.cantwait;
        }

        //Checks if MediaPlayer is already playing
        if (mMediaplayer.isPlaying()){
            mMediaplayer.stop();
            mMediaplayer.reset();
            mMediaplayer.release();
            mMediaplayer = MediaPlayer.create(context, song);
        }

        else{
            if (mMediaplayer != null ){
                mMediaplayer.stop();
                mMediaplayer.reset();
                mMediaplayer.release();

            }
            mMediaplayer = MediaPlayer.create(context, song);

        }

        //Passes duration to MainActivity
        mMediaplayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                ((MainActivity)context).setDuration(mMediaplayer.getDuration());
            }
        });

        mMediaplayer.start();

        mMediaplayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                //do something when track is finished
                ((MainActivity)context).playNextSong();
            }
        });
    }

    //Passes the current position of MediaPlayer to MainActivity
    public int getcurrentpos(){
        int currentpos = 0;
        if (mMediaplayer != null){
            currentpos = mMediaplayer.getCurrentPosition();
        }
        return currentpos;
    }

    //Does smthn when pause or play are clicked
    public void musicbuttons(String buttonpressed){
        this.buttonpressed = buttonpressed;
        if (buttonpressed == "pause"){
            if (mMediaplayer.isPlaying()){
                mMediaplayer.pause();
            }
            else{
                Toast.makeText(context, "Play the song first", Toast.LENGTH_SHORT).show();
            }

            Log.d(TAG, "musicbuttons: pause button clicked");
        }
        else if(buttonpressed == "play"){
            Log.d(TAG, "musicbuttons: play");
            mMediaplayer.start();
        }


    }

    //Changes the progress of mediaplayer when the seekbar is used
    public void mSeekTo(int progress){
        mMediaplayer.seekTo(progress);
    }

    //Does smthn when shufflebutton is pressed
    public void shufflebutton(String currentsong, Context context, String author){
        currentmusic = currentsong;
        Log.d(TAG, "shufflebutton: current song is " + currentsong);

        ((MainActivity)context).setCurrentSong(currentsong, author);
    }


}
