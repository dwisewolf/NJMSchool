package com.wisewolf.njmschool.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.AlgorithmParameters;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.text.SimpleDateFormat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
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
import android.os.StrictMode;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import com.vimeo.networking.Configuration;
import com.vimeo.networking.VimeoClient;
import com.vimeo.networking.callbacks.AuthCallback;
import com.vimeo.networking.callbacks.ModelCallback;
import com.vimeo.networking.model.Video;
import com.vimeo.networking.model.VideoFile;
import com.vimeo.networking.model.VideoList;
import com.vimeo.networking.model.error.VimeoError;
import com.vimeo.networking.model.playback.Play;
import com.wisewolf.njmschool.Adapter.Class_videoAdapter;
import com.wisewolf.njmschool.Adapter.SubjectAdapter;
import com.wisewolf.njmschool.Database.OfflineDatabase;
import com.wisewolf.njmschool.Exceptions.VimeoException;
import com.wisewolf.njmschool.Globals.GlobalData;
import com.wisewolf.njmschool.Globals.SubjectList;
import com.wisewolf.njmschool.Models.ClassVideo;
import com.wisewolf.njmschool.Models.OfflineVideos;
import com.wisewolf.njmschool.Models.VideoUp;
import com.wisewolf.njmschool.R;
import com.wisewolf.njmschool.RetrofitClientInstance;
import com.wisewolf.njmschool.VimeoCallback;

