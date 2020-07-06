package com.wisewolf.njmschool.Models;

import com.google.gson.annotations.SerializedName;

public class DailyTask{

	@SerializedName("date")
	private String date;

	@SerializedName("textbook")
	private String textbook;

	@SerializedName("school_code")
	private String schoolCode;

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

	@SerializedName("video")
	private String video;

	@SerializedName("Notes")
	private String notes;

	public String getDate(){
		return date;
	}

	public String getTextbook(){
		return textbook;
	}

	public String getSchoolCode(){
		return schoolCode;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public String getClas(){
		return clas;
	}

	public String getSubject(){
		return subject;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public int getId(){
		return id;
	}

	public String getVideo(){
		return video;
	}

	public String getNotes(){
		return notes;
	}
}