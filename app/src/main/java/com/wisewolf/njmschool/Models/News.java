package com.wisewolf.njmschool.Models;

import com.google.gson.annotations.SerializedName;

public class News{

	@SerializedName("news")
	private String news;

	@SerializedName("expiryDate")
	private String expiryDate;

	@SerializedName("school_code")
	private String schoolCode;

	@SerializedName("user_code")
	private String userCode;

	@SerializedName("modifyDate")
	private String modifyDate;

	@SerializedName("link")
	private String link;

	@SerializedName("id")
	private int id;

	@SerializedName("addDate")
	private String addDate;

	public String getNews(){
		return news;
	}

	public String getExpiryDate(){
		return expiryDate;
	}

	public String getSchoolCode(){
		return schoolCode;
	}

	public String getUserCode(){
		return userCode;
	}

	public String getModifyDate(){
		return modifyDate;
	}

	public String getLink(){
		return link;
	}

	public int getId(){
		return id;
	}

	public String getAddDate(){
		return addDate;
	}
}