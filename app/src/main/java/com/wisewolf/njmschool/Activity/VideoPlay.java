package com.wisewolf.njmschool.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.AlgorithmParameters;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.text.SimpleDateFormat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.vimeo.networking.model.Video;
import com.wisewolf.njmschool.Adapter.Class_videoAdapter;
import com.wisewolf.njmschool.Adapter.SubjectAdapter;
import com.wisewolf.njmschool.Database.OfflineDatabase;
import com.wisewolf.njmschool.Exceptions.VimeoException;
import com.wisewolf.njmschool.Globals.GlobalData;
import com.wisewolf.njmschool.Globals.SubjectList;
import com.wisewolf.njmschool.Models.OfflineVideos;
import com.wisewolf.njmschool.Models.VideoUp;
import com.wisewolf.njmschool.R;
import com.wisewolf.njmschool.RetrofitClientInstance;
import com.wisewolf.njmschool.VimeoCallback;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import retrofit2.Call;
import retrofit2.Callback;

import static android.content.ContentValues.TAG;

public class VideoPlay extends AppCompatActivity implements VimeoCallback {
    RecyclerView subj_list, video_play_list;
    String StudentClass = "12", studentdiv = "S";
    ImageView download, fullscreen;
    VideoView videoView;
    Spinner lesson_selectSpinner;
    ArrayList nowShowing = new ArrayList();

    String lessn[] = {"Lesson 1", "Lesson 2", "Lesson 3", "Lesson 4", "Lesson 5", "Lesson 6", "Lesson 7"};
    String lessn_code[] = {"L1", "L2", "L3", "L4", "L5", "L6", "L7"};
    String lesson_flag = "ALL";
    String details = ".-.-.", vURL = "",download_url="",download_name="",pass="WISEWOLF";
    TextView topic, head, teacher;
    Video selectedVideo;
    ProgressDialog mProgressDialog;

    OfflineDatabase dbb;
    String nametostore="",salt_name="",dir_name="",inv_name="",location="",userid="",extra1="",extra2="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_listing);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        isStoragePermissionGranted();
        mProgressDialogInit();
        userid=GlobalData.regno;
        dbb = new OfflineDatabase(getApplicationContext());

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();
        final RetrofitClientInstance.GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(RetrofitClientInstance.GetDataService.class);


        subj_list = findViewById(R.id.subject_list);
        video_play_list = findViewById(R.id.video_play_list);
        fullscreen = findViewById(R.id.fullscreen);
        download = findViewById(R.id.download);
        download.setVisibility(View.INVISIBLE);
        fullscreen.setVisibility(View.INVISIBLE);
        videoView = (VideoView) findViewById(R.id.videoView);
        lesson_selectSpinner = findViewById(R.id.lesson_select);
        head = findViewById(R.id.mainHead);
        topic = findViewById(R.id.topic);
        teacher = findViewById(R.id.teacher);
        lessonSpinnerLoad();


        fullscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VideoPlay.this, Fullscreen.class);
                intent.putExtra("url", vURL);
                startActivity(intent);
            }
        });

        intent();
        subjectAdapter();

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    dbb.OfflineVideoss();

                    Date c = Calendar.getInstance().getTime();
                    int found = 0;

                    SimpleDateFormat sd = new SimpleDateFormat("yymmhh");
                    String date = sd.format(new Date());
                    if (GlobalData.OfflineVideos==null){
                        found=0;
                    }
                    else {for (int i = 0; i < GlobalData.OfflineVideos.size(); i++) {
                        OfflineVideos offlineVideos = (OfflineVideos) GlobalData.OfflineVideos.get(i);
                        if (selectedVideo.name.equals(offlineVideos.getName()))
                            found = 1;


                    }}

                    if (found == 1) {
                        Toast.makeText(VideoPlay.this, "Video is already Downloaded", Toast.LENGTH_SHORT).show();
                    } else {
                        if (c.before(selectedVideo.download.get(0).expires)) {
                            String url = selectedVideo.download.get(0).link;
                            download_name = selectedVideo.name;
                            download_url = url;
                            nametostore = selectedVideo.name;
                            extra1 = selectedVideo.pictures.sizes.get(selectedVideo.pictures.sizes.size()-1).link;
                            extra2=selectedVideo.description;
                            download();
                        } else {
                            new AlertDialog.Builder(VideoPlay.this)
                                .setTitle("SECURITY ALERT")
                                .setMessage("Please Restart the app to follow security policy.\n" +
                                    "Download Link Expires every day.")

                                // Specifying a listener allows you to take an action before dismissing the dialog.
                                // The dialog is automatically dismissed when a dialog button is clicked.
                                .setPositiveButton("Restart", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(VideoPlay.this);
                                        SharedPreferences.Editor editor = sharedPrefs.edit();
                                        editor.putString("size", "0");
                                        editor.putString("Allvideolist", "");
                                        editor.apply();
                                        finishAffinity();
                                        System.exit(0);
                                    }
                                })

                                // A null listener allows the button to dismiss the dialog and take no further action.
                                .setNegativeButton("Later", null)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                        }
                    }

                }
                catch (Exception ignored){}


            }
        });

        videoListAdapter();
        media("android.resource://" + getPackageName() + "/" + R.raw.v1);
        vURL = "android.resource://" + getPackageName() + "/" + R.raw.v1;

        lesson_selectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
              //  Toast.makeText(VideoPlay.this, lessn[position] + " is showing", Toast.LENGTH_SHORT).show();
                lesson_flag = lessn_code[position];
                subj_wise_videoListAdapter(nowShowing);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });

    }

    private void mProgressDialogInit() {
        mProgressDialog = new ProgressDialog(VideoPlay.this);
        mProgressDialog.setMessage("Downloading Please wait . . .");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(true);
    }

    private void download() {
        try{
            videoView.pause();
            if (videoView.isPlaying())
                videoView.stopPlayback();
            Downback DB = new Downback();
            DB.execute("");
        }
        catch (Exception e){
            Toast.makeText(this, String.valueOf(e), Toast.LENGTH_SHORT).show();
        }

    }

    @SuppressLint("StaticFieldLeak")
    private class Downback extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            videoView.pause();
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            mProgressDialog.dismiss();

            dbb.insertinto(nametostore,salt_name,dir_name,inv_name,location,userid,extra1,extra2);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setMax(100);
            mProgressDialog.setProgress(values[0]);
        }

        @Override
        protected String doInBackground(String... strings) {


            String s = "";
            String name = download_name;

            try {
                String rootDir;
                File rootFile ;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                    rootFile = new File(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), File.separator + "Fil");
                else
                    rootFile =new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                        + File.separator + "Fil");



               // dir_name = rootDir;
                location = rootFile.toString();

                boolean a=rootFile.mkdirs();



                URL url = new URL(download_url);
                HttpURLConnection c = (HttpURLConnection) url.openConnection();
                c.setRequestMethod("GET");


                c.connect();
                int fileLength = c.getContentLength();

                FileOutputStream f = new FileOutputStream(new File(rootFile,
                    name));
                int status = c.getResponseCode();
                InputStream error = c.getErrorStream();
                InputStream in = c.getInputStream();
                byte[] buffer = new byte[1024];
                int len1 = 0;
                long total = 0;
                int count = 0;
                while ((len1 = in.read(buffer)) > 0) {
                    total += len1;
                    if (fileLength > 0) // only if total length is known
                        publishProgress((int) (total * 100 / fileLength));
                    f.write(buffer, 0, len1);
                }
                f.close();
                encry(buffer, new File(rootFile,
                    name),rootFile);

                s = "sucess";


            } catch (Exception e) {
                s = e.toString();

            }


            return s;
        }


    }

    void encry(byte[] args, File file, File rootFile)   throws Exception {
        try {
            // file to be encrypted
            FileInputStream inFile = new FileInputStream(file);

            SimpleDateFormat sd = new SimpleDateFormat("yymmhh");
            String date = sd.format(new Date());
            String name = download_name + ".des";
     /*   String rootDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            + File.separator + "Fil";
        File rootFile = new File(rootDir);
        rootFile.mkdir();*/
            // encrypted file
            FileOutputStream outFile = new FileOutputStream(new File(rootFile,
                name));

            // password to encrypt the file
            String password = pass;

            // password, iv and salt should be transferred to the other end
            // in a secure manner

            // salt is used for encoding
            // writing it to a file
            // salt should be transferred to the recipient securely
            // for decryption
            byte[] salt = new byte[8];
            SecureRandom secureRandom = new SecureRandom();
            secureRandom.nextBytes(salt);
            name =download_name+ "salt" + ".enc";
            salt_name=name;  // to store in local db
            FileOutputStream saltOutFile = new FileOutputStream(new File(rootFile,
                name));
            saltOutFile.write(salt);
            saltOutFile.close();

            SecretKeyFactory factory = SecretKeyFactory
                .getInstance("PBKDF2WithHmacSHA1");
            KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt, 65536,
                256);
            SecretKey secretKey = factory.generateSecret(keySpec);
            SecretKey secret = new SecretKeySpec(secretKey.getEncoded(), "AES");

            //
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secret);
            AlgorithmParameters params = cipher.getParameters();

            // iv adds randomness to the text and just makes the mechanism more
            // secure
            // used while initializing the cipher
            // file to store the iv
            name =download_name+ "iv" + ".enc";
            inv_name=name;
            FileOutputStream ivOutFile = new FileOutputStream(new File(rootFile,
                name));
            byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();
            ivOutFile.write(iv);
            ivOutFile.close();

            //file encryption
            byte[] input = new byte[64];
            int bytesRead;

            while ((bytesRead = inFile.read(input)) != -1) {
                byte[] output = cipher.update(input, 0, bytesRead);
                if (output != null)
                    outFile.write(output);
            }

            byte[] output = cipher.doFinal();
            if (output != null)
                outFile.write(output);

            inFile.close();
            outFile.flush();
            outFile.close();
            file.delete();

        }catch (Exception e)
        {
            String a="";
        }




    }

    private void lessonSpinnerLoad() {
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lessn);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        lesson_selectSpinner.setAdapter(spinnerArrayAdapter);
    }

    private void intent() {
        Intent intent = getIntent();
        StudentClass = intent.getStringExtra("class");
        //TODO
        StudentClass = GlobalData.clas;
        studentdiv = GlobalData.sect;
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
        nowShowing = GlobalData.addedVideos;


        video_play_list.setAdapter(new Class_videoAdapter(VideoPlay.this, GlobalData.addedVideos, video_play_list, new Class_videoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Video item) {
                Toast.makeText(VideoPlay.this, "playing -" + item.name, Toast.LENGTH_SHORT).show();
                details = item.description;
                media(item.files.get(0).link);
                vURL = item.files.get(0).link;
                download.setVisibility(View.VISIBLE);
                fullscreen.setVisibility(View.VISIBLE);
                String regno=GlobalData.regno;
                Glide.with(VideoPlay.this).asGif().load(R.raw.download).into(download);
                String[] time=String.valueOf(item.createdTime).split("\\s+");
                final RetrofitClientInstance.GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(RetrofitClientInstance.GetDataService.class);

                Call<VideoUp> saveVideo = service.saveVideo(regno,item.name,item.files.get(0).link,item.description,item.pictures.sizes.get(selectedVideo.pictures.sizes.size()-1).link,time[3]);
                saveVideo.enqueue(new Callback<VideoUp>() {
                    @Override
                    public void onResponse(Call<VideoUp> call, retrofit2.Response<VideoUp> response) {
                        Toast.makeText(VideoPlay.this, "Video Uploaded", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<VideoUp> call, Throwable t) {
                        Toast.makeText(VideoPlay.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                });

            }


        }));

        LinearLayoutManager added_liste_adapterlayoutManager
            = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        video_play_list.setLayoutManager(added_liste_adapterlayoutManager);

    }

    private void subj_wise_videoListAdapter(ArrayList selectedVideoList) {
        nowShowing = selectedVideoList;
        selectedVideoList = getLesson(selectedVideoList);

        video_play_list.setAdapter(new Class_videoAdapter(VideoPlay.this, selectedVideoList, video_play_list, new Class_videoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Video item) {
                selectedVideo=item;
                Toast.makeText(VideoPlay.this, "playing -" + item.name, Toast.LENGTH_SHORT).show();
                details = item.description;
                media(item.files.get(0).link);
                vURL = item.files.get(0).link;
                download.setVisibility(View.VISIBLE);
                fullscreen.setVisibility(View.VISIBLE);
                Glide.with(VideoPlay.this).asGif().load(R.raw.download).into(download);
                String regno=GlobalData.regno;
                String[] time=String.valueOf(item.createdTime).split("\\s+");
                final RetrofitClientInstance.GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(RetrofitClientInstance.GetDataService.class);

                Call<VideoUp> saveVideo = service.saveVideo(regno,item.name,item.files.get(0).link,item.description,item.pictures.sizes.get(selectedVideo.pictures.sizes.size()-1).link,time[3]);
                saveVideo.enqueue(new Callback<VideoUp>() {
                    @Override
                    public void onResponse(Call<VideoUp> call, retrofit2.Response<VideoUp> response) {
                        Toast.makeText(VideoPlay.this, "Video Uploaded", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<VideoUp> call, Throwable t) {
                        Toast.makeText(VideoPlay.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                });

            }


        }));
        LinearLayoutManager added_liste_adapterlayoutManager
            = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        video_play_list.setLayoutManager(added_liste_adapterlayoutManager);

    }

    private ArrayList getLesson(ArrayList selectedVideoList) {
        ArrayList returnVideo = new ArrayList();
        String lesn = "";
        if (StudentClass.equals("12") || StudentClass.equals("11") || StudentClass.equals("10")) {
            for (int i = 0; i < selectedVideoList.size(); i++) {
                Video video = (Video) selectedVideoList.get(i);
                lesn = (video.name.substring(3));
                lesn = lesn.substring(0, Math.min(lesn.length(), 2));
                if (lesn.equals(lesson_flag)) {
                    returnVideo.add(selectedVideoList.get(i));
                }

            }

        }
        else if (StudentClass.equals("6")){

            String school=GlobalData.regno.substring(0, Math.min(GlobalData.regno.length(), 3));
            if (school.equals("AND")){
                for (int i = 0; i < selectedVideoList.size(); i++) {
                    Video video = (Video) selectedVideoList.get(i);
                    if (video.name.substring(0, Math.min(video.name.length(), 1)).equals("N")){
                        lesn = (video.name.substring(3));
                        lesn = lesn.substring(0, Math.min(lesn.length(), 2));
                        if (lesn.equals(lesson_flag)) {
                            returnVideo.add(selectedVideoList.get(i));
                        }
                    }
                    else {
                        lesn = (video.name.substring(2));
                        lesn = lesn.substring(0, Math.min(lesn.length(), 2));
                        if (lesn.equals(lesson_flag)) {
                            returnVideo.add(selectedVideoList.get(i));
                        }
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
        return returnVideo;
    }

    private void getVideosList(String s) {
        switch (StudentClass) {
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


        } else if (studentdiv.equals("H")) {
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


        } else {
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
            subj = (video.name.substring(4));
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
        String school=GlobalData.regno.substring(0, Math.min(GlobalData.regno.length(), 3));
        if (school.equals("AND")){

            for (int i = 0; i < videoList.size(); i++) {
                Video video = (Video) videoList.get(i);
                if (video.name.substring(0, Math.min(video.name.length(), 1)).equals("N")){
                    subj = (video.name.substring(5));
                    subj = subj.substring(0, Math.min(subj.length(), 3));
                    if (subj.equals(subCode)) {
                        subject_videoList.add(videoList.get(i));
                    }
                }
                else {
                    subj = (video.name.substring(4));
                    subj = subj.substring(0, Math.min(subj.length(), 3));
                    if (subj.equals("EGG")||subj.equals("HNG")||subj.equals("MAT")||subj.equals("CMP")){
                        if (subj.equals(subCode)) {
                            subject_videoList.add(videoList.get(i));
                        }
                    }


                }
                subj = (video.name.substring(5));
                subj = subj.substring(0, Math.min(subj.length(), 3));
                if (subj.equals(subCode)) {
                    subject_videoList.add(videoList.get(i));
                }

            }
        }
        else {
            for (int i = 0; i < videoList.size(); i++) {
                Video video = (Video) videoList.get(i);
                subj = (video.name.substring(4));
                subj = subj.substring(0, Math.min(subj.length(), 3));
                if (subj.equals(subCode)) {
                    subject_videoList.add(videoList.get(i));
                }

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

        String school=GlobalData.regno.substring(0, Math.min(GlobalData.regno.length(), 3));
        if (school.equals("AND")){

            for (int i = 0; i < videoList.size(); i++) {
                Video video = (Video) videoList.get(i);
                if (video.name.substring(0, Math.min(video.name.length(), 1)).equals("N")){
                    subj = (video.name.substring(5));
                    subj = subj.substring(0, Math.min(subj.length(), 3));
                    if (subj.equals(subCode)) {
                        subject_videoList.add(videoList.get(i));
                    }
                }
                else {
                    subj = (video.name.substring(4));
                    subj = subj.substring(0, Math.min(subj.length(), 3));
                    if (subj.equals("EGG")||subj.equals("HNG")||subj.equals("MAT")||subj.equals("CMP")){
                        if (subj.equals(subCode)) {
                            subject_videoList.add(videoList.get(i));
                        }
                    }


                }
                subj = (video.name.substring(5));
                subj = subj.substring(0, Math.min(subj.length(), 3));
                if (subj.equals(subCode)) {
                    subject_videoList.add(videoList.get(i));
                }

            }
        }
        else {
            for (int i = 0; i < videoList.size(); i++) {
                Video video = (Video) videoList.get(i);
                subj = (video.name.substring(4));
                subj = subj.substring(0, Math.min(subj.length(), 3));
                if (subj.equals(subCode)) {
                    subject_videoList.add(videoList.get(i));
                }

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
        String school=GlobalData.regno.substring(0, Math.min(GlobalData.regno.length(), 3));
        String subj = "";
        if (school.equals("AND")){

            for (int i = 0; i < videoList.size(); i++) {
                Video video = (Video) videoList.get(i);
                if (video.name.substring(0, Math.min(video.name.length(), 1)).equals("N")){
                    subj = (video.name.substring(5));
                    subj = subj.substring(0, Math.min(subj.length(), 3));
                    if (subj.equals(subCode)) {
                        subject_videoList.add(videoList.get(i));
                    }
                }
                else {
                    subj = (video.name.substring(4));
                    subj = subj.substring(0, Math.min(subj.length(), 3));
                    if (subj.equals("EGG")||subj.equals("HNG")||subj.equals("MAT")||subj.equals("CMP")){
                        if (subj.equals(subCode)) {
                            subject_videoList.add(videoList.get(i));
                        }
                    }


                }
                subj = (video.name.substring(5));
                subj = subj.substring(0, Math.min(subj.length(), 3));
                if (subj.equals(subCode)) {
                    subject_videoList.add(videoList.get(i));
                }

            }
        }
        else {
            for (int i = 0; i < videoList.size(); i++) {
                Video video = (Video) videoList.get(i);
                subj = (video.name.substring(4));
                subj = subj.substring(0, Math.min(subj.length(), 3));
                if (subj.equals(subCode)) {
                    subject_videoList.add(videoList.get(i));
                }

            }
        }




        subj_wise_videoListAdapter(subject_videoList);
    }


    private void class5(String subject) {
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

    private void class4(String subject) {
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

    private void class3(String subject) {
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
        String[] detail = details.split("-");
        topic.setText(detail[0]);
        head.setText(detail[1]);
        teacher.setText(detail[2]);

            mProgressDialog.setMessage("Please wait . . .");
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();


        //Use a media controller so that you can scroll the video contents
        //and also to pause, start the video.
        final MediaController mediaController = new MediaController(this);

        //  mediaController.setAnchorView(videoView);

        videoView.setMediaController(mediaController);
        videoView.setVideoURI(Uri.parse(videoUrl));
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(final MediaPlayer mediaPlayer) {
                mediaController.setAnchorView(videoView);
               // videoView.start();
                mediaPlayer.start();
                mProgressDialog.cancel();
                mediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                        mp.start();
                        mProgressDialog.cancel();
                    }
                });
            }
        });

    }

    public  boolean isStoragePermissionGranted() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted");
                return true;
            } else {

                Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        //permission is automatically granted on sdk<23 upon installation
        Log.v(TAG,"Permission is granted");
        return true;

    }


    @Override
    public void vimeoURLCallback(String callback) {

    }

    @Override
    public void videoExceptionCallback(VimeoException exceptionCallback) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Log.v(TAG,"Permission: "+permissions[0]+ "was "+grantResults[0]);
            //resume tasks needing this permission
        }
    }
}