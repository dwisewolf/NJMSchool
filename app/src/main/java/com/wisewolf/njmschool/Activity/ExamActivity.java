package com.wisewolf.njmschool.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.wisewolf.njmschool.Adapter.QuizHeadAdapter;
import com.wisewolf.njmschool.Adapter.StudentAdapter;
import com.wisewolf.njmschool.Database.OfflineDatabase;
import com.wisewolf.njmschool.Globals.GlobalData;
import com.wisewolf.njmschool.Models.News;
import com.wisewolf.njmschool.Models.QuizHead;
import com.wisewolf.njmschool.Models.SchoolDiff;
import com.wisewolf.njmschool.R;
import com.wisewolf.njmschool.RetrofitClientInstance;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import kotlin.text.Regex;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExamActivity extends AppCompatActivity {
RecyclerView mcq_recycler;
    String formattedDate="";
    TextView mcqHead;
    ProgressDialog mProgressDialog;
    OfflineDatabase dbb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();
        dbb = new OfflineDatabase(getApplicationContext());

        initProgress();
        getDate();

        mcq_recycler=findViewById(R.id.mcq_recycler);
        mcqHead=findViewById(R.id.mcq_head);

        getQuizHead(GlobalData.school_code,GlobalData.regno,GlobalData.classes);

    }

    private void initProgress() {
        mProgressDialog = new ProgressDialog(ExamActivity.this);
        mProgressDialog.setMessage("Loading. . .");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);

    }

    private void getDate() {
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA.getDefault());
          formattedDate = df.format(c);
     //   formattedDate="2020-08-10";
    }


    private void getQuizHead( String school, String regNo,String clas) {
        try {
            mProgressDialog.show();
            final RetrofitClientInstance.GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(RetrofitClientInstance.GetDataService.class);
            Call<List<QuizHead>> call = service.get_QuizHead(school, regNo,clas,formattedDate);
            call.enqueue(new Callback<List<QuizHead>>() {
                @Override
                public void onResponse(Call<List<QuizHead>> call, final Response<List<QuizHead>> response) {
                    try {
                        if (response.body() != null) {
                            mProgressDialog.cancel();
                            mcq_recycler.setAdapter(new QuizHeadAdapter(response.body(), mcq_recycler, new QuizHeadAdapter.OnItemClickListener() {

                                @Override
                                public void onItemClick(QuizHead s) {
                                 if (s.getDate().equals(formattedDate)) {
                                     String topic=s.getSubject().toUpperCase();
                                     if (topic.equals("HINDI")||topic.equals("PHYSICAL EDUCATION")||topic.equals("COMPUTER SCIENCE")|| topic.equals("PSYCHOLOGY"))
                                     {
                                         selectOptional(topic);
                                         DoTest(s);
                                      /*   String a =testdone();
                                         if (a.equals("")){
                                             DoTest(s);
                                         }
                                         else {
                                             alertBox();
                                         }*/
                                     }
                                     else {
                                         Intent intent = new Intent(ExamActivity.this, QuestionsActivity.class);
                                         intent.putExtra("title", s.getTitle());
                                         intent.putExtra("subject", s.getSubject());
                                         intent.putExtra("time", s.getTime());
                                         startActivity(intent);
                                         finish();
                                     }
                                 }else
                                     Toast.makeText(ExamActivity.this, "Exam Date is on "+s.getDate(), Toast.LENGTH_SHORT).show();
                                }
                            }));
                            LinearLayoutManager added_liste_adapterlayoutManager
                                = new LinearLayoutManager(ExamActivity.this, LinearLayoutManager.VERTICAL, false);
                            mcq_recycler.setLayoutManager(added_liste_adapterlayoutManager);
                        }
                        else {
                            finish();Toast.makeText(ExamActivity.this, "No Test Available", Toast.LENGTH_LONG).show(); mcqHead.setText("MCQ not available for today");}
                    } catch (Exception e)
                    {
                        Toast.makeText(ExamActivity.this, "Very slow internet(EAS84)", Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onFailure(Call<List<QuizHead>> call, Throwable t) {
                    finish();

                    Toast.makeText(ExamActivity.this, "Internet Speed very less(EA143)", Toast.LENGTH_LONG).show();
                    String a="";
                }
            });
        }catch (Exception ignored){
            String a="";
        }


    }

    private void selectOptional(String topic) {
        mProgressDialog.setMessage("Uploading counts ..please wait");
        mProgressDialog.show();
        // Access a Cloud Firestore instance from your Activity
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        try {
            Map<String, Object> updateMap = new HashMap();
            HashMap<String, String> data = new HashMap<String, String>();
            data.put("topic",topic);
            data.put("reg",GlobalData.regno);
            data.put("date",formattedDate);

            updateMap.put("data", data);
            db.collection("optional_unit")
                .add(updateMap)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        mProgressDialog.cancel();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mProgressDialog.cancel();
                        Log.w("TAG", "Error adding document", e);
                        Toast.makeText(ExamActivity.this, "Internet needed to submit result(ExamActivityFB224)", Toast.LENGTH_SHORT).show();

                    }
                });
        } catch (Exception e) {
            String a = "";
            mProgressDialog.cancel();
            Toast.makeText(ExamActivity.this, "Internet needed to submit result(ExamActivityQa425)", Toast.LENGTH_SHORT).show();

        }
    }

    private void DoTest(final QuizHead s) {
        new AlertDialog.Builder(ExamActivity.this)
            .setTitle("Choose the CORRECT subject.")
            .setMessage(
                "You will not be able to change the subject later")

            // Specifying a listener allows you to take an action before dismissing the dialog.
            // The dialog is automatically dismissed when a dialog button is clicked.
            .setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dbb.insertoptionTest(GlobalData.regno, "1");
                    Intent intent = new Intent(ExamActivity.this, QuestionsActivity.class);
                    intent.putExtra("title", s.getTitle());
                    intent.putExtra("subject", s.getSubject());
                    intent.putExtra("time", s.getTime());
                    startActivity(intent);
                    finish();

                }
            })

            .setIcon(android.R.drawable.ic_dialog_alert)
            .show();

    }

    private void alertBox() {
        new AlertDialog.Builder(ExamActivity.this)
            .setTitle("Message")
            .setMessage("Sorry! You have already attempted one optional subject ")

            // Specifying a listener allows you to take an action before dismissing the dialog.
            // The dialog is automatically dismissed when a dialog button is clicked.
            .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {


                }
            })

            .setIcon(android.R.drawable.ic_dialog_alert)
            .show();
    }

    private String testdone() {

        return dbb.getOptiontest(GlobalData.regno);

    }
}
