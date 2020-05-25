package com.wisewolf.njmschool.Activity;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.net.http.SslCertificate;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vimeo.networking.VimeoClient;
import com.vimeo.networking.callbacks.ModelCallback;
import com.vimeo.networking.model.Video;
import com.vimeo.networking.model.VideoList;
import com.vimeo.networking.model.error.VimeoError;
import com.wisewolf.njmschool.Adapter.OfflineVideoAdapter;
import com.wisewolf.njmschool.Adapter.RecentPlayedAdapter;
import com.wisewolf.njmschool.Adapter.VideoAddedAdapter;
import com.wisewolf.njmschool.Database.OfflineDatabase;
import com.wisewolf.njmschool.Globals.GlobalData;
import com.wisewolf.njmschool.Models.OfflineVideos;
import com.wisewolf.njmschool.Models.VideoUp;
import com.wisewolf.njmschool.ObjectSerialiser;
import com.wisewolf.njmschool.R;
import com.wisewolf.njmschool.RetrofitClientInstance;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideoListing extends AppCompatActivity {


    String StudentClass = "12",pass="WISEWOLF",DcryptName="",DcryptSalt="",DcryptInv="";
    RecyclerView added_list,offlineList,recentVideo;


    private ProgressDialog mProgressDialog;
    Video videoTest;
    ImageView topic,logout;
    int exitflag=0;

    ArrayList allVideoList = new ArrayList();
    ArrayList allVideoList1 = new ArrayList();
    ArrayList allVideoList2 = new ArrayList();
    ArrayList allVideoList3 = new ArrayList();
    ArrayList allVideoList4 = new ArrayList();
    ArrayList allVideoList5 = new ArrayList();
    ArrayList allVideoList6 = new ArrayList();
    ArrayList allVideoListN6 = new ArrayList();
    ArrayList allVideoList7 = new ArrayList();
    ArrayList allVideoList8 = new ArrayList();
    ArrayList allVideoList9 = new ArrayList();
    ArrayList allVideoList10 = new ArrayList();
    ArrayList allVideoList11 = new ArrayList();
    ArrayList allVideoList12 = new ArrayList();
    ArrayList allVideoListnrl = new ArrayList();
    ArrayList allVideoListlkg = new ArrayList();
    ArrayList allVideoListukg = new ArrayList();
    TextView no_of_videos,offlineFlagTextview,playFlag;

    ArrayList addedLessons = new ArrayList();
    CardView mediaCard,photocard,docucard;
    OfflineDatabase dbb;

    @Override
    protected void onResume() {
        super.onResume();

        try{


            recentVideos();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    offlinevideos();


                }
            });


        }
        catch (Exception e){
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void recentVideos() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                final RetrofitClientInstance.GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(RetrofitClientInstance.GetDataService.class);
                String regno=GlobalData.regno;
                Call<List<VideoUp>> getVideo = service.getVideo(regno);
                getVideo.enqueue(new Callback<List<VideoUp>>() {
                    @Override
                    public void onResponse(Call<List<VideoUp>> call, Response<List<VideoUp>> response) {


                        playFlag.setVisibility(View.VISIBLE);
                        added_list.setVisibility(View.GONE);
                        recentVideo.setAdapter(new RecentPlayedAdapter(VideoListing.this, (ArrayList) response.body(), recentVideo, new RecentPlayedAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(VideoUp item) {
                                Intent intent=new Intent(VideoListing.this,Fullscreen.class);
                                intent.putExtra("url",item.getVideoLink());
                                startActivity(intent);
                            }
                        }));
                        LinearLayoutManager added_liste_adapterlayoutManager
                            = new LinearLayoutManager(VideoListing.this, LinearLayoutManager.HORIZONTAL, false);
                        recentVideo.setLayoutManager(added_liste_adapterlayoutManager);
                    }

                    @Override
                    public void onFailure(Call<List<VideoUp>> call, Throwable t) {
                        Toast.makeText(VideoListing.this, "", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });


    }
    private void mProgressDialogInit() {
        mProgressDialog = new ProgressDialog(VideoListing.this);
        mProgressDialog.setMessage("Please wait . . .");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        topic = findViewById(R.id.profilepic);
        mProgressDialogInit();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                Glide.with(VideoListing.this).asGif().load(R.raw.profile).into(topic);

            }
        });

        no_of_videos = findViewById(R.id.no_of_files);
        mediaCard=findViewById(R.id.media_card);
        photocard=findViewById(R.id.photo_card);
        docucard=findViewById(R.id.docu_card);
        offlineList=findViewById(R.id.offline_list);
        offlineFlagTextview=findViewById(R.id.lesonListFlag);
        added_list = findViewById(R.id.added_list);
        recentVideo = findViewById(R.id.recent_video);
        playFlag = findViewById(R.id.playListFlag);
        logout = findViewById(R.id.logout);

        // media("android.resource://" + getPackageName() + "/" + R.raw.v1); tobe used in video play screen
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();
        Intent intent=getIntent();
        getData(intent);
        dbb = new OfflineDatabase(getApplicationContext());
       // offlinevideos();



        allVideoList = GlobalData.allVideoList;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

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

            }
        });





        //TODO VIDEO SCHOOL WISE CUTTING

        allVideoList= schoolwise(allVideoList);


        actionBar.hide();

        photocard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(VideoListing.this, "update coming soon....", Toast.LENGTH_SHORT).show();
            }
        });
        docucard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(VideoListing.this, "update coming soon....", Toast.LENGTH_SHORT).show();
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout_funct();
            }
        });

        // media(videoList.data.get(1).files.get(0).link);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
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

                    if (classname.equals("NC6")) {

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

                    if (classname.equals("LKL")) {

                        ArrayList aList = new ArrayList();
                        if (GlobalData.classlkg != null) {
                            aList = GlobalData.classlkg;
                        }

                        aList.add(videoList);

                        GlobalData.classlkg = (aList);

                        // allVideoList.remove(videoList);
                    }

                    if (classname.equals("UKL")) {

                        ArrayList aList = new ArrayList();
                        if (GlobalData.classukg != null) {
                            aList = GlobalData.classukg;
                        }

                        aList.add(videoList);

                        GlobalData.classukg = (aList);

                        // allVideoList.remove(videoList);
                    }

                    if (classname.equals("NRL")) {

                        ArrayList aList = new ArrayList();
                        if (GlobalData.classNRL != null) {
                            aList = GlobalData.classNRL;
                        }

                        aList.add(videoList);

                        GlobalData.classNRL = (aList);

                        //  // allVideoList.remove(videoList);
                    }


                }


            }
        });

        configure();




        mediaCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(VideoListing.this,VideoPlay.class);
                intent.putExtra("class",StudentClass);
                startActivity(intent);
            }
        });


    }

    private void logout_funct() {
        GlobalData.addedVideos=new ArrayList();
        GlobalData.ReservallVideoList =new ArrayList();

        GlobalData.addedVideos =new ArrayList();

        GlobalData.class1 =new ArrayList();
        GlobalData.class2 =new ArrayList();
        GlobalData.class3 =new ArrayList();
        GlobalData.class4 =new ArrayList();
        GlobalData.class5 =new ArrayList();
        GlobalData.class6 =new ArrayList();
        GlobalData.classN6 =new ArrayList();
        GlobalData.class7 =new ArrayList();
        GlobalData.class8 =new ArrayList();
        GlobalData.class9 =new ArrayList();
        GlobalData.class10 =new ArrayList();
        GlobalData.class11 =new ArrayList();
        GlobalData.class12 =new ArrayList();
        GlobalData.classlkg =new ArrayList();
        GlobalData.classukg =new ArrayList();
        GlobalData.classNRL =new ArrayList();

        GlobalData.class1LS1 =new ArrayList();
        GlobalData.class2LS1 =new ArrayList();
        GlobalData.class3LS1 =new ArrayList();
        GlobalData.class4LS1 =new ArrayList();
        GlobalData.class5LS1 =new ArrayList();
        GlobalData.class6LS1 =new ArrayList();
        GlobalData.class7LS1 =new ArrayList();
        GlobalData.class8LS1 =new ArrayList();
        GlobalData.class9LS1 =new ArrayList();
        GlobalData.class10LS1 =new ArrayList();
        GlobalData.class11LS1 =new ArrayList();
        GlobalData.class12LS1 =new ArrayList();
        GlobalData.classlkgLS1 =new ArrayList();
        GlobalData.classukgLS1 =new ArrayList();
        GlobalData.classnrlLS1 =new ArrayList();

        GlobalData.class1LS2=new ArrayList();
        GlobalData.class2LS2 =new ArrayList();
        GlobalData.class3LS2 =new ArrayList();
        GlobalData.class4LS2 =new ArrayList();
        GlobalData.class5LS2 =new ArrayList();
        GlobalData.class6LS2 =new ArrayList();
        GlobalData.class7LS2 =new ArrayList();
        GlobalData.class8LS2 =new ArrayList();
        GlobalData.class9LS2 =new ArrayList();
        GlobalData.class10LS2 =new ArrayList();
        GlobalData.class11LS2 =new ArrayList();
        GlobalData.class12LS2 =new ArrayList();
        GlobalData.classlkgLS2 =new ArrayList();
        GlobalData.classukgLS2 =new ArrayList();
        GlobalData.classnrlLS2 =new ArrayList();

        GlobalData.class1LS3 =new ArrayList();
        GlobalData.class2LS3 =new ArrayList();
        GlobalData.class3LS3 =new ArrayList();
        GlobalData.class4LS3 =new ArrayList();
        GlobalData.class5LS3 =new ArrayList();
        GlobalData.class6LS3 =new ArrayList();
        GlobalData.class7LS3 =new ArrayList();
        GlobalData.class8LS3 =new ArrayList();
        GlobalData.class9LS3 =new ArrayList();
        GlobalData.class10LS3 =new ArrayList();
        GlobalData.class11LS3 =new ArrayList();
        GlobalData.class12LS3 =new ArrayList();
        GlobalData.classlkgLS3 =new ArrayList();
        GlobalData.classukgLS3 =new ArrayList();
        GlobalData.classnrlLS3 =new ArrayList();

        GlobalData.class1LS4 =new ArrayList();
        GlobalData.class2LS4 =new ArrayList();
        GlobalData.class3LS4 =new ArrayList();
        GlobalData.class4LS4 =new ArrayList();
        GlobalData.class5LS4 =new ArrayList();
        GlobalData.class6LS4 =new ArrayList();
        GlobalData.class7LS4 =new ArrayList();
        GlobalData.class8LS4 =new ArrayList();
        GlobalData.class9LS4 =new ArrayList();
        GlobalData.class10LS4 =new ArrayList();
        GlobalData.class11LS4 =new ArrayList();
        GlobalData.class12LS4 =new ArrayList();
        GlobalData.classlkgLS4 =new ArrayList();
        GlobalData.classukgLS4 =new ArrayList();
        GlobalData.classnrlLS4 =new ArrayList();

        GlobalData.class1LS5 =new ArrayList();
        GlobalData.class2LS5=new ArrayList();
        GlobalData.class3LS5 =new ArrayList();
        GlobalData.class4LS5 =new ArrayList();
        GlobalData.class5LS5 =new ArrayList();
        GlobalData.class6LS5 =new ArrayList();
        GlobalData.class7LS5 =new ArrayList();
        GlobalData.class8LS5 =new ArrayList();
        GlobalData.class9LS5 =new ArrayList();
        GlobalData.class10LS5 =new ArrayList();
        GlobalData.class11LS5 =new ArrayList();
        GlobalData.class12LS5 =new ArrayList();
        GlobalData.classlkgLS5 =new ArrayList();
        GlobalData.classukgLS5 =new ArrayList();
        GlobalData.classnrlLS5 =new ArrayList();

        GlobalData.class1LS6 =new ArrayList();
        GlobalData.class2LS6=new ArrayList();
        GlobalData.class3LS6 =new ArrayList();
        GlobalData.class4LS6 =new ArrayList();
        GlobalData.class5LS6 =new ArrayList();
        GlobalData.class6LS6 =new ArrayList();
        GlobalData.class7LS6 =new ArrayList();
        GlobalData.class8LS6 =new ArrayList();
        GlobalData.class9LS6 =new ArrayList();
        GlobalData.class10LS6 =new ArrayList();
        GlobalData.class11LS6 =new ArrayList();
        GlobalData.class12LS6 =new ArrayList();
        GlobalData.classlkgLS6 =new ArrayList();
        GlobalData.classukgLS6 =new ArrayList();
        GlobalData.classnrlLS6 =new ArrayList();

        GlobalData.class1LS7 =new ArrayList();
        GlobalData.class2LS7=new ArrayList();
        GlobalData.class3LS7 =new ArrayList();
        GlobalData.class4LS7 =new ArrayList();
        GlobalData.class5LS7 =new ArrayList();
        GlobalData.class6LS7 =new ArrayList();
        GlobalData.class7LS7 =new ArrayList();
        GlobalData.class8LS7 =new ArrayList();
        GlobalData.class9LS7 =new ArrayList();
        GlobalData.class10LS7 =new ArrayList();
        GlobalData.class11LS7 =new ArrayList();
        GlobalData.class12LS7 =new ArrayList();
        GlobalData.classlkgLS7 =new ArrayList();
        GlobalData.classukgLS7 =new ArrayList();
        GlobalData.classnrlLS7 =new ArrayList();

        finish();
    }

    private void configure() {
        Configure DB = new Configure();
        DB.execute("");
    }

    private class Configure extends AsyncTask<String, String, String> {
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            no_of_videos.setText("Files - " + String.valueOf(addedLessons.size()));


            VideoAddedAdapter videoAddedAdapter = new
                VideoAddedAdapter(addedLessons, added_list);
            added_list.setAdapter(videoAddedAdapter);
            LinearLayoutManager added_liste_adapterlayoutManager
                = new LinearLayoutManager(VideoListing.this, LinearLayoutManager.VERTICAL, false);
            added_list.setLayoutManager(added_liste_adapterlayoutManager);
            mProgressDialog.cancel();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

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
            allVideoListnrl = GlobalData.classNRL;
            allVideoListlkg = GlobalData.classlkg;
            allVideoListukg = GlobalData.classukg;



            try {
                if (StudentClass.equals("NRL")) {

                    if (allVideoListnrl != null) {

                        for (int i = 0; i < allVideoListnrl.size(); i++) {
                            Video videoList = (Video) allVideoListnrl.get(i);
                            String x = videoList.name;
                            x = x.substring(2);

                            String lesson = x.substring(0, Math.min(x.length(), 2));
                            if (lesson.equals("L1")) {

                                ArrayList aList = new ArrayList();
                                if (GlobalData.classnrlLS1 != null) {
                                    aList = GlobalData.classnrlLS1;
                                }

                                aList.add(videoList);

                                GlobalData.classnrlLS1 = (aList);

                                //  allVideoList12.remove(videoList);
                            }

                            if (lesson.equals("L2")) {

                                ArrayList aList = new ArrayList();
                                if (GlobalData.classnrlLS2 != null) {
                                    aList = GlobalData.classnrlLS2;
                                }

                                aList.add(videoList);

                                GlobalData.classnrlLS2 = (aList);

                                //  allVideoList12.remove(videoList);
                            }

                            if (lesson.equals("L3")) {

                                ArrayList aList = new ArrayList();
                                if (GlobalData.classnrlLS3 != null) {
                                    aList = GlobalData.classnrlLS3;
                                }

                                aList.add(videoList);

                                GlobalData.classnrlLS3 = (aList);

                                // allVideoList12.remove(videoList);
                            }

                            if (lesson.equals("L4")) {

                                ArrayList aList = new ArrayList();
                                if (GlobalData.classnrlLS4 != null) {
                                    aList = GlobalData.classnrlLS4;
                                }

                                aList.add(videoList);

                                GlobalData.classnrlLS4 = (aList);

                                //   allVideoList12.remove(videoList);
                            }

                            if (lesson.equals("L5")) {

                                ArrayList aList = new ArrayList();
                                if (GlobalData.classnrlLS5 != null) {
                                    aList = GlobalData.classnrlLS5;
                                }

                                aList.add(videoList);

                                GlobalData.classnrlLS5 = (aList);

                                //allVideoList12.remove(videoList);
                            }

                            if (lesson.equals("L6")) {

                                ArrayList aList = new ArrayList();
                                if (GlobalData.classnrlLS6 != null) {
                                    aList = GlobalData.classnrlLS6;
                                }

                                aList.add(videoList);

                                GlobalData.classnrlLS6 = (aList);

                                //  allVideoList12.remove(videoList);
                            }

                            if (lesson.equals("L7")) {

                                ArrayList aList = new ArrayList();
                                if (GlobalData.classnrlLS7 != null) {
                                    aList = GlobalData.classnrlLS7;
                                }

                                aList.add(videoList);

                                GlobalData.classnrlLS7 = (aList);

                                //  allVideoList12.remove(videoList);
                            }


                        }
                    }

                }

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

                if (StudentClass.equals("LKG")) {
                    if (allVideoListlkg != null) {
                        for (int i = 0; i < allVideoListlkg.size(); i++) {
                            Video videoList = (Video) allVideoListlkg.get(i);
                            String x = videoList.name;
                            x = x.substring(2);

                            String lesson = x.substring(0, Math.min(x.length(), 2));
                            if (lesson.equals("L1")) {

                                ArrayList aList = new ArrayList();
                                if (GlobalData.classlkgLS1 != null) {
                                    aList = GlobalData.classlkgLS1;
                                }

                                aList.add(videoList);

                                GlobalData.classlkgLS1 = (aList);

                                //allVideoList9.remove(videoList);
                            }

                            if (lesson.equals("L2")) {

                                ArrayList aList = new ArrayList();
                                if (GlobalData.classlkgLS2 != null) {
                                    aList = GlobalData.classlkgLS2;
                                }

                                aList.add(videoList);

                                GlobalData.classlkgLS2 = (aList);

                                //allVideoList9.remove(videoList);
                            }

                            if (lesson.equals("L3")) {

                                ArrayList aList = new ArrayList();
                                if (GlobalData.classlkgLS3 != null) {
                                    aList = GlobalData.classlkgLS3;
                                }

                                aList.add(videoList);

                                GlobalData.classlkgLS3 = (aList);

                                //allVideoList9.remove(videoList);
                            }

                            if (lesson.equals("L4")) {

                                ArrayList aList = new ArrayList();
                                if (GlobalData.classlkgLS4 != null) {
                                    aList = GlobalData.classlkgLS4;
                                }

                                aList.add(videoList);

                                GlobalData.classlkgLS4 = (aList);

                                //allVideoList9.remove(videoList);
                            }

                            if (lesson.equals("L5")) {

                                ArrayList aList = new ArrayList();
                                if (GlobalData.classlkgLS5 != null) {
                                    aList = GlobalData.classlkgLS5;
                                }

                                aList.add(videoList);

                                GlobalData.classlkgLS5 = (aList);

                                //allVideoList9.remove(videoList);
                            }

                            if (lesson.equals("L6")) {

                                ArrayList aList = new ArrayList();
                                if (GlobalData.classlkgLS6 != null) {
                                    aList = GlobalData.classlkgLS6;
                                }

                                aList.add(videoList);

                                GlobalData.classlkgLS6 = (aList);

                                //allVideoList9.remove(videoList);
                            }

                            if (lesson.equals("L7")) {

                                ArrayList aList = new ArrayList();
                                if (GlobalData.classlkgLS7 != null) {
                                    aList = GlobalData.classlkgLS7;
                                }

                                aList.add(videoList);

                                GlobalData.classlkgLS7 = (aList);

                                //allVideoList9.remove(videoList);
                            }


                        }
                    }
                }

                if (StudentClass.equals("UKG")) {
                    if (allVideoListukg != null) {
                        for (int i = 0; i < allVideoListukg.size(); i++) {
                            Video videoList = (Video) allVideoListukg.get(i);
                            String x = videoList.name;
                            x = x.substring(2);

                            String lesson = x.substring(0, Math.min(x.length(), 2));
                            if (lesson.equals("L1")) {

                                ArrayList aList = new ArrayList();
                                if (GlobalData.classukgLS1 != null) {
                                    aList = GlobalData.classukgLS1;
                                }

                                aList.add(videoList);

                                GlobalData.classukgLS1 = (aList);

                                //allVideoList9.remove(videoList);
                            }

                            if (lesson.equals("L2")) {

                                ArrayList aList = new ArrayList();
                                if (GlobalData.classukgLS2 != null) {
                                    aList = GlobalData.classukgLS2;
                                }

                                aList.add(videoList);

                                GlobalData.classukgLS2 = (aList);

                                //allVideoList9.remove(videoList);
                            }

                            if (lesson.equals("L3")) {

                                ArrayList aList = new ArrayList();
                                if (GlobalData.classukgLS3 != null) {
                                    aList = GlobalData.classukgLS3;
                                }

                                aList.add(videoList);

                                GlobalData.classukgLS3 = (aList);

                                //allVideoList9.remove(videoList);
                            }

                            if (lesson.equals("L4")) {

                                ArrayList aList = new ArrayList();
                                if (GlobalData.classukgLS4 != null) {
                                    aList = GlobalData.classukgLS4;
                                }

                                aList.add(videoList);

                                GlobalData.classukgLS4 = (aList);

                                //allVideoList9.remove(videoList);
                            }

                            if (lesson.equals("L5")) {

                                ArrayList aList = new ArrayList();
                                if (GlobalData.classukgLS5 != null) {
                                    aList = GlobalData.classukgLS5;
                                }

                                aList.add(videoList);

                                GlobalData.classukgLS5 = (aList);

                                //allVideoList9.remove(videoList);
                            }

                            if (lesson.equals("L6")) {

                                ArrayList aList = new ArrayList();
                                if (GlobalData.classukgLS6 != null) {
                                    aList = GlobalData.classukgLS6;
                                }

                                aList.add(videoList);

                                GlobalData.classukgLS6 = (aList);

                                //allVideoList9.remove(videoList);
                            }

                            if (lesson.equals("L7")) {

                                ArrayList aList = new ArrayList();
                                if (GlobalData.classukgLS7 != null) {
                                    aList = GlobalData.classukgLS7;
                                }

                                aList.add(videoList);

                                GlobalData.classukgLS7 = (aList);

                                //allVideoList9.remove(videoList);
                            }


                        }
                    }
                }




            }

            catch (Exception e) {

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
                addedLessons.addAll(allVideoList6);
             /*   if (GlobalData.class6LS1 != null) {
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
                }*/
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

            if (StudentClass.equals("NRL")) {
                if (GlobalData.classnrlLS1 != null) {
                    addedLessons.addAll(GlobalData.classnrlLS1);
                }
                if (GlobalData.classnrlLS2 != null) {
                    addedLessons.addAll(GlobalData.classnrlLS2);
                }
                if (GlobalData.classnrlLS3 != null) {
                    addedLessons.addAll(GlobalData.classnrlLS3);
                }
                if (GlobalData.classnrlLS4 != null) {
                    addedLessons.addAll(GlobalData.classnrlLS4);
                }
                if (GlobalData.classnrlLS5 != null) {
                    addedLessons.addAll(GlobalData.classnrlLS5);
                }
                if (GlobalData.classnrlLS6 != null) {
                    addedLessons.addAll(GlobalData.classnrlLS6);
                }
                if (GlobalData.classnrlLS7 != null) {
                    addedLessons.addAll(GlobalData.classnrlLS7);
                }
            }

            if (StudentClass.equals("LKG")) {
                if (GlobalData.classlkgLS1 != null) {
                    addedLessons.addAll(GlobalData.classlkgLS1);
                }
                if (GlobalData.classlkgLS2 != null) {
                    addedLessons.addAll(GlobalData.classlkgLS2);
                }
                if (GlobalData.classlkgLS3 != null) {
                    addedLessons.addAll(GlobalData.classlkgLS3);
                }
                if (GlobalData.classlkgLS4 != null) {
                    addedLessons.addAll(GlobalData.classlkgLS4);
                }
                if (GlobalData.classlkgLS5 != null) {
                    addedLessons.addAll(GlobalData.classlkgLS5);
                }
                if (GlobalData.classlkgLS6 != null) {
                    addedLessons.addAll(GlobalData.classlkgLS6);
                }
                if (GlobalData.classlkgLS7 != null) {
                    addedLessons.addAll(GlobalData.classlkgLS7);
                }
            }

            if (StudentClass.equals("UKG")) {
                if (GlobalData.classukgLS1 != null) {
                    addedLessons.addAll(GlobalData.classukgLS1);
                }
                if (GlobalData.classukgLS2 != null) {
                    addedLessons.addAll(GlobalData.classukgLS2);
                }
                if (GlobalData.classukgLS3 != null) {
                    addedLessons.addAll(GlobalData.classukgLS3);
                }
                if (GlobalData.classukgLS4 != null) {
                    addedLessons.addAll(GlobalData.classukgLS4);
                }
                if (GlobalData.classukgLS5 != null) {
                    addedLessons.addAll(GlobalData.classukgLS5);
                }
                if (GlobalData.classukgLS6 != null) {
                    addedLessons.addAll(GlobalData.classukgLS6);
                }
                if (GlobalData.classukgLS7 != null) {
                    addedLessons.addAll(GlobalData.classukgLS7);
                }
            }


            GlobalData.addedVideos = addedLessons;
            return  "z";
        }


    }

    private ArrayList schoolwise(ArrayList allVideoList) {
        ArrayList arrayList=new ArrayList();
        try {
            for (int i=0;i<allVideoList.size();i++){
                Video video=(Video)allVideoList.get(i);
                String[] name=video.name.split("-");
                ((Video) allVideoList.get(i)).name=name[1];

           /* if (video.name.substring(0, Math.min(video.name.length(), 1)).equals("C")){
                arrayList.add(video);
            }*/
            }
        }
        catch (Exception e)
        {
            Toast.makeText(this, "Video Nameing error Ln 2526", Toast.LENGTH_SHORT).show();
        }

        return allVideoList;

    }

    private void offlinevideos() {
        dbb.OfflineVideoss();
        if (GlobalData.OfflineVideos==null|| GlobalData.OfflineVideos.size()==0){}
        else {
            try {

                offlineFlagTextview.setText("Downloaded Videos");
                offlineFlagTextview.setVisibility(View.VISIBLE);
                added_list.setVisibility(View.GONE);

                offlineList.setAdapter(new OfflineVideoAdapter(GlobalData.OfflineVideos, VideoListing.this, offlineList, new OfflineVideoAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(OfflineVideos item) {
                        OFFLINE_MANAGE(item.getName(),item.getSalt(),item.getInv());


                    }


                }, new OfflineVideoAdapter.OnLongClickListener() {
                    @Override
                    public void onItemLongClick(final OfflineVideos item) {

                    }
                }));
                LinearLayoutManager added_liste_adapterlayoutManager
                    = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
                offlineList.setLayoutManager(added_liste_adapterlayoutManager);


            }
            catch (Exception e){
                Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void OFFLINE_MANAGE(String name, String salt, String inv) {
      DcryptName=name;
        DcryptSalt=salt;
        DcryptInv=inv;
        Decrypt DB = new Decrypt();
        DB.execute("");

    }

    private class Decrypt extends AsyncTask<String, String, String> {
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


            mProgressDialog.cancel();
            Intent intent=new Intent(VideoListing.this,OfflinePlayFullScreen.class);
            intent.putExtra("root",Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                + File.separator + "Fil");
            intent.putExtra("name", DcryptName);
            startActivity(intent);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.setMessage("Decrypting your video...");
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                d_encry(DcryptName,DcryptSalt,DcryptInv);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "s";
        }


    }

    void d_encry(String M_name, String M_salt, String M_inv) throws Exception {

        String password = pass;
        File rootFile ;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            rootFile = new File(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), File.separator + "Fil");
        else
            rootFile =new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                + File.separator + "Fil");
        boolean a=rootFile.mkdirs();
        // reading the salt
        // user should have secure mechanism to transfer the
        // salt, iv and password to the recipient
        String name = M_salt;
        FileInputStream saltFis = new FileInputStream(new File(rootFile,
            name));
        byte[] salt = new byte[8];
        saltFis.read(salt);
        saltFis.close();

        // reading the iv
        name = M_inv;
        FileInputStream ivFis = new FileInputStream(new File(rootFile,
            name));
        byte[] iv = new byte[16];
        ivFis.read(iv);
        ivFis.close();

        SecretKeyFactory factory = SecretKeyFactory
            .getInstance("PBKDF2WithHmacSHA1");
        KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt, 65536,
            256);
        SecretKey tmp = factory.generateSecret(keySpec);
        SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");

        // file decryption
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(iv));
        name = M_name + ".des";
        FileInputStream fis = new FileInputStream(new File(rootFile,
            name));
        name = M_name + ".mp4";
        FileOutputStream fos = new FileOutputStream(new File(rootFile,
            name));
        byte[] in = new byte[64];
        int read;
        while ((read = fis.read(in)) != -1) {
            byte[] output = cipher.update(in, 0, read);
            if (output != null)
                fos.write(output);
        }

        byte[] output = cipher.doFinal();
        if (output != null)
            fos.write(output);
        fis.close();
        fos.flush();
        fos.close();


    }



    private void getData(Intent intent) {
        TextView name,clas,phn,sec;
        name=findViewById(R.id.name_id);
        clas=findViewById(R.id.class_id);
        phn=findViewById(R.id.phone_id);
        sec=findViewById(R.id.setion_id);
String a=intent.getStringExtra("phone");
        name.setText(intent.getStringExtra("name"));
        clas.setText("Class :"+intent.getStringExtra("class"));
        phn.setText("Numb :"+intent.getStringExtra("phone"));
        sec.setText("Sect :"+intent.getStringExtra("sec"));

        switch(intent.getStringExtra("class")) {
            case "PREP":
                StudentClass = "NRL";
                break;
            case "LKG":
                StudentClass = "LKG";
                break;
            case "UKG":
                StudentClass = "UKG";
                break;
            case "XII":
            case "XI":
                StudentClass = "12";
                break;
            case "X":
                StudentClass = "10";
                break;
            case "IX":
                StudentClass = "9";
                break;
            case "VIII":
                StudentClass = "8";
                break;
            case "VII":
                StudentClass = "7";
                break;
            case "VI":
                StudentClass = "6";
                break;
            case "V":
                StudentClass = "5";
                break;
            case "IV":
                StudentClass = "4";
                break;
            case "III":
                StudentClass = "3";
                break;
            case "II":
                StudentClass = "2";
                break;
            case "I":
                StudentClass = "1";
                break;
        }
        GlobalData.clas=StudentClass;
        GlobalData.sect=intent.getStringExtra("sec");

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

    public void downloadFile(String s) {
        String DownloadUrl = s;
        DownloadManager.Request request1 = new DownloadManager.Request(Uri.parse(DownloadUrl));
        request1.setDescription("Sample Music File");   //appears the same in Notification bar while downloading
        request1.setTitle("Alphabet.mp4");
        request1.setVisibleInDownloadsUi(false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            request1.allowScanningByMediaScanner();
            request1.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
        }
        request1.setDestinationInExternalFilesDir(getApplicationContext(), "/File", "Alphabet.mp4");

        DownloadManager manager1 = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        Objects.requireNonNull(manager1).enqueue(request1);
        if (DownloadManager.STATUS_SUCCESSFUL == 8) {
            Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
       if (exitflag==0){
           new AlertDialog.Builder(VideoListing.this)
               .setTitle("CLOSE APP")
               .setMessage("Do you really want to close the`b app")

               // Specifying a listener allows you to take an action before dismissing the dialog.
               // The dialog is automatically dismissed when a dialog button is clicked.
               .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int which) {
                        finishAffinity();

                   }
               })

               // A null listener allows the button to dismiss the dialog and take no further action.
               .setNegativeButton("CLOSE", null)
               .setIcon(android.R.drawable.ic_dialog_alert)
               .show();
       }
    }
}

