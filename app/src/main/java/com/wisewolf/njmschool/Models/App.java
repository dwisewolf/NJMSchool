package com.wisewolf.njmschool.Models;

import com.google.gson.annotations.SerializedName;

public class App{

	@SerializedName("name")
	private String name;

	@SerializedName("uri")
	private String uri;

	public String getName(){
		return name;
	}

	public String getUri(){
		return uri;
	}
}