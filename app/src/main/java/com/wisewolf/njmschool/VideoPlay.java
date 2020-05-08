package com.wisewolf.njmschool;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.Arrays;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.vimeo.networking.model.Video;

import java.util.ArrayList;

import static com.wisewolf.njmschool.GlobalData.class12;

public class VideoPlay extends AppCompatActivity {
    RecyclerView subj_list, video_play_list;
    String StudentClass="12",studentdiv="S";
    ImageView download,fullscreen;
     VideoView videoView;
     Spinner lesson_selectSpinner;
     ArrayList nowShowing=new ArrayList();

    String lessn[] = {"Lesson 1","Lesson 2","Lesson 3","Lesson 4","Lesson 5", "Lesson 6","Lesson 7"};
    String lessn_code[] = {"L1","L2","L3","L4","L5", "L6","L7"};
    String lesson_flag="ALL";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_listing);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();

        subj_list = findViewById(R.id.subject_list);
        video_play_list = findViewById(R.id.video_play_list);
        fullscreen = findViewById(R.id.fullscreen);
        download = findViewById(R.id.download);
        download.setVisibility(View.INVISIBLE);
        fullscreen.setVisibility(View.INVISIBLE);
        videoView = (VideoView) findViewById(R.id.videoView);
        lesson_selectSpinner = findViewById(R.id.lesson_select);
        lessonSpinnerLoad();

        fullscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        intent();
        subjectAdapter();

        videoListAdapter();
        media("android.resource://" + getPackageName() + "/" + R.raw.v1);

        lesson_selectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Toast.makeText(VideoPlay.this,lessn[position] +" is showing", Toast.LENGTH_SHORT).show();
                lesson_flag=lessn_code[position];
                subj_wise_videoListAdapter(nowShowing);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });

    }

    private void lessonSpinnerLoad() {
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, lessn);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        lesson_selectSpinner.setAdapter(spinnerArrayAdapter);
    }

    private void intent() {
        Intent intent=getIntent();
        StudentClass=intent.getStringExtra("class");
        //TODO
        StudentClass="12";
    }
    private void subjectAdapter() {
        SubjectAdapter subjectAdapter = null;

        if (StudentClass.equals("1")) {
            subjectAdapter = new SubjectAdapter(SubjectList.class2, subj_list, new SubjectAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(String s) {

                    getVideosList(s);
                    Toast.makeText(VideoPlay.this, s, Toast.LENGTH_SHORT).show();
                }
            });
        }
        if (StudentClass.equals("2")) {
            subjectAdapter = new SubjectAdapter(SubjectList.class2, subj_list, new SubjectAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(String s) {
                    getVideosList(s);
                }
            });
        }
        if (StudentClass.equals("3")) {
            subjectAdapter = new SubjectAdapter(SubjectList.class3, subj_list, new SubjectAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(String s) {
                    getVideosList(s);
                }
            });
        }
        if (StudentClass.equals("4")) {
            subjectAdapter = new SubjectAdapter(SubjectList.class4, subj_list, new SubjectAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(String s) {
                    getVideosList(s);
                }
            });
        }
        if (StudentClass.equals("5")) {
            subjectAdapter = new SubjectAdapter(SubjectList.class5, subj_list, new SubjectAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(String s) {
                    getVideosList(s);
                }
            });
        }
        if (StudentClass.equals("6")) {
            subjectAdapter = new SubjectAdapter(SubjectList.class6, subj_list, new SubjectAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(String s) {
                    getVideosList(s);
                }
            });
        }
        if (StudentClass.equals("7")) {
            subjectAdapter = new SubjectAdapter(SubjectList.class7, subj_list, new SubjectAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(String s) {
                    getVideosList(s);
                }
            });
        }
        if (StudentClass.equals("8")) {
            subjectAdapter = new SubjectAdapter(SubjectList.class8, subj_list, new SubjectAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(String s) {
                    getVideosList(s);
                }
            });
        }
        if (StudentClass.equals("9")) {
            subjectAdapter = new SubjectAdapter(SubjectList.class9, subj_list, new SubjectAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(String s) {
                    getVideosList(s);
                }
            });
        }
        if (StudentClass.equals("10")) {
            subjectAdapter = new SubjectAdapter(SubjectList.class10, subj_list, new SubjectAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(String s) {
                    getVideosList(s);
                }
            });
        }
        if (StudentClass.equals("11")) {
            subjectAdapter = new SubjectAdapter(SubjectList.class1, subj_list, new SubjectAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(String s) {
                    getVideosList(s);
                }
            });
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

            subjectAdapter = new SubjectAdapter(list, subj_list, new SubjectAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(String s) {
                    getVideosList(s);
                }
            });
        }

        if (StudentClass.equals("NRL")) {
            subjectAdapter = new SubjectAdapter(SubjectList.nrl, subj_list, new SubjectAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(String s) {
                    getVideosList(s);
                }
            });
        }

        if (StudentClass.equals("LKG")) {
            subjectAdapter = new SubjectAdapter(SubjectList.lkg, subj_list, new SubjectAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(String s) {
                    getVideosList(s);
                }
            });
        }

        if (StudentClass.equals("UKG")) {
            subjectAdapter = new SubjectAdapter(SubjectList.ukg, subj_list, new SubjectAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(String s) {
                    getVideosList(s);
                }
            });
        }



        subj_list.setAdapter(subjectAdapter);
        LinearLayoutManager subj_manager
            = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        subj_list.setLayoutManager(subj_manager);

    }
    private void videoListAdapter() {
        nowShowing=GlobalData.addedVideos;

        video_play_list.setAdapter(new Class_videoAdapter(GlobalData.addedVideos, video_play_list, new Class_videoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Video item) {
                Toast.makeText(VideoPlay.this, "playing -" + item.name, Toast.LENGTH_SHORT).show();
                media(item.files.get(0).link);
                download.setVisibility(View.VISIBLE);
                fullscreen.setVisibility(View.VISIBLE);
                Glide.with(VideoPlay.this).asGif().load(R.raw.download).into(download);

            }


        }));
        LinearLayoutManager added_liste_adapterlayoutManager
            = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        video_play_list.setLayoutManager(added_liste_adapterlayoutManager);

    }
    private void subj_wise_videoListAdapter(ArrayList selectedVideoList) {
        nowShowing=selectedVideoList;
        selectedVideoList= getLesson(selectedVideoList);

        video_play_list.setAdapter(new Class_videoAdapter(selectedVideoList, video_play_list, new Class_videoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Video item) {
                Toast.makeText(VideoPlay.this, "playing -" + item.name, Toast.LENGTH_SHORT).show();
                media(item.files.get(0).link);
                download.setVisibility(View.VISIBLE);
                fullscreen.setVisibility(View.VISIBLE);
                Glide.with(VideoPlay.this).asGif().load(R.raw.download).into(download);

            }


        }));
        LinearLayoutManager added_liste_adapterlayoutManager
            = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        video_play_list.setLayoutManager(added_liste_adapterlayoutManager);

    }

    private ArrayList getLesson(ArrayList selectedVideoList) {
        ArrayList returnVideo=new ArrayList();
        String lesn="";
        if (StudentClass.equals("12")||StudentClass.equals("11")||StudentClass.equals("10")||StudentClass.equals("NRL")){
            for (int i = 0; i < selectedVideoList.size(); i++) {
                Video video = (Video) selectedVideoList.get(i);
                lesn = (video.name.substring(3));
                lesn = lesn.substring(0, Math.min(lesn.length(), 2));
                if (lesn.equals(lesson_flag)) {
                    returnVideo.add(selectedVideoList.get(i));
                }

            }

        }
        else {
            for (int i = 0; i < selectedVideoList.size(); i++) {
                Video video = (Video) selectedVideoList.get(i);
                lesn = (video.name.substring(2));
                lesn = lesn.substring(0, Math.min(lesn.length(), 2));
                if (lesn.equals(lesson_flag)) {
                    returnVideo.add(selectedVideoList.get(i));
                }

            }

        }
        return  returnVideo;
    }


    private void getVideosList(String s) {
        switch(StudentClass) {
            case "NRL":
                classNRL(s);
                break;
            case "LKG":
                classLKG(s);
                break;
            case "UKG":
                classUKG(s);
                break;
            case "12":
               class12(s);
                break;
            case "11":
                class11(s);
                break;
            case "10":
                class10(s);
                break;
            case "9":
                class9(s);
                break;
            case "8":
                class8(s);
                break;
            case "7":
                class7(s);
                break;
            case "6":
                class6(s);
                break;
            case "5":
                class5(s);
                break;
            case "4":
                class4(s);
                break;
            case "3":
                class3(s);
                break;
            case "2":
                class2(s);
                break;
            case "1":
                class1(s);
                break;

            default:
                // code block
        }


    }

    private void class12(String subject) {
        String subCode = "";
        if (studentdiv.equals("S")) {
            for (int i = 0; i < SubjectList.class12_SCI.length; i++) {
                if (SubjectList.class12_SCI[i].equals(subject)) {
                    subCode = SubjectList.class12_SCI_code[i];
                }

            }
            ArrayList subject_videoList = new ArrayList();
            ArrayList videoList = new ArrayList();
            videoList = GlobalData.addedVideos;
            String subj = "";
            for (int i = 0; i < videoList.size(); i++) {
                Video video = (Video) videoList.get(i);
                subj = (video.name.substring(5));
                subj = subj.substring(0, Math.min(subj.length(), 3));
                if (subj.equals(subCode)) {
                    subject_videoList.add(videoList.get(i));
                }

            }
            subj_wise_videoListAdapter(subject_videoList);



        }
        else if (studentdiv.equals("H")) {
            for (int i = 0; i < SubjectList.class12_HUM.length; i++) {
                if (SubjectList.class12_HUM[i].equals(subject)) {
                    subCode = SubjectList.class12_HUM_code[i];
                }

            }
            ArrayList subject_videoList = new ArrayList();
            ArrayList videoList = new ArrayList();
            videoList = GlobalData.addedVideos;
            String subj = "";
            for (int i = 0; i < videoList.size(); i++) {
                Video video = (Video) videoList.get(i);
                subj = (video.name.substring(5));
                subj = subj.substring(0, Math.min(subj.length(), 3));
                if (subj.equals(subCode)) {
                    subject_videoList.add(videoList.get(i));
                }

            }
            subj_wise_videoListAdapter(subject_videoList);



        }
        else{
            for (int i = 0; i < SubjectList.class12_COMR.length; i++) {
                if (SubjectList.class12_COMR[i].equals(subject)) {
                    subCode = SubjectList.class12_COMR_code[i];
                }

            }
            ArrayList subject_videoList = new ArrayList();
            ArrayList videoList = new ArrayList();
            videoList = GlobalData.addedVideos;
            String subj = "";
            for (int i = 0; i < videoList.size(); i++) {
                Video video = (Video) videoList.get(i);
                subj = (video.name.substring(5));
                subj = subj.substring(0, Math.min(subj.length(), 3));
                if (subj.equals(subCode)) {
                    subject_videoList.add(videoList.get(i));
                }

            }
            subj_wise_videoListAdapter(subject_videoList);



        }
    }
    private void class11(String subject) {
    }
    private void class10(String subject) {
        String subCode = "";

            for (int i = 0; i < SubjectList.class10.length; i++) {
                if (SubjectList.class10[i].equals(subject)) {
                    subCode = SubjectList.class10_code[i];
                }

            }
            ArrayList subject_videoList = new ArrayList();
            ArrayList videoList = new ArrayList();
            videoList = GlobalData.addedVideos;
            String subj = "";
            for (int i = 0; i < videoList.size(); i++) {
                Video video = (Video) videoList.get(i);
                subj = (video.name.substring(5));
                subj = subj.substring(0, Math.min(subj.length(), 3));
                if (subj.equals(subCode)) {
                    subject_videoList.add(videoList.get(i));
                }

            }
            subj_wise_videoListAdapter(subject_videoList);
    }
    private void classNRL(String subject) {
        String subCode = "";

        for (int i = 0; i < SubjectList.nrl.length; i++) {
            if (SubjectList.nrl[i].equals(subject)) {
                subCode = SubjectList.nrl_code[i];
            }

        }
        ArrayList subject_videoList = new ArrayList();
        ArrayList videoList = new ArrayList();
        videoList = GlobalData.addedVideos;
        String subj = "";
        for (int i = 0; i < videoList.size(); i++) {
            Video video = (Video) videoList.get(i);
            subj = (video.name.substring(5));
            subj = subj.substring(0, Math.min(subj.length(), 3));
            if (subj.equals(subCode)) {
                subject_videoList.add(videoList.get(i));
            }

        }
        subj_wise_videoListAdapter(subject_videoList);
    }


    private void class9(String subject) {
        String subCode = "";

        for (int i = 0; i < SubjectList.class9.length; i++) {
            if (SubjectList.class9[i].equals(subject)) {
                subCode = SubjectList.class9_code[i];
            }

        }
        ArrayList subject_videoList = new ArrayList();
        ArrayList videoList = new ArrayList();
        videoList = GlobalData.addedVideos;
        String subj = "";
        for (int i = 0; i < videoList.size(); i++) {
            Video video = (Video) videoList.get(i);
            subj = (video.name.substring(4));
            subj = subj.substring(0, Math.min(subj.length(), 3));
            if (subj.equals(subCode)) {
                subject_videoList.add(videoList.get(i));
            }

        }
        subj_wise_videoListAdapter(subject_videoList);
    }


    private void class8(String subject) {
        String subCode = "";

        for (int i = 0; i < SubjectList.class8.length; i++) {
            if (SubjectList.class8[i].equals(subject)) {
                subCode = SubjectList.class8_code[i];
            }

        }
        ArrayList subject_videoList = new ArrayList();
        ArrayList videoList = new ArrayList();
        videoList = GlobalData.addedVideos;
        String subj = "";
        for (int i = 0; i < videoList.size(); i++) {
            Video video = (Video) videoList.get(i);
            subj = (video.name.substring(4));
            subj = subj.substring(0, Math.min(subj.length(), 3));
            if (subj.equals(subCode)) {
                subject_videoList.add(videoList.get(i));
            }

        }
        subj_wise_videoListAdapter(subject_videoList);
    }
    private void class7(String subject) {
        String subCode = "";

        for (int i = 0; i < SubjectList.class7.length; i++) {
            if (SubjectList.class7[i].equals(subject)) {
                subCode = SubjectList.class7_code[i];
            }

        }
        ArrayList subject_videoList = new ArrayList();
        ArrayList videoList = new ArrayList();
        videoList = GlobalData.addedVideos;
        String subj = "";
        for (int i = 0; i < videoList.size(); i++) {
            Video video = (Video) videoList.get(i);
            subj = (video.name.substring(4));
            subj = subj.substring(0, Math.min(subj.length(), 3));
            if (subj.equals(subCode)) {
                subject_videoList.add(videoList.get(i));
            }

        }
        subj_wise_videoListAdapter(subject_videoList);
    }
    private void class6(String subject) {
        String subCode = "";

        for (int i = 0; i < SubjectList.class6.length; i++) {
            if (SubjectList.class6[i].equals(subject)) {
                subCode = SubjectList.class6_code[i];
            }

        }
        ArrayList subject_videoList = new ArrayList();
        ArrayList videoList = new ArrayList();
        videoList = GlobalData.addedVideos;
        String subj = "";
        for (int i = 0; i < videoList.size(); i++) {
            Video video = (Video) videoList.get(i);
            subj = (video.name.substring(4));
            subj = subj.substring(0, Math.min(subj.length(), 3));
            if (subj.equals(subCode)) {
                subject_videoList.add(videoList.get(i));
            }

        }
        subj_wise_videoListAdapter(subject_videoList);
    }


    private void class5(String subject)  {
        String subCode = "";

        for (int i = 0; i < SubjectList.class5.length; i++) {
            if (SubjectList.class5[i].equals(subject)) {
                subCode = SubjectList.class5_code[i];
            }

        }
        ArrayList subject_videoList = new ArrayList();
        ArrayList videoList = new ArrayList();
        videoList = GlobalData.addedVideos;
        String subj = "";
        for (int i = 0; i < videoList.size(); i++) {
            Video video = (Video) videoList.get(i);
            subj = (video.name.substring(4));
            subj = subj.substring(0, Math.min(subj.length(), 3));
            if (subj.equals(subCode)) {
                subject_videoList.add(videoList.get(i));
            }

        }
        subj_wise_videoListAdapter(subject_videoList);
    }
    private void class4(String subject)  {
        String subCode = "";

        for (int i = 0; i < SubjectList.class4.length; i++) {
            if (SubjectList.class4[i].equals(subject)) {
                subCode = SubjectList.class4_code[i];
            }

        }
        ArrayList subject_videoList = new ArrayList();
        ArrayList videoList = new ArrayList();
        videoList = GlobalData.addedVideos;
        String subj = "";
        for (int i = 0; i < videoList.size(); i++) {
            Video video = (Video) videoList.get(i);
            subj = (video.name.substring(4));
            subj = subj.substring(0, Math.min(subj.length(), 3));
            if (subj.equals(subCode)) {
                subject_videoList.add(videoList.get(i));
            }

        }
        subj_wise_videoListAdapter(subject_videoList);
    }
    private void class3(String subject)  {
        String subCode = "";

        for (int i = 0; i < SubjectList.class3.length; i++) {
            if (SubjectList.class3[i].equals(subject)) {
                subCode = SubjectList.class3_code[i];
            }

        }
        ArrayList subject_videoList = new ArrayList();
        ArrayList videoList = new ArrayList();
        videoList = GlobalData.addedVideos;
        String subj = "";
        for (int i = 0; i < videoList.size(); i++) {
            Video video = (Video) videoList.get(i);
            subj = (video.name.substring(4));
            subj = subj.substring(0, Math.min(subj.length(), 3));
            if (subj.equals(subCode)) {
                subject_videoList.add(videoList.get(i));
            }

        }
        subj_wise_videoListAdapter(subject_videoList);
    }


    private void class2(String subject) {
        String subCode = "";

        for (int i = 0; i < SubjectList.class2.length; i++) {
            if (SubjectList.class2[i].equals(subject)) {
                subCode = SubjectList.class2_code[i];
            }

        }
        ArrayList subject_videoList = new ArrayList();
        ArrayList videoList = new ArrayList();
        videoList = GlobalData.addedVideos;
        String subj = "";
        for (int i = 0; i < videoList.size(); i++) {
            Video video = (Video) videoList.get(i);
            subj = (video.name.substring(4));
            subj = subj.substring(0, Math.min(subj.length(), 3));
            if (subj.equals(subCode)) {
                subject_videoList.add(videoList.get(i));
            }

        }
        subj_wise_videoListAdapter(subject_videoList);
    }
    private void class1(String subject) {
        String subCode = "";

        for (int i = 0; i < SubjectList.class1.length; i++) {
            if (SubjectList.class1[i].equals(subject)) {
                subCode = SubjectList.class1_code[i];
            }

        }
        ArrayList subject_videoList = new ArrayList();
        ArrayList videoList = new ArrayList();
        videoList = GlobalData.addedVideos;
        String subj = "";
        for (int i = 0; i < videoList.size(); i++) {
            Video video = (Video) videoList.get(i);
            subj = (video.name.substring(4));
            subj = subj.substring(0, Math.min(subj.length(), 3));
            if (subj.equals(subCode)) {
                subject_videoList.add(videoList.get(i));
            }

        }
        subj_wise_videoListAdapter(subject_videoList);
    }
    private void classLKG(String subject) {
        String subCode = "";

        for (int i = 0; i < SubjectList.lkg.length; i++) {
            if (SubjectList.lkg[i].equals(subject)) {
                subCode = SubjectList.lkg_code[i];
            }

        }
        ArrayList subject_videoList = new ArrayList();
        ArrayList videoList = new ArrayList();
        videoList = GlobalData.addedVideos;
        String subj = "";
        for (int i = 0; i < videoList.size(); i++) {
            Video video = (Video) videoList.get(i);
            subj = (video.name.substring(4));
            subj = subj.substring(0, Math.min(subj.length(), 3));
            if (subj.equals(subCode)) {
                subject_videoList.add(videoList.get(i));
            }

        }
        subj_wise_videoListAdapter(subject_videoList);
    }
    private void classUKG(String subject) {
        String subCode = "";

        for (int i = 0; i < SubjectList.ukg.length; i++) {
            if (SubjectList.ukg[i].equals(subject)) {
                subCode = SubjectList.ukg_code[i];
            }

        }
        ArrayList subject_videoList = new ArrayList();
        ArrayList videoList = new ArrayList();
        videoList = GlobalData.addedVideos;
        String subj = "";
        for (int i = 0; i < videoList.size(); i++) {
            Video video = (Video) videoList.get(i);
            subj = (video.name.substring(4));
            subj = subj.substring(0, Math.min(subj.length(), 3));
            if (subj.equals(subCode)) {
                subject_videoList.add(videoList.get(i));
            }

        }
        subj_wise_videoListAdapter(subject_videoList);
    }

    void media(String videoUrl) {

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
