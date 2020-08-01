package com.wisewolf.njmschool.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.jsibbold.zoomage.ZoomageView;
import com.wisewolf.njmschool.Globals.GlobalData;
import com.wisewolf.njmschool.Models.MCCQ;
import com.wisewolf.njmschool.Models.MCQSubmit;
import com.wisewolf.njmschool.Models.QuizQuestion;
import com.wisewolf.njmschool.R;
import com.wisewolf.njmschool.RetrofitClientInstance;

import java.util.Locale;
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
    ProgressDialog mProgressDialog;
    String name = "", title, subject, time;
    TextView textView;
    public static int marks = 0, correct = 0, wrong = 0;
    CountDownTimer countDownTimer;

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


        final TextView score = (TextView) findViewById(R.id.textView4);
        textView = (TextView) findViewById(R.id.DispName);
        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        subject = intent.getStringExtra("subject");
        time = intent.getStringExtra("time");
que_image=findViewById(R.id.que_image);


        submitbutton = (Button) findViewById(R.id.button3);
        quitbutton = (Button) findViewById(R.id.buttonquit);
        tv = (TextView) findViewById(R.id.tvque);

        radio_g = (RadioGroup) findViewById(R.id.answersgrp);
        rb1 = (RadioButton) findViewById(R.id.radioButton);
        rb2 = (RadioButton) findViewById(R.id.radioButton2);
        rb3 = (RadioButton) findViewById(R.id.radioButton3);
        rb4 = (RadioButton) findViewById(R.id.radioButton4);


        getQuizHead(title);
        submitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //int color = mBackgroundColor.getColor();
                //mLayout.setBackgroundColor(color);

                if (radio_g.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(getApplicationContext(), "Please select one choice", Toast.LENGTH_SHORT).show();
                    return;
                } else {

                    RadioButton uans = (RadioButton) findViewById(radio_g.getCheckedRadioButtonId());
                    String ansText = uans.getText().toString();
//              Toast.makeText(getApplicationContext(), ansText, Toast.LENGTH_SHORT).show();
                    postMCQ_Answer(GlobalData.regno, GlobalData.clas, title, subject, questions[flag], ansText,image[flag]);
                    if (ansText.equals(answers[flag])) {
                        correct++;

                    } else {
                        wrong++;

                    }

                    flag++;

                    if (score != null)
                        score.setText("" + correct);

                    if (flag < questions.length) {
                        if (flags[flag].equals("true")) {
                            que_image.setVisibility(View.VISIBLE);
                            Glide.with(QuestionsActivity.this)
                                .load("http://139.59.79.78/media/"+image[flag])
                                .into(que_image);
                        }
                        else {
                            que_image.setVisibility(View.GONE);
                        }
                        tv.setText(questions[flag]);
                        rb1.setText(opt[flag * 4]);
                        rb2.setText(opt[flag * 4 + 1]);
                        rb3.setText(opt[flag * 4 + 2]);
                        rb4.setText(opt[flag * 4 + 3]);
                    } else {
                        marks = correct;
                        postMCQ_Result(GlobalData.regno, GlobalData.classes, title, subject);

                    }
                    radio_g.clearCheck();

                }

            }
        });

        quitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postMCQ_Result(GlobalData.regno, GlobalData.classes, title, subject);

            }
        });
    }

    private void initProgress() {
        mProgressDialog = new ProgressDialog(QuestionsActivity.this);
        mProgressDialog.setMessage("Loading. . .");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(true);

    }

    private void getQuizHead( String title) {
        mProgressDialog.show();

        try {
            final RetrofitClientInstance.GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(RetrofitClientInstance.GetDataService.class);
            Call<QuizQuestion> call = service.get_QuizQuestions(GlobalData.school_code, GlobalData.classes, title);
            call.enqueue(new Callback<QuizQuestion>() {
                @Override
                public void onResponse(Call<QuizQuestion> call, Response<QuizQuestion> response) {
                    try {
                        mProgressDialog.cancel();
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
                                    .load("http://139.59.79.78/media/"+image[flag])
                                .into(que_image);
                            }
                            rb1.setText(opt[0]);
                            rb2.setText(opt[1]);
                            rb3.setText(opt[2]);
                            rb4.setText(opt[3]);
                        } else {
                            Intent in = new Intent(getApplicationContext(), ExamActivity.class);
                            startActivity(in);

                            Toast.makeText(QuestionsActivity.this, "No Questions Added", Toast.LENGTH_SHORT).show();
                        }


                    } catch (Exception e) {
                        Toast.makeText(QuestionsActivity.this, "Your Network Error ..please restart App (QA188)", Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onFailure(Call<QuizQuestion> call, Throwable t) {
                    mProgressDialog.cancel();
                    String a = "";
                }
            });
        } catch (Exception ignored) {
            mProgressDialog.cancel();
            String a = "";
        }


    }

    private void postMCQ_Answer(String userid, String clas, String title, String subject, String question, String answer,String image) {
        try {
            mProgressDialog.show();
            final RetrofitClientInstance.GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(RetrofitClientInstance.GetDataService.class);
            Call<MCCQ> call = service.saveMCQ_Answer(GlobalData.regno, GlobalData.classes, title, subject, question, answer,image);
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

    private void postMCQ_Result(String userid, String clas, String title, String subject) {
        try {
            mProgressDialog.show();
            final RetrofitClientInstance.GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(RetrofitClientInstance.GetDataService.class);
            Call<MCQSubmit> call = service.saveMCQ_Result(GlobalData.regno, GlobalData.classes, title, subject);
            call.enqueue(new Callback<MCQSubmit>() {
                @Override
                public void onResponse(Call<MCQSubmit> call, Response<MCQSubmit> response) {
                    mProgressDialog.cancel();
                    Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
                    startActivity(intent);
                }

                @Override
                public void onFailure(Call<MCQSubmit> call, Throwable t) {
                    String a = "";
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

        Long dur=Long.valueOf(time)*60000;

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
                if (flag!=length){
                    int count=length-flag;
                    for (int i=0;i<count;i++){
                        postMCQ_Answer(GlobalData.regno, GlobalData.clas, title, subject, questions[flag], "TIMED OUT","");
                        wrong++;

                    }
                    marks = correct;
                    postMCQ_Result(GlobalData.regno, GlobalData.clas, title, subject);
                }



            }

        }.start();


    }
}