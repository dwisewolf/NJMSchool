package com.wisewolf.njmschool;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.wisewolf.njmschool.Database.OfflineDatabase;
import com.wisewolf.njmschool.Globals.GlobalData;

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
import java.util.Date;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import io.opencensus.internal.Utils;

public class DownloadTask {
    String pass="WISEWOLF";
    OfflineDatabase dbb;

    private static final String TAG = "Download Task";
    private Context context;

    private String downloadUrl = "", downloadFileName = "";
    private String salt_name="";
    private String inv_name="";
    private String dir_name="";
    private String location="";
    private String userid="";
    private String extra1="";
    private String extra2="";

    public DownloadTask(Context context, String downloadUrl, String downloadname, String extra1, String extra2) {
        this.context = context;
        this.extra1=extra1;
        this.extra2=extra2;

        this.downloadUrl = downloadUrl;

        downloadFileName = downloadname;//Create file name by picking download file name from URL
        Log.e(TAG, downloadFileName);

        //Start Downloading Task
        new DownloadingTask().execute();
    }

    private class DownloadingTask extends AsyncTask<String, Integer, String> {
        private NotificationHelper mNotificationHelper=new NotificationHelper(context,downloadFileName);
        File apkStorage = null;
        File outputFile = null;

        @Override
        protected void onPreExecute() {
          //  super.onPreExecute();
            mNotificationHelper.createNotification();
          //  mNotificationHelper = new NotificationHelper(context);
           // buttonText.setEnabled(false);
           // buttonText.setText(R.string.downloadStarted);//Set Button Text when download started
            Toast.makeText(context, "download started", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            mNotificationHelper.progressUpdate(values[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            mNotificationHelper.completed();
            try {
                if (!s.equals("f")){
                    dbb = new OfflineDatabase(context);
                    userid= GlobalData.regno;
                    dbb.insertinto(downloadFileName,salt_name,dir_name,inv_name,location,userid,extra1,extra2);
                    String a="";
                }
                if (outputFile != null) {

               /*     buttonText.setEnabled(true);
                    buttonText.setText(R.string.downloadCompleted);*///If Download completed then change button text
                    //  Toast.makeText(context, "download completed", Toast.LENGTH_SHORT).show();
                } else {
                    //  Toast.makeText(context, "download downloadFailed", Toast.LENGTH_SHORT).show();
                    // buttonText.setText(R.string.downloadFailed);//If download failed change button text
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                          /*  buttonText.setEnabled(true);
                            buttonText.setText(R.string.downloadAgain);*/
                            //Toast.makeText(context, "download downloadFailed again", Toast.LENGTH_SHORT).show();//Change button text again after 3sec
                        }
                    }, 3000);

                    Log.e(TAG, "Download Failed");

                }
            } catch (Exception e) {
                e.printStackTrace();

                //Change button text if exception occurs
                //    buttonText.setText(R.string.downloadFailed);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                    /*    buttonText.setEnabled(true);
                        buttonText.setText(R.string.downloadAgain);*/
                    }
                }, 3000);
                Log.e(TAG, "Download Failed with Exception - " + e.getLocalizedMessage());

            }



        }

        @Override
        protected String doInBackground(String... strings) {


            String s = "";
            String name = downloadFileName;

            try {
                String rootDir="";
                File rootFile ;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                    rootFile = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), File.separator + "Fil");
                else
                    rootFile =new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                        + File.separator + "Fil");

                 dir_name = rootDir;
                 location = rootFile.toString();

                boolean a=rootFile.mkdirs();


                URL url = new URL(downloadUrl);
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
            String name = downloadFileName + ".des";
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
            name =downloadFileName+ "salt" + ".enc";
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
            name =downloadFileName+ "iv" + ".enc";
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


}
