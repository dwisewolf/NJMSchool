package com.wisewolf.njmschool.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.wisewolf.njmschool.Adapter.DailyTaskAdapter;
import com.wisewolf.njmschool.Database.OfflineDatabase;
import com.wisewolf.njmschool.Globals.GlobalData;
import com.wisewolf.njmschool.Models.DailyTask;
import com.wisewolf.njmschool.R;
import com.wisewolf.njmschool.RetrofitClientInstance;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DailyTask_Activity extends AppCompatActivity {
    RecyclerView dailyTaskList;
    TextView datess, noNotes;
    String formattedDate;
    List<DailyTask> todaysVideo;
    OfflineDatabase dbb;
    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_task);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();


        dailyTaskList = findViewById(R.id.taskList);
        datess = findViewById(R.id.date_id);
        noNotes = findViewById(R.id.textView13);
        noNotes.setVisibility(View.GONE);

        mProgressDialog = new ProgressDialog(DailyTask_Activity.this);
        mProgressDialog.setMessage("Loading. . .");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();


        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);


        dbb = new OfflineDatabase(getApplicationContext());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        formattedDate = df.format(c);





        datess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateSelect();
            }
        });

        getDailyTask(GlobalData.school_code,GlobalData.classes,formattedDate);

    }

    private void dateSelect() {
        final Calendar dateSelected = Calendar.getInstance();

        DatePickerDialog datePickerDialog;
        Calendar newCalendar = dateSelected;
        final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                dateSelected.set(year, monthOfYear, dayOfMonth, 0, 0);
                datess.setText(dateFormatter.format(dateSelected.getTime()));
                getDailyTask(GlobalData.school_code,GlobalData.classes,dateFormatter.format(dateSelected.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
        //datess.setText(dateFormatter.format(dateSelected.getTime()));
    }

    private void getDailyTask(final String school, String finalU_class,String dt) {
        try {
            final RetrofitClientInstance.GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(RetrofitClientInstance.GetDataService.class);
            Call<List<DailyTask>> call = service.get_DailyTask(school,finalU_class,dt);
            call.enqueue(new Callback<List<DailyTask>>() {
                @Override
                public void onResponse(Call<List<DailyTask>> call, Response<List<DailyTask>> response) {
                    GlobalData.dailyTaskList=response.body();
                    todaysVideo=response.body();
                    datess.setText(GlobalData.dailyTaskList.get(0).getDate());
                    mProgressDialog.cancel();
                  videoList();
                }

                @Override
                public void onFailure(Call<List<DailyTask>> call, Throwable t) {
                    mProgressDialog.cancel();
                    todaysVideo=null;
                    videoList();
                }
            });
        }catch (Exception ignored){
            String a="";
        }


    }

    private void videoList() {
        if (todaysVideo != null&& todaysVideo.size()!=0) {
            dailyTaskList.setVisibility(View.VISIBLE);
            noNotes.setVisibility(View.GONE);
            noNotes.setText("");
            dailyTaskList.setAdapter(new DailyTaskAdapter(todaysVideo, dailyTaskList, DailyTask_Activity.this, formattedDate, new DailyTaskAdapter.OnItemClickListener() {


                @Override
                public void onItemClick(final DailyTask s) {

                }
            }));
            LinearLayoutManager added_liste_adapterlayoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            dailyTaskList.setLayoutManager(added_liste_adapterlayoutManager);


        } else {
            noNotes.setVisibility(View.VISIBLE);
            dailyTaskList.setVisibility(View.GONE);
            noNotes.setText("No tasks added");
        }
    }

}