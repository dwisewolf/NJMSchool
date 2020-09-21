package com.wisewolf.njmschool.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.facebook.react.modules.core.PermissionListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.wisewolf.njmschool.Globals.GlobalData;
import com.wisewolf.njmschool.R;

import org.jitsi.meet.sdk.JitsiMeet;
import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetActivityDelegate;
import org.jitsi.meet.sdk.JitsiMeetActivityInterface;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;
import org.jitsi.meet.sdk.JitsiMeetUserInfo;
import org.jitsi.meet.sdk.JitsiMeetView;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;

public class LiveClass extends FragmentActivity implements JitsiMeetActivityInterface {
JitsiMeetUserInfo jitsiMeetUserInfo=new JitsiMeetUserInfo();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_class);
        Toast.makeText(this, "this ", Toast.LENGTH_SHORT).show();
        view = new JitsiMeetView(this);
        JitsiMeetConferenceOptions options = null;
        try {
            options = new JitsiMeetConferenceOptions.Builder()
                .setServerURL(new URL("https://navjeevanmeet.njmis.school"))
                .setRoom("njms_liveclass")
                .setAudioMuted(false)
                .setVideoMuted(false)
                .setAudioOnly(false)
                .setWelcomePageEnabled(false)
                .build();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        view.join(options);

        setContentView(view);
    }

    private JitsiMeetView view;

    @Override
    protected void onActivityResult(
        int requestCode,
        int resultCode,
        Intent data) {
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

    }

}