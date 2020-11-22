package com.wisewolf.njmschool.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jsibbold.zoomage.ZoomageView;
import com.mindorks.paracamera.Camera;
import com.wisewolf.njmschool.Database.OfflineDatabase;
import com.wisewolf.njmschool.Globals.GlobalData;
import com.wisewolf.njmschool.Models.Feedback;
import com.wisewolf.njmschool.Models.MCCQ;
import com.wisewolf.njmschool.Models.MCQSubmit;
import com.wisewolf.njmschool.Models.QuizQuestion;
import com.wisewolf.njmschool.R;
import com.wisewolf.njmschool.RetrofitClientInstance;
import com.wisewolf.njmschool.service.KillNotificationService;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuestionsActivity extends AppCompatActivity {
    TextView tv;
    Button submitbutton, quitbutton;
    RadioGroup radio_g;
    ZoomageView que_image;
    RadioButton rb1, rb2, rb3, rb4;

    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    Camera camera;
    String questions[];
    String answers[];
    String opt[];
    String flags[];
    String image[];
    String answer_flags[];
    String marksList[];
    String answer_reg,answer_class,answer_title,answer_subject,answer_questions,answer_ansText,answer_image,answer_date;

    EditText descripptive_answer;
    Bitmap answerImage;
    int flag = 0,imgFlag=0;
    ImageView camera_View,previewImg;
    String formattedDate="";
    ProgressDialog mProgressDialog;
    String name = "", title, subject, time,id;
    TextView textView,marks_view;
    public static int marks = 0, correct = 0, wrong = 0;
    CountDownTimer countDownTimer;
    HashMap<String, String> answersFire = new HashMap<String, String>();
    List<HashMap> answFire=new ArrayList<>();
    QuizQuestion QuizQuestions;
    private final int GALLERY_ACTIVITY_CODE=400;
    private String imgpath="";
    OfflineDatabase dbb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();
        dbb = new OfflineDatabase(getApplicationContext());
        initProgress();
        startService(new Intent(QuestionsActivity.this, KillNotificationService.class));

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.CANADA.getDefault());
        formattedDate = df.format(c);


        final TextView score = (TextView) findViewById(R.id.textView4);
        textView = (TextView) findViewById(R.id.DispName);
        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        subject = intent.getStringExtra("subject");
        time = intent.getStringExtra("time");
        id = intent.getStringExtra("id");
        que_image = findViewById(R.id.que_image);
        camera_View = findViewById(R.id.camera_view);
        descripptive_answer = findViewById(R.id.descripptive_answer);
        previewImg = findViewById(R.id.previewImg);

        submitbutton = (Button) findViewById(R.id.button3);
        quitbutton = (Button) findViewById(R.id.buttonquit);
        tv = (TextView) findViewById(R.id.tvque);
        marks_view = (TextView) findViewById(R.id.marks_view);

        radio_g = (RadioGroup) findViewById(R.id.answersgrp);
        rb1 = (RadioButton) findViewById(R.id.radioButton);
        rb2 = (RadioButton) findViewById(R.id.radioButton2);
        rb3 = (RadioButton) findViewById(R.id.radioButton3);
        rb4 = (RadioButton) findViewById(R.id.radioButton4);
        radio_g.setVisibility(View.GONE);
        rb1.setVisibility(View.GONE);
        rb2.setVisibility(View.GONE);
        rb3.setVisibility(View.GONE);
        rb4.setVisibility(View.GONE);
        previewImg.setVisibility(View.GONE);
        camera_View.setVisibility(View.GONE);
        descripptive_answer.setVisibility(View.GONE);
        try {
            alertforQuiz();

        } catch (Exception e) {
            Toast.makeText(QuestionsActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
        }

        camera_View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraopen();

            }
        });


        submitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //int color = mBackgroundColor.getColor();
                //mLayout.setBackgroundColor(color);

                if (!submitbutton.getText().equals("Submit")) {
                    if (opt[flag*4].replaceAll("\\s+", "").equals("")) {

                        try {


                            String ansText = descripptive_answer.getText().toString();

//              Toast.makeText(getApplicationContext(), ansText, Toast.LENGTH_SHORT).show();
                            try {
                                if (answer_flags[flag].equals("true")){
                                    if (imgFlag==1){
                                      //  submitbutton.setVisibility(View.GONE);
                                        answer_reg=GlobalData.regno;
                                        answer_class=GlobalData.clas;
                                        answer_title=title;
                                        answer_subject=subject;
                                        answer_questions=questions[flag];
                                        answer_image=image[flag];
                                        answer_date=formattedDate;


                                        uploadImage();




                                    }
                                    else {
                                        Toast.makeText(QuestionsActivity.this, "please click a photo", Toast.LENGTH_SHORT).show();
                                    }

                                }

                                else {
                                    if (!ansText.equals("")){
                                   //     submitbutton.setVisibility(View.GONE);
                                        answersFire = new HashMap<String, String>();
                                        answersFire.put("reg", GlobalData.regno);
                                        answersFire.put("class", GlobalData.clas);
                                        answersFire.put("title", title);
                                        answersFire.put("subject", subject);
                                        answersFire.put("questions", questions[flag]);
                                        answersFire.put("ansText", ansText);
                                        answersFire.put("date", formattedDate);
                                        answersFire.put("marks", marksList[flag]);
                                        answersFire.put("que_Image", image[flag]);
                                        answersFire.put("ans_Image", answer_ansText);
                                        answFire.add(answersFire);
                                        postMCQ_Answer(GlobalData.regno, GlobalData.clas, title, subject, questions[flag], ansText, image[flag]);
                                        flag++;
                                        if (flag < questions.length) {
                                            Toast.makeText(QuestionsActivity.this, "Question "+String.valueOf(flag+1)+"/"+String.valueOf(questions.length), Toast.LENGTH_SHORT).show();

                                            if (flags[flag].equals("true")) {
                                                que_image.setVisibility(View.VISIBLE);
                                                Glide.with(QuestionsActivity.this)
                                                    .load("http://134.209.19.157/media/" + image[flag])
                                                    .into(que_image);
                                            } else {
                                                que_image.setVisibility(View.GONE);
                                            }
                                            tv.setText(questions[flag]);
                                            marks_view.setText("marks - "+marksList[flag]);
                                            if (!opt[flag*4].replaceAll("\\s+", "").equals("")) {
                                                radio_g.setVisibility(View.VISIBLE);
                                                rb1.setVisibility(View.VISIBLE);
                                                rb2.setVisibility(View.VISIBLE);
                                                rb3.setVisibility(View.VISIBLE);
                                                rb4.setVisibility(View.VISIBLE);
                                                descripptive_answer.setVisibility(View.GONE);

                                                if (answer_flags[flag].equals("true"))
                                                    camera_View.setVisibility(View.VISIBLE);


                                                rb1.setText(opt[flag * 4]);
                                                rb2.setText(opt[flag * 4 + 1]);
                                                rb3.setText(opt[flag * 4 + 2]);
                                                rb4.setText(opt[flag * 4 + 3]);

                                            }
                                            else {
                                                radio_g.setVisibility(View.GONE);
                                                rb1.setVisibility(View.GONE);
                                                rb2.setVisibility(View.GONE);
                                                rb3.setVisibility(View.GONE);
                                                rb4.setVisibility(View.GONE);
                                                descripptive_answer.setVisibility(View.VISIBLE);
                                                if (answer_flags[flag].equals("true"))
                                                    camera_View.setVisibility(View.VISIBLE);
                                            }




                                        }
                                        else {
                                            marks = correct;
                                            try {
                                                tv.setVisibility(View.INVISIBLE);
                                                que_image.setVisibility(View.INVISIBLE);
                                                radio_g.setVisibility(View.INVISIBLE);
                                                submitbutton.setText("Submit");

                                                postMCQ_Result(GlobalData.regno, GlobalData.classes, title, subject,answFire);


                                            }
                                            catch (Exception e) {
                                                Toast.makeText(QuestionsActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                                            }


                                        }

                                        radio_g.clearCheck();
                                    }
                                    else {
                                        Toast.makeText(QuestionsActivity.this, "Type your answers", Toast.LENGTH_SHORT).show();
                                    }


                                }

                            }

                            catch (Exception e) {
                                Toast.makeText(QuestionsActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                            }



                        }

                        catch (Exception e) {
                            Toast.makeText(QuestionsActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                        }
                        descripptive_answer.setText("");
                    }
                    else {
                        if (radio_g.getCheckedRadioButtonId() == -1) {
                            Toast.makeText(getApplicationContext(), "Please select an option", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        else {

                            try {
                          //      submitbutton.setVisibility(View.GONE);
                                RadioButton uans = (RadioButton) findViewById(radio_g.getCheckedRadioButtonId());
                                String ansText = uans.getText().toString();
//              Toast.makeText(getApplicationContext(), ansText, Toast.LENGTH_SHORT).show();
                                try {
                                    answersFire = new HashMap<String, String>();
                                    answersFire.put("reg", GlobalData.regno);
                                    answersFire.put("class", GlobalData.clas);
                                    answersFire.put("title", title);
                                    answersFire.put("subject", subject);
                                    answersFire.put("questions", questions[flag]);
                                    answersFire.put("ansText", ansText);
                                    answersFire.put("date", formattedDate);
                                    answersFire.put("marks", marksList[flag]);
                                    answersFire.put("que_Image", image[flag]);
                                    answersFire.put("ans_Image", answer_ansText);
                                    answFire.add(answersFire);
                                    postMCQ_Answer(GlobalData.regno, GlobalData.clas, title, subject, questions[flag], ansText, image[flag]);

                                } catch (Exception e) {
                                    Toast.makeText(QuestionsActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                                }

                                if (ansText.equals(answers[flag])) {
                                    correct++;

                                } else {
                                    wrong++;

                                }

                                flag++;


                                if (score != null)
                                    score.setText("" + correct);

                                if (flag < questions.length) {
                                    Toast.makeText(QuestionsActivity.this, "Question "+String.valueOf(flag+1)+"/"+String.valueOf(questions.length), Toast.LENGTH_SHORT).show();

                                    if (flags[flag].equals("true")) {
                                        que_image.setVisibility(View.VISIBLE);
                                        Glide.with(QuestionsActivity.this)
                                            .load("http://134.209.19.157/media/" + image[flag])
                                            .into(que_image);
                                    } else {
                                        que_image.setVisibility(View.GONE);
                                    }
                                    tv.setText(questions[flag]);
                                    marks_view.setText("marks - "+marksList[flag]);
                                    rb1.setText(opt[flag * 4]);
                                    rb2.setText(opt[flag * 4 + 1]);
                                    rb3.setText(opt[flag * 4 + 2]);
                                    rb4.setText(opt[flag * 4 + 3]);
                                } else {
                                    marks = correct;
                                    try {
                                        tv.setVisibility(View.INVISIBLE);
                                        que_image.setVisibility(View.INVISIBLE);
                                        radio_g.setVisibility(View.INVISIBLE);
                                        submitbutton.setText("Submit");

                                        postMCQ_Result(GlobalData.regno, GlobalData.classes, title, subject,answFire);


                                    } catch (Exception e) {
                                        Toast.makeText(QuestionsActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                                    }


                                }
                                radio_g.clearCheck();
                            } catch (Exception e) {
                                Toast.makeText(QuestionsActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                            }


                        }
                    }

                }
                else {
                //    postMCQ_Result(GlobalData.regno, GlobalData.classes, title, subject,answFire);
                }

            }
        });

        quitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //postMCQ_Result(GlobalData.regno, GlobalData.classes, title, subject);

            }
        });
    }

    //alert showing that quiz is going to start
    private void alertforQuiz() {
        new AlertDialog.Builder(QuestionsActivity.this)
            .setTitle("Unit Test")
            .setCancelable(false)
            .setMessage("Test will start now! Do not CLOSE the app till the test ends ! ")

            // Specifying a listener allows you to take an action before dismissing the dialog.
            // The dialog is automatically dismissed when a dialog button is clicked.
            .setPositiveButton("Take test", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    try {

                        getQuizHead(title);
                        GlobalData.tittle=title;
                    } catch (Exception e) {
                        Toast.makeText(QuestionsActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }

                }
            })

            // A null listener allows the button to dismiss the dialog and take no further action.
            .setNegativeButton("CLOSE", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            })
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show();
    }

    //submit all answers to firebase , called at end of each exam
    private void submitfirebase(List<HashMap> answFire) {
        mProgressDialog.setMessage("Uploading Results ..please wait");
        mProgressDialog.show();
        // Access a Cloud Firestore instance from your Activity
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        try {
            Map<String, Object> updateMap = new HashMap();
            updateMap.put("field1", answFire);
            db.collection("midTerm").document(GlobalData.regno+"_"+id).set(updateMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(QuestionsActivity.this, "sent ...", Toast.LENGTH_SHORT).show();
                        mProgressDialog.cancel();
                        Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
                        startActivity(intent);
                        finish();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mProgressDialog.cancel();
                        Log.w("TAG", "Error adding document", e);
                        Toast.makeText(QuestionsActivity.this, "Internet needed to submit result(FB224)", Toast.LENGTH_SHORT).show();

                    }
                });

        } catch (Exception e) {
            String a = "";
            mProgressDialog.cancel();
            Toast.makeText(QuestionsActivity.this, "Internet needed to submit result(FB224)", Toast.LENGTH_SHORT).show();

        }

    }

    private void initProgress() {
        mProgressDialog = new ProgressDialog(QuestionsActivity.this);
        mProgressDialog.setMessage("Loading. . .");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);

    }

    //get the questions from api
    private void getQuizHead(String title) {
        mProgressDialog.show();

        try {
            final RetrofitClientInstance.GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(RetrofitClientInstance.GetDataService.class);
            Call<QuizQuestion> call = service.get_QuizQuestions(GlobalData.school_code, GlobalData.classes, title);
            call.enqueue(new Callback<QuizQuestion>() {
                @Override
                public void onResponse(Call<QuizQuestion> call, Response<QuizQuestion> response) {
                    try {
                        mProgressDialog.cancel();
                        if (response.body()!=null) {
                            if (response.body().getAsnwers_list() != null || response.body().getOptions_list() != null || response.body().getQuestions_list() != null) {

                                timer(response.body().getQuestions_list().length);
                                questions = new String[response.body().getQuestions_list().length];
                                answers = new String[response.body().getAsnwers_list().length];
                                flags = new String[response.body().getFlag_list().length];
                                image = new String[response.body().getUrl_list().length];
                                marksList = new String[response.body().getMarks_list().length];
                                answer_flags = new String[response.body().getAns_flag_list().length];

                                int size = 0;

                                for (int i = 0; i < response.body().getOptions_list().length; i++) {
                                    for (int j = 0; j < response.body().getOptions_list()[i].length; j++) {
                                        size = size + 1;
                                    }
                                }

                                opt = new String[size];
                                for (int i = 0; i < response.body().getQuestions_list().length; i++) {
                                    questions[i] = response.body().getQuestions_list()[i];
                                }

                                for (int i = 0; i < response.body().getAsnwers_list().length; i++) {
                                    answers[i] = response.body().getAsnwers_list()[i];
                                }

                                for (int i = 0; i < response.body().getFlag_list().length; i++) {
                                    flags[i] = response.body().getFlag_list()[i];
                                }

                                for (int i = 0; i < response.body().getUrl_list().length; i++) {
                                    image[i] = response.body().getUrl_list()[i];
                                }

                                for (int i = 0; i < response.body().getMarks_list().length; i++) {
                                    marksList[i] = response.body().getMarks_list()[i];
                                }

                                for (int i = 0; i < response.body().getAns_flag_list().length; i++) {
                                    answer_flags[i] = response.body().getAns_flag_list()[i];
                                }


                                int pos = 0;
                                for (int i = 0; i < response.body().getOptions_list().length; i++) {
                                    for (int j = 0; j < response.body().getOptions_list()[i].length; j++) {

                                        opt[pos] = response.body().getOptions_list()[i][j];
                                        pos = pos + 1;
                                    }
                                }

                                tv.setText(questions[flag]);
                                marks_view.setText("marks - "+marksList[flag]);
                                if (flags[flag].equals("true")) {
                                    Glide.with(QuestionsActivity.this)
                                        .load("http://134.209.19.157/media/" + image[flag])
                                        .into(que_image);
                                }
                                if (!opt[0].replaceAll("\\s+", "").equals("")) {
                                    radio_g.setVisibility(View.VISIBLE);
                                    rb1.setVisibility(View.VISIBLE);
                                    rb2.setVisibility(View.VISIBLE);
                                    rb3.setVisibility(View.VISIBLE);
                                    rb4.setVisibility(View.VISIBLE);
                                    descripptive_answer.setVisibility(View.GONE);

                                    if (answer_flags[0].equals("true"))
                                        camera_View.setVisibility(View.VISIBLE);

                                    rb1.setText(opt[0]);
                                    rb2.setText(opt[1]);
                                    rb3.setText(opt[2]);
                                    rb4.setText(opt[3]);

                                }
                                else {
                                    radio_g.setVisibility(View.GONE);
                                    rb1.setVisibility(View.GONE);
                                    rb2.setVisibility(View.GONE);
                                    rb3.setVisibility(View.GONE);
                                    rb4.setVisibility(View.GONE);
                                    descripptive_answer.setVisibility(View.VISIBLE);

                                    if (answer_flags[0].equals("true"))
                                        camera_View.setVisibility(View.VISIBLE);
                                }

                                submitCount();
                            }
                            else {
                                finish();

                                Toast.makeText(QuestionsActivity.this, "No Questions  ", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            finish();

                            Toast.makeText(QuestionsActivity.this, "No Questions  ", Toast.LENGTH_SHORT).show();
                        }


                    } catch (Exception e) {
                        Toast.makeText(QuestionsActivity.this, "Very slow internet (QA306)", Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onFailure(Call<QuizQuestion> call, Throwable t) {
                    mProgressDialog.cancel();
                    Toast.makeText(QuestionsActivity.this, "Very slow internet (QA314)", Toast.LENGTH_SHORT).show();
                    finish();
                    String a = "";
                }
            });
        } catch (Exception ignored) {
            mProgressDialog.cancel();
            String a = "";
        }


    }

    //count added to local db
    private void submitCount() {
        mProgressDialog.setMessage("Uploading counts ..please wait");
        mProgressDialog.show();
        // Access a Cloud Firestore instance from your Activity
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        try {

           String y= dbb.insertmidCount(String.valueOf(questions.length),GlobalData.regno,subject,title,formattedDate);
           if (y.equals("y")){
               Toast.makeText(QuestionsActivity.this, "Count uploaded to local", Toast.LENGTH_SHORT).show();
               mProgressDialog.cancel();}

          /* for firebase Map<String, Object> updateMap = new HashMap();
            HashMap<String, String> data = new HashMap<String, String>();
            data.put("count",String.valueOf(questions.length));
            data.put("reg",GlobalData.regno);
            data.put("subject",subject);
            data.put("title",title);
            data.put("time",formattedDate);
            updateMap.put("data", data);
            db.collection("midtermCount")
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
                        Toast.makeText(QuestionsActivity.this, "Internet needed to submit result(FB224)", Toast.LENGTH_SHORT).show();

                    }
                });*/

        } catch (Exception e) {
            String a = "";
            mProgressDialog.cancel();
            Toast.makeText(QuestionsActivity.this, "Internet needed to submit result(Qa425)", Toast.LENGTH_SHORT).show();

        }
    }

    //submit each answer to database , added to local db
    private void postMCQ_Answer(final String userid, String clas, String title, String subject, String question, String answer, String image) {
        try {
            mProgressDialog.show();
            String result=dbb.insert_postMCQ_Answer(GlobalData.regno, GlobalData.classes, title, subject, question, answer,image ,"0",image);
            if (result.equals("y")){
                Toast.makeText(QuestionsActivity.this, "answer uploaded to local", Toast.LENGTH_SHORT).show();
                mProgressDialog.cancel();
                submitbutton.setVisibility(View.VISIBLE);
            }
            else {
                mProgressDialog.cancel();
                Toast.makeText(QuestionsActivity.this, "error", Toast.LENGTH_SHORT).show();
            }


        } catch (Exception ignored) {
            mProgressDialog.cancel();
            String a = "";
        }


    }

    //duplicated for debug
   /* private void postMCQ_Answer(final String userid, String clas, String title, String subject, String question, String answer, String image) {
        try {
            mProgressDialog.show();
            String result=dbb.insert_postMCQ_Answer(GlobalData.regno, GlobalData.classes, title, subject, question, answer,image ,"0",image);
            if (result.equals("y")){

                mProgressDialog.cancel();
                submitbutton.setVisibility(View.VISIBLE);
            }
            else {

            }

            final RetrofitClientInstance.GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(RetrofitClientInstance.GetDataService.class);
            Call<MCCQ> call = service.saveMCQ_Answer(GlobalData.regno, GlobalData.classes, title, subject, question, answer,image ,"0",image);
            call.enqueue(new Callback<MCCQ>() {
                @Override
                public void onResponse(Call<MCCQ> call, Response<MCCQ> response) {
                    mProgressDialog.cancel();
                    submitbutton.setVisibility(View.VISIBLE);

                }

                @Override
                public void onFailure(Call<MCCQ> call, Throwable t) {
                    String a = "";
                    mProgressDialog.cancel();
                }
            });
        } catch (Exception ignored) {
            mProgressDialog.cancel();
            String a = "";
        }


    }
*/

    //submmit final results to db
    //cal the api at final stage ,
    // so submit firebase is done first
    private void postMCQ_Result(String userid, String clas, String title, String subject, final List<HashMap> answFire) {
        try {
            mProgressDialog.setMessage("Uploading results...");
            mProgressDialog.show();

            String result=dbb.insert_postMCQ_Result(GlobalData.regno, GlobalData.classes, title,subject, GlobalData.regno+"_"+id);
            if (result.equals("y"))
            {
                Toast.makeText(QuestionsActivity.this, "Result uploaded to local", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
                startActivity(intent);
                finish();
            }
            else {
                mProgressDialog.cancel();
                Toast.makeText(QuestionsActivity.this, "Error in result submission", Toast.LENGTH_SHORT).show();
            }


        } catch (Exception ignored) {
            mProgressDialog.cancel();
            String a = "";
        }


    }

   /* private void postMCQ_Result(String userid, String clas, String title, String subject, final List<HashMap> answFire) {
        try {
            mProgressDialog.setMessage("Uploading results...");
            mProgressDialog.show();

            String result=dbb.insert_postMCQ_Result(GlobalData.regno, GlobalData.classes, title,subject, GlobalData.regno+"_"+id);
            if (result.equals("y"))
            {
                Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
                startActivity(intent);
                finish();
            }
            else {}
            //do pending here

            final RetrofitClientInstance.GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(RetrofitClientInstance.GetDataService.class);
            Call<MCQSubmit> call = service.saveMCQ_Result(GlobalData.regno, GlobalData.classes, title, GlobalData.regno+"_"+id,subject);
            call.enqueue(new Callback<MCQSubmit>() {
                @Override
                public void onResponse(Call<MCQSubmit> call, Response<MCQSubmit> response) {
                    mProgressDialog.cancel();
                    submitfirebase(answFire);

                }

                @Override
                public void onFailure(Call<MCQSubmit> call, Throwable t) {
                    String a = "";
                    Toast.makeText(QuestionsActivity.this, "internet needed to post result", Toast.LENGTH_SHORT).show();
                    mProgressDialog.cancel();
                }
            });
        } catch (Exception ignored) {
            mProgressDialog.cancel();
            String a = "";
        }


    }
*/

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Your cannot press 'Back' , while in a TEST", Toast.LENGTH_SHORT).show();
    }

    void timer(final int length) {

        Long dur = Long.valueOf(time) * 60000;

       countDownTimer= new CountDownTimer(dur, 1000) {

            public void onTick(long millisUntilFinished) {
                String text = String.format(Locale.getDefault(), "Time Remaining %02d min: %02d sec",
                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60,
                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60);
                textView.setText(text);

                //here you can have your logic to set text to edittext
            }

            public void onFinish() {

                textView.setText("done!");
                if (flag != length) {
                    timedOUtUser(flag);
                    int count = length - flag;
                    postMCQ_AnswerTieout(0, count, title, subject, questions[flag], "TIMED OUT", "");
                    wrong++;

                    marks = correct;

                }


            }

        };
       countDownTimer.start();


    }


    //adding timed out user and exam data to firebase
    private void timedOUtUser(int flag) {
        mProgressDialog.setMessage("Uploading counts ..please wait");
        mProgressDialog.show();
        // Access a Cloud Firestore instance from your Activity
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        try {
            Map<String, Object> updateMap = new HashMap();
            HashMap<String, String> data = new HashMap<String, String>();
            data.put("reg",GlobalData.regno);
            data.put("title",title);
            data.put("subject",subject);
            data.put("q_count",String.valueOf(flag));
            updateMap.put("data", data);
            db.collection("midterm_timedOut")
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
                        Toast.makeText(QuestionsActivity.this, "Internet needed to submit result(FB224)", Toast.LENGTH_SHORT).show();

                    }
                });
        } catch (Exception e) {
            String a = "";
            mProgressDialog.cancel();
            Toast.makeText(QuestionsActivity.this, "Internet needed to submit result(Qa425)", Toast.LENGTH_SHORT).show();

        }
    }

    //submit each answer to db for timedout users
    private void postMCQ_AnswerTieout(final int i, final int count, final String title, final String subject, String question, String answer, String image) {
        try {
            mProgressDialog.show();
            String result=dbb.insert_postMCQ_Answer(GlobalData.regno, GlobalData.classes, title, subject, question, answer, image,"0",image);
            if (result.equals("y")){
                Toast.makeText(QuestionsActivity.this, "timeout"+String.valueOf(i)+"  "+String.valueOf(count), Toast.LENGTH_SHORT).show();
                if (i<count){
                    postMCQ_AnswerTieout(i+1, count, title, subject, questions[flag], "TIMED OUT", "TIMED OUT");
                    wrong++;

                }
                else {
                    postMCQ_Result(GlobalData.regno, GlobalData.clas, title, subject,answFire);
                }
            }
            else {
                mProgressDialog.cancel();
                Toast.makeText(QuestionsActivity.this, "error adding", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception ignored) {
            mProgressDialog.cancel();
            String a = "";
        }


    }


  /*  //submit each answer to db for timedout users
    private void postMCQ_AnswerTieout(final int i, final int count, final String title, final String subject, String question, String answer, String image) {
        try {
            mProgressDialog.show();
            final RetrofitClientInstance.GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(RetrofitClientInstance.GetDataService.class);
            Call<MCCQ> call = service.saveMCQ_Answer(GlobalData.regno, GlobalData.classes, title, subject, question, answer, image,"0",image);
            call.enqueue(new Callback<MCCQ>() {
                @Override
                public void onResponse(Call<MCCQ> call, Response<MCCQ> response) {
                    mProgressDialog.cancel();
                    Toast.makeText(QuestionsActivity.this, "timeout"+String.valueOf(i)+"  "+String.valueOf(count), Toast.LENGTH_SHORT).show();
                    if (i<count){
                        postMCQ_AnswerTieout(i+1, count, title, subject, questions[flag], "TIMED OUT", "TIMED OUT");
                        wrong++;

                    }
                    else {
                        postMCQ_Result(GlobalData.regno, GlobalData.clas, title, subject,answFire);
                    }

                }

                @Override
                public void onFailure(Call<MCCQ> call, Throwable t) {
                    String a = "";
                    mProgressDialog.cancel();
                }
            });
        } catch (Exception ignored) {
            mProgressDialog.cancel();
            String a = "";
        }


    }
*/

    private void cameraopen1() {
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
        }
        else
        {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        }
    }

    private void cameraopen() {
        // Create global camera reference in an activity or fragment


// Build the camera
        camera = new Camera.Builder()
            .resetToCorrectOrientation(true)// it will rotate the camera bitmap to the correct orientation from meta data
            .setTakePhotoRequestCode(1)
            .setDirectory("pics")
            .setName("ali_" + System.currentTimeMillis())
            .setImageFormat(Camera.IMAGE_JPEG)
            .setCompression(50)
            .setImageHeight(500)// it will try to achieve this height as close as possible maintaining the aspect ratio;
            .build(this);

        try {
            camera.takePicture();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

       /* if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK)
        {
            Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
            previewImg.setImageBitmap(selectedImage);
            answerImage=selectedImage;
            previewImg.setVisibility(View.VISIBLE);
            imgFlag=1;
        }*/
        if(requestCode == Camera.REQUEST_TAKE_PHOTO){
            Bitmap bitmap = camera.getCameraBitmap();
            bitmap=camera.resizeAndGetCameraBitmap(500);
            if(bitmap != null) {

                previewImg.setImageBitmap(bitmap);
                answerImage=bitmap;
                previewImg.setVisibility(View.VISIBLE);
                imgFlag=1;
            }else{
                Toast.makeText(this.getApplicationContext(),"Picture not taken!",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private Uri getImageFileUri(){
        // Create a storage directory for the images
        // To be safe(r), you should check that the SD card is mounted
        // using Environment.getExternalStorageState() before doing this

        File imagePath = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + getApplicationContext().getPackageName()
                + "/Files");
        if (!imagePath.exists()) {
            if (!imagePath.mkdirs()) {
                return null;
            } else {
                // create new folder


            }
        }

        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File image = new File(imagePath, "njms_" + timeStamp + ".jpg");

        imgpath=image.getAbsolutePath();
        if (!image.exists()) {
            try {
                image.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // Create an File Uri
        return Uri.fromFile(image);
    }

    private void uploadImage() {
        mProgressDialog.setMessage("uploading image...");
        mProgressDialog.show();

        try {
            if (answerImage!=null) {
                Date c = Calendar.getInstance().getTime();
                System.out.println("Current time => " + c);

                SimpleDateFormat df = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault());
                String date = df.format(c);
                String ref = "answerImage/" + GlobalData.regno + "_"+id + "_"+String.valueOf(flag)+"_"+c+ ".jpg";

                FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
                StorageReference storageReferenceProfilePic = firebaseStorage.getReference();
                StorageReference imageRef = storageReferenceProfilePic.child(ref);
                imageRef.putBytes(byteconvert(answerImage))

                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String a=uri.toString();
                                    answer_ansText=a;

                                    answersFire = new HashMap<String, String>();
                                    answersFire.put("reg", GlobalData.regno);
                                    answersFire.put("class", GlobalData.clas);
                                    answersFire.put("title", title);
                                    answersFire.put("subject", subject);
                                    answersFire.put("questions", questions[flag]);
                                    answersFire.put("ansText", answer_ansText);

                                    answersFire.put("date", formattedDate);
                                    answersFire.put("marks", marksList[flag]);
                                    answersFire.put("que_Image", image[flag]);
                                    answersFire.put("ans_Image", answer_ansText);
                                    answFire.add(answersFire);


                                    previewImg.setVisibility(View.GONE);
                                    answerImage.recycle();
                                    imgFlag=0;

                                    postMCQ_Answer(GlobalData.regno, GlobalData.clas, title, subject, questions[flag], answer_ansText, image[flag]);
                                    flag++;
                                    if (flag < questions.length) {
                                        Toast.makeText(QuestionsActivity.this, "Question "+String.valueOf(flag+1)+"/"+String.valueOf(questions.length), Toast.LENGTH_SHORT).show();

                                        if (flags[flag].equals("true")) {
                                            que_image.setVisibility(View.VISIBLE);
                                            Glide.with(QuestionsActivity.this)
                                                .load("http://134.209.19.157/media/" + image[flag])
                                                .into(que_image);
                                        } else {
                                            que_image.setVisibility(View.GONE);
                                        }
                                        tv.setText(questions[flag]);
                                        if (!opt[flag*4].replaceAll("\\s+", "").equals("")) {
                                            radio_g.setVisibility(View.VISIBLE);
                                            rb1.setVisibility(View.VISIBLE);
                                            rb2.setVisibility(View.VISIBLE);
                                            rb3.setVisibility(View.VISIBLE);
                                            rb4.setVisibility(View.VISIBLE);
                                            descripptive_answer.setVisibility(View.GONE);

                                            if (answer_flags[flag].equals("true"))
                                                camera_View.setVisibility(View.VISIBLE);


                                            rb1.setText(opt[flag * 4]);
                                            rb2.setText(opt[flag * 4 + 1]);
                                            rb3.setText(opt[flag * 4 + 2]);
                                            rb4.setText(opt[flag * 4 + 3]);

                                        }
                                        else {
                                            radio_g.setVisibility(View.GONE);
                                            rb1.setVisibility(View.GONE);
                                            rb2.setVisibility(View.GONE);
                                            rb3.setVisibility(View.GONE);
                                            rb4.setVisibility(View.GONE);
                                            descripptive_answer.setVisibility(View.VISIBLE);
                                            if (answer_flags[flag].equals("true"))
                                                camera_View.setVisibility(View.VISIBLE);
                                        }




                                    }
                                    else {
                                        marks = correct;
                                        try {
                                            tv.setVisibility(View.INVISIBLE);
                                            que_image.setVisibility(View.INVISIBLE);
                                            radio_g.setVisibility(View.INVISIBLE);
                                          //  submitbutton.setVisibility(View.GONE);


                                            postMCQ_Result(GlobalData.regno, GlobalData.classes, title, subject,answFire);


                                        }
                                        catch (Exception e) {
                                            Toast.makeText(QuestionsActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                                        }


                                    }
                                    mProgressDialog.cancel();
                                    imgFlag=0;
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    String a="";
                                    mProgressDialog.cancel();
                                }
                            });



                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            //if the upload is not successful
                            //hiding the progress dialog
                            mProgressDialog.cancel();
                            //and displaying error message
                        }
                    });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private byte[] byteconvert(Bitmap selectedImage) {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        selectedImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        // selectedImage.recycle();
        return byteArray;
    }

}