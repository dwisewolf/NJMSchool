package com.wisewolf.njmschool.Activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jsibbold.zoomage.ZoomageView;
import com.wisewolf.njmschool.Globals.GlobalData;
import com.wisewolf.njmschool.Models.Feedback;
import com.wisewolf.njmschool.Models.MCCQ;
import com.wisewolf.njmschool.Models.MCQSubmit;
import com.wisewolf.njmschool.Models.QuizQuestion;
import com.wisewolf.njmschool.R;
import com.wisewolf.njmschool.RetrofitClientInstance;

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
    String questions[];
    String answers[];
    String opt[];
    String flags[];
    String image[];
    int flag = 0;
    String formattedDate="";
    ProgressDialog mProgressDialog;
    String name = "", title, subject, time;
    TextView textView;
    public static int marks = 0, correct = 0, wrong = 0;
    CountDownTimer countDownTimer;
    HashMap<String, String> answersFire = new HashMap<String, String>();
    List<HashMap> answFire=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();
        initProgress();

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
        que_image = findViewById(R.id.que_image);

        submitbutton = (Button) findViewById(R.id.button3);
        quitbutton = (Button) findViewById(R.id.buttonquit);
        tv = (TextView) findViewById(R.id.tvque);

        radio_g = (RadioGroup) findViewById(R.id.answersgrp);
        rb1 = (RadioButton) findViewById(R.id.radioButton);
        rb2 = (RadioButton) findViewById(R.id.radioButton2);
        rb3 = (RadioButton) findViewById(R.id.radioButton3);
        rb4 = (RadioButton) findViewById(R.id.radioButton4);

        try {
            alertforQuiz();

        } catch (Exception e) {
            Toast.makeText(QuestionsActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
        }


        submitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //int color = mBackgroundColor.getColor();
                //mLayout.setBackgroundColor(color);

                if (!submitbutton.getText().equals("Submit")) {

                    if (radio_g.getCheckedRadioButtonId() == -1) {
                        Toast.makeText(getApplicationContext(), "Please select an option", Toast.LENGTH_SHORT).show();
                        return;
                    } else {

                        try {
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
                                answersFire.put("image", image[flag]);
                                answersFire.put("date", formattedDate);
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
                                Toast.makeText(QuestionsActivity.this, "Question "+String.valueOf(flag+1)+"/"+String.valueOf(questions.length), Toast.LENGTH_LONG).show();

                                if (flags[flag].equals("true")) {
                                    que_image.setVisibility(View.VISIBLE);
                                    Glide.with(QuestionsActivity.this)
                                        .load("http://139.59.79.78/media/" + image[flag])
                                        .into(que_image);
                                } else {
                                    que_image.setVisibility(View.GONE);
                                }
                                tv.setText(questions[flag]);
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
                else {

                    postMCQ_Result(GlobalData.regno, GlobalData.classes, title, subject,answFire);
                }

            }
        });

        quitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  postMCQ_Result(GlobalData.regno, GlobalData.classes, title, subject);

            }
        });
    }

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

    private void submitfirebase(List<HashMap> answFire) {
        mProgressDialog.setMessage("Uploading Results ..please wait");
        mProgressDialog.show();
        // Access a Cloud Firestore instance from your Activity
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        try {
            Map<String, Object> updateMap = new HashMap();
            updateMap.put("field1", answFire);
            db.collection("unitTest")
                .add(updateMap)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(QuestionsActivity.this, "sent ...", Toast.LENGTH_SHORT).show();
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


                                int pos = 0;
                                for (int i = 0; i < response.body().getOptions_list().length; i++) {
                                    for (int j = 0; j < response.body().getOptions_list()[i].length; j++) {

                                        opt[pos] = response.body().getOptions_list()[i][j];
                                        pos = pos + 1;
                                    }
                                }

                                tv.setText(questions[flag]);
                                if (flags[flag].equals("true")) {
                                    Glide.with(QuestionsActivity.this)
                                        .load("http://139.59.79.78/media/" + image[flag])
                                        .into(que_image);
                                }
                                rb1.setText(opt[0]);
                                rb2.setText(opt[1]);
                                rb3.setText(opt[2]);
                                rb4.setText(opt[3]);

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

    private void submitCount() {
        mProgressDialog.setMessage("Uploading counts ..please wait");
        mProgressDialog.show();
        // Access a Cloud Firestore instance from your Activity
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        try {
            Map<String, Object> updateMap = new HashMap();
            HashMap<String, String> data = new HashMap<String, String>();
            data.put("count",String.valueOf(questions.length));
            data.put("reg",GlobalData.regno);
            data.put("subject",subject);
            data.put("title",title);
            data.put("time",formattedDate);
            updateMap.put("data", data);
            db.collection("unitCount")
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

    private void postMCQ_Answer(final String userid, String clas, String title, String subject, String question, String answer, String image) {
        try {
            mProgressDialog.show();
            final RetrofitClientInstance.GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(RetrofitClientInstance.GetDataService.class);
            Call<MCCQ> call = service.saveMCQ_Answer(GlobalData.regno, GlobalData.classes, title, subject, question, answer, image);
            call.enqueue(new Callback<MCCQ>() {
                @Override
                public void onResponse(Call<MCCQ> call, Response<MCCQ> response) {
                    mProgressDialog.cancel();


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

    private void postMCQ_Result(String userid, String clas, String title, String subject, final List<HashMap> answFire) {
        try {
            mProgressDialog.setMessage("Uploading results...");
            mProgressDialog.show();
            final RetrofitClientInstance.GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(RetrofitClientInstance.GetDataService.class);
            Call<MCQSubmit> call = service.saveMCQ_Result(GlobalData.regno, GlobalData.classes, title, subject);
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

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Your cannot press 'Back' , while in a TEST", Toast.LENGTH_SHORT).show();
    }

    void timer(final int length) {

        Long dur = Long.valueOf(time) * 60000;

        new CountDownTimer(dur, 1000) {

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

        }.start();


    }

    private void timedOUtUser(int flag) {
        mProgressDialog.setMessage("Uploading counts ..please wait");
        mProgressDialog.show();
        // Access a Cloud Firestore instance from your Activity
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        try {
            Map<String, Object> updateMap = new HashMap();
            HashMap<String, String> data = new HashMap<String, String>();
            data.put("reg",GlobalData.regno);
            data.put("q_count",String.valueOf(flag));
            updateMap.put("data", data);
            db.collection("timedOut")
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

    private void postMCQ_AnswerTieout(final int i, final int count, final String title, final String subject, String question, String answer, String image) {
        try {
            mProgressDialog.show();
            final RetrofitClientInstance.GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(RetrofitClientInstance.GetDataService.class);
            Call<MCCQ> call = service.saveMCQ_Answer(GlobalData.regno, GlobalData.classes, title, subject, question, answer, image);
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

}