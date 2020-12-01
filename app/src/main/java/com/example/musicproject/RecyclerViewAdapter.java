package com.example.musicproject;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.GenericLifecycleObserver;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{
    private static final String TAG = "RecyclerViewAdapter";
    String currentsong = "";
    String author = "";
    private ArrayList<String> mSongnames = new ArrayList<>();
    private ArrayList<String> mSongartists = new ArrayList<>();
    private ArrayList<String> mSongimages = new ArrayList<>();
    private Context mContext;

    private MediaPlayer mMediaplayer;
    private Button mPlay, mPause;

    public RecyclerViewAdapter(ArrayList<String> lsongname, ArrayList<String> lsongartist, ArrayList<String> lsongimage, Context context){
        mSongnames = lsongname;
        mSongartists = lsongartist;
        mSongimages = lsongimage;

        mContext = context;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_song, parent, false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Log.d(TAG, "onBindViewHolder: called.");

        Glide.with(mContext)
                .asBitmap()
                .load(mSongimages.get(position))
                .into(holder.songimage);



        holder.songtitle.setText(mSongnames.get(position));
        holder.songartist.setText(mSongartists.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked on: " + mSongnames.get(position));
                currentsong = mSongnames.get(position);
                author = mSongartists.get(position);
                ((MainActivity)mContext).setCurrentSong(currentsong, author);
                ((MainActivity)mContext).setCurrent(mSongimages.get(position));
                ((MainActivity)mContext).setPosition(position);
            }
        });

    }



    @Override
    public int getItemCount() {
        return mSongnames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView songimage;
        TextView songtitle;
        TextView songartist;
        ConstraintLayout parent_layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            songimage = itemView.findViewById(R.id.songimage);
            songtitle = itemView.findViewById(R.id.songtitle);
            songartist = itemView.findViewById(R.id.songartist);
            parent_layout = itemView.findViewById(R.id.parent_layout);

        }
    }


}
