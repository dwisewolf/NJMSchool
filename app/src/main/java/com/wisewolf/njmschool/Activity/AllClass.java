package com.wisewolf.njmschool.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.AsyncTask;
import android.os.Bundle;

import com.vimeo.networking.VimeoClient;
import com.vimeo.networking.callbacks.ModelCallback;
import com.vimeo.networking.model.VideoList;
import com.vimeo.networking.model.error.VimeoError;
import com.wisewolf.njmschool.R;

import java.util.ArrayList;

public class AllClass extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_class);
    }
    private void download() {
        Downback DB = new Downback();
        DB.execute("");
    }
    private class Downback extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            calcHandView();
        }

        @Override
        protected String doInBackground(String... strings) {
            String uri ;
            int i=pageWidth;
            uri = "/me/projects/2074244/videos";
            VimeoClient.getInstance().fetchNetworkContent(uri, new ModelCallback<VideoList>(VideoList.class) {
                @Override
                public void success(VideoList videoList) {

                    if (videoList != null && videoList.data != null && !videoList.data.isEmpty()) {

                        ArrayList allVideoList= new ArrayList();
                        allVideoList=(videoList.data);


                        PackageVideoAdapter PackageVideoAdapter = new
                            PackageVideoAdapter(allVideoList, video_list,i,pageLength);
                        video_list.setAdapter(PackageVideoAdapter);
                        LinearLayoutManager added_liste_adapterlayoutManager
                            = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                        video_list.setLayoutManager(added_liste_adapterlayoutManager);

                    }
                }

                @Override
                public void failure(VimeoError error) {
                    String a=String.valueOf(error);
                }
            });
            return null;

        }


    }
}
