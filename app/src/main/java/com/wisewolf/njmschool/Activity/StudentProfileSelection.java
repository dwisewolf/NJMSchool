package com.wisewolf.njmschool.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.wisewolf.njmschool.Globals.GlobalData;
import com.wisewolf.njmschool.Globals.LiveMOd;
import com.wisewolf.njmschool.Models.ClassVideo;
import com.wisewolf.njmschool.Models.DailyTask;
import com.wisewolf.njmschool.Models.News;
import com.wisewolf.njmschool.Models.SchoolDiff;
import com.wisewolf.njmschool.Models.TeacherDetails;
import com.wisewolf.njmschool.R;
import com.wisewolf.njmschool.Adapter.StudentAdapter;
import com.wisewolf.njmschool.RetrofitClientInstance;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import kotlin.text.Regex;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.provider.Settings.Secure;

public class StudentProfileSelection extends AppCompatActivity {
    RecyclerView studentList;
    ProgressDialog mProgressDialog;
    TextView parents;
    String u_class,school;
    SchoolDiff a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student__select);
        parents = findViewById(R.id.parentsDetails);



        try {
              a = (SchoolDiff) GlobalData.profiles.get(0);
            parents.setText(a.getFatherName() + "\nPhone - " + a.getMobileNum());
        }
        catch (Exception e) {
            parents.setText("Student blocked by Office");
        }

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();



        studentList = findViewById(R.id.child_select_list);
        mProgressDialog = new ProgressDialog(StudentProfileSelection.this);
        mProgressDialog.setMessage("Loading. . .");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);
        // callQuote();
        try {
            studentList.setAdapter(new StudentAdapter(GlobalData.profiles, studentList, new StudentAdapter.OnItemClickListener() {


                @Override
                public void onItemClick(SchoolDiff s) {
                    if (s.getCategory().equals("TEACHER")) {
                        GlobalData.regno = s.getUserid();
                        GlobalData.name = s.getName();
                        String original = s.getUserid();
                        Regex reg = new Regex("(?<=[0-9])(?=[A-Za-z])");
                        String[] parts = reg.split(original, 2).toArray(new String[0]);
                        Intent intent = new Intent(StudentProfileSelection.this, ClassSelect.class);
                        intent.putExtra("name", s.getName());
                        intent.putExtra("school", parts[1]);
                        intent.putExtra("phone", s.getMobileNum());
                        startActivity(intent);
                    }
                    if (s.getCategory().equals("STUDENT")) {
                         testPhonenumber(s);



                    }


                }
            }));
            LinearLayoutManager added_liste_adapterlayoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            studentList.setLayoutManager(added_liste_adapterlayoutManager);
        } catch (Exception e)
        {
            Toast.makeText(this, "Your Network Error ..please restart App (SPS84)", Toast.LENGTH_SHORT).show();
            FirebaseCrashlytics.getInstance().log(String.valueOf(e));
        }

    }

    private void checkDate(String school, String u_class, SchoolDiff s, String schoolname) {
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String formattedDate = df.format(c)+s.getUserid();

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(StudentProfileSelection.this);
        String date=sharedPrefs.getString("date", "");

        if (date.equals(formattedDate)) {
            getAllvideoList();
            getTeachers();
            final String cls=u_class;
            u_class = findClass(u_class);

            mProgressDialog.cancel();
            GlobalData.school_code=schoolname;
            GlobalData.classes=cls;
            Intent intent = new Intent(StudentProfileSelection.this, VideoListing.class);
            intent.putExtra("name", schoolname);
            intent.putExtra("class", s.getClas());
            intent.putExtra("sec", s.getSection());
            intent.putExtra("phone", String.valueOf(s.getMobileNum()));
            startActivity(intent);

        }
        else {
            SharedPreferences.Editor editor = sharedPrefs.edit();
            editor.putString("date", formattedDate);
            editor.apply();
            getClassVideo(school,u_class,s,schoolname);
        }

    }

    private void getTeachers() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(StudentProfileSelection.this);
        Gson gson = new Gson();
        String json = sharedPrefs.getString("Teachers", "");

        Type type2 = new TypeToken<List<TeacherDetails>>() {
        }.getType();
        GlobalData.teacherDetails= gson.fromJson(json, type2);

    }

    private void getAllvideoList() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(StudentProfileSelection.this);
        Gson gson = new Gson();
        String json = sharedPrefs.getString("AllVideolist", "");

        Type type2 = new TypeToken< List<ClassVideo>> () {
        }.getType();
        ArrayList arrayList = (ArrayList) gson.fromJson(json, type2);
        GlobalData.allVideoList=arrayList;


    }

    private void getClassVideo(final String school, String u_class, final SchoolDiff s, final String schoolname) {
        final String cls=u_class;
        u_class = findClass(u_class);
        try {
            final RetrofitClientInstance.GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(RetrofitClientInstance.GetDataService.class);
            Call<List<ClassVideo>> call = service.getVideos(school, u_class);
            final String finalU_class = u_class;
            call.enqueue(new Callback<List<ClassVideo>>() {
                @Override
                public void onResponse(Call<List<ClassVideo>> call, Response<List<ClassVideo>> response)
                {

                    GlobalData.allVideoList = (ArrayList) response.body();
                    addAllVideolist((ArrayList) response.body());
                    GlobalData.school_code=schoolname;
                    GlobalData.classes=cls;
                    getDailyTask(schoolname, cls,s.getName(),s.getClas(),s.getSection().replaceAll("\\s+",""),String.valueOf(s.getMobileNum()));


                }

                @Override
                public void onFailure(Call<List<ClassVideo>> call, Throwable t) {

                }
            });

            // Response<List<ClassVideo>> response=   call.execute();

        } catch (Exception e) {
        }

    }

    private void saveTeachers(List<TeacherDetails> body) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(StudentProfileSelection.this);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        Gson gson = new Gson();
        String AllVideolist = gson.toJson(body);
        editor.putString("Teachers", AllVideolist);
        editor.apply();
    }

    private void addAllVideolist(ArrayList body) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(StudentProfileSelection.this);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        Gson gson = new Gson();
        String AllVideolist = gson.toJson(body);
        editor.putString("AllVideolist", AllVideolist);
        editor.apply();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
       finishAffinity();
    }

    private void getnews(String userid) {
        try {
            final RetrofitClientInstance.GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(RetrofitClientInstance.GetDataService.class);
            Call<List<News>> call = service.get_News(userid);
            call.enqueue(new Callback<List<News>>() {
                @Override
                public void onResponse(Call<List<News>> call, Response<List<News>> response) {
                    String a="";
                    GlobalData.newsForstudent=response.body();
                }

                @Override
                public void onFailure(Call<List<News>> call, Throwable t) {
String a="";
                }
            });
        }catch (Exception ignored){
        }


    }

    private void getDailyTask(final String school, String finalU_class, final String name, final String clasD, final String sec, final String phone) {
        try {
            final RetrofitClientInstance.GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(RetrofitClientInstance.GetDataService.class);
            Call<List<TeacherDetails>> call = service.get_teacherDetails(school,finalU_class);
            call.enqueue(new Callback<List<TeacherDetails>>() {
                @Override
                public void onResponse(Call<List<TeacherDetails>> call, Response<List<TeacherDetails>> response) {
                    GlobalData.teacherDetails = response.body();
                    saveTeachers(response.body());
                    mProgressDialog.cancel();
                    Intent intent = new Intent(StudentProfileSelection.this, VideoListing.class);
                    intent.putExtra("name", name);
                    intent.putExtra("class", clasD);
                    intent.putExtra("sec", sec);
                    intent.putExtra("phone", phone);
                    startActivity(intent);


                }

                @Override
                public void onFailure(Call<List<TeacherDetails>> call, Throwable t) {
                    mProgressDialog.cancel();
                    Intent intent = new Intent(StudentProfileSelection.this, VideoListing.class);
                    intent.putExtra("name", name);
                    intent.putExtra("class", clasD);
                    intent.putExtra("sec", sec);
                    intent.putExtra("phone", phone);
                    startActivity(intent);
                }
            });


        }catch (Exception ignored){
            String a="";
        }


    }

    private String findClass(String clas) {
        String clasS = "";
        switch(clas) {
            case "PREP":
                clasS=   "NR";
                GlobalData.clas="NRL";
                break;
            case "LKG":
                clasS="LK";
                GlobalData.clas="LKG";
                break;
            case "UKG":
                clasS= "UK";
                GlobalData.clas="UKG";
                break;
            case "XII":
                clasS= "C12";
                GlobalData.clas="12";
                break;
            case "XI":
                clasS= "C11";
                GlobalData.clas="11";
                break;
            case "X":
                clasS="C10";
                GlobalData.clas="10";
                break;
            case "IX":
               clasS= "C9";
                GlobalData.clas="9";
                break;
            case "VIII":
               clasS= "C8";
                GlobalData.clas="8";
                break;
            case "VII":
               clasS= "C7";
                GlobalData.clas="7";
                break;
            case "VI":
               clasS= "C6";
                GlobalData.clas="6";
                break;
            case "V":
               clasS= "C5";
                GlobalData.clas="5";
                break;
            case "IV":
               clasS= "C4";
                GlobalData.clas="4";
                break;
            case "III":
               clasS= "C3";
                GlobalData.clas="3";
                break;
            case "II":
               clasS= "C2";
                GlobalData.clas="2";
                break;
            case "I":
               clasS= "C1";
                GlobalData.clas="1";
                break;
        }
        return  clasS;
    }

    private  void testPhonenumber(SchoolDiff s){
        String deviceId;
        deviceId = Secure.getString(getContentResolver(), Secure.ANDROID_ID);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("phoneNumberValidation").document(a.getMobileNum()+s.getUserid()).get()
            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    String id = (String) documentSnapshot.get("deviceId");
                    if (id==null){
                        alertforPhone(s,deviceId);
                    }
                    else {
                        if (id.equals(deviceId)){
                            GlobalData.regno = s.getUserid();
                            GlobalData.name = s.getName();
                            getnews(s.getUserid());

                            school=s.getUserid().substring(0,1);
                            u_class=s.getClas();
                            mProgressDialog.show();

                            checkDate(school,u_class,s,s.getUserid().substring(0,3));
                            Toast.makeText(StudentProfileSelection.this, "Device identified", Toast.LENGTH_SHORT).show();

                        }
                        else
                            Toast.makeText(StudentProfileSelection.this, "wrong device,please contact office", Toast.LENGTH_SHORT).show();
                    }

                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });




    }

    private void alertforPhone(SchoolDiff s,String deviceId) {
        new AlertDialog.Builder(StudentProfileSelection.this)
            .setTitle("Phone Number Validation")
            .setCancelable(false)
            .setMessage("Your NJMS app will be registered on this mobile device. If you need access on another phone, please contact the school office. ")

            // Specifying a listener allows you to take an action before dismissing the dialog.
            // The dialog is automatically dismissed when a dialog button is clicked.
            .setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        HashMap hm = new HashMap();
                        hm.put("deviceId", deviceId);

                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        db.collection("phoneNumberValidation").document(a.getMobileNum()+s.getUserid()).set(hm)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    GlobalData.regno = s.getUserid();
                                    GlobalData.name = s.getName();
                                    getnews(s.getUserid());

                                    school=s.getUserid().substring(0,1);
                                    u_class=s.getClas();
                                    mProgressDialog.show();

                                    checkDate(school,u_class,s,s.getUserid().substring(0,3));
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(StudentProfileSelection.this, "Error network", Toast.LENGTH_SHORT).show();
                                }
                            });
                    } catch (Exception e) {
                        Toast.makeText(StudentProfileSelection.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }

                }
            })

            // A null listener allows the button to dismiss the dialog and take no further action.
            .setNegativeButton("CLOSE", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            })
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show();
    }


}