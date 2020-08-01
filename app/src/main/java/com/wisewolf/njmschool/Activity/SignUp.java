package com.wisewolf.njmschool.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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
import com.wisewolf.njmschool.Models.SchoolDiff;
import com.wisewolf.njmschool.R;
import com.wisewolf.njmschool.RetrofitClientInstance;
import com.wisewolf.njmschool.Validation;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class SignUp extends AppCompatActivity {
    ProgressDialog progressDoalog;
    private RecyclerView recyclerView;
    ImageView anim;
    TextView check, fgpswd;
    EditText phone, password;
    CardView signUp, passCard;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();

        anim = findViewById(R.id.child_gif2);
        fgpswd = findViewById(R.id.fpasswd);
        check = findViewById(R.id.check);
        phone = findViewById(R.id.phone);
        password = findViewById(R.id.pass_field);
        signUp = findViewById(R.id.button);
        passCard = findViewById(R.id.password);
        passCard.setVisibility(View.GONE);

        checkLogin();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                Glide.with(SignUp.this).asGif().load(R.raw.child).into(anim);

            }
        });


        progressDoalog = new ProgressDialog(SignUp.this);
        progressDoalog.setMessage("Loading....");
        progressDoalog.show();

        fgpswd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fpswdbox();
            }
        });
        /*Create handle for the RetrofitInstance interface*/
        final RetrofitClientInstance.GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(RetrofitClientInstance.GetDataService.class);
        Call<List<Response>> call = service.getAllStudents();

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<Validation> savePost = service.savePost(phone.getText().toString());
                savePost.enqueue(new Callback<Validation>() {
                    @Override
                    public void onResponse(Call<Validation> call, retrofit2.Response<Validation> response) {
                        try {
                            if (response.body().getResults().equals("Mobile Number is Valid...")) {

                                Toast.makeText(SignUp.this, "Enter password to register", Toast.LENGTH_SHORT).show();
                                check.setText("Enter password to register....");
                                phone.setFocusable(false);
                                password.requestFocus();
                                check.setVisibility(View.INVISIBLE);
                                signUp.setVisibility(View.VISIBLE);
                                passCard.setVisibility(View.VISIBLE);

                            } else {
                                Toast.makeText(SignUp.this, "Number not present !! Please contact School office", Toast.LENGTH_LONG).show();
                            }

                        } catch (Exception e) {
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

                Call<List<SchoolDiff>> get_categ = service.get_Categ(phone.getText().toString(), password.getText().toString());
                get_categ.enqueue(new Callback<List<SchoolDiff>>() {
                    @Override
                    public void onResponse(Call<List<SchoolDiff>> call, retrofit2.Response<List<SchoolDiff>> response) {
                        if (response != null || response.body().get(0) != null) {
                            GlobalData.profiles = response.body();
                            savePass(phone.getText().toString(), password.getText().toString());

                        } else {
                            Toast.makeText(SignUp.this, "Bad Network", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<SchoolDiff>> call, Throwable t) {
                        Toast.makeText(SignUp.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                        incorrectPassword();
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

    private void fpswdbox() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SignUp.this);


        alertDialogBuilder.setTitle("App Credentials ")
            .setMessage("Contact School office")
            .setPositiveButton("Call Now", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:7081803501"));
                    startActivity(intent);
                }
            })
            .setNegativeButton("NO", null)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show();
    }


    private void checkLogin() {
        final SharedPreferences sp1 = getDefaultSharedPreferences(getApplicationContext());
        String logged = sp1.getString("login", "");

        if (logged.equals("logged")) {
            showsnack(sp1.getString("username", ""), sp1.getString("pass", ""));
        }
    }

    private void showsnack(final String username, final String pass) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SignUp.this);


        alertDialogBuilder.setTitle("App Credentials")
            .setMessage("Proceed with saved Credentials of " + username)
            .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    RetrofitClientInstance.GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(RetrofitClientInstance.GetDataService.class);

                    Call<List<SchoolDiff>> get_categ = service.get_Categ(username, pass);
                    get_categ.enqueue(new Callback<List<SchoolDiff>>() {
                        @Override
                        public void onResponse(Call<List<SchoolDiff>> call, retrofit2.Response<List<SchoolDiff>> response) {
                            if (response != null || response.body().get(0) != null) {
                                GlobalData.profiles = response.body();
                                Intent msgIntent = new Intent(SignUp.this, StudentProfileSelection.class);
                                startActivity(msgIntent);

                            } else {
                                Toast.makeText(SignUp.this, "Bad Network", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<List<SchoolDiff>> call, Throwable t) {
                            Toast.makeText(SignUp.this, "Something is wrong", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            })
            .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {


                }
            })
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show();
    }

    private void incorrectPassword() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SignUp.this);


        alertDialogBuilder.setTitle("App Credentials Wrong")
            .setMessage("Contact School office")
            .setPositiveButton("Call Now", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:7081803501"));
                    startActivity(intent);
                }
            })
            .setNegativeButton("NO", null)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show();
    }

    private void savePass(final String phone, final String pass) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SignUp.this);


        alertDialogBuilder.setTitle("App Credentials")
            .setMessage("Do you want to save the username and password ?")
            .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                    final SharedPreferences sp1 = getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = sp1.edit();
                    editor.putString("username", phone);
                    editor.putString("pass", pass);
                    editor.putString("login", "logged");
                    editor.apply();

                    Intent msgIntent = new Intent(SignUp.this, StudentProfileSelection.class);
                    startActivity(msgIntent);
                }
            })
            .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    final SharedPreferences sp1 = getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = sp1.edit();

                    editor.putString("username", phone);
                    editor.putString("pass", pass);
                    editor.putString("login", "Not-logged");
                    editor.commit();

                    Intent msgIntent = new Intent(SignUp.this, StudentProfileSelection.class);
                    startActivity(msgIntent);

                }
            })
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show();
    }

    private void generateDataList(List<Response> photoList) {

    }
}
