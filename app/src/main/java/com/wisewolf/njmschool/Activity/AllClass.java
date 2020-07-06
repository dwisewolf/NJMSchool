package com.wisewolf.njmschool.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.vimeo.networking.Configuration;
import com.vimeo.networking.VimeoClient;
import com.vimeo.networking.callbacks.AuthCallback;
import com.vimeo.networking.callbacks.ModelCallback;
import com.vimeo.networking.model.Video;
import com.vimeo.networking.model.VideoList;
import com.vimeo.networking.model.error.VimeoError;
import com.wisewolf.njmschool.Adapter.PackageVideoAdapter;
import com.wisewolf.njmschool.R;

import java.util.ArrayList;

public class AllClass extends AppCompatActivity {
    int pageLength=0,pageWidth;
    RecyclerView video_list;
    ImageView thumbnail,fulscreen;
    TextView desc;
    int fromstart=1;
    ProgressDialog mProgressDialog;
    VideoView videoPlay;
    String schoolAnthem,vURL;
    ArrayList allVideos=new ArrayList();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();
        setContentView(R.layout.all_class);
        video_list =  findViewById(R.id.videoListMycourse);
        fulscreen =  findViewById(R.id.fullscreen);
        fulscreen.setVisibility(View.GONE);
        fulscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AllClass.this, FullScreen2.class);
                intent.putExtra("url", vURL);
                intent.putExtra("page", "ac");
                startActivity(intent);
            }
        });

        thumbnail =  findViewById(R.id.thumbnail);
        desc =  findViewById(R.id.descp);
        videoPlay =  findViewById(R.id.videoView);
        thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               mediaPlay();
            }
        });

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
    }

    private void mediaPlay() {
        vURL=schoolAnthem;
        fulscreen.setVisibility(View.VISIBLE);
        mProgressDialog = new ProgressDialog(AllClass.this);
        mProgressDialog.setMessage(" Please wait . . .");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(true);
        if (!(allVideos ==null)){

            if (fromstart==1){


                mProgressDialog.show();


                //Use a media controller so that you can scroll theClassVideo contents
                //and also to pause, start the video.
                final MediaController mediaController = new MediaController(this);

                //  mediaController.setAnchorView(videoView);

                videoPlay.setMediaController(mediaController);
                videoPlay.setVideoURI(Uri.parse(schoolAnthem));
                videoPlay.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(final MediaPlayer mediaPlayer) {
                        mediaController.setAnchorView(videoPlay);
                        // videoView.start();
                        mediaPlayer.start();
                        mProgressDialog.cancel();
                        thumbnail.setVisibility(View.GONE);
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
            else {


                mProgressDialog.show();


                //Use a media controller so that you can scroll theClassVideo contents
                //and also to pause, start the video.
                final MediaController mediaController = new MediaController(this);

                //  mediaController.setAnchorView(videoView);

                videoPlay.setMediaController(mediaController);
                videoPlay.setVideoURI(Uri.parse(schoolAnthem));
                videoPlay.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(final MediaPlayer mediaPlayer) {
                        mediaController.setAnchorView(videoPlay);
                        // videoView.start();
                        mediaPlayer.start();
                        mProgressDialog.cancel();
                        thumbnail.setVisibility(View.GONE);
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
        }
    }

    private void download() {
        Downback DB = new Downback();
        DB.execute("");
    }
    private class Downback extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            calcHandView();
        }

        @Override
        protected void onPostExecute(String s) {


        }

        @Override
        protected String doInBackground(String... strings) {
            String uri ;
            final int i=pageWidth;
            uri = "/me/projects/2102320/videos";
            VimeoClient.getInstance().fetchNetworkContent(uri, new ModelCallback<VideoList>(VideoList.class) {
                @Override
                public void success(VideoList videoList) {

                    if (videoList != null && videoList.data != null && !videoList.data.isEmpty()) {

                        ArrayList allVideoList= new ArrayList();
                        allVideoList=(videoList.data);
                        allVideos=(videoList.data);
                        disp(allVideos);
                        video_list.setAdapter(new PackageVideoAdapter(AllClass.this,allVideoList, video_list,i,pageLength, new PackageVideoAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(Video item) {
                                schoolAnthem=item.files.get(0).link;
                                fromstart=2;
                                desc.setText(item.description);
                                mediaPlay();
                            }
                        }));


                        LinearLayoutManager added_liste_adapterlayoutManager
                            = new LinearLayoutManager(AllClass.this, LinearLayoutManager.HORIZONTAL, false);
                        video_list.setLayoutManager(added_liste_adapterlayoutManager);

                    }
                }

                @Override
                public void failure(VimeoError error) {
                    String a=String.valueOf(error);
                }
            });
            return null;

        }


    }


    private void calcHandView() {
        Point size = new Point();  AllClass.this.getWindowManager().getDefaultDisplay().getSize(size);
        pageWidth=size.x;
        pageLength=size.y;
    }
    void disp(ArrayList allVideos){
        try {

            for (int i = 0; i < this.allVideos.size(); i++) {
                Video video= (Video) this.allVideos.get(i);
                if (video.name.equals("KDAG-ALL1")){
                    int x=video.pictures.sizes.size();
                    Glide.with(AllClass.this)
                        .load(video.pictures.sizes.get(x-1).linkWithPlayButton) // image url



                        .centerCrop()
                        .into( thumbnail);
                    desc.setText(video.description);
                    schoolAnthem=video.files.get(0).link;
                }

            }
        }
        catch (Exception e){
            String ex="";
        }
    }

    @Override
    public void onBackPressed() {
        fromstart=1;
        super.onBackPressed();
    }
}
