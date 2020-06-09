package com.wisewolf.njmschool.Models;

import com.google.gson.annotations.SerializedName;

public class ClassVideo{

	@SerializedName("data")
	private Data data;

	@SerializedName("id")
	private int id;

	@SerializedName("title")
	private String title;

	public Data getData(){
		return data;
	}

	public int getId(){
		return id;
	}

	public String getTitle(){
		return title;
	}
}