package com.wisewolf.njmschool.Models;

import com.google.gson.annotations.SerializedName;

public class Feedback{

	@SerializedName("date")
	private String date;

	@SerializedName("msg")
	private String msg;

	@SerializedName("school")
	private String school;

	@SerializedName("user")
	private String user;

	public String getDate(){
		return date;
	}

	public String getMsg(){
		return msg;
	}

	public String getSchool(){
		return school;
	}

	public String getUser(){
		return user;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	public void setUser(String user) {
		this.user = user;
	}
}