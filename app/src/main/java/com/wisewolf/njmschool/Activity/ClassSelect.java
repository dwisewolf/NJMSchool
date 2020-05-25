package com.wisewolf.njmschool.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import com.wisewolf.njmschool.Adapter.TeacherClass_Adapter;
import com.wisewolf.njmschool.Globals.GlobalData;
import com.wisewolf.njmschool.R;

public class ClassSelect extends AppCompatActivity {
    RecyclerView classList;
    String[] TeacherClass={"NURSERY","LKG","UKG","CLASS I","CLASS II","CLASS III","CLASS IV","CLASS V","CLASS VI","CLASS VII","CLASS VIII","CLASS IX","CLASS X","CLASS XI","CLASS XII"};
String name,clas,phone,sect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_select);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();

        Intent intent=getIntent();
        getData(intent);

        classList = findViewById(R.id.class_list);
        classList.setAdapter(new TeacherClass_Adapter(TeacherClass,ClassSelect.this, classList, new TeacherClass_Adapter.OnItemClickListener() {


            @Override
            public void onItemClick(String item) {
                String classid="";
                if (item.equals("NURSERY")) {classid="PREP";
                } else if (item.equals("LKG")) {classid="LKG";
                } else if (item.equals("UKG")) {classid="UKG";
                } else if (item.equals("CLASS I")) {classid="I";
                } else if (item.equals("CLASS II")) {classid="II";
                } else if (item.equals("CLASS III")) {classid="III";
                } else if (item.equals("CLASS IV")) {classid="IV";
                } else if (item.equals("CLASS V")) {classid="V";
                } else if (item.equals("CLASS VI")) {classid="VI";
                } else if (item.equals("CLASS VII")) {classid="VII";
                } else if (item.equals("CLASS VIII")) {classid="VIII";
                } else if (item.equals("CLASS IX")) {classid="IX";
                } else if (item.equals("CLASS X")) {classid="X";
                } else if (item.equals("CLASS XI")) {classid="XI";
                } else if (item.equals("CLASS XII")) {classid="XII";
                }
                else classid="";



                Intent intent = new Intent(ClassSelect.this, VideoListing.class);
                intent.putExtra("name", name);

                intent.putExtra("class", classid);
                intent.putExtra("sec",classid);
                intent.putExtra("phone", phone);
                startActivity(intent);
            }


        }));
        LinearLayoutManager added_liste_adapterlayoutManager
            = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        classList.setLayoutManager(added_liste_adapterlayoutManager);
    }
    private void getData(Intent intent) {


        name=(intent.getStringExtra("name"));
        phone=(intent.getStringExtra("phone"));



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
finishAffinity();
    }
}
