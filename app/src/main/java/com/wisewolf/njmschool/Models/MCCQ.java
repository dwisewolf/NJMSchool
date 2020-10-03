package com.wisewolf.njmschool.Models;

import com.google.gson.annotations.SerializedName;

public class MCCQ{

    @SerializedName("question")
    private String question;

    @SerializedName("answer")
    private String answer;

    @SerializedName("updated_at")
    private String updatedAt;

    @SerializedName("clas")
    private String clas;

    @SerializedName("subject")
    private String subject;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("id")
    private int id;

    @SerializedName("title")
    private String title;

    @SerializedName("userid")
    private String userid;

    @SerializedName("marks")
    private String marks;

    @SerializedName("ans_Image")
    private String ans_Image;
}