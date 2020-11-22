package com.wisewolf.njmschool.Models;

import com.google.gson.annotations.SerializedName;

public class TeacherDetails{

	@SerializedName("school_code")
	private String schoolCode;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("clas")
	private String clas;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("id")
	private int id;

	@SerializedName("info")
	private String info;

	public String getSchoolCode(){
		return schoolCode;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public String getClas(){
		return clas;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public int getId(){
		return id;
	}

	public String getNotes(){
		return info;
	}
}