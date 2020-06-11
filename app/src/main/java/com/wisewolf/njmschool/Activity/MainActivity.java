package com.wisewolf.njmschool.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
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

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.vimeo.networking.Configuration;
import com.vimeo.networking.VimeoClient;
import com.vimeo.networking.callbacks.AuthCallback;
import com.vimeo.networking.callbacks.ModelCallback;
import com.vimeo.networking.model.VideoList;
import com.vimeo.networking.model.error.VimeoError;
import com.wisewolf.njmschool.Globals.GlobalData;
import com.wisewolf.njmschool.ObjectSerialiser;
import com.wisewolf.njmschool.R;

import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.ArrayList;

import static android.net.sip.SipErrorCode.TIME_OUT;

public class MainActivity extends AppCompatActivity {
    private static int TIME_OUT = 2000;
    ImageView education, logo, load;
    String uri = "/me/videos";
    TextView wait;
    String lastpage = "";
    String currentVersion,new_Version;

    ArrayList allVideoList_1stcall = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        education = findViewById(R.id.education_gif);
        logo = findViewById(R.id.logo_id);
        load = findViewById(R.id.loading);
        wait = findViewById(R.id.wait);
        wait.setVisibility(View.GONE);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

             //   Glide.with(MainActivity.this).asGif().load(R.raw.school).into(education);
           //    Glide.with(MainActivity.this).asGif().load(R.raw.loading).into(load);
                try {
                    currentVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;


                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }


            }
        });

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();

 /*       Intent msgIntent = new Intent(MainActivity.this, VideoLinkService.class);
        startService(msgIntent);*/



        if (isNetworkAvailable()) {

            GetVersionCode getVersionCode=new GetVersionCode();
            getVersionCode.execute();


        } else {
            new AlertDialog.Builder(MainActivity.this)
                .setTitle("OOPS....")
                .setMessage("You are not connected to INTERNET..")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton("OFFLINE", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent msgIntent = new Intent(MainActivity.this, OfflineScreen.class);
                        startActivity(msgIntent);

                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton("CLOSE", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
        }



    }







    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
            = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private class GetVersionCode extends AsyncTask<Void, String, String> {
        @Override
        protected String doInBackground(Void... voids) {

            String newVersion = null;
            try {
                newVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=" + MainActivity.this.getPackageName() + "&hl=it")
                    .timeout(30000)
                    .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                    .referrer("http://www.google.com")
                    .get()
                    .select(".hAyfc .htlgb")
                    .get(7)
                    .ownText();
                new_Version=newVersion;
                return newVersion;
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String onlineVersion) {
            super.onPostExecute(onlineVersion);
            Log.d("update", "Current version " + currentVersion + "playstore version " + onlineVersion);
            if (onlineVersion != null && !onlineVersion.isEmpty()) {
                if (Float.parseFloat(currentVersion) < Float.parseFloat(onlineVersion)) {
                    final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                    }
                }
                else {
                    GotoActivity();
                }

            }
        }

    }

    private void GotoActivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(MainActivity.this, SignUp.class);
                startActivity(i);
                finish();
            }
        }, TIME_OUT);
    }
}
