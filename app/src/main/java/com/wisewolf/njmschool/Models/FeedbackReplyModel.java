package com.wisewolf.njmschool.Models;

import com.google.gson.annotations.SerializedName;

public class FeedbackReplyModel{

	@SerializedName("feedback")
	private String feedback;

	@SerializedName("school_code")
	private String schoolCode;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("id")
	private int id;

	@SerializedName("reply")
	private String reply;

	@SerializedName("userid")
	private String userid;

	public String getFeedback(){
		return feedback;
	}

	public String getSchoolCode(){
		return schoolCode;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public int getId(){
		return id;
	}

	public String getReply(){
		return reply;
	}

	public String getUserid(){
		return userid;
	}
}