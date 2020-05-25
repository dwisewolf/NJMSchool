package com.wisewolf.njmschool.Models;

import com.google.gson.annotations.SerializedName;

public class Copyright{

	@SerializedName("year")
	private int year;

	@SerializedName("url")
	private String url;

	public int getYear(){
		return year;
	}

	public String getUrl(){
		return url;
	}
}