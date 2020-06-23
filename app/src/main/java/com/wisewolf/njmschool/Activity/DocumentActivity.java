package com.wisewolf.njmschool.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.wisewolf.njmschool.Globals.GlobalData;
import com.wisewolf.njmschool.R;

import org.jsoup.Jsoup;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class DocumentActivity extends AppCompatActivity {
    FirebaseStorage storage;
    StorageReference storageRef;
    String clas;
    ArrayList reference = new ArrayList();
    String url = "gs://njms-2e633.appspot.com/class1/Hindi Grammar/KDAG-C1L1HNGP1.pdf";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document);
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        //findClassForDocument(GlobalData.clas);
        GetDocuments getDocuments = new GetDocuments();
        getDocuments.execute();

        String a = String.valueOf(1);

        // Create a storage reference from our app
        StorageReference storageRef = storage.getReference();


    }

    private void download(byte[] bytes) throws IOException {
        String rootDir;
        File rootFile;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                rootFile = new File(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), File.separator + "Fil");
            else
                rootFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    + File.separator + "Fil");

            File data = new File(rootFile, "C1L1HNGP1.pdf");
            OutputStream op = new FileOutputStream(data);
            op.write(bytes);

            // dir_name = rootDir;
            String location = rootFile.toString();
        } catch (Exception E) {
            String a = String.valueOf(E);
        }

    }

    private class GetDocuments extends AsyncTask<Void, String, String> {
        String a = "";

        @Override
        protected String doInBackground(Void... voids) {

            StorageReference gsReference = storage.getReferenceFromUrl(url);
            final long ONE_MEGABYTE = 1024 * 1024;
            gsReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    String a = String.valueOf(1);
                    try {
                        download(bytes);
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
            return "";
        }

        @Override
        protected void onPostExecute(String onlineVersion) {
            super.onPostExecute(onlineVersion);
            if (a.equals("true")) {
            }
        }

    }
}
