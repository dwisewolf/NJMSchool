package com.wisewolf.njmschool.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.wisewolf.njmschool.Adapter.QuizHeadAdapter;
import com.wisewolf.njmschool.Adapter.StudentAdapter;
import com.wisewolf.njmschool.Globals.GlobalData;
import com.wisewolf.njmschool.Models.News;
import com.wisewolf.njmschool.Models.QuizHead;
import com.wisewolf.njmschool.Models.SchoolDiff;
import com.wisewolf.njmschool.R;
import com.wisewolf.njmschool.RetrofitClientInstance;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import kotlin.text.Regex;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExamActivity extends AppCompatActivity {
RecyclerView mcq_recycler;
    String formattedDate="";
    TextView mcqHead;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();

        getDate();

        mcq_recycler=findViewById(R.id.mcq_recycler);
        mcqHead=findViewById(R.id.mcq_head);
        getQuizHead(GlobalData.school_code,GlobalData.regno,GlobalData.classes);
    }

    private void getDate() {
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA.getDefault());
          formattedDate = df.format(c);
    }


    private void getQuizHead( String school, String regNo,String clas) {
        try {
            final RetrofitClientInstance.GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(RetrofitClientInstance.GetDataService.class);
            Call<List<QuizHead>> call = service.get_QuizHead(school, regNo,clas,formattedDate);
            call.enqueue(new Callback<List<QuizHead>>() {
                @Override
                public void onResponse(Call<List<QuizHead>> call, final Response<List<QuizHead>> response) {
                    try {
                        if (response.body() != null) {
                            mcq_recycler.setAdapter(new QuizHeadAdapter(response.body(), mcq_recycler, new QuizHeadAdapter.OnItemClickListener() {

                                @Override
                                public void onItemClick(QuizHead s) {
                                 if (s.getDate().equals(formattedDate)) {
                                     Intent intent = new Intent(ExamActivity.this, QuestionsActivity.class);
                                     intent.putExtra("title", s.getTitle());
                                     intent.putExtra("subject", s.getSubject());
                                     intent.putExtra("time", s.getTime());
                                     startActivity(intent);
                                 }else
                                     Toast.makeText(ExamActivity.this, "Exam Date is on "+s.getDate(), Toast.LENGTH_SHORT).show();
                                }
                            }));
                            LinearLayoutManager added_liste_adapterlayoutManager
                                = new LinearLayoutManager(ExamActivity.this, LinearLayoutManager.VERTICAL, false);
                            mcq_recycler.setLayoutManager(added_liste_adapterlayoutManager);
                        }
                        else {Toast.makeText(ExamActivity.this, "No Test Available", Toast.LENGTH_LONG).show(); mcqHead.setText("MCQ not available for today");}
                    } catch (Exception e)
                    {
                        Toast.makeText(ExamActivity.this, "Your Network Error ..please restart App (SPS84)", Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onFailure(Call<List<QuizHead>> call, Throwable t) {
                    mcqHead.setText("MCQ not available for today");
                    Toast.makeText(ExamActivity.this, "MCQ not available for today", Toast.LENGTH_LONG).show();
                    String a="";
                }
            });
        }catch (Exception ignored){
            String a="";
        }


    }
}
