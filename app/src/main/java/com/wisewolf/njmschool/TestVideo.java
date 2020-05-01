package com.wisewolf.njmschool;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;

public class TestVideo extends AppCompatActivity {
    ArrayList arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_video);
arrayList=GlobalData.allVideoList;
        Toast.makeText(this, "dfds", Toast.LENGTH_SHORT).show();
    }
}
