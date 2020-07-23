package com.wisewolf.njmschool.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.wisewolf.njmschool.Globals.GlobalData;
import com.wisewolf.njmschool.Models.ClassVideo;
import com.wisewolf.njmschool.Models.DailyTask;
import com.wisewolf.njmschool.Models.News;
import com.wisewolf.njmschool.Models.SchoolDiff;
import com.wisewolf.njmschool.Models.TeacherDetails;
import com.wisewolf.njmschool.R;
import com.wisewolf.njmschool.Adapter.StudentAdapter;
import com.wisewolf.njmschool.RetrofitClientInstance;

import java.util.ArrayList;
import java.util.List;

import kotlin.text.Regex;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentProfileSelection extends AppCompatActivity {
    RecyclerView studentList;
    ProgressDialog mProgressDialog;
    TextView parents;
    String u_class,school;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student__select);
        parents = findViewById(R.id.parentsDetails);

        try {
            SchoolDiff a = (SchoolDiff) GlobalData.profiles.get(0);
            parents.setText(a.getFatherName() + "\nPhone - " + a.getMobileNum());
        }
        catch (Exception e) {
            parents.setText("Student blocked by Office");
        }

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();

        studentList = findViewById(R.id.child_select_list);
        mProgressDialog = new ProgressDialog(StudentProfileSelection.this);
        mProgressDialog.setMessage("Loading. . .");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(true);
        // callQuote();
        try {
            studentList.setAdapter(new StudentAdapter(GlobalData.profiles, studentList, new StudentAdapter.OnItemClickListener() {


                @Override
                public void onItemClick(SchoolDiff s) {
                    if (s.getCategory().equals("TEACHER")) {
                        GlobalData.regno = s.getUserid();
                        GlobalData.name = s.getName();
                        String original = s.getUserid();
                        Regex reg = new Regex("(?<=[0-9])(?=[A-Za-z])");
                        String[] parts = reg.split(original, 2).toArray(new String[0]);
                        Intent intent = new Intent(StudentProfileSelection.this, ClassSelect.class);
                        intent.putExtra("name", s.getName());
                        intent.putExtra("school", parts[1]);
                        intent.putExtra("phone", s.getMobileNum());
                        startActivity(intent);
                    }
                    if (s.getCategory().equals("STUDENT")) {
                        GlobalData.regno = s.getUserid();
                        GlobalData.name = s.getName();
                         getnews(s.getUserid());

                        school=s.getUserid().substring(0,1);
                        u_class=s.getClas();
                        mProgressDialog.show();

                        getClassVideo(school,u_class,s,s.getUserid().substring(0,3));



                    }


                }
            }));
            LinearLayoutManager added_liste_adapterlayoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            studentList.setLayoutManager(added_liste_adapterlayoutManager);
        } catch (Exception e)
        {
            Toast.makeText(this, "Your Network Error ..please restart App (SPS84)", Toast.LENGTH_SHORT).show();
            FirebaseCrashlytics.getInstance().log(String.valueOf(e));
        }

    }

    private void getClassVideo(final String school, String u_class, final SchoolDiff s, final String schoolname) {
        final String cls=u_class;
        u_class = findClass(u_class);
        try {
            final RetrofitClientInstance.GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(RetrofitClientInstance.GetDataService.class);
            Call<List<ClassVideo>> call = service.getVideos(school, u_class);
            final String finalU_class = u_class;
            call.enqueue(new Callback<List<ClassVideo>>() {
                @Override
                public void onResponse(Call<List<ClassVideo>> call, Response<List<ClassVideo>> response)
                {

                    GlobalData.allVideoList = (ArrayList) response.body();
                    GlobalData.school_code=schoolname;
                    GlobalData.classes=cls;
                    getDailyTask(schoolname, cls,s.getName(),s.getClas(),s.getSection().replaceAll("\\s+",""),String.valueOf(s.getMobileNum()));

                }

                @Override
                public void onFailure(Call<List<ClassVideo>> call, Throwable t) {

                }
            });

            // Response<List<ClassVideo>> response=   call.execute();

        } catch (Exception e) {

        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
       finishAffinity();
    }

    private void getnews(String userid) {
        try {
            final RetrofitClientInstance.GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(RetrofitClientInstance.GetDataService.class);
            Call<List<News>> call = service.get_News(userid);
            call.enqueue(new Callback<List<News>>() {
                @Override
                public void onResponse(Call<List<News>> call, Response<List<News>> response) {
                    String a="";
                }

                @Override
                public void onFailure(Call<List<News>> call, Throwable t) {
String a="";
                }
            });
        }catch (Exception ignored){
        }


    }

    private void getDailyTask(final String school, String finalU_class, final String name, final String clasD, final String sec, final String phone) {
        try {
            final RetrofitClientInstance.GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(RetrofitClientInstance.GetDataService.class);
            Call<List<TeacherDetails>> call = service.get_teacherDetails(school,finalU_class);
            call.enqueue(new Callback<List<TeacherDetails>>() {
                @Override
                public void onResponse(Call<List<TeacherDetails>> call, Response<List<TeacherDetails>> response) {
                    GlobalData.teacherDetails = response.body();
                    mProgressDialog.cancel();
                    Intent intent = new Intent(StudentProfileSelection.this, VideoListing.class);
                    intent.putExtra("name", name);
                    intent.putExtra("class", clasD);
                    intent.putExtra("sec", sec);
                    intent.putExtra("phone", phone);
                    startActivity(intent);

                }

                @Override
                public void onFailure(Call<List<TeacherDetails>> call, Throwable t) {
                    mProgressDialog.cancel();
                    Intent intent = new Intent(StudentProfileSelection.this, VideoListing.class);
                    intent.putExtra("name", name);
                    intent.putExtra("class", clasD);
                    intent.putExtra("sec", sec);
                    intent.putExtra("phone", phone);
                    startActivity(intent);
                }
            });


        }catch (Exception ignored){
            String a="";
        }


    }

    private String findClass(String clas) {
        String clasS = "";
        switch(clas) {
            case "PREP":
                clasS=   "NR";
                break;
            case "LKG":
                clasS="LK";
                break;
            case "UKG":
                clasS= "UK";
                break;
            case "XII":
            case "XI":
                clasS= "C12";
                break;
            case "X":
                clasS="C10";
                break;
            case "IX":
               clasS= "C9";
                break;
            case "VIII":
               clasS= "C8";
                break;
            case "VII":
               clasS= "C7";
                break;
            case "VI":
               clasS= "C6";
                break;
            case "V":
               clasS= "C5";
                break;
            case "IV":
               clasS= "C4";
                break;
            case "III":
               clasS= "C3";
                break;
            case "II":
               clasS= "C2";
                break;
            case "I":
               clasS= "C1";
                break;
        }
        return  clasS;
    }

}