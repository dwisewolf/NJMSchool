package com.wisewolf.njmschool.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QuerySnapshot;
import com.wisewolf.njmschool.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ResultsFirebase extends AppCompatActivity {
    ProgressDialog mProgressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results_firebase);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        initProgress();
     //   FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("unitCount")

            .get()
            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    List<DocumentSnapshot> aaq=   queryDocumentSnapshots.getDocuments();
                    List<String> messages=new ArrayList<>();
                    List<String> dates=new ArrayList<>();
                    List<String> Uname=new ArrayList<>();
                    List<String> images=new ArrayList<>();
                    for (int i=0;i<aaq.size();i++) {
                        DocumentSnapshot ne = aaq.get(i);
                        HashMap<String, String> data = (HashMap<String, String>) ne.get("data");
                        if (data != null) {

                            Log.w("myApp", String.valueOf(i) + data.get("title"));
                            if (data.get("reg").equals("GKP148")) {
                                try {
                                    messages.add(data.get("time")+data.get("title"));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        }


                    }
                    String q="";



                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
String a="";
                }
            });
     //mProgressDialog.show();

   /*     db.collection("unitTest")
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        mProgressDialog.cancel();
                        int count = 0;
                        for (DocumentSnapshot document : task.getResult()) {
                            count++;
                            Log.d("TAG", String.valueOf(count));
                        }
                    } else {
                        Log.d("TAG", "Error getting documents: ", task.getException());
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAG", "Error getting documents: ");
                mProgressDialog.cancel();
            }
        });*/
    }

    private void initProgress() {
        mProgressDialog = new ProgressDialog(ResultsFirebase.this);
        mProgressDialog.setMessage("Loading. . .");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);

    }
}