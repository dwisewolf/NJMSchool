package com.wisewolf.njmschool.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.wisewolf.njmschool.Adapter.FeedbackReply;
import com.wisewolf.njmschool.Adapter.TeacherDetailAdapter;
import com.wisewolf.njmschool.Database.OfflineDatabase;
import com.wisewolf.njmschool.Globals.GlobalData;
import com.wisewolf.njmschool.Models.Feedback;
import com.wisewolf.njmschool.Models.FeedbackReplyModel;
import com.wisewolf.njmschool.Models.TeacherDetails;
import com.wisewolf.njmschool.R;
import com.wisewolf.njmschool.RetrofitClientInstance;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedbackActivity extends AppCompatActivity {
    EditText feedbox;
    Button save;
    String formattedDate;
    RecyclerView repyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();

        feedbox = findViewById(R.id.feedbox);
        repyList = findViewById(R.id.repyList);
        save = findViewById(R.id.save);
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        formattedDate = df.format(c);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
if (!feedbox.getText().toString().equals("")) {
    Feedback feedback = new Feedback();
    feedback.setSchool(GlobalData.school_code);
    feedback.setMsg(feedbox.getText().toString());
    feedback.setUser(GlobalData.regno);
    feedback.setDate(formattedDate);
    addDb(feedback);
}
else {
    Toast.makeText(FeedbackActivity.this, "Fill your feedback", Toast.LENGTH_SHORT).show();
}

            }
        });
        getFeedreply(GlobalData.school_code,GlobalData.regno);
        // writepost();
    }

    private void writepost() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("feedback").document("feeds");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("TAG", "DocumentSnapshot data: " + document.getData());

                    } else {
                        Log.d("TAG", "No such document");
                    }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                }
            }
        });
    }

    private void addDb(Feedback feedback) {
        // Access a Cloud Firestore instance from your Activity
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        try {
            db.collection("feedback")
                .add(feedback)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(FeedbackActivity.this, "sent ...", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error adding document", e);
                        Toast.makeText(FeedbackActivity.this, "sent", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
        } catch (Exception e) {
            String a = "";
        }

    }



    private void getFeedreply(final String school, String userid) {
        try {
            final ProgressDialog mProgressDialog;
            mProgressDialog = new ProgressDialog(FeedbackActivity.this);
            mProgressDialog.setMessage("Loading. . .");
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCancelable(true);
            mProgressDialog.show();
            final RetrofitClientInstance.GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(RetrofitClientInstance.GetDataService.class);
            Call<List<FeedbackReplyModel>> call = service.get_feedbackReply(school,userid);
            call.enqueue(new Callback<List<FeedbackReplyModel>>() {
                @Override
                public void onResponse(Call<List<FeedbackReplyModel>> call, Response<List<FeedbackReplyModel>> response) {

                    mProgressDialog.cancel();
                    repyList.setAdapter(new FeedbackReply(response.body(), repyList));
                    LinearLayoutManager added_liste_adapterlayoutManager
                        = new LinearLayoutManager(FeedbackActivity.this, LinearLayoutManager.VERTICAL, false);
                    repyList.setLayoutManager(added_liste_adapterlayoutManager);

                }

                @Override
                public void onFailure(Call<List<FeedbackReplyModel>> call, Throwable t) {
                    mProgressDialog.cancel();

                }
            });


        }catch (Exception ignored){
            String a="";
        }


    }


}