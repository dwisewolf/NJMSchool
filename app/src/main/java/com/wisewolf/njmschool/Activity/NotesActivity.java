package com.wisewolf.njmschool.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.wisewolf.njmschool.Adapter.Class_NotesAdapter;
import com.wisewolf.njmschool.Adapter.Class_videoAdapter;
import com.wisewolf.njmschool.Adapter.SubjectAdapterLarge;
import com.wisewolf.njmschool.Globals.GlobalData;
import com.wisewolf.njmschool.Globals.SubjectList;
import com.wisewolf.njmschool.Models.ClassVideo;
import com.wisewolf.njmschool.Models.NotesMod;
import com.wisewolf.njmschool.Models.TeacherDetails;
import com.wisewolf.njmschool.Models.VideoUp;
import com.wisewolf.njmschool.R;
import com.wisewolf.njmschool.RetrofitClientInstance;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotesActivity extends AppCompatActivity {
RecyclerView subjectList,NotesList;
String studentdiv,StudentClass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        subjectList = findViewById(R.id.subject_list);
        NotesList = findViewById(R.id.notes_list);
        StudentClass = GlobalData.clas;
        studentdiv = GlobalData.sect;
        SubjectAdapterLarge();
    }

    private void SubjectAdapterLarge() {
        SubjectAdapterLarge SubjectAdapterLarge = null;

        if (StudentClass.equals("1")) {
            SubjectAdapterLarge = new SubjectAdapterLarge(SubjectList.class2, subjectList, new SubjectAdapterLarge.OnItemClickListener() {
                @Override
                public void onItemClick(String s) {

                    getNotes(s);
                    //  Toast.makeText(VideoPlay.this, s, Toast.LENGTH_SHORT).show();
                }
            });
        }
        if (StudentClass.equals("2")) {
            SubjectAdapterLarge = new SubjectAdapterLarge(SubjectList.class2, subjectList, new SubjectAdapterLarge.OnItemClickListener() {
                @Override
                public void onItemClick(String s) {
                    getNotes(s);
                }
            });
        }
        if (StudentClass.equals("3")) {
            SubjectAdapterLarge = new SubjectAdapterLarge(SubjectList.class3, subjectList, new SubjectAdapterLarge.OnItemClickListener() {
                @Override
                public void onItemClick(String s) {
                    getNotes(s);
                }
            });
        }

        if (StudentClass.equals("4")) {
            SubjectAdapterLarge = new SubjectAdapterLarge(SubjectList.class4, subjectList, new SubjectAdapterLarge.OnItemClickListener() {
                @Override
                public void onItemClick(String s) {
                    getNotes(s);
                }
            });
        }
        if (StudentClass.equals("5")) {
            SubjectAdapterLarge = new SubjectAdapterLarge(SubjectList.class5, subjectList, new SubjectAdapterLarge.OnItemClickListener() {
                @Override
                public void onItemClick(String s) {
                    getNotes(s);
                }
            });
        }
        if (StudentClass.equals("6")) {
            SubjectAdapterLarge = new SubjectAdapterLarge(SubjectList.class6, subjectList, new SubjectAdapterLarge.OnItemClickListener() {
                @Override
                public void onItemClick(String s) {
                    getNotes(s);
                }
            });
        }

        if (StudentClass.equals("7")) {
            SubjectAdapterLarge = new SubjectAdapterLarge(SubjectList.class7, subjectList, new SubjectAdapterLarge.OnItemClickListener() {
                @Override
                public void onItemClick(String s) {
                    getNotes(s);
                }
            });
        }
        if (StudentClass.equals("8")) {
            SubjectAdapterLarge = new SubjectAdapterLarge(SubjectList.class8, subjectList, new SubjectAdapterLarge.OnItemClickListener() {
                @Override
                public void onItemClick(String s) {
                    getNotes(s);
                }
            });
        }
        if (StudentClass.equals("9")) {
            SubjectAdapterLarge = new SubjectAdapterLarge(SubjectList.class9, subjectList, new SubjectAdapterLarge.OnItemClickListener() {
                @Override
                public void onItemClick(String s) {
                    getNotes(s);
                }
            });
        }

        if (StudentClass.equals("10")) {
            SubjectAdapterLarge = new SubjectAdapterLarge(SubjectList.class10, subjectList, new SubjectAdapterLarge.OnItemClickListener() {
                @Override
                public void onItemClick(String s) {
                    getNotes(s);
                }
            });
        }
        if (StudentClass.equals("11"))  {
            String[] list;
            if (studentdiv.equals("C")) {

                list = SubjectList.class11_COMR;
            } else if (studentdiv.equals("H")) {
                list = SubjectList.class11_HUM;
            } else if (studentdiv.equals("S")) {
                list = SubjectList.class11_SCI;
            } else {
                list = SubjectList.class11_COMM;
            }

            SubjectAdapterLarge = new SubjectAdapterLarge(list, subjectList, new SubjectAdapterLarge.OnItemClickListener() {
                @Override
                public void onItemClick(String s) {
                    getNotes(s);
                }
            });
        }

        if (StudentClass.equals("12")) {
            String[] list;
            if (studentdiv.equals("C")) {

                list = SubjectList.class12_COMR;
            } else if (studentdiv.equals("H")) {
                list = SubjectList.class12_HUM;
            } else if (studentdiv.equals("S")) {
                list = SubjectList.class12_SCI;
            } else {
                list = SubjectList.class12_COMM;
            }

            SubjectAdapterLarge = new SubjectAdapterLarge(list, subjectList, new SubjectAdapterLarge.OnItemClickListener() {
                @Override
                public void onItemClick(String s) {
                    getNotes(s);
                }
            });
        }

        if (StudentClass.equals("NRL")) {
            SubjectAdapterLarge = new SubjectAdapterLarge(SubjectList.nrl, subjectList, new SubjectAdapterLarge.OnItemClickListener() {
                @Override
                public void onItemClick(String s) {
                    getNotes(s);
                }
            });
        }
        if (StudentClass.equals("LKG")) {
            SubjectAdapterLarge = new SubjectAdapterLarge(SubjectList.lkg, subjectList, new SubjectAdapterLarge.OnItemClickListener() {
                @Override
                public void onItemClick(String s) {
                    getNotes(s);
                }
            });
        }
        if (StudentClass.equals("UKG")) {
            SubjectAdapterLarge = new SubjectAdapterLarge(SubjectList.ukg, subjectList, new SubjectAdapterLarge.OnItemClickListener() {
                @Override
                public void onItemClick(String s) {
                    getNotes(s);
                }
            });
        }


        subjectList.setAdapter(SubjectAdapterLarge);
        LinearLayoutManager subj_manager
              =  new GridLayoutManager(NotesActivity.this, 2, GridLayoutManager.VERTICAL, false);

        subjectList.setLayoutManager(subj_manager);

    }

    private void getNotes(final String s) {
        try {
            final RetrofitClientInstance.GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(RetrofitClientInstance.GetDataService.class);
            Call<List<NotesMod>> call = service.getNotes(GlobalData.school_code,GlobalData.classes);
            call.enqueue(new Callback<List<NotesMod>>() {
                @Override
                public void onResponse(Call<List<NotesMod>> call, Response<List<NotesMod>> response) {
                    List<NotesMod> notesMods=new ArrayList<>();
                    for (int i=0;i<response.body().size();i++){
                        if (response.body().get(i).getSubject().equals(s)){
                            notesMods.add(response.body().get(i));
                        }
                    }
                    if (notesMods.size()==0){
                        Toast.makeText(NotesActivity.this, "No notes Added", Toast.LENGTH_SHORT).show();
                    }

                    NotesList.setAdapter(new Class_NotesAdapter(NotesActivity.this,notesMods, NotesList, new Class_NotesAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(NotesMod item) {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setDataAndType(Uri.parse(item.getPdfUpload()), "application/pdf");
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            Intent newIntent = Intent.createChooser(intent, "Open File");
                            try {
                                startActivity(newIntent);
                            } catch (ActivityNotFoundException e) {
                                // Instruct the user to install a PDF reader here, or something
                                Toast.makeText(NotesActivity.this, "Install a pdf reader", Toast.LENGTH_SHORT).show();
                            }
                        }


                    }));

                    LinearLayoutManager added_liste_adapterlayoutManager
                        = new LinearLayoutManager(NotesActivity.this, LinearLayoutManager.VERTICAL, false);
                    NotesList.setLayoutManager(added_liste_adapterlayoutManager);
                }

                @Override
                public void onFailure(Call<List<NotesMod>> call, Throwable t) {
                    String a="";
                }
            });


        }catch (Exception ignored){
            String a="";
        }
    }
}