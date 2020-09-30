package com.wisewolf.njmschool.Activity;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.content.RestrictionEntry;
import android.content.RestrictionsManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.security.NetworkSecurityPolicy;
import android.util.Log;
import android.view.KeyEvent;

import com.facebook.react.modules.core.PermissionListener;
import com.wisewolf.njmschool.R;

import org.jitsi.meet.sdk.BuildConfig;
import org.jitsi.meet.sdk.JitsiMeet;
import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetActivityDelegate;
import org.jitsi.meet.sdk.JitsiMeetActivityInterface;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;
import org.jitsi.meet.sdk.JitsiMeetUserInfo;
import org.jitsi.meet.sdk.JitsiMeetView;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Map;

public class LiveClass extends FragmentActivity implements JitsiMeetActivityInterface {
JitsiMeetUserInfo jitsiMeetUserInfo=new JitsiMeetUserInfo();
    JitsiMeetView view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_class);
       // Toast.makeText(this, "this ", Toast.LENGTH_SHORT).show();
        view = new JitsiMeetView(this);
        JitsiMeetConferenceOptions options = null;
        try {
            options = new JitsiMeetConferenceOptions.Builder()
                    .setServerURL(new URL("https://navjeevanmeet.njmis.school"))
                    .setRoom("njms_liveclass")
                    .setAudioMuted(false)
                    .setVideoMuted(false)
                    .setAudioOnly(false)
                    .setWelcomePageEnabled(true)
                    .build();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
         view.join(options);
         setContentView(view);
    }

    @Override
    protected void onActivityResult(
            int requestCode,
            int resultCode,
            Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        JitsiMeetActivityDelegate.onActivityResult(
                this, requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        JitsiMeetActivityDelegate.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        view.dispose();
        view = null;

        JitsiMeetActivityDelegate.onHostDestroy(this);
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        JitsiMeetActivityDelegate.onNewIntent(intent);
    }

    @Override
    public void onRequestPermissionsResult(
            final int requestCode,
            final String[] permissions,
            final int[] grantResults) {
        JitsiMeetActivityDelegate.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onResume() {
        super.onResume();

        JitsiMeetActivityDelegate.onHostResume(this);
    }

    @Override
    protected void onStop() {
        super.onStop();

        JitsiMeetActivityDelegate.onHostPause(this);
    }

    @Override
    public void requestPermissions(String[] strings, int i, PermissionListener permissionListener) {
        JitsiMeetActivityDelegate.requestPermissions(this, strings, i, permissionListener);
    }
}

/*
public class LiveClass extends JitsiMeetActivity {
    NetworkSecurityPolicy networkSecurityPolicy;



    */
/**
     * The request code identifying requests for the permission to draw on top
     * of other apps. The value must be 16-bit and is arbitrarily chosen here.
     *//*

    private static final int OVERLAY_PERMISSION_REQUEST_CODE
            = (int) (Math.random() * Short.MAX_VALUE);

    */
/**
     * ServerURL configuration key for restriction configuration using {@link android.content.RestrictionsManager}
     *//*

    public static final String RESTRICTION_SERVER_URL = "server_url";

    */
/**
     * Broadcast receiver for restrictions handling
     *//*

    private BroadcastReceiver broadcastReceiver;

    */
/**
     * Flag if configuration is provided by RestrictionManager
     *//*

    private boolean configurationByRestrictions = false;

    */
/**
     * Default URL as could be obtained from RestrictionManager
     *//*

    private String defaultURL;


    // JitsiMeetActivity overrides
    //
    @RequiresApi(api = Build.VERSION_CODES.N)
    public NetworkSecurityPolicy getNetworkSecurityPolicy() {
       networkSecurityPolicy.isCleartextTrafficPermitted("https://navjeevanmeet.njmis.school/njms_liveclass");
        return networkSecurityPolicy;
    }
    @Override
    protected boolean extraInitialize() {
        Log.d(this.getClass().getSimpleName(), "LIBRE_BUILD="+BuildConfig.LIBRE_BUILD);

        // Setup Crashlytics and Firebase Dynamic Links
        // Here we are using reflection since it may have been disabled at compile time.
        try {
            Class<?> cls = Class.forName("org.jitsi.meet.GoogleServicesHelper");
            Method m = cls.getMethod("initialize", JitsiMeetActivity.class);
            m.invoke(null, this);
        } catch (Exception e) {
            // Ignore any error, the module is not compiled when LIBRE_BUILD is enabled.
        }

        // In Debug builds React needs permission to write over other apps in
        // order to display the warning and error overlays.
        if (BuildConfig.DEBUG) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent
                        = new Intent(
                        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));

                startActivityForResult(intent, OVERLAY_PERMISSION_REQUEST_CODE);

                return true;
            }
        }

        return false;
    }

    @Override
    protected void initialize() {
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // As new restrictions including server URL are received,
                // conference should be restarted with new configuration.
                leave();
                recreate();
            }
        };
        registerReceiver(broadcastReceiver,
                new IntentFilter(Intent.ACTION_APPLICATION_RESTRICTIONS_CHANGED));

       // resolveRestrictions();
      //  getNetworkSecurityPolicy();
        setJitsiMeetConferenceDefaultOptions();
        super.initialize();
    }

    @Override
    public void onDestroy() {
        if (broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver);
            broadcastReceiver = null;
        }

        super.onDestroy();
    }

    private void setJitsiMeetConferenceDefaultOptions() {
        // Set default options

        URL serverURL;
        try {
            serverURL = new URL("https://navjeevanmeet.njmis.school");
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new RuntimeException("Invalid server URL!");
        }
        JitsiMeetConferenceOptions defaultOptions
                = new JitsiMeetConferenceOptions.Builder()
                .setServerURL(serverURL)
                .setWelcomePageEnabled(false)
                .build();

        JitsiMeetConferenceOptions options
                = new JitsiMeetConferenceOptions.Builder()
                .setRoom("njms_liveclass")
                .build();
        // Launch the new activity with the given options. The launch() method takes care
        // of creating the required Intent and passing the options.
        JitsiMeet.setDefaultConferenceOptions(defaultOptions);
        JitsiMeetActivity.launch(this, "https://navjeevanmeet.njmis.school/njms_liveclass");
        this.finish();



       */
/* JitsiMeetConferenceOptions defaultOptions
                = new JitsiMeetConferenceOptions.Builder()
                .setWelcomePageEnabled(true)
                .setServerURL(buildURL("https://navjeevanmeet.njmis.school/njms_liveclass"))
                .setFeatureFlag("call-integration.enabled", false)
                .setFeatureFlag("resolution", 360)
                .setFeatureFlag("server-url-change.enabled", !configurationByRestrictions)
                .build();
        JitsiMeet.setDefaultConferenceOptions(defaultOptions);*//*

    }

    private void resolveRestrictions() {
        RestrictionsManager manager =
                (RestrictionsManager) getSystemService(Context.RESTRICTIONS_SERVICE);
        Bundle restrictions = manager.getApplicationRestrictions();
        Collection<RestrictionEntry> entries = manager.getManifestRestrictions(
                getApplicationContext().getPackageName());
        try{
            for (RestrictionEntry restrictionEntry : entries) {
                String key = restrictionEntry.getKey();
                if (RESTRICTION_SERVER_URL.equals(key)) {
                    // If restrictions are passed to the application.
                    if (restrictions != null &&
                            restrictions.containsKey(RESTRICTION_SERVER_URL)) {
                        defaultURL = restrictions.getString(RESTRICTION_SERVER_URL);
                        configurationByRestrictions = true;
                        // Otherwise use default URL from app-restrictions.xml.
                    } else {
                        defaultURL = restrictionEntry.getSelectedString();
                        configurationByRestrictions = false;
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onConferenceTerminated(Map<String, Object> data) {
        Log.d(TAG, "Conference terminated: " + data);
    }

    // Activity lifecycle method overrides
    //

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == OVERLAY_PERMISSION_REQUEST_CODE) {
            if (Settings.canDrawOverlays(this)) {
                initialize();
                return;
            }

            throw new RuntimeException("Overlay permission is required when running in Debug mode.");
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    // ReactAndroid/src/main/java/com/facebook/react/ReactActivity.java
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (BuildConfig.DEBUG && keyCode == KeyEvent.KEYCODE_MENU) {
            JitsiMeet.showDevOptions();
            return true;
        }

        return super.onKeyUp(keyCode, event);
    }

    @Override
    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode);

        Log.d(TAG, "Is in picture-in-picture mode: " + isInPictureInPictureMode);

        if (!isInPictureInPictureMode) {
            this.startActivity(new Intent(this, getClass())
                    .addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
        }
    }

    // Helper methods
    //

    private @Nullable URL buildURL(String urlStr) {
        try {
            return new URL(urlStr);
        } catch (MalformedURLException e) {
            return null;
        }
    }
}*/