import org.jetbrains.annotations.NotNull;

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
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class VideoPlay extends AppCompatActivity implements VimeoCallback {
    RecyclerView subj_list, video_play_list;
    String StudentClass = "12", studentdiv = "S";
    ImageView download, fullscreen;
    VideoView videoView;
    Spinner lesson_selectSpinner;
    ArrayList nowShowing = new ArrayList();


    String lessn[] = {"Lesson 1", "Lesson 2", "Lesson 3", "Lesson 4", "Lesson 5", "Lesson 6", "Lesson 7",
                      "Lesson 8", "Lesson 9", "Lesson 10", "Lesson 11", "Lesson 12", "Lesson 13", "Lesson 14",
        "Lesson 15", "Lesson 16", "Lesson 17", "Lesson 18", "Lesson 19", "Lesson 20"};
    String lessn_code[] = {"L1", "L2", "L3", "L4", "L5", "L6", "L7",
                           "L8", "L9", "L0", "M1", "M2", "M3", "M4",
        "M5", "M6", "M7", "M8", "M9", "M0"};
    String lesson_flag = "ALL";
    String details = ".-.-.", documenturl,vURL = "",download_url="",download_name="",pass="WISEWOLF",document_name;
    TextView topic, head, teacher,notes;
    ClassVideo selectedVideo;
    ProgressDialog mProgressDialog;

    FirebaseStorage storage;
    StorageReference storageRef;

    OfflineDatabase dbb;
    String nametostore="",salt_name="",dir_name="",inv_name="",location="",userid="",extra1="",extra2="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_listing);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        isStoragePermissionGranted();
        mProgressDialogInit();
        userid=GlobalData.regno;
        dbb = new OfflineDatabase(getApplicationContext());
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();


        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();
        final RetrofitClientInstance.GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(RetrofitClientInstance.GetDataService.class);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        subj_list = findViewById(R.id.subject_list);
        video_play_list = findViewById(R.id.video_play_list);
        fullscreen = findViewById(R.id.fullscreen);
        download = findViewById(R.id.download);
        download.setVisibility(View.INVISIBLE);
        fullscreen.setVisibility(View.INVISIBLE);
        videoView = (VideoView) findViewById(R.id.videoView);
        lesson_selectSpinner = (Spinner) findViewById(R.id.lesson_select);
        head = findViewById(R.id.mainHead);
        topic = findViewById(R.id.topic);
        teacher = findViewById(R.id.teacher);
notes=findViewById(R.id.notes);
notes.setVisibility(View.GONE);
notes.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        String[] desc=documenturl.split("/");
        int len=desc.length;
        document_name=desc[len-1];
        String loc = dbb.DocumentList(document_name);
        if (!loc.equals("false")) {
            File data = new File(loc);
            Intent target = new Intent(Intent.ACTION_VIEW);
            target.setDataAndType(Uri.fromFile(data),"application/pdf");
            target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

            Intent intent = Intent.createChooser(target, "Open File");
            try {
                mProgressDialog.cancel();
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                // Instruct the user to install a PDF reader here, or something
            }
        } else {
            GetDocuments getDocuments = new GetDocuments();
            getDocuments.execute();
        }
    }
});

        lessonSpinnerLoad();
        intent();
        try {
            subjectAdapter();
        }
        catch (Exception e){
            FirebaseCrashlytics.getInstance().log(String.valueOf(e));
        }



        fullscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int current=videoView.getCurrentPosition();
                    Intent intent = new Intent(VideoPlay.this, Fullscreen.class);
                    intent.putExtra("url", vURL);
                    intent.putExtra("page", "vp");
                    intent.putExtra("current", String.valueOf(current));
                    startActivity(intent);

                }catch (Exception E){
                    Toast.makeText(VideoPlay.this, "Video Error", Toast.LENGTH_SHORT).show();
                }

            }
        });



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
                        if (selectedVideo.getData().getName().equals(offlineVideos.getName()))
                            found = 1;


                    }}

                    if (found == 1) {
                        Toast.makeText(VideoPlay.this, "Video is already Downloaded", Toast.LENGTH_SHORT).show();
                    } else {
                        if (c.before(selectedVideo.getData().getDownload().get(0).getExpires())) {
                            String url = selectedVideo.getData().getDownload().get(0).getLink();
                            download_name = selectedVideo.getData().getName();
                            download_url = url;
                            nametostore = selectedVideo.getData().getName();
                            extra1 = selectedVideo.getData().getPictures().getSizes().get(selectedVideo.getData().getPictures().getSizes().size()-1).getLink();
                            extra2=selectedVideo.getData().getDescription();
                            download();
                        } else {

                            configVimeo(selectedVideo);

                        }
                    }

                }
                catch (Exception ignored){}


            }
        });

        if (!StudentClass.equals("12")){ videoListAdapter();}

        media("android.resource://" + getPackageName() + "/" + R.raw.v1);
        vURL = "android.resource://" + getPackageName() + "/" + R.raw.v1;
        try {
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
        catch (Exception e){
            FirebaseCrashlytics.getInstance().log(String.valueOf(e));
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
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
            if (!s.equals("f")){
                dbb.insertinto(nametostore,salt_name,dir_name,inv_name,location,userid,extra1,extra2);
                String a="";
            }


        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setMax(100);
            mProgressDialog.setProgress(values[0]);
            if (values[0]==100){
                mProgressDialog.setMessage("Encrypting your video");
            }
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
                s="f";//for fail

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

        }catch (Exception ignored)
        {
        }




    }

    private void lessonSpinnerLoad() {
        try {

            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lessn);
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
            lesson_selectSpinner.setAdapter(spinnerArrayAdapter);
        }
        catch (Exception ignored)
        {}
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
                  //  Toast.makeText(VideoPlay.this, s, Toast.LENGTH_SHORT).show();
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
        if (StudentClass.equals("11"))  {
            String[] list;
            if (studentdiv.equals("C")) {

                list = SubjectList.class11_COMR;
            } else if (studentdiv.equals("H")) {
                list = SubjectList.class11_HUM;
            } else if (studentdiv.equals("S")) {
                list = SubjectList.class11_SCI;
            } else {
                list = SubjectList.class11_COMM;
            }

            subjectAdapter = new SubjectAdapter(list, subj_list, new SubjectAdapter.OnItemClickListener() {
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
            public void onItemClick(ClassVideo item) {
                notescalculate(item);
              //  Toast.makeText(VideoPlay.this, "playing -" + item.name, Toast.LENGTH_SHORT).show();
                details = item.getData().getDescription();
                media(item.getData().getFiles().get(0).getLink());
                vURL = item.getData().getFiles().get(0).getLink();
                download.setVisibility(View.VISIBLE);
                fullscreen.setVisibility(View.VISIBLE);
                String regno=GlobalData.regno;
                Glide.with(VideoPlay.this).asGif().load(R.raw.download).into(download);
                String[] time=String.valueOf(item.getData().getCreatedTime()).split("\\s+");
                final RetrofitClientInstance.GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(RetrofitClientInstance.GetDataService.class);

                Call<VideoUp> saveVideo = service.saveVideo(regno,item.getData().getName(),item.getData().getFiles().get(0).getLink(),item.getData().getDescription(),item.getData().getPictures().getSizes().get(selectedVideo.getData().getPictures().getSizes().size()-1).getLink(),time[3]);
                saveVideo.enqueue(new Callback<VideoUp>() {
                    @Override
                    public void onResponse(Call<VideoUp> call, retrofit2.Response<VideoUp> response) {
                        //Toast.makeText(VideoPlay.this, "Video Uploaded", Toast.LENGTH_SHORT).show();
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

    private boolean notescalculate(ClassVideo item) {
        String[] desc=item.getData().getDescription().split("-");
        if (desc.length>3){
            notes.setVisibility(View.VISIBLE);
            String url="";

            for (int i=3;i<desc.length;i++){
                 if (i==3)
                     url=desc[i];
                 else
                     url=url+"-"+desc[i];

            }
            documenturl=url;
            Toast.makeText(this, "Notes Present", Toast.LENGTH_SHORT).show();
            return true;
        }
        else {
            notes.setVisibility(View.GONE);
            return false;
        }
    }

    private void subj_wise_videoListAdapter(ArrayList selectedVideoList) {
        nowShowing = selectedVideoList;
        selectedVideoList = getLesson(selectedVideoList);

        video_play_list.setAdapter(new Class_videoAdapter(VideoPlay.this, selectedVideoList, video_play_list, new Class_videoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final ClassVideo item) {
                selectedVideo=item;
               // notescalculate(item);

                new AlertDialog.Builder(VideoPlay.this)
                    .setTitle("Video Options")
                    .setMessage("Please select one to continue...")

                    // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    .setPositiveButton("Notes ", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                           if (notescalculate(item)) {
                               String[] desc = documenturl.split("/");
                               int len = desc.length;
                               document_name = desc[len - 1];
                               String loc = dbb.DocumentList(document_name);
                               if (!loc.equals("false")) {
                                   File data = new File(loc);
                                   Intent target = new Intent(Intent.ACTION_VIEW);
                                   target.setDataAndType(Uri.fromFile(data),"application/pdf");
                                   target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

                                   Intent intent = Intent.createChooser(target, "Open File");
                                   try {
                                       mProgressDialog.cancel();
                                       startActivity(intent);
                                   } catch (ActivityNotFoundException e) {
                                       // Instruct the user to install a PDF reader here, or something
                                   }
                               } else {
                                   GetDocuments getDocuments = new GetDocuments();
                                   getDocuments.execute();
                               }

                           }
                           else {
                               Toast.makeText(VideoPlay.this, "No Notes added", Toast.LENGTH_SHORT).show();
                           }
                        }
                    })

                    // A null listener allows the button to dismiss the dialog and take no further action.
                    .setNegativeButton("Play", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            details = item.getData().getDescription();
                            media(item.getData().getFiles().get(0).getLink());
                            vURL = item.getData().getFiles().get(0).getLink();
                            download.setVisibility(View.VISIBLE);
                            fullscreen.setVisibility(View.VISIBLE);
                            String regno=GlobalData.regno;
                            Glide.with(VideoPlay.this).asGif().load(R.raw.download).into(download);
                            String[] time=String.valueOf(item.getData().getCreatedTime()).split("T");
                            final RetrofitClientInstance.GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(RetrofitClientInstance.GetDataService.class);

                            Call<VideoUp> saveVideo = service.saveVideo(regno,item.getData().getName(),item.getData().getFiles().get(0).getLink(),item.getData().getDescription(),item.getData().getPictures().getSizes().get(selectedVideo.getData().getPictures().getSizes().size()-1).getLink(),time[1]);
                            saveVideo.enqueue(new Callback<VideoUp>() {
                                @Override
                                public void onResponse(Call<VideoUp> call, Response<VideoUp> response) {
                                    //Toast.makeText(VideoPlay.this, "Video Uploaded", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailure(Call<VideoUp> call, Throwable t) {
                                    Toast.makeText(VideoPlay.this, "Error", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();


            }


        }));
        LinearLayoutManager added_liste_adapterlayoutManager
            = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        video_play_list.setLayoutManager(added_liste_adapterlayoutManager);

    }

    private ArrayList getLesson(ArrayList selectedVideoList) {
        ArrayList returnVideo = new ArrayList();
        try {



            String lesn = "";
            if (StudentClass.equals("12") || StudentClass.equals("11") || StudentClass.equals("10")) {
                for (int i = 0; i < selectedVideoList.size(); i++) {
                   ClassVideo video = (ClassVideo) selectedVideoList.get(i);
                    lesn = (video.getData().getName().substring(3));
                    lesn = lesn.substring(0, Math.min(lesn.length(), 2));
                    if (lesn.equals(lesson_flag)) {
                        returnVideo.add(selectedVideoList.get(i));
                    }

                }

            } else if (StudentClass.equals("6")) {

                String school = GlobalData.regno.substring(0, Math.min(GlobalData.regno.length(), 3));
                if (school.equals("AND")) {
                    for (int i = 0; i < selectedVideoList.size(); i++) {
                       ClassVideo video = (ClassVideo) selectedVideoList.get(i);
                        if (video.getData().getName().substring(0, Math.min(video.getData().getName().length(), 1)).equals("N")) {
                            lesn = (video.getData().getName().substring(3));
                            lesn = lesn.substring(0, Math.min(lesn.length(), 2));
                            if (lesn.equals(lesson_flag)) {
                                returnVideo.add(selectedVideoList.get(i));
                            }
                        } else {
                            lesn = (video.getData().getName().substring(2));
                            lesn = lesn.substring(0, Math.min(lesn.length(), 2));
                            if (lesn.equals(lesson_flag)) {
                                returnVideo.add(selectedVideoList.get(i));
                            }
                        }


                    }

                } else {
                    for (int i = 0; i < selectedVideoList.size(); i++) {
                       ClassVideo video = (ClassVideo) selectedVideoList.get(i);
                        lesn = (video.getData().getName().substring(2));
                        lesn = lesn.substring(0, Math.min(lesn.length(), 2));
                        if (lesn.equals(lesson_flag)) {
                            returnVideo.add(selectedVideoList.get(i));
                        }

                    }
                }

            } else if (StudentClass.equals("7")) {

                String school = GlobalData.regno.substring(0, Math.min(GlobalData.regno.length(), 3));
                if (school.equals("AND")) {
                    for (int i = 0; i < selectedVideoList.size(); i++) {
                       ClassVideo video = (ClassVideo) selectedVideoList.get(i);
                        if (video.getData().getName().substring(0, Math.min(video.getData().getName().length(), 1)).equals("N")) {
                            lesn = (video.getData().getName().substring(3));
                            lesn = lesn.substring(0, Math.min(lesn.length(), 2));
                            if (lesn.equals(lesson_flag)) {
                                returnVideo.add(selectedVideoList.get(i));
                            }
                        } else {
                            lesn = (video.getData().getName().substring(2));
                            lesn = lesn.substring(0, Math.min(lesn.length(), 2));
                            if (lesn.equals(lesson_flag)) {
                                returnVideo.add(selectedVideoList.get(i));
                            }
                        }


                    }

                } else {
                    for (int i = 0; i < selectedVideoList.size(); i++) {
                       ClassVideo video = (ClassVideo) selectedVideoList.get(i);
                        lesn = (video.getData().getName().substring(2));
                        lesn = lesn.substring(0, Math.min(lesn.length(), 2));
                        if (lesn.equals(lesson_flag)) {
                            returnVideo.add(selectedVideoList.get(i));
                        }

                    }
                }

            } else if (StudentClass.equals("8")) {

                String school = GlobalData.regno.substring(0, Math.min(GlobalData.regno.length(), 3));
                if (school.equals("AND")) {
                    for (int i = 0; i < selectedVideoList.size(); i++) {
                       ClassVideo video = (ClassVideo) selectedVideoList.get(i);
                        if (video.getData().getName().substring(0, Math.min(video.getData().getName().length(), 1)).equals("N")) {
                            lesn = (video.getData().getName().substring(3));
                            lesn = lesn.substring(0, Math.min(lesn.length(), 2));
                            if (lesn.equals(lesson_flag)) {
                                returnVideo.add(selectedVideoList.get(i));
                            }
                        } else {
                            lesn = (video.getData().getName().substring(2));
                            lesn = lesn.substring(0, Math.min(lesn.length(), 2));
                            if (lesn.equals(lesson_flag)) {
                                returnVideo.add(selectedVideoList.get(i));
                            }
                        }


                    }

                } else {
                    for (int i = 0; i < selectedVideoList.size(); i++) {
                       ClassVideo video = (ClassVideo) selectedVideoList.get(i);
                        lesn = (video.getData().getName().substring(2));
                        lesn = lesn.substring(0, Math.min(lesn.length(), 2));
                        if (lesn.equals(lesson_flag)) {
                            returnVideo.add(selectedVideoList.get(i));
                        }

                    }
                }

            } else {

                for (int i = 0; i < selectedVideoList.size(); i++) {
                   ClassVideo video = (ClassVideo) selectedVideoList.get(i);
                    lesn = (video.getData().getName().substring(2));
                    lesn = lesn.substring(0, Math.min(lesn.length(), 2));
                    if (lesn.equals(lesson_flag)) {
                        returnVideo.add(selectedVideoList.get(i));
                    }

                }

            }


        }
        catch (Exception e){
            FirebaseCrashlytics.getInstance().log(String.valueOf(e));
            Toast.makeText(this, "Network Error..", Toast.LENGTH_SHORT).show();
        }
        return returnVideo;
    }

    private void getVideosList(String s) {
        notes.setVisibility(View.GONE);
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
               ClassVideo video = (ClassVideo) videoList.get(i);
                subj = (video.getData().getName().substring(5));
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
               ClassVideo video = (ClassVideo) videoList.get(i);
                subj = (video.getData().getName().substring(5));
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
               ClassVideo video = (ClassVideo) videoList.get(i);
                subj = (video.getData().getName().substring(5));
                subj = subj.substring(0, Math.min(subj.length(), 3));
                if (subj.equals(subCode)) {
                    subject_videoList.add(videoList.get(i));
                }

            }
            subj_wise_videoListAdapter(subject_videoList);


        }
    }

    private void class11(String subject) {
        String subCode = "";
        if (studentdiv.equals("S")) {
            for (int i = 0; i < SubjectList.class11_SCI.length; i++) {
                if (SubjectList.class11_SCI[i].equals(subject)) {
                    subCode = SubjectList.class11_SCI_code[i];
                }

            }
            ArrayList subject_videoList = new ArrayList();
            ArrayList videoList = new ArrayList();
            videoList = GlobalData.addedVideos;
            String subj = "";
            for (int i = 0; i < videoList.size(); i++) {
                ClassVideo video = (ClassVideo) videoList.get(i);
                subj = (video.getData().getName().substring(5));
                subj = subj.substring(0, Math.min(subj.length(), 3));
                if (subj.equals(subCode)) {
                    subject_videoList.add(videoList.get(i));
                }

            }
            subj_wise_videoListAdapter(subject_videoList);


        } else if (studentdiv.equals("H")) {
            for (int i = 0; i < SubjectList.class11_HUM.length; i++) {
                if (SubjectList.class11_HUM[i].equals(subject)) {
                    subCode = SubjectList.class11_HUM_code[i];
                }

            }
            ArrayList subject_videoList = new ArrayList();
            ArrayList videoList = new ArrayList();
            videoList = GlobalData.addedVideos;
            String subj = "";
            for (int i = 0; i < videoList.size(); i++) {
                ClassVideo video = (ClassVideo) videoList.get(i);
                subj = (video.getData().getName().substring(5));
                subj = subj.substring(0, Math.min(subj.length(), 3));
                if (subj.equals(subCode)) {
                    subject_videoList.add(videoList.get(i));
                }

            }
            subj_wise_videoListAdapter(subject_videoList);


        } else {
            for (int i = 0; i < SubjectList.class11_COMR.length; i++) {
                if (SubjectList.class11_COMR[i].equals(subject)) {
                    subCode = SubjectList.class11_COMR_code[i];
                }

            }
            ArrayList subject_videoList = new ArrayList();
            ArrayList videoList = new ArrayList();
            videoList = GlobalData.addedVideos;
            String subj = "";
            for (int i = 0; i < videoList.size(); i++) {
                ClassVideo video = (ClassVideo) videoList.get(i);
                subj = (video.getData().getName().substring(5));
                subj = subj.substring(0, Math.min(subj.length(), 3));
                if (subj.equals(subCode)) {
                    subject_videoList.add(videoList.get(i));
                }

            }
            subj_wise_videoListAdapter(subject_videoList);


        }
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
           ClassVideo video = (ClassVideo) videoList.get(i);
            subj = (video.getData().getName().substring(5));
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
           ClassVideo video = (ClassVideo) videoList.get(i);
            subj = (video.getData().getName().substring(4));
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
           ClassVideo video = (ClassVideo) videoList.get(i);
            subj = (video.getData().getName().substring(4));
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
               ClassVideo video = (ClassVideo) videoList.get(i);
                if (video.getData().getName().substring(0, Math.min(video.getData().getName().length(), 1)).equals("N")){
                    subj = (video.getData().getName().substring(5));
                    subj = subj.substring(0, Math.min(subj.length(), 3));
                    if (subj.equals(subCode)) {
                        subject_videoList.add(videoList.get(i));
                    }
                }
                else {
                    subj = (video.getData().getName().substring(4));
                    subj = subj.substring(0, Math.min(subj.length(), 3));
                    if (subj.equals("EGG")||subj.equals("HNG")||subj.equals("MAT")||subj.equals("CMP")){
                        if (subj.equals(subCode)) {
                            subject_videoList.add(videoList.get(i));
                        }
                    }


                }
              /*  subj = (video.getData().getName().substring(5));
                subj = subj.substring(0, Math.min(subj.length(), 3));
                if (subj.equals(subCode)) {
                    subject_videoList.add(videoList.get(i));
                }*/

            }
        }
        else {
            for (int i = 0; i < videoList.size(); i++) {
               ClassVideo video = (ClassVideo) videoList.get(i);
                subj = (video.getData().getName().substring(4));
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
               ClassVideo video = (ClassVideo) videoList.get(i);
                if (video.getData().getName().substring(0, Math.min(video.getData().getName().length(), 1)).equals("N")){
                    subj = (video.getData().getName().substring(5));
                    subj = subj.substring(0, Math.min(subj.length(), 3));
                    if (subj.equals(subCode)) {
                        subject_videoList.add(videoList.get(i));
                    }
                }
                else {
                    subj = (video.getData().getName().substring(4));
                    subj = subj.substring(0, Math.min(subj.length(), 3));
                    if (subj.equals("EGG")||subj.equals("HNG")||subj.equals("MAT")||subj.equals("CMP")){
                        if (subj.equals(subCode)) {
                            subject_videoList.add(videoList.get(i));
                        }
                    }


                }
             /*   subj = (video.getData().getName().substring(5));
                subj = subj.substring(0, Math.min(subj.length(), 3));
                if (subj.equals(subCode)) {
                    subject_videoList.add(videoList.get(i));
                }*/

            }
        }
        else {
            for (int i = 0; i < videoList.size(); i++) {
               ClassVideo video = (ClassVideo) videoList.get(i);
                subj = (video.getData().getName().substring(4));
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
               ClassVideo video = (ClassVideo) videoList.get(i);
                if (video.getData().getName().substring(0, Math.min(video.getData().getName().length(), 1)).equals("N")){
                    subj = (video.getData().getName().substring(5));
                    subj = subj.substring(0, Math.min(subj.length(), 3));
                    if (subj.equals(subCode)) {
                        subject_videoList.add(videoList.get(i));
                    }
                }
                else {
                    subj = (video.getData().getName().substring(4));
                    subj = subj.substring(0, Math.min(subj.length(), 3));
                    if (subj.equals("EGG")||subj.equals("HNG")||subj.equals("MAT")||subj.equals("CMP")){
                        if (subj.equals(subCode)) {
                            subject_videoList.add(videoList.get(i));
                        }
                    }


                }
              /*  subj = (video.getData().getName().substring(5));
                subj = subj.substring(0, Math.min(subj.length(), 3));
                if (subj.equals(subCode)) {
                    subject_videoList.add(videoList.get(i));
                }*/

            }
        }
        else {
            for (int i = 0; i < videoList.size(); i++) {
               ClassVideo video = (ClassVideo) videoList.get(i);
                subj = (video.getData().getName().substring(4));
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
           ClassVideo video = (ClassVideo) videoList.get(i);
            subj = (video.getData().getName().substring(4));
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
           ClassVideo video = (ClassVideo) videoList.get(i);
            subj = (video.getData().getName().substring(4));
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
           ClassVideo video = (ClassVideo) videoList.get(i);
            subj = (video.getData().getName().substring(4));
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
           ClassVideo video = (ClassVideo) videoList.get(i);
            subj = (video.getData().getName().substring(4));
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
           ClassVideo video = (ClassVideo) videoList.get(i);
            subj = (video.getData().getName().substring(4));
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
           ClassVideo video = (ClassVideo) videoList.get(i);
            subj = (video.getData().getName().substring(4));
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
           ClassVideo video = (ClassVideo) videoList.get(i);
            subj = (video.getData().getName().substring(4));
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


        //Use a media controller so that you can scroll theClassVideo contents
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

    public void isStoragePermissionGranted() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted");
                return;
            } else {

                Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return;
            }
        }
        //permission is automatically granted on sdk<23 upon installation
        Log.v(TAG,"Permission is granted");

    }

    @Override
    public void vimeoURLCallback(String callback) {

    }

    @Override
    public void videoExceptionCallback(VimeoException exceptionCallback) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Log.v(TAG,"Permission: "+permissions[0]+ "was "+grantResults[0]);
            //resume tasks needing this permission
        }
    }

    private void downloadDocument(byte[] bytes) throws IOException {
        String rootDir;
        File rootFile;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                rootFile = new File(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), File.separator + "Fil");
            else
                rootFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    + File.separator + "Fil");
            boolean a=rootFile.mkdirs();
            File data = new File(rootFile, document_name);
            OutputStream op = new FileOutputStream(data);
            op.write(bytes);

            // dir_name = rootDir;
            String location = rootFile.toString();
            dbb.insertDocument(document_name,String.valueOf(rootFile),String.valueOf(data),userid,extra1);

            Intent target = new Intent(Intent.ACTION_VIEW);
            target.setDataAndType(Uri.fromFile(data),"application/pdf");
            target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

            Intent intent = Intent.createChooser(target, "Open File");
            try {
                mProgressDialog.cancel();
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                // Instruct the user to install a PDF reader here, or something
            }


        } catch (Exception E) {
            String a = String.valueOf(E);
            Toast.makeText(this, a, Toast.LENGTH_LONG).show();
        }

    }


    private class GetDocuments extends AsyncTask<Void, String, String> {
        String a = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(Void... voids) {

            StorageReference gsReference = null;
            String a = String.valueOf(1);
            try {
                gsReference = storage.getReferenceFromUrl(documenturl);
                final long FIVE_MEGABYTE = 5120 * 5120;
                gsReference.getBytes(FIVE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        String a = String.valueOf(1);
                        try {
                            downloadDocument(bytes);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        String a = String.valueOf(1);

                    }
                });




            } catch (Exception e) {
                e.printStackTrace();
                return "False";
            }

            return "";
        }

        @Override
        protected void onPostExecute(String onlineVersion) {
            super.onPostExecute(onlineVersion);

            if (onlineVersion.equals("False")) {
                Toast.makeText(VideoPlay.this, "Error link", Toast.LENGTH_SHORT).show();
                mProgressDialog.cancel();
            }
        }

    }

    private void configVimeo(ClassVideo selectedVideo) {

        final Configuration.Builder configBuilder =
            new Configuration.Builder("70b8a941d1f7a9950d7c09d3abf322ba")
                .setCacheDirectory(this.getCacheDir());
        VimeoClient.initialize(configBuilder.build());


        final VimeoClient mApiClient = VimeoClient.getInstance();
        // ---- Client Credentials Auth ----
        if (mApiClient.getVimeoAccount().getAccessToken() == null) {
            VimeoClient.getInstance().authorizeWithClientCredentialsGrant(new AuthCallback() {
                @Override
                public void success() {
                    String accessToken = VimeoClient.getInstance().getVimeoAccount().getAccessToken();
                    Configuration.Builder configBuilder =
                        new Configuration.Builder(accessToken);
                    VimeoClient.initialize(configBuilder.build());


                }

                @Override
                public void failure(VimeoError error) {
                    String errorMessage = error.getDeveloperMessage();

                }
            });
        }

        if (mApiClient.getVimeoAccount().getAccessToken() == null) {
            Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
        }

        DwndVideo();
    }

    private void DwndVideo() {
        DwndVideos DB = new  DwndVideos();
        DB.execute("");
    }

    private class DwndVideos extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {


        }

        @Override
        protected String doInBackground(String... strings) {
            String uri ;

            uri =  selectedVideo.getData().getUri();
            VimeoClient.getInstance().fetchNetworkContent(uri, new ModelCallback<Video>(Video.class) {
                @Override
                public void success(Video video) {



                    String a="";
                    String url = video.download.get(0).link;
                    for (int i=0;i<video.download.size();i++){
                        if (video.download.get(i).getHeight()==480){
                            download_url = video.download.get(i).link;
                        }
                    }
                    download_name = selectedVideo.getData().getName();

                    nametostore = selectedVideo.getData().getName();
                    extra1 = selectedVideo.getData().getPictures().getSizes().get(selectedVideo.getData().getPictures().getSizes().size()-1).getLink();
                    extra2=selectedVideo.getData().getDescription();
                    download();
                    // use the video
                }

                @Override
                public void failure(VimeoError error) {
                    String a="";
                    // voice the error
                }
            });

            return null;

        }


    }
}