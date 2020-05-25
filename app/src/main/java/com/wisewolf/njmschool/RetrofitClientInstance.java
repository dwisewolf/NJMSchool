package com.wisewolf.njmschool;

import com.wisewolf.njmschool.Models.Quotes;
import com.wisewolf.njmschool.Models.Response;
import com.wisewolf.njmschool.Models.SchoolDiff;
import com.wisewolf.njmschool.Models.VideoUp;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public class RetrofitClientInstance {
    private static Retrofit retrofit,retrofitOut;
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
    public static Retrofit getRetrofitInstance1(String baseurl) {
        if (retrofitOut == null) {
            retrofitOut = new retrofit2.Retrofit.Builder()
                .baseUrl(baseurl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        }
        return retrofitOut;
    }

    public interface GetDataService {

        @GET("/stu/")
        Call<List<Response>> getAllStudents();

        @POST("/checkNum/")
        @FormUrlEncoded
        Call<Validation> savePost(@Field("mobileNum") String phone);

        @POST("/data/")
        @FormUrlEncoded
        Call<List<Response>> savePass(@Field("mobileNum") String phone,
                                      @Field("password") String pass);

        @POST("/videoFile/")
        @FormUrlEncoded
        Call<VideoUp> saveVideo(@Field("userid") String userid,
                                @Field("name") String name,
                                @Field("video_link") String video_link,
                                @Field("description") String description,
                                @Field("thumbnail_link") String thumbnail_link,
                                @Field("played_time") String played_time);

        @POST("/getuserData/")
        @FormUrlEncoded
        Call<List<VideoUp>> getVideo(@Field("userid") String userid);


        @POST("/data/")
        @FormUrlEncoded
        Call<List<SchoolDiff>> get_Categ(@Field("mobileNum") String phone,
                                         @Field("password") String pass);

        @GET("/qod/")
        Call<List<Quotes>> getQuote();



    }
}
