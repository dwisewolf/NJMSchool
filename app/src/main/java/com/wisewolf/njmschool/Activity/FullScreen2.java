package com.wisewolf.njmschool.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.VideoView;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.wisewolf.njmschool.R;

public class FullScreen2 extends AppCompatActivity implements  Player.EventListener{
    ProgressBar progressBar;

    PlayerView exoplayerView;

    private String mVedioUrl;
    SimpleExoPlayer player;
    Handler mHandler;
    Runnable mRunnable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();
        exoplayerView=findViewById(R.id.exoplayerView_full);
        progressBar=findViewById(R.id.progressBar_ful);

        Intent intent=getIntent();
        String vURL=intent.getStringExtra("url");
        mVedioUrl=vURL;
        setUp();

    }




    private void setUp() {
        initializePlayer();
        if (mVedioUrl == null) {
            return;
        }
        buildMediaSource(Uri.parse(mVedioUrl));
    }

    private void initializePlayer() {
        if (player == null) {
            // 1. Create a default TrackSelector
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
            TrackSelector trackSelector =
                new DefaultTrackSelector(videoTrackSelectionFactory);
            // 2. Create the player
            player =
                ExoPlayerFactory.newSimpleInstance(this, trackSelector);
            exoplayerView.setPlayer(player);
        }
    }
    private void buildMediaSource(Uri mUri) {
        // Measures bandwidth during playback. Can be null if not required.
        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        // Produces DataSource instances through which media data is loaded.
       /* DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this,
                Util.getUserAgent(this, getString(R.string.app_name)), bandwidthMeter);
       */
        DefaultHttpDataSourceFactory httpDataSourceFactory = new DefaultHttpDataSourceFactory(
            Util.getUserAgent(this, getString(R.string.app_name)),
            null,
            DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS,
            DefaultHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS,
            true /* allowCrossProtocolRedirects */
        );


        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this,
            null /* listener */,
            httpDataSourceFactory
        );
        // This is the MediaSource representing the media to be played.
        MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
            .createMediaSource(mUri);
        // Prepare the player with the source.
        player.prepare(videoSource);

        player.setPlayWhenReady(true);
        player.addListener(this);

    }
    private void releasePlayer() {
        if (player != null) {
            player.release();
            player = null;
        }
    }
    private void pausePlayer() {
        if (player != null) {
            player.setPlayWhenReady(false);
            player.getPlaybackState();
        }
    }
    private void resumePlayer() {
        if (player != null) {
            player.setPlayWhenReady(true);
            player.getPlaybackState();
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        pausePlayer();
        if (mRunnable != null) {
            mHandler.removeCallbacks(mRunnable);
        }
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        resumePlayer();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }
    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {
    }
    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
    }
    @Override
    public void onLoadingChanged(boolean isLoading) {
    }
    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        switch (playbackState) {
            case Player.STATE_BUFFERING:
                progressBar.setVisibility(View.VISIBLE);
                break;
            case Player.STATE_ENDED:
                // Activate the force enable
                break;
            case Player.STATE_IDLE:
                break;
            case Player.STATE_READY:
                progressBar.setVisibility(View.GONE);
                break;
            default:
                // status = PlaybackStatus.IDLE;
                break;
        }
    }
    @Override
    public void onRepeatModeChanged(int repeatMode) {
    }
    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {
    }
    @Override
    public void onPlayerError(ExoPlaybackException error) {
    }
    @Override
    public void onPositionDiscontinuity(int reason) {
    }
    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
    }
    @Override
    public void onSeekProcessed() {
    }

}
