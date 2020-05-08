package com.wisewolf.njmschool;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public class RetrofitClientInstance {
    private static Retrofit retrofit;
    private static final String BASE_URL = "http://139.59.79.78/";

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new retrofit2.Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        }
        return retrofit;
    }

    public interface GetDataService {

        @GET("/stu/")
        Call<List<Response>> getAllStudents();

        @POST("/checkNum/")
        @FormUrlEncoded
        Call <Validation>  savePost(@Field("mobileNum") String phone);

        @POST("/stu/")
        @FormUrlEncoded
        Call<List<Response>> savePass(@Field("mobileNum") String phone,
                                  @Field("password") String pass);

    }
}
