package com.wisewolf.njmschool.Models;

import com.google.gson.annotations.SerializedName;

public class QuizHead{

	@SerializedName("date")
	private String date;

	@SerializedName("school")
	private String school;

	@SerializedName("clas")
	private String clas;

	@SerializedName("subject")
	private String subject;

	@SerializedName("update_at")
	private String updateAt;

	@SerializedName("description")
	private String description;

	@SerializedName("id")
	private int id;

	@SerializedName("time")
	private String time;

	@SerializedName("title")
	private String title;

	@SerializedName("create_at")
	private String createAt;

	public String getDate(){
		return date;
	}

	public String getSchool(){
		return school;
	}

	public String getClas(){
		return clas;
	}

	public String getSubject(){
		return subject;
	}

	public String getUpdateAt(){
		return updateAt;
	}

	public String getDescription(){
		return description;
	}

	public int getId(){
		return id;
	}

	public String getTime(){
		return time;
	}

	public String getTitle(){
		return title;
	}

	public String getCreateAt(){
		return createAt;
	}
}