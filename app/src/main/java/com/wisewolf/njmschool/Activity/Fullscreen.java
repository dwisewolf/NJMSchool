package com.wisewolf.njmschool.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;

import com.wisewolf.njmschool.R;

public class Fullscreen extends AppCompatActivity {
    VideoView videoFull;
    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();

        videoFull=findViewById(R.id.videoFull);
        Intent intent=getIntent();
        int current=Integer.valueOf(intent.getStringExtra("current"));
        String vURL=intent.getStringExtra("url");
        media(vURL,current);

    }

    void media(String videoUrl, final int current) {
        mProgressDialog = new ProgressDialog(Fullscreen.this);
        mProgressDialog.setMessage("Internet Slow ! Please wait . . .");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();



        //Use a media controller so that you can scroll the video contents
        //and also to pause, start the video.
        final MediaController mediaController = new MediaController(this);

        //  mediaController.setAnchorView(videoView);

        videoFull.setMediaController(mediaController);
        videoFull.setVideoURI(Uri.parse(videoUrl));
        videoFull.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mProgressDialog.cancel();
                mediaController.setAnchorView(videoFull);
                videoFull.start();
                videoFull.seekTo(current);
            }
        });

    }
}
