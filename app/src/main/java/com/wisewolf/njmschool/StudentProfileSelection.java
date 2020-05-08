package com.wisewolf.njmschool;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.vimeo.networking.model.Video;

public class StudentProfileSelection extends AppCompatActivity {
RecyclerView studentList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student__select);
        studentList=findViewById(R.id.child_select_list);
        studentList.setAdapter(new StudentAdapter(GlobalData.profiles, studentList, new  StudentAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String s) {

            }




        }));
        LinearLayoutManager added_liste_adapterlayoutManager
            = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        studentList.setLayoutManager(added_liste_adapterlayoutManager);
    }
}
