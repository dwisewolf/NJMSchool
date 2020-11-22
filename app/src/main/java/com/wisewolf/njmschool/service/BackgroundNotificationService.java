package com.wisewolf.njmschool.service;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.widget.Toast;

import com.vimeo.networking.Configuration;
import com.vimeo.networking.VimeoClient;
import com.vimeo.networking.callbacks.AuthCallback;
import com.vimeo.networking.callbacks.ModelCallback;
import com.vimeo.networking.model.Video;
import com.vimeo.networking.model.error.VimeoError;
import com.wisewolf.njmschool.Activity.OfflineScreenUser;
import com.wisewolf.njmschool.Activity.VideoPlay;
import com.wisewolf.njmschool.Database.OfflineDatabase;
import com.wisewolf.njmschool.Globals.GlobalData;
import com.wisewolf.njmschool.Models.ClassVideo;
import com.wisewolf.njmschool.NJMSSharedPreferences;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;

import androidx.annotation.Nullable;
import androidx.collection.CircularArray;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class BackgroundNotificationService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    private NotificationCompat.Builder notificationBuilder;
    private NotificationManager notificationManager;
    private NJMSSharedPreferences njmsSharedPreferences;
    String download_name,download_url, nametostore="",salt_name="",dir_name="",inv_name="",location="",userid="",extra1="",extra2="";
    OfflineDatabase dbb;
    String[] name;

    public BackgroundNotificationService() {
        super("service");
        njmsSharedPreferences=NJMSSharedPreferences.getInstance(this);
        userid = GlobalData.regno;
        dbb = new OfflineDatabase(this);
        nametostore=  njmsSharedPreferences.getData("nametostore");//i added
        extra1=    njmsSharedPreferences.getData("extra1");// i aaded
        extra2=    njmsSharedPreferences.getData("extra2");// i aaded
        download_name= njmsSharedPreferences.getData("download_name");
        download_url=  njmsSharedPreferences.getData("download_url");

        name=extra2.split("-");

    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel("id", "an", NotificationManager.IMPORTANCE_LOW);

            notificationChannel.setDescription("no sound");
            notificationChannel.setSound(null, null);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            notificationManager.createNotificationChannel(notificationChannel);
        }


        notificationBuilder = new NotificationCompat.Builder(this, "id")
                .setSmallIcon(android.R.drawable.stat_sys_download)
                .setContentTitle(name[0])
                .setContentText(name[1])
                .setDefaults(0)
                .setAutoCancel(true);
        notificationManager.notify(0, notificationBuilder.build());

        Downback DB = new Downback();
        DB.execute("");

        //goto downloaded screen on click of notification bar offline
        Intent resultIntent = new Intent(this, OfflineScreenUser.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        notificationBuilder.setContentIntent(pendingIntent);
       /* NotificationManager mNotifyMgr = (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);
        if (mNotifyMgr != null) {
            mNotifyMgr.notify(1, notificationBuilder.build());
        }*/
    }

    private  void updateNotification(int currentProgress) {
 //notificationBuilder.setContentTitle(extra2+"\n");
        notificationBuilder.setProgress(100, currentProgress, false);
        notificationBuilder.setContentText( name[1]+" Downloaded: " + currentProgress + "%");
        notificationManager.notify(0, notificationBuilder.build());
    }

    private void sendProgressUpdate(boolean downloadComplete) {

        Intent intent = new Intent(VideoPlay.PROGRESS_UPDATE);
        intent.putExtra("downloadComplete", downloadComplete);
        LocalBroadcastManager.getInstance(BackgroundNotificationService.this).sendBroadcast(intent);
    }

    private void onDownloadComplete(boolean downloadComplete) {
        sendProgressUpdate(downloadComplete);

        notificationManager.cancel(0);
        notificationBuilder.setSmallIcon(android.R.drawable.stat_sys_download_done);
        notificationBuilder.setProgress(0, 0, false);
        notificationBuilder.setContentText( name[1]+" Video Download Complete");
        notificationManager.notify(0, notificationBuilder.build());

    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        notificationManager.cancel(0);
    }

    @SuppressLint("StaticFieldLeak")
    private class Downback extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

           /* videoView.pause();
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();*/
            VideoPlay.dnFlag="qwertyuiop";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
           // mProgressDialog.dismiss();
            if (!s.equals("f")){
                dbb.insertinto(nametostore,salt_name,dir_name,inv_name,location,userid,extra1,extra2);
                String a="";
               VideoPlay.dnFlag="";
            }


        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
           /* mProgressDialog.setIndeterminate(false);
            mProgressDialog.setMax(100);
            mProgressDialog.setProgress(values[0]);*/

        }

        @Override
        protected String doInBackground(String... strings) {
            String s = "";
            String name =  download_name + ".des";

            try {
                String rootDir;
                File rootFile ;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                    rootFile = new File(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), File.separator + "njms");
                else
                    rootFile =new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                            + File.separator + "njms");



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
                boolean downloadComplete = false;
                while ((len1 = in.read(buffer)) > 0) {
                    total += len1;
                    if (fileLength > 0) // only if total length is known
                       // publishProgress((int) (total * 100 / fileLength)); i commented
                    updateNotification((int) (total * 100 / fileLength));
                    // BackgroundNotificationService.
                    f.write(buffer, 0, len1);
                    downloadComplete = true;
                }
onDownloadComplete(downloadComplete);
                f.close();
               /* encry(buffer, new File(rootFile,
                    name),rootFile);*/

                s = "sucess";


            } catch (Exception e) {
                s = e.toString();
                s="f";//for fail

            }


            return s;
        }


    }

}
