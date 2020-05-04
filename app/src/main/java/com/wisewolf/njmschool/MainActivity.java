package com.wisewolf.njmschool;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.vimeo.networking.Configuration;
import com.vimeo.networking.VimeoClient;
import com.vimeo.networking.callbacks.AuthCallback;
import com.vimeo.networking.callbacks.ModelCallback;
import com.vimeo.networking.model.Video;
import com.vimeo.networking.model.VideoList;
import com.vimeo.networking.model.error.VimeoError;

public class MainActivity extends AppCompatActivity {
    ImageView education;
    TextView link;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        education=findViewById(R.id.education_gif);
        link=findViewById(R.id.videoLink);
        Glide.with(this).asGif().load(R.raw.educat).into(education);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();

        Intent msgIntent = new Intent(MainActivity.this, VideoLinkService.class);
        startService(msgIntent);
        Toast.makeText(this, "bnm", Toast.LENGTH_SHORT).show();

        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent msgIntent = new Intent(MainActivity.this, VideoListing.class);
                startActivity(msgIntent);
            }
        });





    }

}
