package com.wisewolf.njmschool.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.wisewolf.njmschool.Globals.GlobalData;
import com.wisewolf.njmschool.Models.Response;
import com.wisewolf.njmschool.R;
import com.wisewolf.njmschool.RetrofitClientInstance;
import com.wisewolf.njmschool.Validation;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class SignUp extends AppCompatActivity {
    ProgressDialog progressDoalog;
    private RecyclerView recyclerView;
    ImageView anim;
    TextView check;
    EditText phone, password;
    CardView signUp,passCard;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();

        anim = findViewById(R.id.child_gif2);
        check = findViewById(R.id.check);
        phone = findViewById(R.id.phone);
        password = findViewById(R.id.pass_field);
        signUp = findViewById(R.id.button);
        passCard=findViewById(R.id.password);
        passCard.setVisibility(View.GONE);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                Glide.with(SignUp.this).asGif().load(R.raw.child).into(anim);

            }
        });


        progressDoalog = new ProgressDialog(SignUp.this);
        progressDoalog.setMessage("Loading....");
        progressDoalog.show();


        /*Create handle for the RetrofitInstance interface*/
        final RetrofitClientInstance.GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(RetrofitClientInstance.GetDataService.class);
        Call<List<Response>> call = service.getAllStudents();

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call <Validation> savePost = service.savePost(phone.getText().toString());
                savePost.enqueue(new Callback<Validation>() {
                    @Override
                    public void onResponse(Call<Validation> call, retrofit2.Response<Validation> response) {
                        try {
                            if (response.body().getResults().equals("Mobile Number is Valid...")){
                                Toast.makeText(SignUp.this, "Enter password to register", Toast.LENGTH_SHORT).show();
                                check.setText("Enter password to register....");
                                signUp.setVisibility(View.VISIBLE);
                                passCard.setVisibility(View.VISIBLE);

                            }
                            else {
                                Toast.makeText(SignUp.this, "Number not present !! contact office", Toast.LENGTH_SHORT).show();
                            }

                        }
                        catch (Exception e){
                            Toast.makeText(SignUp.this, e.toString(), Toast.LENGTH_SHORT).show();
                        }


                    }

                    @Override
                    public void onFailure(Call<Validation> call, Throwable t) {
                        Toast.makeText(SignUp.this, "fail", Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });
        
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<List<Response>> call = service.savePass(phone.getText().toString(),password.getText().toString());
                call.enqueue(new Callback<List<Response>>() {
                    @Override
                    public void onResponse(Call<List<Response>> call, retrofit2.Response<List<Response>> response) {
                        if (response!=null) {
                            GlobalData.profiles=response.body();
                            Intent msgIntent = new Intent(SignUp.this, StudentProfileSelection.class);
                            startActivity(msgIntent);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Response>> call, Throwable t) {
                        Toast.makeText(SignUp.this, "Something is wrong", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        call.enqueue(new Callback<List<Response>>() {
            @Override
            public void onResponse(Call<List<Response>> call, retrofit2.Response<List<Response>> response) {
                progressDoalog.dismiss();
                generateDataList(response.body());
            }


            @Override
            public void onFailure(Call<List<Response>> call, Throwable t) {
                progressDoalog.dismiss();
                Toast.makeText(SignUp.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void generateDataList(List<Response> photoList) {

    }
}
