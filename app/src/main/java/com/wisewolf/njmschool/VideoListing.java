package com.wisewolf.njmschool;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vimeo.networking.model.Video;
import com.vimeo.networking.model.VideoList;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class VideoListing extends AppCompatActivity {


    String StudentClass = "4";
    RecyclerView added_list;


    private ProgressDialog mProgressDialog;
    Video videoTest;
    ImageView topic;

    ArrayList allVideoList = new ArrayList();
    ArrayList allVideoList1 = new ArrayList();
    ArrayList allVideoList2 = new ArrayList();
    ArrayList allVideoList3 = new ArrayList();
    ArrayList allVideoList4 = new ArrayList();
    ArrayList allVideoList5 = new ArrayList();
    ArrayList allVideoList6 = new ArrayList();
    ArrayList allVideoList7 = new ArrayList();
    ArrayList allVideoList8 = new ArrayList();
    ArrayList allVideoList9 = new ArrayList();
    ArrayList allVideoList10 = new ArrayList();
    ArrayList allVideoList11 = new ArrayList();
    ArrayList allVideoList12 = new ArrayList();
    TextView no_of_videos;

    ArrayList addedLessons = new ArrayList();
    CardView mediaCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        topic = findViewById(R.id.profilepic);
        Glide.with(this).asGif().load(R.raw.profile).into(topic);
        no_of_videos = findViewById(R.id.no_of_files);
        mediaCard=findViewById(R.id.media_card);
        // media("android.resource://" + getPackageName() + "/" + R.raw.v1); tobe used in video play screen

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;


        allVideoList = GlobalData.allVideoList;
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(VideoListing.this);
        Gson gson = new Gson();
        String json = sharedPrefs.getString("Allvideolist", "");
        Type type = new TypeToken<List<Video>>() {
        }.getType();
        List<Video> arrayList = gson.fromJson(json, type);
        allVideoList = (ArrayList) arrayList;
        GlobalData.ReservallVideoList = allVideoList;
        try {
            allVideoList = (ArrayList<Video>) ObjectSerialiser.deserialize(sharedPrefs.getString("task", ObjectSerialiser.serialize(new ArrayList<Video>())));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        allVideoList = (ArrayList) arrayList;


        actionBar.hide();

        // media(videoList.data.get(1).files.get(0).link);


        for (int i = 0; i < allVideoList.size(); i++) {
            Video videoList = (Video) allVideoList.get(i);
            String x = videoList.name;

            String classname = x.substring(0, Math.min(x.length(), 3));
            if (classname.equals("C12")) {

                ArrayList aList = new ArrayList();
                if (GlobalData.class12 != null) {
                    aList = GlobalData.class12;
                }

                aList.add(videoList);

                GlobalData.class12 = (aList);

                // // allVideoList.remove(videoList);
            }

            if (classname.equals("C11")) {

                ArrayList aList = new ArrayList();
                if (GlobalData.class11 != null) {
                    aList = GlobalData.class11;
                }

                aList.add(videoList);

                GlobalData.class11 = (aList);

                //  // allVideoList.remove(videoList);
            }

            if (classname.equals("C10")) {

                ArrayList aList = new ArrayList();
                if (GlobalData.class10 != null) {
                    aList = GlobalData.class10;
                }

                aList.add(videoList);

                GlobalData.class10 = (aList);

                //  // allVideoList.remove(videoList);
            }


        }

        for (int i = 0; i < allVideoList.size(); i++) {
            Video videoList = (Video) allVideoList.get(i);
            String x = videoList.name;

            String classname = x.substring(0, Math.min(x.length(), 3));
            if (classname.equals("C1L")) {

                ArrayList aList = new ArrayList();
                if (GlobalData.class1 != null) {
                    aList = GlobalData.class1;
                }

                aList.add(videoList);

                GlobalData.class1 = (aList);

                //  // allVideoList.remove(videoList);
            }

            if (classname.equals("C2L")) {

                ArrayList aList = new ArrayList();
                if (GlobalData.class2 != null) {
                    aList = GlobalData.class2;
                }

                aList.add(videoList);

                GlobalData.class2 = (aList);

                //  // allVideoList.remove(videoList);
            }

            if (classname.equals("C3L")) {

                ArrayList aList = new ArrayList();
                if (GlobalData.class3 != null) {
                    aList = GlobalData.class3;
                }

                aList.add(videoList);

                GlobalData.class3 = (aList);

                // allVideoList.remove(videoList);
            }

            if (classname.equals("C4L")) {

                ArrayList aList = new ArrayList();
                if (GlobalData.class4 != null) {
                    aList = GlobalData.class4;
                }

                aList.add(videoList);

                GlobalData.class4 = (aList);

                // allVideoList.remove(videoList);
            }

            if (classname.equals("C5L")) {

                ArrayList aList = new ArrayList();
                if (GlobalData.class5 != null) {
                    aList = GlobalData.class5;
                }

                aList.add(videoList);

                GlobalData.class5 = (aList);

                // allVideoList.remove(videoList);
            }

            if (classname.equals("C6L")) {

                ArrayList aList = new ArrayList();
                if (GlobalData.class6 != null) {
                    aList = GlobalData.class6;
                }

                aList.add(videoList);

                GlobalData.class6 = (aList);

                // allVideoList.remove(videoList);
            }

            if (classname.equals("C7L")) {

                ArrayList aList = new ArrayList();
                if (GlobalData.class7 != null) {
                    aList = GlobalData.class7;
                }

                aList.add(videoList);

                GlobalData.class7 = (aList);

                // allVideoList.remove(videoList);
            }

            if (classname.equals("C8L")) {

                ArrayList aList = new ArrayList();
                if (GlobalData.class8 != null) {
                    aList = GlobalData.class8;
                }

                aList.add(videoList);

                GlobalData.class8 = (aList);

                // allVideoList.remove(videoList);
            }

            if (classname.equals("C9L")) {

                ArrayList aList = new ArrayList();
                if (GlobalData.class9 != null) {
                    aList = GlobalData.class9;
                }

                aList.add(videoList);

                GlobalData.class9 = (aList);

                // allVideoList.remove(videoList);
            }


        }

        allVideoList1 = GlobalData.class1;
        allVideoList2 = GlobalData.class2;
        allVideoList3 = GlobalData.class3;
        allVideoList4 = GlobalData.class4;
        allVideoList5 = GlobalData.class5;
        allVideoList6 = GlobalData.class6;
        allVideoList7 = GlobalData.class7;
        allVideoList8 = GlobalData.class8;
        allVideoList9 = GlobalData.class9;
        allVideoList10 = GlobalData.class10;
        allVideoList11 = GlobalData.class11;
        allVideoList12 = GlobalData.class12;


        try {
            if (StudentClass.equals("12")) {

                if (allVideoList12 != null) {

                    for (int i = 0; i < allVideoList12.size(); i++) {
                        Video videoList = (Video) allVideoList12.get(i);
                        String x = videoList.name;
                        x = x.substring(3);

                        String lesson = x.substring(0, Math.min(x.length(), 2));
                        if (lesson.equals("L1")) {

                            ArrayList aList = new ArrayList();
                            if (GlobalData.class12LS1 != null) {
                                aList = GlobalData.class12LS1;
                            }

                            aList.add(videoList);

                            GlobalData.class12LS1 = (aList);

                            //  allVideoList12.remove(videoList);
                        }

                        if (lesson.equals("L2")) {

                            ArrayList aList = new ArrayList();
                            if (GlobalData.class12LS2 != null) {
                                aList = GlobalData.class12LS2;
                            }

                            aList.add(videoList);

                            GlobalData.class12LS2 = (aList);

                            //  allVideoList12.remove(videoList);
                        }

                        if (lesson.equals("L3")) {

                            ArrayList aList = new ArrayList();
                            if (GlobalData.class12LS3 != null) {
                                aList = GlobalData.class12LS3;
                            }

                            aList.add(videoList);

                            GlobalData.class12LS3 = (aList);

                            // allVideoList12.remove(videoList);
                        }

                        if (lesson.equals("L4")) {

                            ArrayList aList = new ArrayList();
                            if (GlobalData.class12LS4 != null) {
                                aList = GlobalData.class12LS4;
                            }

                            aList.add(videoList);

                            GlobalData.class12LS4 = (aList);

                            //   allVideoList12.remove(videoList);
                        }

                        if (lesson.equals("L5")) {

                            ArrayList aList = new ArrayList();
                            if (GlobalData.class12LS5 != null) {
                                aList = GlobalData.class12LS5;
                            }

                            aList.add(videoList);

                            GlobalData.class12LS5 = (aList);

                            //allVideoList12.remove(videoList);
                        }

                        if (lesson.equals("L6")) {

                            ArrayList aList = new ArrayList();
                            if (GlobalData.class12LS6 != null) {
                                aList = GlobalData.class12LS6;
                            }

                            aList.add(videoList);

                            GlobalData.class12LS6 = (aList);

                            //  allVideoList12.remove(videoList);
                        }

                        if (lesson.equals("L7")) {

                            ArrayList aList = new ArrayList();
                            if (GlobalData.class12LS7 != null) {
                                aList = GlobalData.class12LS7;
                            }

                            aList.add(videoList);

                            GlobalData.class12LS7 = (aList);

                            //  allVideoList12.remove(videoList);
                        }


                    }
                }

            }


            if (StudentClass.equals("11")) {
                if (allVideoList11 != null) {
                    for (int i = 0; i < allVideoList11.size(); i++) {
                        Video videoList = (Video) allVideoList11.get(i);
                        String x = videoList.name;
                        x = x.substring(3);

                        String lesson = x.substring(0, Math.min(x.length(), 2));
                        if (lesson.equals("L1")) {

                            ArrayList aList = new ArrayList();
                            if (GlobalData.class11LS1 != null) {
                                aList = GlobalData.class11LS1;
                            }

                            aList.add(videoList);

                            GlobalData.class11LS1 = (aList);

                            //  allVideoList11.remove(videoList);
                        }

                        if (lesson.equals("L2")) {

                            ArrayList aList = new ArrayList();
                            if (GlobalData.class11LS2 != null) {
                                aList = GlobalData.class11LS2;
                            }

                            aList.add(videoList);

                            GlobalData.class11LS2 = (aList);

                            //  allVideoList11.remove(videoList);
                        }

                        if (lesson.equals("L3")) {

                            ArrayList aList = new ArrayList();
                            if (GlobalData.class11LS3 != null) {
                                aList = GlobalData.class11LS3;
                            }

                            aList.add(videoList);

                            GlobalData.class11LS3 = (aList);

                            // allVideoList11.remove(videoList);
                        }

                        if (lesson.equals("L4")) {

                            ArrayList aList = new ArrayList();
                            if (GlobalData.class11LS4 != null) {
                                aList = GlobalData.class11LS4;
                            }

                            aList.add(videoList);

                            GlobalData.class11LS4 = (aList);

                            //  allVideoList11.remove(videoList);
                        }

                        if (lesson.equals("L5")) {

                            ArrayList aList = new ArrayList();
                            if (GlobalData.class11LS5 != null) {
                                aList = GlobalData.class11LS5;
                            }

                            aList.add(videoList);

                            GlobalData.class11LS5 = (aList);

                            //   allVideoList11.remove(videoList);
                        }

                        if (lesson.equals("L6")) {

                            ArrayList aList = new ArrayList();
                            if (GlobalData.class11LS6 != null) {
                                aList = GlobalData.class11LS6;
                            }

                            aList.add(videoList);

                            GlobalData.class11LS6 = (aList);

                            //  allVideoList11.remove(videoList);
                        }

                        if (lesson.equals("L7")) {

                            ArrayList aList = new ArrayList();
                            if (GlobalData.class11LS7 != null) {
                                aList = GlobalData.class11LS7;
                            }

                            aList.add(videoList);

                            GlobalData.class11LS7 = (aList);

                            // allVideoList11.remove(videoList);
                        }


                    }
                }
            }


            if (StudentClass.equals("10")) {
                if (allVideoList10 != null) {
                    for (int i = 0; i < allVideoList10.size(); i++) {
                        Video videoList = (Video) allVideoList10.get(i);
                        String x = videoList.name;
                        x = x.substring(3);

                        String lesson = x.substring(0, Math.min(x.length(), 2));
                        if (lesson.equals("L1")) {

                            ArrayList aList = new ArrayList();
                            if (GlobalData.class10LS1 != null) {
                                aList = GlobalData.class10LS1;
                            }

                            aList.add(videoList);

                            GlobalData.class10LS1 = (aList);

                            //   allVideoList10.remove(videoList);
                        }

                        if (lesson.equals("L2")) {

                            ArrayList aList = new ArrayList();
                            if (GlobalData.class10LS2 != null) {
                                aList = GlobalData.class10LS2;
                            }

                            aList.add(videoList);

                            GlobalData.class10LS2 = (aList);

                            //  allVideoList10.remove(videoList);
                        }

                        if (lesson.equals("L3")) {

                            ArrayList aList = new ArrayList();
                            if (GlobalData.class10LS3 != null) {
                                aList = GlobalData.class10LS3;
                            }

                            aList.add(videoList);

                            GlobalData.class10LS3 = (aList);

                            //   allVideoList10.remove(videoList);
                        }

                        if (lesson.equals("L4")) {

                            ArrayList aList = new ArrayList();
                            if (GlobalData.class10LS4 != null) {
                                aList = GlobalData.class10LS4;
                            }

                            aList.add(videoList);

                            GlobalData.class10LS4 = (aList);

                            //  allVideoList10.remove(videoList);
                        }

                        if (lesson.equals("L5")) {

                            ArrayList aList = new ArrayList();
                            if (GlobalData.class10LS5 != null) {
                                aList = GlobalData.class10LS5;
                            }

                            aList.add(videoList);

                            GlobalData.class10LS5 = (aList);

                            //   allVideoList10.remove(videoList);
                        }

                        if (lesson.equals("L6")) {

                            ArrayList aList = new ArrayList();
                            if (GlobalData.class10LS6 != null) {
                                aList = GlobalData.class10LS6;
                            }

                            aList.add(videoList);

                            GlobalData.class10LS6 = (aList);

                            //  allVideoList10.remove(videoList);
                        }

                        if (lesson.equals("L7")) {

                            ArrayList aList = new ArrayList();
                            if (GlobalData.class10LS7 != null) {
                                aList = GlobalData.class10LS7;
                            }

                            aList.add(videoList);

                            GlobalData.class10LS7 = (aList);

                            //  allVideoList10.remove(videoList);
                        }


                    }
                }
            }


            if (StudentClass.equals("1")) {
                if (allVideoList1 != null) {
                    for (int i = 0; i < allVideoList1.size(); i++) {
                        Video videoList = (Video) allVideoList1.get(i);
                        String x = videoList.name;
                        x = x.substring(2);

                        String lesson = x.substring(0, Math.min(x.length(), 2));
                        if (lesson.equals("L1")) {

                            ArrayList aList = new ArrayList();
                            if (GlobalData.class1LS1 != null) {
                                aList = GlobalData.class1LS1;
                            }

                            aList.add(videoList);

                            GlobalData.class1LS1 = (aList);

                            //   allVideoList1.remove(videoList);
                        }

                        if (lesson.equals("L2")) {

                            ArrayList aList = new ArrayList();
                            if (GlobalData.class1LS2 != null) {
                                aList = GlobalData.class1LS2;
                            }

                            aList.add(videoList);

                            GlobalData.class1LS2 = (aList);

                            //  allVideoList1.remove(videoList);
                        }

                        if (lesson.equals("L3")) {

                            ArrayList aList = new ArrayList();
                            if (GlobalData.class1LS3 != null) {
                                aList = GlobalData.class1LS3;
                            }

                            aList.add(videoList);

                            GlobalData.class1LS3 = (aList);

                            //   allVideoList1.remove(videoList);
                        }

                        if (lesson.equals("L4")) {

                            ArrayList aList = new ArrayList();
                            if (GlobalData.class1LS4 != null) {
                                aList = GlobalData.class1LS4;
                            }

                            aList.add(videoList);

                            GlobalData.class1LS4 = (aList);

                            //    allVideoList1.remove(videoList);
                        }

                        if (lesson.equals("L5")) {

                            ArrayList aList = new ArrayList();
                            if (GlobalData.class1LS5 != null) {
                                aList = GlobalData.class1LS5;
                            }

                            aList.add(videoList);

                            GlobalData.class1LS5 = (aList);

                            //   allVideoList1.remove(videoList);
                        }

                        if (lesson.equals("L6")) {

                            ArrayList aList = new ArrayList();
                            if (GlobalData.class1LS6 != null) {
                                aList = GlobalData.class1LS6;
                            }

                            aList.add(videoList);

                            GlobalData.class1LS6 = (aList);

                            //   allVideoList1.remove(videoList);
                        }

                        if (lesson.equals("L7")) {

                            ArrayList aList = new ArrayList();
                            if (GlobalData.class1LS7 != null) {
                                aList = GlobalData.class1LS7;
                            }

                            aList.add(videoList);

                            GlobalData.class1LS7 = (aList);

                            //  allVideoList1.remove(videoList);
                        }


                    }
                }
            }


            if (StudentClass.equals("2")) {
                if (allVideoList2 != null) {
                    for (int i = 0; i < allVideoList2.size(); i++) {
                        Video videoList = (Video) allVideoList2.get(i);
                        String x = videoList.name;
                        x = x.substring(2);

                        String lesson = x.substring(0, Math.min(x.length(), 2));
                        if (lesson.equals("L1")) {

                            ArrayList aList = new ArrayList();
                            if (GlobalData.class2LS1 != null) {
                                aList = GlobalData.class2LS1;
                            }

                            aList.add(videoList);

                            GlobalData.class2LS1 = (aList);

                            //   allVideoList2.remove(videoList);
                        }

                        if (lesson.equals("L2")) {

                            ArrayList aList = new ArrayList();
                            if (GlobalData.class2LS2 != null) {
                                aList = GlobalData.class2LS2;
                            }

                            aList.add(videoList);

                            GlobalData.class2LS2 = (aList);

                            //   allVideoList2.remove(videoList);
                        }

                        if (lesson.equals("L3")) {

                            ArrayList aList = new ArrayList();
                            if (GlobalData.class2LS3 != null) {
                                aList = GlobalData.class2LS3;
                            }

                            aList.add(videoList);

                            GlobalData.class2LS3 = (aList);

                            //  allVideoList2.remove(videoList);
                        }

                        if (lesson.equals("L4")) {

                            ArrayList aList = new ArrayList();
                            if (GlobalData.class2LS4 != null) {
                                aList = GlobalData.class2LS4;
                            }

                            aList.add(videoList);

                            GlobalData.class2LS4 = (aList);

                            //  allVideoList2.remove(videoList);
                        }

                        if (lesson.equals("L5")) {

                            ArrayList aList = new ArrayList();
                            if (GlobalData.class2LS5 != null) {
                                aList = GlobalData.class2LS5;
                            }

                            aList.add(videoList);

                            GlobalData.class2LS5 = (aList);

                            // allVideoList2.remove(videoList);
                        }

                        if (lesson.equals("L6")) {

                            ArrayList aList = new ArrayList();
                            if (GlobalData.class2LS6 != null) {
                                aList = GlobalData.class2LS6;
                            }

                            aList.add(videoList);

                            GlobalData.class2LS6 = (aList);

                            //  allVideoList2.remove(videoList);
                        }

                        if (lesson.equals("L7")) {

                            ArrayList aList = new ArrayList();
                            if (GlobalData.class2LS7 != null) {
                                aList = GlobalData.class2LS7;
                            }

                            aList.add(videoList);

                            GlobalData.class2LS7 = (aList);

                            //   allVideoList2.remove(videoList);
                        }


                    }
                }
            }


            if (StudentClass.equals("3")) {
                if (allVideoList3 != null) {
                    for (int i = 0; i < allVideoList3.size(); i++) {
                        Video videoList = (Video) allVideoList3.get(i);
                        String x = videoList.name;
                        x = x.substring(2);

                        String lesson = x.substring(0, Math.min(x.length(), 2));
                        if (lesson.equals("L1")) {

                            ArrayList aList = new ArrayList();
                            if (GlobalData.class3LS1 != null) {
                                aList = GlobalData.class3LS1;
                            }

                            aList.add(videoList);

                            GlobalData.class3LS1 = (aList);

                            //   allVideoList3.remove(videoList);
                        }

                        if (lesson.equals("L2")) {

                            ArrayList aList = new ArrayList();
                            if (GlobalData.class3LS2 != null) {
                                aList = GlobalData.class3LS2;
                            }

                            aList.add(videoList);

                            GlobalData.class3LS2 = (aList);

                            //  allVideoList3.remove(videoList);
                        }

                        if (lesson.equals("L3")) {

                            ArrayList aList = new ArrayList();
                            if (GlobalData.class3LS3 != null) {
                                aList = GlobalData.class3LS3;
                            }

                            aList.add(videoList);

                            GlobalData.class3LS3 = (aList);

                            //   allVideoList3.remove(videoList);
                        }

                        if (lesson.equals("L4")) {

                            ArrayList aList = new ArrayList();
                            if (GlobalData.class3LS4 != null) {
                                aList = GlobalData.class3LS4;
                            }

                            aList.add(videoList);

                            GlobalData.class3LS4 = (aList);

                            //  allVideoList3.remove(videoList);
                        }

                        if (lesson.equals("L5")) {

                            ArrayList aList = new ArrayList();
                            if (GlobalData.class3LS5 != null) {
                                aList = GlobalData.class3LS5;
                            }

                            aList.add(videoList);

                            GlobalData.class3LS5 = (aList);

                            //   allVideoList3.remove(videoList);
                        }

                        if (lesson.equals("L6")) {

                            ArrayList aList = new ArrayList();
                            if (GlobalData.class3LS6 != null) {
                                aList = GlobalData.class3LS6;
                            }

                            aList.add(videoList);

                            GlobalData.class3LS6 = (aList);

                            //   allVideoList3.remove(videoList);
                        }

                        if (lesson.equals("L7")) {

                            ArrayList aList = new ArrayList();
                            if (GlobalData.class3LS7 != null) {
                                aList = GlobalData.class3LS7;
                            }

                            aList.add(videoList);

                            GlobalData.class3LS7 = (aList);

                            //     allVideoList3.remove(videoList);
                        }


                    }
                }
            }


            if (StudentClass.equals("4")) {
                if (allVideoList4 != null) {
                    for (int i = 0; i < allVideoList4.size(); i++) {
                        Video videoList = (Video) allVideoList4.get(i);
                        String x = videoList.name;
                        x = x.substring(2);

                        String lesson = x.substring(0, Math.min(x.length(), 2));
                        if (lesson.equals("L1")) {

                            ArrayList aList = new ArrayList();
                            if (GlobalData.class4LS1 != null) {
                                aList = GlobalData.class4LS1;
                            }

                            aList.add(videoList);

                            GlobalData.class4LS1 = (aList);

                            //  //allVideoList4.remove(videoList);
                        }

                        if (lesson.equals("L2")) {

                            ArrayList aList = new ArrayList();
                            if (GlobalData.class4LS2 != null) {
                                aList = GlobalData.class4LS2;
                            }

                            aList.add(videoList);

                            GlobalData.class4LS2 = (aList);

                            //allVideoList4.remove(videoList);
                        }

                        if (lesson.equals("L3")) {

                            ArrayList aList = new ArrayList();
                            if (GlobalData.class4LS3 != null) {
                                aList = GlobalData.class4LS3;
                            }

                            aList.add(videoList);

                            GlobalData.class4LS3 = (aList);

                            //allVideoList4.remove(videoList);
                        }

                        if (lesson.equals("L4")) {

                            ArrayList aList = new ArrayList();
                            if (GlobalData.class4LS4 != null) {
                                aList = GlobalData.class4LS4;
                            }

                            aList.add(videoList);

                            GlobalData.class4LS4 = (aList);

                            //allVideoList4.remove(videoList);
                        }

                        if (lesson.equals("L5")) {

                            ArrayList aList = new ArrayList();
                            if (GlobalData.class4LS5 != null) {
                                aList = GlobalData.class4LS5;
                            }

                            aList.add(videoList);

                            GlobalData.class4LS5 = (aList);

                            //allVideoList4.remove(videoList);
                        }

                        if (lesson.equals("L6")) {

                            ArrayList aList = new ArrayList();
                            if (GlobalData.class4LS6 != null) {
                                aList = GlobalData.class4LS6;
                            }

                            aList.add(videoList);

                            GlobalData.class4LS6 = (aList);

                            //allVideoList4.remove(videoList);
                        }

                        if (lesson.equals("L7")) {

                            ArrayList aList = new ArrayList();
                            if (GlobalData.class4LS7 != null) {
                                aList = GlobalData.class4LS7;
                            }

                            aList.add(videoList);

                            GlobalData.class4LS7 = (aList);

                            //allVideoList4.remove(videoList);
                        }


                    }
                }
            }


            if (StudentClass.equals("5")) {
                if (allVideoList5 != null) {
                    for (int i = 0; i < allVideoList5.size(); i++) {
                        Video videoList = (Video) allVideoList5.get(i);
                        String x = videoList.name;
                        x = x.substring(2);

                        String lesson = x.substring(0, Math.min(x.length(), 2));
                        if (lesson.equals("L1")) {

                            ArrayList aList = new ArrayList();
                            if (GlobalData.class5LS1 != null) {
                                aList = GlobalData.class5LS1;
                            }

                            aList.add(videoList);

                            GlobalData.class5LS1 = (aList);

                            //allVideoList5.remove(videoList);
                        }

                        if (lesson.equals("L2")) {

                            ArrayList aList = new ArrayList();
                            if (GlobalData.class5LS2 != null) {
                                aList = GlobalData.class5LS2;
                            }

                            aList.add(videoList);

                            GlobalData.class5LS2 = (aList);

                            //allVideoList5.remove(videoList);
                        }

                        if (lesson.equals("L3")) {

                            ArrayList aList = new ArrayList();
                            if (GlobalData.class5LS3 != null) {
                                aList = GlobalData.class5LS3;
                            }

                            aList.add(videoList);

                            GlobalData.class5LS3 = (aList);

                            //allVideoList5.remove(videoList);
                        }

                        if (lesson.equals("L4")) {

                            ArrayList aList = new ArrayList();
                            if (GlobalData.class5LS4 != null) {
                                aList = GlobalData.class5LS4;
                            }

                            aList.add(videoList);

                            GlobalData.class5LS4 = (aList);

                            //allVideoList5.remove(videoList);
                        }

                        if (lesson.equals("L5")) {

                            ArrayList aList = new ArrayList();
                            if (GlobalData.class5LS5 != null) {
                                aList = GlobalData.class5LS5;
                            }

                            aList.add(videoList);

                            GlobalData.class5LS5 = (aList);

                            //allVideoList5.remove(videoList);
                        }

                        if (lesson.equals("L6")) {

                            ArrayList aList = new ArrayList();
                            if (GlobalData.class5LS6 != null) {
                                aList = GlobalData.class5LS6;
                            }

                            aList.add(videoList);

                            GlobalData.class5LS6 = (aList);

                            //allVideoList5.remove(videoList);
                        }

                        if (lesson.equals("L7")) {

                            ArrayList aList = new ArrayList();
                            if (GlobalData.class5LS7 != null) {
                                aList = GlobalData.class5LS7;
                            }

                            aList.add(videoList);

                            GlobalData.class5LS7 = (aList);

                            //allVideoList5.remove(videoList);
                        }


                    }
                }
            }


            if (StudentClass.equals("6")) {
                if (allVideoList6 != null) {
                    for (int i = 0; i < allVideoList6.size(); i++) {
                        Video videoList = (Video) allVideoList6.get(i);
                        String x = videoList.name;
                        x = x.substring(2);

                        String lesson = x.substring(0, Math.min(x.length(), 2));
                        if (lesson.equals("L1")) {

                            ArrayList aList = new ArrayList();
                            if (GlobalData.class6LS1 != null) {
                                aList = GlobalData.class6LS1;
                            }

                            aList.add(videoList);

                            GlobalData.class6LS1 = (aList);

                            //allVideoList6.remove(videoList);
                        }

                        if (lesson.equals("L2")) {

                            ArrayList aList = new ArrayList();
                            if (GlobalData.class6LS2 != null) {
                                aList = GlobalData.class6LS2;
                            }

                            aList.add(videoList);

                            GlobalData.class6LS2 = (aList);

                            //allVideoList6.remove(videoList);
                        }

                        if (lesson.equals("L3")) {

                            ArrayList aList = new ArrayList();
                            if (GlobalData.class6LS3 != null) {
                                aList = GlobalData.class6LS3;
                            }

                            aList.add(videoList);

                            GlobalData.class6LS3 = (aList);

                            //allVideoList6.remove(videoList);
                        }

                        if (lesson.equals("L4")) {

                            ArrayList aList = new ArrayList();
                            if (GlobalData.class6LS4 != null) {
                                aList = GlobalData.class6LS4;
                            }

                            aList.add(videoList);

                            GlobalData.class6LS4 = (aList);

                            //allVideoList6.remove(videoList);
                        }

                        if (lesson.equals("L5")) {

                            ArrayList aList = new ArrayList();
                            if (GlobalData.class6LS5 != null) {
                                aList = GlobalData.class6LS5;
                            }

                            aList.add(videoList);

                            GlobalData.class6LS5 = (aList);

                            //allVideoList6.remove(videoList);
                        }

                        if (lesson.equals("L6")) {

                            ArrayList aList = new ArrayList();
                            if (GlobalData.class6LS6 != null) {
                                aList = GlobalData.class6LS6;
                            }

                            aList.add(videoList);

                            GlobalData.class6LS6 = (aList);

                            //allVideoList6.remove(videoList);
                        }

                        if (lesson.equals("L7")) {

                            ArrayList aList = new ArrayList();
                            if (GlobalData.class6LS7 != null) {
                                aList = GlobalData.class6LS7;
                            }

                            aList.add(videoList);

                            GlobalData.class6LS7 = (aList);

                            //allVideoList6.remove(videoList);
                        }


                    }
                }
            }


            if (StudentClass.equals("7")) {
                if (allVideoList7 != null) {
                    for (int i = 0; i < allVideoList7.size(); i++) {
                        Video videoList = (Video) allVideoList7.get(i);
                        String x = videoList.name;
                        x = x.substring(2);

                        String lesson = x.substring(0, Math.min(x.length(), 2));
                        if (lesson.equals("L1")) {

                            ArrayList aList = new ArrayList();
                            if (GlobalData.class7LS1 != null) {
                                aList = GlobalData.class7LS1;
                            }

                            aList.add(videoList);

                            GlobalData.class7LS1 = (aList);

                            //allVideoList7.remove(videoList);
                        }

                        if (lesson.equals("L2")) {

                            ArrayList aList = new ArrayList();
                            if (GlobalData.class7LS2 != null) {
                                aList = GlobalData.class7LS2;
                            }

                            aList.add(videoList);

                            GlobalData.class7LS2 = (aList);

                            //allVideoList7.remove(videoList);
                        }

                        if (lesson.equals("L3")) {

                            ArrayList aList = new ArrayList();
                            if (GlobalData.class7LS3 != null) {
                                aList = GlobalData.class7LS3;
                            }

                            aList.add(videoList);

                            GlobalData.class7LS3 = (aList);

                            //allVideoList7.remove(videoList);
                        }

                        if (lesson.equals("L4")) {

                            ArrayList aList = new ArrayList();
                            if (GlobalData.class7LS4 != null) {
                                aList = GlobalData.class7LS4;
                            }

                            aList.add(videoList);

                            GlobalData.class7LS4 = (aList);

                            //allVideoList7.remove(videoList);
                        }

                        if (lesson.equals("L5")) {

                            ArrayList aList = new ArrayList();
                            if (GlobalData.class7LS5 != null) {
                                aList = GlobalData.class7LS5;
                            }

                            aList.add(videoList);

                            GlobalData.class7LS5 = (aList);

                            //allVideoList7.remove(videoList);
                        }

                        if (lesson.equals("L6")) {

                            ArrayList aList = new ArrayList();
                            if (GlobalData.class7LS6 != null) {
                                aList = GlobalData.class7LS6;
                            }

                            aList.add(videoList);

                            GlobalData.class7LS6 = (aList);

                            //allVideoList7.remove(videoList);
                        }

                        if (lesson.equals("L7")) {

                            ArrayList aList = new ArrayList();
                            if (GlobalData.class7LS7 != null) {
                                aList = GlobalData.class7LS7;
                            }

                            aList.add(videoList);

                            GlobalData.class7LS7 = (aList);

                            //allVideoList7.remove(videoList);
                        }


                    }
                }
            }


            if (StudentClass.equals("8")) {
                if (allVideoList8 != null) {
                    for (int i = 0; i < allVideoList8.size(); i++) {
                        Video videoList = (Video) allVideoList8.get(i);
                        String x = videoList.name;
                        x = x.substring(2);

                        String lesson = x.substring(0, Math.min(x.length(), 2));
                        if (lesson.equals("L1")) {

                            ArrayList aList = new ArrayList();
                            if (GlobalData.class8LS1 != null) {
                                aList = GlobalData.class8LS1;
                            }

                            aList.add(videoList);

                            GlobalData.class8LS1 = (aList);

                            //allVideoList8.remove(videoList);
                        }

                        if (lesson.equals("L2")) {

                            ArrayList aList = new ArrayList();
                            if (GlobalData.class8LS2 != null) {
                                aList = GlobalData.class8LS2;
                            }

                            aList.add(videoList);

                            GlobalData.class8LS2 = (aList);

                            //allVideoList8.remove(videoList);
                        }

                        if (lesson.equals("L3")) {

                            ArrayList aList = new ArrayList();
                            if (GlobalData.class8LS3 != null) {
                                aList = GlobalData.class8LS3;
                            }

                            aList.add(videoList);

                            GlobalData.class8LS3 = (aList);

                            //allVideoList8.remove(videoList);
                        }

                        if (lesson.equals("L4")) {

                            ArrayList aList = new ArrayList();
                            if (GlobalData.class8LS4 != null) {
                                aList = GlobalData.class8LS4;
                            }

                            aList.add(videoList);

                            GlobalData.class8LS4 = (aList);

                            //allVideoList8.remove(videoList);
                        }

                        if (lesson.equals("L5")) {

                            ArrayList aList = new ArrayList();
                            if (GlobalData.class8LS5 != null) {
                                aList = GlobalData.class8LS5;
                            }

                            aList.add(videoList);

                            GlobalData.class8LS5 = (aList);

                            //allVideoList8.remove(videoList);
                        }

                        if (lesson.equals("L6")) {

                            ArrayList aList = new ArrayList();
                            if (GlobalData.class8LS6 != null) {
                                aList = GlobalData.class8LS6;
                            }

                            aList.add(videoList);

                            GlobalData.class8LS6 = (aList);

                            //allVideoList8.remove(videoList);
                        }

                        if (lesson.equals("L7")) {

                            ArrayList aList = new ArrayList();
                            if (GlobalData.class8LS7 != null) {
                                aList = GlobalData.class8LS7;
                            }

                            aList.add(videoList);

                            GlobalData.class8LS7 = (aList);

                            //allVideoList8.remove(videoList);
                        }


                    }
                }
            }


            if (StudentClass.equals("9")) {
                if (allVideoList9 != null) {
                    for (int i = 0; i < allVideoList9.size(); i++) {
                        Video videoList = (Video) allVideoList9.get(i);
                        String x = videoList.name;
                        x = x.substring(2);

                        String lesson = x.substring(0, Math.min(x.length(), 2));
                        if (lesson.equals("L1")) {

                            ArrayList aList = new ArrayList();
                            if (GlobalData.class9LS1 != null) {
                                aList = GlobalData.class9LS1;
                            }

                            aList.add(videoList);

                            GlobalData.class9LS1 = (aList);

                            //allVideoList9.remove(videoList);
                        }

                        if (lesson.equals("L2")) {

                            ArrayList aList = new ArrayList();
                            if (GlobalData.class9LS2 != null) {
                                aList = GlobalData.class9LS2;
                            }

                            aList.add(videoList);

                            GlobalData.class9LS2 = (aList);

                            //allVideoList9.remove(videoList);
                        }

                        if (lesson.equals("L3")) {

                            ArrayList aList = new ArrayList();
                            if (GlobalData.class9LS3 != null) {
                                aList = GlobalData.class9LS3;
                            }

                            aList.add(videoList);

                            GlobalData.class9LS3 = (aList);

                            //allVideoList9.remove(videoList);
                        }

                        if (lesson.equals("L4")) {

                            ArrayList aList = new ArrayList();
                            if (GlobalData.class9LS4 != null) {
                                aList = GlobalData.class9LS4;
                            }

                            aList.add(videoList);

                            GlobalData.class9LS4 = (aList);

                            //allVideoList9.remove(videoList);
                        }

                        if (lesson.equals("L5")) {

                            ArrayList aList = new ArrayList();
                            if (GlobalData.class9LS5 != null) {
                                aList = GlobalData.class9LS5;
                            }

                            aList.add(videoList);

                            GlobalData.class9LS5 = (aList);

                            //allVideoList9.remove(videoList);
                        }

                        if (lesson.equals("L6")) {

                            ArrayList aList = new ArrayList();
                            if (GlobalData.class9LS6 != null) {
                                aList = GlobalData.class9LS6;
                            }

                            aList.add(videoList);

                            GlobalData.class9LS6 = (aList);

                            //allVideoList9.remove(videoList);
                        }

                        if (lesson.equals("L7")) {

                            ArrayList aList = new ArrayList();
                            if (GlobalData.class9LS7 != null) {
                                aList = GlobalData.class9LS7;
                            }

                            aList.add(videoList);

                            GlobalData.class9LS7 = (aList);

                            //allVideoList9.remove(videoList);
                        }


                    }
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, String.valueOf(e), Toast.LENGTH_SHORT).show();
        }

        if (StudentClass.equals("12")) {
            if (GlobalData.class12LS1 != null) {
                addedLessons.addAll(GlobalData.class12LS1);
            }
            if (GlobalData.class12LS2 != null) {
                addedLessons.addAll(GlobalData.class12LS2);
            }
            if (GlobalData.class12LS3 != null) {
                addedLessons.addAll(GlobalData.class12LS3);
            }
            if (GlobalData.class12LS4 != null) {
                addedLessons.addAll(GlobalData.class12LS4);
            }
            if (GlobalData.class12LS5 != null) {
                addedLessons.addAll(GlobalData.class12LS5);
            }
            if (GlobalData.class12LS6 != null) {
                addedLessons.addAll(GlobalData.class12LS6);
            }
            if (GlobalData.class12LS7 != null) {
                addedLessons.addAll(GlobalData.class12LS7);
            }
        }

        if (StudentClass.equals("11")) {
            if (GlobalData.class11LS1 != null) {
                addedLessons.addAll(GlobalData.class11LS1);
            }
            if (GlobalData.class11LS2 != null) {
                addedLessons.addAll(GlobalData.class11LS2);
            }
            if (GlobalData.class11LS3 != null) {
                addedLessons.addAll(GlobalData.class11LS3);
            }
            if (GlobalData.class11LS4 != null) {
                addedLessons.addAll(GlobalData.class11LS4);
            }
            if (GlobalData.class11LS5 != null) {
                addedLessons.addAll(GlobalData.class11LS5);
            }
            if (GlobalData.class11LS6 != null) {
                addedLessons.addAll(GlobalData.class11LS6);
            }
            if (GlobalData.class11LS7 != null) {
                addedLessons.addAll(GlobalData.class11LS7);
            }
        }

        if (StudentClass.equals("10")) {
            if (GlobalData.class10LS1 != null) {
                addedLessons.addAll(GlobalData.class10LS1);
            }
            if (GlobalData.class10LS2 != null) {
                addedLessons.addAll(GlobalData.class10LS2);
            }
            if (GlobalData.class10LS3 != null) {
                addedLessons.addAll(GlobalData.class10LS3);
            }
            if (GlobalData.class10LS4 != null) {
                addedLessons.addAll(GlobalData.class10LS4);
            }
            if (GlobalData.class10LS5 != null) {
                addedLessons.addAll(GlobalData.class10LS5);
            }
            if (GlobalData.class10LS6 != null) {
                addedLessons.addAll(GlobalData.class10LS6);
            }
            if (GlobalData.class10LS7 != null) {
                addedLessons.addAll(GlobalData.class10LS7);
            }
        }

        if (StudentClass.equals("9")) {
            if (GlobalData.class9LS1 != null) {
                addedLessons.addAll(GlobalData.class9LS1);
            }
            if (GlobalData.class9LS2 != null) {
                addedLessons.addAll(GlobalData.class9LS2);
            }
            if (GlobalData.class9LS3 != null) {
                addedLessons.addAll(GlobalData.class9LS3);
            }
            if (GlobalData.class9LS4 != null) {
                addedLessons.addAll(GlobalData.class9LS4);
            }
            if (GlobalData.class9LS5 != null) {
                addedLessons.addAll(GlobalData.class9LS5);
            }
            if (GlobalData.class9LS6 != null) {
                addedLessons.addAll(GlobalData.class9LS6);
            }
            if (GlobalData.class9LS7 != null) {
                addedLessons.addAll(GlobalData.class9LS7);
            }
        }

        if (StudentClass.equals("8")) {
            if (GlobalData.class8LS1 != null) {
                addedLessons.addAll(GlobalData.class8LS1);
            }
            if (GlobalData.class8LS2 != null) {
                addedLessons.addAll(GlobalData.class8LS2);
            }
            if (GlobalData.class8LS3 != null) {
                addedLessons.addAll(GlobalData.class8LS3);
            }
            if (GlobalData.class8LS4 != null) {
                addedLessons.addAll(GlobalData.class8LS4);
            }
            if (GlobalData.class8LS5 != null) {
                addedLessons.addAll(GlobalData.class8LS5);
            }
            if (GlobalData.class8LS6 != null) {
                addedLessons.addAll(GlobalData.class8LS6);
            }
            if (GlobalData.class8LS7 != null) {
                addedLessons.addAll(GlobalData.class8LS7);
            }
        }

        if (StudentClass.equals("7")) {
            if (GlobalData.class7LS1 != null) {
                addedLessons.addAll(GlobalData.class7LS1);
            }
            if (GlobalData.class7LS2 != null) {
                addedLessons.addAll(GlobalData.class7LS2);
            }
            if (GlobalData.class7LS3 != null) {
                addedLessons.addAll(GlobalData.class7LS3);
            }
            if (GlobalData.class7LS4 != null) {
                addedLessons.addAll(GlobalData.class7LS4);
            }
            if (GlobalData.class7LS5 != null) {
                addedLessons.addAll(GlobalData.class7LS5);
            }
            if (GlobalData.class7LS6 != null) {
                addedLessons.addAll(GlobalData.class7LS6);
            }
            if (GlobalData.class7LS7 != null) {
                addedLessons.addAll(GlobalData.class7LS7);
            }
        }

        if (StudentClass.equals("6")) {
            if (GlobalData.class6LS1 != null) {
                addedLessons.addAll(GlobalData.class6LS1);
            }
            if (GlobalData.class6LS2 != null) {
                addedLessons.addAll(GlobalData.class6LS2);
            }
            if (GlobalData.class6LS3 != null) {
                addedLessons.addAll(GlobalData.class6LS3);
            }
            if (GlobalData.class6LS4 != null) {
                addedLessons.addAll(GlobalData.class6LS4);
            }
            if (GlobalData.class6LS5 != null) {
                addedLessons.addAll(GlobalData.class6LS5);
            }
            if (GlobalData.class6LS6 != null) {
                addedLessons.addAll(GlobalData.class6LS6);
            }
            if (GlobalData.class6LS7 != null) {
                addedLessons.addAll(GlobalData.class6LS7);
            }
        }

        if (StudentClass.equals("5")) {
            if (GlobalData.class5LS1 != null) {
                addedLessons.addAll(GlobalData.class5LS1);
            }
            if (GlobalData.class5LS2 != null) {
                addedLessons.addAll(GlobalData.class5LS2);
            }
            if (GlobalData.class5LS3 != null) {
                addedLessons.addAll(GlobalData.class5LS3);
            }
            if (GlobalData.class5LS4 != null) {
                addedLessons.addAll(GlobalData.class5LS4);
            }
            if (GlobalData.class5LS5 != null) {
                addedLessons.addAll(GlobalData.class5LS5);
            }
            if (GlobalData.class5LS6 != null) {
                addedLessons.addAll(GlobalData.class5LS6);
            }
            if (GlobalData.class5LS7 != null) {
                addedLessons.addAll(GlobalData.class5LS7);
            }
        }

        if (StudentClass.equals("4")) {
            if (GlobalData.class4LS1 != null) {
                addedLessons.addAll(GlobalData.class4LS1);
            }
            if (GlobalData.class4LS2 != null) {
                addedLessons.addAll(GlobalData.class4LS2);
            }
            if (GlobalData.class4LS3 != null) {
                addedLessons.addAll(GlobalData.class4LS3);
            }
            if (GlobalData.class4LS4 != null) {
                addedLessons.addAll(GlobalData.class4LS4);
            }
            if (GlobalData.class4LS5 != null) {
                addedLessons.addAll(GlobalData.class4LS5);
            }
            if (GlobalData.class4LS6 != null) {
                addedLessons.addAll(GlobalData.class4LS6);
            }
            if (GlobalData.class4LS7 != null) {
                addedLessons.addAll(GlobalData.class4LS7);
            }
        }

        if (StudentClass.equals("3")) {
            if (GlobalData.class3LS1 != null) {
                addedLessons.addAll(GlobalData.class3LS1);
            }
            if (GlobalData.class3LS2 != null) {
                addedLessons.addAll(GlobalData.class3LS2);
            }
            if (GlobalData.class3LS3 != null) {
                addedLessons.addAll(GlobalData.class3LS3);
            }
            if (GlobalData.class3LS4 != null) {
                addedLessons.addAll(GlobalData.class3LS4);
            }
            if (GlobalData.class3LS5 != null) {
                addedLessons.addAll(GlobalData.class3LS5);
            }
            if (GlobalData.class3LS6 != null) {
                addedLessons.addAll(GlobalData.class3LS6);
            }
            if (GlobalData.class3LS7 != null) {
                addedLessons.addAll(GlobalData.class3LS7);
            }
        }

        if (StudentClass.equals("2")) {
            if (GlobalData.class2LS1 != null) {
                addedLessons.addAll(GlobalData.class2LS1);
            }
            if (GlobalData.class2LS2 != null) {
                addedLessons.addAll(GlobalData.class2LS2);
            }
            if (GlobalData.class2LS3 != null) {
                addedLessons.addAll(GlobalData.class2LS3);
            }
            if (GlobalData.class2LS4 != null) {
                addedLessons.addAll(GlobalData.class2LS4);
            }
            if (GlobalData.class2LS5 != null) {
                addedLessons.addAll(GlobalData.class2LS5);
            }
            if (GlobalData.class2LS6 != null) {
                addedLessons.addAll(GlobalData.class2LS6);
            }
            if (GlobalData.class2LS7 != null) {
                addedLessons.addAll(GlobalData.class2LS7);
            }
        }

        if (StudentClass.equals("1")) {
            if (GlobalData.class1LS1 != null) {
                addedLessons.addAll(GlobalData.class1LS1);
            }
            if (GlobalData.class1LS2 != null) {
                addedLessons.addAll(GlobalData.class1LS2);
            }
            if (GlobalData.class1LS3 != null) {
                addedLessons.addAll(GlobalData.class1LS3);
            }
            if (GlobalData.class1LS4 != null) {
                addedLessons.addAll(GlobalData.class1LS4);
            }
            if (GlobalData.class1LS5 != null) {
                addedLessons.addAll(GlobalData.class1LS5);
            }
            if (GlobalData.class1LS6 != null) {
                addedLessons.addAll(GlobalData.class1LS6);
            }
            if (GlobalData.class1LS7 != null) {
                addedLessons.addAll(GlobalData.class1LS7);
            }
        }


        GlobalData.addedVideos = addedLessons;

        no_of_videos.setText("Files - " + String.valueOf(addedLessons.size()));

        added_list = findViewById(R.id.added_list);
        VideoAddedAdapter videoAddedAdapter = new
            VideoAddedAdapter(addedLessons, added_list);
        added_list.setAdapter(videoAddedAdapter);
        LinearLayoutManager added_liste_adapterlayoutManager
            = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        added_list.setLayoutManager(added_liste_adapterlayoutManager);

        mediaCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(VideoListing.this,VideoPlay.class);
                intent.putExtra("class",StudentClass);
                startActivity(intent);
            }
        });


    }

    private void toast(String string) {
        Toast.makeText(VideoListing.this, string, Toast.LENGTH_SHORT).show();
    }
   //TODO: tobe used in video play screen

    /*void media(String videoUrl) {
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
    }*/

}

