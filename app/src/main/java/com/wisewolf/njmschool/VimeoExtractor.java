package com.wisewolf.njmschool;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.vimeo.networking.Configuration;
import com.vimeo.networking.VimeoClient;
import com.vimeo.networking.callbacks.ModelCallback;
import com.vimeo.networking.model.VideoList;
import com.vimeo.networking.model.error.VimeoError;
import com.wisewolf.njmschool.Exceptions.VimeoException;
import com.wisewolf.njmschool.Exceptions.VimeoExceptionEnumType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import vimeoextractor.OnVimeoExtractionListener;
import vimeoextractor.VimeoVideo;


public class VimeoExtractor {

    private static String VIMEO_URL_CONFIG = "/me/videos/418218268";

    private String vimeoURL;
    private VimeoQuality vimeoQuality;
    private RequestQueue requestQueque;
    private VimeoCallback callback;

    /*
    Public constructor of vimeo Extractor
     */
    public VimeoExtractor(String vimeoURLID, Context context, VimeoQuality vimeoQuality, VimeoCallback callback) {
        this.vimeoURL = VIMEO_URL_CONFIG.replace("video_id", vimeoURLID);
        requestQueque = Volley.newRequestQueue(context);
        this.vimeoQuality = vimeoQuality;
        this.callback = callback;
    }

    public void downloadVideoFile() {

        final Configuration.Builder configBuilder =
            new Configuration.Builder("70b8a941d1f7a9950d7c09d3abf322ba");
        VimeoClient.initialize(configBuilder.build());


        final VimeoClient mApiClient = VimeoClient.getInstance();
        try {
            VimeoClient.getInstance().fetchNetworkContent(VIMEO_URL_CONFIG, new ModelCallback<VideoList>(VideoList.class) {
                @Override
                public void success(VideoList videoList) {

                    if (videoList != null && videoList.data != null && !videoList.data.isEmpty()) {
                        ArrayList allVideoList = new ArrayList();

                    }
                }

                @Override
                public void failure(VimeoError error) {
                    callback.vimeoURLCallback("url");

                }
            });
        }
        catch (Exception e)
        {
            callback.vimeoURLCallback("url");
        }



        callback.vimeoURLCallback("url");

    }
}
