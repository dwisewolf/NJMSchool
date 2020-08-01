package com.wisewolf.njmschool.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import com.wisewolf.njmschool.Adapter.DailyTaskAdapter;
import com.wisewolf.njmschool.Adapter.NewsAdapter;
import com.wisewolf.njmschool.Database.OfflineDatabase;
import com.wisewolf.njmschool.Globals.GlobalData;
import com.wisewolf.njmschool.Models.DailyTask;
import com.wisewolf.njmschool.Models.News;
import com.wisewolf.njmschool.R;

import java.util.List;

public class NEwsListActivity extends AppCompatActivity {
    RecyclerView newRecycler;


    List<News> newsL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_n_ews_list);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();
        newRecycler=findViewById(R.id.news_recycler);


        newRecycler.setAdapter(new NewsAdapter(GlobalData.newsForstudent, newRecycler, NEwsListActivity.this));
        LinearLayoutManager added_liste_adapterlayoutManager
            = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        newRecycler.setLayoutManager(added_liste_adapterlayoutManager);
    }
}