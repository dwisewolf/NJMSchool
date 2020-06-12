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

import com.wisewolf.njmschool.Adapter.TeacherClass_Adapter;
import com.wisewolf.njmschool.Globals.GlobalData;
import com.wisewolf.njmschool.Models.ClassVideo;
import com.wisewolf.njmschool.Models.SchoolDiff;
import com.wisewolf.njmschool.R;
import com.wisewolf.njmschool.RetrofitClientInstance;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClassSelect extends AppCompatActivity {
    RecyclerView classList;
    String[] TeacherClass = {"NURSERY", "LKG", "UKG", "CLASS I", "CLASS II",
        "CLASS III", "CLASS IV", "CLASS V", "CLASS VI", "CLASS VII", "CLASS VIII",
        "CLASS VI-NCERT", "CLASS VII-NCERT", "CLASS VIII-NCERT",
        "CLASS IX", "CLASS X", "CLASS XII_C", "CLASS XII_H", "CLASS XII_S"};
    String name, clas, school, phone, sect;
    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_select);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();

        Intent intent = getIntent();
        getData(intent);

        mProgressDialog = new ProgressDialog(ClassSelect.this);
        mProgressDialog.setMessage("Loading. . .");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(true);

        classList = findViewById(R.id.class_list);
        classList.setAdapter(new TeacherClass_Adapter(TeacherClass, ClassSelect.this, classList, new TeacherClass_Adapter.OnItemClickListener() {

            @Override
            public void onItemClick(String item) {
                String classid = "";
                if (item.equals("NURSERY")) {
                    classid = "PREP";
                } else if (item.equals("LKG")) {
                    classid = "LKG";
                } else if (item.equals("UKG")) {
                    classid = "UKG";
                } else if (item.equals("CLASS I")) {
                    classid = "I";
                } else if (item.equals("CLASS II")) {
                    classid = "II";
                } else if (item.equals("CLASS III")) {
                    classid = "III";
                } else if (item.equals("CLASS IV")) {
                    classid = "IV";
                } else if (item.equals("CLASS V")) {
                    classid = "V";
                } else if (item.equals("CLASS VI")) {
                    classid = "VI";
                } else if (item.equals("CLASS VII")) {
                    classid = "VII";
                } else if (item.equals("CLASS VIII")) {
                    classid = "VIII";
                } else if (item.equals("CLASS VI-NCERT")) {
                    classid = "VI";
                    GlobalData.regno = "AND" + GlobalData.regno;
                } else if (item.equals("CLASS VII-NCERT")) {
                    classid = "VII";
                    GlobalData.regno = "AND" + GlobalData.regno;
                } else if (item.equals("CLASS VIII-NCERT")) {
                    classid = "VIII";
                    GlobalData.regno = "AND" + GlobalData.regno;
                } else if (item.equals("CLASS IX")) {
                    classid = "IX";
                } else if (item.equals("CLASS X")) {
                    classid = "X";
                } else if (item.equals("CLASS XI")) {
                    classid = "XI";
                } else if (item.equals("CLASS XII_C")) {
                    classid = "XII";
                    GlobalData.sect = "C";
                } else if (item.equals("CLASS XII_H")) {
                    classid = "XII";
                    GlobalData.sect = "H";
                } else if (item.equals("CLASS XII_S")) {
                    classid = "XII";
                    GlobalData.sect = "S";
                } else classid = "";

                mProgressDialog.show();
                getClassVideo(school.substring(0, 1), classid, name, GlobalData.sect, phone);

            }


        }));
        LinearLayoutManager added_liste_adapterlayoutManager
            = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        classList.setLayoutManager(added_liste_adapterlayoutManager);
    }

    private void getData(Intent intent) {


        name = (intent.getStringExtra("name"));
        phone = (intent.getStringExtra("phone"));
        school = (intent.getStringExtra("school"));

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    private void getClassVideo(String school, String u_class, final String name, String sect, final String phone) {
        clas = u_class;
        u_class = findClass(u_class);
        try {
            final RetrofitClientInstance.GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(RetrofitClientInstance.GetDataService.class);
            Call<List<ClassVideo>> call = service.getVideos(school, u_class);
            call.enqueue(new Callback<List<ClassVideo>>() {
                @Override
                public void onResponse(Call<List<ClassVideo>> call, Response<List<ClassVideo>> response) {

                    GlobalData.allVideoList = (ArrayList) response.body();
                    mProgressDialog.cancel();

                    Intent intent = new Intent(ClassSelect.this, VideoListing.class);
                    intent.putExtra("name", name);

                    intent.putExtra("class", clas);
                    intent.putExtra("sec", GlobalData.sect);
                    intent.putExtra("phone", phone);
                    startActivity(intent);
                }

                @Override
                public void onFailure(Call<List<ClassVideo>> call, Throwable t) {

                }
            });

            // Response<List<ClassVideo>> response=   call.execute();

        } catch (Exception e) {

        }

    }

    private String findClass(String clas) {
        String clasS = "";
        switch (clas) {
            case "PREP":
                clasS = "NR";
                break;
            case "LKG":
                clasS = "LK";
                break;
            case "UKG":
                clasS = "UK";
                break;
            case "XII":
            case "XI":
                clasS = "C12";
                break;
            case "X":
                clasS = "C10";
                break;
            case "IX":
                clasS = "C9";
                break;
            case "VIII":
                clasS = "C8";
                break;
            case "VII":
                clasS = "C7";
                break;
            case "VI":
                clasS = "C6";
                break;
            case "V":
                clasS = "C5";
                break;
            case "IV":
                clasS = "C4";
                break;
            case "III":
                clasS = "C3";
                break;
            case "II":
                clasS = "C2";
                break;
            case "I":
                clasS = "C1";
                break;
        }
        return clasS;
    }
}
