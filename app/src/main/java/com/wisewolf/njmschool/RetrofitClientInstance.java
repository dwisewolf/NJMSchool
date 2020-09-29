package com.wisewolf.njmschool;

import com.wisewolf.njmschool.Globals.LiveMOd;
import com.wisewolf.njmschool.Models.ClassVideo;
import com.wisewolf.njmschool.Models.DailyTask;
import com.wisewolf.njmschool.Models.FeedbackReplyModel;
import com.wisewolf.njmschool.Models.MCCQ;
import com.wisewolf.njmschool.Models.MCQSubmit;
import com.wisewolf.njmschool.Models.News;
import com.wisewolf.njmschool.Models.NotesMod;
import com.wisewolf.njmschool.Models.NoticeMod;
import com.wisewolf.njmschool.Models.QuizHead;
import com.wisewolf.njmschool.Models.QuizQuestion;
import com.wisewolf.njmschool.Models.Quotes;
import com.wisewolf.njmschool.Models.Response;
import com.wisewolf.njmschool.Models.SchoolDiff;
import com.wisewolf.njmschool.Models.TeacherDetails;
import com.wisewolf.njmschool.Models.VideoUp;
import java.util.List;
import java.util.concurrent.TimeUnit;

import kotlin.collections.CollectionsKt;
import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public class RetrofitClientInstance {
    private static Retrofit retrofit, retrofitOut;

    private static final String BASE_URL = "http://165.22.215.243/";

    public static Retrofit getRetrofitInstance() {
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.connectTimeout(20, TimeUnit.SECONDS);
        client.readTimeout(20, TimeUnit.SECONDS);
        client  .connectionSpecs(CollectionsKt.listOf(ConnectionSpec.MODERN_TLS, ConnectionSpec.CLEARTEXT));
        client.writeTimeout(20, TimeUnit.SECONDS);
        if (retrofit == null) {


            retrofit = new retrofit2.Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client.build())
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

        @POST("/newsData/")
        @FormUrlEncoded
        Call<List<News>> get_News(@Field("user_code") String user_code);

        @POST("/stuListSchool/")
        @FormUrlEncoded
        Call<List<SchoolDiff>> get_students(@Field("school_code") String school_code);

        @POST("/newsUpload/")
        @FormUrlEncoded
        Call<News> set_News(@Field("school_code") String school_code,
                            @Field("user_code") String user_code,
                            @Field("news") String news,
                            @Field("expiryDate") String expiryDate,
                            @Field("link") String link);

        @GET("/api/vimeo/videos/")
        Call<List<ClassVideo>> getVideos(@Query(value = "school", encoded = true) String school,
                                         @Query(value = "class", encoded = true) String user_class);

        @POST("/stuTaskData/")
        @FormUrlEncoded
        Call<List<DailyTask>> get_DailyTask(@Field("school_code") String school_code,
                                            @Field("clas") String clas,
                                            @Field("date") String dt);

        @POST("/notesData/")
        @FormUrlEncoded
        Call<List<TeacherDetails>> get_teacherDetails(@Field("school_code") String school_code,
                                                      @Field("clas") String clas);

        @POST("/feedBackData/")
        @FormUrlEncoded
        Call<List<FeedbackReplyModel>> get_feedbackReply(@Field("school_code") String school_code,
                                                         @Field("userid") String userid);

        @POST("/MCQResData/")
        @FormUrlEncoded
        Call<List<QuizHead>> get_QuizHead(@Field("school") String school_code,
                                          @Field("userid") String userid,
                                          @Field("clas") String clas,
                                          @Field("date") String date_id);

        @POST("/MCQ_QueData/")
        @FormUrlEncoded
        Call<QuizQuestion> get_QuizQuestions(@Field("school") String school_code,
                                             @Field("clas") String clas,
                                             @Field("title") String title);

        @POST("/MCQansData/")
        @FormUrlEncoded
        Call<MCCQ> saveMCQ_Answer(@Field("userid") String userid,
                                  @Field("clas") String clas,
                                  @Field("title") String title,
                                  @Field("subject") String subject,
                                  @Field("question") String question,
                                  @Field("answer") String answer,
                                  @Field("que_Image") String que_Image);

        @POST("/MCQcheckansData/")
        @FormUrlEncoded
        Call<MCQSubmit> saveMCQ_Result(@Field("userid") String userid,
                                       @Field("clas") String clas,
                                       @Field("title") String title,
                                       @Field("subject") String subject);

        @POST("/notesFList/")
        @FormUrlEncoded
        Call<List<NotesMod>> getNotes(@Field(value = "school", encoded = true) String school,
                                      @Field(value = "clas", encoded = true) String user_class);

        @POST("/noteiceFList/")
        @FormUrlEncoded
        Call<List<NoticeMod>> getNoticeImg  (@Field(value = "school", encoded = true) String school,
                                             @Field(value = "clas", encoded = true) String user_class);

        @POST("/videoFData/")
        @FormUrlEncoded
        Call<List<LiveMOd>> getLivestatus  (@Field(value = "school", encoded = true) String school,
                                           @Field(value = "clas", encoded = true) String user_class);




    }

}
