package com.wisewolf.njmschool;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;


import com.vimeo.android.deeplink.VimeoDeeplink;
import com.vimeo.networking.Configuration;
import com.vimeo.networking.VimeoClient;
import com.vimeo.networking.callbacks.AuthCallback;
import com.vimeo.networking.callbacks.ModelCallback;
import com.vimeo.networking.model.BaseResponseList;
import com.vimeo.networking.model.Video;
import com.vimeo.networking.model.VideoFile;
import com.vimeo.networking.model.VideoList;
import com.vimeo.networking.model.error.VimeoError;
import com.vimeo.networking.model.playback.Play;

import java.util.ArrayList;
import java.util.Collection;

public class VideoListing extends AppCompatActivity {


    private ProgressDialog mProgressDialog;
    Video videoTest;
    TextView topic;
    ArrayList allVideoList = new ArrayList();
    String uri = "/me/videos"; // obtained using one of the above methods

    private static final String SCOPE = "private public create edit delete interact";

    String clientId = "ff23a22f9ba21bbef3e55b819841253741a4efdb";
    String clientSecret = "9zi4jDuuoGuSxNmuieuKzS3zF48Bscj8IsSplyGnP0sruv4bNTY/oI61Rl9cYG66EeuB24noO+yl4BdtsM8AI2BHIgAPnIddAILMr1fZiaxCSE6mmZyM32y60Wc5IJaS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_listing);
        media("android.resource://" + getPackageName() + "/" + R.raw.v1);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;

        actionBar.hide();

        topic = findViewById(R.id.topic);
        final Configuration.Builder configBuilder =
            new Configuration.Builder("70b8a941d1f7a9950d7c09d3abf322ba")
                .setCacheDirectory(this.getCacheDir());
        VimeoClient.initialize(configBuilder.build());
        mProgressDialog = new ProgressDialog(this);


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

                    toast("Client Credentials Authorization Success with Access Token: " + accessToken);
                }

                @Override
                public void failure(VimeoError error) {
                    String errorMessage = error.getDeveloperMessage();
                    toast("Client Credentials Authorization Failure: " + errorMessage);
                }
            });
        }

        if (mApiClient.getVimeoAccount().getAccessToken() == null) {
            Toast.makeText(this, "ds", Toast.LENGTH_SHORT).show();
        }


        VimeoClient.getInstance().fetchNetworkContent(uri, new ModelCallback<VideoList>(VideoList.class) {
            @Override
            public void success(VideoList videoList) {

                if (videoList != null && videoList.data != null && !videoList.data.isEmpty()) {
                    videoTest = videoList.data.get(1);
                    allVideoList.addAll(videoList.data);
                    String last_num=videoList.paging.last;
                    String[] parts = last_num.split("=");
                    int x=Integer.parseInt(parts[1]);
                    Log.d("eff",allVideoList.toString());

                    for (int i=2;i<=x;i++){
                        String uri=String.valueOf(i);
                        uri="/me/videos?page="+uri;
                        callApi(uri);
                    }

                    media(videoList.data.get(1).files.get(0).link);
                }
            }

            @Override
            public void failure(VimeoError error) {
                Toast.makeText(VideoListing.this, "e", Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void toast(String string) {
        Toast.makeText(VideoListing.this, string, Toast.LENGTH_SHORT).show();
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

    void callApi(final String url){

        VimeoClient.getInstance().fetchNetworkContent(url, new ModelCallback<VideoList>(VideoList.class) {
            @Override
            public void success(VideoList videoList) {

                if (videoList != null && videoList.data != null && !videoList.data.isEmpty()) {

                    allVideoList.addAll(videoList.data);

                    Toast.makeText(VideoListing.this, "calling"+url, Toast.LENGTH_SHORT).show();


                }
            }

            @Override
            public void failure(VimeoError error) {
                Toast.makeText(VideoListing.this, "e", Toast.LENGTH_SHORT).show();
            }
        });



    }



}