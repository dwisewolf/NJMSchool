package com.wisewolf.njmschool.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.wisewolf.njmschool.Models.News;
import com.wisewolf.njmschool.Models.SchoolDiff;
import com.wisewolf.njmschool.R;
import com.wisewolf.njmschool.RetrofitClientInstance;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsSet extends AppCompatActivity {
    RadioGroup rGroup_school,rGroup_stu;
    RadioButton checkedRadioButton_school,checkedRadioButton_stu;
    String school="",student_code="1",STU_CODE="ALL",News_="",expDates="";
    AutoCompleteTextView autoCompleteTextView;
    String[] sudentname,studentcode,class_name={"PREP","LKG","UKG",
        "CLASS 1","CLASS 2","CLASS 3","CLASS 4","CLASS 5","CLASS 6","CLASS 7","CLASS 8","CLASS 9","CLASS 10","CLASS 11","CLASS 12"};
    int flag_class_student=0;  // 0 for class , 1 for student
    ProgressDialog progressDoalog;
    Button post;
    EditText news;
    TextView expDate;
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener datee;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_set);

        // This will get the radiogroup
        rGroup_school = (RadioGroup) findViewById(R.id.school_radioGroup);
        autoCompleteTextView=findViewById(R.id.autoCompleteTextView);
        autoCompleteTextView.setVisibility(View.GONE);
        post=findViewById(R.id.post_id);
        news=findViewById(R.id.news);
        expDate=findViewById(R.id.date_id);

        myCalendar = Calendar.getInstance();
        datee = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub

                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };
        expDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(NewsSet.this, datee, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


       // This will get the radiobutton in the radiogroup that is checked
        checkedRadioButton_school = (RadioButton) rGroup_school.findViewById(rGroup_school.getCheckedRadioButtonId());

        rGroup_school.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {

                // This will get the radiobutton that has changed in its check state
                RadioButton checkedRadioButton = (RadioButton)group.findViewById(checkedId);
                // This puts the value (true/false) into the variable
                boolean isChecked = checkedRadioButton.isChecked();
                // If the radiobutton that has changed in check state is now checked...
                if (isChecked && student_code.equals("1") )

                {

                    switch (checkedRadioButton.getText().toString()){
                        case "KASIA":
                            school="KAS";
                            break;
                        case "ANANDNAGAR":
                            school="AND";
                            break;
                        case "NJ INTERNATIONAL":
                            school="GKP";
                            break;
                        case "DEORIA":
                            school="DEO";
                            break;
                        case "All":
                            school="All";
                            break;
                    }
                    Toast.makeText(NewsSet.this, checkedRadioButton.getText(), Toast.LENGTH_SHORT).show();

                }
                else {
                 //   rGroup_stu.clearCheck();
                }
            }
        });

        rGroup_stu = (RadioGroup) findViewById(R.id.student_rg);
        // This will get the radiobutton in the radiogroup that is checked
        checkedRadioButton_school = (RadioButton) rGroup_stu.findViewById(rGroup_stu.getCheckedRadioButtonId());

        rGroup_stu.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                if (!school.equals("")){
                    rGroup_school.setFocusable(false);
                }
                // This will get the radiobutton that has changed in its check state
                RadioButton checkedRadioButton = (RadioButton)group.findViewById(checkedId);
                // This puts the value (true/false) into the variable
                boolean isChecked = checkedRadioButton.isChecked();
                // If the radiobutton that has changed in check state is now checked...
                if (isChecked)
                {
                    switch (checkedRadioButton.getText().toString()){
                        case "ALL":
                          student_code="ALL";
                          autoCompleteTextView.setVisibility(View.GONE);
                            break;
                        case "Class":
                            student_code="ALL";
                            autoCompleteTextView.setThreshold(3);
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(NewsSet.this,android.R.layout.simple_dropdown_item_1line, sudentname);
                            autoCompleteTextView.setAdapter(adapter);
                            adapter.setNotifyOnChange(true);
                            autoCompleteTextView.setVisibility(View.VISIBLE);
                            break;
                        case "Student":
                            if (school.equals("All")){
                                Toast.makeText(NewsSet.this, "Cannot select student if All school is selected", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                progressDoalog = new ProgressDialog(NewsSet.this);
                                progressDoalog.setMessage("Loading Student Data....");
                                progressDoalog.show();
                                autoCompleteTextView.setVisibility(View.VISIBLE);
                                callStudentAPI(school);}

                            break;

                    }

                }
            }
        });

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int position, long arg3) {
                Object item = parent.getItemAtPosition(position);
                if(flag_class_student==1) {
                    for (int i = 0; i < sudentname.length; i++) {
                        if (sudentname[i].equals(String.valueOf(item))) {
                            STU_CODE = studentcode[i];
                        }
                    }
                }
                else {
                    student_code=autoCompleteTextView.getText().toString();
                }
            }
        });

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                News_=news.getText().toString();
                if (school.equals("All"))
                {
                    postnewsAll();
                }
                else {
                    postnews();
                }
            }
        });


    }

    private void postnewsAll() {
        final RetrofitClientInstance.GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(RetrofitClientInstance.GetDataService.class);
        if (!school.equals("") && !STU_CODE.equals("") && !News_.equals("") && !expDates.equals("")) {
            Call<News> set_News = service.set_News("KAS", STU_CODE, News_, expDates, "https://stackoverflow.com/questions/3299392/date-picker-in-android");
            set_News.enqueue(new Callback<News>() {
                @Override
                public void onResponse(Call<News> call, Response<News> response) {

                }

                @Override
                public void onFailure(Call<News> call, Throwable t) {
                    school = school;
                }
            });

             set_News = service.set_News("GKP", STU_CODE, News_, expDates, "https://stackoverflow.com/questions/3299392/date-picker-in-android");
            set_News.enqueue(new Callback<News>() {
                @Override
                public void onResponse(Call<News> call, Response<News> response) {

                }

                @Override
                public void onFailure(Call<News> call, Throwable t) {
                    school = school;
                }
            });

             set_News = service.set_News("AND", STU_CODE, News_, expDates, "https://stackoverflow.com/questions/3299392/date-picker-in-android");
            set_News.enqueue(new Callback<News>() {
                @Override
                public void onResponse(Call<News> call, Response<News> response) {

                }

                @Override
                public void onFailure(Call<News> call, Throwable t) {
                    school = school;
                }
            });

             set_News = service.set_News("DEO", STU_CODE, News_, expDates, "https://stackoverflow.com/questions/3299392/date-picker-in-android");
            set_News.enqueue(new Callback<News>() {
                @Override
                public void onResponse(Call<News> call, Response<News> response) {
                    Toast.makeText(NewsSet.this, "News Added", Toast.LENGTH_SHORT).show();
                    finish();
                }

                @Override
                public void onFailure(Call<News> call, Throwable t) {
                    school = school;
                }
            });
        }
        else {
            Toast.makeText(this, "Fill all the field", Toast.LENGTH_SHORT).show();
        }
    }

    private void postnews() {
        final RetrofitClientInstance.GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(RetrofitClientInstance.GetDataService.class);
        if (!school.equals("") && !STU_CODE.equals("") && !News_.equals("") && !expDates.equals("")) {
            Call<News> set_News = service.set_News(school, STU_CODE, News_, expDates, "https://stackoverflow.com/questions/3299392/date-picker-in-android");
            set_News.enqueue(new Callback<News>() {
                @Override
                public void onResponse(Call<News> call, Response<News> response) {
                    Toast.makeText(NewsSet.this, "News Added", Toast.LENGTH_SHORT).show();
                    finish();
                }

                @Override
                public void onFailure(Call<News> call, Throwable t) {
                    school = school;
                }
            });
        }

    }

    private void callStudentAPI(String school) {
        final RetrofitClientInstance.GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(RetrofitClientInstance.GetDataService.class);
        Call<List<SchoolDiff>> getStudents = service.get_students(school);
        getStudents.enqueue(new Callback<List<SchoolDiff>>() {
            @Override
            public void onResponse(Call<List<SchoolDiff>> call, Response<List<SchoolDiff>> response) {
                if (response!=null||response.body().get(0)!=null) {
                    sudentname = new String[response.body().size()];
                    studentcode = new String[response.body().size()];
                    for (int i=0;i<response.body().size();i++){
                        sudentname[i]=(response.body().get(i).getName());
                        studentcode[i]=(response.body().get(i).getUserid());

                    }


                    autoCompleteTextView.setThreshold(3);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(NewsSet.this,android.R.layout.simple_dropdown_item_1line, sudentname);
                    autoCompleteTextView.setAdapter(adapter);
                    adapter.setNotifyOnChange(true);
                    progressDoalog.cancel();
                }
            }

            @Override
            public void onFailure(Call<List<SchoolDiff>> call, Throwable t) {
                autoCompleteTextView.setThreshold(3);
            }
        });


    }

    private void updateLabel() {

        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
        expDate.setText("Expiry Date : "+sdf.format(myCalendar.getTime()));
        expDates = sdf.format(myCalendar.getTime());

    }
}
