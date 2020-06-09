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
import com.wisewolf.njmschool.Globals.GlobalData;
import com.wisewolf.njmschool.Models.News;
import com.wisewolf.njmschool.Models.Quotes;
import com.wisewolf.njmschool.Models.SchoolDiff;
import com.wisewolf.njmschool.R;
import com.wisewolf.njmschool.Adapter.StudentAdapter;
import com.wisewolf.njmschool.RetrofitClientInstance;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentProfileSelection extends AppCompatActivity {
    RecyclerView studentList;
    TextView parents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student__select);
        parents = findViewById(R.id.parentsDetails);

        try {
            SchoolDiff a = (SchoolDiff) GlobalData.profiles.get(0);
            parents.setText(a.getFatherName() + "\nPhone - " + a.getMobileNum());
        } catch (Exception e) {

        }


        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();
        studentList = findViewById(R.id.child_select_list);
        // callQuote();
        try {
            studentList.setAdapter(new StudentAdapter(GlobalData.profiles, studentList, new StudentAdapter.OnItemClickListener() {


                @Override
                public void onItemClick(SchoolDiff s) {
                    if (s.getCategory().equals("TEACHER")) {
                        GlobalData.regno = s.getUserid();
                        Intent intent = new Intent(StudentProfileSelection.this, ClassSelect.class);
                        intent.putExtra("name", s.getName());
                        intent.putExtra("phone", s.getMobileNum());
                        startActivity(intent);
                    }
                    if (s.getCategory().equals("STUDENT")) {
                        GlobalData.regno = s.getUserid();
                        getnews(s.getUserid());
                        Intent intent = new Intent(StudentProfileSelection.this, VideoListing.class);
                        intent.putExtra("name", s.getName());
                        intent.putExtra("class", s.getClas());
                        intent.putExtra("sec", s.getCategory());
                        intent.putExtra("phone", String.valueOf(s.getMobileNum()));
                        startActivity(intent);
                    }


                }
            }));
            LinearLayoutManager added_liste_adapterlayoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            studentList.setLayoutManager(added_liste_adapterlayoutManager);
        } catch (Exception e) {
            Toast.makeText(this, "Your Network Error ..please restart App (SPS84)", Toast.LENGTH_SHORT).show();
            FirebaseCrashlytics.getInstance().log(String.valueOf(e));
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
        }catch (Exception e){
            String a="";
        }


    }

}
