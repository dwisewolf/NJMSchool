package com.wisewolf.njmschool.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wisewolf.njmschool.Adapter.OfflineMainAdapter;
import com.wisewolf.njmschool.Database.OfflineDatabase;
import com.wisewolf.njmschool.Globals.GlobalData;
import com.wisewolf.njmschool.Models.OfflineVideos;
import com.wisewolf.njmschool.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class OfflineScreenUser extends AppCompatActivity {
    OfflineDatabase dbb;
    RecyclerView offlineList;
    String StudentClass = "12",pass="WISEWOLF",DcryptName="",DcryptSalt="",DcryptInv="";
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_screen);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();

        offlineList=findViewById(R.id.offline_main_list);
        mProgressDialog = new ProgressDialog(OfflineScreenUser.this);

        dbb = new OfflineDatabase(getApplicationContext());
        offlinevideos();



    }



    private void offlinevideos() {
        dbb.OfflineVideoss();
        if (GlobalData.OfflineVideos==null|| GlobalData.OfflineVideos.size()==0){}
        else {
            try {


                offlineList.setAdapter(new OfflineMainAdapter(GlobalData.OfflineVideos, OfflineScreenUser.this, offlineList, new OfflineMainAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(OfflineVideos item) {

                        OFFLINE_MANAGE(item.getName(),item.getSalt(),item.getInv());

                    }


                }, new OfflineMainAdapter.OnLongClickListener() {
                    @Override
                    public void onItemLongClick(final OfflineVideos item) {

                    }
                }));
                LinearLayoutManager added_liste_adapterlayoutManager
                    = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                offlineList.setLayoutManager(added_liste_adapterlayoutManager);


            }
            catch (Exception e){
                Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void OFFLINE_MANAGE(String name, String salt, String inv) {
        DcryptName=name;
        DcryptSalt=salt;
        DcryptInv=inv;
         Decrypt DB = new Decrypt();
        DB.execute("");
    }

    private class Decrypt extends AsyncTask<String, String, String> {
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


            mProgressDialog.cancel();
            Intent intent=new Intent(OfflineScreenUser.this,OfflinePlayFullScreen.class);
            intent.putExtra("root", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                + File.separator + "njms");
            intent.putExtra("name", DcryptName);
            startActivity(intent);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.setMessage("Decrypting your video...");
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            File rootFile;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                rootFile = new File(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), File.separator + "njms");
            else
                rootFile =new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    + File.separator + "njms");
            try {
                walkdir(rootFile);
                // d_encry(DcryptName,DcryptSalt,DcryptInv);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "s";
        }


    }

    void d_encry(String M_name, String M_salt, String M_inv) throws Exception {

        String password = pass;
        File rootFile ;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            rootFile = new File(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), File.separator + "njms");
        else
            rootFile =new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                + File.separator + "njms");
        boolean a=rootFile.mkdirs();
        // reading the salt
        // user should have secure mechanism to transfer the
        // salt, iv and password to the recipient
        String name = M_salt;
        FileInputStream saltFis = new FileInputStream(new File(rootFile,
            name));
        byte[] salt = new byte[8];
        saltFis.read(salt);
        saltFis.close();

        // reading the iv
        name = M_inv;
        FileInputStream ivFis = new FileInputStream(new File(rootFile,
            name));
        byte[] iv = new byte[16];
        ivFis.read(iv);
        ivFis.close();

        SecretKeyFactory factory = SecretKeyFactory
            .getInstance("PBKDF2WithHmacSHA1");
        KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt, 65536,
            256);
        SecretKey tmp = factory.generateSecret(keySpec);
        SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");

        // file decryption
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(iv));
        name = M_name + ".des";
        FileInputStream fis = new FileInputStream(new File(rootFile,
            name));
        name = M_name + ".mp4";
        FileOutputStream fos = new FileOutputStream(new File(rootFile,
            name));
        byte[] in = new byte[64];
        int read;
        while ((read = fis.read(in)) != -1) {
            byte[] output = cipher.update(in, 0, read);
            if (output != null)
                fos.write(output);
        }

        byte[] output = cipher.doFinal();
        if (output != null)
            fos.write(output);
        fis.close();
        fos.flush();
        fos.close();


    }


    public void walkdir(File dir) {
        String[] TARGET_EXTENSIONS = { "des" };
        File listFile[] = dir.listFiles();
        if (listFile != null) {
            for (int i = 0; i < listFile.length; i++) {
                if (listFile[i].isDirectory()) {
                    walkdir(listFile[i]);
                } else {
                    String fPath = listFile[i].getPath();
                    for (String ext : TARGET_EXTENSIONS) {
                        if(fPath.endsWith(ext)) {
                            putDotBeforeFileName(listFile[i]);
                        }
                    }
                }
            }
        }
    }

    private String putDotBeforeFileName(File file) {
        String fileName = file.getName();
        String fullPath = file.getAbsolutePath();
        int indexOfFileNameStart = fullPath.lastIndexOf(fileName);
        fullPath=fullPath.substring(0, indexOfFileNameStart);
        StringBuilder sb = new StringBuilder(fullPath);
        sb.insert(indexOfFileNameStart, fileName+".mp4");
        String myRequiredFileName = sb.toString();
        file.renameTo(new File(myRequiredFileName));
        return myRequiredFileName;
    }

}
