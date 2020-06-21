package com.wisewolf.njmschool.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.wisewolf.njmschool.Globals.GlobalData;
import com.wisewolf.njmschool.R;

import org.jsoup.Jsoup;

import java.util.ArrayList;

public class DocumentActivity extends AppCompatActivity {
    FirebaseStorage storage;
    StorageReference storageRef;
    String clas;
    ArrayList reference=new ArrayList();

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
    }

    private void findClassForDocument(String s) {
        switch(s) {
            case "NRL":
                clas = "NRL";
                break;
            case "LKG":
                clas = "LKG";
                break;
            case "UKG":
                clas = "UKG";
                break;
            case "12":
            case "11":
                clas = "class12";
                break;
            case "10":
                clas = "class10";
                break;
            case "9":
                clas = "class9";
                break;
            case "8":
                clas = "class8";
                break;
            case "7":
                clas = "class7";
                break;
            case "6":
                clas = "class6";
                break;
            case "5":
                clas = "class5";
                break;
            case "4":
                clas = "class4";
                break;
            case "3":
                clas = "class3";
                break;
            case "2":
                clas = "class2";
                break;
            case "1":
                clas = "class1";
                break;
        }
        
    }

    private class GetDocuments extends AsyncTask<Void, String, String> {
        String a="";
        @Override
        protected String doInBackground(Void... voids) {

            StorageReference listRef = storage.getReference().child("class1");
            listRef.listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        a="true";
                        for (StorageReference prefix : listResult.getPrefixes()) {
                            // All the prefixes under listRef.
                            // You may call listAll() recursively on them.

                            prefix.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
                                @Override
                                public void onSuccess(ListResult listResult) {

                                    for (StorageReference prefix : listResult.getPrefixes()) {
                                        reference.add(listResult.getItems());
                                    }

                                }
                            });
                        }

                        for (StorageReference item : listResult.getItems()) {
                            // All the items under listRef.
                            String a = String.valueOf(1);
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Uh-oh, an error occurred!
                         a = String.valueOf(e);
                    }
                });
            return  a;

        }

        @Override
        protected void onPostExecute(String onlineVersion) {
            super.onPostExecute(onlineVersion);
         if (a.equals("true")){}
        }

    }
}
