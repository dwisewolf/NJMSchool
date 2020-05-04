package com.wisewolf.njmschool;

import android.app.IntentService;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.vimeo.networking.Configuration;
import com.vimeo.networking.VimeoClient;
import com.vimeo.networking.callbacks.AuthCallback;
import com.vimeo.networking.callbacks.ModelCallback;
import com.vimeo.networking.model.VideoList;
import com.vimeo.networking.model.error.VimeoError;

import java.io.IOException;
import java.util.ArrayList;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class VideoLinkService extends IntentService {
    String uri = "/me/videos";


    ArrayList allVideoList = new ArrayList();
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "com.wisewolf.njmschool.action.FOO";
    private static final String ACTION_BAZ = "com.wisewolf.njmschool.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.wisewolf.njmschool.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.wisewolf.njmschool.extra.PARAM2";

    public VideoLinkService() {
        super("VideoLinkService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionFoo(Context context, String param1, String param2) {
        Intent intent = new Intent(context, VideoLinkService.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, VideoLinkService.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(this, "hai", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            Toast.makeText(this, "hello", Toast.LENGTH_SHORT).show();

            final Configuration.Builder configBuilder =
                new Configuration.Builder("70b8a941d1f7a9950d7c09d3abf322ba")
                    .setCacheDirectory(this.getCacheDir());
            VimeoClient.initialize(configBuilder.build());



            final VimeoClient mApiClient = VimeoClient.getInstance();
            // ---- Client Credentials Auth ----
            if (mApiClient.getVimeoAccount().getAccessToken() == null) {
                VimeoClient.getInstance().authorizeWithClientCredentialsGrant(new AuthCallback() {
                    @Override
                    public void success() {
                        String accessToken = VimeoClient.getInstance().getVimeoAccount().getAccessToken();
                        Configuration.Builder configBuilder =
                            new Configuration.Builder(accessToken);
                        VimeoClient.initialize(configBuilder.build());


                    }

                    @Override
                    public void failure(VimeoError error) {
                        String errorMessage = error.getDeveloperMessage();

                    }
                });
            }

            if (mApiClient.getVimeoAccount().getAccessToken() == null) {
                Toast.makeText(this, "ds", Toast.LENGTH_SHORT).show();
            }


            VimeoClient.getInstance().fetchNetworkContent(uri, new ModelCallback<VideoList>(VideoList.class) {
                @Override
                public void success(VideoList videoList) {

                    if (videoList != null && videoList.data != null && !videoList.data.isEmpty()) {
                        ArrayList allVideoList = new ArrayList();
                        if (GlobalData.allVideoList != null) {
                            allVideoList = GlobalData.allVideoList;
                        }

                        allVideoList.addAll(videoList.data);

                        GlobalData.allVideoList = (allVideoList);


                        allVideoList.addAll(videoList.data);
                        String last_num = videoList.paging.last;
                        String[] parts = last_num.split("=");
                        int x = Integer.parseInt(parts[1]);
                        Log.d("eff", allVideoList.toString());
                        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(VideoLinkService.this);

                        String size = sharedPrefs.getString("size", "");

                        if (!size.equals(String.valueOf(videoList.total))) {
                            for (int i = 2; i <= x; i++) {
                                String uri = String.valueOf(i);
                                uri = "/me/videos?page=" + uri;
                                callApi(uri);
                            }
                        }


                    }
                }

                @Override
                public void failure(VimeoError error) {

                }
            });

        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String param1, String param2) {
        // TODO: Handle action Foo
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }

    void callApi(final String url){

        VimeoClient.getInstance().fetchNetworkContent(url, new ModelCallback<VideoList>(VideoList.class) {
            @Override
            public void success(VideoList videoList) {

                if (videoList != null && videoList.data != null && !videoList.data.isEmpty()) {

                    ArrayList allVideoList=GlobalData.allVideoList;
                    allVideoList.addAll(videoList.data);

                    GlobalData.allVideoList=(allVideoList);
                    SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(VideoLinkService.this);
                    SharedPreferences.Editor editor = sharedPrefs.edit();
                    Gson gson = new Gson();

                    String json = gson.toJson(allVideoList);

                    editor.putString("Allvideolist", json);
                    editor.putString("size",String.valueOf(videoList.total));
                    try {
                        editor.putString("task",ObjectSerialiser.serialize(allVideoList));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    editor.commit();

                    Toast.makeText(VideoLinkService.this, "calling"+url, Toast.LENGTH_SHORT).show();


                }
            }

            @Override
            public void failure(VimeoError error) {

            }
        });



    }
}
