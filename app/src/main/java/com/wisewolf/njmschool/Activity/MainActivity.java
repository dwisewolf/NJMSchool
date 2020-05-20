package com.wisewolf.njmschool.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.vimeo.networking.Configuration;
import com.vimeo.networking.VimeoClient;
import com.vimeo.networking.callbacks.AuthCallback;
import com.vimeo.networking.callbacks.ModelCallback;
import com.vimeo.networking.model.Video;
import com.vimeo.networking.model.VideoList;
import com.vimeo.networking.model.error.VimeoError;
import com.wisewolf.njmschool.Globals.GlobalData;
import com.wisewolf.njmschool.ObjectSerialiser;
import com.wisewolf.njmschool.R;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ImageView education,logo,load;
    String uri = "/me/videos";
    TextView wait;
    String  lastpage="";


    ArrayList allVideoList = new ArrayList();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        education = findViewById(R.id.education_gif);
        logo=findViewById(R.id.logo_id);
        load=findViewById(R.id.loading);
        wait=findViewById(R.id.wait);
        wait.setText("Project Configuration. Please wait");

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                Glide.with(MainActivity.this).asGif().load(R.raw.educat).into(education);
                Glide.with(MainActivity.this).asGif().load(R.raw.loading).into(load);

            }
        });

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();

 /*       Intent msgIntent = new Intent(MainActivity.this, VideoLinkService.class);
        startService(msgIntent);*/

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

        download();




        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent msgIntent = new Intent(MainActivity.this, SignUp.class);
                startActivity(msgIntent);
            }
        });

    }

    private void download() {
        Downback DB = new Downback();
        DB.execute("");
    }

    private class Downback extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
             GlobalData.vimeoclient=VimeoClient.getInstance();
            VimeoClient.getInstance().fetchNetworkContent(uri, new ModelCallback<VideoList>(VideoList.class) {
                @Override
                public void success(VideoList videoList) {

                    if (videoList != null && videoList.data != null && !videoList.data.isEmpty()) {
                        ArrayList allVideoList = new ArrayList();
                        if (GlobalData.allVideoList != null) {
                            allVideoList = GlobalData.allVideoList;
                        }

                        allVideoList.addAll(videoList.data);

                        GlobalData.allVideoList = (allVideoList);


                        allVideoList.addAll(videoList.data);
                        String last_num = videoList.paging.last;
                        String[] parts = last_num.split("=");
                        int x = Integer.parseInt(parts[1]);
                        lastpage=parts[1];
                        Log.d("eff", allVideoList.toString());
                        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);

                        String size = sharedPrefs.getString("size", "");

                        if (!size.equals(String.valueOf(videoList.total))) {
                            for (int i = 2; i <= x; i++) {
                                String uri = String.valueOf(i);
                                uri = "/me/videos?page=" + uri;
                                callApi(uri,i);
                            }
                        }
                        else {
                            Intent msgIntent = new Intent(MainActivity.this, SignUp.class);
                            startActivity(msgIntent);
                        }



                    }
                }

                @Override
                public void failure(VimeoError error) {

                }
            });
            return null;

        }


    }

    void callApi(final String url, final int lp){

        VimeoClient.getInstance().fetchNetworkContent(url, new ModelCallback<VideoList>(VideoList.class) {
            @Override
            public void success(VideoList videoList) {

                if (videoList != null && videoList.data != null && !videoList.data.isEmpty()) {

                    ArrayList allVideoList=GlobalData.allVideoList;
                    allVideoList.addAll(videoList.data);

                    GlobalData.allVideoList=(allVideoList);
                    SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                    SharedPreferences.Editor editor = sharedPrefs.edit();
                    Gson gson = new Gson();

                    String json = gson.toJson(allVideoList);

                    editor.putString("Allvideolist", json);
                    editor.putString("size",String.valueOf(videoList.total));
                    try {
                        editor.putString("task", ObjectSerialiser.serialize(allVideoList));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    editor.apply();


                    wait.setText("Project Configuration. "+url);
                    if (String.valueOf(lp).equals(lastpage)){

                        wait.setText(" Done. ");
                        Intent msgIntent = new Intent(MainActivity.this, SignUp.class);
                        startActivity(msgIntent);
                    }

                }
            }

            @Override
            public void failure(VimeoError error) {

            }
        });



    }

}
