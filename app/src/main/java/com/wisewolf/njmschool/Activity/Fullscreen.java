package com.wisewolf.njmschool.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();

        videoFull=findViewById(R.id.videoFull);
        Intent intent=getIntent();
        String vURL=intent.getStringExtra("url");
        media(vURL);

    }

    void media(String videoUrl) {





        //Use a media controller so that you can scroll the video contents
        //and also to pause, start the video.
        final MediaController mediaController = new MediaController(this);

        //  mediaController.setAnchorView(videoView);

        videoFull.setMediaController(mediaController);
        videoFull.setVideoURI(Uri.parse(videoUrl));
        videoFull.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaController.setAnchorView(videoFull);
                videoFull.start();
            }
        });

    }
}
