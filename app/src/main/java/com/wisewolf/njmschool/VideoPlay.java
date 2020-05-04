package com.wisewolf.njmschool;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Arrays;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.vimeo.networking.model.Video;

import java.util.ArrayList;

public class VideoPlay extends AppCompatActivity {
    RecyclerView subj_list, video_play_list;
    String StudentClass="12",studentdiv="S";
    ImageView download;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_listing);
        subj_list = findViewById(R.id.subject_list);
        video_play_list = findViewById(R.id.video_play_list);
        download=findViewById(R.id.download);
        intent();
        subjectAdapter();



        videoListAdapter();
        media("android.resource://" + getPackageName() + "/" + R.raw.v1);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;


    }

    private void intent() {
        Intent intent=getIntent();
        StudentClass=intent.getStringExtra("class");
    }

    private void videoListAdapter() {
       /* Class_videoAdapter class_videoAdapter = new
            Class_videoAdapter(GlobalData.allVideoList, video_play_list);
        video_play_list.setAdapter(class_videoAdapter);*/


        video_play_list.setAdapter(new Class_videoAdapter(GlobalData.allVideoList, video_play_list,new Class_videoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Video item) {
                Toast.makeText(VideoPlay.this, "playing -"+item.name, Toast.LENGTH_SHORT).show();
                media(item.files.get(0).link);
                download.setVisibility(View.VISIBLE);
                Glide.with(VideoPlay.this).asGif().load(R.raw.download).into(download);

            }


        }));
        LinearLayoutManager added_liste_adapterlayoutManager
            = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        video_play_list.setLayoutManager(added_liste_adapterlayoutManager);

    }

    private void subjectAdapter() {
        SubjectAdapter subjectAdapter = null;

        if (StudentClass.equals("1")) {
            subjectAdapter = new SubjectAdapter(SubjectList.class2, subj_list);
        }
        if (StudentClass.equals("2")) {
            subjectAdapter = new SubjectAdapter(SubjectList.class2, subj_list);
        }
        if (StudentClass.equals("3")) {
            subjectAdapter = new SubjectAdapter(SubjectList.class2, subj_list);
        }
        if (StudentClass.equals("4")) {
            subjectAdapter = new SubjectAdapter(SubjectList.class2, subj_list);
        }
        if (StudentClass.equals("5")) {
            subjectAdapter = new SubjectAdapter(SubjectList.class2, subj_list);
        }
        if (StudentClass.equals("6")) {
            subjectAdapter = new SubjectAdapter(SubjectList.class2, subj_list);
        }
        if (StudentClass.equals("7")) {
            subjectAdapter = new SubjectAdapter(SubjectList.class2, subj_list);
        }
        if (StudentClass.equals("8")) {
            subjectAdapter = new SubjectAdapter(SubjectList.class2, subj_list);
        }
        if (StudentClass.equals("9")) {
            subjectAdapter = new SubjectAdapter(SubjectList.class2, subj_list);
        }
        if (StudentClass.equals("10")) {
            subjectAdapter = new SubjectAdapter(SubjectList.class2, subj_list);
        }
        if (StudentClass.equals("11")) {
            subjectAdapter = new SubjectAdapter(SubjectList.class2, subj_list);
        }
        if (StudentClass.equals("12")) {
            String[] list;
            if (studentdiv.equals("C")) {

                list = SubjectList.class12_COMR;
            } else if (studentdiv.equals("H")) {
                list = SubjectList.class12_HUM;
            } else if (studentdiv.equals("S")) {
                list = SubjectList.class12_SCI;
            } else {
                list = SubjectList.class12_COMM;
            }

            subjectAdapter = new SubjectAdapter(list, subj_list);
        }

        subj_list.setAdapter(subjectAdapter);
        LinearLayoutManager subj_manager
            = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        subj_list.setLayoutManager(subj_manager);

    }

    void media(String videoUrl) {
        final VideoView videoView = (VideoView) findViewById(R.id.videoView);
        //Use a media controller so that you can scroll the video contents
        //and also to pause, start the video.
        final MediaController mediaController = new MediaController(this);
        //  mediaController.setAnchorView(videoView);

        videoView.setMediaController(mediaController);
        videoView.setVideoURI(Uri.parse(videoUrl));
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaController.setAnchorView(videoView);
                videoView.start();
            }
        });
    }
}
