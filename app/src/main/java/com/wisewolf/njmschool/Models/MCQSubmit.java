package com.wisewolf.njmschool.Models;

import com.google.gson.annotations.SerializedName;

public class MCQSubmit{

	@SerializedName("Total Question")
	private int totalQuestion;

	@SerializedName("Score")
	private int score;

	@SerializedName("status_code")
	private int statusCode;

	@SerializedName("success")
	private boolean success;

	@SerializedName("Wrong Answer")
	private int wrongAnswer;

	@SerializedName("userid")
	private String userid;

	public int getTotalQuestion(){
		return totalQuestion;
	}

	public int getScore(){
		return score;
	}

	public int getStatusCode(){
		return statusCode;
	}

	public boolean isSuccess(){
		return success;
	}

	public int getWrongAnswer(){
		return wrongAnswer;
	}

	public String getUserid(){
		return userid;
	}
}