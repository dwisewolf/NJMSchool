package com.wisewolf.njmschool.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import com.wisewolf.njmschool.Globals.GlobalData;
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
        SchoolDiff a = (SchoolDiff) GlobalData.profiles.get(0);
        parents.setText(a.getFatherName() + "\nPhone - " + a.getMobileNum());

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();
        studentList = findViewById(R.id.child_select_list);
       // callQuote();

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
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
       finishAffinity();
    }

    private void callQuote() {
        try {
            final RetrofitClientInstance.GetDataService service = RetrofitClientInstance.getRetrofitInstance1("https://quotes.rest/").create(RetrofitClientInstance.GetDataService.class);
            Call<List<Quotes>> call = service.getQuote();
            call.enqueue(new Callback<List<Quotes>>() {
                @Override
                public void onResponse(Call<List<Quotes>> call, Response<List<Quotes>> response) {
                    String a="";
                }

                @Override
                public void onFailure(Call<List<Quotes>> call, Throwable t) {
String a="";
                }
            });
        }catch (Exception e){
            String a="";
        }


    }

}
