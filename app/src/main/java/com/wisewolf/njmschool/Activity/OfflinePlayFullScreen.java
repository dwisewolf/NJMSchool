package com.wisewolf.njmschool.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;

import com.wisewolf.njmschool.R;

import java.io.File;

public class OfflinePlayFullScreen extends AppCompatActivity {
    String file,name;
    VideoView videoFull;
    File delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_play_full_screen);
        videoFull=findViewById(R.id.OffvideoFull);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();

        Intent intent=getIntent();
        file=intent.getStringExtra("root");
        name=intent.getStringExtra("name");
        File rootFile = new File(file);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            rootFile = new File(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), File.separator + "Fil");
        else
            rootFile =new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                + File.separator + "Fil");

        File play=new File(rootFile,name+".mp4");
        delete=play;

        videoFull.setVideoPath(play.getAbsolutePath());

        MediaController mediaController = new MediaController(this);

        videoFull.setMediaController(mediaController);

        mediaController.setMediaPlayer(videoFull);

        videoFull.setVisibility(View.VISIBLE);

        videoFull.start();

    }

    @Override
    public void onBackPressed() {

        new AlertDialog.Builder(OfflinePlayFullScreen.this)
            .setTitle("SECURITY ALERT")
            .setMessage("Video Re encryption")

            // Specifying a listener allows you to take an action before dismissing the dialog.
            // The dialog is automatically dismissed when a dialog button is clicked.
            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    delete.delete();
                   finish();
                }
            })

            // A null listener allows the button to dismiss the dialog and take no further action.
            .setNegativeButton("Cancel", null)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show();
    }
}
