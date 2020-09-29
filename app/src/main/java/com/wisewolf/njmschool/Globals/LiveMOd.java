package com.wisewolf.njmschool.Globals;

import com.google.gson.annotations.SerializedName;

public class LiveMOd{

	@SerializedName("flag")
	private boolean flag;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("clas")
	private String clas;

	@SerializedName("school")
	private String school;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("id")
	private int id;

	public boolean isFlag(){
		return flag;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public String getClas(){
		return clas;
	}

	public String getSchool(){
		return school;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public int getId(){
		return id;
	}
}