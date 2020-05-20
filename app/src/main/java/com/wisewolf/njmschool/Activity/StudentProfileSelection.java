package com.wisewolf.njmschool.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.vimeo.networking.model.Video;
import com.wisewolf.njmschool.Globals.GlobalData;
import com.wisewolf.njmschool.Models.Response;
import com.wisewolf.njmschool.R;
import com.wisewolf.njmschool.Adapter.StudentAdapter;

public class StudentProfileSelection extends AppCompatActivity {
RecyclerView studentList;
TextView parents;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student__select);
        parents = findViewById(R.id.parentsDetails);
        Response a = (Response) GlobalData.profiles.get(0);
        parents.setText(a.getFatherName() + "\nPhone - " + a.getMobileNum());

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();
        studentList = findViewById(R.id.child_select_list);
        studentList.setAdapter(new StudentAdapter(GlobalData.profiles, studentList, new StudentAdapter.OnItemClickListener() {


            @Override
            public void onItemClick(Response s) {

                GlobalData.regno=s.getRegNum();
                Intent intent = new Intent(StudentProfileSelection.this, VideoListing.class);
                intent.putExtra("name", s.getName());
                intent.putExtra("class", s.getClas());
                intent.putExtra("sec", s.getSection());
                intent.putExtra("phone", s.getMobileNum());
                startActivity(intent);
            }
        }));
        LinearLayoutManager added_liste_adapterlayoutManager
            = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        studentList.setLayoutManager(added_liste_adapterlayoutManager);
    }
}
