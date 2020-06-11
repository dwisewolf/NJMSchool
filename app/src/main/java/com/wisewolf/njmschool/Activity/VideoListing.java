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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.wisewolf.njmschool.Models.ClassVideo;
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

    ImageView topic,logout;
    int exitflag=0;

    ArrayList allVideoList = new ArrayList();

    TextView no_of_videos,offlineFlagTextview,playFlag,shared_id;

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

        shared_id = findViewById(R.id.shared_id);
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



        //TODO VIDEO SCHOOL WISE CUTTING

        allVideoList= schoolwise(allVideoList);
     //   aSize=aSize+"   "+String.valueOf(allVideoList.size());

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
        logout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showNEwsAddAlert();
                return true;
            }
        });



        // media(videoList.data.get(1).files.get(0).link);



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

    private void showNEwsAddAlert() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(VideoListing.this);
        alertDialog.setTitle("PASSWORD");
        alertDialog.setMessage("Enter Password");

        final EditText input = new EditText(VideoListing.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);
        alertDialog.setIcon(R.drawable.logo_njms);

        alertDialog.setPositiveButton("YES",
            new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    String password = input.getText().toString();

                    if (password.equals("njms_news")) {

                        Intent myIntent1 = new Intent(VideoListing.this,
                            NewsSet.class);
                        startActivity(myIntent1);
                    } else {
                        Toast.makeText(getApplicationContext(),
                            "Wrong Password!", Toast.LENGTH_SHORT).show();

                    }
                }
            });

        alertDialog.setNegativeButton("NO",
            new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

        alertDialog.show();
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

        no_of_videos.setText("Files - " + String.valueOf(allVideoList.size()));



        GlobalData.addedVideos = allVideoList;
    }



    private ArrayList schoolwise(ArrayList allVideoList) {
        ArrayList arrayList=new ArrayList();
        try {
            for (int i=0;i<allVideoList.size();i++){
                ClassVideo video=(ClassVideo) allVideoList.get(i);
                String[] name=video.getData().getName().split("-");
                ((ClassVideo) allVideoList.get(i)).getData().setName(name[1]);

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



    @Override
    public void onBackPressed() {
       if (exitflag==0){
           new AlertDialog.Builder(VideoListing.this)
               .setTitle("CLOSE APP")
               .setMessage("Do you really want to close the app")

               // Specifying a listener allows you to take an action before dismissing the dialog.
               // The dialog is automatically dismissed when a dialog button is clicked.
               .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int which) {
                        finishAffinity();
                        System.exit(0);

                   }
               })

               // A null listener allows the button to dismiss the dialog and take no further action.
               .setNegativeButton("Cancel", null)
               .setIcon(android.R.drawable.ic_dialog_alert)
               .show();
       }
    }
}

